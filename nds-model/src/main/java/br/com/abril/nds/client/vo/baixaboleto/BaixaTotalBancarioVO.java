package br.com.abril.nds.client.vo.baixaboleto;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre o total bancário.
 * 
 * Este VO será usado para exportar as informações dos seguintes grids de baixa automática:
 * 
 * 		- Total Bancário.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaTotalBancarioVO extends BaixaBoletoBaseVO {
	
	private static final long serialVersionUID = -9177133396271517680L;
	
	@Export(label = "Pago R$", exhibitionOrder=1)
	private BigDecimal valorPago;
	
	@Export(label="Data de Vencimento", exhibitionOrder=2)
	private String dataVencimento;
	
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
	 * @return the dataVencimento
	 */
	public String getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
}