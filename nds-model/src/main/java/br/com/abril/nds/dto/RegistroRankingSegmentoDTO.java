package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
@Exportable
public class RegistroRankingSegmentoDTO extends RegistroCurvaABCDTO implements Serializable {

	private static final long serialVersionUID = 4187363929072315365L;

	@Export(label="Ranking",exhibitionOrder=1, widthPercent=10)
	private Long ranking;
	
	@Export(label="Cota",exhibitionOrder=2, widthPercent=10)
	private Integer numeroCota;
	
	@Export(label="Nome",exhibitionOrder=3, widthPercent=40)
	private String nomeCota;

	private BigDecimal faturamentoCapa;
	
	@Export(label="Faturamento Capa R$",exhibitionOrder=4)
	private String faturamentoCapaFormatado;
	
	private BigDecimal totalFaturamento;

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
		this.setFaturamentoCapaFormatado(CurrencyUtil.formatarValor(faturamentoCapa));
	}

	public String getFaturamentoCapaFormatado() {
		return faturamentoCapaFormatado;
	}

	public void setFaturamentoCapaFormatado(String faturamentoCapaFormatado) {
		this.faturamentoCapaFormatado = faturamentoCapaFormatado;
	}
	
	@Override
	@Export(label="Part. %", exhibitionOrder=5)
	public String getParticipacaoFormatado() {
		return super.getParticipacaoFormatado();
	}
	
	@Override
	@Export(label="Part. Acum. %", exhibitionOrder=6)
	public String getParticipacaoAcumuladaFormatado() {
		return super.getParticipacaoAcumuladaFormatado();
	}

	public BigDecimal getTotalFaturamento() {
		return totalFaturamento;
	}

	public void setTotalFaturamento(BigDecimal totalFaturamento) {
		this.totalFaturamento = totalFaturamento;
	}
	
}
