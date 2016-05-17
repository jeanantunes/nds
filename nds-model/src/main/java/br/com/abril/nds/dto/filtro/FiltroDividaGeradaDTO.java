package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroDividaGeradaDTO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Data")
	private Date dataMovimento;
	
	@Export(label = "Box")
	private String codigoBox;
	
	@Export(label = "Rota")
	private String rota;
	
	@Export(label = "Roteiro")
	private String roteiro;
	
	@Export(label = "Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome da Cota")
	private String nomeCota;
	
	@Export(label = "Tipo")
	private TipoCobranca tipoCobranca;
	
	private Long idBox;
	private Long idRota;
	private Long idRoteiro;
	
	private Long idBanco;
	
	private PaginacaoVO paginacao;
	
	private	ColunaOrdenacao colunaOrdenacao;
	
	private List<ColunaOrdenacao> listaColunaOrdenacao;
	
	public FiltroDividaGeradaDTO() {}
	
	public FiltroDividaGeradaDTO(Date dataMovimento, Long idBox , Long idRota, Long idRoteiro,Integer numeroCota,TipoCobranca tipoCobranca ) {
		
		this.dataMovimento = dataMovimento;
		this.numeroCota = numeroCota;
		this.tipoCobranca = tipoCobranca;
		this.idRota = idRota;
		this.idRoteiro = idRoteiro;
		this.idBox = idBox;
	}
	
	public enum ColunaOrdenacao {

		BOX("box"),
		ROTA("rota"),
		ROTEIRO("roteiro"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		DATA_VENCIMENTO("dataVencimento"),
		VIA("vias"),
		DATA_EMISSAO("dataEmissao"),
		VALOR("valor"),
		TIPO_COBRANCA("tipoCobranca"),
		ROTEIRIZACAO("roteirizacao");

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
	 * @return the codigoBox
	 */
	public String getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the rota
	 */
	public String getRota() {
		return rota;
	}

	/**
	 * @param rota the rota to set
	 */
	public void setRota(String rota) {
		this.rota = rota;
	}

	/**
	 * @return the roteiro
	 */
	public String getRoteiro() {
		return roteiro;
	}

	/**
	 * @param roteiro the roteiro to set
	 */
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
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
	 * @return the tipoCobranca
	 */
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
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
	 * @return the idBox
	 */
	public Long getIdBox() {
		return idBox;
	}

	/**
	 * @param idBox the idBox to set
	 */
	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	/**
	 * @return the idRota
	 */
	public Long getIdRota() {
		return idRota;
	}

	/**
	 * @param idRota the idRota to set
	 */
	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	/**
	 * @return the idRoteiro
	 */
	public Long getIdRoteiro() {
		return idRoteiro;
	}

	/**
	 * @param idRoteiro the idRoteiro to set
	 */
	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}
	
	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoBox == null) ? 0 : codigoBox.hashCode());
		result = prime * result
				+ ((colunaOrdenacao == null) ? 0 : colunaOrdenacao.hashCode());
		result = prime * result
				+ ((dataMovimento == null) ? 0 : dataMovimento.hashCode());
		result = prime * result + ((idBox == null) ? 0 : idBox.hashCode());
		result = prime * result + ((idRota == null) ? 0 : idRota.hashCode());
		result = prime * result
				+ ((idRoteiro == null) ? 0 : idRoteiro.hashCode());
		result = prime
				* result
				+ ((listaColunaOrdenacao == null) ? 0 : listaColunaOrdenacao
						.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result + ((rota == null) ? 0 : rota.hashCode());
		result = prime * result + ((roteiro == null) ? 0 : roteiro.hashCode());
		result = prime * result
				+ ((tipoCobranca == null) ? 0 : tipoCobranca.hashCode());
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
		FiltroDividaGeradaDTO other = (FiltroDividaGeradaDTO) obj;
		if (codigoBox == null) {
			if (other.codigoBox != null)
				return false;
		} else if (!codigoBox.equals(other.codigoBox))
			return false;
		if (colunaOrdenacao != other.colunaOrdenacao)
			return false;
		if (dataMovimento == null) {
			if (other.dataMovimento != null)
				return false;
		} else if (!dataMovimento.equals(other.dataMovimento))
			return false;
		if (idBox == null) {
			if (other.idBox != null)
				return false;
		} else if (!idBox.equals(other.idBox))
			return false;
		if (idRota == null) {
			if (other.idRota != null)
				return false;
		} else if (!idRota.equals(other.idRota))
			return false;
		if (idRoteiro == null) {
			if (other.idRoteiro != null)
				return false;
		} else if (!idRoteiro.equals(other.idRoteiro))
			return false;
		if (listaColunaOrdenacao == null) {
			if (other.listaColunaOrdenacao != null)
				return false;
		} else if (!listaColunaOrdenacao.equals(other.listaColunaOrdenacao))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (rota == null) {
			if (other.rota != null)
				return false;
		} else if (!rota.equals(other.rota))
			return false;
		if (roteiro == null) {
			if (other.roteiro != null)
				return false;
		} else if (!roteiro.equals(other.roteiro))
			return false;
		if (tipoCobranca != other.tipoCobranca)
			return false;
		return true;
	}

	
	
}