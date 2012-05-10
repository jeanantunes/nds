package br.com.abril.nds.model.movimentacao;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * Entidade que abstrai a sinalização do status da 
 * conferência de encalhe da cota.
 * 
 * @author Discover Technology 
 */
@Entity
@Table(name = "CONTROLE_CONFERENCIA_ENCALHE_COTA")
@SequenceGenerator(name="CTRL_CONF_ENCALHE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ControleConferenciaEncalheCota {
	
	@Id
	@GeneratedValue(generator = "CTRL_CONF_ENCALHE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INICIO", nullable = false)
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_FIM", nullable = false)
	private Date dataFim;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusOperacao status;

	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CTRL_CONF_ENCALHE_ID")
	private ControleConferenciaEncalhe controleConferenciaEncalhe;

	/**
	 * Obtém id
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém dataInicio
	 *
	 * @return Date
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * Atribuí dataInicio
	 * @param dataInicio 
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * Obtém dataFim
	 *
	 * @return Date
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * Atribuí dataFim
	 * @param dataFim 
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * Obtém dataOperacao
	 *
	 * @return Date
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * Atribuí dataOperacao
	 * @param dataOperacao 
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	/**
	 * Obtém status
	 *
	 * @return StatusOperacao
	 */
	public StatusOperacao getStatus() {
		return status;
	}

	/**
	 * Atribuí status
	 * @param status 
	 */
	public void setStatus(StatusOperacao status) {
		this.status = status;
	}
	
	/**
	 * Obtém cota
	 *
	 * @return Cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * Atribuí cota
	 * @param cota 
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * Obtém controleConferenciaEncalhe
	 *
	 * @return ControleConferenciaEncalhe
	 */
	public ControleConferenciaEncalhe getControleConferenciaEncalhe() {
		return controleConferenciaEncalhe;
	}

	/**
	 * Atribuí controleConferenciaEncalhe
	 * @param controleConferenciaEncalhe 
	 */
	public void setControleConferenciaEncalhe(
			ControleConferenciaEncalhe controleConferenciaEncalhe) {
		this.controleConferenciaEncalhe = controleConferenciaEncalhe;
	}

	
	
}
