package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;

public class DataLancamentoPeriodoEdicoesBasesDTO implements Serializable {
	
	private static final long serialVersionUID = 1289541161274963342L;

	private String dataLancamento;
	private Integer numPeriodo;
	
	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarDataPTBR(dataLancamento);
	}
	public Integer getNumPeriodo() {
		return numPeriodo;
	}
	public void setNumPeriodo(Integer numPeriodo) {
		this.numPeriodo = numPeriodo;
	}
}
