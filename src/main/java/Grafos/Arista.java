/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Grafos;

/**
 *
 * @author 52644
 */
public class Arista {
     private final Vertice destino;
    private final double peso; 

    public Arista(Vertice destino, double peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "→ " + destino.getNombre() + " (" + peso + ")";
    }
}
