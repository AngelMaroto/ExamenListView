package org.example.examenpruebalistview.util;


import org.example.examenpruebalistview.model.Equipos;
import org.example.examenpruebalistview.model.Jugadores;
import org.example.examenpruebalistview.model.Partidos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase de utilidad que configura y gestiona Hibernate.
 *
 * Hibernate es el framework ORM (Object Relational Mapping)
 * que se encarga de convertir los objetos Java (entidades)
 * en registros de base de datos y viceversa.
 *
 * Esta clase crea un "SessionFactory" (fábrica de sesiones),
 * que es el punto principal para obtener sesiones Hibernate.
 */
public class HibernateUtil {

    // --- Fábrica de sesiones (solo una para toda la aplicación) ---
    static SessionFactory factory = null;

    // Bloque estático: se ejecuta una única vez al cargar la clase
    static {
        // Creamos un objeto Configuration y cargamos el archivo hibernate.cfg.xml
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml"); // Archivo de configuración principal

        // Registramos las clases (entidades) que se van a mapear con las tablas
        cfg.addAnnotatedClass(Equipos.class);
        cfg.addAnnotatedClass(Partidos.class);
        cfg.addAnnotatedClass(Jugadores.class);

        // Creamos la fábrica de sesiones a partir de la configuración
        factory = cfg.buildSessionFactory();
    }

    /**
     * Devuelve la SessionFactory global.
     * La SessionFactory es costosa de crear, por eso solo debe existir una.
     */
    public static SessionFactory getSessionFactory() {
        return factory;
    }

    /**
     * Abre una nueva sesión de Hibernate.
     * Cada sesión representa una conexión individual a la base de datos.
     *
     * Es importante cerrarla cuando se termina de usar (try-with-resources).
     */
    public static Session getSession() {
        return factory.openSession();
    }
}
