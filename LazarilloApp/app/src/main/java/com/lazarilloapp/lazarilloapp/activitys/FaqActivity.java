package com.lazarilloapp.lazarilloapp.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lazarilloapp.lazarilloapp.R;

/**
 * Activity de preguntas frecuentes
 * <p>
 * Created by Raúl
 */
public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int color_cabecera = -1;
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("alto_contraste", false)) {
            setTheme(R.style.AltoContraste);
            color_cabecera = R.color.primary_material_dark;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_faq));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String preguntas[] = {"¿Qué es Lazarillo?", "¿Cómo funciona?", "¿Cuánto cuesta?"};

        ListView faq = (ListView) findViewById(R.id.lv_faq);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, preguntas);

        faq.setAdapter(adaptador);

        faq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(FaqActivity.this);
                    dialogo.setTitle("¿Qué es Lazarillo?");
                    dialogo.setMessage("Lazarillo es Lorem ipsum dolor sit amet, possit verear phaedrum ius an," +
                            " ei facilis fuisset suscipiantur nec. Ea mel corrumpit voluptatum elaboraret. Qui aeque phaedrum ut. " +
                            "Enim inimicus patrioque te sit. Nec duis iisque at, an sed novum iudico adversarium.");
                    dialogo.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialogo.show();
                } else if (position == 1) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(FaqActivity.this);
                    dialogo.setTitle("¿Cómo funciona?");
                    dialogo.setMessage("Lazarillo funciona Lorem ipsum dolor sit amet, possit verear phaedrum ius an," +
                            " ei facilis fuisset suscipiantur nec. Ea mel corrumpit voluptatum elaboraret. Qui aeque phaedrum ut. " +
                            "Enim inimicus patrioque te sit. Nec duis iisque at, an sed novum iudico adversarium.");
                    dialogo.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialogo.show();
                } else if (position == 2) {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(FaqActivity.this);
                    dialogo.setTitle("¿Cuánto cuesta?");
                    dialogo.setMessage("Lazarillo es totalmente gratutito Lorem ipsum dolor sit amet, possit verear phaedrum ius an," +
                            " ei facilis fuisset suscipiantur nec. Ea mel corrumpit voluptatum elaboraret. Qui aeque phaedrum ut. " +
                            "Enim inimicus patrioque te sit. Nec duis iisque at, an sed novum iudico adversarium.");
                    dialogo.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialogo.show();
                }
            }
        });


        //esto queda hacerlo manual no asigna el tema bien automáticamente al toolbar y a las tabs
        //directamente con el setTheme de arriba.
        if (color_cabecera != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportActionBar().setBackgroundDrawable(getDrawable(color_cabecera));
            } else {
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(color_cabecera));

            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
