package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;

/**
 * Representa um Periodo de funcionamento do PDV
 * 
 */
public class PeriodoFuncionamentoDTO implements Serializable {

	private static final long serialVersionUID = 781210018161144055L;

	private String nomeTipoPeriodo;
	
	private TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV;

	private String inicio;
	
	private String fim;

	/**
	 * Construtor padrão
	 */
	public PeriodoFuncionamentoDTO(){}
	
	
	/**
	 * Construtor com campos
	 * 
	 * @param tipoPeriodoFuncionamentoPDV
	 * @param inicio
	 * @param fim
	 */
	public PeriodoFuncionamentoDTO(
			TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV,
			String inicio, String fim) {
		super();
		this.setTipoPeriodoFuncionamentoPDV(tipoPeriodoFuncionamentoPDV);
		this.inicio = inicio;
		this.fim = fim;
	}




	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public TipoPeriodoFuncionamentoPDV getTipoPeriodoFuncionamentoPDV() {
		return tipoPeriodoFuncionamentoPDV;
	}

	public void setTipoPeriodoFuncionamentoPDV(
			TipoPeriodoFuncionamentoPDV tipoPeriodoFuncionamentoPDV) {
		this.nomeTipoPeriodo = tipoPeriodoFuncionamentoPDV.getDescricao();
		this.tipoPeriodoFuncionamentoPDV = tipoPeriodoFuncionamentoPDV;
	}
	
	public void setTipoPeriodo(String tipoPeriodo) {
		
		if(tipoPeriodo == null || tipoPeriodo.equals("-1"))
			return;
		
		this.setTipoPeriodoFuncionamentoPDV(TipoPeriodoFuncionamentoPDV.valueOf(tipoPeriodo));
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fim == null) ? 0 : fim.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		result = prime
				* result
				+ ((tipoPeriodoFuncionamentoPDV == null) ? 0
						: tipoPeriodoFuncionamentoPDV.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodoFuncionamentoDTO other = (PeriodoFuncionamentoDTO) obj;
		if (fim == null) {
			if (other.fim != null)
				return false;
		} else if (!fim.equals(other.fim))
			return false;
		if (inicio == null) {
			if (other.inicio != null)
				return false;
		} else if (!inicio.equals(other.inicio))
			return false;
		if (tipoPeriodoFuncionamentoPDV != other.tipoPeriodoFuncionamentoPDV)
			return false;
		return true;
	}


	public String getNomeTipoPeriodo() {
		return nomeTipoPeriodo;
	}


	public void setNomeTipoPeriodo(String nomeTipoPeriodo) {
		this.nomeTipoPeriodo = nomeTipoPeriodo;
	}

	
}
