package br.com.abril.nds.model.movimentacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "TIPO_MOVIMENTO")
@SequenceGenerator(name = "TP_MOVIMENTO_SEQ", initialValue = 1, allocationSize = 1)
public class TipoMovimento {

	@Id
	@GeneratedValue(generator = "TP_MOVIMENTO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	@Column(name = "APROVACAO_AUTOMATICA", nullable = false)
	private boolean aprovacaoAutomatica;
	@Column(name = "INCIDE_DIVIDA", nullable = false)
	private boolean incideDivida;
	@Column(name = "TIPO_MOVIMENTO_ESTOQUE", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoMovimentoEstoque tipoMovimentoEstoque;
	@Column(name = "TIPO_OPERACAO", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoOperacao tipoOperacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAprovacaoAutomatica() {
		return aprovacaoAutomatica;
	}

	public void setAprovacaoAutomatica(boolean aprovacaoAutomatica) {
		this.aprovacaoAutomatica = aprovacaoAutomatica;
	}

	public boolean isIncideDivida() {
		return incideDivida;
	}

	public void setIncideDivida(boolean incideDivida) {
		this.incideDivida = incideDivida;
	}

	public TipoMovimentoEstoque getTipoMovimentoEstoque() {
		return tipoMovimentoEstoque;
	}

	public void setTipoMovimentoEstoque(
			TipoMovimentoEstoque tipoMovimentoEstoque) {
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
		this.tipoOperacao = tipoMovimentoEstoque.getTipoOperacao();
	}

	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}