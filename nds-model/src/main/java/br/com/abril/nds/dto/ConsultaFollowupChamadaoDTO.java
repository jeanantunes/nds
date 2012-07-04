package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupChamadaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3078882380053644466L;

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
    private Long numeroCota;    
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	
	@Export(label = "Consignado R$", alignment=Alignment.RIGHT, exhibitionOrder = 3)
   	private BigDecimal valorTotalConsignado;   	
	
	@Export(label = "Suspenso (dias)", alignment=Alignment.RIGHT, exhibitionOrder = 4)
    private Date dataHistoricoEdicao;

	@Export(label = "Data Programado", alignment=Alignment.CENTER, exhibitionOrder = 5)
   	private Date dataProgramadoChamadao;   	

    public ConsultaFollowupChamadaoDTO(){    	
    }
    
    public ConsultaFollowupChamadaoDTO(Long numeroCota, String nomeJornaleiro,
			BigDecimal valorTotalConsignado, Date dataHistoricoEdicao,
			Date dataProgramadoChamadao) {
		super();
		this.numeroCota = numeroCota;
		this.nomeJornaleiro = nomeJornaleiro;
		this.valorTotalConsignado = valorTotalConsignado;
		this.dataHistoricoEdicao = dataHistoricoEdicao;
		this.dataProgramadoChamadao = dataProgramadoChamadao;
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
	public BigDecimal getValorTotalConsignado() {
		return valorTotalConsignado;
	}
	public void setValorTotalConsignado(BigDecimal valorTotalConsignado) {
		this.valorTotalConsignado = valorTotalConsignado;
	}
	public Date getDataProgramadoChamadao() {
		return dataProgramadoChamadao;
	}
	public void setDataProgramadoChamadao(Date dataProgramadoChamadao) {
		this.dataProgramadoChamadao = dataProgramadoChamadao;
	}
	public Date getDataHistoricoEdicao() {
		return dataHistoricoEdicao;
	}

	public void setDataHistoricoEdicao(Date dataHistoricoEdicao) {
		this.dataHistoricoEdicao = dataHistoricoEdicao;
	}
	
	
}