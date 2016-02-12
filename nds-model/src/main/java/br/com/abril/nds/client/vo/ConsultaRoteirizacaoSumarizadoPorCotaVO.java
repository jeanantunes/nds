package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaRoteirizacaoSumarizadoPorCotaVO  implements Serializable {

	private static final long serialVersionUID = 1126569312876362107L;

	@Export(label = "Box", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String nomeBox;
	
	@Export(label = "Roteiro", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String descricaoRoteiro;
	
	@Export(label = "Rota", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String descricaoRota;
	
	@Export(label = "Qtd. Cotas", alignment = Alignment.LEFT, exhibitionOrder = 4)
	private Long qntCotas;
	
	
	private List<ConsultaRoteirizacaoDTO> itens;
	
	/**
	 * Obtém nomeBox
	 *
	 * @return String
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * Atribuí nomeBox
	 * @param nomeBox 
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * Obtém descricaoRoteiro
	 *
	 * @return String
	 */
	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}

	/**
	 * Atribuí descricaoRoteiro
	 * @param descricaoRoteiro 
	 */
	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}

	/**
	 * Obtém descricaoRota
	 *
	 * @return String
	 */
	public String getDescricaoRota() {
		return descricaoRota;
	}

	/**
	 * Atribuí descricaoRota
	 * @param descricaoRota 
	 */
	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	/**
	 * Obtém qntCotas
	 *
	 * @return Long
	 */
	public Long getQntCotas() {
		return qntCotas;
	}

	/**
	 * Atribuí qntCotas
	 * @param qntCotas 
	 */
	public void setQntCotas(Long qntCotas) {
		this.qntCotas = qntCotas;
	}

	public List<ConsultaRoteirizacaoDTO> getItens() {
		return itens;
	}

	public void setItens(List<ConsultaRoteirizacaoDTO> itens) {
		this.itens = itens;
	}	
}