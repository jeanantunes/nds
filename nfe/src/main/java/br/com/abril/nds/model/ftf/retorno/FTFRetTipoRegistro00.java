package br.com.abril.nds.model.ftf.retorno;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;

public class FTFRetTipoRegistro00 extends FTFBaseDTO {

	private String tipoRegistro;
	private String CNPJEstabelecimentoEmissor;
	private String nomeArquivoEnviado;
	private String codigoSistemaOrigem;
	private String dataGeracaoArquivo;
	private String nomeArquivoEnviadoGeradoSistema;
	
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@FTFfield(tamanho = 1, tipo = "char", ordem = 1)
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getCNPJEstabelecimentoEmissor() {
		return CNPJEstabelecimentoEmissor;
	}
	
	@FTFfield(tamanho = 14, tipo = "char", ordem = 2)
	public void setCNPJEstabelecimentoEmissor(String cNPJEstabelecimentoEmissor) {
		CNPJEstabelecimentoEmissor = cNPJEstabelecimentoEmissor;
	}
	public String getNomeArquivoEnviado() {
		return nomeArquivoEnviado;
	}
	
	@FTFfield(tamanho = 20, tipo = "char", ordem = 3)
	public void setNomeArquivoEnviado(String nomeArquivoEnviado) {
		this.nomeArquivoEnviado = nomeArquivoEnviado;
	}
	public String getCodigoSistemaOrigem() {
		return codigoSistemaOrigem;
	}
	
	@FTFfield(tamanho = 3, tipo = "char", ordem = 4)
	public void setCodigoSistemaOrigem(String codigoSistemaOrigem) {
		this.codigoSistemaOrigem = codigoSistemaOrigem;
	}
	public String getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}
	
	@FTFfield(tamanho = 19, tipo = "char", ordem = 5)
	public void setDataGeracaoArquivo(String dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}
	public String getNomeArquivoEnviadoGeradoSistema() {
		return nomeArquivoEnviadoGeradoSistema;
	}
	
	@FTFfield(tamanho = 50, tipo = "char", ordem = 6)
	public void setNomeArquivoEnviadoGeradoSistema(
			String nomeArquivoEnviadoGeradoSistema) {
		this.nomeArquivoEnviadoGeradoSistema = nomeArquivoEnviadoGeradoSistema;
	}
	
	
	
}
