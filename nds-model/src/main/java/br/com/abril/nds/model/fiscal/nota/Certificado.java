package br.com.abril.nds.model.fiscal.nota;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "CERTIFICADO")
public class Certificado {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "SENHA")
	private String senha;

	@Column(name = "ALIAS")
	private String alias;

	@Column(name = "NOME_ARQUIVO")
	private String nomeArquivo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO")
	private Date dataCriacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO")
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FIM")
	private Date dataFim;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;

	@OneToOne
	@JoinColumn(name="USUARIO_ID", nullable=false)
	private Usuario usuario;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		Certificado other = (Certificado) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Certificado [id=" + id + ", senha=" + senha + ", alias="
				+ alias + ", nomeArquivo=" + nomeArquivo + ", dataCriacao="
				+ dataCriacao + ", dataInicio=" + dataInicio + ", dataFim="
				+ dataFim + ", distribuidor=" + distribuidor + ", usuario="
				+ usuario + "]";
	}	
}