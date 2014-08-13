package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * 
 * Classe responsável por armazenar os valores referente a 
 * consulta de resumo de edições expedidas agrupadas por box.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class ResumoExpedicaoBoxVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private String reparte;
	
	@Export(label = "Valor Faturado R$", alignment = Alignment.RIGHT, exhibitionOrder = 7)
	private String valorFaturado;

	@Export(label = "Data Lançamento", exhibitionOrder = 1)
	private String dataLancamento;
	
	@Export(label = "Box", exhibitionOrder = 2)
	private String codigoBox;
	
	@Export(label = "Nome do Box", exhibitionOrder = 3)
	private String descricaoBox;
	
	@Export(label = "Qtde Produto", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String qntProduto;
	
	@Export(label = "Diferença", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private String qntDiferenca;

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
	 * @return the codigoBox
	 */
	public String getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the descricaoBox
	 */
	public String getDescricaoBox() {
		return descricaoBox;
	}

	/**
	 * @param descricaoBox the descricaoBox to set
	 */
	public void setDescricaoBox(String descricaoBox) {
		this.descricaoBox = descricaoBox;
	}

	/**
	 * @return the qntProduto
	 */
	public String getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(String qntProduto) {
		this.qntProduto = qntProduto;
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

}
