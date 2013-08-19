package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class HistoricoVendaPopUpCotaDto {

	private Integer numeroCota;
	private String nomePessoa;
	private TipoDistribuicaoCota tipoDistribuicaoCota;
	private Long rankId;
	private BigDecimal faturamento;
	private String faturamentoFormatado;
	private Date dataGeracao;
	private String dataGeracaoFormat;

	public String getFaturamentoFormatado() {
		return faturamentoFormatado;
	}

	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
		this.faturamentoFormatado = CurrencyUtil.formatarValor(faturamento);
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getDataGeracaoFormat() {
		return dataGeracaoFormat;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
		this.dataGeracaoFormat = DateUtil.formatarData(dataGeracao, "MM/yyyy");
	}

	public TipoDistribuicaoCota getTipoDistribuicaoCota() {
		return tipoDistribuicaoCota;
	}

	public void setTipoDistribuicaoCota(
			TipoDistribuicaoCota tipoDistribuicaoCota) {
		this.tipoDistribuicaoCota = tipoDistribuicaoCota;
	}

	public Long getRankId() {
		return rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

}
