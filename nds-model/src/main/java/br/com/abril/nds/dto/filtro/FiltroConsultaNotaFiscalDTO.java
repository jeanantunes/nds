package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

/**
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroConsultaNotaFiscalDTO implements Serializable {

	/**
	 * Serial Version UID. 
	 */
	private static final long serialVersionUID = 8502348413078956048L;
	
	private PeriodoVO periodo;
	
	private Long idTipoNotaFiscal;
	
//	@Export(label = "Tipo de Nota", exhibitionOrder = 4)
	private String tipoNotaFiscal;
	
	private Long idFornecedor;
	
	@Export(label = "Fornecedor", exhibitionOrder = 1)
	private String nomeFornecedor;

	private String cnpj;
	
	private Long numeroNota;
	
	private Long serie;
	
	private String chave;
	
	private Long idDistribuidor;
	
	
	private Long numeroNotaEnvio;

	private PaginacaoVO paginacao;
	
	private NotaRecebidaEnum notaRecebida;

	private List<ColunaOrdenacao> listaColunaOrdenacao;
	
	public enum NotaRecebidaEnum {
		
		TODAS,
		SOMENTE_NOTAS_RECEBIDAS,
		SOMENTE_NOTAS_NAO_RECEBIDAS,
		NOTAS_NAO_RECEBIDAS_COM_NOTA_DE_ENVIO

	}

	public enum ColunaOrdenacao {

		NUMERO_NOTA("numero"),
		SERIE("serie"),
		NUMERO_NOTA_ENVIO("numeroNotaEnvio"),
		DATA_EMISSAO("dataEmissao"),
		DATA_EXPEDICAO("dataExpedicao"),
		TIPO_NOTA("descricao"),
		FORNECEDOR("razaoSocial"),
		VALOR("valorTotalNota"),
		VALOR_COM_DESCONTO("valorTotalNotaComDesconto"),
		NOTA_RECEBIDA("notaRecebida"), 
		CHAVE_ACESSO("chaveAcesso");		

		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}
	
	
	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
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
	 * @return the notaRecebida
	 */
	public NotaRecebidaEnum getNotaRecebida() {
		return notaRecebida;
	}

	/**
	 * @param notaRecebida the notaRecebida to set
	 */
	public void setNotaRecebida(NotaRecebidaEnum notaRecebida) {
		this.notaRecebida = notaRecebida;
	}

	@Export(label = "Data Inicial", exhibitionOrder = 2)
	public String getDataInicial() {
		
		if (this.periodo == null
				|| this.periodo.getDataInicial() == null) {
			
			return "";
		}
		
		return DateUtil.formatarDataPTBR(this.periodo.getDataInicial());
	}
	
	@Export(label = "Data Final", exhibitionOrder = 3)
	public String getDataFinal() {
		
		if (this.periodo == null
				|| this.periodo.getDataFinal() == null) {
			
			return "";
		}
		
		return DateUtil.formatarDataPTBR(this.periodo.getDataFinal());
	}
	
	@Export(label = "Nota Recebida", exhibitionOrder = 5)
	public String getDescricaoNotaRecebida() {
		
		if (this.notaRecebida == null) {
			
			return "";
		}
		
		switch(this.notaRecebida) {
		
		case SOMENTE_NOTAS_RECEBIDAS:
			return "Sim";
			
		case SOMENTE_NOTAS_NAO_RECEBIDAS:
			return "Não";
			
		case NOTAS_NAO_RECEBIDAS_COM_NOTA_DE_ENVIO:
			return "Nota de envio.";
			
		default:
			return "";
			
		}
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
				+ ((notaRecebida == null) ? 0 : notaRecebida.hashCode());
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
		if (notaRecebida == null) {
			if (other.notaRecebida != null)
				return false;
		} else if (!notaRecebida.equals(other.notaRecebida))
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

	public Long getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Long getSerie() {
		return serie;
	}

	public void setSerie(Long serie) {
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

	/**
	 * @return the tipoNotaFiscal
	 */
	public String getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(String tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the idDistribuidor
	 */
	public Long getIdDistribuidor() {
		return idDistribuidor;
	}

	/**
	 * @param idDistribuidor the idDistribuidor to set
	 */
	public void setIdDistribuidor(Long idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}
}
