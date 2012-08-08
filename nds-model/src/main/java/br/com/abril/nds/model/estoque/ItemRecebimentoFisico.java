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
	@JoinColumn(name = "DIFERENCA_ID")
	private Diferenca diferenca;
	@ManyToOne(optional = false)
	@JoinColumn(name = "RECEBIMENTO_FISICO_ID")
	private RecebimentoFisico recebimentoFisico;
	@OneToOne(optional = false)
	@JoinColumn(name = "ITEM_NF_ENTRADA_ID")
	private ItemNotaFiscalEntrada itemNotaFiscal;
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
}