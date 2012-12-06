package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResumoEncalheFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 8721756404722658789L;
	
	private BigDecimal totalLogico;
	
	private BigDecimal totalFisico;
	
	private BigDecimal totalJuramentado;
	
	private BigDecimal venda;
	
	private BigDecimal totalSobras;
	
	private BigDecimal totalFaltas;
	
	private BigDecimal saldo;

	public BigDecimal getTotalLogico() {
		return totalLogico;
	}

	public void setTotalLogico(BigDecimal totalLogico) {
		this.totalLogico = totalLogico;
	}

	public BigDecimal getTotalFisico() {
		return totalFisico;
	}

	public void setTotalFisico(BigDecimal totalFisico) {
		this.totalFisico = totalFisico;
	}

	public BigDecimal getTotalJuramentado() {
		return totalJuramentado;
	}

	public void setTotalJuramentado(BigDecimal totalJuramentado) {
		this.totalJuramentado = totalJuramentado;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	public BigDecimal getTotalSobras() {
		return totalSobras;
	}

	public void setTotalSobras(BigDecimal totalSobras) {
		this.totalSobras = totalSobras;
	}

	public BigDecimal getTotalFaltas() {
		return totalFaltas;
	}

	public void setTotalFaltas(BigDecimal totalFaltas) {
		this.totalFaltas = totalFaltas;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public String getTotalLogicoFormatado() {
	    return CurrencyUtil.formatarValor(totalLogico);
	}
	
	public String getTotalFisicoFormatado() {
        return CurrencyUtil.formatarValor(totalFisico);
    }
	
	public String getTotalJuramentadoFormatado() {
        return CurrencyUtil.formatarValor(totalJuramentado);
    }
	
	public String getVendaFormatado() {
	    return CurrencyUtil.formatarValor(venda);
	}
	
	public String getTotalSobrasFormatado() {
        return CurrencyUtil.formatarValor(totalSobras);
    }
	
	public String getTotalFaltasFormatado() {
        return CurrencyUtil.formatarValor(totalFaltas);
    }
	
	public String getSaldoFormatado() {
        return CurrencyUtil.formatarValor(saldo);
    }
	
}
