/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algoritmos;

import Grafos.Vertice;

/**
 *
 * @author Abraham Coronel Bringas
 */
public class NodoDistancia {

    public final Vertice nodo;
    public final Double distancia;

    /**
     * Crea una instancia de NodoDistancia
     * @param nodo      Vertice asociado
     * @param distancia Distancia acumulada desde el origen
     */
    public NodoDistancia(Vertice nodo, Double distancia) {
        this.nodo = nodo;
        this.distancia = distancia;
    }

}
