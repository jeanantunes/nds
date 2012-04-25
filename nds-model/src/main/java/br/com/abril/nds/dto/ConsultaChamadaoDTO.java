package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class ConsultaChamadaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4851929385744110963L;
	
	private List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO;
	
	private ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadao;

	public List<ConsignadoCotaChamadaoDTO> getListaConsignadoCotaChamadaoDTO() {
		return listaConsignadoCotaChamadaoDTO;
	}

	public void setListaConsignadoCotaChamadaoDTO(
			List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO) {
		this.listaConsignadoCotaChamadaoDTO = listaConsignadoCotaChamadaoDTO;
	}

	public ResumoConsignadoCotaChamadaoDTO getResumoConsignadoCotaChamadao() {
		return resumoConsignadoCotaChamadao;
	}

	public void setResumoConsignadoCotaChamadao(
			ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadao) {
		this.resumoConsignadoCotaChamadao = resumoConsignadoCotaChamadao;
	}
}