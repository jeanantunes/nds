package br.com.abril.nds.dto.filtro;

public class AnaliseParcialQueryDTO {

	private long estudoId;
	private String sortName;
	private String sortOrder;
	private String filterSortName;
	private Double filterSortFrom;
	private Double filterSortTo;
	private String elemento;
	private Long faixaDe;
	private Long faixaAte;
	private String numeroCotaStr;
	
	public long getEstudoId() {
		return estudoId;
	}

	public void setEstudoId(long id) {
		this.estudoId = id;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String name) {
		this.sortName = name;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getFilterSortName() {
		return filterSortName;
	}

	public void setFilterSortName(String filterSortName) {
		this.filterSortName = filterSortName;
	}

	public Double getFilterSortFrom() {
		return filterSortFrom;
	}

	public void setFilterSortFrom(Double filterSortFrom) {
		this.filterSortFrom = filterSortFrom;
	}

	public Double getFilterSortTo() {
		return filterSortTo;
	}

	public void setFilterSortTo(Double filterSortTo) {
		this.filterSortTo = filterSortTo;
	}

	public boolean possuiOrderBy() {
		return this.getSortOrder() != null && this.getSortName() != null;
	}

	public boolean possuiOrdenacaoPlusFiltro() {
		return filterSortName != null && filterSortFrom != null
				&& filterSortTo != null;
	}

	public boolean possuiOrdenacaoRanking() {
		return possuiOrdenacaoPlusFiltro() && filterSortName.equals("ranking");
	}

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

	public boolean possuiOrdenacaoReparte() {
		return possuiOrdenacaoPlusFiltro() && filterSortName.equals("reparte");
	}

	public boolean possuiPercentualDeVenda() {
		return possuiOrdenacaoPlusFiltro() && filterSortName.equals("percentual_de_venda");
	}

	public boolean possuiReducaoReparte() {
		return possuiOrdenacaoPlusFiltro() && filterSortName.equals("reducao_de_reparte");
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

	public Long getFaixaDe() {
	    return faixaDe;
	}

	public void setFaixaDe(Long faixaDe) {
	    this.faixaDe = faixaDe;
	}

	public Long getFaixaAte() {
	    return faixaAte;
	}

	public void setFaixaAte(Long faixaAte) {
	    this.faixaAte = faixaAte;
	}

	public String getNumeroCotaStr() {
		return numeroCotaStr;
	}

	public void setNumeroCotaStr(String numeroCotaStr) {
		this.numeroCotaStr = numeroCotaStr;
	}
	
	
}
