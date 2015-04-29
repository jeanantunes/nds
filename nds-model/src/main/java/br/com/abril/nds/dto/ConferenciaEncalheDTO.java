package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;

public class ConferenciaEncalheDTO implements Serializable {

	private static final long serialVersionUID = -6012294358522142934L;
	
	private Long idConferenciaEncalhe;
	
	private Long idProdutoEdicao;
	
	private String chamadaCapa;
	
	private String nomeFornecedor;
	
	private String nomeEditor;
	
	private int pacotePadrao;
	
	private boolean possuiBrinde;
	
	private boolean parcialNaoFinal;
	
	private boolean isContagemPacote;
	
	private boolean parcialCalculado;
	
	private boolean ocultarItem;
	
	/**
	 * O instante em que o objeto que representa conferência de encalhe
	 * foi conferido (ou criado).
	 */
	private long instanteConferido = System.currentTimeMillis();
	
	/**
	 * Utilizado como referência para o valor do reparte.
	 */
	private BigInteger qtdReparte;
	
	/**
	 * Quantidade apontada na conferência de encalhe
	 */	
	private BigInteger qtdExemplar;
	
	/**
	 * Também equivale a quantidade apontada na conferência de encalhe, porém 
	 * é um campo do tipo {@code String} devido a possibilidade do usuário digitar
	 * a quantidade acompanhada do sufixo "e" no recolhimento de produtos do
	 * tipo cromo, para assim "forçar" a contabilização deste por envelopes.
	 */
	private String qtdExemplarDaGrid;

	/**
	 *  Quantidade informada. Refere-se a qtde do item da nota fiscal de entrada da cota.
	 */
	private BigInteger qtdInformada;
	
	/**
	 * Preco Capa informado. Refere-se ao preco do item da nota fiscal de entrada da cota.
	 */
	private BigDecimal precoCapaInformado;
	
	
	private String codigoDeBarras;
	
	private Date dataRecolhimento;
	
	private Date dataConferencia;
	
	private Integer codigoSM;
	
	private String codigo;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal precoCapa;
	
	private BigDecimal desconto;
	
	private BigDecimal precoComDesconto;
	
	private BigDecimal valorTotal;
	
	private Integer dia;
	
	private String observacao;
	
	private Boolean juramentada;

	/**
	 * Tipo de chamada de encalhe deste produtoEdicao
	 */
	private TipoChamadaEncalhe tipoChamadaEncalhe;

	
	public long getInstanteConferido() {
		return instanteConferido;
	}

	public void setInstanteConferido(long instanteConferido) {
		this.instanteConferido = instanteConferido;
	}

	public Long getIdConferenciaEncalhe() {
		return idConferenciaEncalhe;
	}

	public void setIdConferenciaEncalhe(Long idConferenciaEncalhe) {
		this.idConferenciaEncalhe = idConferenciaEncalhe;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public BigInteger getQtdExemplar() {
		return qtdExemplar;
	}

	public void setQtdExemplar(BigInteger qtdExemplar) {
		
		this.qtdExemplar = qtdExemplar;
		
		this.instanteConferido = System.currentTimeMillis();
		
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public Date getDataConferencia() {
		return dataConferencia;
	}

	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}

	public Integer getCodigoSM() {
		return codigoSM;
	}

	public void setCodigoSM(Integer codigoSM) {
		this.codigoSM = codigoSM;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setJuramentada(Boolean juramentada) {
		this.juramentada = juramentada;
	}

	public BigInteger getQtdInformada() {
		return qtdInformada;
	}

	public void setQtdInformada(BigInteger qtdInformada) {
		this.qtdInformada = qtdInformada;
	}

	public String getChamadaCapa() {
		return chamadaCapa;
	}

	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public int getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}

	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}

	public TipoChamadaEncalhe getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}

	public void setTipoChamadaEncalhe(String tipoChamadaEncalhe) {
		if(tipoChamadaEncalhe != null && !tipoChamadaEncalhe.isEmpty()) {
			this.tipoChamadaEncalhe = TipoChamadaEncalhe.valueOf(tipoChamadaEncalhe);
		}
	}

	public BigDecimal getPrecoCapaInformado() {
		return precoCapaInformado;
	}

	public void setPrecoCapaInformado(BigDecimal precoCapaInformado) {
		this.precoCapaInformado = precoCapaInformado;
	}

	public boolean isParcialNaoFinal() {
		return parcialNaoFinal;
	}

	public void setParcialNaoFinal(boolean parcialNaoFinal) {
		this.parcialNaoFinal = parcialNaoFinal;
	}

	/**
	 * @return the isContagemPacote
	 */
	public boolean getIsContagemPacote() {
		return isContagemPacote;
	}

	/**
	 * @param isContagemPacote the isContagemPacote to set
	 */
	public void setContagemPacote(boolean isContagemPacote) {
		this.isContagemPacote = isContagemPacote;
	}

	public BigInteger getQtdReparte() {
		return qtdReparte;
	}

	public void setQtdReparte(BigInteger qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public Boolean getJuramentada() {
		return juramentada;
	}
	
	/**
	 * @return the parcialCalculado
	 */
	public boolean isParcialCalculado() {
		return parcialCalculado;
	}

	/**
	 * @param parcialCalculado the parcialCalculado to set
	 */
	public void setParcialCalculado(boolean parcialCalculado) {
		this.parcialCalculado = parcialCalculado;
	}

	public String getQtdExemplarDaGrid() {
		return qtdExemplarDaGrid;
	}

	public void setQtdExemplarDaGrid(String qtdExemplarDaGrid) {
		this.qtdExemplarDaGrid = qtdExemplarDaGrid;
	}
	
	public boolean isOcultarItem() {
		return ocultarItem;
	}

	public void setOcultarItem(boolean ocultarItem) {
		this.ocultarItem = ocultarItem;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idProdutoEdicao == null) ? 0 : idProdutoEdicao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConferenciaEncalheDTO other = (ConferenciaEncalheDTO) obj;
		if (idProdutoEdicao == null) {
			if (other.idProdutoEdicao != null)
				return false;
		} else if (!idProdutoEdicao.equals(other.idProdutoEdicao))
			return false;
		return true;
	}

}