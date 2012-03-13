package br.com.abril.nds.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoFinanceiro;

@Entity
@Table(name = "MOVIMENTO_FINANCEIRO_COTA")
public class MovimentoFinanceiroCota extends AbstractMovimentoFinanceiro {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@OneToMany
	private List<MovimentoEstoqueCota> movimentos = new ArrayList<MovimentoEstoqueCota>();
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public List<MovimentoEstoqueCota> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoEstoqueCota> movimentos) {
		this.movimentos = movimentos;
	}
	
	
}
