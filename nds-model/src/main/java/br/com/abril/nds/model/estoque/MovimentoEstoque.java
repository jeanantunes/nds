
package br.com.abril.nds.model.estoque;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE")
public class MovimentoEstoque extends AbstractMovimentoEstoque {

	@OneToOne(optional = true)
	@JoinColumn(name = "ITEM_REC_FISICO_ID")
	private ItemRecebimentoFisico itemRecebimentoFisico;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTOQUE_PRODUTO_ID")
	private EstoqueProduto estoqueProduto;
	
	@Column(name = "NUM_DOC_ACERTO", nullable = true)
	private Long numeroDocumentoAcerto;

	@Column(name = "DAT_EMISSAO_DOC_ACERTO", nullable = true)
	private Date dataEmicaoDocumentoAcerto;

	@Column(name = "COD_ORIGEM_MOTIVO", nullable = true)
	private String codigoOrigemMotivo;

	/**
	 * @return the itemRecebimentoFisico
	 */
	public ItemRecebimentoFisico getItemRecebimentoFisico() {
		return itemRecebimentoFisico;
	}

	/**
	 * @param itemRecebimentoFisico the itemRecebimentoFisico to set
	 */
	public void setItemRecebimentoFisico(ItemRecebimentoFisico itemRecebimentoFisico) {
		this.itemRecebimentoFisico = itemRecebimentoFisico;
	}

	/**
	 * @return the estoqueProduto
	 */
	public EstoqueProduto getEstoqueProduto() {
		return estoqueProduto;
	}

	/**
	 * @param estoqueProduto the estoqueProduto to set
	 */
	public void setEstoqueProduto(EstoqueProduto estoqueProduto) {
		this.estoqueProduto = estoqueProduto;
	}

	/**
	 * @return the numeroDocumentoAcerto
	 */
	public Long getNumeroDocumentoAcerto() {
		return numeroDocumentoAcerto;
	}

	/**
	 * @param numeroDocumentoAcerto the numeroDocumentoAcerto to set
	 */
	public void setNumeroDocumentoAcerto(Long numeroDocumentoAcerto) {
		this.numeroDocumentoAcerto = numeroDocumentoAcerto;
	}

	/**
	 * @return the dataEmicaoDocumentoAcerto
	 */
	public Date getDataEmicaoDocumentoAcerto() {
		return dataEmicaoDocumentoAcerto;
	}

	/**
	 * @param dataEmicaoDocumentoAcerto the dataEmicaoDocumentoAcerto to set
	 */
	public void setDataEmicaoDocumentoAcerto(Date dataEmicaoDocumentoAcerto) {
		this.dataEmicaoDocumentoAcerto = dataEmicaoDocumentoAcerto;
	}

	/**
	 * @return the codigoOrigemMotivo
	 */
	public String getCodigoOrigemMotivo() {
		return codigoOrigemMotivo;
	}

	/**
	 * @param codigoOrigemMotivo the codigoOrigemMotivo to set
	 */
	public void setCodigoOrigemMotivo(String codigoOrigemMotivo) {
		this.codigoOrigemMotivo = codigoOrigemMotivo;
	}
		

}