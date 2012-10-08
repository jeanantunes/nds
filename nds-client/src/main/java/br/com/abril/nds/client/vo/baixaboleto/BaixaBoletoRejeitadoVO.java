package br.com.abril.nds.client.vo.baixaboleto;

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

	@Export(label = "Histórico", exhibitionOrder=1)
	private String motivoRejeitado;

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
}
