/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Paul Idrovo
 */
@Entity
@Table(name = "vivienda")
@NamedQueries({
    @NamedQuery(name = "Vivienda.findAll", query = "SELECT v FROM Vivienda v"),
    @NamedQuery(name = "Vivienda.findByCodigoCatastral", query = "SELECT v FROM Vivienda v WHERE v.codigoCatastral = :codigoCatastral"),
    @NamedQuery(name = "Vivienda.findByDireccion", query = "SELECT v FROM Vivienda v WHERE v.direccion = :direccion"),
    @NamedQuery(name = "Vivienda.findByEstado", query = "SELECT v FROM Vivienda v WHERE v.estado = :estado"),
    @NamedQuery(name = "Vivienda.findByAvaluo", query = "SELECT v FROM Vivienda v WHERE v.avaluo = :avaluo")})
public class Vivienda implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viviendaCodigoCatastral")
    private Collection<Caso> casoCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_catastral")
    private Integer codigoCatastral;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "avaluo")
    private double avaluo;
    @JoinColumn(name = "persona_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Persona personaCedula;

    public Vivienda() {
    }

    public Vivienda(Integer codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public Vivienda(Integer codigoCatastral, String direccion, String estado, double avaluo) {
        this.codigoCatastral = codigoCatastral;
        this.direccion = direccion;
        this.estado = estado;
        this.avaluo = avaluo;
    }

    public Integer getCodigoCatastral() {
        return codigoCatastral;
    }

    public void setCodigoCatastral(Integer codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(double avaluo) {
        this.avaluo = avaluo;
    }

    public Persona getPersonaCedula() {
        return personaCedula;
    }

    public void setPersonaCedula(Persona personaCedula) {
        this.personaCedula = personaCedula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoCatastral != null ? codigoCatastral.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vivienda)) {
            return false;
        }
        Vivienda other = (Vivienda) object;
        if ((this.codigoCatastral == null && other.codigoCatastral != null) || (this.codigoCatastral != null && !this.codigoCatastral.equals(other.codigoCatastral))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.ups.modelo.Vivienda[ codigoCatastral=" + codigoCatastral + " ]";
    }

    public Collection<Caso> getCasoCollection() {
        return casoCollection;
    }

    public void setCasoCollection(Collection<Caso> casoCollection) {
        this.casoCollection = casoCollection;
    }
    
}
