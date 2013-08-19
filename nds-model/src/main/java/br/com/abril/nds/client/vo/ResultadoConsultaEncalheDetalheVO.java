package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoConsultaEncalheDetalheVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConsultaEncalheDetalheVO>> tableModel;
	
	private Date dataOperacao;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long numeroEdicao;

	/**
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
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
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel<CellModelKeyValue<ConsultaEncalheDetalheVO>> getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(
			TableModel<CellModelKeyValue<ConsultaEncalheDetalheVO>> tableModel) {
		this.tableModel = tableModel;
	}
	
}
