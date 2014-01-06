package br.com.abril.nds.model.fiscal.nfe;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nfe.model.NotaFiscalBase;

@Entity
@Table(name="NOTA_FISCAL_ITEM")
public class NotaFiscalItem {
	@Embedded
	private br.com.abril.nfe.model.NotaFiscalItem notaFiscalItem;
	
	private OrigemItemNotaFiscal origemItemNotaFiscal;
	
	public NotaFiscalItem() {
		if(notaFiscalItem == null){
			this.notaFiscalItem = new br.com.abril.nfe.model.NotaFiscalItem();
		}
	}

	@Id
	public Long getId() {
		return notaFiscalItem.getId();
	}

	public void setId(Long id) {
		this.notaFiscalItem.setId(id);
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

	public NotaFiscalBase getNotaFiscal() {
		return notaFiscalItem.getNotaFiscal();
	}

	public void setNotaFiscal(NotaFiscalBase notaFiscal) {
		this.notaFiscalItem.setNotaFiscal(notaFiscal);
	}

	public OrigemItemNotaFiscal getOrigemItemNotaFiscal() {
		return origemItemNotaFiscal;
	}

	public void setOrigemItemNotaFiscal(OrigemItemNotaFiscal origemItemNotaFiscal) {
		this.origemItemNotaFiscal = origemItemNotaFiscal;
	}
	
}