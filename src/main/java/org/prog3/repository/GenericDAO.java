package org.prog3.repository;

import java.util.List;

public interface GenericDAO <Model>{
    List<Model> showAll(int page, int size);
    void delete(int id);
    Model findById( int id);
    List<Model> saveOrUpdate(List<Model> models);
}
