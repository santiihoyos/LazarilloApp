package com.lazarilloapp.lazarilloapp.logica;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lazarilloapp.lazarilloapp.activitys.MapsActivity;
import com.lazarilloapp.lazarilloapp.modelado.Punto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by santiago on 14/01/16.
 */
public class PeticionRuta extends AsyncTask<String, String, ArrayList<Punto>> {

    private ProgressDialog dialog;
    private Context contexto;

    public PeticionRuta(Context contexto) {

        dialog = new ProgressDialog(contexto);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.contexto = contexto;
    }

    @Override
    protected void onPreExecute() {
        dialog.show();
        dialog.setMessage("Trazando ruta...");
    }

    /**
     * Método que recibe los aprametros de busqueda de las rutas, origen y destino son obligatorios
     *
     * @param params En este orden: {origen, destino, waypoints}
     * @return
     */
    @Override
    protected ArrayList<Punto> doInBackground(String... params) {

        //VARIABLES
        HashMap<Integer, ArrayList<Punto>> rutas_totales = new HashMap<>();
        ArrayList<Punto>[] totalRutas = null;
        String incidencias_json, origen = null, destino = null, respuesta;
        int maximoIncidencias=100;
        int indexRutaFinal=0;
        ArrayList<Punto> incidencias = new ArrayList<>();

        try {

            //ININIO RECUPERACIÓN DE LAS INCIDENCIAS
            incidencias_json = ManejadorDeDatos.getInstance().leerIncidencias(contexto);
            JSONArray incidencias_jsa = new JSONObject(incidencias_json).getJSONArray("incidencias");

            for (int i = 0; i < incidencias_jsa.length(); i++) {
                double x = incidencias_jsa.getJSONObject(i).getDouble("x");
                double y = incidencias_jsa.getJSONObject(i).getDouble("y");
                incidencias.add(new Punto(y, x));
            }
            //FIN DE LA RECUPERAION DE INCIDENCIAS


            //INICIO PETICION DE RUTAS

            origen = params[0];
            origen = URLEncoder.encode(origen, "UTF-8");
            destino = params[1];
            destino = URLEncoder.encode(destino, "UTF-8");

            //Peticion por https en principio de forma directa, modo caminando, alternativas=si región españa
            String url_str = "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + origen + "&destination="
                    + destino
                    + "&region=es&mode=walking&alternatives=true";

            System.out.println(url_str);
            respuesta = jsonToString(new URL(url_str).openConnection().getInputStream());

            publishProgress("Recuperando rutas...");
            JSONObject jsonDatos = new JSONObject(respuesta);
            JSONArray rutas = jsonDatos.getJSONArray("routes");

            publishProgress("Rutas posibles " + rutas.length() + "...");


            totalRutas = new ArrayList[rutas.length()]; //posible null en caso de no encontrar rutas OJO

            for (int j = 0; j < rutas.length(); j++) {

                ArrayList<Punto> rutaPuntos = new ArrayList<>();
                System.out.println("EN RUTA NUMERO======= " + j);
                JSONObject ruta = rutas.getJSONObject(j);
                JSONArray legs = ruta.getJSONArray("legs");
                JSONObject leg0 = legs.getJSONObject(0);
                JSONArray steps = leg0.getJSONArray("steps");

                for (int i = 0; i < steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject punto_json = step.getJSONObject("start_location");
                    Punto punto = new Punto(punto_json.getDouble("lat"), punto_json.getDouble("lng"));
                    rutaPuntos.add(punto);
                }

                //último punto un poco especial es el segundo punto del ultimo que se corresponde con el destino
                JSONObject step = steps.getJSONObject(steps.length() - 1);
                JSONObject punto_json = step.getJSONObject("end_location");
                Punto punto = new Punto(punto_json.getDouble("lat"), punto_json.getDouble("lng"));
                rutaPuntos.add(punto);

                totalRutas[j] = rutaPuntos;

            }

            HashMap<Integer, Integer> incidencias_totales_por_ruta = CalculadorIncidencias.getInstance().escanearRuta(totalRutas, incidencias);

            for (int i = incidencias_totales_por_ruta.size()-1; i >=0 ; i--) {

                System.out.println("TOTAL INCIDENCIAS EN RUTA== "+i+"= "+incidencias_totales_por_ruta.get(i));
                if (incidencias_totales_por_ruta.get(i)<=maximoIncidencias){
                    maximoIncidencias=incidencias_totales_por_ruta.get(i);
                    indexRutaFinal=i;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("RUTA FINAL===" + indexRutaFinal + " para " + origen + " hasta " + destino);
        if (totalRutas==null || totalRutas.length==0){
            totalRutas=new ArrayList[1];
        }
        return totalRutas[indexRutaFinal];
    }

    @Override
    protected void onProgressUpdate(String... values) {
        dialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Punto> s) {

        dialog.dismiss();

        if (s==null || s.size() == 0) {
            Toast.makeText(contexto, "¡Sin rutas disponibles!", Toast.LENGTH_LONG).show();
        } else {
            Intent navegar = new Intent(contexto, MapsActivity.class);
            navegar.putExtra("puntos", s);
            contexto.startActivity(navegar);
        }
    }

    private String jsonToString(InputStream inputStream) throws IOException {
        String respu = "", linea;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((linea = br.readLine()) != null) {
            respu += linea;
        }
        return respu;
    }
}