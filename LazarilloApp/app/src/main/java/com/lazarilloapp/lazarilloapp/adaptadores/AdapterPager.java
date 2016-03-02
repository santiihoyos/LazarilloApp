package com.lazarilloapp.lazarilloapp.adaptadores;

import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.lazarilloapp.lazarilloapp.fragments.FragmentoConfiguracion;
import com.lazarilloapp.lazarilloapp.fragments.FragmentoFavoritos;
import com.lazarilloapp.lazarilloapp.fragments.FragmentoIr;


/**
 * Created by santiago on 12/01/16.
 */
public class AdapterPager extends FragmentStatePagerAdapter {

    private String[] nombres_tabs = {"Ir a un lugar", "Lugares favoritos", "Configuración"};

    public AdapterPager(android.app.FragmentManager fm) {
        super(fm);
    }


    @Override
    public android.app.Fragment getItem(int position) {

        Fragment fg = null;
        switch (position) {
            case 0:
                fg = new FragmentoIr();
                break;
            case 1:
                fg = new FragmentoFavoritos();
                break;
            case 2:
                fg = new FragmentoConfiguracion();
                break;
        }
        return fg;
    }

    @Override
    public int getCount() {
        return nombres_tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nombres_tabs[position];
    }

}
