package br.com.abril.nds.model.fiscal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_COTA")
@DiscriminatorValue(value="COTA")
public class MovimentoFechamentoFiscalCota extends MovimentoFechamentoFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToOne
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@OneToOne
	@JoinColumn(name = "CHAMADA_ENCALHE_COTA_ID")
	private ChamadaEncalheCota chamadaEncalheCota;
	
	@Embedded
	private ValoresAplicados valoresAplicados;

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public ChamadaEncalheCota getChamadaEncalheCota() {
		return chamadaEncalheCota;
	}

	public void setChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		this.chamadaEncalheCota = chamadaEncalheCota;
	}

	public ValoresAplicados getValoresAplicados() {
		return valoresAplicados;
	}

	public void setValoresAplicados(ValoresAplicados valoresAplicados) {
		this.valoresAplicados = valoresAplicados;
	}

}