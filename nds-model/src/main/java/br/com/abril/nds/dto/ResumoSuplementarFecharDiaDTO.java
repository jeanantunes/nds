package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResumoSuplementarFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 7066874904183435754L;
	
	private BigDecimal totalEstoqueLogico;
	
	private BigDecimal totalTransferencia;
	
	private BigDecimal totalVenda;
	
	private BigDecimal totalAlteracaoPreco;
	
	private BigDecimal saldo;

	
	public BigDecimal getTotalEstoqueLogico() {
		return totalEstoqueLogico;
	}

	public void setTotalEstoqueLogico(BigDecimal totalEstoqueLogico) {
		this.totalEstoqueLogico = totalEstoqueLogico;
	}

	public BigDecimal getTotalTransferencia() {
		return totalTransferencia;
	}

	public void setTotalTransferencia(BigDecimal totalTransferencia) {
		this.totalTransferencia = totalTransferencia;
	}

	public BigDecimal getTotalVenda() {
		return totalVenda;
	}

	public void setTotalVenda(BigDecimal totalVenda) {
		this.totalVenda = totalVenda;
	}

	public BigDecimal getTotalAlteracaoPreco() {
		return totalAlteracaoPreco;
	}

	public void setTotalAlteracaoPreco(BigDecimal totalAlteracaoPreco) {
		this.totalAlteracaoPreco = totalAlteracaoPreco;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getTotalEstoqueLogicoFormatado() {
		return CurrencyUtil.formatarValor(totalEstoqueLogico);
	}

	public String getTotalTransferenciaFormatado() {
		return CurrencyUtil.formatarValor(totalTransferencia);
	}

	public String getTotalVendaFormatado() {
		return CurrencyUtil.formatarValor(totalVenda);
	}

	public String getSaldoFormatado() {
		return CurrencyUtil.formatarValor(saldo);
	}
	
	public String getTotalAlteracaoPrecoFormatado() {
		return CurrencyUtil.formatarValor(totalAlteracaoPreco);
	}

}