package org.prog3.repository;

import java.util.List;

public interface GenericDAO <Model>{
    List<Model> showAll();
    void delete(int id);
    Model findById( int id);
    void createOrUpdate(Model newModel);
}
