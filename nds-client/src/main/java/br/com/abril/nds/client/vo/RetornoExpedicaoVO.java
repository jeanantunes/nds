package br.com.abril.nds.client.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class RetornoExpedicaoVO {
	
	private List<ResumoExpedicaoVO> resumosExpedicao;
	
	private List<ResumoExpedicaoBoxVO> resumosExpedicaoBox;
	
	private BigInteger totalReparte;
	
	private BigDecimal totalValorFaturado;

	/**
	 * @return the resumosExpedicao
	 */
	public List<ResumoExpedicaoVO> getResumosExpedicao() {
		return resumosExpedicao;
	}

	/**
	 * @param resumosExpedicao the resumosExpedicao to set
	 */
	public void setResumosExpedicao(List<ResumoExpedicaoVO> resumosExpedicao) {
		this.resumosExpedicao = resumosExpedicao;
	}

	/**
	 * @return the totalReparte
	 */
	public BigInteger getTotalReparte() {
		return totalReparte;
	}

	/**
	 * @param totalReparte the totalReparte to set
	 */
	public void setTotalReparte(BigInteger totalReparte) {
		this.totalReparte = totalReparte;
	}

	/**
	 * @return the totalValorFaturado
	 */
	public BigDecimal getTotalValorFaturado() {
		return totalValorFaturado;
	}

	/**
	 * @param totalValorFaturado the totalValorFaturado to set
	 */
	public void setTotalValorFaturado(BigDecimal totalValorFaturado) {
		this.totalValorFaturado = totalValorFaturado;
	}

	/**
	 * @return the resumosExpedicaoBox
	 */
	public List<ResumoExpedicaoBoxVO> getResumosExpedicaoBox() {
		return resumosExpedicaoBox;
	}

	/**
	 * @param resumosExpedicaoBox the resumosExpedicaoBox to set
	 */
	public void setResumosExpedicaoBox(
			List<ResumoExpedicaoBoxVO> resumosExpedicaoBox) {
		this.resumosExpedicaoBox = resumosExpedicaoBox;
	}
	
	
}
