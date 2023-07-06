package com.maxqiu.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.model.MyPage;
import com.maxqiu.demo.model.ParamSome;

/**
 * 用户表 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> rowBoundList(RowBounds rowBounds, Map<String, String> name);

    List<User> selectMap(Map<String, Object> param);

    @Select("select * from smp_user ${ew.customSqlSegment}")
    List<User> selectByAtSelect(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    List<User> selectListByXml(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    Page<User> selectPageByXml(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

    List<User> myPage(Page<User> myPage);

    MyPage<User> myPageMap(Page<User> pg, Map<String, Object> map);

    /**
     * 3.x 的 page 可以进行取值,多个入参记得加上注解 自定义 page 类必须放在入参第一位 返回值可以用 IPage<T> 接收 也可以使用入参的 MyPage<T> 接收
     * <li>3.1.0 之前的版本使用注解会报错,写在 xml 里就没事</li>
     * <li>3.1.0 开始支持注解,但是返回值只支持 IPage ,不支持 IPage 的子类</li>
     *
     * @param myPage
     *            自定义 page
     * @return 分页数据
     */
    // @Select("select * from user where (age = #{pg.selectInt} and name = #{pg.selectStr}) or (age = #{ps.yihao} and
    // name = #{ps.erhao})")
    MyPage<User> mySelectPage(@Param("pg") MyPage<User> myPage, @Param("ps") ParamSome paramSome);
}
