package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ConsultaRoteirizacaoDTO  implements Serializable{

	private static final long serialVersionUID = -3737578545975332770L;

	@Export(label = "Box", alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String nomeBox;
	
	@Export(label = "Roteiro", alignment = Alignment.CENTER, exhibitionOrder = 2)
	private String descricaoRoteiro;
	
	@Export(label = "Rota", alignment = Alignment.CENTER, exhibitionOrder = 3)
	private String descricaoRota;
	
	@Export(label = "Cota", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private String nome ;
	
	private Long qntCotas;
	
	public ConsultaRoteirizacaoDTO() {}
		
	
	/**
	 * @return the qntCotas
	 */
	public Long getQntCotas() {
		return qntCotas;
	}

	/**
	 * @param qntCotas the qntCotas to set
	 */
	public void setQntCotas(Long qntCotas) {
		this.qntCotas = qntCotas;
	}



	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}

	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}

	public String getDescricaoRota() {
		return descricaoRota;
	}

	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
