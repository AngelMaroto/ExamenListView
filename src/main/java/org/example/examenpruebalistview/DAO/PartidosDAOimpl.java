package org.example.examenpruebalistview.DAO;

import org.example.examenpruebalistview.Connection.DBConnection;
import org.example.examenpruebalistview.model.Equipos;
import org.example.examenpruebalistview.model.Partidos;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartidosDAOimpl implements PartidosDAO{
    @Override
    public List<Partidos> obtenerPartidosPorEquipo(int idEquipo) throws SQLException {
        List<Partidos> partidos = new ArrayList<>();

        String sql = "SELECT p.idpartido, e.idequipo, p.fechapartido, p.rival, p.espectadores, p.jugado " +
                "FROM Partidos p JOIN Equipos e ON p.idequipo = e.idequipo " +
                "WHERE e.idequipo = ?";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Partidos partido1 = new Partidos();
                partido1.setIdpartido(rs.getInt("idpartido"));
                java.sql.Date fechaSql = rs.getDate("fechapartido");
                if (fechaSql != null){
                    partido1.setFechapartido(fechaSql.toLocalDate());
                }
                partido1.setRival(rs.getString("rival"));
                partido1.setEspectadores(rs.getString("espectadores"));
                partido1.setJugado(rs.getString("jugado"));

                Equipos equipo = new Equipos();
                equipo.setIdequipo(rs.getInt("idequipo"));
                partido1.setEquipo(equipo);

                partidos.add(partido1); // Añadimos el partido a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidos;
    }

    @Override
    public void crearPartido(Session session, Partidos partido) {
        session.save(partido);
    }

    @Override
    public void borrarPartido(Session session, Partidos partido) {
        session.beginTransaction();
        session.delete(partido);
        session.getTransaction().commit();
    }

    @Override
    public void actualizarPartido(Session session, Partidos partido) {
        session.update(partido);
    }
}
