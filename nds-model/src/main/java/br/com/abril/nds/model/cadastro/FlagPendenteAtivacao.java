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

import br.com.abril.nds.enums.TipoFlag;
import br.com.abril.nds.model.fiscal.TipoEntidadeDestinoFlag;

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
	@Column(name="NOME")
	private TipoFlag nome;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TIPO")
	private TipoEntidadeDestinoFlag tipo;
	
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
	
	public FlagPendenteAtivacao(TipoFlag tipoFlag, String descricao, TipoEntidadeDestinoFlag tipo, boolean valor, Long idAlterado) {
		this.nome = tipoFlag;
		this.descricao = descricao;
		this.tipo = tipo;
		this.valor = valor;
		this.idAlterado = idAlterado;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoFlag getNome() {
		return nome;
	}

	public void setNome(TipoFlag nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoEntidadeDestinoFlag getTipo() {
		return tipo;
	}

	public void setTipo(TipoEntidadeDestinoFlag tipo) {
		this.tipo = tipo;
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
		result = prime * result + ((this.getNome() == null) ? 0 : this.getNome().hashCode());
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
		if (this.getNome() == null) {
			if (other.getNome() != null)
				return false;
		} else if (!this.getNome().equals(other.getNome()))
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
		return (this.nome == null ? "" : this.nome) +" - "+ (this.descricao == null ? "" : this.descricao);
	}

}