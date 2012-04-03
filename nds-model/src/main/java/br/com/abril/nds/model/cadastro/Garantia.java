package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GARANTIA")
@SequenceGenerator(name="GARANTIA_SEQ", initialValue = 1, allocationSize = 1)
public class Garantia implements Serializable {

	private static final long serialVersionUID = -362934722626599685L;
	
	@Id
	@GeneratedValue(generator = "GARANTIA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VALOR")
	private BigDecimal valor;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_FIADOR")
	private Fiador fiador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Fiador getFiador() {
		return fiador;
	}

	public void setFiador(Fiador fiador) {
		this.fiador = fiador;
	}
}