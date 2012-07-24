package br.com.abril.nds.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "DISTRIBUIDOR")
public class Distribuidor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7636923217808439029L;

	@Id
	@Column(name = "ID_DISTRIBUIDOR_INTERFACE")
	@SerializedName("_id")
	private Long idDistribuidorInterface;
	
	@Column(name = "DATA_OPERACAO")
	private Date dataOperacao;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "UF")
	private String uf;
	
	@OneToOne
	@JoinColumn(name = "STATUS_OPERACAO")
	private StatusOperacao statusOperacao;
	
	@Transient
	private List<Indicador> indicadores;

	public Long getIdDistribuidorInterface() {
		return idDistribuidorInterface;
	}

	public void setIdDistribuidorInterface(Long idDistribuidorInterface) {
		this.idDistribuidorInterface = idDistribuidorInterface;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public StatusOperacao getStatusOperacao() {
		return statusOperacao;
	}

	public void setStatusOperacao(StatusOperacao statusOperacao) {
		this.statusOperacao = statusOperacao;
	}

	public List<Indicador> getIndicadores() {
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}
}