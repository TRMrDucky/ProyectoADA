/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Grafos;

import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author 52644
 */
public class Vertice {
    private final String nombre;
    private final Coordinate coordenada;

    public Vertice(String nombre, Coordinate coordenada
    ) {
        this.nombre = nombre;
        this.coordenada = coordenada;
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public double getLatitud() {
        return coordenada.getLat();
    }

    public double getLongitud() {
        return coordenada.getLon();
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertice nodo = (Vertice) obj;
        return nombre.equals(nodo.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

}
