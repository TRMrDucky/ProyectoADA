package Algoritmos;

import Grafos.Arista;
import Grafos.Grafo;
import Grafos.Vertice;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class AlgoritmosBusqueda {

    /**
     * Realiza un recorrido BFS (Breadth-First Search) en el grafo desde un
     * vertice de origen.
     *
     * @param grafo Grafo en el que se realizara la busqueda
     * @param origen Vertice de inicio del recorrido
     * @return Lista de vertices en el orden en que fueron visitados durante el
     * BFS
     */
    public Grafo BFS(Grafo grafo, Vertice origen) {
        //Guarda el orden de visita de cada vertice
        List<Vertice> ordenVisitas = new ArrayList<>();

        //Guarda los vertices ya visitados
        Map<Vertice, Boolean> visitado = new HashMap<>();

        // Grafo que representara el BFS
        Grafo grafoResultante = new Grafo();

        //Pone todos los vertices como no visitados
        for (Vertice v : grafo.getVertices()) {
            visitado.put(v, false);
            // Agregar todos los vértices al grafo resultante
            grafoResultante.agregarVertice(v);
        }

        //Cola para gestionar el orden de visita
        Queue<Vertice> cola = new ArrayDeque<>();
        //Marca y encola el vertice origen
        visitado.put(origen, true);
        cola.offer(origen);

        while (!cola.isEmpty()) {
            //Saca el siguiente vertice de la cola
            Vertice actual = cola.poll();
            ordenVisitas.add(actual);

            //Obtiene todos los vecinos del vertice actual
            for (var arista : grafo.getVecinos(actual)) {
                Vertice vecino = arista.getDestino();
                //Va marcando los vertices que no esten visitados
                if (!visitado.get(vecino)) {
                    visitado.put(vecino, true);
                    cola.offer(vecino);
                    grafoResultante.agregarArista(actual, vecino, arista.getPeso());
                }
            }
        }
        return grafoResultante;
    }

    /**
     * Realiza un recorrido DFS (Depth-First Search) en el grafo desde un
     * vertice de origen.
     *
     * @param grafo Grafo en el que se realizara la busqueda
     * @param origen Vertice de inicio del recorrido
     * @return Lista de vertices en el orden en que fueron visitados durante el
     * DFS
     */
    public Grafo DFS(Grafo grafo, Vertice origen) {
        //Guarda el orden de visita de cada vertice
        List<Vertice> ordenVisitas = new ArrayList<>();
        //Guarda los vertices ya visitados
        Map<Vertice, Boolean> visitado = new HashMap<>();
        // Grafo que representara el DFS
        Grafo grafoResultante = new Grafo();

        //Pone todos los vertices como no visitados
        for (Vertice v : grafo.getVertices()) {
            visitado.put(v, false);
            // Agregar todos los vertices al grafo resultante
            grafoResultante.agregarVertice(v);
        }

        //Pila para gestionar el orden de visita
        Deque<Vertice> pila = new ArrayDeque<>();
        //Apilar el vertice origen
        pila.push(origen);

        while (!pila.isEmpty()) {
            //Extraer el siguiente vertice de la pila
            Vertice actual = pila.pop();

            //Procesa los vertices no visitados
            if (!visitado.get(actual)) {
                visitado.put(actual, true);
                ordenVisitas.add(actual);

                //Agarra todos los vecinos del vertice actual
                for (var arista : grafo.getVecinos(actual)) {
                    Vertice vecino = arista.getDestino();
                    if (!visitado.get(vecino)) {
                        pila.push(vecino);
                        grafoResultante.agregarArista(actual, vecino, arista.getPeso());
                    }
                }
            }
        }
        return grafoResultante;
    }

    public Grafo Dijkstra(Grafo grafo, Vertice origen) {
        //Mapa para almacenar la distancia minima desde el origen a cada vertice
        Map<Vertice, Double> distancias = new HashMap<>();
        //Mapa para ir guardando el vertice previo al camino optimo
        Map<Vertice, Vertice> predecesores = new HashMap<>();
        //Grafo que representa el Dijkstra
        Grafo arbolCaminos = new Grafo();
        //Cola que ordena los vertices por distancia
        PriorityQueue<NodoDistancia> cola = inicializarEstructuras(grafo, origen, distancias, predecesores);

        for (Vertice v : grafo.getVertices()) {
            arbolCaminos.agregarVertice(v);
        }

        //Procesa la cola hasta dejarla vacia
        procesarCola(grafo, distancias, predecesores, cola);

        for (Vertice v : predecesores.keySet()) {
            Vertice predecesor = predecesores.get(v);
            if (predecesor != null) {
                // Buscar el peso de la arista en el grafo original
                double peso = obtenerPesoArista(grafo, predecesor, v);
                arbolCaminos.agregarArista(predecesor, v, peso);
            }
        }

        //Retorna los resultados de las distancias y sus predecesores
        return arbolCaminos;
    }

    /**
     * Inicializa todos los datos necesario para el algoritmo de Dijkstra.
     *
     * @param grafo Grafo de trabajo
     * @param origen Vertice de origen
     * @param distancias Mapa para almacenar distancias minimas
     * @param predecesores Mapa para almacenar predecesores en caminos
     * @return Cola de prioridad inicializada con el vertice origen
     */
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

    /**
     * Procesa la cola de prioridad del algoritmo de Dijkstra.
     *
     * @param grafo Grafo de trabajo
     * @param distancias Mapa de distancias mínimas
     * @param predecesores Mapa de predecesores
     * @param cola Cola de prioridad con nodos a procesar
     */
    private void procesarCola(
            Grafo grafo,
            Map<Vertice, Double> distancias,
            Map<Vertice, Vertice> predecesores,
            PriorityQueue<NodoDistancia> cola) {

        while (!cola.isEmpty()) {
            //Agarra el vertice con menor distancia acumulada
            NodoDistancia actual = cola.poll();
            Vertice u = actual.nodo;

            //Verifa si encuentra una mejor distancia
            if (actual.distancia > distancias.get(u)) {
                continue;
            }

            //Iterar sobre los vecinos
            for (Arista arista : grafo.getVecinos(u)) {
                procesarVecino(u, arista, distancias, predecesores, cola);
            }
        }
    }

    /**
     * Procesa un vecino del nodo actual en el algoritmo de Dijkstra.
     *
     * @param actual Nodo actual siendo procesado
     * @param arista Arista que conecta con el vecino
     * @param distancias Mapa de distancias mínimas
     * @param predecesores Mapa de predecesores
     * @param cola Cola de prioridad
     */
    private void procesarVecino(
            Vertice actual,
            Arista arista,
            Map<Vertice, Double> distancias,
            Map<Vertice, Vertice> predecesores,
            PriorityQueue<NodoDistancia> cola) {

        Vertice vecino = arista.getDestino();
        double distanciaActual = distancias.get(actual); //Distancia del vertice actual
        double nuevaDistancia = distanciaActual + arista.getPeso(); //Calcula la nueva distancia 
        double distanciaVecino = distancias.get(vecino); //Mejor distancia conocida del vecino

        //Comprueva si se encuentra un mejor camino
        if (nuevaDistancia < distanciaVecino) {
            actualizarDistancias(vecino, nuevaDistancia, actual, distancias, predecesores, cola);
        }
    }

    /**
     * Actualiza distancias y predecesores cuando se encuentra un camino mas
     * corto.
     *
     * @param vecino Vertice vecino a actualizar
     * @param nuevaDistancia Nueva distancia calculada
     * @param predecesor Predecesor en el nuevo camino
     * @param distancias Mapa de distancias
     * @param predecesores Mapa de predecesores
     * @param cola Cola de prioridad
     */
    private void actualizarDistancias(
            Vertice vecino,
            double nuevaDistancia,
            Vertice predecesor,
            Map<Vertice, Double> distancias,
            Map<Vertice, Vertice> predecesores,
            PriorityQueue<NodoDistancia> cola) {

        //Actualiza la distancia minima para el vertice
        distancias.put(vecino, nuevaDistancia);
        //Guarda un nuevo predecesor en el camino optimo
        predecesores.put(vecino, predecesor);
        //Inserta en la cola para el procesamiento
        cola.offer(new NodoDistancia(vecino, nuevaDistancia));
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
