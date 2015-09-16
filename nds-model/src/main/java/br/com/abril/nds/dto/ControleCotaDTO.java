package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.seguranca.Permissao;

public class ControleCotaDTO implements Serializable {

	private static final long serialVersionUID = -7936154707508705341L;
	
	private String situacao;
	
	
	
	private Long id;
	private Integer numeroCotaMaster;
	private Integer numeroCota;
	
	public Long getId() {
		 return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public ControleCotaDTO(){};
		
	public ControleCotaDTO(Integer numeroCotaMaster,Integer numeroCota, String situacao) {
		this.numeroCotaMaster = numeroCotaMaster;
		this.numeroCota = numeroCota;
		this.situacao = situacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getNumeroCotaMaster() {
		return numeroCotaMaster;
	}

	public void setNumeroCotaMaster(Integer numeroCotaMaster) {
		this.numeroCotaMaster = numeroCotaMaster;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	

	


}
