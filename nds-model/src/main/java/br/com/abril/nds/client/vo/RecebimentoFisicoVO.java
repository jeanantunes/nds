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
	private String precoDesconto;
	private String repartePrevisto;
	private String qtdPacote;
	private String qtdExemplar;
	private String diferenca;
	private String valorTotalCapa;
	private String valorTotalDesconto;
	
	private String pacotePadrao;
	
	private String peso;
	
	private String destacarValorNegativo;
	
	private String dataLancamento;
	private String dataRecolhimento;
	
	private String tipoLancamento;
	
	private String edicaoItemNotaPermitida;
	private String edicaoItemRecFisicoPermitida;
	
	private boolean produtoSemCadastro;

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
	public String getValorTotalCapa() {
		return valorTotalCapa;
	}

	/**
	 * Atribuí valorTotal
	 * @param valorTotal 
	 */
	public void setValorTotalCapa(String valorTotalCapa) {
		this.valorTotalCapa = valorTotalCapa;
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

	/**
	 * Obtém dataLancamento
	 *
	 * @return String
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * Atribuí dataLancamento
	 * @param dataLancamento 
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * Obtém dataRecolhimento
	 *
	 * @return String
	 */
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * Atribuí dataRecolhimento
	 * @param dataRecolhimento 
	 */
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * Obtém tipoLancamento
	 *
	 * @return String
	 */
	public String getTipoLancamento() {
		return tipoLancamento;
	}

	/**
	 * Atribuí tipoLancamento
	 * @param tipoLancamento 
	 */
	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	/**
	 * Obtém pacotePadrao
	 *
	 * @return String
	 */
	public String getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * Atribuí pacotePadrao
	 * @param pacotePadrao 
	 */
	public void setPacotePadrao(String pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * Obtém peso
	 *
	 * @return String
	 */
	public String getPeso() {
		return peso;
	}

	/**
	 * Atribuí peso
	 * @param peso 
	 */
	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public String getValorTotalDesconto() {
		return valorTotalDesconto;
	}

	public void setValorTotalDesconto(String valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	public boolean isProdutoSemCadastro() {
		return produtoSemCadastro;
	}

	public void setProdutoSemCadastro(boolean produtoSemCadastro) {
		this.produtoSemCadastro = produtoSemCadastro;
	}
	
	
}
