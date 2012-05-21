package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

/**
 * Entidade de abstrai os movimentos da cota especificos de envio de encalhe 
 * e auxilia a identificar com qual dataRecolhimento original o movimento esta 
 * relacionado (devido a possibilidade de um envio de encalhe antecipado).
 * 
 * @author Discover Technology
 *
 */
@Entity
@Table(name = "CONFERENCIA_ENCALHE")
@SequenceGenerator(name="CONFERENCIA_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class ConferenciaEncalhe implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CONFERENCIA_ENCALHE_SEQ")
	private Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID")
	private MovimentoEstoqueCota movimentoEstoqueCota;

	@ManyToOne(optional = false)
	@JoinColumn(name = "CHAMADA_ENCALHE_COTA_ID")
	private ChamadaEncalheCota chamadaEncalheCota;

	@ManyToOne(optional = false)
	@JoinColumn(name = "CONTROLE_CONFERENCIA_ENCALHE_COTA_ID")
	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;

	@Column(name = "OBSERVACAO")
	private String observacao;
	
	@Column(name = "JURAMENTADA")
	private boolean juramentada;
	
	@Column(name = "QTDE_INFORMADA")
	private BigDecimal qtdeInformada;
	
	
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
	 * Obtém movimentoEstoqueCota
	 *
	 * @return MovimentoEstoqueCota
	 */
	public MovimentoEstoqueCota getMovimentoEstoqueCota() {
		return movimentoEstoqueCota;
	}

	/**
	 * Atribuí movimentoEstoqueCota
	 * @param movimentoEstoqueCota 
	 */
	public void setMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		this.movimentoEstoqueCota = movimentoEstoqueCota;
	}

	/**
	 * Obtém chamadaEncalheCota
	 *
	 * @return ChamadaEncalheCota
	 */
	public ChamadaEncalheCota getChamadaEncalheCota() {
		return chamadaEncalheCota;
	}

	/**
	 * Atribuí chamadaEncalheCota
	 * @param chamadaEncalheCota 
	 */
	public void setChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		this.chamadaEncalheCota = chamadaEncalheCota;
	}

	/**
	 * Obtém controleConferenciaEncalheCota
	 *
	 * @return ControleConferenciaEncalheCota
	 */
	public ControleConferenciaEncalheCota getControleConferenciaEncalheCota() {
		return controleConferenciaEncalheCota;
	}

	/**
	 * Atribuí controleConferenciaEncalheCota
	 * @param controleConferenciaEncalheCota 
	 */
	public void setControleConferenciaEncalheCota(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		this.controleConferenciaEncalheCota = controleConferenciaEncalheCota;
	}

	/**
	 * Obtém observacao
	 *
	 * @return String
	 */
	public String getObservacao() {
		return observacao;
	}

	/**
	 * Atribuí observacao
	 * @param observacao 
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * Obtém juramentada
	 *
	 * @return boolean
	 */
	public boolean isJuramentada() {
		return juramentada;
	}

	/**
	 * Atribuí juramentada
	 * @param juramentada 
	 */
	public void setJuramentada(boolean juramentada) {
		this.juramentada = juramentada;
	}

	/**
	 * Obtém qtdeInformada
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdeInformada() {
		return qtdeInformada;
	}

	/**
	 * Atribuí qtdeInformada
	 * @param qtdeInformada 
	 */
	public void setQtdeInformada(BigDecimal qtdeInformada) {
		this.qtdeInformada = qtdeInformada;
	}
	
		
}
