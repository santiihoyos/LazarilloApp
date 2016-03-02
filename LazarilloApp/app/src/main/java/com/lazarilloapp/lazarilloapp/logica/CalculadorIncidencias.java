package com.lazarilloapp.lazarilloapp.logica;

import com.lazarilloapp.lazarilloapp.modelado.Punto;
import com.lazarilloapp.lazarilloapp.modelado.Tramo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fede y Santi on 22/01/16.
 */
public class CalculadorIncidencias {

    private static final double margen = 10.0; //metros de margen
    private static CalculadorIncidencias ourInstance = new CalculadorIncidencias();

    private CalculadorIncidencias() {
    }

    public static CalculadorIncidencias getInstance() {
        return ourInstance;
    }

    /**
     * Método pripal que calcula el total de incidencias para cada ruta de las pasadas.
     *
     * @param rutas       Array de ArrayList<Punto> con cada una de las rutas a analizar.
     * @param incidencias ArrayList<Punto> con las incidencias.
     * @return HashMap<Integer,Integer> donde la clave es el número de ruta
     * y el valor el total de incidencias para esa ruta
     */
    public HashMap<Integer, Integer> escanearRuta(ArrayList<Punto>[] rutas, ArrayList<Punto> incidencias) {

        HashMap<Integer, Integer> rutasConIncidencias = new HashMap<>();
        ArrayList<Punto> incidenicasenUTM = arrayList_a_UTM(incidencias);

        for (int i = 0; i < rutas.length; i++) {
            ArrayList<Punto> puntosRuta_enUTM = arrayList_a_UTM(rutas[i]);
            int total = incidencia_a_Tramo(incidenicasenUTM, puntosRuta_enUTM);
            rutasConIncidencias.put(i, total);
        }

        System.out.println(rutasConIncidencias);
        return rutasConIncidencias;
    }


    /**
     * Metodo que convierte ArrayList de puntos geograficos a ArrayList de puntos en UTM
     *
     * @param ruta = ArrayList de partida.
     * @return devuelve arraylist de puntos en UTM.
     */
    private ArrayList<Punto> arrayList_a_UTM(ArrayList<Punto> ruta) {
        ArrayList<Punto> listaUTM = new ArrayList<>();
        for (Punto pto : ruta) {
            listaUTM.add(Punto.coordenadas_A_UTM(pto.getX(), pto.getY()));
        }
        return listaUTM;
    }


    /**
     * Metodo que calcula la distancia desde cada incidencia a cada tramo de la ruta.
     * y deveulve el total de incidencias en esa ruta.
     *
     * @param incidenciasUTM --> arraylist de puntos de incidencia.
     * @param rutaUTM        --> ruta de recorrido.
     */
    private int incidencia_a_Tramo(ArrayList<Punto> incidenciasUTM, ArrayList<Punto> rutaUTM) {
        Tramo t = null;
        Punto pto = null;
        int contador = 0;
        for(int j=0; j<incidenciasUTM.size();j++){
            System.out.println("\tIncidencia "+j+"--> " +incidenciasUTM.get(j));
            for(int k = 0; k < rutaUTM.size()-1; k++){
                double distancia = pto.redondear((t.distanciaPunto(rutaUTM.get(k),rutaUTM.get(k+1), incidenciasUTM.get(j))),2);
                if (distancia<margen || distancia ==0){
                    contador++;
                    System.out.println("\t\tdistancia a Tramo "+k+".- Desde "+rutaUTM.get(k)+" a "+ rutaUTM.get(k+1)+" --> "+distancia+" m.");
                }
            }
            System.out.println();
        }
        return contador;
    }


}
