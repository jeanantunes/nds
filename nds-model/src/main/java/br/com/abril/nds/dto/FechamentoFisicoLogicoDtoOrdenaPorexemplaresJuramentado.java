package br.com.abril.nds.dto;

import java.util.Comparator;
 
public class FechamentoFisicoLogicoDtoOrdenaPorexemplaresJuramentado implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;

	public FechamentoFisicoLogicoDtoOrdenaPorexemplaresJuramentado(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getExemplaresJuramentado().compareTo(o2.getExemplaresJuramentado()) *(-1) );
		}
		else
		{
			return o1.getExemplaresJuramentado().compareTo(o2.getExemplaresJuramentado());
		}
	}

}
