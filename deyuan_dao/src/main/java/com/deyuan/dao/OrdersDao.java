package com.deyuan.dao;

import com.deyuan.pojo.Member;
import com.deyuan.pojo.Orders;
import com.deyuan.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrdersDao {

    @Select("select * from orders")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(column = "orderNum",property = "orderNum"),
            @Result(column = "orderTime",property = "orderTime"),
            @Result(column = "orderStatus",property = "orderStatus"),
            @Result(column = "peopleCount",property = "peopleCount"),
            @Result(column = "payType",property = "payType"),
            @Result(column = "orderDesc",property = "orderDesc"),
            @Result(column = "PRODUCTID",property = "product",javaType = Product.class,one = @One(select = "com.deyuan.dao.ProductDao.findById"))
    })
    List<Orders> findAll() throws Exception;

    @Select("select * from orders where id=#{ordersId}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(column = "orderNum",property = "orderNum"),
            @Result(column = "orderTime",property = "orderTime"),
            @Result(column = "orderStatus",property = "orderStatus"),
            @Result(column = "peopleCount",property = "peopleCount"),
            @Result(column = "payType",property = "payType"),
            @Result(column = "orderDesc",property = "orderDesc"),
            @Result(column = "PRODUCTID",property = "product",javaType = Product.class,one = @One(select = "com.deyuan.dao.ProductDao.findById")),
            @Result(column = "MEMBERID",property = "member",javaType = Member.class,one = @One(select = "com.deyuan.dao.MemberDao.findById")),
            @Result(column = "id",property = "travellers",javaType = java.util.List.class,many = @Many(select = "com.deyuan.dao.TravellerDao.findByOrderId")),
    })

    Orders findByOrderId(String orderId) throws Exception;
}
