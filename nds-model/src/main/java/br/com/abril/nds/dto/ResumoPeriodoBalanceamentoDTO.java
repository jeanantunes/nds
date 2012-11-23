package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ResumoPeriodoBalanceamentoDTO implements Serializable {

	private static final long serialVersionUID = 7487823794102857136L;

	private Date data;
	private String dataFormatada;
	private Long qtdeTitulos;
	private BigInteger qtdeExemplares;
	private String qtdeExemplaresFormatada;
	private BigInteger pesoTotal;
	private String pesoTotalFormatado;
	private BigDecimal valorTotal;
	private String valorTotalFormatado;
	private Long qtdeTitulosParciais;
	private boolean exibeDestaque;
	private boolean excedeCapacidadeDistribuidor;
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
		if (data != null) {
			dataFormatada = DateUtil.formatarDataPTBR(data);
		}
	}
	
	public String getDataFormatada() {
		return dataFormatada;
	}
	
	public Long getQtdeTitulos() {
		return qtdeTitulos;
	}
	
	public void setQtdeTitulos(Long qtdeTitulos) {
		this.qtdeTitulos = qtdeTitulos;
	}
	
	public BigInteger getQtdeExemplares() {
		return qtdeExemplares;
	}
	
	public void setQtdeExemplares(BigInteger qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
		if (qtdeExemplares != null) {
			qtdeExemplaresFormatada = qtdeExemplares.toString();
		}
	}
	
	public String getQtdeExemplaresFormatada() {
		return qtdeExemplaresFormatada;
	}
	
	public BigInteger getPesoTotal() {
		return pesoTotal;
	}
	
	public void setPesoTotal(BigInteger pesoTotal) {
		this.pesoTotal = pesoTotal;
		if (pesoTotal != null) {
			pesoTotalFormatado = pesoTotal.toString();
		}
	}
	
	public String getPesoTotalFormatado() {
		return pesoTotalFormatado;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
		if (valorTotal != null) {
			valorTotalFormatado = CurrencyUtil.formatarValor(valorTotal);
		}
	}
	
	public String getValorTotalFormatado() {
		return valorTotalFormatado;
	}
	
	public static ResumoPeriodoBalanceamentoDTO empty(Date data) {
		ResumoPeriodoBalanceamentoDTO resumo = new ResumoPeriodoBalanceamentoDTO();
		resumo.setData(data);
		resumo.setPesoTotal(BigInteger.ZERO);
		resumo.setQtdeExemplares(BigInteger.ZERO);
		resumo.setQtdeTitulos(0L);
		resumo.setValorTotal(BigDecimal.ZERO);
		return resumo;
	}

	/**
	 * @return the qtdeTitulosParciais
	 */
	public Long getQtdeTitulosParciais() {
		return qtdeTitulosParciais;
	}

	/**
	 * @param qtdeTitulosParciais the qtdeTitulosParciais to set
	 */
	public void setQtdeTitulosParciais(Long qtdeTitulosParciais) {
		this.qtdeTitulosParciais = qtdeTitulosParciais;
	}

	/**
	 * @return the exibeDestaque
	 */
	public boolean isExibeDestaque() {
		return exibeDestaque;
	}

	/**
	 * @param exibeDestaque the exibeDestaque to set
	 */
	public void setExibeDestaque(boolean exibeDestaque) {
		this.exibeDestaque = exibeDestaque;
	}

	/**
	 * @return the excedeCapacidadeDistribuidor
	 */
	public boolean isExcedeCapacidadeDistribuidor() {
		return excedeCapacidadeDistribuidor;
	}

	/**
	 * @param excedeCapacidadeDistribuidor the excedeCapacidadeDistribuidor to set
	 */
	public void setExcedeCapacidadeDistribuidor(boolean excedeCapacidadeDistribuidor) {
		this.excedeCapacidadeDistribuidor = excedeCapacidadeDistribuidor;
	}
	
}
