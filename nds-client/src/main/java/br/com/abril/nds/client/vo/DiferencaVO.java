package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para lançamento e consulta de diferença.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class DiferencaVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6329780427551941318L;

	private Long id;
	
	@Export(label = "Data", alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String dataLancamento;
	
	@Export(label = "Código", alignment = Alignment.CENTER, exhibitionOrder = 2)
	private String codigoProduto;
	
	@Export(label = "Produto", exhibitionOrder = 3)
	private String descricaoProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String numeroEdicao;
	
	@Export(label = "Preço Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 5)
	private String precoVenda;
	
	@Export(label = "Preço Desconto R$", alignment = Alignment.RIGHT, exhibitionOrder = 6)
	private String precoDesconto;
	
	private String pacotePadrao;
	
	@Export(label = "Exemplar", alignment = Alignment.RIGHT, exhibitionOrder = 9)
	private BigInteger quantidade;
	
	@Export(label = "Tipo de Diferença", exhibitionOrder = 7)
	private String tipoDiferenca;
	
	@Export(label = "Nota", exhibitionOrder = 8)
	private String numeroNotaFiscal;
	
	@Export(label = "Status", exhibitionOrder = 10, alignment = Alignment.CENTER)
	private String statusAprovacao;
	
	private String motivoAprovacao;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, exhibitionOrder = 11)
	private String valorTotalDiferenca;
	
	private BigInteger qtdeEstoqueAtual;
	
	private Boolean automatica;
	
	private boolean cadastrado;
	
	/**
	 * Construtor padrão.
	 */
	public DiferencaVO() {
		
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
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
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
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
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
	 * @return the precoDesconto
	 */
	public String getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	/**
	 * @return the pacotePadrao
	 */
	public String getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(String pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the quantidade
	 */
	public BigInteger getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the tipoDiferenca
	 */
	public String getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(String tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the numeroNotaFiscal
	 */
	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	/**
	 * @param numeroNotaFiscal the numeroNotaFiscal to set
	 */
	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	/**
	 * @return the statusAprovacao
	 */
	public String getStatusAprovacao() {
		return statusAprovacao;
	}

	/**
	 * @param statusAprovacao the statusAprovacao to set
	 */
	public void setStatusAprovacao(String statusAprovacao) {
		this.statusAprovacao = statusAprovacao;
	}

	/**
	 * @return the motivoAprovacao
	 */
	public String getMotivoAprovacao() {
		return motivoAprovacao;
	}

	/**
	 * @param motivoAprovacao the motivoAprovacao to set
	 */
	public void setMotivoAprovacao(String motivoAprovacao) {
		this.motivoAprovacao = motivoAprovacao;
	}
	
	/**
	 * @return the valorDiferenca
	 */
	public String getValorTotalDiferenca() {
		return valorTotalDiferenca;
	}

	/**
	 * @param valorDiferenca the valorDiferenca to set
	 */
	public void setValorTotalDiferenca(String valorTotalDiferenca) {
		this.valorTotalDiferenca = valorTotalDiferenca;
	}

	/**
	 * @return the qtdeEstoqueAtual
	 */
	public BigInteger getQtdeEstoqueAtual() {
		return qtdeEstoqueAtual;
	}

	/**
	 * @param qtdeEstoqueAtual the qtdeEstoqueAtual to set
	 */
	public void setQtdeEstoqueAtual(BigInteger qtdeEstoqueAtual) {
		this.qtdeEstoqueAtual = qtdeEstoqueAtual;
	}
	
	/**
	 * @return the cadastrado
	 */
	public boolean isCadastrado() {
		return cadastrado;
	}

	/**
	 * @param cadastrado the cadastrado to set
	 */
	public void setCadastrado(boolean cadastrado) {
		this.cadastrado = cadastrado;
	}
	
	/**
	 * @return the automatica
	 */
	public Boolean isAutomatica() {
		return automatica;
	}

	/**
	 * @param automatica the automatica to set
	 */
	public void setAutomatica(Boolean automatica) {
		this.automatica = automatica;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiferencaVO other = (DiferencaVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
