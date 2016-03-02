package com.lazarilloapp.lazarilloapp.adaptadores;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lazarilloapp.lazarilloapp.R;
import com.lazarilloapp.lazarilloapp.logica.ManejadorDeDatos;
import com.lazarilloapp.lazarilloapp.modelado.Lugar;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador tanto de la lista en la pestaña de "Favoritos", y se reutilizara en la lista de los sppiner
 * en la pestaña de "Ir a un lugar".
 * <p>
 * Created by Santiago on 17/01/16.
 */
public class AdapterListaFav<L> extends ArrayAdapter<Lugar> {

    private ArrayList<Lugar> favoritos;

    /**
     * Constructor del adaptador de la lista.
     *
     * @param context
     * @param resource
     * @param objects
     */
    public AdapterListaFav(Context context, int resource, List<Lugar> objects) {
        super(context, resource, objects);
        this.favoritos = (ArrayList<Lugar>) objects;
    }

    /**
     * Obtine la vista de cada item del spinner, se usa esto mismo para el getDropDownView(...);
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView; //<--- comprobación de para eficicencia (queda pendiente el holder)

        if (v == null) {
            v = View.inflate(getContext(), R.layout.layout_lista_favoritos, null);
        }

        ((TextView) v.findViewById(R.id.tv_nombre_fav_lista)).setText(getItem(position).getEtiqueta());
        ((TextView) v.findViewById(R.id.tv_dir_fav_lista)).setText(getItem(position).getDireccion());

        return v;
    }

    /**
     * LLama al método getView(...) para obtener la vista de despligue hacia abajo, en este caso igual
     * que la vista seleccionada.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /**
     * Redeficinion del método add original para que cada vez que se añade un Lugar se guarde
     * en memoria.
     *
     * @param object
     */
    @Override
    public void add(Lugar object) {
        super.add(object);
        ManejadorDeDatos.getInstance().escribirLugaresFavoritos(favoritos, getContext());
    }

    /**
     * Redeficinion del método add original para que cada vez que se borre un Lugar se guarde
     * en memoria.
     *
     * @param object
     */
    @Override
    public void remove(Lugar object) {
        super.remove(object);
        ManejadorDeDatos.getInstance().escribirLugaresFavoritos(favoritos, getContext());
    }
}
