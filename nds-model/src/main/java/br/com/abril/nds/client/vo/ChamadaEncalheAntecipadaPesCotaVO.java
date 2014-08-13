package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ChamadaEncalheAntecipadaPesCotaVO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Cota",alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String numeroCota;
	
	@Export(label = "Nome", exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Qtde.Exemplares",alignment = Alignment.CENTER, exhibitionOrder = 3)
	private String qntExemplares;
	
	public ChamadaEncalheAntecipadaPesCotaVO() {}
	
	public ChamadaEncalheAntecipadaPesCotaVO(String numeroCota,String nomeCota,String qntExemplares) {
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
	}
	
	/**
	 * @return the numeroCota
	 */
	public String getNumeroCota() {
		return numeroCota;
	}
	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}
	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}
	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	/**
	 * @return the qntExemplares
	 */
	public String getQntExemplares() {
		return qntExemplares;
	}
	/**
	 * @param qntExemplares the qntExemplares to set
	 */
	public void setQntExemplares(String qntExemplares) {
		this.qntExemplares = qntExemplares;
	}
	
	
	
}
