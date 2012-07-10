package br.com.abril.nds.model.estoque;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;
import br.com.abril.nds.model.planejamento.EstudoCota;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE_COTA")
public class MovimentoEstoqueCota  extends AbstractMovimentoEstoque {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTOQUE_PROD_COTA_ID")
	private EstoqueProdutoCota estoqueProdutoCota;
	
	/**
	 * Estudo cota que originou o movimento, 
	 * caso o movimento seja de reparte
	 */
	@OneToOne(optional = true)
	@JoinColumn(name = "ESTUDO_COTA_ID")
	private EstudoCota estudoCota;
	
	@ManyToMany(mappedBy="listaMovimentoEstoqueCota", targetEntity=ProdutoServico.class)
	private List<ProdutoEdicao> listaProdutoEdicao;
	
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

	/**
	 * @return the estudoCota
	 */
	public EstudoCota getEstudoCota() {
		return estudoCota;
	}

	/**
	 * @param estudoCota the estudoCota to set
	 */
	public void setEstudoCota(EstudoCota estudoCota) {
		this.estudoCota = estudoCota;
	}
	
}
