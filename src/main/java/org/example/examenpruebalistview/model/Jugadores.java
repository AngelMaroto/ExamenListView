package org.example.examenpruebalistview.model;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "jugadores")
public class Jugadores implements Serializable{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idjugador")
    private int idjugador;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "posicion")
    private String posicion;

    @ManyToOne
    @JoinColumn(name = "idequipo", referencedColumnName = "idequipo")
    private Equipos equipo;

    public Jugadores() {
    }

    public Jugadores(int idjugador, String nombre, String posicion, Equipos equipo) {
        this.idjugador = idjugador;
        this.nombre = nombre;
        this.posicion = posicion;
        this.equipo = equipo;
    }

    public int getIdjugador() {
        return idjugador;
    }

    public void setIdjugador(int idjugador) {
        this.idjugador = idjugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Equipos getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipos equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return nombre + " ,posición: " + posicion ;
    }
}
