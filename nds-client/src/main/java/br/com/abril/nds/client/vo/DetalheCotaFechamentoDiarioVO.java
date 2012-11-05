package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
@SuppressWarnings("serial")
public class DetalheCotaFechamentoDiarioVO implements Serializable {

	@Export(label = "Cota", alignment = Alignment.LEFT)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.LEFT)
	private String nome;

	/**
	 * Construtor.
	 */
	public DetalheCotaFechamentoDiarioVO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param numeroCota - número da cota
	 * @param nome - nome da cota
	 */
	public DetalheCotaFechamentoDiarioVO(Integer numeroCota, String nome) {
		
		this.numeroCota = numeroCota;
		this.nome = nome;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
