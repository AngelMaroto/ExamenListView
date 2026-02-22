package org.example.examenpruebalistview.DAO;

import org.example.examenpruebalistview.Connection.DBConnection;
import org.example.examenpruebalistview.model.Equipos;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EquiposDAOimpl implements EquiposDAO{
    @Override
    public Equipos buscarPorCodigo(String code) throws SQLException {
        Equipos equipo = null;

        String sql = "SELECT * FROM Equipos WHERE codigo = ?";

        try (Connection conn = DBConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Sustituimos el parámetro ? por el CODIGO real
            ps.setString(1, code);

            // Ejecutamos la consulta
            ResultSet rs = ps.executeQuery();

            // Si existe un resultado, creamos un objeto Paciente con los datos
            if (rs.next()) {
                equipo = new Equipos();
                equipo.setIdequipo(rs.getInt("idequipo"));
                equipo.setNombre(rs.getString("nombre"));
                equipo.setCiudad(rs.getString("ciudad"));
                equipo.setPresupuesto(rs.getInt("presupuesto"));
                equipo.setCodigo(rs.getString("codigo"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si no se encontró, se devolverá null
        return equipo;
    }

    @Override
    public void modificarEquipo(Session session, Equipos equipo) {
        session.update(equipo);
    }
}
