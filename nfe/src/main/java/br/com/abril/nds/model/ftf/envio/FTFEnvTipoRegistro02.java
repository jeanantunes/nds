package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro02 extends FTFBaseDTO implements FTFCommons {
	
	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "2";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codigoCentroEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEmpresaEmissora;

	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;

	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=7)
	private String numItemPedido;
	
	@FTFfield(tamanho=3, tipo="char", ordem=8)
	private String codSistemaOrigemPedido;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=9)
	private String codProdutoOuServicoSistemaOrigem;
	
	@FTFfield(tamanho=15, tipo="numeric", ordem=10)
	private String qtdeProdutoOuServico;
	
	@FTFfield(tamanho=18, tipo="numeric", ordem=11)
	private String valorUnitario;
	
	@FTFfield(tamanho=15, tipo="numeric", ordem=12)
	private String valorBrutoTabela;
	
	@FTFfield(tamanho=15, tipo="numeric", ordem=13)
	private String valorPrecoSubstituicao;
	
	@FTFfield(tamanho=4, tipo="numeric", ordem=14)
	private String percentualDescontoItem;
	
	@FTFfield(tamanho=15, tipo="numeric", ordem=15)
	private String valorDescontoComercial;
	
	@FTFfield(tamanho=1, tipo="char", ordem=16)
	private String tipoUtilizacaoProduto;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=17)
	private String codTipoNaturecaOperacao;
	
	@FTFfield(tamanho=130, tipo="char", ordem=18)
	private String textoObservacoes;
	
	@FTFfield(tamanho=6, tipo="char", ordem=19)
	private String numEdicaoRevista;
	
	@FTFfield(tamanho=10, tipo="char", ordem=20)
	private String dataCompetencia;
	
	@FTFfield(tamanho=18, tipo="char", ordem=21)
	private String codBarrasProduto;
	
	@FTFfield(tamanho=1, tipo="numeric", ordem=22)
	private String indicadorProdutoServicoMaterial;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=23)
	private String codMaterialOuServicoCorporativo;
	
	@FTFfield(tamanho=4, tipo="numeric", ordem=24)
	private String novoCodigoTipoNaturezaOperacao;
	
	@FTFfield(tamanho=90, tipo="char", ordem=25)
	private String descricaoProduto;
	
	@FTFfield(tamanho=14, tipo="char", ordem=26)
	private String codEanProduto;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodigoCentroEmissor() {
		return codigoCentroEmissor;
	}

	public void setCodigoCentroEmissor(String codigoCentroEmissor) {
		this.codigoCentroEmissor = codigoCentroEmissor;
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

	public String getNumItemPedido() {
		return numItemPedido;
	}

	public void setNumItemPedido(String numItemPedido) {
		this.numItemPedido = numItemPedido;
	}

	public String getCodSistemaOrigemPedido() {
		return codSistemaOrigemPedido;
	}

	public void setCodSistemaOrigemPedido(String codSistemaOrigemPedido) {
		this.codSistemaOrigemPedido = codSistemaOrigemPedido;
	}

	public String getCodProdutoOuServicoSistemaOrigem() {
		return codProdutoOuServicoSistemaOrigem;
	}

	public void setCodProdutoOuServicoSistemaOrigem(
			String codProdutoOuServicoSistemaOrigem) {
		this.codProdutoOuServicoSistemaOrigem = codProdutoOuServicoSistemaOrigem;
	}

	public String getQtdeProdutoOuServico() {
		return qtdeProdutoOuServico;
	}

	public void setQtdeProdutoOuServico(String qtdeProdutoOuServico) {
		this.qtdeProdutoOuServico = qtdeProdutoOuServico;
	}

	public String getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public String getValorBrutoTabela() {
		return valorBrutoTabela;
	}

	public void setValorBrutoTabela(String valorBrutoTabela) {
		this.valorBrutoTabela = valorBrutoTabela;
	}

	public String getValorPrecoSubstituicao() {
		return valorPrecoSubstituicao;
	}

	public void setValorPrecoSubstituicao(String valorPrecoSubstituicao) {
		this.valorPrecoSubstituicao = valorPrecoSubstituicao;
	}

	public String getPercentualDescontoItem() {
		return percentualDescontoItem;
	}

	public void setPercentualDescontoItem(String percentualDescontoItem) {
		this.percentualDescontoItem = percentualDescontoItem;
	}

	public String getValorDescontoComercial() {
		return valorDescontoComercial;
	}

	public void setValorDescontoComercial(String valorDescontoComercial) {
		this.valorDescontoComercial = valorDescontoComercial;
	}

	public String getTipoUtilizacaoProduto() {
		return tipoUtilizacaoProduto;
	}

	public void setTipoUtilizacaoProduto(String tipoUtilizacaoProduto) {
		this.tipoUtilizacaoProduto = tipoUtilizacaoProduto;
	}

	public String getCodTipoNaturecaOperacao() {
		return codTipoNaturecaOperacao;
	}

	public void setCodTipoNaturecaOperacao(String codTipoNaturecaOperacao) {
		this.codTipoNaturecaOperacao = codTipoNaturecaOperacao;
	}

	public String getTextoObservacoes() {
		return textoObservacoes;
	}

	public void setTextoObservacoes(String textoObservacoes) {
		this.textoObservacoes = textoObservacoes;
	}

	public String getNumEdicaoRevista() {
		return numEdicaoRevista;
	}

	public void setNumEdicaoRevista(String numEdicaoRevista) {
		this.numEdicaoRevista = numEdicaoRevista;
	}

	public String getDataCompetencia() {
		return dataCompetencia;
	}

	public void setDataCompetencia(String dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public String getCodBarrasProduto() {
		return codBarrasProduto;
	}

	public void setCodBarrasProduto(String codBarrasProduto) {
		this.codBarrasProduto = codBarrasProduto;
	}

	public String getIndicadorProdutoServicoMaterial() {
		return indicadorProdutoServicoMaterial;
	}

	public void setIndicadorProdutoServicoMaterial(
			String indicadorProdutoServicoMaterial) {
		this.indicadorProdutoServicoMaterial = indicadorProdutoServicoMaterial;
	}

	public String getCodMaterialOuServicoCorporativo() {
		return codMaterialOuServicoCorporativo;
	}

	public void setCodMaterialOuServicoCorporativo(
			String codMaterialOuServicoCorporativo) {
		this.codMaterialOuServicoCorporativo = codMaterialOuServicoCorporativo;
	}

	public String getNovoCodigoTipoNaturezaOperacao() {
		return novoCodigoTipoNaturezaOperacao;
	}

	public void setNovoCodigoTipoNaturezaOperacao(
			String novoCodigoTipoNaturezaOperacao) {
		this.novoCodigoTipoNaturezaOperacao = novoCodigoTipoNaturezaOperacao;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public String getCodEanProduto() {
		return codEanProduto;
	}

	public void setCodEanProduto(String codEanProduto) {
		this.codEanProduto = codEanProduto;
	}

	
	
}