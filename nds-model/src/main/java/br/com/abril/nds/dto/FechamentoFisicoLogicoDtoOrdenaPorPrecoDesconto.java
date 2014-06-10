package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;

	public FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getPrecoCapaDesconto().compareTo(o2.getPrecoCapaDesconto()) *(-1) );
		}
		else
		{
			return o1.getPrecoCapaDesconto().compareTo(o2.getPrecoCapaDesconto());
		}
	}

}
