package br.com.abril.nds.client.vo.baixaboleto;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre Boletos que foram rejeitados.
 * 
 * Este VO será usado para exportar as informações dos seguintes grids de baixa automática:
 * 
 * 		- Boletos Rejeitados.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaBoletoRejeitadoVO extends BaixaBoletoBaseVO {

	private static final long serialVersionUID = 5156198122887009066L;

	 
	@Export(label="Cota", exhibitionOrder=-4, widthPercent = 5)
	private Integer numeroCota;
	
	@Export(label="Nome", exhibitionOrder=-3, widthPercent = 30)
	private String nomeCota;
	
	@Export(label="NossoNumero", exhibitionOrder=-3, widthPercent = 10)
	private String nossoNumero;
	

	@Export(label = "Histórico", exhibitionOrder=-1)
	private String motivoRejeitado;
	
	@Export(label = "Valor R$", exhibitionOrder=1)
	private BigDecimal valorBoleto;

	/**
	 * @return the motivoRejeitado
	 */
	public String getMotivoRejeitado() {
		return motivoRejeitado;
	}

	/**
	 * @param motivoRejeitado the motivoRejeitado to set
	 */
	public void setMotivoRejeitado(String motivoRejeitado) {
		this.motivoRejeitado = motivoRejeitado;
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
	
	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	
}
