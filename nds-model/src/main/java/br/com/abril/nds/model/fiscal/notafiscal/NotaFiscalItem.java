package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NotaFiscalItem implements Serializable {

	private static final long serialVersionUID = -6124932093852972727L;

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

}