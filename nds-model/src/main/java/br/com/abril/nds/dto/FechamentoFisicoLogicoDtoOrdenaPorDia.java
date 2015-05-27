package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorDia implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorDia(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getRecolhimento().compareTo(o2.getRecolhimento()) *(-1) );
		}
		else
		{
			return o1.getRecolhimento().compareTo(o2.getRecolhimento());
		}
	}

}
