package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object para filtro da pesquisa de 
 * controle de aprovação.
 * 
 * @author Discover Technology
 */
public class FiltroControleAprovacaoDTO extends FiltroDTO implements Serializable {
	
	private static final long serialVersionUID = -6999848869244920043L;

	private Date dataMovimento;
	
	private Long idTipoMovimento;
	
	private OrdenacaoColunaControleAprovacao ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroControleAprovacaoDTO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param dataMovimento - data de movimento
	 * @param idTipoMovimento - id tipo de movimento
	 */
	public FiltroControleAprovacaoDTO(Date dataMovimento, Long idTipoMovimento) {
		
		this.dataMovimento = dataMovimento;
		this.idTipoMovimento = idTipoMovimento;
	}

	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 */
	public enum OrdenacaoColunaControleAprovacao {
		
		TIPO_MOVIMENTO("tipoMovimento"),
		DATA_MOVIMENTO("dataMovimento"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		VALOR("valor"),
		PARCELAS("parcelas"),
		PRAZO("prazo"),
		REQUERENTE("requerente");
		
		private String nomeColuna;
		
		private OrdenacaoColunaControleAprovacao(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}

	/**
	 * @return the dataMovimento
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	
	/**
	 * @return the idTipoMovimento
	 */
	public Long getIdTipoMovimento() {
		return idTipoMovimento;
	}

	/**
	 * @param idTipoMovimento the idTipoMovimento to set
	 */
	public void setIdTipoMovimento(Long idTipoMovimento) {
		this.idTipoMovimento = idTipoMovimento;
	}

	/**
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColunaControleAprovacao getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaControleAprovacao ordenacaoColuna) {
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
				+ ((dataMovimento == null) ? 0 : dataMovimento.hashCode());
		result = prime * result
				+ ((idTipoMovimento == null) ? 0 : idTipoMovimento.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((super.paginacao == null) ? 0 : super.paginacao.hashCode());
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
		FiltroControleAprovacaoDTO other = (FiltroControleAprovacaoDTO) obj;
		if (dataMovimento == null) {
			if (other.dataMovimento != null)
				return false;
		} else if (!dataMovimento.equals(other.dataMovimento))
			return false;
		if (idTipoMovimento == null) {
			if (other.idTipoMovimento != null)
				return false;
		} else if (!idTipoMovimento.equals(other.idTipoMovimento))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (super.paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!super.paginacao.equals(other.paginacao))
			return false;
		return true;
	}

}
