package com.organization.curator;

import java.util.concurrent.TimeUnit;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.RetryNTimes;

public class DistributedLock {
  private CuratorFramework client;
  private String path;

  public DistributedLock(String hosts, String path) {
    int sleepMsBetweenRetries = 100;
    int maxRetries = 3;
    this.path = path;
    RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);
    client = CuratorFrameworkFactory.newClient(hosts, retryPolicy);
    client.start();
  }

  public InterProcessLock acquire(String lockName) {
    try {
      InterProcessSemaphoreMutex sharedLock = new InterProcessSemaphoreMutex(client, path + "/" + lockName);

      if (!sharedLock.acquire(0, TimeUnit.SECONDS)) {
        return null;
      }

      return sharedLock;
    } catch (Exception e) {
      System.out.println("Error msg:");
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public boolean release(InterProcessLock sharedLock) {
    try {
      sharedLock.release();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  public void close() {
    client.close();
  }
}
