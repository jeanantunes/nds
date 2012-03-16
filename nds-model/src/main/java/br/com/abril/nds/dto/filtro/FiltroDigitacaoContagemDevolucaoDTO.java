package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de digitação contagem devolução
 * 
 * @author Discover Technology
 *
 */
public class FiltroDigitacaoContagemDevolucaoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date dataPesquisa;
	
	private Long idFornecedor;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroDigitacaoContagemDevolucaoDTO(){}	
	
	
	public FiltroDigitacaoContagemDevolucaoDTO(Date dataPesquisa, Long idFornecedor){
		this.dataPesquisa = dataPesquisa;
		this.idFornecedor = idFornecedor;
	}	
	
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna{
		
		NOSSO_NUMERO("nossoNumero"),
		DATA_EMISSAO("dataVencimento"),
		DATA_VENCIMENTO("dataVencimento"),
		DATA_PAGAMENTO("dataPagamento"),
		ENCARGOS("encargos"),
		VALOR("valor"),
		TIPO_BAIXA("tipoBaixa"),
		STATUS("status"),
		ACAO("acao");
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
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
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
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
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataPesquisa == null) ? 0 : dataPesquisa.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroDigitacaoContagemDevolucaoDTO other = (FiltroDigitacaoContagemDevolucaoDTO) obj;
		if (dataPesquisa == null) {
			if (other.dataPesquisa != null)
				return false;
		} else if (!dataPesquisa.equals(other.dataPesquisa))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}
	
	
}
