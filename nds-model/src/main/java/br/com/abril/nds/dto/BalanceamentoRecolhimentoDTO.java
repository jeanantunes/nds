package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO com dados do balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4498486203318581867L;
	
	private Map<Date, List<RecolhimentoDTO>> matrizRecolhimento;

	/**
	 * Construtor padrão.
	 */
	public BalanceamentoRecolhimentoDTO() {
		
	}

	/**
	 * @return the matrizRecolhimento
	 */
	public Map<Date, List<RecolhimentoDTO>> getMatrizRecolhimento() {
		return matrizRecolhimento;
	}

	/**
	 * @param matrizRecolhimento the matrizRecolhimento to set
	 */
	public void setMatrizRecolhimento(
			Map<Date, List<RecolhimentoDTO>> matrizRecolhimento) {
		this.matrizRecolhimento = matrizRecolhimento;
	}
	
}
