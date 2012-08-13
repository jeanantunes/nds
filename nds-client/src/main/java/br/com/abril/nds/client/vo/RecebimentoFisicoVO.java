package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class RecebimentoFisicoVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private int lineId;
	
	private Long idItemNota;
	private Long idItemRecebimentoFisico;
	
	private String codigo;
	private String nomeProduto;
	private String edicao;
	private String precoCapa;
	private String repartePrevisto;
	private String qtdPacote;
	private String qtdExemplar;
	private String diferenca;
	private String valorTotal;
	private String destacarValorNegativo;
	
	private String edicaoItemNotaPermitida;
	private String edicaoItemRecFisicoPermitida;

	/**
	 * Obtém codigo
	 *
	 * @return String
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Atribuí codigo
	 * @param codigo 
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	 * Obtém edicao
	 *
	 * @return String
	 */
	public String getEdicao() {
		return edicao;
	}

	/**
	 * Atribuí edicao
	 * @param edicao 
	 */
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	/**
	 * Obtém precoCapa
	 *
	 * @return String
	 */
	public String getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * Atribuí precoCapa
	 * @param precoCapa 
	 */
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	/**
	 * Obtém repartePrevisto
	 *
	 * @return String
	 */
	public String getRepartePrevisto() {
		return repartePrevisto;
	}

	/**
	 * Atribuí repartePrevisto
	 * @param repartePrevisto 
	 */
	public void setRepartePrevisto(String repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	/**
	 * Obtém qtdPacote
	 *
	 * @return String
	 */
	public String getQtdPacote() {
		return qtdPacote;
	}

	/**
	 * Atribuí qtdPacote
	 * @param qtdPacote 
	 */
	public void setQtdPacote(String qtdPacote) {
		this.qtdPacote = qtdPacote;
	}

	/**
	 * Obtém qtdExemplar
	 *
	 * @return String
	 */
	public String getQtdExemplar() {
		return qtdExemplar;
	}

	/**
	 * Atribuí qtdExemplar
	 * @param qtdExemplar 
	 */
	public void setQtdExemplar(String qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}

	/**
	 * Obtém diferenca
	 *
	 * @return String
	 */
	public String getDiferenca() {
		return diferenca;
	}

	/**
	 * Atribuí diferenca
	 * @param diferenca 
	 */
	public void setDiferenca(String diferenca) {
		this.diferenca = diferenca;
	}

	/**
	 * Obtém valorTotal
	 *
	 * @return String
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * Atribuí valorTotal
	 * @param valorTotal 
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * Obtém destacarValorNegativo
	 *
	 * @return String
	 */
	public String getDestacarValorNegativo() {
		return destacarValorNegativo;
	}

	/**
	 * Atribuí destacarValorNegativo
	 * @param destacarValorNegativo 
	 */
	public void setDestacarValorNegativo(String destacarValorNegativo) {
		this.destacarValorNegativo = destacarValorNegativo;
	}

	/**
	 * Obtém edicaoItemNotaPermitida
	 *
	 * @return String
	 */
	public String getEdicaoItemNotaPermitida() {
		return edicaoItemNotaPermitida;
	}

	/**
	 * Atribuí edicaoItemNotaPermitida
	 * @param edicaoItemNotaPermitida 
	 */
	public void setEdicaoItemNotaPermitida(String edicaoItemNotaPermitida) {
		this.edicaoItemNotaPermitida = edicaoItemNotaPermitida;
	}

	/**
	 * Obtém edicaoItemRecFisicoPermitida
	 *
	 * @return String
	 */
	public String getEdicaoItemRecFisicoPermitida() {
		return edicaoItemRecFisicoPermitida;
	}

	/**
	 * Atribuí edicaoItemRecFisicoPermitida
	 * @param edicaoItemRecFisicoPermitida 
	 */
	public void setEdicaoItemRecFisicoPermitida(String edicaoItemRecFisicoPermitida) {
		this.edicaoItemRecFisicoPermitida = edicaoItemRecFisicoPermitida;
	}

	/**
	 * Obtém lineId
	 *
	 * @return int
	 */
	public int getLineId() {
		return lineId;
	}

	/**
	 * Atribuí lineId
	 * @param lineId 
	 */
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	/**
	 * Obtém idItemNota
	 *
	 * @return Long
	 */
	public Long getIdItemNota() {
		return idItemNota;
	}

	/**
	 * Atribuí idItemNota
	 * @param idItemNota 
	 */
	public void setIdItemNota(Long idItemNota) {
		this.idItemNota = idItemNota;
	}

	/**
	 * Obtém idItemRecebimentoFisico
	 *
	 * @return Long
	 */
	public Long getIdItemRecebimentoFisico() {
		return idItemRecebimentoFisico;
	}

	/**
	 * Atribuí idItemRecebimentoFisico
	 * @param idItemRecebimentoFisico 
	 */
	public void setIdItemRecebimentoFisico(Long idItemRecebimentoFisico) {
		this.idItemRecebimentoFisico = idItemRecebimentoFisico;
	}
	
	
}
