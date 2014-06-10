package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorProduto implements Comparator<FechamentoFisicoLogicoDTO>{

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorProduto(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getProduto().compareTo(o2.getProduto()) *(-1) );
		}
		else
		{
			return o1.getProduto().compareTo(o2.getProduto());
		}
	}

}
