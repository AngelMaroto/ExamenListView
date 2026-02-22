package org.example.examenpruebalistview.DAO;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.examenpruebalistview.Connection.MongoDBConnection;
import org.example.examenpruebalistview.model.Partidos;

public class PartidosMongoDAOimpl implements PartidosMongoDAO {
    @Override
    public void crearPartidoMongo(Partidos partido) {
        try {
            // CORRECCIÓN 1: Cambia el nombre de la colección a "Reparaciones"
            MongoCollection<Document> coleccion = MongoDBConnection.getDatabase().getCollection("Partidos");

            Document doc = new Document()
                    .append("idpartido", partido.getIdpartido())

                    // CORRECCIÓN 2: Guarda solo el ID del coche, no el objeto entero
                    // Si pones reparacion.getCoche() falla porque no sabe convertirlo
                    .append("idequipo", partido.getEquipo().getIdequipo())

                    .append("fechapartido", partido.getFechapartido().toString())
                    // Ojo: asegúrate de usar los nombres exactos que quieres en Mongo (con tilde o sin ella según tu script)
                    .append("rival", partido.getRival()) // En tu script Mongo pusiste "descripción" con tilde
                    .append("espectadores", partido.getEspectadores())
                    .append("jugado", partido.getJugado());

            coleccion.insertOne(doc);
            System.out.println("Guardado en MongoDB correctamente"); // Mensaje de control

        } catch (Exception e) {
            System.err.println("ERROR AL GUARDAR EN MONGO:");
            e.printStackTrace(); // ¡Esto te dirá por qué falla si vuelve a pasar!
        }
    }
}
