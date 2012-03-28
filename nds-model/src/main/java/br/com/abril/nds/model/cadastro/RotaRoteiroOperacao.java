package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "ROTA_ROTEIRO_OPERACAO")
@SequenceGenerator(name="ROTA_ROTEIRO_OPERACAO_SEQ", initialValue = 1, allocationSize = 1)
public class RotaRoteiroOperacao {
	
	@Id
	@GeneratedValue(generator = "ROTA_ROTEIRO_OPERACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cota;
	
	@ManyToOne
	@JoinColumn(name = "ID_ROTA")
	private Rota rota;
	
	@ManyToOne
	@JoinColumn(name = "ID_ROTEIRO")
	private Roteiro roteiro;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_OPERACAO", nullable = false)
	private TipoOperacao tipoOperacao; 
	
	public enum TipoOperacao{
		IMPRESSAO_DIVIDA
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
	 * @return the rota
	 */
	public Rota getRota() {
		return rota;
	}

	/**
	 * @param rota the rota to set
	 */
	public void setRota(Rota rota) {
		this.rota = rota;
	}

	/**
	 * @return the roteiro
	 */
	public Roteiro getRoteiro() {
		return roteiro;
	}

	/**
	 * @param roteiro the roteiro to set
	 */
	public void setRoteiro(Roteiro roteiro) {
		this.roteiro = roteiro;
	}

	/**
	 * @return the tipoOperacao
	 */
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	/**
	 * @param tipoOperacao the tipoOperacao to set
	 */
	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	
}
