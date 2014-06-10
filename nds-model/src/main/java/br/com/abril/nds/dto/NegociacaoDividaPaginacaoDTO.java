package br.com.abril.nds.dto;

import java.util.List;

public class NegociacaoDividaPaginacaoDTO {

	private List<NegociacaoDividaDTO> listaNegociacaoDividaDTO;
	
	private Long quantidadeRegistros;

	public List<NegociacaoDividaDTO> getListaNegociacaoDividaDTO() {
		return listaNegociacaoDividaDTO;
	}

	public void setListaNegociacaoDividaDTO(
			List<NegociacaoDividaDTO> listaNegociacaoDividaDTO) {
		this.listaNegociacaoDividaDTO = listaNegociacaoDividaDTO;
	}

	public Long getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(Long quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}
}