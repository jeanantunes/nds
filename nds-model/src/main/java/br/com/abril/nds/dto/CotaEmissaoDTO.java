package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaEmissaoDTO implements Serializable{

	private static final long serialVersionUID = 3527246197853581250L;

	@Export(label="Cota")
	private Integer numCota;
	@Export(label="Nome")
	private String nomeCota;
	@Export(label="Qtde. Exemplares")
	private Integer qtdeExemplares;
	
	/**
	 * @return the numCota
	 */
	public Integer getNumCota() {
		return numCota;
	}
	/**
	 * @param numCota the numCota to set
	 */
	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
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
	 * @return the qtdeExemplares
	 */
	public Integer getQtdeExemplares() {
		return qtdeExemplares;
	}
	/**
	 * @param qtdeExemplares the qtdeExemplares to set
	 */
	public void setQtdeExemplares(Integer qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
	}
	
	
}
