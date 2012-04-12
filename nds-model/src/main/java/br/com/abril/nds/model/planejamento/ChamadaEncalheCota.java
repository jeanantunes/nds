package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * Entidade com as chamadas de encalhes das cotas
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "CHAMADA_ENCALHE_COTA")
@SequenceGenerator(name="CHAMADA_ENCALHE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ChamadaEncalheCota implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CHAMADA_ENCALHE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "CONFERIDO", nullable = false)
	private boolean conferido;
	
	@Column(name = "QTDE_PREVISTA", nullable = false)
	private BigDecimal qtdePrevista;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CHAMADA_ENCALHE_ID")
	private ChamadaEncalhe chamadaEncalhe;

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
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	/**
	 * @return the conferido
	 */
	public boolean isConferido() {
		return conferido;
	}

	/**
	 * @param conferido the conferido to set
	 */
	public void setConferido(boolean conferido) {
		this.conferido = conferido;
	}

	public BigDecimal getQtdePrevista() {
		return qtdePrevista;
	}
	
	public void setQtdePrevista(BigDecimal qtdePrevista) {
		this.qtdePrevista = qtdePrevista;
	}

	/**
	 * @return the chamadaEncalhe
	 */
	public ChamadaEncalhe getChamadaEncalhe() {
		return chamadaEncalhe;
	}

	/**
	 * @param chamadaEncalhe the chamadaEncalhe to set
	 */
	public void setChamadaEncalhe(ChamadaEncalhe chamadaEncalhe) {
		this.chamadaEncalhe = chamadaEncalhe;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
