package br.com.abril.nds.model.movimentacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

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

	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToMany(mappedBy = "cotaAusente", cascade = CascadeType.ALL)
	private List<RateioCotaAusente> rateios = new ArrayList<RateioCotaAusente>(); 
	
	@OneToMany
	@JoinTable(name = "COTA_AUSENTE_MOVIMENTO_ESTOQUE_COTA",
			   joinColumns = {@JoinColumn(name = "COTA_AUSENTE_ID")}, 
			   inverseJoinColumns = {@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID")})
	private List<MovimentoEstoqueCota> movimentosEstoqueCota = new ArrayList<MovimentoEstoqueCota>();

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

	public List<MovimentoEstoqueCota> getMovimentosEstoqueCota() {
		return movimentosEstoqueCota;
	}

	public void setMovimentosEstoqueCota(
			List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		this.movimentosEstoqueCota = movimentosEstoqueCota;
	}

}