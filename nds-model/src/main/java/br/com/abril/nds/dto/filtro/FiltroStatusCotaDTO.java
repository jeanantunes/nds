package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

/**
 * Data Transfer Object para filtro de pesquisa de status da cota.
 * 
 * @author Discover Technology
 *
 */
public class FiltroStatusCotaDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1676192822603323258L;
	
	private Integer numeroCota;
	
	private SituacaoCadastro statusCota;
	
	private PeriodoVO periodo;
	
	private MotivoAlteracaoSituacao motivoStatusCota;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunasStatusCota ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroStatusCotaDTO() {
		
	}
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunasStatusCota {
		
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		DATA("data"),
		STATUS_ANTERIOR("statusAnterior"),
		STATUS_ATUALIZADO("statusAtualizado"),
		USUARIO("usuario"),
		MOTIVO("motivo"),
		DESCRICAO("descricao");
		
		private String nomeColuna;
		
		private OrdenacaoColunasStatusCota(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the statusCota
	 */
	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	/**
	 * @param statusCota the statusCota to set
	 */
	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

	/**
	 * @return the periodo
	 */
	public PeriodoVO getPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(PeriodoVO periodo) {
		this.periodo = periodo;
	}

	/**
	 * @return the motivoStatusCota
	 */
	public MotivoAlteracaoSituacao getMotivoStatusCota() {
		return motivoStatusCota;
	}

	/**
	 * @param motivoStatusCota the motivoStatusCota to set
	 */
	public void setMotivoStatusCota(MotivoAlteracaoSituacao motivoStatusCota) {
		this.motivoStatusCota = motivoStatusCota;
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
	public OrdenacaoColunasStatusCota getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunasStatusCota ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
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
				+ ((motivoStatusCota == null) ? 0 : motivoStatusCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
		result = prime * result
				+ ((statusCota == null) ? 0 : statusCota.hashCode());
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
		FiltroStatusCotaDTO other = (FiltroStatusCotaDTO) obj;
		if (motivoStatusCota != other.motivoStatusCota)
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		if (statusCota != other.statusCota)
			return false;
		return true;
	}

}
