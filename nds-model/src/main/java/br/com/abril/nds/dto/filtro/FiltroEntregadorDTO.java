package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

/**
 * DTO que representa o filtro de pesquisa relacionado a entidade
 * {@link br.com.abril.nds.model.cadastro.Entregador} 
 * 
 * @author Discover Technology
 *
 */
public class FiltroEntregadorDTO implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -6611945974104514807L;

	private String nomeRazaoSocial;
	
	private String apelidoNomeFantasia;
	
	private String cpfCnpj;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaEntregador ordenacaoColunaEntregador;
	
	/**
	 * Enum que representa as colunas ordenáveis para pesquisa de entregadores. 
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaEntregador {
		
		CODIGO("codigo"),
		NOME_RAZAO_SOCIAL("nomeRazaoSocial"),
		CPF_CNPJ("cpfCnpj"),
		APELIDO_NOME_FANTASIA("apelidoNomeFantasia"),
		TELEFONE("telefone"),
		EMAIL("email");
		
		private String coluna;

		/**
		 * Construtor.
		 * 
		 * @param coluna
		 */
		OrdenacaoColunaEntregador(String coluna) {
			
			this.coluna = coluna;
		}
		
		/**
		 * @return the coluna
		 */
		public String getColuna() {
			return coluna;
		}

		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return this.coluna;
		}
	}

	/**
	 * @return the nomeRazaoSocial
	 */
	public String getNomeRazaoSocial() {
		return nomeRazaoSocial;
	}

	/**
	 * @param nomeRazaoSocial the nomeRazaoSocial to set
	 */
	public void setNomeRazaoSocial(String nomeRazaoSocial) {
		this.nomeRazaoSocial = nomeRazaoSocial;
	}

	/**
	 * @return the apelidoNomeFantasia
	 */
	public String getApelidoNomeFantasia() {
		return apelidoNomeFantasia;
	}

	/**
	 * @param apelidoNomeFantasia the apelidoNomeFantasia to set
	 */
	public void setApelidoNomeFantasia(String apelidoNomeFantasia) {
		this.apelidoNomeFantasia = apelidoNomeFantasia;
	}

	/**
	 * @return the cpfCnpj
	 */
	public String getCpfCnpj() {
		return cpfCnpj;
	}

	/**
	 * @param cpfCnpj the cpfCnpj to set
	 */
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
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
	 * @return the ordenacaoColunaEntregador
	 */
	public OrdenacaoColunaEntregador getOrdenacaoColunaEntregador() {
		return ordenacaoColunaEntregador;
	}

	/**
	 * @param ordenacaoColunaEntregador the ordenacaoColunaEntregador to set
	 */
	public void setOrdenacaoColunaEntregador(
			OrdenacaoColunaEntregador ordenacaoColunaEntregador) {
		this.ordenacaoColunaEntregador = ordenacaoColunaEntregador;
	}
}
