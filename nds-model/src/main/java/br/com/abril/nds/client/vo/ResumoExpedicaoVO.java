package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.export.ColumnType;
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
public class ResumoExpedicaoVO implements Serializable {
	
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
	
	@Export(label = "Preço Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 4, columnType=ColumnType.DECIMAL)
	private String precoCapa;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger reparte;
	
	@Export(label = "Valor Faturado R$", alignment = Alignment.RIGHT, exhibitionOrder = 7, columnType=ColumnType.DECIMAL)
	private String valorFaturado;
	
	@Export(label = "Diferença", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger qntDiferenca;
	
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
	public BigInteger getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
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
	public BigInteger getQntDiferenca() {
		return qntDiferenca;
	}
	/**
	 * @param qntDiferenca the qntDiferenca to set
	 */
	public void setQntDiferenca(BigInteger qntDiferenca) {
		this.qntDiferenca = qntDiferenca;
	}
	
	
}

