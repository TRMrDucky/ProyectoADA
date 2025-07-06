package Algoritmos;

import Grafos.Arista;
import Grafos.Grafo;
import Grafos.Vertice;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlgoritmosMST{

private static class UnionFind {
        private final Map<Vertice, Vertice> padre = new HashMap<>();

        public void makeSet(Set<Vertice> vertices) {
            for (Vertice v : vertices) {
                padre.put(v, v);
            }
        }

        public Vertice find(Vertice v) {
            if (padre.get(v) != v) {
                padre.put(v, find(padre.get(v))); // Compresión de camino
            }
            return padre.get(v);
        }

        public void union(Vertice u, Vertice v) {
            Vertice raizU = find(u);
            Vertice raizV = find(v);
            if (!raizU.equals(raizV)) {
                padre.put(raizU, raizV); // Unión simple
            }
        }
    }

    public static Grafo aplicarKruskal(Grafo grafoOriginal) {
        Grafo mst = new Grafo(); // Grafo resultante (MST)

        // Paso 1: Agregar todos los vértices al nuevo grafo MST
        for (Vertice v : grafoOriginal.getVertices()) {
            mst.agregarVertice(v);
        }

        // Paso 2: Obtener y ordenar todas las aristas por peso
        List<Arista> aristas = new ArrayList<>();
        Set<String> vistas = new HashSet<>();
        for (Vertice origen : grafoOriginal.getVertices()) {
            for (Arista arista : grafoOriginal.getVecinos(origen)) {
                String id = origen.getNombre() + "-" + arista.getDestino().getNombre();
                String idReverso = arista.getDestino().getNombre() + "-" + origen.getNombre();
                if (!vistas.contains(id) && !vistas.contains(idReverso)) {
                    aristas.add(new AristaPair(origen, arista));
                    vistas.add(id);
                }
            }
        }

        aristas.sort(Comparator.comparingDouble(Arista::getPeso));

        // Paso 3: Inicializar Union-Find
        UnionFind uf = new UnionFind();
        uf.makeSet(grafoOriginal.getVertices());

        // Paso 4: Construir MST
        for (Arista arista : aristas) {
            Vertice u = ((AristaPair) arista).origen;
            Vertice v = arista.getDestino();

            if (!uf.find(u).equals(uf.find(v))) {
                mst.agregarArista(u, v, arista.getPeso());
                uf.union(u, v);
            }
        }

        return mst;
    }

    // Clase interna para mantener referencia al origen
    private static class AristaPair extends Arista {
        private final Vertice origen;

        public AristaPair(Vertice origen, Arista arista) {
            super(arista.getDestino(), arista.getPeso());
            this.origen = origen;
        }
    }

}