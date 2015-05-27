package br.com.abril.nds.dto;

import java.util.Comparator;
            
public class FechamentoFisicoLogicoDtoOrdenaPorexemplaresVendaEncalhe implements Comparator<FechamentoFisicoLogicoDTO> {

	private String sortorder;

	public FechamentoFisicoLogicoDtoOrdenaPorexemplaresVendaEncalhe(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		
		if(sortorder.equals("desc"))
		{
			return (o1.getExemplaresVendaEncalhe().compareTo(o2.getExemplaresVendaEncalhe()) *(-1) );
		}
		else
		{
			return o1.getExemplaresVendaEncalhe().compareTo(o2.getExemplaresVendaEncalhe());
		}
	}

}
