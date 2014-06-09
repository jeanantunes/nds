package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class FiltroConsultaBox {
	
	private Integer codigoBox;
	
	private String orderBy;
	
	private TipoBox tipoBox;
	
	private Ordenacao ordenacao;
	
	private int initialResult;
	
	private int maxResults;

	/**
	 * @return the codigoBox
	 */
	public Integer getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
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
	 * @return the tipoBox
	 */
	public TipoBox getTipoBox() {
		return tipoBox;
	}

	/**
	 * @param tipoBox the tipoBox to set
	 */
	public void setTipoBox(TipoBox tipoBox) {
		this.tipoBox = tipoBox;
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
