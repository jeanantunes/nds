package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.CotaDTO;

public class FiltroExcecaoSegmentoParciaisDTO extends FiltroDTO {

	private static final long serialVersionUID = -1787036634086865972L;

	private Long idExcecaoSegmentoProduto;
	private CotaDTO cotaDto;
	private String nomeProduto;
	
	private boolean excecaoSegmento;
	private boolean excecaoParciais;
	private boolean reload;

	// fazer restante do filtro por produto

	public Long getIdExcecaoSegmentoProduto() {
		return idExcecaoSegmentoProduto;
	}

	public void setIdExcecaoSegmentoProduto(Long idExcecaoSegmentoProduto) {
		this.idExcecaoSegmentoProduto = idExcecaoSegmentoProduto;
	}

	public CotaDTO getCotaDto() {
		return cotaDto;
	}

	public void setCotaDto(CotaDTO cotaDto) {
		this.cotaDto = cotaDto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public boolean isExcecaoSegmento() {
		return excecaoSegmento;
	}

	public void setExcecaoSegmento(boolean excecaoSegmento) {
		this.excecaoSegmento = excecaoSegmento;
	}

	public boolean isReload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	public boolean isExcecaoParciais() {
		return excecaoParciais;
	}

	public void setExcecaoParciais(boolean excecaoParciais) {
		this.excecaoParciais = excecaoParciais;
	}

}
