package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro de pesquisa de diferenças de estoque.
 * 
 * @author Discover Technology
 *
 */
public class FiltroConsultaDiferencaEstoqueDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2032477830142408298L;
	
	private String codigoProduto;
	
	private Long numeroEdicao;
	
	private Long idFornecedor;
	
	private Date dataLancamentoDe;
	
	private Date dataLancamentoAte;
	
	private TipoDiferenca tipoDiferenca;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;

	/**
	 * Construtor padrão.
	 */
	public FiltroConsultaDiferencaEstoqueDTO() {
		
	}
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaConsulta {
		
		DATA_LANCAMENTO("dataLancamento"),
		CODIGO_PRODUTO("codigoProduto"),
		DESCRICAO_PRODUTO("descricaoProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_VENDA("precoVenda"),
		TIPO_DIFERENCA("tipoDiferenca"),
		NUMERO_NOTA_FISCAL("numeroNotaFiscal"),
		QUANTIDADE("quantidade"),
		STATUS_APROVACAO("statusAprovacao");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}
	
	/**
	 * @return codigoProduto the codigoProduto to set
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param the codigoProduto
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the dataLancamentoDe
	 */
	public Date getDataLancamentoDe() {
		return dataLancamentoDe;
	}

	/**
	 * @param dataLancamentoDe the dataLancamentoDe to set
	 */
	public void setDataLancamentoDe(Date dataLancamentoDe) {
		this.dataLancamentoDe = dataLancamentoDe;
	}

	/**
	 * @return the dataLancamentoAte
	 */
	public Date getDataLancamentoAte() {
		return dataLancamentoAte;
	}

	/**
	 * @param dataLancamentoAte the dataLancamentoAte to set
	 */
	public void setDataLancamentoAte(Date dataLancamentoAte) {
		this.dataLancamentoAte = dataLancamentoAte;
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
	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

}
