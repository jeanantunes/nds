package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroAreaInfluenciaGeradorFluxoDTO {

	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long areaInfluenciaId;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private Long geradorFluxoPrincipalId;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 5)
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((areaInfluenciaId == null) ? 0 : areaInfluenciaId.hashCode());
		result = prime * result + (cotasAtivas ? 1231 : 1237);
		result = prime
				* result
				+ ((geradorFluxoPrincipalId == null) ? 0
						: geradorFluxoPrincipalId.hashCode());
		result = prime
				* result
				+ ((geradorFluxoSecundarioId == null) ? 0
						: geradorFluxoSecundarioId.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroAreaInfluenciaGeradorFluxoDTO other = (FiltroAreaInfluenciaGeradorFluxoDTO) obj;
		if (areaInfluenciaId == null) {
			if (other.areaInfluenciaId != null)
				return false;
		} else if (!areaInfluenciaId.equals(other.areaInfluenciaId))
			return false;
		if (cotasAtivas != other.cotasAtivas)
			return false;
		if (geradorFluxoPrincipalId == null) {
			if (other.geradorFluxoPrincipalId != null)
				return false;
		} else if (!geradorFluxoPrincipalId
				.equals(other.geradorFluxoPrincipalId))
			return false;
		if (geradorFluxoSecundarioId == null) {
			if (other.geradorFluxoSecundarioId != null)
				return false;
		} else if (!geradorFluxoSecundarioId
				.equals(other.geradorFluxoSecundarioId))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		return true;
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
