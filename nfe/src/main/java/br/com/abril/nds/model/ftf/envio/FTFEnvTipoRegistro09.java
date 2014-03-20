package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro09 extends FTFBaseDTO implements FTFCommons{
	
	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "9";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codEstabelecimentoEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEstabelecimentoEmissor;

	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;

	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=14, tipo="number", ordem=7)
	private String qtdePedidos;
	
	@FTFfield(tamanho=14, tipo="numeric", ordem=8)
	private String qtdeRegistros;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
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

	public String getCodEstabelecimentoEmissor() {
		return codEstabelecimentoEmissor;
	}

	public void setCodEstabelecimentoEmissor(String codEstabelecimentoEmissor) {
		this.codEstabelecimentoEmissor = codEstabelecimentoEmissor;
	}

	public String getCnpjEstabelecimentoEmissor() {
		return cnpjEstabelecimentoEmissor;
	}

	public void setCnpjEstabelecimentoEmissor(String cnpjEstabelecimentoEmissor) {
		this.cnpjEstabelecimentoEmissor = cnpjEstabelecimentoEmissor != null ? cnpjEstabelecimentoEmissor.replaceAll("\\D+","") : null;
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
	
}
