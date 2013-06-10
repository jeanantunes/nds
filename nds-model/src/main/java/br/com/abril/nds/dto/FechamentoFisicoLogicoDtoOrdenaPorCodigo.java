package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.Comparator;

public class FechamentoFisicoLogicoDtoOrdenaPorCodigo implements Comparator<FechamentoFisicoLogicoDTO>{
	private String sortorder;
	public FechamentoFisicoLogicoDtoOrdenaPorCodigo(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int compare(FechamentoFisicoLogicoDTO o1,
			FechamentoFisicoLogicoDTO o2) {
		BigInteger primeiroCodigo = new BigInteger(o1.getCodigo());
		BigInteger segundoCodigo = new BigInteger(o2.getCodigo());
		int resultado = primeiroCodigo.compareTo(segundoCodigo);
		
		if(this.sortorder != null && this.sortorder.equals("desc"))
		{
			resultado = resultado *(-1);
		}
		return resultado;
	}
}
