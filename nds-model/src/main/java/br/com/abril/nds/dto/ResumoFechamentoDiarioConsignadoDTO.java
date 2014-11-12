package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;

public class ResumoFechamentoDiarioConsignadoDTO implements Serializable {

	private ResumoConsignado resumoConsignado;
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3572331535302313947L;
	
	private abstract class Resumo {
		
		protected BigDecimal saldoAnterior;
		
		protected BigDecimal valorEntradas;
		
		protected BigDecimal valorSaidas;
		
		protected BigDecimal saldoAtual;
		
		protected BigDecimal valorAlteracaoPreco;

		/**
		 * @return the saldoAnterior
		 */
		public BigDecimal getSaldoAnterior() {
			return saldoAnterior;
		}

		/**
		 * @param saldoAnterior the saldoAnterior to set
		 */
		public void setSaldoAnterior(BigDecimal saldoAnterior) {
			this.saldoAnterior = saldoAnterior;
		}

		/**
		 * @return the valorEntradas
		 */
		public BigDecimal getValorEntradas() {
			return valorEntradas;
		}

		/**
		 * @param valorEntradas the valorEntradas to set
		 */
		public void setValorEntradas(BigDecimal valorEntradas) {
			this.valorEntradas = valorEntradas;
		}

		/**
		 * @return the valorSaidas
		 */
		public BigDecimal getValorSaidas() {
			return valorSaidas;
		}

		/**
		 * @param valorSaidas the valorSaidas to set
		 */
		public void setValorSaidas(BigDecimal valorSaidas) {
			this.valorSaidas = valorSaidas;
		}

		/**
		 * @return the saldoAtual
		 */
		public BigDecimal getSaldoAtual() {
			return saldoAtual;
		}

		/**
		 * @param saldoAtual the saldoAtual to set
		 */
		public void setSaldoAtual(BigDecimal saldoAtual) {
			this.saldoAtual = saldoAtual;
		}
		
		public String getSaldoAnteriorFormatado() {
			
			return CurrencyUtil.formatarValor(this.saldoAnterior);
		}
		
		public String getSaldoAtualFormatado() {
			
			return CurrencyUtil.formatarValor(this.saldoAtual);
		}

		public String getValorEntradasFormatado() {
			
			return CurrencyUtil.formatarValor(this.valorEntradas);
		}
		
		public String getValorSaidasFormatado() {
			
			return CurrencyUtil.formatarValor(this.valorSaidas);
		}

		public BigDecimal getValorAlteracaoPreco() {
			return valorAlteracaoPreco;
		}

		public void setValorAlteracaoPreco(BigDecimal valorAlteracaoPreco) {
			this.valorAlteracaoPreco = valorAlteracaoPreco;
		}
		
	}
	
	public class ResumoConsignado extends Resumo {
		
		protected BigDecimal valorCE;
		
		protected BigDecimal valorExpedicao;
		
		protected BigDecimal valorOutrosValoresSaidas;
		
		protected BigDecimal valorOutrosValoresEntrada;
		
		protected BigDecimal valorAVista;
		
		protected BigDecimal valorAVistaCE;
		
		public BigDecimal getValorCE() {
			return valorCE;
		}

		public void setValorCE(BigDecimal valorCE) {
			this.valorCE = valorCE;
		}

		public BigDecimal getValorExpedicao() {
			return valorExpedicao;
		}

		public void setValorExpedicao(BigDecimal valorExpedicao) {
			this.valorExpedicao = valorExpedicao;
		}

		public BigDecimal getValorOutrosValoresSaidas() {
			return valorOutrosValoresSaidas;
		}

		public void setValorOutrosValoresSaidas(BigDecimal valorOutrosValoresSaidas) {
			this.valorOutrosValoresSaidas = valorOutrosValoresSaidas;
		}

		public BigDecimal getValorOutrosValoresEntrada() {
			return valorOutrosValoresEntrada;
		}

		public void setValorOutrosValoresEntrada(BigDecimal valorOutrosValoresEntrada) {
			this.valorOutrosValoresEntrada = valorOutrosValoresEntrada;
		}
		
		public String getValorOutrosValoresSaidasFormatado() {
			return CurrencyUtil.formatarValor(this.valorOutrosValoresSaidas);
		}
		
		public String getValorOutrosValoresEntradaFormatado() {
			return CurrencyUtil.formatarValor(this.valorOutrosValoresEntrada);
		}
		
		public String getValorCEFormatado() {
			return CurrencyUtil.formatarValor(this.valorCE);
		}
		
		public String getValorExpedicaoFormatado() {
			return CurrencyUtil.formatarValor(this.valorExpedicao);
		}
		
		public String getValorAVistaFormatado() {
			return CurrencyUtil.formatarValor(this.valorAVista);
		}
		
		public String getValorAVistaCEFormatado() {
			return CurrencyUtil.formatarValor(this.valorAVistaCE);
		}
		
		public BigDecimal getValorAVista() {
			return valorAVista;
		}

		public void setValorAVista(BigDecimal valorAVista) {
			this.valorAVista = valorAVista;
		}

		public BigDecimal getValorAVistaCE() {
			return valorAVistaCE;
		}

		public void setValorAVistaCE(BigDecimal valorAVistaCE) {
			this.valorAVistaCE = valorAVistaCE;
		}

	}

	/**
	 * @return the resumoConsignado
	 */
	public ResumoConsignado getResumoConsignado() {
		return resumoConsignado;
	}

	/**
	 * @param resumoConsignado the resumoConsignado to set
	 */
	public void setResumoConsignado(ResumoConsignado resumoConsignado) {
		this.resumoConsignado = resumoConsignado;
	}
}
