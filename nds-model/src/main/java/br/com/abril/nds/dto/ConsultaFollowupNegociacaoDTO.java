package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupNegociacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3176883412810853021L;
	
	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
    private Integer numeroCota;   
	
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	
	private BigDecimal valorParcela;    
	
	@Export(label = "Negociacao", alignment=Alignment.CENTER, exhibitionOrder = 3)
    private String valorParcelaFormatado;
	
	@Export(label = "Parcela", alignment=Alignment.CENTER, exhibitionOrder = 4)
    private String descricaoParcelamento;    
	
	@Export(label = "Forma de Pagamento", alignment=Alignment.CENTER, exhibitionOrder = 5)
    private String descricaoFormaPagamento;    
	
	@Export(label = "Data Vencto", alignment=Alignment.CENTER, exhibitionOrder = 6)
   	private String dataVencimentoFormatada;
	
	private Date dataVencimento;
		
	private Long idNegociacao;
	
	private Long numeroParcelaAtual;
	
	private Long quantidadeParcelas;
	
	private TipoCobranca tipoCobranca;
	
   	public ConsultaFollowupNegociacaoDTO() {   		
   	}
   	
   	public ConsultaFollowupNegociacaoDTO(Long idNegociacao,Integer numeroCota, String nomeJornaleiro, BigDecimal valorParcela,    
   	    String descricaoParcelamento, String descricaoFormaPagamento, Date dataVencimento ) {   	
   	    this.numeroCota = numeroCota;    
   	    this.nomeJornaleiro = nomeJornaleiro;	
   	    this.valorParcela = valorParcela;    
   	    this.valorParcelaFormatado = CurrencyUtil.formatarValor(valorParcela);   	    
   	    this.descricaoParcelamento = descricaoParcelamento; 
   	    this.descricaoFormaPagamento = descricaoFormaPagamento; 
   	    this.dataVencimento = dataVencimento; 
   	    this.idNegociacao = idNegociacao;
   	}

  
	/**
	 * @return the dataVencimentoFormatada
	 */
	public String getDataVencimentoFormatada() {
		return dataVencimentoFormatada;
	}

	/**
	 * @param dataVencimentoFormatada the dataVencimentoFormatada to set
	 */
	public void setDataVencimentoFormatada(String dataVencimentoFormatada) {
		this.dataVencimentoFormatada = dataVencimentoFormatada;
	}

	/**
	 * @return the tipoCobranca
	 */
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	/**
	 * @return the numeroParcelaAtual
	 */
	public Long getNumeroParcelaAtual() {
		return numeroParcelaAtual;
	}

	/**
	 * @param numeroParcelaAtual the numeroParcelaAtual to set
	 */
	public void setNumeroParcelaAtual(Long numeroParcelaAtual) {
		this.numeroParcelaAtual = numeroParcelaAtual;
	}

	/**
	 * @return the quantidadeParcelas
	 */
	public Long getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	/**
	 * @param quantidadeParcelas the quantidadeParcelas to set
	 */
	public void setQuantidadeParcelas(Long quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	/**
	 * @return the idNegociacao
	 */
	public Long getIdNegociacao() {
		return idNegociacao;
	}

	/**
	 * @param idNegociacao the idNegociacao to set
	 */
	public void setIdNegociacao(Long idNegociacao) {
		this.idNegociacao = idNegociacao;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	/**
	 * @return the valorParcela
	 */
	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	/**
	 * @param valorParcela the valorParcela to set
	 */
	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public String getDescricaoParcelamento() {
		return descricaoParcelamento;
	}

	public void setDescricaoParcelamento(String descricaoParcelamento) {
		this.descricaoParcelamento = descricaoParcelamento;
	}

	public String getDescricaoFormaPagamento() {
		return descricaoFormaPagamento;
	}

	public void setDescricaoFormaPagamento(String descricaoFormaPagamento) {
		this.descricaoFormaPagamento = descricaoFormaPagamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getValorParcelaFormatado() {
		return valorParcelaFormatado;
	}

	public void setValorParcelaFormatado(String valorParcelaFormatado) {
		this.valorParcelaFormatado = valorParcelaFormatado;
	}

}
