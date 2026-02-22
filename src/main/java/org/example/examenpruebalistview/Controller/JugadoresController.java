package org.example.examenpruebalistview.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.examenpruebalistview.DAO.EquiposDAO;
import org.example.examenpruebalistview.DAO.EquiposDAOimpl;
import org.example.examenpruebalistview.DAO.JugadoresDAO;
import org.example.examenpruebalistview.DAO.JugadoresDAOimpl;
import org.example.examenpruebalistview.model.Ciudades;
import org.example.examenpruebalistview.model.Equipos;
import org.example.examenpruebalistview.model.Jugadores;
import org.example.examenpruebalistview.util.HibernateUtil;
import org.hibernate.Session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JugadoresController {

    @FXML private TextField txtCodigoMod, txtNombreMod, txtPresupuestoMod;
    @FXML private Button btnModificarMod, btnVolver;
    @FXML private ComboBox<Ciudades> cmbCiudad;
    @FXML private ListView<Jugadores> tableJugadores;

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private Equipos equipo;
    private JugadoresDAO jugadoresDAO = new JugadoresDAOimpl();
    private EquiposDAO equiposDAO = new EquiposDAOimpl();

    @FXML
    private void initialize(){
        cargaCiudades();
    }
    private void cargaJugadores(Equipos eq){
        try {
            List<Jugadores> jugadores = jugadoresDAO.buscarJugadoresPorEquipo(eq.getIdequipo());

            if (jugadores==null){
                jugadores = new ArrayList<>();
            }

            ObservableList<Jugadores> datos = FXCollections.observableList(jugadores);
            tableJugadores.setItems(datos);
        }catch (Exception e){
            e.printStackTrace();
            mostrarError("Error al cargar los jugadores en la lista");
        }
    }
    private void cargaCiudades(){
        cmbCiudad.getItems().clear();
        ArrayList<Ciudades> ciudades;
        try {
            ciudades = JSON_MAPPER.readValue(new File("src/main/resources/JSON/ciudades.json"),
                    JSON_MAPPER.getTypeFactory().constructCollectionType
                            (ArrayList.class, Ciudades.class));

            ObservableList<Ciudades> datos = FXCollections.observableArrayList(ciudades);
            cmbCiudad.setItems(datos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setEquipo(Equipos eq){
        this.equipo = eq;
        txtCodigoMod.setText(eq.getCodigo());
        txtNombreMod.setText(eq.getNombre());
        txtPresupuestoMod.setText(eq.getPresupuesto().toString());



        for (Ciudades ciudadObjeto : cmbCiudad.getItems()){
            if (ciudadObjeto.getNombreCiudad().equals(eq.getCiudad())){
                cmbCiudad.getSelectionModel().select(ciudadObjeto);
                break;
            }
        }

        txtCodigoMod.setDisable(true);
        txtNombreMod.setDisable(true);
        cargaJugadores(this.equipo);
    }

    @FXML
    private void modificarEquipo(){
        String presupuestoStr = txtPresupuestoMod.getText();
        Ciudades ciudadSeleccionada = cmbCiudad.getValue();

        if(presupuestoStr == null || presupuestoStr.isEmpty() || ciudadSeleccionada == null) {
            mostrarError("Por favor, completa todos los campos (Presupuesto y Ciudad).");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // 2. IMPORTANTE: En lugar de usar el objeto viejo, vamos a traer el fresco de la BD
            // Así evitamos problemas de objetos "desenganchados" (detached) de la sesión
            Equipos equipoParaActualizar = session.get(Equipos.class, this.equipo.getIdequipo());

            equipoParaActualizar.setPresupuesto(Integer.valueOf(presupuestoStr));
            equipoParaActualizar.setCiudad(ciudadSeleccionada.toString());
            equiposDAO.modificarEquipo(session, equipoParaActualizar);
            session.getTransaction().commit();

            mostrarInfo("Equipo Actualizado");

        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarError("Error al modificar el equipo.");
        }
    }

    @FXML
    public void cerrarPantalla(){
        // Solo cerramos esta ventana, la principal (que está detrás) volverá a ser visible
        Stage currentStage = (Stage) btnVolver.getScene().getWindow();
        currentStage.close();
    }

    // --- Métodos auxiliares para mostrar alertas ---
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }



    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
