package me.t3sl4.httpproxy.Server;

import java.util.Queue;

public class PrinterHelper extends Thread {

    PrinterHelper() {
        start();
    }

    @Override
    public void run() {

        while (true) {
            synchronized (PrinterClass.lock) {
                try {
                    PrinterClass.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Queue<String> tmp = PrinterClass.handlerList.element();
            while (!tmp.isEmpty()) {
                System.out.println(tmp.remove());
            }
            synchronized (PrinterClass.removeLock) {
                PrinterClass.removeLock.notifyAll();
            }

        }

    }

}
