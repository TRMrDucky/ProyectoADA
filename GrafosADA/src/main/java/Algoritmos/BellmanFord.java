/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algoritmos;

import Grafos.Arista;
import Grafos.Grafo;
import Grafos.Vertice;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class BellmanFord {

    public static Map<String, Object> BellmanFord(Grafo grafo, Vertice origen, Vertice destino) {
        Map<Vertice, Double> distancias = new HashMap<>();
        Map<Vertice, Vertice> predecesores = new HashMap<>();
        Map<String, Object> resultado = new HashMap<>();

        for (Vertice v : grafo.getVertices()) {
            distancias.put(v, Double.MAX_VALUE);
            predecesores.put(v, null);
        }
        distancias.put(origen, 0.0);

        int numVertices = grafo.getVertices().size();
        for (int i = 1; i < numVertices; i++) {
            for (Vertice u : grafo.getVertices()) {
                for (Arista arista : grafo.getVecinos(u)) {
                    Vertice v = arista.getDestino();
                    double peso = arista.getPeso();
                    if (distancias.get(u) != Double.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                        distancias.put(v, distancias.get(u) + peso);
                        predecesores.put(v, u);
                    }
                }

            }
        }
        for (Vertice u : grafo.getVertices()) {
            for (Arista arista : grafo.getVecinos(u)) {
                Vertice v = arista.getDestino();
                double peso = arista.getPeso();
                if (distancias.get(u) != Double.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                    resultado.put("ciclo negativo", true);
                    resultado.put("distancia", Double.NEGATIVE_INFINITY);
                    resultado.put("ruta", new java.util.ArrayList<>());
                    return resultado;
                }
            }
        }
        resultado.put("ciclo negativo", false);

        List<Vertice> ruta = new java.util.ArrayList<>();
        Vertice paso = destino;
        if (predecesores.get(paso) != null || paso.equals(origen)) {
            while (paso != null) {
                ruta.add(paso);
                paso = predecesores.get(paso);

            }
        }
        Collections.reverse(ruta);
        if (ruta.isEmpty() || !ruta.get(0).equals(origen)) {
            resultado.put("ruta", new java.util.ArrayList<>());//aqui devuelve una ruta vacia
        } else {
            resultado.put("ruta", ruta);
        }

        resultado.put("distancia", distancias.get(destino));

        return resultado;
    }

}
