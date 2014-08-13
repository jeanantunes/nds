package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;


/**
 * Value Object para chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Exportable
public class ChamadaoVO {
	
	@Export(label = "Código", alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 2, widthPercent = 25)
	private String produto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 3, widthPercent = 5)
	private String edicao;
	
	@Export(label = "Brinde", alignment = Alignment.RIGHT, exhibitionOrder = 4, widthPercent = 5)
	private String brinde;
	
	@Export(label = "Preço Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 5)
	private String precoVenda;
	
	@Export(label = "Preço Desconto R$", alignment = Alignment.RIGHT, exhibitionOrder = 6)
	private String precoDesconto;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 7, widthPercent = 5)
	private String reparte;
	
	@Export(label = "Fornecedor", alignment = Alignment.LEFT, exhibitionOrder = 8)
	private String fornecedor;
	
	@Export(label = "Recolhimento", alignment = Alignment.CENTER, exhibitionOrder = 9)
	private String dataRecolhimento;
	
	@Export(label = "Valor R$", alignment = Alignment.RIGHT, exhibitionOrder = 10)
	private String valorTotal;
	
	@Export(label = "Valor c/ desconto R$", alignment = Alignment.RIGHT, exhibitionOrder = 11)
	private String valorTotalDesconto;
	
	private String idLancamento;
	
	private boolean checked;

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the produto
	 */
	public String getProduto() {
		return produto;
	}

	/**
	 * @param produto the produto to set
	 */
	public void setProduto(String produto) {
		this.produto = produto;
	}

	/**
	 * @return the edicao
	 */
	public String getEdicao() {
		return edicao;
	}

	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	
	/**
	 * @return the brinde
	 */
	public String getBrinde() {
		return brinde;
	}

	/**
	 * @param brinde the brinde to set
	 */
	public void setBrinde(String brinde) {
		this.brinde = brinde;
	}

	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the precoDesconto
	 */
	public String getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	/**
	 * @return the reparte
	 */
	public String getReparte() {
		return reparte;
	}

	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}

	/**
	 * @return the fornecedor
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
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
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the idLancamento
	 */
	public String getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(String idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the valorTotalDesconto
	 */
	public String getValorTotalDesconto() {
		return valorTotalDesconto;
	}

	/**
	 * @param valorTotalDesconto the valorTotalDesconto to set
	 */
	public void setValorTotalDesconto(String valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
