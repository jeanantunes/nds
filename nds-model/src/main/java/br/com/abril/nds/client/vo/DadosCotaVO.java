package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.dto.ItemDTO;

public class DadosCotaVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private  String dataInicioAtividade;
	
	private List<ItemDTO<String, String>> listaClassificacao;
	
	private Integer numeroSugestaoCota;
	
	private String status;

	/**
	 * @return the dataInicioAtividae
	 */
	public String getDataInicioAtividade() {
		return dataInicioAtividade;
	}

	/**
	 * @param dataInicioAtividae the dataInicioAtividae to set
	 */
	public void setDataInicioAtividade(String dataInicioAtividade) {
		this.dataInicioAtividade = dataInicioAtividade;
	}

	/**
	 * @return the listaClassificacao
	 */
	public List<ItemDTO<String, String>> getListaClassificacao() {
		return listaClassificacao;
	}

	/**
	 * @param listaClassificacao the listaClassificacao to set
	 */
	public void setListaClassificacao(
			List<ItemDTO<String, String>> listaClassificacao) {
		this.listaClassificacao = listaClassificacao;
	}

	/**
	 * @return the numeroSugestaoCota
	 */
	public Integer getNumeroSugestaoCota() {
		return numeroSugestaoCota;
	}

	/**
	 * @param numeroSugestaoCota the numeroSugestaoCota to set
	 */
	public void setNumeroSugestaoCota(Integer numeroSugestaoCota) {
		this.numeroSugestaoCota = numeroSugestaoCota;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
