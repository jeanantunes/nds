package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;

@Exportable
public class ConsultaEncalheVO {

	private String idProdutoEdicao;
	
	private String dataRecolhimento;

	private String dataMovimento;

	private String idFornecedor;

	private String idCota;

	@Export(label="Código")
	private String codigoProduto;
	
	@Export(label="Produto", widthPercent=18)
	private String nomeProduto;
	
	@Export(label="Edição")
	private String numeroEdicao;
	
	@Export(label="Preço Capa R$")
	private String precoVenda;
	
	@Export(label="Preço com Desc. R$")
	private String precoComDesconto;
	
	@Export(label="Reparte")
	private String reparte;
	
	@Export(label="Encalhe")
	private String encalhe;
	
	@Export(label="Venda")
	@Footer(type = FooterType.COUNT)
	private String venda;
	
	@Export(label="Fornecedor")
	private String fornecedor;
	
	@Export(label="Rep Capa")
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String valorRepartePrecoCapa;
	
	@Export(label="Encalhe Valor R$")
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String valorEncalhePrecoCapa;
	
	@Export(label="Venda Capa")
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String valorVenda;
	
	@Export(label="Rep Desc.")
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String valorReparteDesconto;

	@Export(label="Encalhe valor c/ Desc.")
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String valorEncalheDesconto;
	
	@Export(label="Venda c/ Desc")
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private String valorVendaDesc;
	
	@Export(label="Recolhimento")
	private String recolhimento;
	
	private String valorComDesconto;
	
	private String valor;
	
	private Integer totalQtdeVenda;

	private boolean indPossuiObservacaoConferenciaEncalhe;
	
	/**
	 * Obtém idProdutoEdicao
	 *
	 * @return String
	 */
	public String getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * Atribuí idProdutoEdicao
	 * @param idProdutoEdicao 
	 */
	public void setIdProdutoEdicao(String idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * Obtém codigoProduto
	 *
	 * @return String
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * Atribuí codigoProduto
	 * @param codigoProduto 
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * Obtém nomeProduto
	 *
	 * @return String
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * Atribuí nomeProduto
	 * @param nomeProduto 
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * Obtém numeroEdicao
	 *
	 * @return String
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * Atribuí numeroEdicao
	 * @param numeroEdicao 
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * Obtém precoVenda
	 *
	 * @return String
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * Atribuí precoVenda
	 * @param precoVenda 
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * Obtém precoComDesconto
	 *
	 * @return String
	 */
	public String getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * Atribuí precoComDesconto
	 * @param precoComDesconto 
	 */
	public void setPrecoComDesconto(String precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * Obtém reparte
	 *
	 * @return String
	 */
	public String getReparte() {
		return reparte;
	}

	/**
	 * Atribuí reparte
	 * @param reparte 
	 */
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}

	/**
	 * Obtém encalhe
	 *
	 * @return String
	 */
	public String getEncalhe() {
		return encalhe;
	}

	/**
	 * Atribuí encalhe
	 * @param encalhe 
	 */
	public void setEncalhe(String encalhe) {
		this.encalhe = encalhe;
	}
	
	public String getVenda() {
		return venda;
	}

	public void setVenda(String venda) {
		this.venda = venda;
	}

	/**
	 * Obtém fornecedor
	 *
	 * @return String
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * Atribuí fornecedor
	 * @param fornecedor 
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public String getValorRepartePrecoCapa() {
		return valorRepartePrecoCapa;
	}

	public void setValorRepartePrecoCapa(String valorRepartePrecoCapa) {
		this.valorRepartePrecoCapa = valorRepartePrecoCapa;
	}
	
	public String getValorEncalhePrecoCapa() {
		return valorEncalhePrecoCapa;
	}

	public void setValorEncalhePrecoCapa(String valorEncalhePrecoCapa) {
		this.valorEncalhePrecoCapa = valorEncalhePrecoCapa;
	}
	
	public String getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(String valorVenda) {
		this.valorVenda = valorVenda;
	}

	/**
	 * Obtém recolhimento
	 *
	 * @return String
	 */
	public String getRecolhimento() {
		return recolhimento;
	}

	/**
	 * Atribuí recolhimento
	 * @param recolhimento 
	 */
	public void setRecolhimento(String recolhimento) {
		this.recolhimento = recolhimento;
	}
	
	public String getValorVendaDesc() {
		return valorVendaDesc;
	}

	public void setValorVendaDesc(String valorVendaDesc) {
		this.valorVendaDesc = valorVendaDesc;
	}

	/**
	 * @return the valorComDesconto
	 */
	public String getValorComDesconto() {
		return valorComDesconto;
	}

	/**
	 * @param valorComDesconto the valorComDesconto to set
	 */
	public void setValorComDesconto(String valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}
	
	public String getValorReparteDesconto() {
		return valorReparteDesconto;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public Integer getTotalQtdeVenda() {
		return totalQtdeVenda;
	}

	public void setTotalQtdeVenda(Integer totalQtdeVenda) {
		this.totalQtdeVenda = totalQtdeVenda;
	}

	public void setValorReparteDesconto(String valorReparteDesconto) {
		this.valorReparteDesconto = valorReparteDesconto;
	}

	public String getValorEncalheDesconto() {
		return valorEncalheDesconto;
	}

	public void setValorEncalheDesconto(String valorEncalheDesconto) {
		this.valorEncalheDesconto = valorEncalheDesconto;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the dataMovimento
	 */
	public String getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * @return the idFornecedor
	 */
	public String getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(String idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the idCota
	 */
	public String getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(String idCota) {
		this.idCota = idCota;
	}
	
//	@Export(label = "Total Desc $", alignment= Alignment.RIGHT, exhibitionOrder = 11, columnType = ColumnType.MOEDA_QUATRO_CASAS, widthPercent = 12.5f)
//	@Footer(type = FooterType.SUM, columnType = ColumnType.MOEDA_QUATRO_CASAS)
//	public Integer getTotalDesconto() {
//		return totalDesconto;
//	}
//
//	public void setTotalDesconto(BigDecimal totalDesconto) {
//		this.totalDesconto = totalDesconto;
//		if(totalDesconto != null){
//			totalDescontoFormatado = CurrencyUtil.formatarValorQuatroCasas(totalDesconto);
//		}
//	}
	
	public boolean getIndPossuiObservacaoConferenciaEncalhe() {
		return indPossuiObservacaoConferenciaEncalhe;
	}

	public void setIndPossuiObservacaoConferenciaEncalhe(
			boolean indPossuiObservacaoConferenciaEncalhe) {
		this.indPossuiObservacaoConferenciaEncalhe = indPossuiObservacaoConferenciaEncalhe;
	}

	
}
