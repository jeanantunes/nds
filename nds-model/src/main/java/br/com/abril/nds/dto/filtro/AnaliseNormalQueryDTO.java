package br.com.abril.nds.dto.filtro;

public class AnaliseNormalQueryDTO {

	private String sortName;
	private String sortOrder;
	private Long estudoId;
	private String filterSortName;
	private Double filterSortFrom;
	private Double filterSortTo;
	private int[] arrayEdicoes;
	private String elemento;
	
	
	public String getTipoElemento() {
		return elemento.substring(0, elemento.lastIndexOf("_"));
	}

	public String getValorElemento() {
		return elemento.substring(elemento.lastIndexOf("_") + 1);
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public boolean possuiElemento() {
		return elemento != null && !elemento.isEmpty();
	}
	
	public boolean elementoIsTipoPontoVenda() {
		return getTipoElemento().equals("tipo_ponto_venda");
	}

	public boolean elementoIsGeradoorDeFluxo() {
		return getTipoElemento().equals("gerador_de_fluxo");
	}

	public boolean elementoIsBairro() {
		return getTipoElemento().equals("bairro");
	}

	public boolean elementoIsRegiao() {
		return getTipoElemento().equals("regiao");
	}

	public boolean elementoIsAreaDeInfluencia() {
		return getTipoElemento().equals("area_de_influencia");
	}

	public boolean elementoIsDistrito() {
		return getTipoElemento().equals("distrito");
	}

	public boolean elementoIsCotasAVista() {
		return getTipoElemento().equals("cotas_a_vista");
	}

	public boolean elementoIsCotasNovas() {
		return getTipoElemento().equals("cotas_novas");
	}

	public void sortGrid(String sortName,String sortOrder) {
		this.sortName = sortName.equals("undefined")?null:sortName;
		this.sortOrder = sortOrder.equals("undefined")?null:sortOrder;
	}
	
	public String getSortName() {
		return sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void estudoId(Long estudoId) {
		this.estudoId = estudoId;
	}
	
	public Long getEstudoId() {
		return estudoId;
	}
	
	public int[] edicoes() {
		return arrayEdicoes;
	}

	public void filterSort(String filterSortName, Double filterSortFrom,
			Double filterSortTo) {
				this.filterSortName = filterSortName;
				this.filterSortFrom = filterSortFrom;
				this.filterSortTo = filterSortTo;
	}

	public boolean hasGridSort() {
		return sortName != null && !sortName.isEmpty() && sortOrder != null && !sortOrder.isEmpty();
	}

	public boolean isRankingFilteredSorted() {
		return "filtroRanking".equals(filterSortName) &&
				isFromOrToPopulated();
	}
	
	public boolean isReparteFilteredSorted() {
		return "filtroReparte".equals(filterSortName) &&
				isFromOrToPopulated();
	}

	private boolean isFromOrToPopulated() {
		return filterSortFrom != null || filterSortTo != null;
	}

	public Double from() {
		return this.filterSortFrom;
	}

	public Double to() {
		return this.filterSortTo;
	}

	@Override
	public String toString() {
		return "AnaliseNormalQueryDTO [sortName=" + sortName + ", sortOrder="
				+ sortOrder + ", estudoId=" + estudoId + ", filterSortName="
				+ filterSortName + ", filterSortFrom=" + filterSortFrom
				+ ", filterSortTo=" + filterSortTo + "]";
	}

	public boolean isPorcentagemVendaFilteredSorted() {
		return "filtroPercVenda".equals(filterSortName) && isFromOrToPopulated();
	}

	public void edicoes(int[] arrayEdicoes) {
		this.arrayEdicoes = arrayEdicoes;
		
	}

}
