package br.com.abril.nds.util.export;

import java.util.List;

/**
 * Classe que define os parâmetros de exportação de arquivo.
 * 
 * @author Discover Technology
 *
 * @param <T> - tipo de dados da lista
 * @param <F> - tipo de dados do filtro
 * @param <FT> - tipo de dados do rodapé
 */
public class ExportParameters<T, F, FT> {
	
	private NDSFileHeader ndsFileHeader;
	
	private F filter;
	
	private FT footer;
	
	private List<T> dataList;
	
	private Class<T> listClass;

	/**
	 * Construtor padrão.
	 */
	public ExportParameters() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param ndsFileHeader
	 * @param filter
	 * @param footer
	 * @param dataList
	 * @param listClass
	 */
	public ExportParameters(NDSFileHeader ndsFileHeader, F filter, FT footer,
							List<T> dataList, Class<T> listClass) {
		
		this.ndsFileHeader = ndsFileHeader;
		this.filter = filter;
		this.footer = footer;
		this.dataList = dataList;
		this.listClass = listClass;
	}

	/**
	 * @return the ndsFileHeader
	 */
	public NDSFileHeader getNdsFileHeader() {
		return ndsFileHeader;
	}

	/**
	 * @param ndsFileHeader the ndsFileHeader to set
	 */
	public void setNdsFileHeader(NDSFileHeader ndsFileHeader) {
		this.ndsFileHeader = ndsFileHeader;
	}

	/**
	 * @return the filter
	 */
	public F getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(F filter) {
		this.filter = filter;
	}

	/**
	 * @return the footer
	 */
	public FT getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(FT footer) {
		this.footer = footer;
	}

	/**
	 * @return the dataList
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * @return the listClass
	 */
	public Class<T> getListClass() {
		return listClass;
	}

	/**
	 * @param listClass the listClass to set
	 */
	public void setListClass(Class<T> listClass) {
		this.listClass = listClass;
	}

}
