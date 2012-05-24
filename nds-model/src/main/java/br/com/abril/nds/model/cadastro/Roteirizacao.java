package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROTEIRIZACAO")
@SequenceGenerator(name="ROTEIRIZACAO_SEQ", initialValue = 1, allocationSize = 1)
public class Roteirizacao {


	@Id
	@GeneratedValue(generator = "ROTEIRIZACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;

	@ManyToOne
	@JoinColumn(name = "ROTA_ID")
	private Rota rota;
	
	@Column(name="ORDEM", nullable = false)
	private Integer ordem;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
