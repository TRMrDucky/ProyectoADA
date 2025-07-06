/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 52644
 */
public class Grafo {
    
    private final Map<Vertice, List<Arista>> adyacencias = new HashMap<>();

    public void agregarVertice(Vertice nodo) {
        adyacencias.putIfAbsent(nodo, new ArrayList<>());
    }

    public void agregarArista(Vertice origen, Vertice destino, double peso) {
        if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
            throw new IllegalArgumentException("Ambos nodos deben existir en el grafo");
        }
        adyacencias.get(origen).add(new Arista(destino, peso));
        adyacencias.get(destino).add(new Arista(origen, peso)); 
    }

    public Map<Vertice, List<Arista>> getAdyacencias() {
        return adyacencias;
    }

    public Set<Vertice> getVertices() {
        return adyacencias.keySet();
    }

    public List<Arista> getVecinos(Vertice nodo) {
        return adyacencias.getOrDefault(nodo, Collections.emptyList());
    }

    public void imprimirGrafo() {
        for (Vertice nodo : adyacencias.keySet()) {
            System.out.println(nodo + " conectado a:");
            for (Arista arista : adyacencias.get(nodo)) {
                System.out.println("   " + arista);
            }
        }
    }
}
