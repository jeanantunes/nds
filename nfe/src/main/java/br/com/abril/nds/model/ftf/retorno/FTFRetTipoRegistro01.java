package br.com.abril.nds.model.ftf.retorno;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;

public class FTFRetTipoRegistro01 extends FTFBaseDTO{


	
	private String tipoRegistro;
	private String cnpjCpfDestinatario;
	private Long numeroDocumentoOrigem;
	private String serieNFe;
	private String numeroNFe;
	private String dataEmissaoNFe;
	private String dataCancelamento;
	private String dataInutilizacao;
	private String statusNFe;
	private String chaveAcessoNFe;
	private String numeroProtocoloAutorizacao;
	private String cnpjEmissor;
	private String serieNFeEntrada;
	private String numeroNFeEntrada;
	private String dataEmissaoNFeEntrada;
	private String descricaoErroStatusNota;
	private String dataSaidaMercadoria;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	@FTFfield(tamanho = 1, tipo = "char", ordem = 1)
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCnpjCpfDestinatario() {
		return cnpjCpfDestinatario;
	}

	@FTFfield(tamanho = 14, tipo = "char", ordem = 2)
	public void setCnpjCpfDestinatario(String cnpjCpfDestinatario) {
		this.cnpjCpfDestinatario = cnpjCpfDestinatario;
	}

	public Long getNumeroDocumentoOrigem() {
		return numeroDocumentoOrigem;
	}

	@FTFfield(tamanho = 8, tipo = "long", ordem = 3)
	public void setNumeroDocumentoOrigem(Long numeroDocumentoOrigem) {
		this.numeroDocumentoOrigem = numeroDocumentoOrigem;
	}

	public String getSerieNFe() {
		return serieNFe;
	}

	@FTFfield(tamanho = 3, tipo = "char", ordem = 4)
	public void setSerieNFe(String serieNFe) {
		this.serieNFe = serieNFe;
	}

	public String getNumeroNFe() {
		return numeroNFe;
	}

	@FTFfield(tamanho = 9, tipo = "char", ordem = 5)
	public void setNumeroNFe(String numeroNFe) {
		this.numeroNFe = numeroNFe;
	}

	public String getDataEmissaoNFe() {
		return dataEmissaoNFe;
	}

	@FTFfield(tamanho = 10, tipo = "char", ordem = 6)
	public void setDataEmissaoNFe(String dataEmissaoNFe) {
		this.dataEmissaoNFe = dataEmissaoNFe;
	}

	public String getDataCancelamento() {
		return dataCancelamento;
	}

	@FTFfield(tamanho = 10, tipo = "char", ordem = 7)
	public void setDataCancelamento(String dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getDataInutilizacao() {
		return dataInutilizacao;
	}

	@FTFfield(tamanho = 10, tipo = "char", ordem = 8)
	public void setDataInutilizacao(String dataInutilizacao) {
		this.dataInutilizacao = dataInutilizacao;
	}

	public String getStatusNFe() {
		return statusNFe;
	}

	@FTFfield(tamanho = 40, tipo = "char", ordem = 9)
	public void setStatusNFe(String statusNFe) {
		this.statusNFe = statusNFe;
	}

	public String getChaveAcessoNFe() {
		return chaveAcessoNFe;
	}

	@FTFfield(tamanho = 44, tipo = "char", ordem = 10)
	public void setChaveAcessoNFe(String chaveAcessoNFe) {
		this.chaveAcessoNFe = chaveAcessoNFe;
	}

	public String getNumeroProtocoloAutorizacao() {
		return numeroProtocoloAutorizacao;
	}

	@FTFfield(tamanho = 15, tipo = "char", ordem = 11)
	public void setNumeroProtocoloAutorizacao(String numeroProtocoloAutorizacao) {
		this.numeroProtocoloAutorizacao = numeroProtocoloAutorizacao;
	}

	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	@FTFfield(tamanho = 14, tipo = "char", ordem = 12)
	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	public String getSerieNFeEntrada() {
		return serieNFeEntrada;
	}
	
	@FTFfield(tamanho = 3, tipo = "char", ordem = 13)
	public void setSerieNFeEntrada(String serieNFeEntrada) {
		this.serieNFeEntrada = serieNFeEntrada;
	}

	public String getNumeroNFeEntrada() {
		return numeroNFeEntrada;
	}

	@FTFfield(tamanho = 9, tipo = "char", ordem = 14)
	public void setNumeroNFeEntrada(String numeroNFeEntrada) {
		this.numeroNFeEntrada = numeroNFeEntrada;
	}

	public String getDataEmissaoNFeEntrada() {
		return dataEmissaoNFeEntrada;
	}

	@FTFfield(tamanho = 10, tipo = "char", ordem = 15)
	public void setDataEmissaoNFeEntrada(String dataEmissaoNFeEntrada) {
		this.dataEmissaoNFeEntrada = dataEmissaoNFeEntrada;
	}

	public String getDescricaoErroStatusNota() {
		return descricaoErroStatusNota;
	}

	@FTFfield(tamanho = 500, tipo = "char", ordem = 16)
	public void setDescricaoErroStatusNota(String descricaoErroStatusNota) {
		this.descricaoErroStatusNota = descricaoErroStatusNota;
	}

	public String getDataSaidaMercadoria() {
		return dataSaidaMercadoria;
	}

	@FTFfield(tamanho = 10, tipo = "char", ordem = 17)
	public void setDataSaidaMercadoria(String dataSaidaMercadoria) {
		this.dataSaidaMercadoria = dataSaidaMercadoria;
	}

	
	
	
}
