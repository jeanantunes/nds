package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
	private Long peso;
	private String codigoProduto;
	private String nomeProduto;
	
	private String descricaoProduto;
	private boolean possuiBrinde;
	private String descricaoBrinde;
	
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
	private String statusLancamento;
	private String statusSituacao;
	private String temBrinde;
	
	// Campos para cadastrar uma nova Edição:
	// codigoProduto;
	private String nomeComercialProduto;
	private String fase;
	private Integer numeroLancamento;	
	// numeroEdicao; pacotePadrao;
	private BigDecimal precoPrevisto;
	// precoVenda; (Real)
	private BigInteger repartePrevisto;
	private BigInteger repartePromocional;

	// codigoDeBarras
	private String codigoDeBarrasCorporativo;
	private String descricaoDesconto;
	// desconto;
	private String chamadaCapa;
	// parcial;	-- Regime de Recolhimento;
	// brinde;
	// peso;
	private float largura;
	private float comprimento;
	private float espessura;
	
	private String boletimInformativo;
	
	// Lancamento:
	// tipoLancamento;
	private Date dataLancamentoPrevisto;
	private Date dataRecolhimentoPrevisto;
	private Date dataRecolhimentoReal;
	
	private Integer semanaRecolhimento;
	
	private boolean origemInterface;
	
	
	private String editor;
	
	
	
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
	public Long getPeso() {
		return peso;
	}
	public void setPeso(Long peso) {
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
	
	/**
	 * @return the statusLancamento
	 */
	public String getStatusLancamento() {
		return this.statusLancamento;
	}
	public void setStatusLancamento(TipoLancamento statusLancamento) {
		this.statusLancamento = statusLancamento.getDescricao(); 
	}
	
	/**
	 * @return the situacaoLancamento
	 */
	public String getStatusSituacao() {
		return this.statusSituacao;
	}
	public void setStatusSituacao(StatusLancamento statusSituacao) {
		this.statusSituacao = statusSituacao.getDescricao();
	}
	
	/**
	 * 
	 * @return the possuiBrindeDescricao;
	 */
	public String getTemBrinde() {
		return this.temBrinde;
	}
	public void setTemBrinde(Boolean temBrinde) {
		this.temBrinde = temBrinde.booleanValue() ? "Sim" : "Não";
	}

	/**
	 * @return the nomeComercialProduto
	 */
	public String getNomeComercialProduto() {
		return nomeComercialProduto;
	}
	/**
	 * @param nomeComercialProduto the nomeComercialProduto to set
	 */
	public void setNomeComercialProduto(String nomeComercialProduto) {
		this.nomeComercialProduto = nomeComercialProduto;
	}

	/**
	 * @return the precoPrevisto
	 */
	public BigDecimal getPrecoPrevisto() {
		return precoPrevisto;
	}
	/**
	 * @param precoPrevisto the precoPrevisto to set
	 */
	public void setPrecoPrevisto(BigDecimal precoPrevisto) {
		this.precoPrevisto = precoPrevisto;
	}
	/**
	 * @return the dataLancamentoPrevisto
	 */
	public Date getDataLancamentoPrevisto() {
		return dataLancamentoPrevisto;
	}
	/**
	 * @param dataLancamentoPrevisto the dataLancamentoPrevisto to set
	 */
	public void setDataLancamentoPrevisto(Date dataLancamentoPrevisto) {
		this.dataLancamentoPrevisto = dataLancamentoPrevisto;
	}
	/**
	 * @return the repartePrevisto
	 */
	public BigInteger getRepartePrevisto() {
		return repartePrevisto;
	}
	/**
	 * @param repartePrevisto the repartePrevisto to set
	 */
	public void setRepartePrevisto(BigInteger repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	/**
	 * @return the repartePromocional
	 */
	public BigInteger getRepartePromocional() {
		return repartePromocional;
	}
	/**
	 * @param repartePromocional the repartePromocional to set
	 */
	public void setRepartePromocional(BigInteger repartePromocional) {
		this.repartePromocional = repartePromocional;
	}
	/**
	 * @return the codigoDeBarrasCorporativo
	 */
	public String getCodigoDeBarrasCorporativo() {
		return codigoDeBarrasCorporativo;
	}
	/**
	 * @param codigoDeBarrasCorporativo the codigoDeBarrasCorporativo to set
	 */
	public void setCodigoDeBarrasCorporativo(String codigoDeBarrasCorporativo) {
		this.codigoDeBarrasCorporativo = codigoDeBarrasCorporativo;
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
	 * @return the boletimInformativo
	 */
	public String getBoletimInformativo() {
		return boletimInformativo;
	}
	/**
	 * @param boletimInformativo the boletimInformativo to set
	 */
	public void setBoletimInformativo(String boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
	}
	/**
	 * @return the fase
	 */
	public String getFase() {
		return fase;
	}
	/**
	 * @param fase the fase to set
	 */
	public void setFase(String fase) {
		this.fase = fase;
	}
	/**
	 * @return the numeroLancamento
	 */
	public Integer getNumeroLancamento() {
		return numeroLancamento;
	}
	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}
	/**
	 * @return the largura
	 */
	public float getLargura() {
		return largura;
	}
	/**
	 * @param largura the largura to set
	 */
	public void setLargura(float largura) {
		this.largura = largura;
	}
	/**
	 * @return the comprimento
	 */
	public float getComprimento() {
		return comprimento;
	}
	/**
	 * @param comprimento the comprimento to set
	 */
	public void setComprimento(float comprimento) {
		this.comprimento = comprimento;
	}
	/**
	 * @return the espessura
	 */
	public float getEspessura() {
		return espessura;
	}
	/**
	 * @param espessura the espessura to set
	 */
	public void setEspessura(float espessura) {
		this.espessura = espessura;
	}
	/**
	 * @return the origemInterface
	 */
	public boolean isOrigemInterface() {
		return origemInterface;
	}
	/**
	 * @param origemInterface the origemInterface to set
	 */
	public void setOrigemInterface(boolean origemInterface) {
		this.origemInterface = origemInterface;
	}
	/**
	 * @return the descricaoDesconto
	 */
	public String getDescricaoDesconto() {
		return descricaoDesconto;
	}
	/**
	 * @param descricaoDesconto the descricaoDesconto to set
	 */
	public void setDescricaoDesconto(String descricaoDesconto) {
		this.descricaoDesconto = descricaoDesconto;
	}
	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	/**
	 * @return the dataRecolhimentoPrevisto
	 */
	public Date getDataRecolhimentoPrevisto() {
		return dataRecolhimentoPrevisto;
	}
	/**
	 * @param dataRecolhimentoPrevisto the dataRecolhimentoPrevisto to set
	 */
	public void setDataRecolhimentoPrevisto(Date dataRecolhimentoPrevisto) {
		this.dataRecolhimentoPrevisto = dataRecolhimentoPrevisto;
	}
	/**
	 * @return the dataRecolhimentoReal
	 */
	public Date getDataRecolhimentoReal() {
		return dataRecolhimentoReal;
	}
	/**
	 * @param dataRecolhimentoReal the dataRecolhimentoReal to set
	 */
	public void setDataRecolhimentoReal(Date dataRecolhimentoReal) {
		this.dataRecolhimentoReal = dataRecolhimentoReal;
	}
	/**
	 * @return the semanaRecolhimento
	 */
	public Integer getSemanaRecolhimento() {
		return semanaRecolhimento;
	}
	/**
	 * @param semanaRecolhimento the semanaRecolhimento to set
	 */
	public void setSemanaRecolhimento(Integer semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}
	/**
	 * @return the descricaoBrinde
	 */
	public String getDescricaoBrinde() {
		return descricaoBrinde;
	}
	/**
	 * @param descricaoBrinde the descricaoBrinde to set
	 */
	public void setDescricaoBrinde(String descricaoBrinde) {
		this.descricaoBrinde = descricaoBrinde;
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
	
}
