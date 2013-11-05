package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorSequencia implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorSequencia(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(!(o1 != null && o1.getSequencia() != null 
				&& o2 != null && o2.getSequencia() != null)) {
			return 0;
		}
		
		if(sortorder.equals("desc")) {
			return (o1.getSequencia().compareTo(o2.getSequencia()) *(-1) );
		} else {
			return o1.getSequencia().compareTo(o2.getSequencia());
		}
	}

}
