package br.com.abril.nds.model.estoque;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.FuroProduto;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE")
public class MovimentoEstoque extends AbstractMovimentoEstoque {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8922062664013183350L;

	@OneToOne(optional = true)
	@JoinColumn(name = "ITEM_REC_FISICO_ID")
	private ItemRecebimentoFisico itemRecebimentoFisico;
	
	@OneToOne(optional = true, mappedBy = "movimentoEstoque", fetch = FetchType.LAZY)
	private LancamentoDiferenca lancamentoDiferenca;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ESTOQUE_PRODUTO_ID")
	private EstoqueProduto estoqueProduto;
	
	@Column(name = "NUM_DOC_ACERTO", nullable = true)
	private Long numeroDocumentoAcerto;

	@Column(name = "DAT_EMISSAO_DOC_ACERTO", nullable = true)
	private Date dataEmicaoDocumentoAcerto;

	@Column(name = "COD_ORIGEM_MOTIVO", nullable = true)
	private String codigoOrigemMotivo;

	@ManyToOne(optional = true)
	@JoinColumn(name = "FURO_PRODUTO_ID")
	private FuroProduto furoProduto;
	
	@Column(name = "INTEGRADO_COM_CE", nullable = true)
	private boolean integracaoCE;
	
	
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

	public FuroProduto getFuroProduto() {
		return furoProduto;
	}

	public void setFuroProduto(FuroProduto furoProduto) {
		this.furoProduto = furoProduto;
	}

	public boolean isIntegracaoCE() {
		return integracaoCE;
	}

	public void setIntegracaoCE(boolean integracaoCE) {
		this.integracaoCE = integracaoCE;
	}
}