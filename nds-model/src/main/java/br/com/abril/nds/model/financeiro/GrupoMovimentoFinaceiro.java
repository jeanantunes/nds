package br.com.abril.nds.model.financeiro;

public enum GrupoMovimentoFinaceiro {
	
	RECEBIMENTO_REPARTE(OperacaoFinaceira.DEBITO),
	ENVIO_ENCALHE(OperacaoFinaceira.CREDITO),
	POSTERGADO(OperacaoFinaceira.DEBITO),
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
