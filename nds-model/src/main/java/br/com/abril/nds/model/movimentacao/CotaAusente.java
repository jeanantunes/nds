package br.com.abril.nds.model.movimentacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;

@Entity
@Table(name = "COTA_AUSENTE")
@SequenceGenerator(name="COTA_AUSENTE_SEQ", initialValue = 1, allocationSize = 1)
public class CotaAusente {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "COTA_AUSENTE_SEQ")
	private Long id;
	
	@Column(name = "DATA", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToMany(mappedBy = "cotaAusente")
	private List<RateioCotaAusente> rateios = new ArrayList<RateioCotaAusente>(); 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public List<RateioCotaAusente> getRateios() {
		return rateios;
	}
	
	public void setRateios(List<RateioCotaAusente> rateios) {
		this.rateios = rateios;
	}

}
