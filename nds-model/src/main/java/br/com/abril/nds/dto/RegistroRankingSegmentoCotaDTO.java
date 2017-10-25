package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroRankingSegmentoCotaDTO implements Serializable {

	private static final long serialVersionUID = -1125371111579523027L;

	@Export(label="Ranking", exhibitionOrder=1)
	private Long ranking;
	
	@Export(label="Segmento", exhibitionOrder=2)
	private String segmentoDescricao;
	
	@Export(label="Faturamento Capa R$", exhibitionOrder=3)
	private String faturamentoCapaFormatado;
	
	@Export(label="Part. %", exhibitionOrder=4)
	private String participacaoFormatado;
	
	@Export(label="Part. Acum. %", exhibitionOrder=5)
	private String participacaoAcumuladaFormatado;
	

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

	public String getFaturamentoCapaFormatado() {
		return faturamentoCapaFormatado;
	}

	public void setFaturamentoCapaFormatado(String faturamentoCapaFormatado) {
		this.faturamentoCapaFormatado = faturamentoCapaFormatado;
	}
	
	public String getSegmentoDescricao() {
		return segmentoDescricao;
	}

	public void setSegmentoDescricao(String segmentoDescricao) {
		this.segmentoDescricao = segmentoDescricao;
	}

	public String getParticipacaoFormatado() {
		return participacaoFormatado;
	}

	public void setParticipacaoFormatado(String participacaoFormatado) {
		this.participacaoFormatado = participacaoFormatado;
	}

	public String getParticipacaoAcumuladaFormatado() {
		return participacaoAcumuladaFormatado;
	}

	public void setParticipacaoAcumuladaFormatado(String participacaoAcumuladaFormatado) {
		this.participacaoAcumuladaFormatado = participacaoAcumuladaFormatado;
	}
	
}
