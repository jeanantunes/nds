package br.com.abril.nds.client.vo.baixaboleto;

import java.math.BigDecimal;
import java.util.Date;

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

	@Export(label="Cota", exhibitionOrder=-3, widthPercent = 5)
	private Integer numeroCota;
	
	@Export(label="Nome", exhibitionOrder=-2, widthPercent = 30)
	private String nomeCota;

	@Export(label = "Motivo", exhibitionOrder=-1)
	private String motivoDivergencia;

	@Export(label = "Valor R$", exhibitionOrder=1)
	private BigDecimal valorBoleto;
	
	@Export(label = "Pago R$", exhibitionOrder=2)
	private BigDecimal valorPago;
	
	@Export(label = "Diferença R$", exhibitionOrder=3)
	private BigDecimal diferencaValor;
	
	@Export(label = "Data Emissão", exhibitionOrder=4)
	private String dataEmissao;
	
	@Export(label = "Data Vencimento", exhibitionOrder=5)
	private String dataVencimento;
	

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

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
}
