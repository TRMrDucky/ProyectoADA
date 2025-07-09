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
import java.util.StringJoiner;

/**
 *
 * @author Adolfo
 */
public class BellmanFord {

    public static Map<String, Object> BellmanFord(Grafo grafo, Vertice origen, Vertice destino) {
        Map<Vertice, Double> distancias = new HashMap<>();
        Map<Vertice, Vertice> predecesores = new HashMap<>();
        Map<String, Object> resultado = new HashMap<>();
        StringBuilder tablaIteraciones = new StringBuilder("Evolución de Distancias (Bellman-Ford):\n\n");

        for (Vertice v : grafo.getVertices()) {
            distancias.put(v, Double.MAX_VALUE);
            predecesores.put(v, null);
        }
        distancias.put(origen, 0.0);

        tablaIteraciones.append("Iteración 0 (Inicial):\n").append(distanciasToString(distancias)).append("\n\n");

        int numVertices = grafo.getVertices().size();
        for (int i = 1; i < numVertices; i++) {
            boolean cambio = false;
            for (Vertice u : grafo.getVertices()) {
                for (Arista arista : grafo.getVecinos(u)) {
                    Vertice v = arista.getDestino();
                    double peso = arista.getPeso();
                    if (distancias.get(u) != Double.MAX_VALUE && distancias.get(u) + peso < distancias.get(v)) {
                        distancias.put(v, distancias.get(u) + peso);
                        predecesores.put(v, u);
                        cambio = true;
                    }
                }
            }
            tablaIteraciones.append("Iteración ").append(i).append(":\n").append(distanciasToString(distancias)).append("\n\n");
            if (!cambio) {
                break;
            }
        }

        // Detección de ciclos negativos
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
        resultado.put("tablaIteraciones", tablaIteraciones.toString());

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
            resultado.put("ruta", new java.util.ArrayList<>());
        } else {
            resultado.put("ruta", ruta);
        }

        resultado.put("distancia", distancias.get(destino));

        return resultado;
    }

    private static String distanciasToString(Map<Vertice, Double> distancias) {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        distancias.forEach((key, value) -> {
            String dist = value == Double.MAX_VALUE ? "∞" : String.format("%.2f", value);
            sj.add(key.getNombre() + "=" + dist);
        });
        return sj.toString();
    }
}
