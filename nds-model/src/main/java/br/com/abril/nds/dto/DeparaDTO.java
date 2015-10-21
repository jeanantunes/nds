package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.seguranca.Permissao;

public class DeparaDTO implements Serializable {

	private static final long serialVersionUID = -7936154707508705341L;
	
	private String fc;
	
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
