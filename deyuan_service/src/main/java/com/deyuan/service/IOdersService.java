package com.deyuan.service;

import com.deyuan.pojo.Orders;

import java.util.List;

public interface IOdersService {

    List<Orders> findAll(int page,int size) throws Exception;

    Orders findById(String orderId) throws Exception;
}
