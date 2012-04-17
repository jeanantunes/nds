package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoTelefone;

public class TelefoneAssociacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1156114629027482122L;

	private Telefone telefone;
	
	private TipoTelefone tipoTelefone;
	
	private boolean principal;
	
	private int referencia;
	
	public TelefoneAssociacaoDTO(){}
	
	public TelefoneAssociacaoDTO(boolean principal, Telefone telefone, TipoTelefone tipoTelefone, Telefone telefonePessoa){
		
		if (tipoTelefone != null){
			this.principal = principal;
			this.telefone = telefone;
			this.tipoTelefone = tipoTelefone;
			this.referencia = telefone.getId().intValue();
		} else {
			this.telefone = telefonePessoa;
		}
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}
}