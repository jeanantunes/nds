package br.com.abril.nds.model.financeiro;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.abril.nds.model.TipoMovimento;

@Entity
@DiscriminatorValue(value = "FINANCEIRO")
public class TipoMovimentoFinanceiro extends TipoMovimento {
	
	@Column(name = "GRUPO_MOVIMENTO_FINANCEIRO")
	@Enumerated(EnumType.STRING)
	private GrupoMovimentoFinaceiro grupoMovimentoFinaceiro;
	
	@Column(name = "OPERACAO_FINANCEIRA")
	@Enumerated(EnumType.STRING)
	private OperacaoFinaceira operacaoFinaceira;

	public GrupoMovimentoFinaceiro getGrupoMovimentoFinaceiro() {
		return grupoMovimentoFinaceiro;
	}
	
	public void setGrupoMovimentoFinaceiro(
			GrupoMovimentoFinaceiro grupoMovimentoFinaceiro) {
		this.grupoMovimentoFinaceiro = grupoMovimentoFinaceiro;
		this.operacaoFinaceira = grupoMovimentoFinaceiro.getOperacaoFinaceira();
	}
	
	public OperacaoFinaceira getOperacaoFinaceira() {
		return operacaoFinaceira;
	}

}
