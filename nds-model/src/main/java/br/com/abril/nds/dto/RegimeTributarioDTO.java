package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class RegimeTributarioDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6097502456643446957L;

	private Long id;
	
	private String codigo;
	
	private String descricao;
	
	private boolean ativo;
	
	private List<TributoAliquotaDTO> tributosAliquotas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<TributoAliquotaDTO> getTributosAliquotas() {
		return tributosAliquotas;
	}

	public void setTributosAliquotas(List<TributoAliquotaDTO> tributosAliquotas) {
		this.tributosAliquotas = tributosAliquotas;
	}
	
}