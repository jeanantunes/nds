package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Filtro utilizado para consulta de Fornecedores.
 * 
 * @author Discover Technology
 *
 */
public class FiltroConsultaFornecedorDTO implements Serializable {

	private static final long serialVersionUID = 4559738495050228125L;

	private String razaoSocial;
	
	private String cnpj;
	
	private String nomeFantasia;

	private ColunaOrdenacao colunaOrdenacao;
	
	private PaginacaoVO paginacao;
	
	public enum ColunaOrdenacao {
		
		CODIGO("codigoInterface"),
		RAZAO_SOCIAL("razaoSocial"),
		CNPJ("cnpj"),
		RESPONSAVEL("responsavel"),
		TELEFONE("telefone"),
		EMAIL("email");
		
		private String ordenacao;

		private ColunaOrdenacao(String ordenacao) {
			
			this.ordenacao = ordenacao;
		}

		/**
		 * @return the ordenacao
		 */
		public String getOrdenacao() {
			return ordenacao;
		}
	}
	
	/**
	 * @return the razaoSocial
	 */
	public String getRazaoSocial() {
		return razaoSocial;
	}

	/**
	 * @param razaoSocial the razaoSocial to set
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the nomeFantasia
	 */
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	/**
	 * @param nomeFantasia the nomeFantasia to set
	 */
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	/**
	 * @return the colunaOrdenacao
	 */
	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	/**
	 * @param colunaOrdenacao the colunaOrdenacao to set
	 */
	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
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
}
