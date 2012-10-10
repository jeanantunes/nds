package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

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
    private Long numeroCota;   
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	@Export(label = "Negociacao", alignment=Alignment.CENTER, exhibitionOrder = 3)
    private String descricaoNegociacao;    
	@Export(label = "Parcela", alignment=Alignment.CENTER, exhibitionOrder = 4)
    private String descricaoParcelamento;    
	@Export(label = "Forma de Pagamento", alignment=Alignment.CENTER, exhibitionOrder = 5)
    private String descricaoFormaPagamento;    
	@Export(label = "Data Vencto", alignment=Alignment.CENTER, exhibitionOrder = 6)
   	private Date dataVencimento;   	
	
	private Long idNegociacao;

   	public ConsultaFollowupNegociacaoDTO() {   		
   	}

   	public ConsultaFollowupNegociacaoDTO(Long idNegociacao, Long numeroCota, String nomeJornaleiro, String descricaoNegociacao,    
   	    String descricaoParcelamento, String descricaoFormaPagamento, Date dataVencimento ) {   	
   	    this.numeroCota = numeroCota;    
   	    this.nomeJornaleiro = nomeJornaleiro;	
   	    this.descricaoNegociacao = descricaoNegociacao;     
   	    this.descricaoParcelamento = descricaoParcelamento; 
   	    this.descricaoFormaPagamento = descricaoFormaPagamento; 
   	    this.dataVencimento = dataVencimento; 
   	    this.idNegociacao = idNegociacao;
   	}
   	
	public Long getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Long numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	public String getDescricaoNegociacao() {
		return descricaoNegociacao;
	}

	public void setDescricaoNegociacao(String descricaoNegociacao) {
		this.descricaoNegociacao = descricaoNegociacao;
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

	public Long getIdNegociacao() {
		return idNegociacao;
	}

	public void setIdNegociacao(Long idNegociacao) {
		this.idNegociacao = idNegociacao;
	}

}
