package com.maxqiu.demo.service;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Stock;
import com.maxqiu.demo.mapper.StockMapper;

/**
 * 库存（MySQL） 服务类
 *
 * @author Max_Qiu
 */
@Service
public class Stock2Service extends ServiceImpl<StockMapper, Stock> {
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 无锁模式，数据库中的库存最终不为0
     */
    public void normal() {
        LambdaQueryWrapper<Stock> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Stock::getProductId, 1);
        wrapper.eq(Stock::getWarehouseId, 1);
        Stock stock = getOne(wrapper);
        if (stock != null && stock.getNum() > 0) {
            stock.setNum(stock.getNum() - 1);
            updateById(stock);
        }
    }

    /**
     * 使用 ReentrantLock 锁解决问题
     */
    public void lock() {
        lock.lock();
        try {
            LambdaQueryWrapper<Stock> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Stock::getProductId, 1);
            wrapper.eq(Stock::getWarehouseId, 1);
            Stock stock = getOne(wrapper);
            if (stock != null && stock.getNum() > 0) {
                stock.setNum(stock.getNum() - 1);
                updateById(stock);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 添加事务后 ReentrantLock 失效，因为先释放锁，后提交事务
     */
    @Transactional(rollbackFor = Exception.class
    // 除非设置事务模式为读未提交（不过不会这么搞）
    // , isolation = Isolation.READ_UNCOMMITTED
    )
    public void transactional() {
        lock.lock();
        try {
            LambdaQueryWrapper<Stock> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Stock::getProductId, 1);
            wrapper.eq(Stock::getWarehouseId, 1);
            Stock stock = getOne(wrapper);
            if (stock != null && stock.getNum() > 0) {
                stock.setNum(stock.getNum() - 1);
                updateById(stock);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 使用一条 SQL 解决问题<br>
     * MySQL悲观锁使用行级锁要注意：<br>
     * 1. 锁的查询或者更新条件必须是索引字段<br>
     * 2. 查询或者更新条件必须是具体值
     */
    public void oneSql() {
        baseMapper.deduct(1, 1, 1);
    }

    /**
     * 使用 MySQL 悲观锁： select ... for update + 事务
     */
    @Transactional(rollbackFor = Exception.class)
    public void selectForUpdate() {
        // 查询库存并锁定
        LambdaQueryWrapper<Stock> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Stock::getProductId, 1);
        wrapper.eq(Stock::getWarehouseId, 1);
        wrapper.last("for update");
        Stock stock = getOne(wrapper);
        // 判断库存
        if (stock != null && stock.getNum() > 0) {
            stock.setNum(stock.getNum() - 1);
            // 扣减库存
            updateById(stock);
        }
    }

    /**
     * 乐观锁
     */
    public void version() {
        LambdaQueryWrapper<Stock> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Stock::getProductId, 1);
        queryWrapper.eq(Stock::getWarehouseId, 1);
        Stock stock = getOne(queryWrapper);
        if (stock != null && stock.getNum() > 0) {
            LambdaUpdateWrapper<Stock> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.set(Stock::getNum, stock.getNum() - 1);
            updateWrapper.set(Stock::getVersion, stock.getVersion() + 1);
            updateWrapper.eq(Stock::getId, stock.getId());
            updateWrapper.eq(Stock::getVersion, stock.getVersion());
            if (!update(updateWrapper)) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                version();
            }
        }
    }
}
