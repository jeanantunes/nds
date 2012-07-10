package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object que representa a consulta de um lote de Notas Fiscais.
 * 
 * @author Discover Technology
 *
 */
public class ConsultaLoteNotaFiscalDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8316849144436282133L;

	private Set<Long> idsCotasDestinatarias;
	
	private Date dateEmissao;
	
	private Intervalo<Date> periodoMovimento;
	
	private TipoNotaFiscal tipoNotaFiscal;
	
	private List<Produto> listaProdutos;
	
	private List<Fornecedor> listaFornecedores;
	
	private PaginacaoVO paginacao;
	
	/**
	 * Construtor padrão.
	 */
	public ConsultaLoteNotaFiscalDTO() {
		
	}

	/**
	 * @return the idsCotasDestinatarias
	 */
	public Set<Long> getIdsCotasDestinatarias() {
		return idsCotasDestinatarias;
	}

	/**
	 * @param idsCotasDestinatarias the idsCotasDestinatarias to set
	 */
	public void setIdsCotasDestinatarias(Set<Long> idsCotasDestinatarias) {
		this.idsCotasDestinatarias = idsCotasDestinatarias;
	}

	/**
	 * @return the periodoMovimento
	 */
	public Intervalo<Date> getPeriodoMovimento() {
		return periodoMovimento;
	}

	/**
	 * @param periodoMovimento the periodoMovimento to set
	 */
	public void setPeriodoMovimento(Intervalo<Date> periodoMovimento) {
		this.periodoMovimento = periodoMovimento;
	}

	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	/**
	 * @return the dateEmissao
	 */
	public Date getDateEmissao() {
		return dateEmissao;
	}

	/**
	 * @param dateEmissao the dateEmissao to set
	 */
	public void setDateEmissao(Date dateEmissao) {
		this.dateEmissao = dateEmissao;
	}

	/**
	 * @return the listaProdutos
	 */
	public List<Produto> getListaProdutos() {
		return listaProdutos;
	}

	/**
	 * @param listaProdutos the listaProdutos to set
	 */
	public void setListaProdutos(List<Produto> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	/**
	 * @return the listaFornecedores
	 */
	public List<Fornecedor> getListaFornecedores() {
		return listaFornecedores;
	}

	/**
	 * @param listaFornecedores the listaFornecedores to set
	 */
	public void setListaFornecedores(List<Fornecedor> listaFornecedores) {
		this.listaFornecedores = listaFornecedores;
	}

}
