package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupStatusCotaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9049896618190379822L;

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private Long numeroCota;   
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	@Export(label = "Periodo", alignment=Alignment.CENTER, exhibitionOrder = 2)
    private String periodoStatus;    
	@Export(label = "Status", alignment=Alignment.CENTER, exhibitionOrder = 2)
    private String statusAtual;    
	@Export(label = "Novo Status", alignment=Alignment.CENTER, exhibitionOrder = 2)
    private String statusNovo;    
	@Export(label = "Data Vencto", alignment=Alignment.CENTER, exhibitionOrder = 2)
   	private Date dataVencimento;   	

   	public ConsultaFollowupStatusCotaDTO() {   		
   	}
   	
   	public ConsultaFollowupStatusCotaDTO( Long numeroCota, String nomeJornaleiro, String periodoStatus,     
       String statusAtual, String statusNovo, Date dataVencimento) {   		
   		this.numeroCota = numeroCota;   
   		this.nomeJornaleiro = nomeJornaleiro;	
   		this.periodoStatus = periodoStatus;    
   		this.statusAtual = statusAtual;    
   		this.statusNovo = statusNovo;    
   		this.dataVencimento = dataVencimento;    	
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
	public String getPeriodoStatus() {
		return periodoStatus;
	}
	public void setPeriodoStatus(String periodoStatus) {
		this.periodoStatus = periodoStatus;
	}
	public String getStatusAtual() {
		return statusAtual;
	}
	public void setStatusAtual(String statusAtual) {
		this.statusAtual = statusAtual;
	}
	public String getStatusNovo() {
		return statusNovo;
	}
	public void setStatusNovo(String statusNovo) {
		this.statusNovo = statusNovo;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

}
