package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "BOX")
@SequenceGenerator(name="BOX_SEQ", initialValue = 1, allocationSize = 1)
public class Box implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8925902448339516787L;
	@Id
	@GeneratedValue(generator = "BOX_SEQ")
	@Column(name = "ID")
	
	private Long id;
	@Column(name = "CODIGO", nullable = false)
	
	private String codigo;
	@Enumerated(EnumType.STRING)
	
	@Column(name = "TIPO_BOX", nullable = false)
	private TipoBox tipoBox;
	
	@Column(name="NOME")
	private String nome;
	
	@OneToMany(mappedBy = "box")
	private Set<Cota> cotas = new HashSet<Cota>();
	
	public Set<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(Set<Cota> cotas) {
		this.cotas = cotas;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

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
	
	public TipoBox getTipoBox() {
		return tipoBox;
	}
	
	public void setTipoBox(TipoBox tipoBox) {
		this.tipoBox = tipoBox;
	}


}