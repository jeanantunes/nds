package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Value Object para filtro da pesquisa de balanceamento de recolhimento.
 * 
 * @author Discover Technology
 */
@Exportable
public class FiltroPesquisaMatrizRecolhimentoVO implements Serializable {
	
    /**
     * 
     */
    private static final long serialVersionUID = 7085318512478940528L;
    
    @Export(label = "Semana")
	private Integer anoNumeroSemana;
	
	@Export(label="Data")
	private Date dataPesquisa;
	
	private List<Long> listaIdsFornecedores;
	
	private PaginacaoVO paginacaoVO;

	public FiltroPesquisaMatrizRecolhimentoVO(Integer anoNumeroSemana,
											  Date dataPesquisa,
											  List<Long> listaIdsFornecedores) {
		
		this.anoNumeroSemana = anoNumeroSemana;
		this.dataPesquisa = dataPesquisa;
		this.listaIdsFornecedores = listaIdsFornecedores;
	}
	
	/**
	 * @return the anoNumeroSemana
	 */
	public Integer getAnoNumeroSemana() {
		return anoNumeroSemana;
	}

	/**
	 * @param anoNumeroSemana the anoNumeroSemana to set
	 */
	public void setAnoNumeroSemana(Integer anoNumeroSemana) {
		this.anoNumeroSemana = anoNumeroSemana;
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

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}

}
