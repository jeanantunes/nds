package br.com.abril.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_ITEM")
public class NotaFiscalItem implements Serializable {

	private static final long serialVersionUID = -6124932093852972727L;

	@Id
	@GeneratedValue()
	@Column(name="ID")
	private Long id;
	
	@Column(name="CODIGO_ITEM")
	private Long codigoItem;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="NCM")
	private String NCM;
	
	@Column(name="CST")
	private String CST;
	
	@Column(name="UNIDADE")
	private String unidade;
	
	@Column(name="VALOR_TOTAL")
	private BigDecimal valorTotal;

	@Column(name="QUANTIDADE")
	private BigInteger quantidade;
	
	@Column(name="VALOR_UNITARIO")
	private BigDecimal valorUnitario;

	@Column(name="VALOR_PRODUTO")
	private BigDecimal valorProduto;
	
	@Column(name="NOTA_FISCAL_ID")
	@OneToMany(mappedBy="notaFiscal")
	private NotaFiscal notaFiscal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(Long codigoItem) {
		this.codigoItem = codigoItem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNCM() {
		return NCM;
	}

	public void setNCM(String nCM) {
		NCM = nCM;
	}

	public String getCST() {
		return CST;
	}

	public void setCST(String cST) {
		CST = cST;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigInteger getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorProduto() {
		return valorProduto;
	}

	public void setValorProduto(BigDecimal valorProduto) {
		this.valorProduto = valorProduto;
	}

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	public String toString() {
		return "NotaFiscalItem [id=" + id + ", codigoItem=" + codigoItem
				+ ", descricao=" + descricao + ", NCM=" + NCM + ", CST=" + CST
				+ ", unidade=" + unidade + ", valorTotal=" + valorTotal
				+ ", quantidade=" + quantidade + ", valorUnitario="
				+ valorUnitario + ", valorProduto=" + valorProduto + "]";
	}

}
