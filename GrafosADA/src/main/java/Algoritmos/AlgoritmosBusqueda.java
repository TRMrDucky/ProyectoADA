package Algoritmos;

import Grafos.Grafo;
import Grafos.Vertice;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<Vertice> BFS(Grafo grafo, Vertice origen) {
        //Guarda el orden de visita de cada vertice
        List<Vertice> ordenVisitas = new ArrayList<>();

        //Guarda los vertices ya visitados
        Map<Vertice, Boolean> visitado = new HashMap<>();

        //Pone todos los vertices como no visitados
        for (Vertice v : grafo.getVertices()) {
            visitado.put(v, false);
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
                }
            }
        }
        return ordenVisitas;
    }

    /**
     * Realiza un recorrido DFS (Depth-First Search) en el grafo desde un
     * vertice de origen.
     * @param grafo Grafo en el que se realizara la busqueda
     * @param origen Vertice de inicio del recorrido
     * @return Lista de vertices en el orden en que fueron visitados durante el DFS
     */
    public List<Vertice> DFS(Grafo grafo, Vertice origen) {
        //Guarda el orden de visita de cada vertice
        List<Vertice> ordenVisitas = new ArrayList<>();

        //Guarda los vertices ya visitados
        Map<Vertice, Boolean> visitado = new HashMap<>();

        //Pone todos los vertices como no visitados
        for (Vertice v : grafo.getVertices()) {
            visitado.put(v, false);
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
                    }
                }
            }
        }
        return ordenVisitas;
    }

}
