package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class ProdutoEdicaoDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer sequenciaMatriz;
	private String codigoDeBarras;
	private Long numeroEdicao;
	private BigDecimal precoVenda;
	private BigDecimal desconto;
	private int pacotePadrao;
	private int peb;
	private BigDecimal precoCusto;
	private BigDecimal peso;
	private String codigoProduto;
	private String nomeProduto;
	private boolean possuiBrinde;
	private BigDecimal expectativaVenda;
	private boolean permiteValeDesconto;
	private boolean parcial;
	private Integer dia;
	private Date dataRecolhimentoDistribuidor;
	
	// Usados na listagem de Edições:
	private Date dataLancamento;
	private StatusLancamento situacaoLancamento;
	private String nomeFornecedor;
	private TipoLancamento tipoLancamento;
	
	
	/**
	 * Tipo de chamada de encalhe deste produtoEdicao
	 */
	private TipoChamadaEncalhe tipoChamadaEncalhe;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}
	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}
	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	public int getPeb() {
		return peb;
	}
	public void setPeb(int peb) {
		this.peb = peb;
	}
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}
	public BigDecimal getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}
	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}
	public BigDecimal getExpectativaVenda() {
		return expectativaVenda;
	}
	public void setExpectativaVenda(BigDecimal expectativaVenda) {
		this.expectativaVenda = expectativaVenda;
	}
	public boolean isPermiteValeDesconto() {
		return permiteValeDesconto;
	}
	public void setPermiteValeDesconto(boolean permiteValeDesconto) {
		this.permiteValeDesconto = permiteValeDesconto;
	}
	public boolean isParcial() {
		return parcial;
	}
	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	public TipoChamadaEncalhe getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}
	public void setTipoChamadaEncalhe(TipoChamadaEncalhe tipoChamadaEncalhe) {
		this.tipoChamadaEncalhe = tipoChamadaEncalhe;
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
	 * @return the situacaoLancamento
	 */
	public StatusLancamento getSituacaoLancamento() {
		return situacaoLancamento;
	}
	
	/**
	 * @param situacaoLancamento
	 *            the situacaoLancamento to set
	 */
	public void setSituacaoLancamento(StatusLancamento situacaoLancamento) {
		this.situacaoLancamento = situacaoLancamento;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor
	 *            the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the tipoLancamento
	 */
	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	/**
	 * @param tipoLancamento
	 *            the tipoLancamento to set
	 */
	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
}
