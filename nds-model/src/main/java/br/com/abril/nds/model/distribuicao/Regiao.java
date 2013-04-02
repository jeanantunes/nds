package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "REGIAO")
@SequenceGenerator(name = "REGIAO_SEQ", initialValue = 1, allocationSize = 1)
public class Regiao implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4841355309648872908L;

	@Id
	@GeneratedValue(generator = "REGIAO_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOME_REGIAO", nullable = false)
	private String nomeRegiao;

	@Column(name = "REGIAO_IS_FIXA", nullable = false)
	private boolean regiaoIsFixa;

	/**
	 * Data de início da Região
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_REGIAO", nullable = false)
	private Date dataRegiao;

	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario idUsuario;

	public Regiao() {
		this.dataRegiao = new Date();
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeRegiao() {
		return nomeRegiao;
	}

	public void setNomeRegiao(String nomeRegiao) {
		this.nomeRegiao = nomeRegiao;
	}

	public boolean isRegiaoIsFixa() {
		return regiaoIsFixa;
	}

	public void setRegiaoIsFixa(boolean regiaoIsFixa) {
		this.regiaoIsFixa = regiaoIsFixa;
	}

	public Date getDataRegiao() {
		return dataRegiao;
	}

	public void setDataRegiao(Date dataRegiao) {
		this.dataRegiao = dataRegiao;
	}
}
