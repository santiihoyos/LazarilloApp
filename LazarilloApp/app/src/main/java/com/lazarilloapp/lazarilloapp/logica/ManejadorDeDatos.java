package com.lazarilloapp.lazarilloapp.logica;

import android.content.Context;
import com.lazarilloapp.lazarilloapp.modelado.Lugar;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Siguiendo el patrón Singleton se implementa una clase de
 * instancia única para escribir y leer el arraylist de favoritos que esta serializado en memoría.
 * Created by santiago on 20/01/16.
 */
public class ManejadorDeDatos {

    private static ManejadorDeDatos laInstancia = new ManejadorDeDatos();

    public static ManejadorDeDatos getInstance() {
        return laInstancia;
    }

    private ManejadorDeDatos() {
    }

    public ArrayList<Lugar> leerLugaresFavoritos(Context contexto) {

        File fichero = new File(contexto.getFilesDir(), "data1.obj");

        if (!fichero.exists()) {
            try {
                FileOutputStream favoritos99 = contexto.openFileOutput("data1.obj", contexto.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(favoritos99));
                oos.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        ArrayList<Lugar> favoritos = new ArrayList<>();

        try {
            FileInputStream favoritosF = contexto.openFileInput("data1.obj");
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(favoritosF));
            favoritos = (ArrayList<Lugar>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("ERROR AL LEER EL FICHERO DE FAVORITOS");
            e.printStackTrace();
        }
        return favoritos;
    }

    /**
     * Escribe los datos en el fichero data1.obj dentro del alamacenamiento local de la app.
     *
     * @param listaFavoritos ArrayList de lugares con los favoritos.
     * @param contexto
     */
    public void escribirLugaresFavoritos(ArrayList<Lugar> listaFavoritos, Context contexto) {

        try {
            FileOutputStream salidaFavo = contexto.openFileOutput("data1.obj", contexto.MODE_PRIVATE);
            ObjectOutputStream oosN = new ObjectOutputStream(new BufferedOutputStream(salidaFavo));
            oosN.writeObject(listaFavoritos);
            oosN.close();
            salidaFavo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String leerIncidencias(Context contexto) {

        String salida = "";

        try {
            //obtenemos al ruta de los ficheros de la app
            File incidencias = new File(contexto.getFilesDir(), "incidencias.json");

            if (!incidencias.exists()) {

                //llama a refrescar para que se conecte a interent descargue las incidencias
                //y las escriba en un .json
                salida=refrescarIncidencias(contexto).toString();
            } else {

                BufferedReader br = new BufferedReader(new FileReader(incidencias));
                String linea;
                while ((linea = br.readLine()) != null) {
                    salida += linea;
                }
                JSONObject nodoprinciapl = new JSONObject(salida);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                Date fecha_actual = sdf.parse(sdf.format(new Date())); //<-- me cargo los milis
                Date fecha_fichero = sdf.parse(nodoprinciapl.getString("fecha"));

                //si la fecha del fichero almacenado no es de hoy se descarga para actualizar
                if (fecha_fichero.before(fecha_actual)) {
                    salida=refrescarIncidencias(contexto).toString();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //lo que no quieres que se haga en priemr plano
                }
            }).start();
        }
        return salida;
    }


    private JSONObject refrescarIncidencias(final Context contexto) {

        final JSONObject[] respu = {null};

        Thread hilo =new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String json_respu = null;
                    File incidencias = new File(contexto.getFilesDir(), "incidencias.json");

                    String url_ruta = "http://51.254.134.174/lazarillodata/incidencias-valladolid.json";
                    json_respu = Jsoup.connect(url_ruta).get().text();

                    incidencias.createNewFile();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(incidencias));
                    bw.write(json_respu);
                    bw.close();

                    respu[0] = new JSONObject(json_respu);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        hilo.start();

        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return respu[0];
    }


}
