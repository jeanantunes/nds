package br.com.abril.nds.model.financeiro;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.abril.nds.model.movimentacao.TipoMovimento;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorValue(value = "FINANCEIRO")
public class TipoMovimentoFinanceiro extends TipoMovimento {

	private static final long serialVersionUID = -6127568503693464740L;

	@Column(name = "GRUPO_MOVIMENTO_FINANCEIRO")
	@Enumerated(EnumType.STRING)
	private GrupoMovimentoFinaceiro grupoMovimentoFinaceiro;
	
	@Column(name = "OPERACAO_FINANCEIRA")
	@Enumerated(EnumType.STRING)
	private OperacaoFinaceira operacaoFinaceira;

	public TipoMovimentoFinanceiro() {
		
	}
		
	public TipoMovimentoFinanceiro(
			GrupoMovimentoFinaceiro grupoMovimentoFinaceiro,
			OperacaoFinaceira operacaoFinaceira) {
		super();
		this.grupoMovimentoFinaceiro = grupoMovimentoFinaceiro;
		this.setOperacaoFinaceira(operacaoFinaceira);
	}

	public GrupoMovimentoFinaceiro getGrupoMovimentoFinaceiro() {
		return grupoMovimentoFinaceiro;
	}
	
	public void setGrupoMovimentoFinaceiro(
			GrupoMovimentoFinaceiro grupoMovimentoFinaceiro) {
		this.grupoMovimentoFinaceiro = grupoMovimentoFinaceiro;
		this.setOperacaoFinaceira(grupoMovimentoFinaceiro.getOperacaoFinaceira());
	}

	public OperacaoFinaceira getOperacaoFinaceira() {
		return operacaoFinaceira;
	}

	public void setOperacaoFinaceira(OperacaoFinaceira operacaoFinaceira) {
		this.operacaoFinaceira = operacaoFinaceira;
	}
	
}
