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
		
		if (Ordenacao.ASC.getOrdenacao().equals(sortorder)) {
			
			this.ordenacao = Ordenacao.ASC;
			
		} else if (Ordenacao.DESC.getOrdenacao().equals(sortorder)) {
			
			this.ordenacao = Ordenacao.DESC;
		}
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
		
		return ((this.qtdResultadosPorPagina * this.paginaAtual) - this.qtdResultadosPorPagina) + 1;
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
}
