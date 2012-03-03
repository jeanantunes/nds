package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de lançamento
 * de diferenças no estoque.
 * 
 * @author Discover Technology
 *
 */
public class FiltroLancamentoDiferencaEstoqueDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1821183862532025765L;
	
	private Date dataMovimento;
	
	private TipoDiferenca tipoDiferenca;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaLancamento ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroLancamentoDiferencaEstoqueDTO() {
		
	}

	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaLancamento {
		
		CODIGO_PRODUTO("codigoProduto"),
		DESCRICAO_PRODUTO("descricaoProduto"),
		NUMERO_EDICAO("edicaoProduto"),
		PRECO_PRODUTO("precoProduto"),
		PACOTE_PADRAO("pacotePadrao"),
		EXEMPLARES("exemplares"),
		TIPO_DIFERENCA("tipoDiferenca");
		
		private String nomeColuna;
		
		private OrdenacaoColunaLancamento(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}

	/**
	 * @return the dataMovimento
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * @return the tipoDiferenca
	 */
	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	/**
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColunaLancamento getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaLancamento ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

}
