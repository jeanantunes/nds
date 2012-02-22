package br.com.abril.nds.model.planejamento;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO_COTA")
@SequenceGenerator(name="ESTUDO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoCota {

	@Id
	@GeneratedValue(generator = "ESTUDO_COTA_SEQ")
	private Long id;
	private double qtdePrevista;
	private double qtdeEfetiva;
	@ManyToOne
	private Estudo estudo;
	@ManyToOne
	private Cota cota;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public double getQtdePrevista() {
		return qtdePrevista;
	}
	
	public void setQtdePrevista(double qtdePrevista) {
		this.qtdePrevista = qtdePrevista;
	}
	
	public double getQtdeEfetiva() {
		return qtdeEfetiva;
	}
	
	public void setQtdeEfetiva(double qtdeEfetiva) {
		this.qtdeEfetiva = qtdeEfetiva;
	}
	
	public Estudo getEstudo() {
		return estudo;
	}
	
	public void setEstudo(Estudo estudo) {
		this.estudo = estudo;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}

}