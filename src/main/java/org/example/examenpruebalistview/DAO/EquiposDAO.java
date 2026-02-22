package org.example.examenpruebalistview.DAO;

import org.example.examenpruebalistview.model.Equipos;
import org.hibernate.Session;

import java.sql.SQLException;

public interface EquiposDAO {

    public Equipos buscarPorCodigo(String code) throws SQLException;

    void modificarEquipo(Session session, Equipos equipo);
}
