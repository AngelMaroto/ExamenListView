package org.example.examenpruebalistview.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.examenpruebalistview.DAO.*;
import org.example.examenpruebalistview.model.Equipos;
import org.example.examenpruebalistview.model.Partidos;
import org.example.examenpruebalistview.util.HibernateUtil;
import org.hibernate.Session;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class EquiposController {

    @FXML private TextField txtCode, txtNombre, txtCiudad, txtPresupuesto, txtRival, txtEspectadores;
    @FXML private Button btnBuscar, btnModificar, btnVerPartidos, btnNuevo, btnBorrar, btnJugar, btnLimpiar;
    @FXML private TableView<Partidos> tablePartidos;
    @FXML private TableColumn<Partidos, Integer> colID;
    @FXML private TableColumn<Partidos, LocalDate> colFecha;
    @FXML private TableColumn<Partidos, String> colRival;
    @FXML private TableColumn<Partidos, String> colEspectadores;
    @FXML private TableColumn<Partidos, String> colJugado;
    @FXML private DatePicker dpFechaPartido;

    private Equipos equipo;
    EquiposDAO equiposDAO = new EquiposDAOimpl();
    PartidosDAO partidosDAO = new PartidosDAOimpl();
    PartidosMongoDAO partidosMongoDAO = new PartidosMongoDAOimpl();

    @FXML
    private void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("idpartido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechapartido"));
        colRival.setCellValueFactory(new PropertyValueFactory<>("rival"));
        colEspectadores.setCellValueFactory(new PropertyValueFactory<>("espectadores"));
        colJugado.setCellValueFactory(new PropertyValueFactory<>("jugado"));

        txtCode.setOnAction(e ->{
            try {
                buscarEquiposPorCodigo();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        tablePartidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Cargar datos en los campos de text
                dpFechaPartido.setValue(newSelection.getFechapartido());
                txtRival.setText(newSelection.getRival());
                txtEspectadores.setText(newSelection.getEspectadores().toString());
            }
        });
    }

    @FXML
    private void buscarEquiposPorCodigo() throws SQLException{
        String code = txtCode.getText();
        Equipos equipos = equiposDAO.buscarPorCodigo(code);

        if (equipos !=null){
            equipo = equipos;
            txtNombre.setText(equipo.getNombre());
            txtCiudad.setText(equipo.getCiudad());
            txtPresupuesto.setText(equipo.getPresupuesto().toString());

        } else {
            mostrarError("Equipo no encontrado con Codigo: "+code);
        }
    }

    @FXML
    private void verPartidos() throws SQLException{

        int idEquipo = equipo.getIdequipo();
        List<Partidos> partidos = partidosDAO.obtenerPartidosPorEquipo(idEquipo);

        ObservableList<Partidos> partidosFX = FXCollections.observableArrayList(partidos);
        tablePartidos.setItems(partidosFX);

        if (partidos.isEmpty()){
            mostrarWarning("El equipo no tiene partidos");
        }
    }

    @FXML
    private void limpiarCamposPartido(){
        txtEspectadores.clear();
        txtRival.clear();
        dpFechaPartido.setValue(null);
    }

    @FXML
    private void crearNuevoPartido(){
        if (equipo == null){
            mostrarError("Primero busca y selecciona el equipo");
        }

        LocalDate fecha = dpFechaPartido.getValue();
        String espectadores = txtEspectadores.getText();
        String rival = txtRival.getText();

        if (fecha == null || espectadores.isEmpty() || rival.isEmpty()){
            mostrarError("Completa todos los campos");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();

            Partidos nuevoPartido = new Partidos();
            nuevoPartido.setEquipo(this.equipo);
            nuevoPartido.setFechapartido(fecha);
            nuevoPartido.setRival(rival);
            nuevoPartido.setEspectadores(espectadores);
            nuevoPartido.setJugado("No");

            partidosDAO.crearPartido(session, nuevoPartido);
            session.getTransaction().commit();

            partidosMongoDAO.crearPartidoMongo(nuevoPartido);
        }catch (Exception ex){
            ex.printStackTrace();
            mostrarError("Error guardando con Hibernate");
            return;
        }
        actualizarPartidosTableView();
        limpiarCamposPartido();
        mostrarInfo("Partido creado exitosamente");
    }

    private void actualizarPartidosTableView(){
        try {
            List<Partidos> partidos = partidosDAO.obtenerPartidosPorEquipo(equipo.getIdequipo());
            tablePartidos.setItems(FXCollections.observableArrayList(partidos));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void borrarPartidoSeleccionado(){
        Partidos partido = tablePartidos.getSelectionModel().getSelectedItem();

        if (partido==null){
            mostrarError("Selecciona un partido para borrar");
            return;
        }

        if (Objects.equals(partido.getJugado(), "Si")){
            mostrarError("No se puede borrar ya que ya esta jugado");
            return;
        }
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            partidosDAO.borrarPartido(session, partido);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("error al borrar el partido");
            return;
        }
        actualizarPartidosTableView();
    }

    @FXML
    private void jugarPartido(){
        Partidos partidos = tablePartidos.getSelectionModel().getSelectedItem();

        if (partidos == null){
            mostrarError("Primero debes elegir un partido para poder jugarlo");
        }

        if ("Si".equalsIgnoreCase(partidos.getJugado())){
            mostrarWarning("Este partido ya esta jugado");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();

            partidos.setJugado("Si");

            partidosDAO.actualizarPartido(session, partidos);

            session.getTransaction().commit();

            tablePartidos.refresh();
            mostrarInfo("Partido jugado correctamente");
        }catch (Exception e){
            e.printStackTrace();
            mostrarError("Error al jugar el partido: "+e.getMessage());
        }
    }


    @FXML
    private void abrir(){abrirInterfazJugadores(equipo);}

    private void abrirInterfazJugadores(Equipos equip){

        if(equipo == null) {
            mostrarError("Busca un equipo primero.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/jugadores.fxml"));
            Parent root = loader.load();

            // Pasamos los datos al otro controlador
            JugadoresController controller = loader.getController();
            controller.setEquipo(this.equipo);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Usamos showAndWait() para que el código de ESTA ventana se pause aquí
            // hasta que el usuario cierre la ventana de Modificar
            stage.showAndWait();

            // Cuando el usuario cierra la ventana de modificar, el código continúa aquí.
            // ¡Este es el momento de refrescar los datos!
            txtCode.setText(this.equipo.getCodigo()); // Nos aseguramos de tener el código puesto
            buscarEquiposPorCodigo(); // Volvemos a lanzar la búsqueda para traer los datos frescos

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir la ventana de modificación.");
        }
    }

    // --- Métodos auxiliares para mostrar alertas ---
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void mostrarWarning(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
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
