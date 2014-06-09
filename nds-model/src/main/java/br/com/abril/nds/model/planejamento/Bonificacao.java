package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.util.ComponentesPDV;

@Entity
@Table(name = "ESTUDO_BONIFICACOES", uniqueConstraints = { @UniqueConstraint(columnNames = { "ESTUDO_ID" }) })
@SequenceGenerator(name = "BONIFICACAO_SEQ", initialValue = 1, allocationSize = 1)
public class Bonificacao implements Serializable {

	private static final long serialVersionUID = -6777798299415378397L;

	@Id
	@GeneratedValue(generator = "BONIFICACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ESTUDO_ID", nullable = false)
	private EstudoGerado estudo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "COMPONENTE")
	private ComponentesPDV componente;
	
	@Column(name = "ELEMENTO")
	private String elemento;
	
	@Column(name = "BONIFICACAO")
	private Integer bonificacao;
	
	@Column(name = "REPARTE_MINIMO")
	private BigInteger reparteMinimo;
	
	@Column(name = "TODAS_AS_COTAS")
	private boolean todasAsCotas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EstudoGerado getEstudo() {
		return estudo;
	}

	public void setEstudo(EstudoGerado estudo) {
		this.estudo = estudo;
	}

	public ComponentesPDV getComponente() {
		return componente;
	}

	public void setComponente(ComponentesPDV componente) {
		this.componente = componente;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public Integer getBonificacao() {
		return bonificacao;
	}

	public void setBonificacao(Integer bonificacao) {
		this.bonificacao = bonificacao;
	}

	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}

	public void setReparteMinimo(BigInteger reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}

	public boolean isTodasAsCotas() {
		return todasAsCotas;
	}

	public void setTodasAsCotas(boolean todasAsCotas) {
		this.todasAsCotas = todasAsCotas;
	}
	
}
