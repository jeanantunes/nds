package br.com.abril.nds.integracao.ems0198.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0198Trailer {

	
	private String registro;  
	
	private String codigoCota;
	
	private String quantidadeRegistros;

	/**
	 * 
	 * FIXME: O tamanho esta "erroneamente" fixado em 20 posições porque este
	 * a API FixedFormat4J gera apenas arquivos do tipo posicional (e 
	 * este arquivo é do tipo que utiliza delimitadores).
	 * O tamanho de 20 posições deve ser suficiente para este tipo de linha.
	 * 
	 * @return
	 */
	@Field(offset = 1, length = 20)
	public String getRegistro() {
		
		registro = "3|" + this.codigoCota + "|" + this.quantidadeRegistros;
		
		return registro;
	}

	public String getCodigoCota() {
		return codigoCota;
	}

	public String getQuantidadeRegistros() {
		return quantidadeRegistros;
	}
	
	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public void setQuantidadeRegistros(String quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}	
}
