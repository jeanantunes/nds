package br.com.abril.nds.model.movimentacao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum DominioTipoMovimento  {
	
	RECEBIMENTO_FISICO(TipoOperacao.ENTRADA), 
	ENVIO_JORNALEIRO(TipoOperacao.SAIDA),
	RECEBIMENTO_ENCALHE(TipoOperacao.ENTRADA), 
	SOBRA_DE(TipoOperacao.ENTRADA), 
	SOBRA_EM(TipoOperacao.ENTRADA), 
	FALTA_DE(TipoOperacao.SAIDA), 
	FALTA_EM(TipoOperacao.SAIDA),
	RECEBIMENTO_REPARTE(TipoOperacao.ENTRADA),
	ENVIO_ENCALHE(TipoOperacao.SAIDA);
	
	private TipoOperacao tipoOperacao;
	
	private DominioTipoMovimento(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}