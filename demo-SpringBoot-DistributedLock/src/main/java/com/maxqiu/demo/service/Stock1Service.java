package com.maxqiu.demo.service;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.maxqiu.demo.pojo.StockBO;

/**
 * 库存（变量） 服务类
 *
 * @author Max_Qiu
 */
@Service
public class Stock1Service {
    private StockBO stock = new StockBO();

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 无锁模式，并发修改时最终库存不为0
     */
    public void normal() {
        stock.setStock(stock.getStock() - 1);
        System.out.println("库存余量：" + stock.getStock());
    }

    /**
     * 方法添加 synchronized 关键字，解决问题，但是并发降低
     */
    public synchronized void sync() {
        stock.setStock(stock.getStock() - 1);
        System.out.println("库存余量：" + stock.getStock());
    }

    /**
     * 同样，可以使用 ReentrantLock 锁解决问题，但是并发降低
     */
    public void lock() {
        lock.lock();
        try {
            stock.setStock(stock.getStock() - 1);
            System.out.println("库存余量：" + stock.getStock());
        } finally {
            lock.unlock();
        }
    }
}
