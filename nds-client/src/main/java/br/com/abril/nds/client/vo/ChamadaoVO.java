package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;


/**
 * Value Object para chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Exportable
public class ChamadaoVO {
	
	@Export(label = "Código", alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String produto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 3)
	private String edicao;
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapa;
	
	@Export(label = "Desconto R$", alignment = Alignment.RIGHT, exhibitionOrder = 5)
	private String valorDesconto;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private String reparte;
	
	@Export(label = "Fornecedor", alignment = Alignment.LEFT, exhibitionOrder = 7)
	private String fornecedor;
	
	@Export(label = "Recolhimento", alignment = Alignment.CENTER, exhibitionOrder = 8)
	private String dataRecolhimento;
	
	@Export(label = "Valor R$", alignment = Alignment.RIGHT, exhibitionOrder = 9)
	private String valorTotal;
	
	private String idLancamento;

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
	 * @return the precoCapa
	 */
	public String getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	/**
	 * @return the valorDesconto
	 */
	public String getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
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

}
