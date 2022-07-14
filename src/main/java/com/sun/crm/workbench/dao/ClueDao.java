package com.sun.crm.workbench.dao;


import com.sun.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
