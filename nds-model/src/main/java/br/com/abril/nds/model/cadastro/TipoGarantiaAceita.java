/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name = "TIPO_GARANTIA_ACEITA")
@SequenceGenerator(name="TIPO_GARANTIA_ACEITA_SEQ", initialValue = 1, allocationSize = 1)
public class TipoGarantiaAceita implements Serializable {

    private static final long serialVersionUID = 7578054367634139540L;

    @Id
	@GeneratedValue(generator="TIPO_GARANTIA_ACEITA_SEQ")
	@Column
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoGarantia tipoGarantia;

	@Column(name = "VALOR", nullable = true)
	private Integer valor;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
    public TipoGarantiaAceita() {
    }
	
	public TipoGarantiaAceita(TipoGarantia tipoGarantia, Integer valor,
            Distribuidor distribuidor) {
        this.tipoGarantia = tipoGarantia;
        this.valor = valor;
        this.distribuidor = distribuidor;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the tipoGarantia
     */
    public TipoGarantia getTipoGarantia() {
        return tipoGarantia;
    }

    /**
     * @param tipoGarantia the tipoGarantia to set
     */
    public void setTipoGarantia(TipoGarantia tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }

    /**
     * @return the valor
     */
    public Integer getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Integer valor) {
        this.valor = valor;
    }

    /**
     * @return the distribuidor
     */
    public Distribuidor getDistribuidor() {
        return distribuidor;
    }

    /**
     * @param distribuidor the distribuidor to set
     */
    public void setDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((distribuidor == null) ? 0 : distribuidor.hashCode());
        result = prime * result
                + ((tipoGarantia == null) ? 0 : tipoGarantia.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TipoGarantiaAceita other = (TipoGarantiaAceita) obj;
        if (distribuidor == null) {
            if (other.distribuidor != null) {
                return false;
            }
        } else if (!distribuidor.equals(other.distribuidor)) {
            return false;
        }
        if (tipoGarantia != other.tipoGarantia) {
            return false;
        }
        return true;
    }
    
    
	
}
