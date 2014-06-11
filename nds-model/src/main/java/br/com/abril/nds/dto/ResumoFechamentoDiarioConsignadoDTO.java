package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;

public class ResumoFechamentoDiarioConsignadoDTO implements Serializable {

	private ResumoConsignado resumoConsignado;
	
	private ResumoAVista resumoAVista;
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3572331535302313947L;
	
	private abstract class Resumo {
		
		protected BigDecimal saldoAnterior;
		
		protected BigDecimal valorEntradas;
		
		protected BigDecimal valorSaidas;
		
		protected BigDecimal saldoAtual;

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
		
	}
	
	public class ResumoConsignado extends Resumo {}
	
	public class ResumoAVista extends Resumo {}

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

	/**
	 * @return the resumoAVista
	 */
	public ResumoAVista getResumoAVista() {
		return resumoAVista;
	}

	/**
	 * @param resumoAVista the resumoAVista to set
	 */
	public void setResumoAVista(ResumoAVista resumoAVista) {
		this.resumoAVista = resumoAVista;
	}

}
