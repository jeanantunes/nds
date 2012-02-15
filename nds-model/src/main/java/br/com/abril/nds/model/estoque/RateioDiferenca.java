package br.com.abril.nds.model.estoque;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "RATEIO_DIFERENCA")
public class RateioDiferenca {

	@Id
	private Long id;
	private double qtde;
	@ManyToOne
	private Cota cota;
	@ManyToOne
	private Diferenca diferenca;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public double getQtde() {
		return qtde;
	}
	
	public void setQtde(double qtde) {
		this.qtde = qtde;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public Diferenca getDiferenca() {
		return diferenca;
	}
	
	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}
	

}