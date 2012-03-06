package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

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

	private String cnpj;
	
	private String numeroNota;
	
	private String serie;
	
	private String chave;
	
	private Boolean isNotaRecebida;

	private PaginacaoVO paginacao;

	private List<ColunaOrdenacao> listaColunaOrdenacao;

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
	public Boolean getIsNotaRecebida() {
		return isNotaRecebida;
	}

	/**
	 * @param isNotaRecebida the isNotaRecebida to set
	 */
	public void setIsNotaRecebida(Boolean isNotaRecebida) {
		this.isNotaRecebida = isNotaRecebida;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((listaColunaOrdenacao == null) ? 0 : listaColunaOrdenacao.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime
				* result
				+ ((idTipoNotaFiscal == null) ? 0 : idTipoNotaFiscal.hashCode());
		result = prime * result
				+ ((isNotaRecebida == null) ? 0 : isNotaRecebida.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
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
		FiltroConsultaNotaFiscalDTO other = (FiltroConsultaNotaFiscalDTO) obj;
		if (listaColunaOrdenacao == null) {
			if (other.listaColunaOrdenacao != null)
				return false;
		} else if (!listaColunaOrdenacao.equals(other.listaColunaOrdenacao))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (idTipoNotaFiscal == null) {
			if (other.idTipoNotaFiscal != null)
				return false;
		} else if (!idTipoNotaFiscal.equals(other.idTipoNotaFiscal))
			return false;
		if (isNotaRecebida == null) {
			if (other.isNotaRecebida != null)
				return false;
		} else if (!isNotaRecebida.equals(other.isNotaRecebida))
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
		return true;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	/**
	 * @return the listaColunaOrdenacao
	 */
	public List<ColunaOrdenacao> getListaColunaOrdenacao() {
		return listaColunaOrdenacao;
	}

	/**
	 * @param listaColunaOrdenacao the listaColunaOrdenacao to set
	 */
	public void setListaColunaOrdenacao(List<ColunaOrdenacao> listaColunaOrdenacao) {
		this.listaColunaOrdenacao = listaColunaOrdenacao;
	}
}
