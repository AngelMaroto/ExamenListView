package org.example.examenpruebalistview.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "partidos")
public class Partidos implements Serializable{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idpartido")
    private int idpartido;

    @Column(name = "fechapartido")
    private LocalDate fechapartido;

    @Column(name = "rival")
    private String rival;

    @Column(name = "espectadores")
    private String espectadores;

    @Column(name = "jugado")
    private String jugado;

    @ManyToOne
    @JoinColumn(name = "idequipo", referencedColumnName = "idequipo")
    private Equipos equipo;

    public Partidos() {
    }

    public Partidos(int idpartido, LocalDate fechapartido, String rival, String espectadores, String jugado, Equipos equipo) {
        this.idpartido = idpartido;
        this.fechapartido = fechapartido;
        this.rival = rival;
        this.espectadores = espectadores;
        this.jugado = jugado;
        this.equipo = equipo;
    }

    public int getIdpartido() {
        return idpartido;
    }

    public void setIdpartido(int idpartido) {
        this.idpartido = idpartido;
    }

    public LocalDate getFechapartido() {
        return fechapartido;
    }

    public void setFechapartido(LocalDate fechapartido) {
        this.fechapartido = fechapartido;
    }

    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public String getEspectadores() {
        return espectadores;
    }

    public void setEspectadores(String espectadores) {
        this.espectadores = espectadores;
    }

    public String getJugado() {
        return jugado;
    }

    public void setJugado(String jugado) {
        this.jugado = jugado;
    }

    public Equipos getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipos equipo) {
        this.equipo = equipo;
    }
}
