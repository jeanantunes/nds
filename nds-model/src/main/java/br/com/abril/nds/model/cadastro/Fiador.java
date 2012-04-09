package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FIADOR")
@SequenceGenerator(name="FIADOR_SEQ", initialValue = 1, allocationSize = 1)
public class Fiador implements Serializable {

	private static final long serialVersionUID = -1574679595779557L;
	
	@Id
	@GeneratedValue(generator = "FIADOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO_ATIVIDADE")
	private Date inicioAtividade;
	
	@OneToOne
	@JoinColumn(name = "PESSOA_ID_CONJUGE")
	private Pessoa conjuge;
	
	@OneToMany(mappedBy = "fiador")
	private List<Cota> cotasAssociadas;
	
	@OneToMany(mappedBy = "fiador")
	private List<Garantia> garantias;
	
	@OneToMany(mappedBy = "fiador")
	private List<Pessoa> socios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Date getInicioAtividade() {
		return inicioAtividade;
	}

	public void setInicioAtividade(Date inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
	}

	public Pessoa getConjuge() {
		return conjuge;
	}

	public void setConjuge(Pessoa conjuge) {
		this.conjuge = conjuge;
	}

	public List<Cota> getCotasAssociadas() {
		return cotasAssociadas;
	}

	public void setCotasAssociadas(List<Cota> cotasAssociadas) {
		this.cotasAssociadas = cotasAssociadas;
	}

	public List<Garantia> getGarantias() {
		return garantias;
	}

	public void setGarantias(List<Garantia> garantias) {
		this.garantias = garantias;
	}

	public List<Pessoa> getSocios() {
		return socios;
	}

	public void setSocios(List<Pessoa> socios) {
		this.socios = socios;
	}
}