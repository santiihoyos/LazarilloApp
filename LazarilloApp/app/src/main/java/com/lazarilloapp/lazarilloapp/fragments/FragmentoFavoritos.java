package com.lazarilloapp.lazarilloapp.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.lazarilloapp.lazarilloapp.R;
import com.lazarilloapp.lazarilloapp.adaptadores.AdapterListaFav;
import com.lazarilloapp.lazarilloapp.logica.ManejadorDeDatos;
import com.lazarilloapp.lazarilloapp.modelado.Lugar;

import java.util.ArrayList;

/**
 * Clase que extiende de Fragment del paquete android.app y se corresponde con la pestaña de "favoritos"
 * e implementa toda su funcionalidad.
 * Created by santiago on 20/01/2016.
 */
public class FragmentoFavoritos extends Fragment implements View.OnClickListener {

    private ArrayList<Lugar> favoritos;
    private ListView lista;

    /**
     * Creación de la vista para el fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_fragment_favoritos, container, false);

        favoritos = ManejadorDeDatos.getInstance().leerLugaresFavoritos(getActivity());
        lista = (ListView) v.findViewById(R.id.lista_favoritos);
        lista.setAdapter(new AdapterListaFav<Lugar>(getActivity(), 0, favoritos));


        //ASIGNACION DE LISTENERS
        v.findViewById(R.id.ftbn_aniadorFav).setOnClickListener(this);
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog confirmacion = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmación")
                        .setMessage("¿Está seguro que desea borrar esta dirección?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((AdapterListaFav<Lugar>) lista.getAdapter()).remove(favoritos.get(position));
                            }
                        })
                        .setNegativeButton("NO", null)
                        .create();
                confirmacion.show();
                return true;
            }
        });
        //FIN DE LA ASIGNACION

        return v;
    }

    /**
     * Implementa el lsitener del onClick de la clase View para todos los componentes que lo tienen
     * asignado en el fragmento de favoritos.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /**Cuando se lanza el evento de click soble el floating button se lanza un dialogo
             * preguntado nombre y dirección para añadir un nuevo favorito a la lista.
             */
            case R.id.ftbn_aniadorFav:
                final FloatingActionButton fab = (FloatingActionButton) v;
                fab.hide();
                final View contenido_dialog = View.inflate(getActivity(), R.layout.layout_dialogo_addfav, null);

                AlertDialog dialogo = new AlertDialog.Builder(getActivity())
                        .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //PROCESO DE AÑADIR EL FAVORITO:
                                ((ArrayAdapter<Lugar>) lista.getAdapter())
                                        .add(new Lugar(
                                                ((EditText) contenido_dialog.findViewById(R.id.et_direccion_dialogo_favorito)).getText().toString(),
                                                ((EditText) contenido_dialog.findViewById(R.id.et_nombre_dialogo_favorito)).getText().toString()));
                                //FIN DEL PROOCESO DE AÑADIR
                                fab.show();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fab.show();
                            }
                        })
                        .setView(contenido_dialog)
                        .create();
                dialogo.show();
                break;
        }
    }
}
