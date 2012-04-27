package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;

/**
 * Representa um Periodo de funcionamento do PDV
 * 
 */
public class PeriodoFuncionamentoDTO implements Serializable {

	private static final long serialVersionUID = 781210018161144055L;

	
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
		this.tipoPeriodoFuncionamentoPDV = tipoPeriodoFuncionamentoPDV;
		this.inicio = inicio;
		this.fim = fim;
	}



	/**
	 * Obtém lista com os possíveis peridos a serem selecionados
	 * 
	 * @param selecionados - Periodos já selecionados
	 * @return - períodos que ainda podem ser selecionados
	 */
	public static List<TipoPeriodoFuncionamentoPDV> getPeriodosPossiveis(List<PeriodoFuncionamentoDTO> selecionados) {
		
		List<TipoPeriodoFuncionamentoPDV> possiveis = new ArrayList<TipoPeriodoFuncionamentoPDV>();
		
		for(TipoPeriodoFuncionamentoPDV periodo: TipoPeriodoFuncionamentoPDV.values()) {
			
			try{
				selecionados.add(new PeriodoFuncionamentoDTO(periodo, null,null));
				validarPeriodos(selecionados);
				selecionados.remove(selecionados.size() - 1);
				
				possiveis.add(periodo);
			} catch (Exception e) {
				selecionados.remove(selecionados.size() - 1);
			}
		}
		return possiveis;
	}
	
	/**
	 * Valida se uma lista de períodos é valida, de acordo com as regras definidas na EMS 0159
	 * 
	 * @param listaTipos
	 * @throws Exception
	 */
	public static void validarPeriodos(List<PeriodoFuncionamentoDTO> periodos) throws Exception {
		
		List<TipoPeriodoFuncionamentoPDV> listaTipos = new ArrayList<TipoPeriodoFuncionamentoPDV>();
		
		for(PeriodoFuncionamentoDTO p : periodos) {
			listaTipos.add(p.getTipoPeriodoFuncionamentoPDV());
		}
		
		validarDuplicidadeDePeriodo(listaTipos);
		
		if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.DIARIA)) {
			
			if(listaTipos.size()>1) {
				
				throw new Exception("Ao selecionar " + TipoPeriodoFuncionamentoPDV.DIARIA.getDescricao() + ", nenhum outro item deve ser incluido.");
			} 
		
		} else if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS)) {
			
			if(listaTipos.size() > 1) {
				
				throw new Exception("Ao selecionar " + TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS.getDescricao() + ", nenhum outro item deve ser incluido.");
			} 
		
		} else if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEGUNDA_SEXTA)) {
			
			if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEGUNDA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.TERCA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.QUARTA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.QUINTA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEXTA_FEIRA)) {
				
				throw new Exception("Ao selecionar o período de '"+TipoPeriodoFuncionamentoPDV.SEGUNDA_SEXTA.getDescricao()+"', não é permitido a selecao específica de um dia da semana.");				
			}
		} else if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA)) {
			
			if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SABADO)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.DOMINGO)) {
				
				throw new Exception("Ao selecionar o período de '"+TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA.getDescricao()+"', não é permitido a definição específíca para sábado ou domingo.");				
			}
		}		
	}

	/**
	 * Valida duplicidade de período
	 * 
	 * @param periodos - periodos
	 * @throws Exception - Exceção ao encontrar registro duplicado.
	 */
	private static void validarDuplicidadeDePeriodo(List<TipoPeriodoFuncionamentoPDV> periodos) throws Exception {
		
		List<TipoPeriodoFuncionamentoPDV> clone = new ArrayList<TipoPeriodoFuncionamentoPDV>(periodos);
		
		for(TipoPeriodoFuncionamentoPDV item : periodos) {
			clone.remove(item);
		}
		
		if(clone.size() > 0) {
			throw new Exception("O período " + 
					clone.get(0).getDescricao() + 
					" foi incluido a lista mais de uma vez.");
		}
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
		this.tipoPeriodoFuncionamentoPDV = tipoPeriodoFuncionamentoPDV;
	}
	
	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodoFuncionamentoPDV = TipoPeriodoFuncionamentoPDV.valueOf(tipoPeriodo);
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

	
}
