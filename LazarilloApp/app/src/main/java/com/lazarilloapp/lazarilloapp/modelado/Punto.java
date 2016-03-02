/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lazarilloapp.lazarilloapp.modelado;

import java.io.Serializable;

/**
 * @author fede
 */
public class Punto implements Serializable {

    private double x;//longitud
    private double y;//latitud

    public Punto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Punto{" + x + ", " + y + '}';
    }

    public static double redondear(double numero, int decimales) {
        return Math.round(numero * Math.pow(10, decimales)) / Math.pow(10, decimales);
    }

    public static Punto maps_A_Punto(double longitud, double latitud) {
        return new Punto((redondear((longitud * 40077000 / 360), 2)), (redondear((latitud * 40009000 / 360), 2)));
    }

    public static Punto coordenadas_A_UTM(double longitud, double latitud) {
        Punto pun = null;
        double longitudRad = longitud * Math.PI / 180;
        //System.out.println("longitudRad = " + longitudRad);
        double latitudRad = latitud * Math.PI / 180;
        //System.out.println("latitudRad = " + latitudRad);

        int huso = (int) ((longitudRad / 6) + 31);
        //System.out.println("huso = " + huso);
        int meridianoCentral = huso * 6 - 183;
        //System.out.println("meridianoCentral = " + meridianoCentral);
        double meridianoCentralRad = longitudRad - meridianoCentral * Math.PI / 180;
        // System.out.println("meridianoCentralRad = " + meridianoCentralRad);

        double A = (Math.cos(latitudRad)) * (Math.sin(meridianoCentralRad));
        // System.out.println("A = " + A);
        double e1 = (Math.log((1 + A) / (1 - A))) * 1 / 2;
        //System.out.println("e1 = " + e1);
        double n = Math.atan(Math.tan(latitudRad) / Math.cos(meridianoCentralRad)) - latitudRad;
        //System.out.println("n = " + n);
        double semieje_M = 6378388.0;
        double semieje_m = 6356911.946130;
        double excentricidad_1 = Math.sqrt(semieje_M * semieje_M - semieje_m * semieje_m) / semieje_M;
        //System.out.println("excentricidad_1 = " + excentricidad_1);
        double excentricidad_2 = Math.sqrt(semieje_M * semieje_M - semieje_m * semieje_m) / semieje_m;
        //System.out.println("excentricidad_2 = " + excentricidad_2);
        double cuaExcentricidad_2 = excentricidad_2 * excentricidad_2;
        //System.out.println("cuaExcentricidad_2 = " + cuaExcentricidad_2);
        double radioPolar = semieje_M * semieje_M / semieje_m;
        //System.out.println("radioPolar = " + radioPolar);
        double aplanamiento = (semieje_M - semieje_m) / semieje_M;
        //System.out.println("aplanamiento = " + aplanamiento);
        //double v  = (radioPolar*0.9996)/Math.pow((1+cuaExcentricidad_2*Math.pow(Math.cos(latitudRad),2)),0.5);
        double v = (radioPolar * 0.999554435) / Math.pow((1 + cuaExcentricidad_2 * Math.pow(Math.cos(latitudRad), 2)), 0.5);
        //System.out.println("v = " + v);
        double E = (cuaExcentricidad_2 / 2) * (Math.pow(e1, 2) * Math.pow(Math.cos(latitudRad), 2));
        //System.out.println("E = " + E);
        double A1 = Math.sin(2 * latitudRad);
        //System.out.println("A1 = " + A1);
        double A2 = A1 * Math.pow(Math.cos(latitudRad), 2);
        //System.out.println("A2 = " + A2);
        double J2 = latitudRad + (A1 / 2);
        //System.out.println("J2 = " + J2);
        double J4 = ((3 * J2) + A2) / 4;
        //System.out.println("J4 = " + J4);
        double J6 = ((5 * J4) + (A2 * Math.pow(Math.cos(latitudRad), 2))) / 3;
        //System.out.println("J6 = " + J6);
        double alfa = 3 * cuaExcentricidad_2 / 4;
        //System.out.println("alfa = " + alfa);
        double beta = 5 * Math.pow(alfa, 2) / 3;
        //System.out.println("beta = " + beta);
        double gamma = 35 * Math.pow(alfa, 3) / 27;
        //System.out.println("gamma = " + gamma);
        double BETAS = 0.999582439 * radioPolar * (latitudRad - (alfa * J2) + (beta * J4) - (gamma * J6));
        //double BETAS = 0.9996*radioPolar*(latitudRad-(alfa*J2)+(beta*J4)-(gamma*J6));
        //System.out.println("BETAS = " + BETAS);
        double X = e1 * v * (1 + (E / 3)) + 500000;
        //System.out.println("X = " + X);
        double Y = (n * v * (1 + E)) + BETAS;
        //System.out.println("Y = " + Y);
        pun = new Punto(X, Y);


        return pun;
    }

}
