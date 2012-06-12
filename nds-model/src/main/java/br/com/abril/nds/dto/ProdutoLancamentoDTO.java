package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;

/**
 * @author Discover Technology
 *
 */
public class ProdutoLancamentoDTO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3924519267530987652L;

	private Long idProdutoEdicao;
	
	private Long numeroEdicao;

	private BigDecimal precoVenda;
	
	private BigDecimal precoComDesconto;
	
	private StatusLancamento statusLancamento;

	private BigDecimal peso;

	private BigDecimal desconto = BigDecimal.ZERO;

	private String codigoProduto;

	private String nomeProduto;
	
	private BigDecimal repartePrevisto;
	
	private BigDecimal reparteFisico;
	
	private Long idLancamento;
	
	private Integer sequencia;

	//TODO: private Long idFornecedor;
	
	//TODO: private String nomeFornecedor;

	private TipoLancamentoParcial parcial;
	
	private Date dataLancamentoPrevista;

	private Date dataLancamentoDistribuidor;
	
	private Date dataRecolhimentoPrevista;

	private BigDecimal valorTotal;

	private Date novaDataLancamento;
	
	private boolean possuiEstudo;
	
	private boolean possuiRecebimentoFisico;

	/**
	 * Construtor padrão.
	 */
	public ProdutoLancamentoDTO() {
		
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
	 * @return the precoComDesconto
	 */
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * @param precoComDesconto the precoComDesconto to set
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * @return the statusLancamento
	 */
	public StatusLancamento getStatusLancamento() {
		return statusLancamento;
	}

	/**
	 * @param statusLancamento the statusLancamento to set
	 */
	public void setStatusLancamento(StatusLancamento statusLancamento) {
		this.statusLancamento = statusLancamento;
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
	 * @return the repartePrevisto
	 */
	public BigDecimal getRepartePrevisto() {
		return repartePrevisto;
	}

	/**
	 * @param repartePrevisto the repartePrevisto to set
	 */
	public void setRepartePrevisto(BigDecimal repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	/**
	 * @return the reparteFisico
	 */
	public BigDecimal getReparteFisico() {
		return reparteFisico;
	}

	/**
	 * @param reparteFisico the reparteFisico to set
	 */
	public void setReparteFisico(BigDecimal reparteFisico) {
		this.reparteFisico = reparteFisico;
	}

	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the sequencia
	 */
	public Integer getSequencia() {
		return sequencia;
	}

	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
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
	 * @return the dataLancamentoPrevista
	 */
	public Date getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}

	/**
	 * @param dataLancamentoPrevista the dataLancamentoPrevista to set
	 */
	public void setDataLancamentoPrevista(Date dataLancamentoPrevista) {
		this.dataLancamentoPrevista = dataLancamentoPrevista;
	}

	/**
	 * @return the dataLancamentoDistribuidor
	 */
	public Date getDataLancamentoDistribuidor() {
		return dataLancamentoDistribuidor;
	}

	/**
	 * @param dataLancamentoDistribuidor the dataLancamentoDistribuidor to set
	 */
	public void setDataLancamentoDistribuidor(Date dataLancamentoDistribuidor) {
		this.dataLancamentoDistribuidor = dataLancamentoDistribuidor;
	}

	/**
	 * @return the dataRecolhimentoPrevista
	 */
	public Date getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	/**
	 * @param dataRecolhimentoPrevista the dataRecolhimentoPrevista to set
	 */
	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
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
	 * @return the novaDataLancamento
	 */
	public Date getNovaDataLancamento() {
		return novaDataLancamento;
	}

	/**
	 * @param novaDataLancamento the novaDataLancamento to set
	 */
	public void setNovaDataLancamento(Date novaDataLancamento) {
		this.novaDataLancamento = novaDataLancamento;
	}

	/**
	 * @return the possuiEstudo
	 */
	public boolean isPossuiEstudo() {
		return possuiEstudo;
	}

	/**
	 * @param possuiEstudo the possuiEstudo to set
	 */
	public void setPossuiEstudo(boolean possuiEstudo) {
		this.possuiEstudo = possuiEstudo;
	}

	/**
	 * @return the possuiRecebimentoFisico
	 */
	public boolean isPossuiRecebimentoFisico() {
		return possuiRecebimentoFisico;
	}

	/**
	 * @param possuiRecebimentoFisico the possuiRecebimentoFisico to set
	 */
	public void setPossuiRecebimentoFisico(boolean possuiRecebimentoFisico) {
		this.possuiRecebimentoFisico = possuiRecebimentoFisico;
	}
	
}
