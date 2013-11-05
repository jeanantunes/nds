package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de lançamento
 * de diferenças no estoque.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroLancamentoDiferencaEstoqueDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1821183862532025765L;
	
	@Export(label = "Data Movimento")
	private Date dataMovimento;
	
	@Export(label = "Tipo de Diferença")
	private TipoDiferenca tipoDiferenca;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaLancamento ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroLancamentoDiferencaEstoqueDTO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param dataMovimento - data de movimento
	 * @param tipoDiferenca - tipo de diferença
	 */
	public FiltroLancamentoDiferencaEstoqueDTO(Date dataMovimento, TipoDiferenca tipoDiferenca) {
		
		this.dataMovimento = dataMovimento;
		this.tipoDiferenca = tipoDiferenca;
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
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_VENDA("precoVenda"),
		PRECO_DESCONTO("precoDesconto"),
		PACOTE_PADRAO("pacotePadrao"),
		QUANTIDADE("quantidade"),
		TIPO_DIFERENCA("descricaoTipoDiferenca"),
		VALOR_TOTAL_DIFERENCA("valorTotalDiferenca");
		
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataMovimento == null) ? 0 : dataMovimento.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((tipoDiferenca == null) ? 0 : tipoDiferenca.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroLancamentoDiferencaEstoqueDTO other = (FiltroLancamentoDiferencaEstoqueDTO) obj;
		if (dataMovimento == null) {
			if (other.dataMovimento != null)
				return false;
		} else if (!dataMovimento.equals(other.dataMovimento))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (tipoDiferenca != other.tipoDiferenca)
			return false;
		return true;
	}

}
