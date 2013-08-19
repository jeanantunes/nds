package br.com.abril.nds.client.vo.baixaboleto;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre Boletos que foram baixados com divergências.
 * 
 * Este VO será usado para exportar as informações dos seguintes grids de baixa automática:
 * 
 * 		- Boletos Divergentes.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaBoletoDivergenteVO extends BaixaBoletoBaseVO {

	private static final long serialVersionUID = -9188939992448279466L;

	@Export(label = "Motivo", exhibitionOrder=-1)
	private String motivoDivergencia;

	@Export(label = "Valor R$", exhibitionOrder=1)
	private BigDecimal valorBoleto;
	
	@Export(label = "Pago R$", exhibitionOrder=2)
	private BigDecimal valorPago;
	
	@Export(label = "Diferença R$", exhibitionOrder=3)
	private BigDecimal diferencaValor;
	

	/**
	 * @return the motivoDivergencia
	 */
	public String getMotivoDivergencia() {
		return motivoDivergencia;
	}

	/**
	 * @param motivoDivergencia the motivoDivergencia to set
	 */
	public void setMotivoDivergencia(String motivoDivergencia) {
		this.motivoDivergencia = motivoDivergencia;
	}

	/**
	 * @return the valorPago
	 */
	public BigDecimal getValorPago() {
		return valorPago;
	}

	/**
	 * @param valorPago the valorPago to set
	 */
	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	/**
	 * @return the diferencaValor
	 */
	public BigDecimal getDiferencaValor() {
		return diferencaValor;
	}

	/**
	 * @param diferencaValor the diferencaValor to set
	 */
	public void setDiferencaValor(BigDecimal diferencaValor) {
		this.diferencaValor = diferencaValor;
	}

	/**
	 * @return the valorBoleto
	 */
	public BigDecimal getValorBoleto() {
		return valorBoleto;
	}

	/**
	 * @param valorBoleto the valorBoleto to set
	 */
	public void setValorBoleto(BigDecimal valorBoleto) {
		this.valorBoleto = valorBoleto;
	}
	
}
