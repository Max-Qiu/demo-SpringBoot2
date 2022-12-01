package com.maxqiu.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.demo.entity.Stock;
import org.apache.ibatis.annotations.Param;

/**
 * 库存 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * 减库存
     */
    void deduct(@Param("productId") int productId, @Param("warehouseId") int warehouseId, @Param("deductNum") int deductNum);
}
