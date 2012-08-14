package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/** 
 * Data Transfer Object para filtro da pesquisa de resumo de expedição
 * de Produto e Box.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroResumoExpedicaoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1821183862532025765L;
	
	@Export(label = "Data Lançamento")
	private Date dataLancamento;
	
	@Export(label = "Tipo Consulta")
	private TipoPesquisaResumoExpedicao tipoConsulta;
	
	@Export(label = "Código Box")
	private Integer codigoBox;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaProduto ordenacaoColunaProduto;
	
	private OrdenacaoColunaBox ordenacaoColunaBox;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroResumoExpedicaoDTO() {
		
	}

	/**
	 * Enum para ordenação das colunas do filtro de Produto.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaProduto {
		
		CODIGO_PRODUTO("codigoProduto"),
		DESCRICAO_PRODUTO("descricaoProduto"),
		NUMERO_EDICAO("edicaoProduto"),
		PRECO_CAPA("precoCapa"),
		REPARTE("reparte"),
		DIFERENCA("qntDiferenca"),
		VALOR_FATURADO("valorFaturado");
		
		private String nomeColuna;
		
		private OrdenacaoColunaProduto(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}
		
	/**
	 * Enum para ordenação das colunas do filtro de Box.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaBox{
			
			CODIGO_BOX("codigoBox"),
			DESCRICAO_BOX("descricaoBox"),
			QNT_PRODUTO("qntProduto"),
			REPARTE("reparte"),
			DIFERENCA("qntDiferenca"),
			VALOR_FATURADO("valorFaturado");
			
			private String nomeColuna;
			
			private OrdenacaoColunaBox(String nomeColuna) {
				
				this.nomeColuna = nomeColuna;
			}
			
			@Override
			public String toString() {
				
				return this.nomeColuna;
			}
	}
	
	public enum TipoPesquisaResumoExpedicao{
		
		PRODUTO("Produto"),
		BOX("Box");
		
		private TipoPesquisaResumoExpedicao(String nome) {
			this.nome = nome;
		}
		
		public String getNome() {
			return nome;
		}
		
		private String nome;
		
		@Override
		public String toString() {
			
			return this.nome;
		}
	}

	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
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
	 * @return the ordenacaoColunaProduto
	 */
	public OrdenacaoColunaProduto getOrdenacaoColunaProduto() {
		return ordenacaoColunaProduto;
	}

	/**
	 * @param ordenacaoColunaProduto the ordenacaoColunaProduto to set
	 */
	public void setOrdenacaoColunaProduto(
			OrdenacaoColunaProduto ordenacaoColunaProduto) {
		this.ordenacaoColunaProduto = ordenacaoColunaProduto;
	}

	/**
	 * @return the ordenacaoColunaBox
	 */
	public OrdenacaoColunaBox getOrdenacaoColunaBox() {
		return ordenacaoColunaBox;
	}

	/**
	 * @param ordenacaoColunaBox the ordenacaoColunaBox to set
	 */
	public void setOrdenacaoColunaBox(OrdenacaoColunaBox ordenacaoColunaBox) {
		this.ordenacaoColunaBox = ordenacaoColunaBox;
	}
	
	/**
	 * @return the tipoConsulta
	 */
	public TipoPesquisaResumoExpedicao getTipoConsulta() {
		return tipoConsulta;
	}

	/**
	 * @param tipoConsulta the tipoConsulta to set
	 */
	public void setTipoConsulta(TipoPesquisaResumoExpedicao tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	
	/**
	 * Obtém codigoBox
	 *
	 * @return Integer
	 */
	public Integer getCodigoBox() {
		return codigoBox;
	}

	/**
	 * Atribuí codigoBox
	 * @param codigoBox 
	 */
	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoBox == null) ? 0 : codigoBox.hashCode());
		result = prime * result
				+ ((dataLancamento == null) ? 0 : dataLancamento.hashCode());
		result = prime
				* result
				+ ((ordenacaoColunaBox == null) ? 0 : ordenacaoColunaBox
						.hashCode());
		result = prime
				* result
				+ ((ordenacaoColunaProduto == null) ? 0
						: ordenacaoColunaProduto.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((tipoConsulta == null) ? 0 : tipoConsulta.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FiltroResumoExpedicaoDTO)) {
			return false;
		}
		FiltroResumoExpedicaoDTO other = (FiltroResumoExpedicaoDTO) obj;
		if (codigoBox == null) {
			if (other.codigoBox != null) {
				return false;
			}
		} else if (!codigoBox.equals(other.codigoBox)) {
			return false;
		}
		if (dataLancamento == null) {
			if (other.dataLancamento != null) {
				return false;
			}
		} else if (!dataLancamento.equals(other.dataLancamento)) {
			return false;
		}
		if (ordenacaoColunaBox != other.ordenacaoColunaBox) {
			return false;
		}
		if (ordenacaoColunaProduto != other.ordenacaoColunaProduto) {
			return false;
		}
		if (paginacao == null) {
			if (other.paginacao != null) {
				return false;
			}
		} else if (!paginacao.equals(other.paginacao)) {
			return false;
		}
		if (tipoConsulta != other.tipoConsulta) {
			return false;
		}
		return true;
	}

	

}
