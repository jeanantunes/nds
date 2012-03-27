package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

/**
 * Data Transfer Object para filtro de pesquisa de diferenças de estoque.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroConsultaDiferencaEstoqueDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2032477830142408298L;
	
	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", exhibitionOrder = 3)
	private Long numeroEdicao;
	
	private Long idFornecedor;
	
	@Export(label = "Fornecedor", exhibitionOrder = 4)
	private String nomeFornecedor;

	private PeriodoVO periodoVO;
	
	@Export(label = "Tipo de Diferença", exhibitionOrder = 7)
	private TipoDiferenca tipoDiferenca;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	private Date dataLimiteLancamentoDistribuidor;

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
		
		DATA_LANCAMENTO_NUMERO_EDICAO("dataLancamentoNumeroEdicao"),
		DATA_LANCAMENTO("dataLancamento"),
		CODIGO_PRODUTO("codigoProduto"),
		DESCRICAO_PRODUTO("descricaoProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_VENDA("precoVenda"),
		TIPO_DIFERENCA("tipoDiferenca"),
		NUMERO_NOTA_FISCAL("numeroNotaFiscal"),
		QUANTIDADE("quantidade"),
		STATUS_APROVACAO("statusAprovacao"),
		VALOR_TOTAL_DIFERENCA("valorTotalDiferenca");
		
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
	 * @return the periodoVO
	 */
	public PeriodoVO getPeriodoVO() {
		return periodoVO;
	}

	/**
	 * @param periodoVO the periodoVO to set
	 */
	public void setPeriodoVO(PeriodoVO periodoVO) {
		this.periodoVO = periodoVO;
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

	/**
	 * @return the dataLimiteLancamentoDistribuidor
	 */
	public Date getDataLimiteLancamentoDistribuidor() {
		return dataLimiteLancamentoDistribuidor;
	}

	/**
	 * @param dataLimiteLancamentoDistribuidor the dataLimiteLancamentoDistribuidor to set
	 */
	public void setDataLimiteLancamentoDistribuidor(Date dataLimiteLancamentoDistribuidor) {
		this.dataLimiteLancamentoDistribuidor = dataLimiteLancamentoDistribuidor;
	}
	
	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	
	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	@Export(label = "Data Inicial", exhibitionOrder = 5)
	public String getDataInicial() {
		
		if (this.periodoVO == null
				|| this.periodoVO.getDataInicial() == null) {
			
			return "";
		}
		
		return DateUtil.formatarDataPTBR(this.periodoVO.getDataInicial());
	}
	
	@Export(label = "Data Final", exhibitionOrder = 6)
	public String getDataFinal() {
		
		if (this.periodoVO == null
				|| this.periodoVO.getDataFinal() == null) {
			
			return "";
		}
		
		return DateUtil.formatarDataPTBR(this.periodoVO.getDataInicial());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((periodoVO == null) ? 0 : periodoVO.hashCode());
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
		FiltroConsultaDiferencaEstoqueDTO other = (FiltroConsultaDiferencaEstoqueDTO) obj;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (periodoVO == null) {
			if (other.periodoVO != null)
				return false;
		} else if (!periodoVO.equals(other.periodoVO))
			return false;
		if (tipoDiferenca != other.tipoDiferenca)
			return false;
		return true;
	}
	
}
