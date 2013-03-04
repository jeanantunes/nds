package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;

public class HistoricoVendaPopUpCotaDto {

	private Integer numeroCota;
	private String nomePessoa;
	// TIPO COTA EMS 169
	// RANKING EMS 2017
	private BigDecimal faturamento;
	private String faturamentoFormatado;
	private String data;

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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
