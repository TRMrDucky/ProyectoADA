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
import javax.swing.JOptionPane;

public class AlgoritmosMST {

    private static class UnionFind {

        private final Map<Vertice, Vertice> padre = new HashMap<>();

        public void makeSet(Set<Vertice> vertices) {
            for (Vertice v : vertices) {
                padre.put(v, v);
            }
        }

        public Vertice find(Vertice v) {
            if (padre.get(v) != v) {
                padre.put(v, find(padre.get(v)));
            }
            return padre.get(v);
        }

        public void union(Vertice u, Vertice v) {
            Vertice raizU = find(u);
            Vertice raizV = find(v);
            if (!raizU.equals(raizV)) {
                padre.put(raizU, raizV);
            }
        }
    }

    public static Grafo aplicarKruskal(Grafo grafoOriginal) {
        Grafo mst = new Grafo();

        for (Vertice v : grafoOriginal.getVertices()) {
            mst.agregarVertice(v);
        }

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

        UnionFind uf = new UnionFind();
        uf.makeSet(grafoOriginal.getVertices());

        StringBuilder str = new StringBuilder("");
        str.append("Aristas seleccionadas para el MST:\n");
        for (Arista arista : aristas) {
            Vertice u = ((AristaPair) arista).origen;
            Vertice v = arista.getDestino();
            if (!uf.find(u).equals(uf.find(v))) {
                mst.agregarArista(u, v, arista.getPeso());
                uf.union(u, v);
                str.append("• ").append(u.getNombre())
                        .append(" — ").append(v.getNombre())
                        .append(" (").append(arista.getPeso()).append(" km)\n");
            }
        }

        JOptionPane.showMessageDialog(null, str.toString(), str.toString(), JOptionPane.INFORMATION_MESSAGE);

        return mst;
    }

    private static class AristaPair extends Arista {

        private final Vertice origen;

        public AristaPair(Vertice origen, Arista arista) {
            super(arista.getDestino(), arista.getPeso());
            this.origen = origen;
        }
    }

}
