package br.com.abril.nds.model.estoque;
import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;

@Entity
@Table(name = "ITEM_RECEB_FISICO")
@SequenceGenerator(name="ITEM_REC_FISICO_SEQ", initialValue = 1, allocationSize = 1)
public class ItemRecebimentoFisico implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6607298060913283881L;
	
	@Id
	@GeneratedValue(generator = "ITEM_REC_FISICO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "QTDE_FISICO", nullable = false)
	private BigInteger qtdeFisico;
	
	@OneToOne
	@JoinColumn(name = "DIFERENCA_ID", updatable = true, insertable = true, nullable = true)
	private Diferenca diferenca;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "RECEBIMENTO_FISICO_ID")
	private RecebimentoFisico recebimentoFisico;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "ITEM_NF_ENTRADA_ID")
	private ItemNotaFiscalEntrada itemNotaFiscal;
	
	public ItemRecebimentoFisico() {
		
	}
	
	public ItemRecebimentoFisico(Long id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the qtdeFisico
	 */
	public BigInteger getQtdeFisico() {
		return qtdeFisico;
	}
	/**
	 * @param qtdeFisico the qtdeFisico to set
	 */
	public void setQtdeFisico(BigInteger qtdeFisico) {
		this.qtdeFisico = qtdeFisico;
	}
	/**
	 * @return the diferenca
	 */
	public Diferenca getDiferenca() {
		return diferenca;
	}
	/**
	 * @param diferenca the diferenca to set
	 */
	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}
	/**
	 * @return the recebimentoFisico
	 */
	public RecebimentoFisico getRecebimentoFisico() {
		return recebimentoFisico;
	}
	/**
	 * @param recebimentoFisico the recebimentoFisico to set
	 */
	public void setRecebimentoFisico(RecebimentoFisico recebimentoFisico) {
		this.recebimentoFisico = recebimentoFisico;
	}
	/**
	 * @return the itemNotaFiscal
	 */
	public ItemNotaFiscalEntrada getItemNotaFiscal() {
		return itemNotaFiscal;
	}
	/**
	 * @param itemNotaFiscal the itemNotaFiscal to set
	 */
	public void setItemNotaFiscal(ItemNotaFiscalEntrada itemNotaFiscal) {
		this.itemNotaFiscal = itemNotaFiscal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getItemNotaFiscal() == null) ? 0 : this.getItemNotaFiscal().hashCode());
		result = prime * result + ((this.getRecebimentoFisico() == null) ? 0 : this.getRecebimentoFisico().hashCode());
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
		ItemRecebimentoFisico other = (ItemRecebimentoFisico) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getItemNotaFiscal() == null) {
			if (other.getItemNotaFiscal() != null)
				return false;
		} else if (!this.getItemNotaFiscal().equals(other.getItemNotaFiscal()))
			return false;
		if (this.getRecebimentoFisico() == null) {
			if (other.getRecebimentoFisico() != null)
				return false;
		} else if (!this.getRecebimentoFisico().equals(other.getRecebimentoFisico()))
			return false;
		return true;
	}
}