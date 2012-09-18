package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ProdutoLancamentoVO  implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;
	
	private Long id;
	
	@Export(label="Codigo", exhibitionOrder = 0)
	private String codigoProduto;
	
	@Export(label="Produto", exhibitionOrder = 1)
	private String nomeProduto;
	
	@Export(label="Edição", exhibitionOrder = 2)
	private Long numeroEdicao;
	
	@Export(label="Preço Capa R$", exhibitionOrder = 3)
	private String precoVenda;
	
	@Export(label="Reparte", exhibitionOrder = 4)
	private String repartePrevisto;
	
	@Export(label="Lançamento", exhibitionOrder = 5)
	private String descricaoLancamento;
	
	@Export(label="Recolhimento", exhibitionOrder = 6)
	private String dataRecolhimentoPrevista;
	
	@Export(label="Total R$", exhibitionOrder = 7)
	private String valorTotal;
	
	@Export(label="Físico", exhibitionOrder = 8)
	private String reparteFisico;
	
	@Export(label="Distribuição", exhibitionOrder = 9)
	private String distribuicao;
	
	@Export(label="Previsto", exhibitionOrder = 10)
	private String dataLancamentoPrevista;
	
	private String novaData;
	
	private boolean bloquearData;
	
	private boolean bloquearExclusao;
	
	private Long idProdutoEdicao;
	
	private boolean destacarLinha;
	
	private boolean possuiFuro;
	
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
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the repartePrevisto
	 */
	public String getRepartePrevisto() {
		return repartePrevisto;
	}

	/**
	 * @param repartePrevisto the repartePrevisto to set
	 */
	public void setRepartePrevisto(String repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	/**
	 * @return the descricaoLancamento
	 */
	public String getDescricaoLancamento() {
		return descricaoLancamento;
	}

	/**
	 * @param descricaoLancamento the descricaoLancamento to set
	 */
	public void setDescricaoLancamento(String descricaoLancamento) {
		this.descricaoLancamento = descricaoLancamento;
	}

	/**
	 * @return the dataRecolhimentoPrevista
	 */
	public String getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	/**
	 * @param dataRecolhimentoPrevista the dataRecolhimentoPrevista to set
	 */
	public void setDataRecolhimentoPrevista(String dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
	}

	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the reparteFisico
	 */
	public String getReparteFisico() {
		return reparteFisico;
	}

	/**
	 * @param reparteFisico the reparteFisico to set
	 */
	public void setReparteFisico(String reparteFisico) {
		this.reparteFisico = reparteFisico;
	}

	/**
	 * @return the dataLancamentoPrevista
	 */
	public String getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}

	/**
	 * @param dataLancamentoPrevista the dataLancamentoPrevista to set
	 */
	public void setDataLancamentoPrevista(String dataLancamentoPrevista) {
		this.dataLancamentoPrevista = dataLancamentoPrevista;
	}

	/**
	 * @return the novaData
	 */
	public String getNovaData() {
		return novaData;
	}

	/**
	 * @param novaData the novaData to set
	 */
	public void setNovaData(String novaData) {
		this.novaData = novaData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProdutoLancamentoVO other = (ProdutoLancamentoVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getDistribuicao() {
		return distribuicao;
	}

	public void setDistribuicao(String distribuicao) {
		this.distribuicao = distribuicao;
	}

	public boolean isBloquearData() {
		return bloquearData;
	}

	public void setBloquearData(boolean bloquearData) {
		this.bloquearData = bloquearData;
	}
	
	public boolean isBloquearExclusao() {
		return bloquearExclusao;
	}
	
	public void setBloquearExclusao(boolean bloquearExclusao) {
		this.bloquearExclusao = bloquearExclusao;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the destacarLinha
	 */
	public boolean isDestacarLinha() {
		return destacarLinha;
	}

	/**
	 * @param destacarLinha the destacarLinha to set
	 */
	public void setDestacarLinha(boolean destacarLinha) {
		this.destacarLinha = destacarLinha;
	}

	/**
	 * @return the possuiFuro
	 */
	public boolean isPossuiFuro() {
		return possuiFuro;
	}

	/**
	 * @param possuiFuro the possuiFuro to set
	 */
	public void setPossuiFuro(boolean possuiFuro) {
		this.possuiFuro = possuiFuro;
	}

}
