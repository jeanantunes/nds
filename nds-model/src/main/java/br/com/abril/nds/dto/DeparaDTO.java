package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class DeparaDTO implements Serializable {

	private static final long serialVersionUID = -7936154707508705341L;
	
	@Export(label="FC")
	private String fc;
	
	@Export(label="DINAP")
	private String dinap;
	
	private Long id;
	
	public Long getId() {
		 return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public DeparaDTO(){};
		
	public DeparaDTO(String fc,String dinap) {
		this.fc = fc;
		this.dinap = dinap;
	}
	
	public String getFc() {
		if ( fc == null )
			 return "";
		return fc;
	}

	public void setFc(String fc) {
		if ( fc == null )
			 this.fc ="";
		else
		  this.fc = fc;
	}
	
	public String getDinap() {
		if ( dinap == null)
			return "";
		return dinap;
	}

	public void setDinap(String dinap) {
		this.dinap = dinap;
	}

	


}
