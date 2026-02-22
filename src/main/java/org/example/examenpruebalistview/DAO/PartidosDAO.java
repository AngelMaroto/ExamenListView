package org.example.examenpruebalistview.DAO;

import org.example.examenpruebalistview.model.Partidos;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public interface PartidosDAO {
    List<Partidos> obtenerPartidosPorEquipo(int idEquipo) throws SQLException;

    void crearPartido(Session session, Partidos partido);

    void borrarPartido(Session session, Partidos partido);

    void actualizarPartido(Session session, Partidos partido);
}
