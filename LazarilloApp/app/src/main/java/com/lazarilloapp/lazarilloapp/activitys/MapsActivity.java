package com.lazarilloapp.lazarilloapp.activitys;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lazarilloapp.lazarilloapp.R;
import com.lazarilloapp.lazarilloapp.logica.ManejadorDeDatos;
import com.lazarilloapp.lazarilloapp.modelado.Punto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Punto> puntos;
    private ArrayList<Punto> incidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layu_navegacion);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        puntos = (ArrayList<Punto>) getIntent().getExtras().getSerializable("puntos");

        //solo apra demo
        incidencias = new ArrayList<>();

        //ININIO RECUPERACIÓN DE LAS INCIDENCIAS
        String incidencias_json = ManejadorDeDatos.getInstance().leerIncidencias(getApplicationContext());
        JSONArray incidencias_jsa = null;
        try {
            incidencias_jsa = new JSONObject(incidencias_json).getJSONArray("incidencias");

        for (int i = 0; i < incidencias_jsa.length(); i++) {
            double x = incidencias_jsa.getJSONObject(i).getDouble("x");
            double y = incidencias_jsa.getJSONObject(i).getDouble("y");
            incidencias.add(new Punto(y, x));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //FIN DE LA RECUPERAION DE INCIDENCIAS

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        int marca = 0;

        for (Punto incidencia :incidencias) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(incidencia.getX(), incidencia.getY())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }

        for (Punto punto : puntos) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(punto.getX(), punto.getY())).title("Step " + (++marca)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(puntos.get(0).getX(), puntos.get(0).getY())));

        UiSettings ui = mMap.getUiSettings();
        ui.setZoomControlsEnabled(true);


        for (int i = 0; i < puntos.size() - 1; i++) {
            PolylineOptions linea = new PolylineOptions().add(new LatLng(puntos.get(i).getX(), puntos.get(i).getY())).add(new LatLng(puntos.get(i + 1).getX(), puntos.get(i + 1).getY()));
            linea.color(Color.BLUE);
            linea.width(3f);
            mMap.addPolyline(linea);
        }
    }
}