package org.prog3.repository;

import org.prog3.model.Order;

import java.util.List;

public interface GenericDAO <Model>{
    List<Model> showAll(int page, int size, Order order);
    void delete(int id);
    Model findById( int id);
    List<Model> saveOrUpdate(List<Model> models);
}
/*
*
*
*
*
*
* */