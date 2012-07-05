package br.com.abril.nds.model.fiscal.nota.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;

@Embeddable
public class MovimentoEstoqueNotaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7782150399454641379L;

	private TipoNotaFiscal tipoNotaFiscal;
	
	private ProdutoServico produtoServico;
	
	//private MovimentoEstoqueCota movimentoEstoqueCota;

	/**
	 * @return the tipoNotaFiscal
	 */
	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	/**
	 * @return the movimentoEstoqueCota
	
	public MovimentoEstoqueCota getMovimentoEstoqueCota() {
		return movimentoEstoqueCota;
	}

	/**
	 * @param movimentoEstoqueCota the movimentoEstoqueCota to set
	
	public void setMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		this.movimentoEstoqueCota = movimentoEstoqueCota;
	} */

	/**
	 * @return the produtoServico
	 */
	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	/**
	 * @param produtoServico the produtoServico to set
	 */
	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
	
}
