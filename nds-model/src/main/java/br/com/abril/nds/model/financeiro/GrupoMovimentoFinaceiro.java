package br.com.abril.nds.model.financeiro;

public enum GrupoMovimentoFinaceiro {
	
	ENVIO_JORNALEIRO(OperacaoFinaceira.DEBITO),
	CREDITO(OperacaoFinaceira.CREDITO),
	DEBITO(OperacaoFinaceira.DEBITO);
	
	private OperacaoFinaceira operacaoFinaceira;
	
	private GrupoMovimentoFinaceiro(OperacaoFinaceira operacaoFinaceira) {
		this.operacaoFinaceira = operacaoFinaceira;
	}
	
	public OperacaoFinaceira getOperacaoFinaceira() {
		return operacaoFinaceira;
	}

}
