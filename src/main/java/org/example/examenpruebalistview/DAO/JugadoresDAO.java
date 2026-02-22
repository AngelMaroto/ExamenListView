package org.example.examenpruebalistview.DAO;

import org.example.examenpruebalistview.model.Jugadores;

import java.sql.SQLException;
import java.util.List;

public interface JugadoresDAO {
    public List<Jugadores> buscarJugadoresPorEquipo(int idEquipo) throws SQLException;
}
