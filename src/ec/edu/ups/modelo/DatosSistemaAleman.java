/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.modelo;

/**
 *
 * @author Paul Idrovo
 */
public class DatosSistemaAleman {
    private int numeroCuota;
    private double capital;
    private double amortizacion;
    private double interesPeriodo;
    private double couta;

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(double amortizacion) {
        this.amortizacion = amortizacion;
    }

    public double getInteresPeriodo() {
        return interesPeriodo;
    }

    public void setInteresPeriodo(double interesPeriodo) {
        this.interesPeriodo = interesPeriodo;
    }

    public double getCouta() {
        return couta;
    }

    public void setCouta(double couta) {
        this.couta = couta;
    }
    
    public void cuotaCalcular(double interes){
        this.interesPeriodo = this.capital*interes/100;
        this.interesPeriodo = Math.round(interesPeriodo*100.0)/100.0;
        this.couta = this.amortizacion+this.interesPeriodo;
        this.couta = Math.round(couta*100.0)/100.0;
        this.capital = Math.round(capital*100.0)/100.0;
        this.amortizacion = Math.round(amortizacion*100.0)/100.0;
    }
}
