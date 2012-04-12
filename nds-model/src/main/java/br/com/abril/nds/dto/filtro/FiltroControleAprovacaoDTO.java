package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de 
 * controle de aprovação.
 * 
 * @author Discover Technology
 */
public class FiltroControleAprovacaoDTO implements Serializable {
	
	private static final long serialVersionUID = -6999848869244920043L;

	private Date dataMovimento;
	
	private TipoMovimento tipoMovimento;
	
	private PaginacaoVO paginacao;
	
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
	 * @param tipoMovimento - tipo de movimento
	 */
	public FiltroControleAprovacaoDTO(Date dataMovimento, TipoMovimento tipoMovimento) {
		
		this.dataMovimento = dataMovimento;
		this.tipoMovimento = tipoMovimento;
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
	 * @return the tipoMovimento
	 */
	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}

	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
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
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((tipoMovimento == null) ? 0 : tipoMovimento.hashCode());
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
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (tipoMovimento == null) {
			if (other.tipoMovimento != null)
				return false;
		} else if (!tipoMovimento.equals(other.tipoMovimento))
			return false;
		return true;
	}

}
