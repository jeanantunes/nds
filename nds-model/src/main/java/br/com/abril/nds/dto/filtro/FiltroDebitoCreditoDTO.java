package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroDebitoCreditoDTO implements Serializable {

	private static final long serialVersionUID = -8244798178959257323L;

	private Integer numeroCota;
	
	private Long idTipoMovimento;
	
	private Date dataLancamentoInicio; 
	
	private Date dataLancamentoFim;
	
	private Date dataVencimentoInicio; 

	private Date dataVencimentoFim;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacao colunaOrdenacao;
	
	public enum ColunaOrdenacao {
		
		DATA_LANCAMENTO("dataLancamento"),
		DATA_VENCIMENTO("dataVencimento"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		TIPO_LANCAMENTO("tipoLancamento"),
		VALOR("valor"),
		OBSERVACAO("observacao");
		
		private String colunaOrdenacao;
		
		private ColunaOrdenacao(String colunaOrdenacao) {
			
			this.colunaOrdenacao = colunaOrdenacao;
		}

		/**
		 * @return the colunaOrdenacao
		 */
		public String getColunaOrdenacao() {
			return colunaOrdenacao;
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
	 * @return the dataLancamentoInicio
	 */
	public Date getDataLancamentoInicio() {
		return dataLancamentoInicio;
	}

	/**
	 * @param dataLancamentoInicio the dataLancamentoInicio to set
	 */
	public void setDataLancamentoInicio(Date dataLancamentoInicio) {
		this.dataLancamentoInicio = dataLancamentoInicio;
	}

	/**
	 * @return the dataLancamentoFim
	 */
	public Date getDataLancamentoFim() {
		return dataLancamentoFim;
	}

	/**
	 * @param dataLancamentoFim the dataLancamentoFim to set
	 */
	public void setDataLancamentoFim(Date dataLancamentoFim) {
		this.dataLancamentoFim = dataLancamentoFim;
	}

	/**
	 * @return the dataVencimentoInicio
	 */
	public Date getDataVencimentoInicio() {
		return dataVencimentoInicio;
	}

	/**
	 * @param dataVencimentoInicio the dataVencimentoInicio to set
	 */
	public void setDataVencimentoInicio(Date dataVencimentoInicio) {
		this.dataVencimentoInicio = dataVencimentoInicio;
	}

	/**
	 * @return the dataVencimentoFim
	 */
	public Date getDataVencimentoFim() {
		return dataVencimentoFim;
	}

	/**
	 * @param dataVencimentoFim the dataVencimentoFim to set
	 */
	public void setDataVencimentoFim(Date dataVencimentoFim) {
		this.dataVencimentoFim = dataVencimentoFim;
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
}
