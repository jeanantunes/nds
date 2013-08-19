package br.com.abril.nds.client.vo;

import java.util.Date;
import java.util.List;

/**
 * Value Object para filtro da pesquisa de balanceamento de recolhimento.
 * 
 * @author Discover Technology
 */
public class FiltroPesquisaMatrizRecolhimentoVO {
	
	private Integer numeroSemana;
	
	private Date dataPesquisa;
	
	private List<Long> listaIdsFornecedores;

	public FiltroPesquisaMatrizRecolhimentoVO(Integer numeroSemana,
											  Date dataPesquisa,
											  List<Long> listaIdsFornecedores) {
		
		this.numeroSemana = numeroSemana;
		this.dataPesquisa = dataPesquisa;
		this.listaIdsFornecedores = listaIdsFornecedores;
	}
	
	/**
	 * @return the numeroSemana
	 */
	public Integer getNumeroSemana() {
		return numeroSemana;
	}

	/**
	 * @param numeroSemana the numeroSemana to set
	 */
	public void setNumeroSemana(Integer numeroSemana) {
		this.numeroSemana = numeroSemana;
	}

	/**
	 * @return the dataPesquisa
	 */
	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	/**
	 * @param dataPesquisa the dataPesquisa to set
	 */
	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	/**
	 * @return the listaIdsFornecedores
	 */
	public List<Long> getListaIdsFornecedores() {
		return listaIdsFornecedores;
	}

	/**
	 * @param listaIdsFornecedores the listaIdsFornecedores to set
	 */
	public void setListaIdsFornecedores(List<Long> listaIdsFornecedores) {
		this.listaIdsFornecedores = listaIdsFornecedores;
	}

}
