package br.com.abril.nds.dto;

import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;

	public FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getExemplaresDevolucao().compareTo(o2.getExemplaresDevolucao()) *(-1) );
		}
		else
		{
			return o1.getExemplaresDevolucao().compareTo(o2.getExemplaresDevolucao());
		}
	}

}
