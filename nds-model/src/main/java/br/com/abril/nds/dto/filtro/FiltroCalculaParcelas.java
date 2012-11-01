package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.dto.DiaSemanaDTO;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public class FiltroCalculaParcelas extends FiltroConsultaNegociacaoDivida implements Serializable{

	private static final long serialVersionUID = -1866576603729081522L;
	
	private PeriodicidadeCobranca periodicidade;
	
	private List<DiaSemanaDTO> semanalDias;
	
	private Integer quinzenalDia1; 
	
	private Integer quinzenalDia2;
	
	private Integer mensalDia;
	
	private TipoCobranca tipoPagamento;
	
	private Integer qntdParcelas;
	
	private Double valorSelecionado;
	
	private List<String> listDividas;
	
	private Long idBanco;
	
	private Boolean isentaEncargos;

	public PeriodicidadeCobranca getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeCobranca periodicidade) {
		this.periodicidade = periodicidade;
	}

	public Integer getQuinzenalDia1() {
		return quinzenalDia1;
	}

	public void setQuinzenalDia1(Integer quinzenalDia1) {
		this.quinzenalDia1 = quinzenalDia1;
	}

	public Integer getQuinzenalDia2() {
		return quinzenalDia2;
	}

	public void setQuinzenalDia2(Integer quinzenalDia2) {
		this.quinzenalDia2 = quinzenalDia2;
	}

	public Integer getMensalDia() {
		return mensalDia;
	}

	public void setMensalDia(Integer mensalDia) {
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

	public Double getValorSelecionado() {
		return valorSelecionado;
	}

	public void setValorSelecionado(Double valorSelecionado) {
		this.valorSelecionado = valorSelecionado;
	}

	public List<String> getListDividas() {
		return listDividas;
	}

	public void setListDividas(List<String> listDividas) {
		this.listDividas = listDividas;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public Boolean getIsentaEncargos() {
		return isentaEncargos;
	}

	public void setIsentaEncargos(Boolean isentaEncargos) {
		this.isentaEncargos = isentaEncargos;
	}

	public List<DiaSemanaDTO> getSemanalDias() {
		return semanalDias;
	}

	public void setSemanalDias(List<DiaSemanaDTO> semanalDias) {
		this.semanalDias = semanalDias;
	}

	
	
	
	
	

}
