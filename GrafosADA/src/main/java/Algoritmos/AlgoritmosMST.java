package Algoritmos;

import Grafos.Grafo;
import Grafos.Vertice;
import java.util.HashMap;
import java.util.Map;
import org.openstreetmap.gui.jmapviewer.Coordinate;

public class AlgoritmosMST{

public static Grafo MstKruskal(Grafo grafo) {
    Grafo mst = new Grafo();
    Map<Vertice, Vertice> padre = new HashMap<>();

    for (Vertice v : grafo.getVertices()) {
        padre.put(v, v);
        mst.agregarVertice(v); // se agregan todos los vértices
    }

    Vertice encontrar(Vertice v) {
        if (!padre.get(v).equals(v)) {
            padre.put(v, encontrar(padre.get(v)));
        }
        return padre.get(v);
    }

    void unir(Vertice a, Vertice b) {
        Vertice raizA = encontrar(a);
        Vertice raizB = encontrar(b);
        padre.put(raizA, raizB);
    }

    List<Arista> aristas = new ArrayList<>();
    List<Vertice> origenes = new ArrayList<>();
    Set<String> vistas = new HashSet<>();

    for (Vertice origen : grafo.getVertices()) {
        for (Arista arista : grafo.getVecinos(origen)) {
            Vertice destino = arista.getDestino();
            String clave1 = origen.toString() + "-" + destino.toString();
            String clave2 = destino.toString() + "-" + origen.toString();
            if (!vistas.contains(clave1) && !vistas.contains(clave2)) {
                aristas.add(arista);
                origenes.add(origen);
                vistas.add(clave1);
            }
        }
    }

    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < aristas.size(); i++) indices.add(i);
    indices.sort(Comparator.comparingDouble(i -> aristas.get(i).getPeso()));

    for (int i : indices) {
        Vertice u = origenes.get(i);
        Vertice v = aristas.get(i).getDestino();

        if (!encontrar(u).equals(encontrar(v))) {
            mst.agregarArista(u, v, aristas.get(i).getPeso());
            unir(u, v);
        }
    }

    return mst;
}


}