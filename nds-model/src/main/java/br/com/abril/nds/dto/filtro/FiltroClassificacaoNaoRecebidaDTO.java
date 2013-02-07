package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroClassificacaoNaoRecebidaDTO extends FiltroDTO {

	private static final long serialVersionUID = 8700462255954193185L;
	
	private Long idTipoClassificacaoProduto;
	private CotaDTO cotaDto;
	
	public Long getIdTipoClassificacaoProduto() {
		return idTipoClassificacaoProduto;
	}
	public void setIdTipoClassificacaoProduto(Long idTipoClassificacaoProduto) {
		this.idTipoClassificacaoProduto = idTipoClassificacaoProduto;
	}
	public CotaDTO getCotaDto() {
		return cotaDto;
	}
	public void setCotaDto(CotaDTO cotaDto) {
		this.cotaDto = cotaDto;
	}

}
