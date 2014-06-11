package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoTelefone;

public class TelefoneAssociacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1156114629027482122L;
	
	private Long id;
	
	private TelefoneDTO telefone;
	
	private TipoTelefone tipoTelefone;
	
	private boolean principal;
	
	private int referencia;
	
	public TelefoneAssociacaoDTO(){}
	
	public TelefoneAssociacaoDTO(boolean principal, Telefone telefone, TipoTelefone tipoTelefone, Telefone telefonePessoa){
		
		if (tipoTelefone != null){
			
			this.principal = principal;
			this.telefone = TelefoneDTO.fromTelefone(telefone);
			this.tipoTelefone = tipoTelefone;
			this.referencia = telefone.getId().intValue();
		} else {
			
			this.telefone = TelefoneDTO.fromTelefone(telefonePessoa);
			this.referencia = telefonePessoa.getId().intValue();
		}
	}
	
    public TelefoneAssociacaoDTO(Integer referencia, Telefone telefone, boolean principal,
            TipoTelefone tipoTelefone) {
        this(principal, telefone, tipoTelefone, null);
        this.referencia = referencia;
    }

	public TelefoneDTO getTelefone() {
		return telefone;
	}

	public void setTelefone(TelefoneDTO telefone) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}