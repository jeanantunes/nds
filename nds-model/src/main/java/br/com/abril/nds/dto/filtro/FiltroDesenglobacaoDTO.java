package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.CotaDTO;

public class FiltroDesenglobacaoDTO extends FiltroDTO {

	private static final long serialVersionUID = -246049342296601717L;
	private CotaDTO cotaDto;

	public CotaDTO getCotaDto() {
		return cotaDto;
	}

	public void setCotaDto(CotaDTO cotaDto) {
		this.cotaDto = cotaDto;
	}

}
