package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.Util;

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
	
	private Long idProduto;
	
	private Long numeroEdicao;

	private BigDecimal precoVenda;
	
	private StatusLancamento statusLancamento;

	private Long peso;

	private String codigoProduto;

	private String nomeProduto;
	
	private BigDecimal repartePrevisto;
	
	private BigDecimal reparteFisico;
	
	private Long idLancamento;

	private TipoLancamentoParcial parcial;
	
	private String descricaoLancamento;
	
	private Date dataLancamentoPrevista;

	private Date dataLancamentoDistribuidor;
	
	private Date dataRecolhimentoPrevista;

	private BigDecimal valorTotal;

	private Date novaDataLancamento;
	
	private Integer numeroReprogramacoes;
	
	private boolean possuiRecebimentoFisico;
	
	private PeriodicidadeProduto periodicidadeProduto;
	
	private Integer ordemPeriodicidadeProduto;
  	
  	private String distribuicao;
  	
  	private boolean possuiFuro;
  	
  	private boolean lancamentoAgrupado;
  	
  	private List<ProdutoLancamentoDTO> produtosLancamentoAgrupados = new ArrayList<ProdutoLancamentoDTO>();
  	
  	private boolean alteradoInteface;
	
	/**
	 * Construtor padrão.
	 */
	public ProdutoLancamentoDTO() {
		
	}

	/**
	 * Verifica se o produto permite reprogramação ou não
	 */
	public boolean permiteReprogramacao() {
		
		if (this.possuiRecebimentoFisico
				&& this.numeroReprogramacoes != null
				&& this.numeroReprogramacoes >= Constantes.NUMERO_REPROGRAMACOES_LIMITE) {
			
			return false;
		}
		
		if (StatusLancamento.CANCELADO_GD.equals(this.statusLancamento)) {
			
			return false;
		}
		
		return true;
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
	 * @return the idProduto
	 */
	public Long getIdProduto() {
		return idProduto;
	}

	/**
	 * @param idProduto the idProduto to set
	 */
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
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
	 * @return the statusLancamento
	 */
	public StatusLancamento getStatusLancamento() {
		return statusLancamento;
	}

	/**
	 * @param status do lançamento em formato de String para ser convertida
	 */
	public void setStatusLancamento(String statusLancamento) {
		
		this.statusLancamento = Util.getEnumByStringValue(StatusLancamento.values(), statusLancamento);
	}

	/**
	 * @return the peso
	 */
	public Long getPeso() {
		return peso;
	}

	/**
	 * @param peso
	 */
	public void setPeso(Long peso) {
		this.peso = peso;
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
	 * @return the parcial
	 */
	public TipoLancamentoParcial getParcial() {
		return parcial;
	}

	/**
	 * @param parcial em formato de String para ser convertida.
	 */
	public void setParcial(String parcial) {
		
		this.parcial = Util.getEnumByStringValue(TipoLancamentoParcial.values(), parcial);
		
		if (this.parcial == null) {
			this.descricaoLancamento = "Lancamento";
		} else {
			this.descricaoLancamento = this.parcial.getDescricao();
		}
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
		this.valorTotal = valorTotal == null ? BigDecimal.ZERO : valorTotal;
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
	 * @return the numeroReprogramacoes
	 */
	public Integer getNumeroReprogramacoes() {
		return numeroReprogramacoes;
	}

	/**
	 * @param numeroReprogramacoes the numeroReprogramacoes to set
	 */
	public void setNumeroReprogramacoes(Integer numeroReprogramacoes) {
		this.numeroReprogramacoes = numeroReprogramacoes;
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

	/**
	 * @return the periodicidadeProduto
	 */
	public PeriodicidadeProduto getPeriodicidadeProduto() {
		return periodicidadeProduto;
	}

	/**
	 * @param periodicidadeProduto em formato de String para ser convertido.
	 */
	public void setPeriodicidadeProduto(String periodicidadeProduto) {
		this.periodicidadeProduto =
			Util.getEnumByStringValue(PeriodicidadeProduto.values(), periodicidadeProduto);
		
		this.ordemPeriodicidadeProduto = this.periodicidadeProduto.getOrdem();
	}

	/**
	 * @return the ordemPeriodicidadeProduto
	 */
	public Integer getOrdemPeriodicidadeProduto() {
		return ordemPeriodicidadeProduto;
	}

	/**
	 * @param ordemPeriodicidadeProduto the ordemPeriodicidadeProduto to set
	 */
	public void setOrdemPeriodicidadeProduto(Integer ordemPeriodicidadeProduto) {
		this.ordemPeriodicidadeProduto = ordemPeriodicidadeProduto;
	}

	/**
	 * @return the distribuicao
	 */
	public String getDistribuicao() {
		return distribuicao;
	}

	/**
	 * @param distribuicao the distribuicao to set
	 */
	public void setDistribuicao(String distribuicao) {
		this.distribuicao = distribuicao;
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

	/**
	 * @return the lancamentoAgrupado
	 */
	public boolean isLancamentoAgrupado() {
		return lancamentoAgrupado;
	}

	/**
	 * @param lancamentoAgrupado the lancamentoAgrupado to set
	 */
	public void setLancamentoAgrupado(boolean lancamentoAgrupado) {
		this.lancamentoAgrupado = lancamentoAgrupado;
	}

	/**
	 * @return the produtosLancamentoAgrupados
	 */
	public List<ProdutoLancamentoDTO> getProdutosLancamentoAgrupados() {
		return produtosLancamentoAgrupados;
	}

	/**
	 * @param produtosLancamentoAgrupados the produtosLancamentoAgrupados to set
	 */
	public void setProdutosLancamentoAgrupados(
			List<ProdutoLancamentoDTO> produtosLancamentoAgrupados) {
		this.produtosLancamentoAgrupados = produtosLancamentoAgrupados;
	}

	/**
	 * @return the alteradoInteface
	 */
	public boolean isAlteradoInteface() {
		return alteradoInteface;
	}

	/**
	 * @param alteradoInteface the alteradoInteface to set
	 */
	public void setAlteradoInteface(boolean alteradoInteface) {
		this.alteradoInteface = alteradoInteface;
	}
	
}
