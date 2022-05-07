package com.example.ufmanagerf.services.Pla;

import com.example.ufmanagerf.model.Curs;
import com.example.ufmanagerf.model.Pla;

import java.util.List;

public interface Service_Pla {

    public Pla get(int id);

    public List<Pla> getAll();

    public void add(Pla pla);

    public void remove(int id);

    public void edit(Pla pla);

/*
    public List<Uf> getAllWhereMpIsNull();

    public List<Uf> getAllWhereMpIsNullOrMpIsEquals(Mp mp);

    boolean exists(String nomUf);

    public boolean existsEdit(String nomUf, int idUf);
*/
}
