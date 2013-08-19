package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;

public class TipoMovimentoDTO implements Serializable{

	private static final long serialVersionUID = 8582779997204233061L;

	private Long codigo;
	private String descricao;
	
	private String grupoOperacao;
	private GrupoOperacao grupoOperacaoValue;
	
	private String operacao;
	private Operacao operacaoValue;
	
	private String aprovacao;
	private Aprovacao aprovacaoValue;
	
	private String incideDivida;
	private IncideDivida incideDividaValue;
	
	private boolean permiteAlteracao;
	
	public TipoMovimentoDTO() {
		permiteAlteracao = true;
	}			
	
	public enum IncideDivida {
		SIM("Sim",true),
		NAO("Não",false),
		NAO_SE_APLICA("-",null);
		
		private String descricao;
		private Boolean incide;
		
		private IncideDivida(String descricao, Boolean incide) {
			this.descricao= descricao;
			this.incide = incide;
		}
		
		public String toString() {
			return descricao;
		}

		public Boolean getIncide() {
			return incide;
		}

	
	}
	
	public enum Aprovacao {
		
		SIM("Sim", true),
		NAO("Não", false);		
		
		private String descricao;
		private Boolean aprovado;
		
		private Aprovacao(String descricao, Boolean aprovado) {
			this.aprovado = aprovado;
			this.descricao= descricao;
		}
		
		public String toString() {
			return descricao;
		}

		public Boolean getAprovado() {
			return aprovado;
		}
	}
	
	public enum GrupoOperacao {
		
		FINANCEIRO("Financeiro"),
		ESTOQUE("Estoque");
		
		private String descricao;
				
		private GrupoOperacao(String descricao) {
			this.descricao= descricao;
		}
		
		public String toString() {
			return descricao;
		}		
	}
	
	public enum Operacao {
		
		CREDITO(GrupoOperacao.FINANCEIRO,"Crédito"),
		DEBITO(GrupoOperacao.FINANCEIRO,"Débito"),
		SAIDA(GrupoOperacao.ESTOQUE,"Saída"),
		ENTRADA(GrupoOperacao.ESTOQUE,"Entrada");
		
		private String descricao;
		private GrupoOperacao grupo;
		
		private Operacao(GrupoOperacao grupo, String descricao) {
			this.grupo = grupo;
			this.descricao= descricao;
		}
		
		public String toString() {
			return descricao;
		}

		public GrupoOperacao getGrupo() {
			return grupo;
		}
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
		GrupoOperacao grupo = GrupoOperacao.valueOf(grupoOperacao);
		this.grupoOperacao = grupo.descricao;
		this.grupoOperacaoValue = grupo;
	}
	
	public void setOperacaoEstoque(OperacaoEstoque operacaoEstoque) {
		if(operacaoEstoque != null)
			this.setOperacaoValue(Operacao.valueOf(operacaoEstoque.name()));
	}
	public void setOperacaoFinanceira(OperacaoFinaceira operacaoFinanceira) {
		if(operacaoFinanceira != null)
			this.setOperacaoValue(Operacao.valueOf(operacaoFinanceira.name()));
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
	public void setAprovacao(Boolean aprovacao) {
		
		if (aprovacao==true) {
			this.aprovacao="Sim";
			this.aprovacaoValue = Aprovacao.SIM;
		} else if (aprovacao==false) {
			this.aprovacao="Não";
			this.aprovacaoValue = Aprovacao.NAO;
		}
	}
	public String getIncideDivida() {
		return incideDivida;
	}
	public void setIncideDivida(Boolean incideDivida) {
		if(incideDivida==null) {
			this.incideDivida = "-";
			this.incideDividaValue = IncideDivida.NAO_SE_APLICA;
		} else if (incideDivida==true) {
			this.incideDivida="Sim";
			this.incideDividaValue = IncideDivida.SIM;
		} else if (incideDivida==false) {
			this.incideDivida="Não";
			this.incideDividaValue = IncideDivida.NAO;
		}
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

	public GrupoOperacao getGrupoOperacaoValue() {
		return grupoOperacaoValue;
	}

	public void setGrupoOperacaoValue(GrupoOperacao grupoOperacaoValue) {
		this.grupoOperacaoValue = grupoOperacaoValue;
	}

	public Operacao getOperacaoValue() {
		return operacaoValue;
	}

	public void setOperacaoValue(Operacao operacaoValue) {
		this.operacao = operacaoValue.descricao;
		this.operacaoValue = operacaoValue;
	}

	public Aprovacao getAprovacaoValue() {
		return aprovacaoValue;
	}

	public void setAprovacaoValue(Aprovacao aprovacaoValue) {
		this.aprovacaoValue = aprovacaoValue;
	}

	public IncideDivida getIncideDividaValue() {
		return incideDividaValue;
	}

	public void setIncideDividaValue(IncideDivida incideDividaValue) {
		this.incideDividaValue = incideDividaValue;
	}
	public Boolean getPermiteAlteracao() {
		return permiteAlteracao;
	}
	public void setPermiteAlteracao(Boolean permiteAlteracao) {
		this.permiteAlteracao = permiteAlteracao;
	}

	public void setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro grupo) {
		if(grupo != null)
			permiteAlteracao = false;
	}
	
	public void setGrupoMovimentoEstoque(GrupoMovimentoEstoque grupo) {
		if(grupo != null)
			permiteAlteracao = false;
	}	
}
