/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lazarilloapp.lazarilloapp.modelado;

import com.lazarilloapp.lazarilloapp.modelado.Punto;

/**
 *
 * @author fede
 */
public class Tramo {
   private Punto p_inicial;
   private Punto p_final;

   public Tramo(){

   }

    public Tramo(Punto p_inicial, Punto p_final) {
        this.p_inicial = p_inicial;
        this.p_final = p_final;
    }

    public Punto getP_inicial() {
        return p_inicial;
    }

    public void setP_inicial(Punto p_inicial) {
        this.p_inicial = p_inicial;
    }

    public Punto getP_final() {
        return p_final;
    }

    public void setP_final(Punto p_final) {
        this.p_final = p_final;
    }

    @Override
    public String toString() {
        return "Tramo --> {" + "p_inicial=" + p_inicial + ", p_final=" + p_final + '}';
    }
    public static double distanciaPunto(Punto p_inicial,Punto p_final,Punto p_incidencia){

       double x1 = p_inicial.getX();
       double y1 = p_inicial.getY();
       double x2 = p_final.getX();
       double y2 = p_final.getY();
       double xi = p_incidencia.getX();
       double yi = p_incidencia.getY();

       Punto p=null;
    /*Recta que pasa por dos puntos p_inicial y p_final*/
//       double A = p.redondear(y2-y1,2);
       double A = (y2-y1);
//       double B = p.redondear((x2-x1),2);
       double B = (x2-x1);
//       double C = p.redondear(((x2-x1)*y1)-((y2-y1)*x1),2);
       double C = ((x2-x1)*y1)-((y2-y1)*x1);
//       System.out.println("Tramo p_inicial=" + p_inicial);
//       System.out.println("Tramo p_final= "+ p_final);
//       System.out.println("Incidencia= "+ p_incidencia);
//
//       System.out.println("A = y2-y1 = ("+y2+" - "+y1+") = "+p.redondear(A,2));
//       System.out.println("B = (x2-x1) = ("+x2+" - ("+x1+"))= "+p.redondear(B,2));
//       System.out.println("C = ((x2-x1)*y1)-((y2-y1)*x1) = "+p.redondear(C,2));
//       System.out.println("A*xi = "+A+" * " + xi +" = " + A*xi);
//       System.out.println("B*yi = " + B +" * " + yi + " = " +B*yi);

    /*Distancia de un punto a la recta. |((A*xi)+(B*yi)+C)|/*/
       double numerador =(Math.abs(p.redondear(((A*xi)-(B*yi)+C),2)));
//       System.out.println("Numerador = "+numerador);
       double denominador =(Math.sqrt((Math.pow(A,2))+(Math.pow(B,2))));
//       System.out.println("Denominador = "+denominador);
       double distancia = p.redondear(numerador/denominador,2);
//       System.out.println("Distancia = "+distancia);
//       System.out.println("Tramo p_inicial=" + p_inicial + ", p_final= "+ p_final + ", Incidencia= "+p_incidencia+" --> distancia = " + distancia);
        return distancia;
    }
}
