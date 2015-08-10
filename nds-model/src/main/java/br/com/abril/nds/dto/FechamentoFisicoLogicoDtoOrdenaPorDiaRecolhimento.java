package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorDiaRecolhimento implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorDiaRecolhimento(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(!(o1 != null && o1.getDiaRecolhimento() != null 
				&& o2 != null && o2.getDiaRecolhimento() != null)) {
			return 0;
		}
		
		
		
		if(sortorder.equals("desc")) {
			return (o1.getDiaRecolhimento().compareTo(o2.getDiaRecolhimento()) *(-1) );
		} else {
			return o1.getDiaRecolhimento().compareTo(o2.getDiaRecolhimento());
		}
	}

}
