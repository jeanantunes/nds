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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		FiltroHistogramaVendas otherFiltro = (FiltroHistogramaVendas)obj;
		
		String[] values = { this.getFiltroPor(), this.getInserirComponentes(),
				this.getComponente(), this.getElemento(), this.getCodigo(),
				this.getProduto(), this.getEdicao() };
		
		String[] otherValues = { otherFiltro.getFiltroPor(), otherFiltro.getInserirComponentes(),
				otherFiltro.getComponente(), otherFiltro.getElemento(), otherFiltro.getCodigo(),
				otherFiltro.getProduto(), otherFiltro.getEdicao() };
		
		
		for (int i = 0; i < values.length; i++) {
			String v = values[i];
			String otherValue = otherValues[i];
			
			if (v == null) {
				if (otherValue != null)
					return false;
			} else if (!v.equals(otherValue))
				return false;
			
		}
		
		return true;
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
	
}
