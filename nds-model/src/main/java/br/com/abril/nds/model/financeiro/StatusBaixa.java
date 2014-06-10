package br.com.abril.nds.model.financeiro;

/**
 * @author Discover Technology
 *
 */
public enum StatusBaixa {
	
	PAGO("Pago com sucesso."),
	PAGAMENTO_PARCIAL("Pagamento parcial."),
	PAGO_DIVERGENCIA_VALOR("Pago com divergência de valor."),
	PAGO_DIVERGENCIA_DATA("Pago com divergência de data."),
	PAGO_BOLETO_NAO_ENCONTRADO("Boleto não encontrado."),
	
	NAO_PAGO_DIVERGENCIA_VALOR("Não pago por divergência de valor."),
	NAO_PAGO_DIVERGENCIA_DATA("Não pago por divergência de data."),
	NAO_PAGO_BAIXA_JA_REALIZADA("Baixa já foi realizada."),
	NAO_PAGO_POSTERGADO("Baixa postergada.");
	
	private String descricao;
	
	private StatusBaixa(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {

		return this.descricao;
	}
}
