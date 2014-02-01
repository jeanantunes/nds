package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.nota.Aliquota;

@Entity
@Table(name="REGIME_TRIBUTARIO")
public class RegimeTributario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="CODIGO")
	private Long codigo;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@OneToMany
	@JoinTable(name = "REGIME_TRIBUTARIO_ALIQUOTA", joinColumns = {@JoinColumn(name = "REGIME_TRIBUTARIO_ID")}, 
		inverseJoinColumns = {@JoinColumn(name = "ALIQUOTA_ID")})
	private List<Aliquota> aliquotas;
	
	public RegimeTributario() {
		super();
	}
	
	public RegimeTributario(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return "RegimeTributario [id=" + id + ", codigo=" + codigo
				+ ", descricao=" + descricao + "]";
	}
}
