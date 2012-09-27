package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class FiltroCalculaParcelas implements Serializable{

	private static final long serialVersionUID = -1866576603729081522L;
	
	private String periodicidade;
	
	private List<Integer> semanalDias;
	
	private String quinzenalDia1; 
	
	private String quinzenalDia2;
	
	private String mensalDia;
	
	private TipoCobranca tipoPagamento;
	
	private Integer qntdParcelas;
	
	private String valorSelecionado;

	public String getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public List<Integer> getSemanalDias() {
		return semanalDias;
	}

	public void setSemanalDias(List<Integer> semanalDias) {
		this.semanalDias = semanalDias;
	}

	public String getQuinzenalDia1() {
		return quinzenalDia1;
	}

	public void setQuinzenalDia1(String quinzenalDia1) {
		this.quinzenalDia1 = quinzenalDia1;
	}

	public String getQuinzenalDia2() {
		return quinzenalDia2;
	}

	public void setQuinzenalDia2(String quinzenalDia2) {
		this.quinzenalDia2 = quinzenalDia2;
	}

	public String getMensalDia() {
		return mensalDia;
	}

	public void setMensalDia(String mensalDia) {
		this.mensalDia = mensalDia;
	}

	public TipoCobranca getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoCobranca tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public Integer getQntdParcelas() {
		return qntdParcelas;
	}

	public void setQntdParcelas(Integer qntdParcelas) {
		this.qntdParcelas = qntdParcelas;
	}

	public String getValorSelecionado() {
		return valorSelecionado;
	}

	public void setValorSelecionado(String valorSelecionado) {
		this.valorSelecionado = valorSelecionado;
	}
	
	
	
	

}
