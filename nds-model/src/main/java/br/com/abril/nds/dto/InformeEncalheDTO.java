package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.util.CurrencyUtil;

/**
 * 
 * @author Diego Fernandes
 *
 */
public class InformeEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2161743590665713176L;
	
	
	private Long idLancamento;
	
	private Long idProdutoEdicao;
	
	private Integer sequenciaMatriz;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private String chamadaCapa;
	
	private int pacotePadrao;
	
	private String codigoDeBarras;
	
	private BigDecimal precoVenda;
	
	private BigDecimal desconto;
	
	private BigDecimal precoDesconto;
	
	private Date dataLancamento;
	
	private Date dataRecolhimento;
	
	private Date dataRecolhimentoParcialFinal;
	
	private Integer seqCapa;
	
	private Date dataRecolhimentoFinal;
	
	private boolean brinde;
	
	private String nomeEditor;
	
	private boolean imagem;
	
	private String precoVendaFormatado;
	
	private String precoDescontoFormatado;
	
	private String tipoLancamentoParcial;

	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the sequenciaMatriz
	 */
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	/**
	 * @param sequenciaMatriz the sequenciaMatriz to set
	 */
	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
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
	 * @param nomeProduto the descricaoProduto to set
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
	
	public int getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the codigoDeBarras
	 */
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	/**
	 * @param codigoDeBarras the codigoDeBarras to set
	 */
	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	/**
	 * @return the precoVenda
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
		this.precoVendaFormatado = CurrencyUtil.formatarValor(precoVenda);
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	/**
	 * @return the precoDesconto
	 */
	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
		this.precoDescontoFormatado = CurrencyUtil.formatarValor(precoDesconto);
	}

	/**
	 * @return the chamadaCapa
	 */
	public String getChamadaCapa() {
		return chamadaCapa;
	}

	/**
	 * @param chamadaCapa the chamadaCapa to set
	 */
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	/**
	 * @return the dataRecolhimentoFinal
	 */
	public Date getDataRecolhimentoParcialFinal() {
		return dataRecolhimentoParcialFinal;
	}

	/**
	 * @param dataRecolhimentoFinal the dataRecolhimentoFinal to set
	 */
	public void setDataRecolhimentoParcialFinal(Date dataRecolhimentoParcialFinal) {
		this.dataRecolhimentoParcialFinal = dataRecolhimentoParcialFinal;
	}

	/**
	 * @return the seqCapa
	 */
	public Integer getSeqCapa() {
		return seqCapa;
	}

	/**
	 * @param seqCapa the seqCapa to set
	 */
	public void setSeqCapa(Integer seqCapa) {
		this.seqCapa = seqCapa;
	}

	public Date getDataRecolhimentoFinal() {
		return dataRecolhimentoFinal;
	}

	public void setDataRecolhimentoFinal(Date dataRecolhimentoFinal) {
		this.dataRecolhimentoFinal = dataRecolhimentoFinal;
	}

	public boolean isBrinde() {
		return brinde;
	}

	public void setBrinde(boolean brinde) {
		this.brinde = brinde;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public boolean isImagem() {
		return imagem;
	}

	public void setImagem(boolean imagem) {
		this.imagem = imagem;
	}

	/**
	 * @return the precoVendaFormatado
	 */
	public String getPrecoVendaFormatado() {
		return precoVendaFormatado;
	}

	/**
	 * @return the precoDescontoFormatado
	 */
	public String getPrecoDescontoFormatado() {
		return precoDescontoFormatado;
	}

	/**
	 * @return the tipoLancamentoParcial
	 */
	public String getTipoLancamentoParcial() {
		return tipoLancamentoParcial;
	}

	/**
	 * @param tipoLancamentoParcial the tipoLancamentoParcial to set
	 */
	public void setTipoLancamentoParcial(String tipoLancamentoParcial) {
		
        this.tipoLancamentoParcial = tipoLancamentoParcial != null ? tipoLancamentoParcial : "N";
	}
}
