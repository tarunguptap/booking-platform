package com.organization.service;

import com.organization.curator.DistributedLock;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LockingExecutor<T> {
  private static final Logger logger = LoggerFactory.getLogger(LockingExecutor.class);
  private Executor executor;
  private DistributedLock distributedLock;

  public LockingExecutor(String hosts, String path, int executorCount) {
    executor = Executors.newFixedThreadPool(executorCount);
    distributedLock = new DistributedLock(hosts, path);
  }

  public void execute(List<String> lockNames, List<T> items, Consumer<T> consumer) {
    try {
      final List<CompletableFuture<Void>> completableFutures = new ArrayList<>();

      for (int i = 0; i < lockNames.size(); i++) {
        final int row = i;
        CompletableFuture<Void> future =
            CompletableFuture.runAsync(
                () -> {
                  T item = items.get(row);
                  final String lockName = lockNames.get(row);
                  InterProcessLock interProcessLock = null;
                  final StopWatch watch = new StopWatch();
                  watch.start();

                  try {
                    interProcessLock = distributedLock.acquire(lockName);

                    if (interProcessLock != null) {
                      logger.info("Acquired lockName:{} lockHash:{}", lockName, interProcessLock.hashCode());

                      // Call external method
                      consumer.accept(item);
                    } else {
                      logger.info("Already locked lockName:{}", lockName);
                    }
                  } finally {
                    if (interProcessLock != null) {
                      distributedLock.release(interProcessLock);
                      watch.stop();
                      logger.info("Released lockHash:{} elapsedMs:{}", interProcessLock.hashCode(), watch.getTotalTimeMillis());
                    }
                  }
                },
                executor);
        completableFutures.add(future);
      }

      // Block until all are done
      CompletableFuture<Void> completable = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
      completable.get();
    } catch (Exception e) {
      logger.error("Error msg:{}", e.getMessage(), e);
    }
  }
}
