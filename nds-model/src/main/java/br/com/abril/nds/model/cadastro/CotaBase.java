package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COTA_BASE")
@SequenceGenerator(name="COTA_BASE_SEQ", initialValue = 1, allocationSize = 1)
public class CotaBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "COTA_BASE_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToMany
	@JoinTable(name = "COTA_BASE_COTA",
		joinColumns = {@JoinColumn(name="COTA_BASE_ID")},
		inverseJoinColumns = {@JoinColumn(name="COTA_ID", unique = true)}
	)
	private List<Cota> cotas = new ArrayList<Cota>();
	
	@Column(name = "DATA_INICIO")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@Column(name = "DATA_FIM")
	@Temporal(TemporalType.DATE)
	private Date dataFim;
	
	@Column(name = "ATIVA")
	private Boolean ativa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Boolean getAtiva() {
		return ativa;
	}

	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}
	
}
