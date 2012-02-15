package br.com.abril.nds.model.estoque;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.ItemNotaFiscal;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "ITEM_RECEB_FISICO")
public class ItemRecebimentoFisico {

	@Id
	private Long id;
	private BigDecimal qtdeFisico;
	@ManyToOne
	private RecebimentoFisico recebimentoFisico;
	@OneToOne
	private ItemNotaFiscal itemNotaFiscal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getQtdeFisico() {
		return qtdeFisico;
	}
	
	public void setQtdeFisico(BigDecimal qtdeFisico) {
		this.qtdeFisico = qtdeFisico;
	}
	
	public RecebimentoFisico getRecebimentoFisico() {
		return recebimentoFisico;
	}
	
	public void setRecebimentoFisico(RecebimentoFisico recebimentoFisico) {
		this.recebimentoFisico = recebimentoFisico;
	}
	
	public ItemNotaFiscal getItemNotaFiscal() {
		return itemNotaFiscal;
	}
	
	public void setItemNotaFiscal(ItemNotaFiscal itemNotaFiscal) {
		this.itemNotaFiscal = itemNotaFiscal;
	}
	
}