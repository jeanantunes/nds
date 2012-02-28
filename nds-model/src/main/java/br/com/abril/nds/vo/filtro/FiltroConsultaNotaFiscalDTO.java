package br.com.abril.nds.vo.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

/**
 * 
 * @author Discover Technology
 *
 */
public class FiltroConsultaNotaFiscalDTO implements Serializable {

	/**
	 * Serial Version UID. 
	 */
	private static final long serialVersionUID = 8502348413078956048L;
	
	private PeriodoVO periodo;
	
	private Long idTipoNotaFiscal;
	
	private Long idFornecedor;

	private Boolean isNotaRecebida;

	private PaginacaoVO paginacao;

	private ColunaOrdenacao colunaOrdenacao;

	public enum ColunaOrdenacao {

		NUMERO_NOTA("numero"),
		DATA_EMISSAO("dataEmissao"),
		DATA_EXPEDICAO("dataExpedicao"),
		TIPO_NOTA("descricao"),
		FORNECEDOR("razaoSocial"),
		NOTA_RECEBIDA("statusNotaFiscal");		

		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
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
	 * @return the idTipoNotaFiscal
	 */
	public Long getIdTipoNotaFiscal() {
		return idTipoNotaFiscal;
	}

	/**
	 * @param idTipoNotaFiscal the idTipoNotaFiscal to set
	 */
	public void setIdTipoNotaFiscal(Long idTipoNotaFiscal) {
		this.idTipoNotaFiscal = idTipoNotaFiscal;
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
	 * @return the isNotaRecebida
	 */
	public Boolean isNotaRecebida() {
		return isNotaRecebida;
	}

	/**
	 * @param isNotaRecebida the isNotaRecebida to set
	 */
	public void setNotaRecebida(Boolean isNotaRecebida) {
		this.isNotaRecebida = isNotaRecebida;
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
