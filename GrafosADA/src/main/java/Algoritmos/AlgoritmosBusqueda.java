package Algoritmos;

import Grafos.Arista;
import Grafos.Grafo;
import Grafos.Vertice;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class AlgoritmosBusqueda {
//algoritmo BFS

    public Map<String, Object> BFS(Grafo grafo, Vertice origen) {
        Map<String, Object> resultado = new HashMap<>();
        StringBuilder recorridoPorNiveles = new StringBuilder();
        Map<Vertice, Boolean> visitado = new HashMap<>();
        Grafo grafoResultante = new Grafo();

        for (Vertice v : grafo.getVertices()) {
            visitado.put(v, false);
            grafoResultante.agregarVertice(v);
        }

        Queue<Vertice> cola = new ArrayDeque<>();
        visitado.put(origen, true);
        cola.offer(origen);

        int nivel = 0;
        while (!cola.isEmpty()) {
            int tamanoNivel = cola.size();
            recorridoPorNiveles.append("Nivel ").append(nivel).append(": ");
            StringJoiner sj = new StringJoiner(", ");

            for (int i = 0; i < tamanoNivel; i++) {
                Vertice actual = cola.poll();
                sj.add(actual.getNombre());

                for (var arista : grafo.getVecinos(actual)) {
                    Vertice vecino = arista.getDestino();
                    if (!visitado.get(vecino)) {
                        visitado.put(vecino, true);
                        cola.offer(vecino);
                        grafoResultante.agregarArista(actual, vecino, arista.getPeso());
                    }
                }
            }
            recorridoPorNiveles.append(sj.toString()).append("\n");
            nivel++;
        }

        resultado.put("grafo", grafoResultante);
        resultado.put("recorrido", recorridoPorNiveles.toString());
        return resultado;
    }

    /**
     * Prepara y lanza el recorrido DFS.
     *
     * @param grafo El grafo a recorrer.
     * @param origen El vértice de inicio.
     * @return Un mapa con el grafo del árbol DFS y el recorrido textual.
     */
    //ALGORITMO DFS
    public Map<String, Object> DFS(Grafo grafo, Vertice origen) {
        Map<String, Object> resultado = new HashMap<>();
        List<String> ordenVisitas = new ArrayList<>();
        Map<Vertice, Boolean> visitado = new HashMap<>();
        Grafo grafoResultante = new Grafo();

        for (Vertice v : grafo.getVertices()) {
            visitado.put(v, false);
            grafoResultante.agregarVertice(v);
        }

        // Llamada al método recursivo auxiliar
        dfsRecursivo(origen, grafo, visitado, ordenVisitas, grafoResultante);

        resultado.put("grafo", grafoResultante);
        resultado.put("recorrido", String.join(" -> ", ordenVisitas));
        return resultado;
    }

    /**
     * Método auxiliar recursivo para el recorrido DFS.
     */
    private void dfsRecursivo(Vertice actual, Grafo grafo, Map<Vertice, Boolean> visitado, List<String> ordenVisitas, Grafo grafoResultante) {
        visitado.put(actual, true);
        ordenVisitas.add(actual.getNombre());

        for (Arista arista : grafo.getVecinos(actual)) {
            Vertice vecino = arista.getDestino();
            if (!visitado.get(vecino)) {
                // La arista se agrega aquí, al explorar recursivamente
                grafoResultante.agregarArista(actual, vecino, arista.getPeso());
                dfsRecursivo(vecino, grafo, visitado, ordenVisitas, grafoResultante);
            }
        }
    }

    //ALGORITMO DE DIJKSTRA
    public Map<String, Object> Dijkstra(Grafo grafo, Vertice origen, Vertice destino) {
        Map<String, Object> resultado = new HashMap<>();
        Map<Vertice, Double> distancias = new HashMap<>();
        Map<Vertice, Vertice> predecesores = new HashMap<>();
        StringBuilder reporte = new StringBuilder("Evolución de distancias (Dijkstra):\n\n");
        Grafo arbolCaminos = new Grafo();

        for (Vertice v : grafo.getVertices()) {
            arbolCaminos.agregarVertice(v);
        }

        PriorityQueue<NodoDistancia> cola = inicializarEstructuras(grafo, origen, distancias, predecesores);

        reporte.append("Estado inicial: \n").append(distanciasToString(distancias)).append("\n");

        while (!cola.isEmpty()) {
            NodoDistancia actual = cola.poll();
            Vertice u = actual.nodo;

            if (actual.distancia > distancias.get(u)) {
                continue;
            }

            for (Arista arista : grafo.getVecinos(u)) {
                Vertice vecino = arista.getDestino();
                double nuevaDistancia = distancias.get(u) + arista.getPeso();

                if (nuevaDistancia < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDistancia);
                    predecesores.put(vecino, u);
                    cola.offer(new NodoDistancia(vecino, nuevaDistancia));

                    reporte.append("Paso: se relaja la arista (")
                            .append(u.getNombre()).append(" -> ").append(vecino.getNombre()).append(")\n")
                            .append(distanciasToString(distancias)).append("\n");
                }
            }
        }

        List<Vertice> ruta = new ArrayList<>();
        Vertice paso = destino;
        while (paso != null) {
            ruta.add(0, paso);
            paso = predecesores.get(paso);
        }

        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            for (int i = 0; i < ruta.size() - 1; i++) {
                Vertice u = ruta.get(i);
                Vertice v = ruta.get(i + 1);
                arbolCaminos.agregarArista(u, v, obtenerPesoArista(grafo, u, v));
            }
            reporte.append("\nRuta más corta encontrada: ")
                    .append(ruta.stream().map(Vertice::getNombre).collect(Collectors.joining(" -> ")))
                    .append("\nDistancia total: ").append(distancias.get(destino)).append(" km");
        } else {
            reporte.append("\nNo se encontró una ruta entre ").append(origen.getNombre()).append(" y ").append(destino.getNombre());
        }

        resultado.put("grafo", arbolCaminos);
        resultado.put("reporte", reporte.toString());
        return resultado;
    }

    private String distanciasToString(Map<Vertice, Double> distancias) {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        distancias.forEach((key, value) -> {
            String dist = value == Double.POSITIVE_INFINITY ? "∞" : String.format("%.2f", value);
            sj.add(key.getNombre() + "=" + dist);
        });
        return sj.toString();
    }

    private PriorityQueue<NodoDistancia> inicializarEstructuras(
            Grafo grafo, Vertice origen,
            Map<Vertice, Double> distancias,
            Map<Vertice, Vertice> predecesores) {

        //Crea la cola de prioridad ordenada por distancia de manera ascendente
        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>(
                Comparator.comparingDouble(n -> n.distancia)
        );

        //Inicializa todos los vertices en infinito
        for (Vertice v : grafo.getVertices()) {
            distancias.put(v, Double.POSITIVE_INFINITY);
            predecesores.put(v, null);
        }

        //Pone las distancias en 0
        distancias.put(origen, 0.0);
        cola.offer(new NodoDistancia(origen, 0.0));

        return cola;
    }

    private double obtenerPesoArista(Grafo grafo, Vertice origen, Vertice destino) {
        for (Arista arista : grafo.getVecinos(origen)) {
            if (arista.getDestino().equals(destino)) {
                return arista.getPeso();
            }
        }
        return 0.0;
    }
}
