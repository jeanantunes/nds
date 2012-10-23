package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ContasApagarConsultaPorProdutoDTO implements Serializable{
	
	private static final long serialVersionUID = 3890007339807389818L;
	
	private Long produtoEdicaoId;
	private Date rctl;
	private String codigo;
	private String produto;
	private Long edicao;
	private boolean tipo;
	private BigInteger reparte;
	private BigInteger suplementacao;
	private BigInteger encalhe;
	private BigInteger venda;
	private BigInteger faltasSobras;
	private BigInteger debitosCreditos;
	private BigDecimal saldoAPagar;
	
	// Dados do cabecalho da popup "Tipo"
	private String fornecedor;
	private Date dataLcto;
	private Date dataFinal;

	public ContasApagarConsultaPorProdutoDTO(){
		
	}
	
	/**
	 * @param produtoEdicaoId
	 * @param rctl
	 * @param codigo
	 * @param produto
	 * @param edicao
	 * @param tipo
	 * @param reparte
	 * @param suplementacao
	 * @param encalhe
	 * @param venda
	 * @param faltasSobras
	 * @param debitosCreditos
	 * @param saldoAPagar
	 */
	public ContasApagarConsultaPorProdutoDTO(Long produtoEdicaoId, Date rctl, String codigo,
			String produto, Long edicao, boolean tipo, BigInteger reparte,
			BigInteger suplementacao, BigInteger encalhe, BigInteger venda,
			BigInteger faltasSobras, BigInteger debitosCreditos) {
		
		super();
		
		this.produtoEdicaoId = produtoEdicaoId;
		this.rctl = rctl;
		this.codigo = codigo;
		this.produto = produto;
		this.edicao = edicao;
		this.tipo = tipo;
		this.reparte = reparte!=null?reparte:BigInteger.ZERO;
		this.suplementacao = suplementacao!=null?suplementacao:BigInteger.ZERO;
		this.encalhe = encalhe!=null?encalhe:BigInteger.ZERO;
		this.venda = venda!=null?venda:BigInteger.ZERO;
		this.faltasSobras = faltasSobras!=null?faltasSobras:BigInteger.ZERO;
		this.debitosCreditos = debitosCreditos!=null?debitosCreditos:BigInteger.ZERO;
		
		this.saldoAPagar = BigDecimal.valueOf(this.encalhe.subtract(this.faltasSobras == null ? BigInteger.ZERO : this.faltasSobras)
				                              .subtract(this.debitosCreditos == null ? BigInteger.ZERO : this.debitosCreditos).longValue());
	}
	
	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}
	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}
	public Date getRctl() {
		return rctl;
	}
	public void setRctl(Date rctl) {
		this.rctl = rctl;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public boolean isTipo() {
		return tipo;
	}
	public void setTipo(boolean tipo) {
		this.tipo = tipo;
	}
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public BigInteger getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(BigInteger suplementacao) {
		this.suplementacao = suplementacao;
	}
	public BigInteger getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	public BigInteger getVenda() {
		return venda;
	}
	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}
	public BigInteger getFaltasSobras() {
		return faltasSobras;
	}
	public void setFaltasSobras(BigInteger faltasSobras) {
		this.faltasSobras = faltasSobras;
	}
	public BigInteger getDebitosCreditos() {
		return debitosCreditos;
	}
	public void setDebitosCreditos(BigInteger debitosCreditos) {
		this.debitosCreditos = debitosCreditos;
	}
	public BigDecimal getSaldoAPagar() {
		return saldoAPagar;
	}
	public void setSaldoAPagar(BigDecimal saldoAPagar) {
		this.saldoAPagar = saldoAPagar;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public Date getDataLcto() {
		return dataLcto;
	}
	public void setDataLcto(Date dataLcto) {
		this.dataLcto = dataLcto;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
}
