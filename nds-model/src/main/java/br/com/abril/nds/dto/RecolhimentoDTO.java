package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;

/**
 * DTO que representa os dados referentes ao balanceamento do recolhimento. 
 * 
 * @author Discover Technology
 *
 */
public class RecolhimentoDTO implements Serializable {

	private static final long serialVersionUID = -2168766293591158494L;

	private Long idProdutoEdicao;
	
	private Long codigoProduto;

	private String descricaoProduto;

	private Long numeroEdicao;
	
	private BigDecimal precoVenda;

	private Long idFornecedor;
	
	private String nomeFornecedor;

	private Long idEditor;

	private String nomeEditor;

	private TipoLancamentoParcial parcial;

	private boolean possuiBrinde;

	private Date dataLancamento;

	private Date dataRecolhimento;

	private BigDecimal sede;

	private BigDecimal atendida;

	private BigDecimal qtdeExemplares;

	private BigDecimal valorTotal;

	private BigDecimal peso;

	private Date novaData;

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
	 * @return the codigoProduto
	 */
	public Long getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(Long codigoProduto) {
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
	 * @return the idEditor
	 */
	public Long getIdEditor() {
		return idEditor;
	}

	/**
	 * @param idEditor the idEditor to set
	 */
	public void setIdEditor(Long idEditor) {
		this.idEditor = idEditor;
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
	public TipoLancamentoParcial getParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(TipoLancamentoParcial parcial) {
		this.parcial = parcial;
	}

	/**
	 * @return the possuiBrinde
	 */
	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}

	/**
	 * @param possuiBrinde the possuiBrinde to set
	 */
	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
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
	 * @return the sede
	 */
	public BigDecimal getSede() {
		return sede;
	}

	/**
	 * @param sede the sede to set
	 */
	public void setSede(BigDecimal sede) {
		this.sede = sede;
	}

	/**
	 * @return the atendida
	 */
	public BigDecimal getAtendida() {
		return atendida;
	}

	/**
	 * @param atendida the atendida to set
	 */
	public void setAtendida(BigDecimal atendida) {
		this.atendida = atendida;
	}

	/**
	 * @return the qtdeExemplares
	 */
	public BigDecimal getQtdeExemplares() {
		return qtdeExemplares;
	}

	/**
	 * @param qtdeExemplares the qtdeExemplares to set
	 */
	public void setQtdeExemplares(BigDecimal qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
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
	 * @return the peso
	 */
	public BigDecimal getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
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
}
