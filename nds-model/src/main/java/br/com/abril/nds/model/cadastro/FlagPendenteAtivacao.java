package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.enums.Dominio;
import br.com.abril.nds.enums.Flag;

@Entity
@Table(name = "FLAG_PENDENTE_ATIVACAO")
public class FlagPendenteAtivacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2865825553157700966L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="FLAG")
	private Flag flag;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TIPO")
	private Dominio dominio;
	
	@Column(name="VALOR")
	private boolean valor;
	
	@Column(name="ID_ALTERADO")
	private Long idAlterado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_ALTERACAO")
	private Date dataAlteracao;

	public FlagPendenteAtivacao() {
		super();
	}
	
	public FlagPendenteAtivacao(Flag tipoFlag, String descricao, Dominio dominio, boolean valor, Long idAlterado) {
		this.flag = tipoFlag;
		this.descricao = descricao;
		this.dominio = dominio;
		this.valor = valor;
		this.idAlterado = idAlterado;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public boolean isValor() {
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}

	public Long getIdAlterado() {
		return idAlterado;
	}

	public void setIdAlterado(Long idAlterado) {
		this.idAlterado = idAlterado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getFlag() == null) ? 0 : this.getFlag().hashCode());
		result = prime * result + ((this.getIdAlterado() == null) ? 0 : this.getIdAlterado().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlagPendenteAtivacao other = (FlagPendenteAtivacao) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getFlag() == null) {
			if (other.getFlag() != null)
				return false;
		} else if (!this.getFlag().equals(other.getFlag()))
			return false;
		if (this.getIdAlterado() == null) {
			if (other.getIdAlterado() != null)
				return false;
		} else if (!this.getIdAlterado().equals(other.getIdAlterado()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return (this.flag == null ? "" : this.flag) +" - "+ (this.descricao == null ? "" : this.descricao);
	}

}