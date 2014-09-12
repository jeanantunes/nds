package br.com.abril.nds.model.fechar.dia;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CONSIGNADO")
public class FechamentoDiarioResumoConsignado extends FechamentoDiarioConsignado {

	private static final long serialVersionUID = 1L;
	
	@Column(name="VALOR_CE")
	private BigDecimal valorCE;
	
	@Column(name="VALOR_OUTRAS_MOVIMENTACOES_ENTRADA")
	private BigDecimal valorOutrasMovimentacoesEntrada;
	
	
	@Column(name="VALOR_EXPEDICAO")
	private BigDecimal valorExpedicao;
	
	
	@Column(name="VALOR_OUTRAS_MOVIMENTACOES_SAIDA")
	private BigDecimal valorOutrasMovimentacoesSaida;
	
	@Column(name="VALOR_OUTRAS_MOVIMENTACOES_A_VISTA")
	private BigDecimal valorAVista;


	public BigDecimal getValorCE() {
		return valorCE;
	}


	public void setValorCE(BigDecimal valorCE) {
		this.valorCE = valorCE;
	}


	public BigDecimal getValorOutrasMovimentacoesEntrada() {
		return valorOutrasMovimentacoesEntrada;
	}


	public void setValorOutrasMovimentacoesEntrada(
			BigDecimal valorOutrasMovimentacoesEntrada) {
		this.valorOutrasMovimentacoesEntrada = valorOutrasMovimentacoesEntrada;
	}


	public BigDecimal getValorExpedicao() {
		return valorExpedicao;
	}


	public void setValorExpedicao(BigDecimal valorExpedicao) {
		this.valorExpedicao = valorExpedicao;
	}


	public BigDecimal getValorOutrasMovimentacoesSaida() {
		return valorOutrasMovimentacoesSaida;
	}


	public void setValorOutrasMovimentacoesSaida(
			BigDecimal valorOutrasMovimentacoesSaida) {
		this.valorOutrasMovimentacoesSaida = valorOutrasMovimentacoesSaida;
	}


	public BigDecimal getValorAVista() {
		return valorAVista;
	}


	public void setValorAVista(BigDecimal valorAVista) {
		this.valorAVista = valorAVista;
	}
}
