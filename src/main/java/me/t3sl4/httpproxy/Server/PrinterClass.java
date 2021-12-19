package me.t3sl4.httpproxy.Server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PrinterClass {

    static Queue<Queue<String>> handlerList = new LinkedBlockingQueue<Queue<String>>();

    final static PrinterHelper pH = new PrinterHelper();

    Queue<String> list;

    static Object lock = new Object();

    static Object removeLock = new Object();

    public PrinterClass() {
        list = new LinkedBlockingQueue<>();
        handlerList.add(list);
        synchronized (lock) {
            lock.notify();
        }
    }

    public void add(String str) {
        list.add(str);
        synchronized (lock) {
            lock.notify();
        }
    }

    public void removeThread() {
        while (!list.isEmpty()) {
            synchronized (removeLock) {
                try {
                    removeLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        handlerList.remove(list);
    }

}
