/**
 * 
 * This is an unpublished work containing 3M confidential and proprietary information. Disclosure or
 * reproduction without the written authorization of 3M is prohibited. If publication occurs, the
 * following notice applies:
 * 
 * Copyright (C) 2003-2017, 3M All rights reserved.
 * 
 */
package demo.pluto.maven.executor;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
 *用给定的计数 初始化 CountDownLatch。由于调用了 countDown() 方法，所以在当前计数到达零之前，await 方法会一直受阻塞。
 *之后，会释放所有等待的线程，await 的所有后续调用都将立即返回。这种现象只出现一次——计数无法被重置。如果需要重置计数，请考虑使用 CyclicBarrier。
 *CountDownLatch 是一个通用同步工具，它有很多用途。将计数 1 初始化的 CountDownLatch 用作一个简单的开/关锁存器，
 *或入口：在通过调用 countDown() 的线程打开入口前，所有调用 await 的线程都一直在入口处等待。
 *用 N 初始化的 CountDownLatch 可以使一个线程在 N 个线程完成某项操作之前一直等待，或者使其在某项操作完成 N 次之前一直等待。
 *CountDownLatch 的一个有用特性是，它不要求调用 countDown 方法的线程等到计数到达零时才继续，
 *而在所有线程都能通过之前，它只是阻止任何线程继续通过一个 await。
 * <br/> 
 * CountDownLatch最重要的方法是countDown()和await()，前者主要是倒数一次，后者是等待倒数到0，如果没有到达0，就只有阻塞等待了。
 * @author Pluto Xu a4yl9zz
 * 
 */
public class TestCountDownLatch {
    /**
     * 
     * 
     * @author Pluto Xu a4yl9zz
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // 开始的倒数锁
        final CountDownLatch begin = new CountDownLatch(1);
        // 结束的倒数锁
        final CountDownLatch end = new CountDownLatch(10);
        // 十名选手
        final ExecutorService exec = Executors.newFixedThreadPool(10);

        for (int index = 0; index < 10; index++) {
            final int NO = index + 1;
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        begin.await();// 一直阻塞
                        System.out.println(begin.getCount());
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("No." + NO + " arrived");
                    } catch (InterruptedException e) {
                    } finally {
                        System.out.println(end.getCount());
                        end.countDown(); //计数器倒扣一次
                    }
                }
            };
            exec.submit(run);
        }
        System.out.println("Game Start");
        begin.countDown(); //计数器倒扣一次
        end.await(); //阻塞，直至扣为0
        System.out.println("Game Over");
        exec.shutdown();
    }
}
