package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AssociacaoEndereco {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ENDERECO_ID")
	private Endereco endereco;
	@Column(name = "TIPO_ENDERECO", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoEndereco tipoEndereco;
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}
	
	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}
	
	public boolean isPrincipal() {
		return principal;
	}
	
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

}
