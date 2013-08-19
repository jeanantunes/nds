package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupChamadaoDTO implements Serializable {

	private static final long serialVersionUID = 3078882380053644466L;

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
    private Integer numeroCota;    
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	
   	private BigDecimal valorTotalConsignado;   	
	
    private Date dataHistoricoEdicao;

	@Export(label = "Data Programado", alignment=Alignment.CENTER, exhibitionOrder = 5)
   	private String dataProgramadoChamadao;
	
	private Long qtdDiasSuspensao;
	
	private String valorTotalConsignadoFormatado;
	
	//Construtores
    public ConsultaFollowupChamadaoDTO(){}
    
    public ConsultaFollowupChamadaoDTO(Integer numeroCota, String nomeJornaleiro,
			BigDecimal valorTotalConsignado, Date dataHistoricoEdicao,
			String dataProgramadoChamadao) {
		super();
		this.numeroCota = numeroCota;
		this.nomeJornaleiro = nomeJornaleiro;
		this.valorTotalConsignado = valorTotalConsignado;
		this.dataHistoricoEdicao = dataHistoricoEdicao;
		this.dataProgramadoChamadao = dataProgramadoChamadao;
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
	public BigDecimal getValorTotalConsignado() {
		return valorTotalConsignado;
	}
	public void setValorTotalConsignado(BigDecimal valorTotalConsignado) {
		this.valorTotalConsignado = valorTotalConsignado;
		if (valorTotalConsignado != null) {
			valorTotalConsignadoFormatado = CurrencyUtil.formatarValor(valorTotalConsignado);
		}
	}
	
	@Export(label = "Consignado R$", alignment=Alignment.RIGHT, exhibitionOrder = 3)
	public String getValorTotalConsignadoFormatado() {
		return valorTotalConsignadoFormatado;
	}

	public String getDataProgramadoChamadao() {
		return dataProgramadoChamadao;
	}
	public void setDataProgramadoChamadao(Date dataProgramadoChamadao) {
		this.dataProgramadoChamadao = DateUtil.formatarData(dataProgramadoChamadao, Constantes.DATE_PATTERN_PT_BR) ;		
	}
	
	public Date getDataHistoricoEdicao() {
		return dataHistoricoEdicao;
	}

	public void setDataHistoricoEdicao(Date dataHistoricoEdicao) {
		this.dataHistoricoEdicao = dataHistoricoEdicao;
		qtdDiasSuspensao = DateUtil.obterDiferencaDias(new Date(), getDataHistoricoEdicao());
	}
	
	@Export(label = "Suspenso (dias)", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	public Long getQtdDiasSuspensao() {
		return qtdDiasSuspensao;
	}
	
}