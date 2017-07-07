package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ProdutoLancamentoVO  implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;
	
	private Long id;
	
	@Export(label="Codigo", exhibitionOrder = 0, alignment = Alignment.CENTER, fontSize=6, widthPercent = 6)
	private String codigoProduto;
	
	@Export(label="Produto", exhibitionOrder = 1, alignment = Alignment.LEFT, fontSize=6, widthPercent = 15)
	private String nomeProduto;
	
	@Export(label="Edição", exhibitionOrder = 2, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private Long numeroEdicao;
	
	@Export(label="Capa R$", exhibitionOrder = 3, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private String precoVenda;
	
	@Export(label="Reparte", exhibitionOrder = 4, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private String repartePrevisto;
	
	@Export(label="Lançamento", exhibitionOrder = 5, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private String descricaoLancamento;
	
	@Export(label="Rec.", exhibitionOrder = 6, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private String dataRecolhimentoPrevista;

	@Export(label="Rec. Dist.", exhibitionOrder = 7, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private String dataRecolhimentoDistribuidor;
	
	@Export(label="Total R$", exhibitionOrder = 8, alignment = Alignment.CENTER, fontSize=6, widthPercent = 7)
	@Footer(label="Total R$", colspan = 2, type = FooterType.SUM,  columnType = ColumnType.MOEDA)
	private String valorTotal;
	
	@Export(label="PEB", exhibitionOrder = 9, alignment = Alignment.CENTER, fontSize=6, widthPercent = 3)
	private Long peb;
	
	@Export(label="Desc.", exhibitionOrder = 10, alignment = Alignment.CENTER, fontSize=6, widthPercent = 5)
	private String descontoLogistica;
	
	@Export(label="Físico", exhibitionOrder = 11, fontSize=6, alignment = Alignment.CENTER, widthPercent = 4)
	private String reparteFisico;
	
	@Export(label="Distrib.", exhibitionOrder = 12, fontSize=6, alignment = Alignment.CENTER, widthPercent = 5)
	private String distribuicao;
	
	@Export(label="Previsto", exhibitionOrder = 13, fontSize=6, alignment = Alignment.CENTER, widthPercent = 7)
	private String dataLancamentoPrevista;
	
	//@Export(label="Excluir", exhibitionOrder = 13, widthPercent = 0)
	private boolean cancelado;
	
	@Export(label="Status", exhibitionOrder = 14, fontSize=6, alignment = Alignment.CENTER, widthPercent = 0)
	private String statusLancamento;
	
	@Export(label="Fornec.", exhibitionOrder = 15, fontSize=6, alignment = Alignment.CENTER, widthPercent = 7)
	private String nomeFantasia;
	
	
	@Export(label="Seq.", exhibitionOrder = 16, fontSize=6, alignment = Alignment.CENTER, widthPercent = 4)
	private String sequenciaMatriz;
	
	private Long fornecedorId;

	private String novaDataLancamento;
	
	private boolean bloquearData;
	
	private Long idProdutoEdicao;
	
	private boolean destacarLinha;
	
	private boolean possuiFuro;
	


	public String getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(String sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}

	private boolean produtoContaFirme;
	
	
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

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getStatusLancamento() {
		return statusLancamento;
	}

	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public Long getFornecedorId() {
		return fornecedorId;
	}

	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
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

	public String getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}

	public void setDataRecolhimentoDistribuidor(String dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
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
	 * @return the novaDataLancamento
	 */
	public String getNovaDataLancamento() {
		return novaDataLancamento;
	}

	/**
	 * @param novaDataLancamento the novaDataLancamento to set
	 */
	public void setNovaDataLancamento(String novaDataLancamento) {
		this.novaDataLancamento = novaDataLancamento;
	}
	
	public String getDescontoLogistica() {
		return descontoLogistica;
	}

	public void setDescontoLogistica(String descontoLogistica) {
		this.descontoLogistica = descontoLogistica;
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

	public Long getPeb() {
		return peb;
	}

	public void setPeb(Long peb) {
		this.peb = peb;
	}
	
	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

	public boolean isProdutoContaFirme() {
		return produtoContaFirme;
	}

	public void setProdutoContaFirme(boolean produtoContaFirme) {
		this.produtoContaFirme = produtoContaFirme;
	}
}
