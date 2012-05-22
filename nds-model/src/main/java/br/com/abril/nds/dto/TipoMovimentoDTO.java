package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;

public class TipoMovimentoDTO implements Serializable{

	private static final long serialVersionUID = 8582779997204233061L;

	private Long codigo;
	private String descricao;
	private String grupoOperacao;
	private String operacao;
	private String aprovacao;
	private String incideDivida;
	private OperacaoEstoque teste;
	
	public TipoMovimentoDTO() {
		
	}			
	
	public TipoMovimentoDTO(Long codigo, String descricao,
			String grupoOperacao, String operacao, String aprovacao,
			String incideDivida) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.grupoOperacao = grupoOperacao;
		this.operacao = operacao;
		this.aprovacao = aprovacao;
		this.incideDivida = incideDivida;
		this.teste = OperacaoEstoque.ENTRADA;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getGrupoOperacao() {
		return grupoOperacao;
	}
	public void setGrupoOperacao(String grupoOperacao) {
		this.grupoOperacao = grupoOperacao;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public String getAprovacao() {
		return aprovacao;
	}
	public void setAprovacao(String aprovacao) {
		this.aprovacao = aprovacao;
	}
	public String getIncideDivida() {
		return incideDivida;
	}
	public void setIncideDivida(String incideDivida) {
		this.incideDivida = incideDivida;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aprovacao == null) ? 0 : aprovacao.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((grupoOperacao == null) ? 0 : grupoOperacao.hashCode());
		result = prime * result
				+ ((incideDivida == null) ? 0 : incideDivida.hashCode());
		result = prime * result
				+ ((operacao == null) ? 0 : operacao.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoMovimentoDTO other = (TipoMovimentoDTO) obj;
		if (aprovacao == null) {
			if (other.aprovacao != null)
				return false;
		} else if (!aprovacao.equals(other.aprovacao))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (grupoOperacao == null) {
			if (other.grupoOperacao != null)
				return false;
		} else if (!grupoOperacao.equals(other.grupoOperacao))
			return false;
		if (incideDivida == null) {
			if (other.incideDivida != null)
				return false;
		} else if (!incideDivida.equals(other.incideDivida))
			return false;
		if (operacao == null) {
			if (other.operacao != null)
				return false;
		} else if (!operacao.equals(other.operacao))
			return false;
		return true;
	}
}
