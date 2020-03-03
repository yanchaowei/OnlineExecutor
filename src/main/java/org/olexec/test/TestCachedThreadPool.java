package org.olexec.test;

import java.util.concurrent.*;

/**
 * @author ycw
 */
public class TestCachedThreadPool {
    public static void main(String[] args){
        //创建等待队列
        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(5);
        //创建线程池，池中保存的线程数为3，允许的最大线程数为5
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5,15,50,TimeUnit.MILLISECONDS,bqueue);
        //创建七个任务
        Runnable t1 = new MyThread();
        Runnable t2 = new MyThread();
        Runnable t3 = new MyThread();
        Runnable t4 = new MyThread();
        Runnable t5 = new MyThread();
        Runnable t6 = new MyThread();
        Runnable t7 = new MyThread();
        Runnable t8 = new MyThread();
        Runnable t9 = new MyThread();
        Runnable t10 = new MyThread();
        Runnable t11= new MyThread();
        Runnable t12 = new MyThread();
        Runnable t13 = new MyThread();
        Runnable t14 = new MyThread();
        //每个任务会在一个线程上执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t6);
        pool.execute(t7);
        pool.execute(t8);
        pool.execute(t9);
        pool.execute(t10);
        pool.execute(t11);
        pool.execute(t12);
        pool.execute(t13);
        pool.execute(t14);
        //关闭线程池
        pool.shutdown();
    }
}

class MyThread implements Runnable{
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
        try{
            Thread.sleep(100);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
