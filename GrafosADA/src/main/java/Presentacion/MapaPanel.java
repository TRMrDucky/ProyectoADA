/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Grafos.Arista;
import Grafos.Grafo;
import Grafos.Vertice;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

/**
 *
 * @author 52644
 */
public class MapaPanel extends JMapViewer {

    private final Grafo grafo;
    private final List<MapPolygon> lineas;
    private Vertice verticeSeleccionado = null;

    public MapaPanel(Grafo grafo) {
        this.grafo = grafo;
        this.lineas = new ArrayList<>();
        setDisplayPosition(new Coordinate(22.278199, -101.1453780), 7);
        setTileSource(new OsmTileSource.Mapnik());
        setZoomContolsVisible(true);
        dibujarGrafo();
        agregarListenerSeleccion();
    }

    private void agregarListenerSeleccion() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Convertir el punto del clic a coordenadas geograficas
                ICoordinate Icoord = getPosition(e.getPoint());
                Coordinate coord = new Coordinate(Icoord.getLat(), Icoord.getLon());
                // Buscar el vertice mas cercano
                verticeSeleccionado = encontrarVerticeCercano(coord);
                try {
                    // Volver a dibujar el grafo para resaltar la seleccion
                    actualizarGrafo(grafo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MapaPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private Vertice encontrarVerticeCercano(Coordinate coord) {
        double minDistancia = Double.MAX_VALUE;
        Vertice verticeCercano = null;
        double umbral = 0.01;

        for (Vertice v : grafo.getVertices()) {
            double distancia = distanciaCoord(coord, new Coordinate(v.getLatitud(), v.getLongitud()));
            if (distancia < umbral && distancia < minDistancia) {
                minDistancia = distancia;
                verticeCercano = v;
            }
        }
        return verticeCercano;
    }

    private double distanciaCoord(Coordinate c1, Coordinate c2) {
        double lat = c1.getLat() - c2.getLat();
        double lon = c1.getLon() - c2.getLon();
        return Math.sqrt(lat * lat + lon * lon);
    }

    public Vertice getVerticeSeleccionado() {
        return verticeSeleccionado;
    }

    public void actualizarGrafo(Grafo g) throws InterruptedException {
        getMapMarkerList().clear();
        lineas.forEach(this::removeMapPolygon);
        lineas.clear();

        for (Vertice nodo : g.getVertices()) {
            Coordinate coord = new Coordinate(nodo.getLatitud(), nodo.getLongitud());
            MapMarkerDot marker = new MapMarkerDot(nodo.getNombre(), coord);
            if (nodo.equals(verticeSeleccionado)) {
                marker.setBackColor(Color.GREEN);
            } else {
                marker.setBackColor(Color.RED);
            }
            marker.setColor(Color.WHITE);
            marker.setName(nodo.getNombre());
            addMapMarker(marker);
        }

        for (Vertice nodo : g.getVertices()) {
            Coordinate coordOrigen = new Coordinate(nodo.getLatitud(), nodo.getLongitud());

            for (Arista arista : g.getVecinos(nodo)) {
                Vertice destino = arista.getDestino();
                Coordinate coordDestino = new Coordinate(destino.getLatitud(), destino.getLongitud());

                List<Coordinate> puntos = new ArrayList<>();
                puntos.add(coordOrigen);
                puntos.add(coordDestino);
                puntos.add(coordDestino);

                MapPolygon linea = new CustomPolyline(puntos, Color.BLUE);
                addMapPolygon(linea);
                lineas.add(linea);

            }
        }
        repaint();
    }

    private void dibujarGrafo() {
        getMapMarkerList().clear();
        lineas.forEach(this::removeMapPolygon);
        lineas.clear();

        for (Vertice nodo : grafo.getVertices()) {
            Coordinate coord = new Coordinate(nodo.getLatitud(), nodo.getLongitud());
            MapMarkerDot marker = new MapMarkerDot(nodo.getNombre(), coord);
            marker.setBackColor(Color.RED);
            marker.setColor(Color.WHITE);
            marker.setName(nodo.getNombre());
            addMapMarker(marker);
        }

        for (Vertice nodo : grafo.getVertices()) {
            Coordinate coordOrigen = new Coordinate(nodo.getLatitud(), nodo.getLongitud());

            for (Arista arista : grafo.getVecinos(nodo)) {
                Vertice destino = arista.getDestino();
                Coordinate coordDestino = new Coordinate(destino.getLatitud(), destino.getLongitud());

                List<Coordinate> puntos = new ArrayList<>();
                puntos.add(coordOrigen);
                puntos.add(coordDestino);
                puntos.add(coordDestino);

                MapPolygon linea = new CustomPolyline(puntos, Color.BLUE);
                addMapPolygon(linea);
                lineas.add(linea);
            }
        }
        repaint();
    }

    public Grafo getGrafo() {
        return this.grafo;
    }

    private static class CustomPolyline extends MapPolygonImpl {

        private final Color color;
        private final Stroke stroke;

        public CustomPolyline(List<Coordinate> points, Color color) {
            super(points);
            this.color = color;
            this.stroke = new BasicStroke(4);
        }

        @Override
        public void paint(Graphics g, List<Point> points) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(stroke);

            if (points.size() >= 2) {
                Point prev = points.get(0);
                for (int i = 1; i < points.size(); i++) {
                    Point curr = points.get(i);
                    g2.drawLine(prev.x, prev.y, curr.x, curr.y);
                    prev = curr;
                }
            }
            g2.dispose();
        }
    }
   
}
