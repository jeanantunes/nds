package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroHistogramaVendas extends FiltroDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 94932782630094290L;
	private String filtroPor;
	private String inserirComponentes;
	private String componente;
	private String elemento;
	
	private String codigo;
	private String produto;
	private String edicao;
	private Long idTipoClassificacaoProduto;
	private Long idProduto;
	
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		CODIGO("codigoProduto"),
		CLASSIFICACAO("tipoClassificacaoFormatado"),
		EDICAO("edicao"),		
		PERIODO("periodo"),		
		REPARTE("reparte"),
		VENDA("venda"),
		DT_LANCAMENTO("dataLancamento"),
		DT_RECOLHIMENTO("dataRecolhimento"),
		STATUS("status");
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		@Override
		public String toString() {
			
			return this.descricao;
		}
	}
	
	public String getFiltroPor() {
		return filtroPor;
	}
	public void setFiltroPor(String filtroPor) {
		this.filtroPor = filtroPor;
	}
	public String getInserirComponentes() {
		return inserirComponentes;
	}
	public void setInserirComponentes(String inserirComponentes) {
		this.inserirComponentes = inserirComponentes;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
	}
	public String getElemento() {
		return elemento;
	}
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public String getEdicao() {
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}

	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}

	public Long getIdTipoClassificacaoProduto() {
		return idTipoClassificacaoProduto;
	}

	public void setIdTipoClassificacaoProduto(Long idTipoClassificacaoProduto) {
		this.idTipoClassificacaoProduto = idTipoClassificacaoProduto;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((componente == null) ? 0 : componente.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime * result
				+ ((elemento == null) ? 0 : elemento.hashCode());
		result = prime * result
				+ ((filtroPor == null) ? 0 : filtroPor.hashCode());
		result = prime
				* result
				+ ((inserirComponentes == null) ? 0 : inserirComponentes
						.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroHistogramaVendas other = (FiltroHistogramaVendas) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (componente == null) {
			if (other.componente != null)
				return false;
		} else if (!componente.equals(other.componente))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (elemento == null) {
			if (other.elemento != null)
				return false;
		} else if (!elemento.equals(other.elemento))
			return false;
		if (filtroPor == null) {
			if (other.filtroPor != null)
				return false;
		} else if (!filtroPor.equals(other.filtroPor))
			return false;
		if (inserirComponentes == null) {
			if (other.inserirComponentes != null)
				return false;
		} else if (!inserirComponentes.equals(other.inserirComponentes))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
}
