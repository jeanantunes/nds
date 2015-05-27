package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="REGIME_TRIBUTARIO")
public class RegimeTributario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6097502456643446957L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="ativo")
	private boolean ativo;
	
	@OneToMany
	@JoinTable(	name = "regime_tributario_tributo_aliquota", 
				joinColumns = {@JoinColumn(name = "regime_tributario_id")}, 
				inverseJoinColumns = {@JoinColumn(name = "tributo_aliquota_id")})
	private List<TributoAliquota> tributosAliquotas;

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

	public List<TributoAliquota> getTributosAliquotas() {
		return tributosAliquotas;
	}

	public void setTributosAliquotas(List<TributoAliquota> tributosAliquotas) {
		this.tributosAliquotas = tributosAliquotas;
	}
	
}