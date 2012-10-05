package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de digitação contagem devolução
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroDigitacaoContagemDevolucaoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Intervalo<Date> periodo;
	
	private Long idFornecedor;
	
	@Export(label="Fornecedor")
	private String nomeFornecedor;
	
	private Integer semanaCE;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroDigitacaoContagemDevolucaoDTO(){}	
	
	
	public FiltroDigitacaoContagemDevolucaoDTO(Intervalo<Date> periodo, Long idFornecedor){
		this.idFornecedor = idFornecedor;
		this.periodo = periodo;
	}	
	
	
	public FiltroDigitacaoContagemDevolucaoDTO(Intervalo<Date> periodo,
			Long idFornecedor, Integer semanaCE) {
		super();
		this.periodo = periodo;
		this.idFornecedor = idFornecedor;
		this.semanaCE = semanaCE;
	}




	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna {
		
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_CAPA("precoVenda"),
		QTD_DEVOLUCAO("qtdDevolucao"),
		VALOR_TOTAL("valorTotal"),
		QTD_NOTA("qtdNota"),
		DIFERENCA("diferenca");
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}
	
	@Export(label="Data Inicial")
	public Date getDataInicial(){
		
		if(periodo==null) {
			return null;
		}
		return periodo.getDe();
		
	}

	@Export(label="Data Final")
	public Date getDataFinal(){
		
		if(periodo==null) {
			return null;
		}
		return periodo.getAte();
		
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
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	/**
	 * @return the periodo
	 */
	public Intervalo<Date> getPeriodo() {
		return periodo;
	}


	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Intervalo<Date> periodo) {
		this.periodo = periodo;
	}

	
	/**
	 * Obtém nomeFornecedor
	 *
	 * @return String
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * Atribuí nomeFornecedor
	 * @param nomeFornecedor 
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	
	/**
	 * @return the semanaCE
	 */
	public Integer getSemanaCE() {
		return semanaCE;
	}


	/**
	 * @param semanaCE the semanaCE to set
	 */
	public void setSemanaCE(Integer semanaCE) {
		this.semanaCE = semanaCE;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
		result = prime * result
				+ ((semanaCE == null) ? 0 : semanaCE.hashCode());
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
		FiltroDigitacaoContagemDevolucaoDTO other = (FiltroDigitacaoContagemDevolucaoDTO) obj;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		if (semanaCE == null) {
			if (other.semanaCE != null)
				return false;
		} else if (!semanaCE.equals(other.semanaCE))
			return false;
		return true;
	}

	
}
