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
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

/**
 *
 * @author 52644
 */
public class MapaPanel extends JMapViewer{
    private final Grafo grafo;
    private final List<MapPolygon> lineas;

    public MapaPanel(Grafo grafo) {
        this.grafo = grafo;
        this.lineas = new ArrayList<>();
        setDisplayPosition(new Coordinate(22.278199, -101.1453780), 7); 
        setTileSource(new OsmTileSource.Mapnik());
        setZoomContolsVisible(true);
        dibujarGrafo();
        
        
    }

private void actualizarGrafo(Grafo g){
	getMapMarkerList().clear();
        lineas.forEach(this::removeMapPolygon);
        lineas.clear();

        for (Vertice nodo : g.getVertices()) {
            Coordinate coord = new Coordinate(nodo.getLatitud(), nodo.getLongitud());
            MapMarkerDot marker = new MapMarkerDot(nodo.getNombre(), coord);
            marker.setBackColor(Color.RED);
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
	private Grafo getGrafo(){
		return this.grafo;
	}
}
