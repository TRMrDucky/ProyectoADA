/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algoritmos;

import Grafos.Grafo;
import Grafos.Vertice;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author 52644
 */
public class MapaCiudades {

    public static Grafo construirGrafo() {
        Grafo grafo = new Grafo();
        Map<String, Vertice> nodos = new HashMap<>();

        Map<String, Coordinate> coordenadas = Map.ofEntries(
                Map.entry("San Luis Potosí", new Coordinate(22.1498, -100.9792)),
                Map.entry("Soledad de Graciano Sánchez", new Coordinate(22.18912, -100.93792)),
                Map.entry("Ciudad Valles", new Coordinate(21.98333, -99.01667)),
                Map.entry("Matehuala", new Coordinate(23.65278, -100.64444)),
                Map.entry("Rio Verde", new Coordinate(21.92778, -99.99694)),
                Map.entry("Ciudad Fernández", new Coordinate(21.94045, -100.01153)),
                Map.entry("Ébano", new Coordinate(22.21808, -98.37706)),
                Map.entry("Tamazunchale", new Coordinate(21.26667, -98.78333))
        );

        for (Map.Entry<String, Coordinate> entry : coordenadas.entrySet()) {
            Vertice nodo = new Vertice(entry.getKey(), entry.getValue());
            grafo.agregarVertice(nodo);
            nodos.put(entry.getKey(), nodo);
        }

        agregarConexiones(grafo, nodos);

        return grafo;
    }

    private static void agregarConexiones(Grafo grafo, Map<String, Vertice> nodos) {
        grafo.agregarArista(nodos.get("San Luis Potosí"), nodos.get("Soledad de Graciano Sánchez"), 7.7);
        grafo.agregarArista(nodos.get("San Luis Potosí"), nodos.get("Matehuala"), 193);
        grafo.agregarArista(nodos.get("Matehuala"), nodos.get("Ciudad Fernández"), 90);
        grafo.agregarArista(nodos.get("San Luis Potosí"), nodos.get("Ciudad Fernández"), 50);
        grafo.agregarArista(nodos.get("Rio Verde"), nodos.get("San Luis Potosí"), 129);
        grafo.agregarArista(nodos.get("Rio Verde"), nodos.get("Ciudad Valles"), 123);
        grafo.agregarArista(nodos.get("Ciudad Valles"), nodos.get("Tamazunchale"), 188);
        grafo.agregarArista(nodos.get("Ébano"), nodos.get("Rio Verde"), 90);
        grafo.agregarArista(nodos.get("Ébano"), nodos.get("Ciudad Valles"), 200);
    }
}
