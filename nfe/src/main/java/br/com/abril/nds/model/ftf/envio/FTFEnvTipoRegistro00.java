package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro00 extends FTFBaseDTO implements FTFCommons {

	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro="0";
	
	@FTFfield(tamanho=2, tipo="char", ordem=2)
	private String codigoEstabelecimentoEmissor;

	@FTFfield(tamanho=14, tipo="char", ordem=3)
	private String cnpjEmpresaEmissora;

	@FTFfield(tamanho=11, tipo="char", ordem=4)
	private String codLocal;

	@FTFfield(tamanho=2, tipo="char", ordem=5)
	private String tipoPedido;

	@FTFfield(tamanho=8, tipo="char", ordem=6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=10, tipo="char", ordem=7)
	private String dataGeracao;
	
	@FTFfield(tamanho=20, tipo="char", ordem=8)
	private String nomeArquivo;
	
	@FTFfield(tamanho=14, tipo="char", ordem=9)
	private String qtdePedidos;
	
	@FTFfield(tamanho=14, tipo="char", ordem=10)
	private String qtdeRegistros;
	
	@FTFfield(tamanho=6, tipo="char", ordem=11)
	private String numSequencia;

	@FTFfield(tamanho=50, tipo="char", ordem=12)
	private String novoNomeArquivo;
	
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

	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
	}

	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora != null ? cnpjEmpresaEmissora.replaceAll("\\D+","") : null;
	}

	public String getCodLocal() {
		return codLocal;
	}

	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal;
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public String getNumeroDocOrigem() {
		return numeroDocOrigem;
	}

	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}
	
	public String getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(String dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getQtdePedidos() {
		return qtdePedidos;
	}

	public void setQtdePedidos(String qtdePedidos) {
		this.qtdePedidos = qtdePedidos;
	}

	public String getQtdeRegistros() {
		return qtdeRegistros;
	}

	public void setQtdeRegistros(String qtdeRegistros) {
		this.qtdeRegistros = qtdeRegistros;
	}

	public String getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(String numSequencia) {
		this.numSequencia = numSequencia;
	}

	public String getNovoNomeArquivo() {
		return novoNomeArquivo;
	}

	public void setNovoNomeArquivo(String novoNomeArquivo) {
		this.novoNomeArquivo = novoNomeArquivo;
	}
}