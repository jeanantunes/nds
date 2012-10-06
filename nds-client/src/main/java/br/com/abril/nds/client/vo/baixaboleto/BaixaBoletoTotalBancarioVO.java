package br.com.abril.nds.client.vo.baixaboleto;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre Total Bancário realizado em determinada data.
 * 
 * Este VO será usado para exportar as informações dos seguintes grids de baixa automática:
 * 
 * 		- Total bancário.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaBoletoTotalBancarioVO extends BaixaBoletoBaseVO {

	private static final long serialVersionUID = 2582894777493594258L;

	@Export(label = "Data Vencimento", exhibitionOrder=4)
	private Date dataVencimento;

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
}
