package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.export.Exportable;

/**
 *  DTO que agrupa os parâmetros do sistema a serem exibidos/alterados na tela
 *  "Venda Encalhe".
 * 
 * @author Discover Technology
 */
@Exportable
public class VendaEncalheDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idVenda;
	
	private Date dataVenda;
	
	private Integer numeroCota;

	private String nomeCota;
		
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal precoCapa;
	
	private BigInteger qntProduto;
	
	private BigDecimal precoDesconto;
	
	private BigDecimal valoTotalProduto;

	private TipoVendaEncalhe tipoVendaEncalhe;

	private BigInteger qntDisponivelProduto;
	
	private String codigoBarras;
	
	private FormaComercializacao formaVenda;
	
	private Date dataVencimentoDebito;
	
	private String codBox;
	
	private Usuario usuario;
	
	private Date horarioVenda;
	
	private FormaComercializacao formaComercializacao;
	
	private String comercializacao;
	
	private boolean produtoContaFirme;
	
	public boolean isProdutoContaFirme() {
		return produtoContaFirme;
	}

	public void setProdutoContaFirme(boolean produtoContaFirme) {
		this.produtoContaFirme = produtoContaFirme;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the dataVencimentoDebito
	 */
	public Date getDataVencimentoDebito() {
		return dataVencimentoDebito;
	}

	/**
	 * @param dataVencimentoDebito the dataVencimentoDebito to set
	 */
	public void setDataVencimentoDebito(Date dataVencimentoDebito) {
		this.dataVencimentoDebito = dataVencimentoDebito;
	}

	/**
	 * @return the codBox
	 */
	public String getCodBox() {
		return codBox;
	}

	/**
	 * @param codBox the codBox to set
	 */
	public void setCodBox(String codBox) {
		this.codBox = codBox;
	}

	/**
	 * @return the formaVenda
	 */
	public FormaComercializacao getFormaVenda() {
		return formaVenda;
	}

	/**
	 * @param formaVenda the formaVenda to set
	 */
	public void setFormaVenda(FormaComercializacao formaVenda) {
		this.formaVenda = formaVenda;
	}

	/**
	 * @return the codigoBarras
	 */
	public String getCodigoBarras() {
		return codigoBarras;
	}

	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	/**
	 * @return the idVenda
	 */
	public Long getIdVenda() {
		return idVenda;
	}

	/**
	 * @param idVenda the idVenda to set
	 */
	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}

	/**
	 * @return the dataVenda
	 */
	public Date getDataVenda() {
		return dataVenda;
	}

	/**
	 * @param dataVenda the dataVenda to set
	 */
	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
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
	 * @return the qntProduto
	 */
	public BigInteger getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(BigInteger qntProduto) {
		this.qntProduto = qntProduto;
	}

	/**
	 * @return the valoTotalProduto
	 */
	public BigDecimal getValoTotalProduto() {
		return valoTotalProduto;
	}

	/**
	 * @param valoTotalProduto the valoTotalProduto to set
	 */
	public void setValoTotalProduto(BigDecimal valoTotalProduto) {
		this.valoTotalProduto = valoTotalProduto;
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
	 * @return the tipoVendaEncalhe
	 */
	public TipoVendaEncalhe getTipoVendaEncalhe() {
		return tipoVendaEncalhe;
	}

	/**
	 * @param tipoVendaEncalhe the tipoVendaEncalhe to set
	 */
	public void setTipoVendaEncalhe(TipoVendaEncalhe tipoVendaEncalhe) {
		this.tipoVendaEncalhe = tipoVendaEncalhe;
	}

	/**
	 * @return the qntDisponivelProduto
	 */
	public BigInteger getQntDisponivelProduto() {
		return qntDisponivelProduto;
	}

	/**
	 * @param qntDisponivelProduto the qntDisponivelProduto to set
	 */
	public void setQntDisponivelProduto(BigInteger qntDisponivelProduto) {
		this.qntDisponivelProduto = qntDisponivelProduto;
	}

	public Date getHorarioVenda() {
		return horarioVenda;
	}

	public void setHorarioVenda(Date horarioVenda) {
		this.horarioVenda = horarioVenda;
	}

	public FormaComercializacao getFormaComercializacao() {
		return formaComercializacao;
	}

	public void setFormaComercializacao(FormaComercializacao formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
		this.comercializacao = formaComercializacao.name();
	}

	public String getComercializacao() {
		return formaComercializacao.name();
	}

	public void setComercializacao(String comercializacao) {
		this.comercializacao = formaComercializacao.name();
	}
}
