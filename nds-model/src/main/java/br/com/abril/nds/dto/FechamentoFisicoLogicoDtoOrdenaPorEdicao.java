package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorEdicao implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;
	
	public FechamentoFisicoLogicoDtoOrdenaPorEdicao(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getEdicao().compareTo(o2.getEdicao()) *(-1) );
		}
		else
		{
			return o1.getEdicao().compareTo(o2.getEdicao());
		}
	}

}
