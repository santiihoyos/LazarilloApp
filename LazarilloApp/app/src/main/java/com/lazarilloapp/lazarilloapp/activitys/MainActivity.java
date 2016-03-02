package com.lazarilloapp.lazarilloapp.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.lazarilloapp.lazarilloapp.R;
import com.lazarilloapp.lazarilloapp.adaptadores.AdapterPager;
import com.lazarilloapp.lazarilloapp.logica.ManejadorDeDatos;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int color_cabecera = -1;

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("alto_contraste", false)) {
            setTheme(R.style.AltoContraste);
            color_cabecera = R.color.primary_material_dark;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //INICIO DEL ANCLAJE DE LA TOOLBAR AL ACTIVITY COMO ACTIONBAR
        Toolbar barra = (Toolbar) findViewById(R.id.barra);
        barra.setTitle("Lazarillo");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barra.setTitleTextColor(getColor(R.color.color_title_toolbar));
        } else {
            barra.setTitleTextColor(getResources().getColor(R.color.color_title_toolbar));
        }

        setSupportActionBar(barra);
        //FIN DEL ANCLAJE DE LA  TOOLBAR


        //INICIO DE LA CONFIGURACION DE LAS TABS CON EL VIEWPAGER
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);


        pager.setAdapter(new AdapterPager(getFragmentManager()));
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        //FIN DE LA CONFIGURACION DE LA TABS.

        //esto queda hacerlo manual no asigna el tema bien automáticamente al toolbar y a las tabs
        //directamente con el setTheme de arriba.
        if (color_cabecera != -1) {
            tabs.setBackgroundResource(color_cabecera);
            barra.setBackgroundResource(color_cabecera);
            tabs.setTabTextColors(Color.WHITE, Color.WHITE);
        } else {
            tabs.setTabTextColors(Color.WHITE, Color.WHITE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ManejadorDeDatos.getInstance().leerIncidencias(MainActivity.this);
            }
        }).start();


    }

    //captura la respeusta de la peticion de permiso de acceso a la ubicacion
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 10) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    /**
     * Control sobre el click y porterior seleccionado de una de las opciones del menu.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.faq:
                startActivity(new Intent(getApplicationContext(), FaqActivity.class));
                break;
            case R.id.about_option:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
        }

        return true;
    }


    /**
     * Infla el menú del activity principal
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pricipal, menu);
        return true;
    }






}
