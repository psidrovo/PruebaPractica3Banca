/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Paul Idrovo
 */
@Entity
@Table(name = "caso")
@NamedQueries({
    @NamedQuery(name = "Caso.findAll", query = "SELECT c FROM Caso c"),
    @NamedQuery(name = "Caso.findByCodigo", query = "SELECT c FROM Caso c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Caso.findByCapital", query = "SELECT c FROM Caso c WHERE c.capital = :capital"),
    @NamedQuery(name = "Caso.findByValorInteres", query = "SELECT c FROM Caso c WHERE c.valorInteres = :valorInteres"),
    @NamedQuery(name = "Caso.findByFechaInicio", query = "SELECT c FROM Caso c WHERE c.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Caso.findByFechaFinal", query = "SELECT c FROM Caso c WHERE c.fechaFinal = :fechaFinal"),
    @NamedQuery(name = "Caso.findByGaranteId", query = "SELECT c FROM Caso c WHERE c.garanteId = :garanteId")})
public class Caso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "capital")
    private double capital;
    @Basic(optional = false)
    @Column(name = "valor_interes")
    private double valorInteres;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @Column(name = "garante_id")
    private String garanteId;
    @JoinColumn(name = "banco_id", referencedColumnName = "banco_id")
    @ManyToOne(optional = false)
    private Banco bancoId;
    @JoinColumn(name = "vivienda_codigo_catastral", referencedColumnName = "codigo_catastral")
    @ManyToOne(optional = false)
    private Vivienda viviendaCodigoCatastral;

    public Caso() {
    }

    public Caso(Integer codigo) {
        this.codigo = codigo;
    }

    public Caso(Integer codigo, double capital, double valorInteres, Date fechaInicio, Date fechaFinal) {
        this.codigo = codigo;
        this.capital = capital;
        this.valorInteres = valorInteres;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(double valorInteres) {
        this.valorInteres = valorInteres;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getGaranteId() {
        return garanteId;
    }

    public void setGaranteId(String garanteId) {
        this.garanteId = garanteId;
    }

    public Banco getBancoId() {
        return bancoId;
    }

    public void setBancoId(Banco bancoId) {
        this.bancoId = bancoId;
    }

    public Vivienda getViviendaCodigoCatastral() {
        return viviendaCodigoCatastral;
    }

    public void setViviendaCodigoCatastral(Vivienda viviendaCodigoCatastral) {
        this.viviendaCodigoCatastral = viviendaCodigoCatastral;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Caso)) {
            return false;
        }
        Caso other = (Caso) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.ups.modelo.Caso[ codigo=" + codigo + " ]";
    }
    
}
