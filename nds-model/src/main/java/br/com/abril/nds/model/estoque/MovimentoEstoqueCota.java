package br.com.abril.nds.model.estoque;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE_COTA")
public class MovimentoEstoqueCota  extends AbstractMovimentoEstoque {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTOQUE_PROD_COTA_ID")
	private EstoqueProdutoCota estoqueProdutoCota;
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public EstoqueProdutoCota getEstoqueProdutoCota() {
		return estoqueProdutoCota;
	}
	
	public void setEstoqueProdutoCota(EstoqueProdutoCota estoqueProdutoCota) {
		this.estoqueProdutoCota = estoqueProdutoCota;
	}

}
