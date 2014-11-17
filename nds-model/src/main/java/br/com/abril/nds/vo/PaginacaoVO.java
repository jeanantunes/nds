package br.com.abril.nds.vo;

import java.io.Serializable;

/**
 * Value Object para paginação.
 * 
 * @author Discover Technology
 *
 */
public class PaginacaoVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1143885478818801313L;
	
	private static final String ASC = "asc";

	private static final String DESC = "desc";
	
	private Integer paginaAtual; 
	
	private Integer qtdResultadosPorPagina;
	
	private Ordenacao ordenacao;
	
	private String sortOrder;
	
	private String sortColumn;
	
	private Integer qtdResultadosTotal = 0;
	
	/**
	 * Construtor padrão.
	 */
	public PaginacaoVO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param page - página atual
	 * @param rp - quantidade de resultados por página
	 * @param sortOrder - tipo de ordenação
	 */
	public PaginacaoVO(Integer page, Integer rp, String sortorder) {
		
		this.paginaAtual = page;
		
		this.qtdResultadosPorPagina = rp;
		
		this.sortOrder = sortorder;
		
		if (Ordenacao.ASC.getOrdenacao().equals(sortorder)) {
			
			this.ordenacao = Ordenacao.ASC;
			
		} else if (Ordenacao.DESC.getOrdenacao().equals(sortorder)) {
			
			this.ordenacao = Ordenacao.DESC;
		}
	}
	
	public PaginacaoVO(Integer page, Integer rp, String sortOrder, String sortColumn) {
			
			this.paginaAtual = page;
			
			this.qtdResultadosPorPagina = rp;
			
			this.sortOrder = sortOrder;
			
			if (ASC.equalsIgnoreCase(sortOrder)) {
				
				this.ordenacao = Ordenacao.ASC;
				
			} else if (DESC.equalsIgnoreCase(sortOrder)) {
				
				this.ordenacao = Ordenacao.DESC;
			}
			
			this.sortColumn = sortColumn;
		}
	
	
	public PaginacaoVO(String sortname, String sortorder) {
		
		this.sortOrder = sortorder;
		
		if (ASC.equalsIgnoreCase(sortOrder)) {
			
			this.ordenacao = Ordenacao.ASC;
			
		} else if (DESC.equalsIgnoreCase(sortOrder)) {
			
			this.ordenacao = Ordenacao.DESC;
		}
		
		this.sortColumn = sortname;
	}


	public enum Ordenacao {
		
		ASC(PaginacaoVO.ASC),
		DESC(PaginacaoVO.DESC);
		
		private String ordenacao;
		
		private Ordenacao(String ordenacao) {
			
			this.ordenacao = ordenacao;
		}
		
		/**
		 * @return the ordenacao
		 */
		public String getOrdenacao() {
			return ordenacao;
		}
		
		@Override
		public String toString() {
			return this.ordenacao;
		}
	}
	
	/**
	 * @return posição inicial
	 */
	public Integer getPosicaoInicial() {
		
		if (this.qtdResultadosPorPagina == null
				|| this.paginaAtual == null) {
			
			return null;
		}
		
		return (this.qtdResultadosPorPagina * this.paginaAtual) - this.qtdResultadosPorPagina;
	}

	/**
	 * @return the paginaAtual
	 */
	public Integer getPaginaAtual() {
		return paginaAtual;
	}

	/**
	 * @param paginaAtual the paginaAtual to set
	 */
	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	/**
	 * @return the qtdResultadosPorPagina
	 */
	public Integer getQtdResultadosPorPagina() {
		return qtdResultadosPorPagina;
	}

	/**
	 * @param qtdResultadosPorPagina the qtdResultadosPorPagina to set
	 */
	public void setQtdResultadosPorPagina(Integer qtdResultadosPorPagina) {
		this.qtdResultadosPorPagina = qtdResultadosPorPagina;
	}

	/**
	 * @return the ordenacao
	 */
	public Ordenacao getOrdenacao() {
		return ordenacao;
	}

	/**
	 * @param ordenacao the ordenacao to set
	 */
	public void setOrdenacao(Ordenacao ordenacao) {
		this.ordenacao = ordenacao;
	}
	
	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the sortColumn
	 */
	public String getSortColumn() {
		return sortColumn;
	}

	/**
	 * @param sortColumn the sortColumn to set
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public Integer getQtdResultadosTotal() {
		return qtdResultadosTotal;
	}

	public void setQtdResultadosTotal(Integer qtdResultadosTotal) {
		this.qtdResultadosTotal = qtdResultadosTotal;
	}
	
	public String getOrderByClause() {
		return this.sortColumn + " " + this.sortOrder;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ordenacao == null) ? 0 : ordenacao.hashCode());
		result = prime
				* result
				+ ((qtdResultadosPorPagina == null) ? 0
						: qtdResultadosPorPagina.hashCode());
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
		PaginacaoVO other = (PaginacaoVO) obj;
		if (ordenacao != other.ordenacao)
			return false;
		if (qtdResultadosPorPagina == null) {
			if (other.qtdResultadosPorPagina != null)
				return false;
		} else if (!qtdResultadosPorPagina.equals(other.qtdResultadosPorPagina))
			return false;
		return true;
	}

}