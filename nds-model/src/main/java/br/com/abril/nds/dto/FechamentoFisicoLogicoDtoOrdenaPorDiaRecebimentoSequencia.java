package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorDiaRecebimentoSequencia implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorDiaRecebimentoSequencia(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(!(o1 != null && o1.getSequencia() != null 
				&& o2 != null && o2.getSequencia() != null)) {
			return 0;
		}
		
		
		int dia = o1.getDiaRecolhimento().compareTo(o2.getDiaRecolhimento());
		
			if (dia != 0 ) 
				if(sortorder.equals("desc"))
				return dia *-1 ;
				else
				return dia ;
		
		
		if(sortorder.equals("desc")) {
			return (o1.getSequencia().compareTo(o2.getSequencia()) *(-1) );
		} else {
			return o1.getSequencia().compareTo(o2.getSequencia());
		}
	}

}
