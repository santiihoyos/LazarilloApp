package com.lazarilloapp.lazarilloapp.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lazarilloapp.lazarilloapp.R;

/**
 * Fragment que controla la configuración de la aplicación para el modo normal.
 * Se hace uso de las caracteristicas de las preferences del api de android.
 * Created by Santiago on 18/01/2016.
 */
public class FragmentoConfiguracion extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asigna el layout de la preferencias definido en el xml al fragment
        addPreferencesFromResource(R.xml.configuraciones);

        Preference dir_casa = findPreference("dir_casa");
        Preference alto_contraste = findPreference("alto_contraste");
        Preference reset = findPreference("reset");

        dir_casa.setOnPreferenceChangeListener(this);
        alto_contraste.setOnPreferenceChangeListener(this);
        reset.setOnPreferenceClickListener(this);

        //restaurar valor del sumary
        dir_casa.setSummary(((EditTextPreference) dir_casa).getText());
    }

    /**
     * Listener para controlar los cambios en las preferencias de la aplicación
     * cada vez uqe cambien.
     *
     * @param preference
     * @param newValue
     * @return true
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference instanceof EditTextPreference) {
            preference.setSummary(newValue.toString());
        } else if (preference instanceof SwitchPreference) {

            getActivity().recreate();
            Toast.makeText(getActivity(), "¡Tema Cambiado!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        switch (key) {
            case "reset":
                AlertDialog confirmacion = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmación")
                        .setMessage("¿Está seguro que desea resetear la configuración?. Esto cerrará la aplicación.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = defaultSharedPreferences.edit();
                                editor.remove("dir_casa");
                                editor.remove("alto_contraste");
                                editor.commit();
                                getActivity().recreate();
                                Toast.makeText(getActivity(), "Configuración reseteada", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .create();
                confirmacion.show();
                break;
        }

        return true;
    }
}