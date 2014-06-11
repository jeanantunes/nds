package br.com.abril.nds.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "OPERACAO_DISTRIBUIDOR")
public class OperacaoDistribuidor implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7636923217808439029L;

	@Id
	@Column(name = "ID_DISTRIBUIDOR_INTERFACE")
	@SerializedName("_id")
	private String idDistribuidorInterface;
	 
	@Column(name = "REVISAO", nullable = true)
	@SerializedName("_rev")
	private String revisao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO")
	private Date dataOperacao;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "UF")
	private String uf;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "STATUS_OPERACAO")
	private StatusOperacao statusOperacao;
	
	@OneToMany(mappedBy = "operacaoDistribuidor", cascade = CascadeType.ALL)
	private List<Indicador> indicadores;

	public String getIdDistribuidorInterface() {
		return idDistribuidorInterface;
	}

	public void setIdDistribuidorInterface(String idDistribuidorInterface) {
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

	/**
	 * @return the revisao
	 */
	public String getRevisao() {
		return revisao;
	}

	/**
	 * @param revisao the revisao to set
	 */
	public void setRevisao(String revisao) {
		this.revisao = revisao;
	}
	
}