package org.example.examenpruebalistview.DAO;

import org.example.examenpruebalistview.Connection.DBConnection;
import org.example.examenpruebalistview.model.Jugadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JugadoresDAOimpl implements JugadoresDAO{

    @Override
    public List<Jugadores> buscarJugadoresPorEquipo(int idEquipo) throws SQLException {
        List<Jugadores> lista = new ArrayList<>();

        String sql = "SELECT * FROM Jugadores WHERE idequipo = ?";
        try (Connection conn = DBConnection.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Jugadores j = new Jugadores();
                j.setIdjugador(rs.getInt("idjugador"));
                j.setNombre(rs.getString("nombre"));
                j.setPosicion(rs.getString("posicion"));
                lista.add(j);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }
}
