package br.com.abril.nds.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

import br.com.abril.nds.model.cadastro.Endereco;

@Entity
@Table(name = "DISTRIBUIDOR_SERVER")
public class DistribuidorServer implements Serializable {

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
	
	@Column(name = "ENDERECO")
	private Endereco endereco;
	
	@Column(name = "STATUS_OPERACAO")
	private StatusOperacao statusOperacao;
	
	@OneToMany
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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