package br.com.abril.nds.model.ftf.retorno;

import java.util.ArrayList;
import java.util.List;

public class FTFRetornoRET {

	private FTFRetTipoRegistro00 tipo00;
	private FTFRetTipoRegistro09 tipo09;
	
	private List<FTFRetTipoRegistro01> tipo01List = new ArrayList<FTFRetTipoRegistro01>();

	public FTFRetTipoRegistro00 getTipo00() {
		return tipo00;
	}

	public void setTipo00(FTFRetTipoRegistro00 tipo00) {
		this.tipo00 = tipo00;
	}

	public FTFRetTipoRegistro09 getTipo09() {
		return tipo09;
	}

	public void setTipo09(FTFRetTipoRegistro09 tipo09) {
		this.tipo09 = tipo09;
	}

	public List<FTFRetTipoRegistro01> getTipo01List() {
		return tipo01List;
	}

	public void setTipo01List(List<FTFRetTipoRegistro01> tipo01List) {
		this.tipo01List = tipo01List;
	}

	
	
	
}
