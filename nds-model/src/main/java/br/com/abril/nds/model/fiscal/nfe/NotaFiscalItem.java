package br.com.abril.nds.model.fiscal.nfe;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;

@Entity
@Table(name="NOTA_FISCAL_ITEM")
public class NotaFiscalItem {
	
	@Id
	@GeneratedValue()
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="NOTA_FISCAL_ID")
	private NotaFiscalNds notaFiscal;
	
	@Embedded
	private br.com.abril.nfe.model.NotaFiscalItem notaFiscalItem;
	
	@Column(name="ORIGEM_ITEM_NOTA_FISCAL")
	private OrigemItemNotaFiscal origemItemNotaFiscal;
	
	public NotaFiscalItem() {
		if(notaFiscalItem == null){
			this.notaFiscalItem = new br.com.abril.nfe.model.NotaFiscalItem();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotaFiscalNds getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscalNds notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public Long getCodigoItem() {
		return notaFiscalItem.getCodigoItem();
	}

	public void setCodigoItem(Long codigoItem) {
		this.notaFiscalItem.setCodigoItem(codigoItem);
	}

	public String getDescricao() {
		return notaFiscalItem.getDescricao();
	}

	public void setDescricao(String descricao) {
		this.notaFiscalItem.setDescricao(descricao);
	}

	public String getNCM() {
		return notaFiscalItem.getNCM();
	}

	public void setNCM(String nCM) {
		notaFiscalItem.setNCM(nCM);
	}

	public String getCST() {
		return notaFiscalItem.getCST();
	}

	public void setCST(String cST) {
		notaFiscalItem.setCST(cST);
	}

	public String getUnidade() {
		return notaFiscalItem.getUnidade();
	}

	public void setUnidade(String unidade) {
		this.notaFiscalItem.setUnidade(unidade);
	}

	public BigDecimal getValorTotal() {
		return notaFiscalItem.getValorTotal();
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.notaFiscalItem.setValorTotal(valorTotal);
	}

	public BigInteger getQuantidade() {
		return notaFiscalItem.getQuantidade();
	}

	public void setQuantidade(BigInteger quantidade) {
		this.notaFiscalItem.setQuantidade(quantidade);
	}

	public BigDecimal getValorUnitario() {
		return notaFiscalItem.getValorUnitario();
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.notaFiscalItem.setValorUnitario(valorUnitario);
	}

	public OrigemItemNotaFiscal getOrigemItemNotaFiscal() {
		return origemItemNotaFiscal;
	}

	public void setOrigemItemNotaFiscal(OrigemItemNotaFiscal origemItemNotaFiscal) {
		this.origemItemNotaFiscal = origemItemNotaFiscal;
	}
	
}