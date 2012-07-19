package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupStatusCotaDTO implements Serializable {

	private static final long serialVersionUID = 9049896618190379822L;

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private Integer numeroCota;   
	
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	
	@Export(label = "Periodo", alignment=Alignment.CENTER, exhibitionOrder = 3)
    private String periodoStatus;   
	
	@Export(label = "Status", alignment=Alignment.CENTER, exhibitionOrder = 4)
    private SituacaoCadastro statusAtual;    
	
	@Export(label = "Novo Status", alignment=Alignment.CENTER, exhibitionOrder = 5)
    private SituacaoCadastro statusNovo;    
	
	@Export(label = "Data Vencto", alignment=Alignment.CENTER, exhibitionOrder = 6)
   	private Date dataVencimento;
	
	private String dataInicioPeriodo; 
	private String dataFimPeriodo;

   	public ConsultaFollowupStatusCotaDTO() {   		
   	}
   	
   	public ConsultaFollowupStatusCotaDTO(Integer numeroCota,
			String nomeJornaleiro, SituacaoCadastro statusAtual,
			SituacaoCadastro statusNovo, String dataInicioPeriodo,
			String dataFimPeriodo) {
		super();
		this.numeroCota = numeroCota;
		this.nomeJornaleiro = nomeJornaleiro;
		this.statusAtual = statusAtual;
		this.statusNovo = statusNovo;
		this.dataInicioPeriodo = dataInicioPeriodo;
		this.dataFimPeriodo = dataFimPeriodo;
		setPeriodoStatus("GHOMMA");
	}



	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {		
		this.numeroCota = numeroCota;
		setPeriodoStatus("ghomma");
		
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
		this.periodoStatus =  periodoStatus;
		
	}
	public SituacaoCadastro getStatusAtual() {
		return statusAtual;
	}
	public void setStatusAtual(SituacaoCadastro statusAtual) {
		this.statusAtual = statusAtual;
	}
	public SituacaoCadastro getStatusNovo() {
		return statusNovo;
	}
	public void setStatusNovo(SituacaoCadastro statusNovo) {
		this.statusNovo = statusNovo;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getDataInicioPeriodo() {
		return dataInicioPeriodo;
	}

	public void setDataInicioPeriodo(Date dataInicioPeriodo) {
		this.dataInicioPeriodo = DateUtil.formatarData(dataInicioPeriodo, Constantes.DATE_PATTERN_PT_BR);
	}

	public String getDataFimPeriodo() {
		return dataFimPeriodo;
	}

	public void setDataFimPeriodo(Date dataFimPeriodo) {
		this.dataFimPeriodo =  DateUtil.formatarData(dataFimPeriodo, Constantes.DATE_PATTERN_PT_BR);
	}
	

}
