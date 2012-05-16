package br.com.abril.nds.model.planejamento;

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

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * Cadastro de lançamento e recolhimento parcial, 
 * contém a data inicial de lançamento e a última
 * data de recolhimento 
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "LANCAMENTO_PARCIAL")
@SequenceGenerator(name = "LANCAMENTO_PARCIAL_SEQ", initialValue = 1, allocationSize = 1)
public class LancamentoParcial {
	
	@Id
	@GeneratedValue(generator = "LANCAMENTO_PARCIAL_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID", nullable = false)
	private ProdutoEdicao produtoEdicao;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "LANCAMENTO_INICIAL", nullable = false)
	private Date lancamentoInicial;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "RECOLHIMENTO_FINAL", nullable = false)
	private Date recolhimentoFinal;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusLancamentoParcial status;

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
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the lancamentoInicial
	 */
	public Date getLancamentoInicial() {
		return lancamentoInicial;
	}

	/**
	 * @param lancamentoInicial the lancamentoInicial to set
	 */
	public void setLancamentoInicial(Date lancamentoInicial) {
		this.lancamentoInicial = lancamentoInicial;
	}

	/**
	 * @return the recolhimentoFinal
	 */
	public Date getRecolhimentoFinal() {
		return recolhimentoFinal;
	}

	/**
	 * @param recolhimentoFinal the recolhimentoFinal to set
	 */
	public void setRecolhimentoFinal(Date recolhimentoFinal) {
		this.recolhimentoFinal = recolhimentoFinal;
	}

	/**
	 * @return the status
	 */
	public StatusLancamentoParcial getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusLancamentoParcial status) {
		this.status = status;
	}

}
