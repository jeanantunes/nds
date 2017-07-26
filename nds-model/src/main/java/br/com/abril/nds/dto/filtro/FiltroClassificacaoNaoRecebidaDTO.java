package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroClassificacaoNaoRecebidaDTO extends FiltroDTO {

	private static final long serialVersionUID = 8700462255954193185L;

	private Long idTipoClassificacaoProduto;
	private CotaDTO cotaDto;

	private boolean autoComplete;
	
	@Export(label="Número cota")
	private Integer numeroCota;
	
	@Export(label="Nome cota")
	private String nomeCota;
	
	@Export(label="Tipo classificação")
	private String classificacao;
	

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

	public boolean isAutoComplete() {
		return autoComplete;
	}

	public void setAutoComplete(boolean autoComplete) {
		this.autoComplete = autoComplete;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	
}
