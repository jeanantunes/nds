package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParametrosAprovacaoDistribuidor implements Serializable {

	private static final long serialVersionUID = 556923148720562261L;

	@Column(name = "DEBITO_CREDITO",nullable=true)
	private boolean debitoCredito;
	
	@Column(name = "NEGOCIACAO",nullable=true)
	private boolean negociacao;
	
	@Column(name = "AJUSTE_ESTOQUE",nullable=true)
	private boolean ajusteEstoque;
	
	@Column(name = "POSTERGACAO_COBRANCA",nullable=true)
	private boolean postergacaoCobranca;
	
	@Column(name = "DEVOLUCAO_FORNECEDOR",nullable=true)
	private boolean devolucaoFornecedor;
	
	@Column(name = "FALTAS_SOBRAS",nullable=true)
	private boolean faltasSobras;

	public boolean isDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(boolean debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	public boolean isNegociacao() {
		return negociacao;
	}

	public void setNegociacao(boolean negociacao) {
		this.negociacao = negociacao;
	}

	public boolean isAjusteEstoque() {
		return ajusteEstoque;
	}

	public void setAjusteEstoque(boolean ajusteEstoque) {
		this.ajusteEstoque = ajusteEstoque;
	}

	public boolean isPostergacaoCobranca() {
		return postergacaoCobranca;
	}

	public void setPostergacaoCobranca(boolean postergacaoCobranca) {
		this.postergacaoCobranca = postergacaoCobranca;
	}

	public boolean isDevolucaoFornecedor() {
		return devolucaoFornecedor;
	}

	public void setDevolucaoFornecedor(boolean devolucaoFornecedor) {
		this.devolucaoFornecedor = devolucaoFornecedor;
	}

	public boolean isFaltasSobras() {
		return faltasSobras;
	}

	public void setFaltasSobras(boolean faltasSobras) {
		this.faltasSobras = faltasSobras;
	}
	
}
