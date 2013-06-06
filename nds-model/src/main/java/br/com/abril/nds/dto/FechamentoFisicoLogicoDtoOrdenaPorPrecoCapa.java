package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getPrecoCapa().compareTo(o2.getPrecoCapa()) *(-1) );
		}
		else
		{
			return o1.getPrecoCapa().compareTo(o2.getPrecoCapa());
		}
	}

}
