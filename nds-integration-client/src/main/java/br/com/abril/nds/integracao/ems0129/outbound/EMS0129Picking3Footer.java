package br.com.abril.nds.integracao.ems0129.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking3Footer implements Serializable {
	
	private static final long serialVersionUID = -9095703543777297489L;
	
	private String identificadorLinha;

	private String numeroTotalCotas;
	
	private String quantidadeEfetiva;
	
	

	public EMS0129Picking3Footer(String identificadorLinha, String numeroTotalCotas, String quantidadeEfetiva) {
		this.identificadorLinha = identificadorLinha;
		this.numeroTotalCotas = numeroTotalCotas;
		this.quantidadeEfetiva = quantidadeEfetiva;
	}
	
	@Field(offset = 1, length = 2)
	public String getIdentificadorLinha() {
		return identificadorLinha;
	}

	@Field(offset = 3, length = 7)
	public String getNumeroTotalCotas() {
		return numeroTotalCotas;
	}

	@Field(offset = 10, length = 7)
	public String getQuantidadeEfetiva() {
		return quantidadeEfetiva;
	}

	public void setNumeroTotalCotas(String numeroTotalCotas) {
		this.numeroTotalCotas = numeroTotalCotas;
	}

	public void setQuantidadeEfetiva(String quantidadeEfetiva) {
		this.quantidadeEfetiva = quantidadeEfetiva;
	}

	

	public void setIdentificadorLinha(String identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}
	
	
	
	
	
	
}
