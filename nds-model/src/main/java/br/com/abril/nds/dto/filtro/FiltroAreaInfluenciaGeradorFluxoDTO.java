package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroAreaInfluenciaGeradorFluxoDTO {

	private Integer numeroCota;
	private String nomeCota;
	private Long areaInfluenciaId;
	private Long geradorFluxoPrincipalId;
	private Long geradorFluxoSecundarioId;
	private boolean cotasAtivas; // False = todas as cotas

	private PaginacaoVO paginacao;

	public enum ColunaOrdenacao {

		EDICAO("edicao"), CHAMADA_CAPA("chamadaCapa");

		private String nomeColuna;

		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}

		@Override
		public String toString() {
			return this.nomeColuna;
		}

		public static ColunaOrdenacao getPorDescricao(String descricao) {
			for (ColunaOrdenacao coluna : ColunaOrdenacao.values()) {
				if (coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public FiltroAreaInfluenciaGeradorFluxoDTO() {

	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		
		if (object == null) 
			return false;

		if (this.getClass() != object.getClass())
			return false;
		
		FiltroAreaInfluenciaGeradorFluxoDTO other = (FiltroAreaInfluenciaGeradorFluxoDTO) object;
		
		String novoNomeCota = "";
		String nomeCota = "";
		
		if (other.getNomeCota() != null) {
			novoNomeCota = other.getNomeCota(); 	
		}
		
		if (this.getNomeCota() != null) {
			nomeCota = this.getNomeCota();
		}
		
		if (this.getNumeroCota() == other.getNumeroCota() &&
			nomeCota.equals(novoNomeCota) &&
			this.getAreaInfluenciaId() == other.getAreaInfluenciaId() &&
			this.getGeradorFluxoPrincipalId() == other.getGeradorFluxoPrincipalId() &&
			this.getGeradorFluxoSecundarioId() == other.getGeradorFluxoSecundarioId() &&
			this.isCotasAtivas() == other.isCotasAtivas()) {
			return true;
		}else {
			return false;
		}
	}
	
	
	// GETTER AND SETTERS
	
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

	public Long getAreaInfluenciaId() {
		return areaInfluenciaId;
	}

	public void setAreaInfluenciaId(Long areaInfluenciaId) {
		this.areaInfluenciaId = areaInfluenciaId;
	}

	public Long getGeradorFluxoPrincipalId() {
		return geradorFluxoPrincipalId;
	}

	public void setGeradorFluxoPrincipalId(Long geradorFluxoPrincipalId) {
		this.geradorFluxoPrincipalId = geradorFluxoPrincipalId;
	}

	public Long getGeradorFluxoSecundarioId() {
		return geradorFluxoSecundarioId;
	}

	public void setGeradorFluxoSecundarioId(Long geradorFluxoSecundarioId) {
		this.geradorFluxoSecundarioId = geradorFluxoSecundarioId;
	}

	public boolean isCotasAtivas() {
		return cotasAtivas;
	}

	public void setCotasAtivas(boolean cotasAtivas) {
		this.cotasAtivas = cotasAtivas;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

}