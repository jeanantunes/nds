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
@Table(name = "COTA_BASE_COTA")
@SequenceGenerator(name="COTA_BASE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class CotaBaseCota {
	
	@Id
	@GeneratedValue(generator = "COTA_BASE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;	
	
	@ManyToOne  
    @JoinColumn(name = "COTA_BASE_ID")  
    private CotaBase cotaBase;
	
	
	@ManyToOne  
    @JoinColumn(name = "COTA_ID")  
    private Cota cota;
	
	@Column(name="ATIVO")
	private Boolean ativo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CotaBase getCotaBase() {
		return cotaBase;
	}

	public void setCotaBase(CotaBase cotaBase) {
		this.cotaBase = cotaBase;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
	

}
