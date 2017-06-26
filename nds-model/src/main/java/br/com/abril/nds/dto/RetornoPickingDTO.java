package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class RetornoPickingDTO implements Serializable {

	private static final long serialVersionUID = -7194226345701411329L;
	
	private String codigoProduto;
	private String numeroEdicao;
	private String dataLancamento;
	private String dataLed;
	private String horaLed;
	
	private List<Long> idLancamentos;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public String getDataLed() {
		return dataLed;
	}
	public void setDataLed(String dataLed) {
		this.dataLed = dataLed;
	}
	public String getHoraLed() {
		return horaLed;
	}
	public void setHoraLed(String horaLed) {
		this.horaLed = horaLed;
	}
	public List<Long> getIdLancamentos() {
		return idLancamentos;
	}
	public void setIdLancamentos(List<Long> idLancamentos) {
		this.idLancamentos = idLancamentos;
	}
	
}
