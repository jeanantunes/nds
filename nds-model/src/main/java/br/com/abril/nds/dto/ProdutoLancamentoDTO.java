package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
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
	
	private Long idFornecedor;
	
	private String tipoLancamento;
	
	private Long numeroEdicao;

	private BigDecimal precoVenda;
	
	private StatusLancamento status;
	
	private String statusLancamento;

	private Long peso;

	private String codigoProduto;
	
	private String codigoProdutoFormatado;

	private String nomeProduto;
	
	private BigInteger repartePrevisto;
	
	private BigInteger reparteFisico;
	
	private Long idLancamento;

	private TipoLancamentoParcial parcial;
	
	private String descricaoLancamento;
	
	private Date dataLancamentoPrevista;

	private Date dataLancamentoDistribuidor;
	
	private Date dataRecolhimentoPrevista;
	
	private Date dataRecolhimentoDistribuidor;

	private BigDecimal valorTotal;

	private Date novaDataLancamento;
		
	private PeriodicidadeProduto periodicidadeProduto;
	
	private Integer ordemPeriodicidadeProduto;
  	
  	private boolean possuiFuro;
  	
  	private boolean lancamentoAgrupado;
  	
  	private List<ProdutoLancamentoDTO> produtosLancamentoAgrupados = new ArrayList<ProdutoLancamentoDTO>();
  	
  	private boolean alteradoInteface;
  	
  	private BigInteger distribuicao;
  	
  	private Integer sequenciaMatriz;
  	
  	private Long peb;
  	
  	private boolean cancelado;
	
  	private boolean alterado;
  	
  	private String nomeFantasia;
  	
  	private boolean produtoContaFirme;
  	
  	
  	private String descontoLogistica;
  	
  	
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}

	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {

		this.tipoLancamento = tipoLancamento;
	}

	public boolean isAlterado() {
		return alterado;
	}

	public void setAlterado(boolean alterado) {
		this.alterado = alterado;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

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
	public String getStatusLancamento() {
		return statusLancamento;
	}

	/**
	 * @param status do lançamento em formato de String para ser convertida
	 */
	public void setStatusLancamento(String statusLancamento) {
		
		this.statusLancamento = statusLancamento;
		
		this.status = Util.getEnumByStringValue(StatusLancamento.values(), statusLancamento);
	}

	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
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
		
		this.codigoProdutoFormatado = Util.padLeft(codigoProduto, "0", 30);
	}

	/**
	 * @return the codigoProdutoFormatado
	 */
	public String getCodigoProdutoFormatado() {
		return codigoProdutoFormatado;
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
	public BigInteger getRepartePrevisto() {
		return repartePrevisto;
	}

	/**
	 * @param repartePrevisto the repartePrevisto to set
	 */
	public void setRepartePrevisto(BigInteger repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}

	/**
	 * @return the reparteFisico
	 */
	public BigInteger getReparteFisico() {
		return reparteFisico;
	}

	/**
	 * @param reparteFisico the reparteFisico to set
	 */
	public void setReparteFisico(BigInteger reparteFisico) {
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
		
		if (this.tipoLancamento != null && this.tipoLancamento.equals(TipoLancamento.REDISTRIBUICAO.name())) {
			this.descricaoLancamento = TipoLancamento.REDISTRIBUICAO.getDescricao();

		} else if (this.parcial != null) {
			if(this.parcial.equals(TipoLancamentoParcial.FINAL.name())){
			 
				this.descricaoLancamento = TipoLancamentoParcial.FINAL.getDescricao();
			 
	 	    } if(this.parcial.equals(TipoLancamentoParcial.PARCIAL.name())){
			
	 	    	this.descricaoLancamento = TipoLancamentoParcial.PARCIAL.getDescricao();
	 	    } else{
	 	    	
	 	    	this.descricaoLancamento = this.parcial.getDescricao();
	 	    }

		} else {
			this.descricaoLancamento = TipoLancamento.LANCAMENTO.getDescricao();
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
	
	public boolean isStatusLancamentoFuro() {
		return StatusLancamento.FURO.equals(status);
	}
	
	public boolean isStatusLancamentoEmBalanceamento() {
		return StatusLancamento.EM_BALANCEAMENTO.equals(status);
	}
	
	public boolean isStatusLancamentoBalanceado() {
		return StatusLancamento.BALANCEADO.equals(status);
	}
	
	public boolean isStatusLancamentoExpedido() {
		return StatusLancamento.EXPEDIDO.equals(status);
	}

	/**
	 * @return the distribuicao
	 */
	public BigInteger getDistribuicao() {
		return distribuicao;
	}

	/**
	 * @param distribuicao the distribuicao to set
	 */
	public void setDistribuicao(BigInteger distribuicao) {
		this.distribuicao = distribuicao;
	}

	/**
	 * @return the sequenciaMatriz
	 */
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	/**
	 * @param sequenciaMatriz the sequenciaMatriz to set
	 */
	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
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
	
	

	public boolean isProdutoContaFirme() {
		return produtoContaFirme;
	}

	public void setProdutoContaFirme(boolean produtoContaFirme) {
		this.produtoContaFirme = produtoContaFirme;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((idLancamento == null) ? 0 : idLancamento.hashCode());
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
		ProdutoLancamentoDTO other = (ProdutoLancamentoDTO) obj;
		if (idLancamento == null) {
			if (other.idLancamento != null)
				return false;
		} else if (!idLancamento.equals(other.idLancamento))
			return false;
		return true;
	}

	public Long getPeb() {
		return peb;
	}

	public void setPeb(Long peb) {
		this.peb = peb;
	}

	public String getDescontoLogistica() {
		return descontoLogistica;
	}

	public void setDescontoLogistica(String descontoLogistica) {
		this.descontoLogistica = descontoLogistica;
	}
	
}
