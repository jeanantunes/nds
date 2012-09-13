package br.com.abril.nds.integracao.ems0198.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0198Header  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String registro;
	
	private String codigoCota;
	
	private String nomeCota;
	
	private String data;

	
	@Field(offset = 1, length = 1000)
	public String getTipoRegistro() {
		
		registro = "1|" + this.codigoCota + "|" + 
							this.nomeCota + "|" +
							this.data;
		
		return registro;
	}

	public String getCodigoCota() {
		return codigoCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public String getData() {
		return data;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
