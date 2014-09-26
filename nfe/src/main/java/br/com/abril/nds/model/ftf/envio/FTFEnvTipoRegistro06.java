package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro06 extends FTFBaseDTO implements FTFCommons {

	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "6";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codigoEstabelecimentoEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEmpresaEmissora;

	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;

	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=8, tipo="char", ordem=7)
	private String numDocumentoOrigemAssociado;
	
	@FTFfield(tamanho=10, tipo="char", ordem=8)
	private String dataEntradaOuSaidaNfReferenciada;
	
	@FTFfield(tamanho=1, tipo="char", ordem=9)
	private String indicadorTipoNfReferenciada;
	
	@FTFfield(tamanho=250, tipo="char", ordem=10)
	private String observacaoNotaReferenciada;
	
	@FTFfield(tamanho=1, tipo="char", ordem=11)
	private String indicadorEmitenteTituloNfReferenciada;
	
	@FTFfield(tamanho=3, tipo="char", ordem=12)
	private String modeloDocumentoNfReferenciada;
	
	@FTFfield(tamanho=14, tipo="char", ordem=13)
	private String cpfCnpjEmissorNfReferenciada;
	
	@FTFfield(tamanho=2, tipo="char", ordem=14)
	private String categoriaPfPjNfReferenciada;
	
	@FTFfield(tamanho=5, tipo="char", ordem=15)
	private String serieNfReferenciada;
	
	@FTFfield(tamanho=15, tipo="char", ordem=16)
	private String numNfRreferenciada;
	
	@FTFfield(tamanho=10, tipo="char", ordem=17)
	private String dataEmissaoNfReferenciada;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=18)
	private String numSequencial;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=19)
	private String numItemPedido;
	
	@FTFfield(tamanho=44, tipo="char", ordem=20)
	private String chaveAcessoNfe;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodigoEstabelecimentoEmissor() {
		return codigoEstabelecimentoEmissor;
	}

	public void setCodigoEstabelecimentoEmissor(String codigoEstabelecimentoEmissor) {
		this.codigoEstabelecimentoEmissor = codigoEstabelecimentoEmissor;
	}

	public String getNumDocumentoOrigemAssociado() {
		return numDocumentoOrigemAssociado;
	}

	public void setNumDocumentoOrigemAssociado(String numDocumentoOrigemAssociado) {
		this.numDocumentoOrigemAssociado = numDocumentoOrigemAssociado;
	}

	public String getDataEntradaOuSaidaNfReferenciada() {
		return dataEntradaOuSaidaNfReferenciada;
	}

	public void setDataEntradaOuSaidaNfReferenciada(
			String dataEntradaOuSaidaNfReferenciada) {
		this.dataEntradaOuSaidaNfReferenciada = dataEntradaOuSaidaNfReferenciada;
	}
	
	public String getIndicadorTipoNfReferenciada() {
		return indicadorTipoNfReferenciada;
	}

	public void setIndicadorTipoNfReferenciada(String indicadorTipoNfReferenciada) {
		this.indicadorTipoNfReferenciada = indicadorTipoNfReferenciada;
	}

	public String getObservacaoNotaReferenciada() {
		return observacaoNotaReferenciada;
	}

	public void setObservacaoNotaReferenciada(String observacaoNotaReferenciada) {
		this.observacaoNotaReferenciada = observacaoNotaReferenciada;
	}

	public String getIndicadorEmitenteTituloNfReferenciada() {
		return indicadorEmitenteTituloNfReferenciada;
	}

	public void setIndicadorEmitenteTituloNfReferenciada(
			String indicadorEmitenteTituloNfReferenciada) {
		this.indicadorEmitenteTituloNfReferenciada = indicadorEmitenteTituloNfReferenciada;
	}

	public String getModeloDocumentoNfReferenciada() {
		return modeloDocumentoNfReferenciada;
	}

	public void setModeloDocumentoNfReferenciada(
			String modeloDocumentoNfReferenciada) {
		this.modeloDocumentoNfReferenciada = modeloDocumentoNfReferenciada;
	}

	public String getCpfCnpjEmissorNfReferenciada() {
		return cpfCnpjEmissorNfReferenciada;
	}

	public void setCpfCnpjEmissorNfReferenciada(String cpfCnpjEmissorNfReferenciada) {
		this.cpfCnpjEmissorNfReferenciada = cpfCnpjEmissorNfReferenciada;
	}

	public String getCategoriaPfPjNfReferenciada() {
		return categoriaPfPjNfReferenciada;
	}

	public void setCategoriaPfPjNfReferenciada(String categoriaPfPjNfReferenciada) {
		this.categoriaPfPjNfReferenciada = categoriaPfPjNfReferenciada;
	}

	public String getSerieNfReferenciada() {
		return serieNfReferenciada;
	}

	public void setSerieNfReferenciada(String serieNfReferenciada) {
		this.serieNfReferenciada = serieNfReferenciada;
	}

	public String getNumNfRreferenciada() {
		return numNfRreferenciada;
	}

	public void setNumNfRreferenciada(String numNfRreferenciada) {
		this.numNfRreferenciada = numNfRreferenciada;
	}

	public String getDataEmissaoNfReferenciada() {
		return dataEmissaoNfReferenciada;
	}

	public void setDataEmissaoNfReferenciada(String dataEmissaoNfReferenciada) {
		this.dataEmissaoNfReferenciada = dataEmissaoNfReferenciada;
	}

	public String getNumSequencial() {
		return numSequencial;
	}

	public void setNumSequencial(String numSequencial) {
		this.numSequencial = numSequencial;
	}

	public String getNumItemPedido() {
		return numItemPedido;
	}

	public void setNumItemPedido(String numItemPedido) {
		this.numItemPedido = numItemPedido;
	}

	public String getChaveAcessoNfe() {
		return chaveAcessoNfe;
	}

	public void setChaveAcessoNfe(String chaveAcessoNfe) {
		this.chaveAcessoNfe = chaveAcessoNfe;
	}
	
	@Override
	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora;
	}

	@Override
	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal;
	}

	@Override
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	@Override
	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}

	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
	}

	public String getCodLocal() {
		return codLocal;
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public String getNumeroDocOrigem() {
		return numeroDocOrigem;
	}
	
}
