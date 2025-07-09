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
import java.util.PriorityQueue;
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

    //algoritmo de kruscal
    public static Grafo aplicarKruskal(Grafo grafoOriginal) {
        Grafo mst = new Grafo();
        double w = 0;

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
        str.append("Aristas seleccionadas para el MST (Kruskal):\n");
        for (Arista arista : aristas) {
            Vertice u = ((AristaPair) arista).origen;
            Vertice v = arista.getDestino();
            if (!uf.find(u).equals(uf.find(v))) {
                mst.agregarArista(u, v, arista.getPeso());
                uf.union(u, v);
                str.append("• ").append(u.getNombre())
                        .append(" — ").append(v.getNombre())
                        .append(" (").append(arista.getPeso()).append(" km)\n");
                w += arista.getPeso();
            }
        }

        str.append("\nPeso total: ").append(String.format("%.2f", w)).append(" KM");
        JOptionPane.showMessageDialog(null, str.toString(), "Resultado Kruskal", JOptionPane.INFORMATION_MESSAGE);

        return mst;
    }

    private static class AristaPair extends Arista {

        private final Vertice origen;

        public AristaPair(Vertice origen, Arista arista) {
            super(arista.getDestino(), arista.getPeso());
            this.origen = origen;
        }

        public Vertice getOrigen() {
            return this.origen;
        }
    }

    //algoritmo de boruvka
    public static Grafo aplicarBoruvka(Grafo grafoOriginal) {
        Grafo mst = new Grafo();
        grafoOriginal.getVertices().forEach(mst::agregarVertice);

        UnionFind uf = new UnionFind();
        uf.makeSet(grafoOriginal.getVertices());

        long numComponentes = grafoOriginal.getVertices().size();
        int fase = 1;
        double pesoTotal = 0;
        StringBuilder reporte = new StringBuilder("Progresión del Algoritmo de Borůvka:\n\n");

        while (numComponentes > 1) {
            Map<Vertice, AristaPair> aristasMasBaratas = new HashMap<>();

            for (Vertice origen : grafoOriginal.getVertices()) {
                for (Arista arista : grafoOriginal.getVecinos(origen)) {
                    Vertice destino = arista.getDestino();
                    Vertice raizOrigen = uf.find(origen);
                    Vertice raizDestino = uf.find(destino);

                    if (!raizOrigen.equals(raizDestino)) {
                        AristaPair aristaActual = aristasMasBaratas.get(raizOrigen);
                        if (aristaActual == null || arista.getPeso() < aristaActual.getPeso()) {
                            aristasMasBaratas.put(raizOrigen, new AristaPair(origen, arista));
                        }
                    }
                }
            }

            reporte.append("--- Fase ").append(fase).append(" ---\n");
            reporte.append("Número de componentes: ").append(numComponentes).append("\n");
            reporte.append("Aristas más baratas seleccionadas:\n");

            long componentesAntesDeUnion = contarComponentes(grafoOriginal.getVertices(), uf);
            if (aristasMasBaratas.isEmpty()) {
                break;
            }

            for (AristaPair aristaBarata : aristasMasBaratas.values()) {
                Vertice u = aristaBarata.getOrigen();
                Vertice v = aristaBarata.getDestino();

                if (!uf.find(u).equals(uf.find(v))) {
                    mst.agregarArista(u, v, aristaBarata.getPeso());
                    uf.union(u, v);
                    pesoTotal += aristaBarata.getPeso();
                    reporte.append("• ").append(u.getNombre()).append(" — ").append(v.getNombre())
                            .append(" (").append(aristaBarata.getPeso()).append(" km)\n");
                }
            }

            numComponentes = contarComponentes(grafoOriginal.getVertices(), uf);
            reporte.append("Componentes restantes: ").append(numComponentes).append("\n\n");

            if (numComponentes == componentesAntesDeUnion) {
                break;
            }
            fase++;
        }

        reporte.append("--- Resultado Final ---\n");
        reporte.append("Peso total del MST: ").append(String.format("%.2f", pesoTotal)).append(" KM");
        JOptionPane.showMessageDialog(null, reporte.toString(), "Resultado Borůvka", JOptionPane.INFORMATION_MESSAGE);

        return mst;
    }

    public static long contarComponentes(Set<Vertice> vertices, UnionFind uf) {
        return vertices.stream().map(uf::find).distinct().count();
    }

    //algoritmo de prim
    public static Grafo aplicarPrim(Grafo grafoOriginal, Vertice inicio) {
        Grafo mst = new Grafo();
        Set<Vertice> visitados = new HashSet<>();
        PriorityQueue<AristaPair> cola = new PriorityQueue<>(Comparator.comparingDouble(Arista::getPeso));
        double pesoTotal = 0;

        if (grafoOriginal.getVertices().isEmpty()) {
            return mst;
        }

        mst.agregarVertice(inicio);
        visitados.add(inicio);

        for (Arista arista : grafoOriginal.getVecinos(inicio)) {
            cola.add(new AristaPair(inicio, arista));
        }

        StringBuilder reporte = new StringBuilder("Aristas seleccionadas para el MST (Prim) desde " + inicio.getNombre() + ":\n");

        while (!cola.isEmpty() && visitados.size() < grafoOriginal.getVertices().size()) {
            AristaPair arista = cola.poll();
            Vertice origen = arista.getOrigen();
            Vertice destino = arista.getDestino();
            if (visitados.contains(destino)) {
                continue;
            }

            mst.agregarVertice(destino);
            mst.agregarArista(origen, destino, arista.getPeso());
            visitados.add(destino);
            pesoTotal += arista.getPeso();

            reporte.append("• ").append(origen.getNombre()).append(" — ").append(destino.getNombre())
                    .append(" (").append(arista.getPeso()).append(" km)\n");

            for (Arista siguiente : grafoOriginal.getVecinos(destino)) {
                if (!visitados.contains(siguiente.getDestino())) {
                    cola.add(new AristaPair(destino, siguiente));
                }
            }
        }

        reporte.append("\nPeso total: ").append(String.format("%.2f", pesoTotal)).append(" KM");
        JOptionPane.showMessageDialog(null, reporte.toString(), "Resultado Prim", JOptionPane.INFORMATION_MESSAGE);

        return mst;
    }
}
