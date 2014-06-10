package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

/**
 * 
 * Objeto para transportar dados referentes a expedição de lançamentos de produtos.  
 * 
 * @author Discover Technology
 *
 */
public class ExpedicaoDTO implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dataLancamento;
	private Long idBox;
	private String codigoProduto;
	private String nomeProduto;
	private Long codigoBox;
	private String nomeBox;
	private Long numeroEdicao;
	private BigDecimal precoCapa = BigDecimal.ZERO;
	private BigDecimal desconto;
	private BigInteger qntReparte = BigInteger.ZERO;
	private BigInteger qntDiferenca = BigInteger.ZERO;
	private BigDecimal valorFaturado = BigDecimal.ZERO;
	private Long qntProduto;
	private String razaoSocial;
	
	private Long idLancamento;
	private Long idUsuario; 
	private Date dataOperacao;
	private TipoMovimentoEstoque tipoMovimentoEstoque; 
	private TipoMovimentoEstoque tipoMovimentoEstoqueCota;
	private TipoMovimentoEstoque tipoMovimentoEstoqueJuramentado;
	private TipoMovimentoFinanceiro tipoMovimentoDebito;
	private Date dataVencimentoDebito;
	private Long idFornecedorPadraoDistribuidor;
	
	public ExpedicaoDTO() {}
	
	public ExpedicaoDTO( final Long idLancamento,
						 final Long idUsuario,
						 final Date dataOperacao,
						 final TipoMovimentoEstoque tipoMovimentoEstoque,
						 final TipoMovimentoEstoque tipoMovimentoEstoqueCota,
						 final TipoMovimentoEstoque tipoMovimentoEstoqueJuramentado,
						 final Date dataVencimentoDebito,
						 final Long idFornecedorPadraoDistribuidor) {
		
		this.idLancamento = idLancamento;
		this.idUsuario = idUsuario; 
		this.dataOperacao = dataOperacao;
		this.tipoMovimentoEstoque = tipoMovimentoEstoque; 
		this.tipoMovimentoEstoqueCota = tipoMovimentoEstoqueCota;
		this.tipoMovimentoEstoqueJuramentado = tipoMovimentoEstoqueJuramentado;
		this.dataVencimentoDebito = dataVencimentoDebito;
		this.idFornecedorPadraoDistribuidor = idFornecedorPadraoDistribuidor;
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
	 * @return the precoCapa
	 */
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	

	/**
	 * Obtém qntReparte
	 *
	 * @return BigInteger
	 */
	public BigInteger getQntReparte() {
		return qntReparte;
	}

	/**
	 * Atribuí qntReparte
	 * @param qntReparte 
	 */
	public void setQntReparte(BigInteger qntReparte) {
		this.qntReparte = qntReparte;
	}

	/**
	 * Obtém qntDiferenca
	 *
	 * @return BigInteger
	 */
	public BigInteger getQntDiferenca() {
		return qntDiferenca;
	}

	/**
	 * Atribuí qntDiferenca
	 * @param qntDiferenca 
	 */
	public void setQntDiferenca(BigInteger qntDiferenca) {
		this.qntDiferenca = qntDiferenca;
	}

	/**
	 * @return the valorFaturado
	 */
	public BigDecimal getValorFaturado() {
		return valorFaturado;
	}

	/**
	 * @param valorFaturado the valorFaturado to set
	 */
	public void setValorFaturado(BigDecimal valorFaturado) {
		this.valorFaturado = valorFaturado;
	}

	/**
	 * @return the idBox
	 */
	public Long getIdBox() {
		return idBox;
	}

	/**
	 * @param idBox the idBox to set
	 */
	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	/**
	 * @return the codigoBox
	 */
	public Long getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(Long codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * @return the qntProduto
	 */
	public Long getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(Long qntProduto) {
		this.qntProduto = qntProduto;
	}

	/**
	 * Obtém razaoSocial
	 *
	 * @return String
	 */
	public String getRazaoSocial() {
		return razaoSocial;
	}

	/**
	 * Atribuí razaoSocial
	 * @param razaoSocial 
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	/**
	 * Obtém dataLancamento
	 *
	 * @return Date
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * Atribuí dataLancamento
	 * @param dataLancamento 
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Long getIdLancamento() {
		return idLancamento;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public TipoMovimentoEstoque getTipoMovimentoEstoque() {
		return tipoMovimentoEstoque;
	}

	public TipoMovimentoEstoque getTipoMovimentoEstoqueCota() {
		return tipoMovimentoEstoqueCota;
	}

	public TipoMovimentoEstoque getTipoMovimentoEstoqueJuramentado() {
		return tipoMovimentoEstoqueJuramentado;
	}

	public TipoMovimentoFinanceiro getTipoMovimentoDebito() {
		return tipoMovimentoDebito;
	}

	public Date getDataVencimentoDebito() {
		return dataVencimentoDebito;
	}

	public Long getIdFornecedorPadraoDistribuidor() {
		return idFornecedorPadraoDistribuidor;
	}

	public void setTipoMovimentoEstoque(TipoMovimentoEstoque tipoMovimentoEstoque) {
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
	}

	public void setTipoMovimentoEstoqueCota(
			TipoMovimentoEstoque tipoMovimentoEstoqueCota) {
		this.tipoMovimentoEstoqueCota = tipoMovimentoEstoqueCota;
	}

	public void setTipoMovimentoDebito(TipoMovimentoFinanceiro tipoMovimentoDebito) {
		this.tipoMovimentoDebito = tipoMovimentoDebito;
	}
	
	
}