package com.organization.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.organization.curator.DistributedLock;
import java.io.IOException;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DistributedLockTest {

  String lockName = "Test/test3";
  DistributedLock distributedLock;
  String path = "/distributedLockTest";

  TestingServer zkServer;

  @Before
  public void setupThisLock() throws Exception {
    zkServer = new TestingServer(true);
    zkServer.start();
    distributedLock = new DistributedLock("localhost:" + zkServer.getPort(), path);
  }

  @After
  public void tearDownThisLock() throws IOException {
    zkServer.stop();
    distributedLock.close();
  }

  @Test
  public void trivialSingleLock() {
    assertThat(distributedLock.acquire(lockName)).isNotNull();
  }

  @Test
  public void cannotAcquireLock() {
    InterProcessLock firstMutex = distributedLock.acquire(lockName);

    // Should block
    InterProcessLock shouldBeBlockedByFirstMutex = distributedLock.acquire(lockName);
    assertThat(shouldBeBlockedByFirstMutex).isNull();
  }
}
