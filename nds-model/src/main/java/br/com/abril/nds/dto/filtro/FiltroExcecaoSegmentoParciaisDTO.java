package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroExcecaoSegmentoParciaisDTO extends FiltroDTO {

	private static final long serialVersionUID = -1787036634086865972L;

	private Long idExcecaoSegmentoProduto;
	private CotaDTO cotaDto;
	private ProdutoDTO produtoDto;
	
	private boolean excecaoSegmento;
	private boolean reload;
	private boolean autoComplete;

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

	public boolean isAutoComplete() {
		return autoComplete;
	}

	public void setAutoComplete(boolean autoComplete) {
		this.autoComplete = autoComplete;
	}

	public ProdutoDTO getProdutoDto() {
		return produtoDto;
	}

	public void setProdutoDto(ProdutoDTO produtoDto) {
		this.produtoDto = produtoDto;
	}
	
}