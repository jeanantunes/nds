package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaEncalheDTO {
	
	@Export(label="Data")
	private Date dataRecolhimento;
	
	private Long idFornecedor;
	
	private Long idCota;

	@Export(label="Fornecedor")
	private String nomeFornecedor;
	
	@Export(label="Cota")
	private String nomeCota;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna {
		
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_CAPA("precoVenda"),
		PRECO_COM_DESCONTO("precoComDesconto"),
		REPARTE("reparte"),
		ENCALHE("encalhe"),
		FORNECEDOR("fornecedor"),
		TOTAL("total"),
		RECOLHIMENTO("recolhimento");
		
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
	 * Obtém dataRecolhimento
	 *
	 * @return Date
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * Atribuí dataRecolhimento
	 * @param dataRecolhimento 
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * Obtém idFornecedor
	 *
	 * @return Long
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * Atribuí idFornecedor
	 * @param idFornecedor 
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}


	/**
	 * Obtém paginacao
	 *
	 * @return PaginacaoVO
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * Atribuí paginacao
	 * @param paginacao 
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	/**
	 * Obtém ordenacaoColuna
	 *
	 * @return OrdenacaoColuna
	 */
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * Atribuí ordenacaoColuna
	 * @param ordenacaoColuna 
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	
	/**
	 * Obtém nomeFornecedor
	 *
	 * @return String
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * Atribuí nomeFornecedor
	 * @param nomeFornecedor 
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * Obtém nomeCota
	 *
	 * @return String
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * Atribuí nomeCota
	 * @param nomeCota 
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * Obtém idCota
	 *
	 * @return Long
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * Atribuí idCota
	 * @param idCota 
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dataRecolhimento == null) ? 0 : dataRecolhimento.hashCode());
		result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FiltroConsultaEncalheDTO)) {
			return false;
		}
		FiltroConsultaEncalheDTO other = (FiltroConsultaEncalheDTO) obj;
		if (dataRecolhimento == null) {
			if (other.dataRecolhimento != null) {
				return false;
			}
		} else if (!dataRecolhimento.equals(other.dataRecolhimento)) {
			return false;
		}
		if (idCota == null) {
			if (other.idCota != null) {
				return false;
			}
		} else if (!idCota.equals(other.idCota)) {
			return false;
		}
		if (idFornecedor == null) {
			if (other.idFornecedor != null) {
				return false;
			}
		} else if (!idFornecedor.equals(other.idFornecedor)) {
			return false;
		}
		if (ordenacaoColuna != other.ordenacaoColuna) {
			return false;
		}
		if (paginacao == null) {
			if (other.paginacao != null) {
				return false;
			}
		} else if (!paginacao.equals(other.paginacao)) {
			return false;
		}
		return true;
	}

	
	
}
