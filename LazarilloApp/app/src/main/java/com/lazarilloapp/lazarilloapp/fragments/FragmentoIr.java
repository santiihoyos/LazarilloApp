package com.lazarilloapp.lazarilloapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.lazarilloapp.lazarilloapp.R;
import com.lazarilloapp.lazarilloapp.logica.ManejadorDeDatos;
import com.lazarilloapp.lazarilloapp.logica.PeticionRuta;
import com.lazarilloapp.lazarilloapp.modelado.Lugar;

import java.util.ArrayList;

/**
 * Fragmento de inicio de la aplicacioón recibe los datos para la
 * Created by santiago on 12/01/16.
 */
public class FragmentoIr extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private ArrayList<Lugar> favoritos;
    private EditText origen, destino, llamanteSpeech;
    private Spinner fav_origen, fav_destino;
    private View view;

    /**
     * Método llamado por el View pager al obtener la vista de un fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        favoritos = ManejadorDeDatos.getInstance().leerLugaresFavoritos(getActivity());
        view = inflater.inflate(R.layout.layout_fragment_inicio, container, false);

        //RECUPERACION DE COMPONENTES
        origen = (EditText) view.findViewById(R.id.et_origen);
        destino = (EditText) view.findViewById(R.id.et_destino);
        fav_origen = (Spinner) view.findViewById(R.id.sp_origen);
        fav_destino = (Spinner) view.findViewById(R.id.sp_destino);
        //FIN DE LA RECUPERACION

        origen.setEnabled(false);
        destino.setEnabled(false);
        fav_origen.setEnabled(false);
        fav_destino.setEnabled(false);

        //ASIGNACION DE LISTENER A LOS COMPONENTES
        //((RadioGroup) view.findViewById(R.id.rg_posicionOrigen)).setOnCheckedChangeListener(this);
        view.findViewById(R.id.bt_navegar).setOnClickListener(this);
        RadioGroup rborigen=((RadioGroup) view.findViewById(R.id.rb_grupo_origen));
        rborigen.setOnCheckedChangeListener(this);
        ((RadioGroup) view.findViewById(R.id.rb_grupo_destino)).setOnCheckedChangeListener(this);
        //FIN DE LA ASIGNACION DE LISTENERS

        destino.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("dir_casa", "¡No se ha configurado!"));

        //comprobacion de alto contraste para el fondo de las cardView
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("alto_contraste", false)) {
            view.findViewById(R.id.card1).setBackgroundResource(R.color.cardsAltoContraste);
            view.findViewById(R.id.card2).setBackgroundResource(R.color.cardsAltoContraste);

        }
        //Comprobacion de las cardViews

        RadioButton actual= (RadioButton) view.findViewById(R.id.rb_desdeactual);

        onCheckedChanged(rborigen,actual.getId());

        return view;

    }


    /**
     * Según el radiogroup que llame se hace una separación para no deshabilitar componentes
     * por error. Una vez se sabe si se esta en origen o destino se inicializan los estados
     * de los componentes y según quien sea el radiobutton llamante se habilitaran de nuevo unos
     * componentes u otros.
     *
     * @param grupoLLamante
     * @param rbLLamante
     */
    @Override
    public void onCheckedChanged(RadioGroup grupoLLamante, int rbLLamante) {

        if (grupoLLamante.getId() == R.id.rb_grupo_origen) {

            view.findViewById(R.id.speechOrigen).setOnClickListener(null);
            origen.setEnabled(false);
            fav_origen.setEnabled(false);
            fav_origen.setVisibility(View.INVISIBLE);

            switch (rbLLamante) {
                case R.id.rb_desdeactual:
                    localizarAlObjetivo(origen);
                    break;
                case R.id.rb_desdefavortios:
                    fav_origen.setVisibility(View.VISIBLE);
                    fav_origen.setEnabled(true);
                    rellenarSpinner(fav_origen, origen);
                    locationManager=null;
                    break;
                case R.id.rb_desdeotra:
                    origen.setEnabled(true);
                    origen.setText("");
                    locationManager=null;
                    view.findViewById(R.id.speechOrigen).setOnClickListener(this);
                    break;
            }
        } else {

            view.findViewById(R.id.speechDestino).setOnClickListener(null);
            destino.setEnabled(false);
            fav_destino.setEnabled(false);
            fav_destino.setVisibility(View.INVISIBLE);

            switch (rbLLamante) {
                case R.id.rb_destinocasa:
                    destino.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("dir_casa", "¡No se ha configurado!"));
                    break;
                case R.id.rb_destinofavorito:
                    fav_destino.setEnabled(true);
                    fav_destino.setVisibility(View.VISIBLE);
                    rellenarSpinner(fav_destino, destino);
                    break;
                case R.id.rb_destinootro:
                    destino.setEnabled(true);
                    destino.setText("");
                    view.findViewById(R.id.speechDestino).setOnClickListener(this);
                    break;
            }
        }
    }

    /**
     * Control de todos los eventos click de la pestaña de "Ir a un lugar"
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_navegar:
                String origenStr=origen.getText().toString();
                String destinoStr=destino.getText().toString();
                String msg;
                if (origenStr==null || origenStr.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"¡Falta origen!",Toast.LENGTH_SHORT).show();
                }else if (destinoStr==null || destinoStr.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"¡Falta origen!",Toast.LENGTH_SHORT).show();
                }else {
                    new PeticionRuta(getActivity()).execute(origenStr, destinoStr, null);
                }
                break;
            case R.id.speechOrigen:
                llamanteSpeech = origen;
                reconocerVoz();
                break;
            case R.id.speechDestino:
                llamanteSpeech = destino;
                reconocerVoz();
                break;
        }
    }

    /**
     * Controla la respuesta por parte del activity de reconocimiento de voz.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String dir = speech.get(0);
                    if (dir != null) {
                        llamanteSpeech.setText(speech.get(0));
                    }
                }
                break;
        }
    }

    /**
     * Lanza el intent para el reconocimiento de la voz por parte de la api de Google para ello.
     * luego retorna el texto hacia atrás por el metodo onActivityResult.
     */
    private void reconocerVoz() {

        // Intent del Reconocimiento de Voz
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Configura el Lenguaje (Español-España)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");

        // Inicia la Actividad
        try {
            startActivityForResult(intentActionRecognizeSpeech, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(), "¡Opps! Tú dispositivo no soporta Speech to Text, deberas instalar Google Voice", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Rellena el spinner dado con la lista de favoritos.
     *
     * @param spinner
     */
    private void rellenarSpinner(Spinner spinner, final EditText destino_dir) {

        fav_destino.invalidate();
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, favoritos));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destino_dir.setText(((Lugar) parent.getSelectedItem()).getDireccion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Metodo para localzar al cliente en su posición actual.
     *
     * @param objetivo_texto
     */
    private void localizarAlObjetivo(EditText objetivo_texto) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(),"Persmiso OK",Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        } else {
            //si no hay permiso se pide
            Toast.makeText(getActivity().getApplicationContext(),"Persmiso KO",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }

        origen.setText("Ubicando...");
    }

    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            origen.setText(location.getLatitude()+"," + location.getLongitude());
        }

        //region noUsados
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {
        }
        //endregion
    };
}
