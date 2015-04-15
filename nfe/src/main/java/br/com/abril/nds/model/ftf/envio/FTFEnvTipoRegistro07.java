package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro07 extends FTFBaseDTO implements FTFCommons {
	
	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "7";
	
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
	
	@FTFfield(tamanho=13, tipo="numeric", ordem=7)
	private String codigoEANLocalEntrega;
	
	@FTFfield(tamanho=13, tipo="numeric", ordem=8)
	private String codigoEANLocalCobranca;
	
	@FTFfield(tamanho=15, tipo="char", ordem=9)
	private String numeroPrimeiroPedidoCompraCliente;
	
	@FTFfield(tamanho=15, tipo="char", ordem=10)
	private String numeroSegundoPedidoCompraCliente;
	
	@FTFfield(tamanho=15, tipo="char", ordem=11)
	private String numeroTerceiroPedidoCompraCliente;
	
	@FTFfield(tamanho=10, tipo="char", ordem=12)
	private String dataPrevistaEntregaProdutos;
	
	@FTFfield(tamanho=1, tipo="numeric", ordem=13)
	private String indicadorEmpresaEDI;
	
	@FTFfield(tamanho=13, tipo="numeric", ordem=14)
	private String codigoEANEmissor;
	
	@FTFfield(tamanho=13, tipo="numeric", ordem=15)
	private String codigoEANDestinatario;
	
	@FTFfield(tamanho=10, tipo="char", ordem=16)
	private String extensaoArquivoEDI;

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

	public String getCodigoEANLocalEntrega() {
		return codigoEANLocalEntrega;
	}

	public void setCodigoEANLocalEntrega(String codigoEANLocalEntrega) {
		this.codigoEANLocalEntrega = codigoEANLocalEntrega;
	}

	public String getCodigoEANLocalCobranca() {
		return codigoEANLocalCobranca;
	}

	public void setCodigoEANLocalCobranca(String codigoEANLocalCobranca) {
		this.codigoEANLocalCobranca = codigoEANLocalCobranca;
	}

	public String getNumeroPrimeiroPedidoCompraCliente() {
		return numeroPrimeiroPedidoCompraCliente;
	}

	public void setNumeroPrimeiroPedidoCompraCliente(
			String numeroPrimeiroPedidoCompraCliente) {
		this.numeroPrimeiroPedidoCompraCliente = numeroPrimeiroPedidoCompraCliente;
	}

	public String getNumeroSegundoPedidoCompraCliente() {
		return numeroSegundoPedidoCompraCliente;
	}

	public void setNumeroSegundoPedidoCompraCliente(
			String numeroSegundoPedidoCompraCliente) {
		this.numeroSegundoPedidoCompraCliente = numeroSegundoPedidoCompraCliente;
	}

	public String getNumeroTerceiroPedidoCompraCliente() {
		return numeroTerceiroPedidoCompraCliente;
	}

	public void setNumeroTerceiroPedidoCompraCliente(
			String numeroTerceiroPedidoCompraCliente) {
		this.numeroTerceiroPedidoCompraCliente = numeroTerceiroPedidoCompraCliente;
	}

	public String getDataPrevistaEntregaProdutos() {
		return dataPrevistaEntregaProdutos;
	}

	public void setDataPrevistaEntregaProdutos(String dataPrevistaEntregaProdutos) {
		this.dataPrevistaEntregaProdutos = dataPrevistaEntregaProdutos;
	}

	public String getIndicadorEmpresaEDI() {
		return indicadorEmpresaEDI;
	}

	public void setIndicadorEmpresaEDI(String indicadorEmpresaEDI) {
		this.indicadorEmpresaEDI = indicadorEmpresaEDI;
	}

	public String getCodigoEANEmissor() {
		return codigoEANEmissor;
	}

	public void setCodigoEANEmissor(String codigoEANEmissor) {
		this.codigoEANEmissor = codigoEANEmissor;
	}

	public String getCodigoEANDestinatario() {
		return codigoEANDestinatario;
	}

	public void setCodigoEANDestinatario(String codigoEANDestinatario) {
		this.codigoEANDestinatario = codigoEANDestinatario;
	}

	public String getExtensaoArquivoEDI() {
		return extensaoArquivoEDI;
	}

	public void setExtensaoArquivoEDI(String extensaoArquivoEDI) {
		this.extensaoArquivoEDI = extensaoArquivoEDI;
	}
	
	@Override
	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora;
	}

	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
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