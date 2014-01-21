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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "REGISTRO_COTA_REGIAO")
@SequenceGenerator(name="REGISTRO_SEQ", initialValue = 1, allocationSize = 1)
public class RegistroCotaRegiao implements Serializable {

	private static final long serialVersionUID = -1006995384547703984L;

	@Id
	@GeneratedValue(generator = "REGISTRO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "REGIAO_ID")
	@Cascade({CascadeType.REMOVE})
	private Regiao regiao;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne (optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Column (name = "DATA_ALTERACAO")
	private Date dataAlteracao;
	
	public RegistroCotaRegiao() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
}
