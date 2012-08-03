package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * 
 * Classe responsável por armazenar os valores referente a 
 * consulta de resumo de edições expedidas agrupadas por produto.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class ResumoExpedicaoDetalheVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String dataLancamento;
	
	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", exhibitionOrder = 2)
	private String descricaoProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 3)
	private String edicaoProduto;
	
	@Export(label = "Preço Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapa;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private String reparte;
	
	@Export(label = "Valor Faturado R$", alignment = Alignment.RIGHT, exhibitionOrder = 7)
	private String valorFaturado;
	
	@Export(label = "Diferença", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private String qntDiferenca;
	
	@Export(label = "Fornecedor", alignment = Alignment.CENTER, exhibitionOrder = 8)
	private String nomeFornecedor;
	
	
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}
	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	/**
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}
	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
	/**
	 * @return the edicaoProduto
	 */
	public String getEdicaoProduto() {
		return edicaoProduto;
	}
	/**
	 * @param edicaoProduto the edicaoProduto to set
	 */
	public void setEdicaoProduto(String edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
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
	 * @return the valorFaturado
	 */
	public String getValorFaturado() {
		return valorFaturado;
	}
	/**
	 * @param valorFaturado the valorFaturado to set
	 */
	public void setValorFaturado(String valorFaturado) {
		this.valorFaturado = valorFaturado;
	}
	/**
	 * @return the qntDiferenca
	 */
	public String getQntDiferenca() {
		return qntDiferenca;
	}
	/**
	 * @param qntDiferenca the qntDiferenca to set
	 */
	public void setQntDiferenca(String qntDiferenca) {
		this.qntDiferenca = qntDiferenca;
	}
	/**
	 * Obtém nomeFornecedor
	 *
	 * @return String
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	/**
	 * Atribuí nomeFornecedor
	 * @param nomeFornecedor 
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	
}

