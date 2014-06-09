package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroDebitoCreditoDTO implements Serializable {

	private static final long serialVersionUID = -8244798178959257323L;

	private Integer numeroCota;
	
	@Export(label="Nome da Cota")
	private String nomeCota;
	
	private Long idTipoMovimento;
	
	@Export(label="Tipo Movimento")
	private String descricaoTipoMovimento;
	
	@Export(label="Data Lançamento Inicial")
	private Date dataLancamentoInicio; 

	@Export(label="Data Lançamento Final")
	private Date dataLancamentoFim;

	@Export(label="Data Vencimento Inicial")
	private Date dataVencimentoInicio; 

	@Export(label="Data Vencimento Final")
	private Date dataVencimentoFim;

	private List<GrupoMovimentoFinaceiro> grupoMovimentosFinanceirosDebitosCreditos;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacao colunaOrdenacao;
	
	private List<Long> idsTipoMovimentoTaxaEntrega;
	
	/**
	 * @author Discover Technology
	 *
	 */
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
		
		@Override
		public String toString() {
			return this.colunaOrdenacao;
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

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the descricaoTipoMovimento
	 */
	public String getDescricaoTipoMovimento() {
		return descricaoTipoMovimento;
	}

	/**
	 * @param descricaoTipoMovimento the descricaoTipoMovimento to set
	 */
	public void setDescricaoTipoMovimento(String descricaoTipoMovimento) {
		this.descricaoTipoMovimento = descricaoTipoMovimento;
	}

	public List<GrupoMovimentoFinaceiro> getGrupoMovimentosFinanceirosDebitosCreditos() {
		return grupoMovimentosFinanceirosDebitosCreditos;
	}

	public void setGrupoMovimentosFinanceirosDebitosCreditos(
			List<GrupoMovimentoFinaceiro> grupoMovimentosFinanceirosDebitosCreditos) {
		this.grupoMovimentosFinanceirosDebitosCreditos = grupoMovimentosFinanceirosDebitosCreditos;
	}

	public List<Long> getIdsTipoMovimentoTaxaEntrega() {
		return idsTipoMovimentoTaxaEntrega;
	}

	public void setIdsTipoMovimentoTaxaEntrega(
			List<Long> idsTipoMovimentoTaxaEntrega) {
		this.idsTipoMovimentoTaxaEntrega = idsTipoMovimentoTaxaEntrega;
	}
	
	
	
}
