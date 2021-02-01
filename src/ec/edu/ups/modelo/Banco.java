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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Paul Idrovo
 */
@Entity
@Table(name = "banco")
@NamedQueries({
    @NamedQuery(name = "Banco.findAll", query = "SELECT b FROM Banco b"),
    @NamedQuery(name = "Banco.findByBancoId", query = "SELECT b FROM Banco b WHERE b.bancoId = :bancoId"),
    @NamedQuery(name = "Banco.findByNombre", query = "SELECT b FROM Banco b WHERE b.nombre = :nombre"),
    @NamedQuery(name = "Banco.findByUsuario", query = "SELECT b FROM Banco b WHERE b.usuario = :usuario"),
    @NamedQuery(name = "Banco.findByPassword", query = "SELECT b FROM Banco b WHERE b.password = :password")})
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "banco_id")
    private Integer bancoId;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bancoId")
    private Collection<Caso> casoCollection;

    public Banco() {
    }

    public Banco(Integer bancoId) {
        this.bancoId = bancoId;
    }

    public Banco(Integer bancoId, String nombre, String usuario, String password) {
        this.bancoId = bancoId;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }

    public Integer getBancoId() {
        return bancoId;
    }

    public void setBancoId(Integer bancoId) {
        this.bancoId = bancoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Caso> getCasoCollection() {
        return casoCollection;
    }

    public void setCasoCollection(Collection<Caso> casoCollection) {
        this.casoCollection = casoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bancoId != null ? bancoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Banco)) {
            return false;
        }
        Banco other = (Banco) object;
        if ((this.bancoId == null && other.bancoId != null) || (this.bancoId != null && !this.bancoId.equals(other.bancoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.ups.modelo.Banco[ bancoId=" + bancoId + " ]";
    }
    
}
