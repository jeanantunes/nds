package br.com.abril.nds.model.cadastro;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@MappedSuperclass
public abstract class AssociacaoTelefone {

	@ManyToOne(optional = false)
	@JoinColumn(name = "TELEFONE_ID")
    @Cascade(value = CascadeType.SAVE_UPDATE)
	private Telefone telefone;
	
	@Column(name = "TIPO_TELEFONE", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoTelefone tipoTelefone;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;

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
}
