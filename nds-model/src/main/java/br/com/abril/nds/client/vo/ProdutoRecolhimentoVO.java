package br.com.abril.nds.client.vo;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para os produtos da matriz de recolhimento.
 * 
 * @author Discover Technology 
 */
@Exportable
public class ProdutoRecolhimentoVO {

	private String idLancamento;
	
	private Long idProdutoEdicao;
	
	@Export(label="Status")
	private String statusLancamento;
	
	@Export(label="Código")
	private String codigoProduto;
	
	@Export(label="Produto", widthPercent=13f)
	private String nomeProduto;
	
	@Export(label="Ed.", widthPercent=4f)
	private Long numeroEdicao;
	
	@Export(label="Capa R$", columnType=ColumnType.MOEDA)
	private BigDecimal precoVenda;
	
	@Export(label="Desc R$", columnType=ColumnType.MOEDA)
	private BigDecimal precoDesconto;
	
	private Long idFornecedor;
	
	@Export(label="Fornecedor", widthPercent=8f)
	private String nomeFornecedor;
	
	@Export(label="Editor")
	private String nomeEditor;
	
	@Export(label="Parcial")
	private String parcial;
	
	@Export(label="Brinde")
	private String brinde;
	
	@Export(label="Lçto")
	private Date dataLancamento;
	
	@Export(label="PEB")
	private Long peb;
	
	@Export(label="Rcto")
	private Date dataRecolhimento;
	
	private String encalheSedeFormatado;
	
	private String encalheAtendidaFormatado;
	
	@Export(label="Exemplar")
	private String encalheFormatado;
	
	private BigDecimal encalheSede;

	private BigDecimal encalheAtendida;
	
	private BigDecimal encalhe;
	
	
	public Long getPeb() {
		return peb;
	}

	public void setPeb(Long peb) {
		this.peb = peb;
	}

	@Export(label="Total R$", columnType=ColumnType.MOEDA)
	private BigDecimal valorTotal;
	
	@Export(label="Nova Data")
	private Date novaData;
	
	private boolean bloqueioAlteracaoBalanceamento;

	/**
	 * @return the idLancamento
	 */
	public String getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(String idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the statusLancamento
	 */
	public String getStatusLancamento() {
		return statusLancamento;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
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
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the precoDesconto
	 */
	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	/**
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the nomeEditor
	 */
	public String getNomeEditor() {
		return nomeEditor;
	}

	/**
	 * @param nomeEditor the nomeEditor to set
	 */
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	/**
	 * @return the parcial
	 */
	public String getParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(String parcial) {
		this.parcial = parcial;
	}

	/**
	 * @return the brinde
	 */
	public String getBrinde() {
		return brinde;
	}

	/**
	 * @param brinde the brinde to set
	 */
	public void setBrinde(String brinde) {
		this.brinde = brinde;
	}

	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the encalheSede
	 */
	public BigDecimal getEncalheSede() {
		return encalheSede;
	}

	/**
	 * @param encalheSede the encalheSede to set
	 */
	public void setEncalheSede(BigDecimal encalheSede) {
		this.encalheSede = encalheSede;
		this.encalheSedeFormatado = 
			encalheSede == null ? "0" : String.valueOf(encalheSede.longValue());
	}

	/**
	 * @return the encalheAtendida
	 */
	public BigDecimal getEncalheAtendida() {
		return encalheAtendida;
	}

	/**
	 * @param encalheAtendida the encalheAtendida to set
	 */
	public void setEncalheAtendida(BigDecimal encalheAtendida) {
		this.encalheAtendida = encalheAtendida;
		this.encalheAtendidaFormatado = 
			encalheAtendida == null ? "0" : String.valueOf(encalheAtendida.longValue());
	}

	/**
	 * @return the encalhe
	 */
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	/**
	 * @param encalhe the encalhe to set
	 */
	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
		this.encalheFormatado = 
			encalhe == null ? "0" : String.valueOf(encalhe.longValue());
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the novaData
	 */
	public Date getNovaData() {
		return novaData;
	}

	/**
	 * @param novaData the novaData to set
	 */
	public void setNovaData(Date novaData) {
		this.novaData = novaData;
	}

	/**
	 * @return the bloqueioAlteracaoBalanceamento
	 */
	public boolean isBloqueioAlteracaoBalanceamento() {
		return bloqueioAlteracaoBalanceamento;
	}

	/**
	 * @param bloqueioAlteracaoBalanceamento the bloqueioAlteracaoBalanceamento to set
	 */
	public void setBloqueioAlteracaoBalanceamento(
			boolean bloqueioAlteracaoBalanceamento) {
		this.bloqueioAlteracaoBalanceamento = bloqueioAlteracaoBalanceamento;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the encalheSedeFormatado
	 */
	public String getEncalheSedeFormatado() {
		return encalheSedeFormatado;
	}

	/**
	 * @return the encalheAtendidaFormatado
	 */
	public String getEncalheAtendidaFormatado() {
		return encalheAtendidaFormatado;
	}

	/**
	 * @return the encalheFormatado
	 */
	public String getEncalheFormatado() {
		return encalheFormatado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
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
		ProdutoRecolhimentoVO other = (ProdutoRecolhimentoVO) obj;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		return true;
	}
	
	
}
