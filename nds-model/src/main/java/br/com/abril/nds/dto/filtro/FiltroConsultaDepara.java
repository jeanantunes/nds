package br.com.abril.nds.dto.filtro;


import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class FiltroConsultaDepara {
	
	private String  fc;
	
	private String orderBy;
	
	public String getDinap() {
		return dinap;
	}



	public void setDinap(String dinap) {
		this.dinap = dinap;
	}

	private String  dinap;
	
	private Ordenacao ordenacao;
	
	private int initialResult;
	
	private int maxResults;

	/**
	 * @return the codigoDepara
	 */
	public String getFc() {
		return fc;
	}

	

	/**
	 * @param codigoDepara the codigoDepara to set
	 */
	public void setFc(String  fc) {
		this.fc = fc;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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

	/**
	 * @return the initialResult
	 */
	public int getInitialResult() {
		return initialResult;
	}

	/**
	 * @param initialResult the initialResult to set
	 */
	public void setInitialResult(int initialResult) {
		this.initialResult = initialResult;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
}
