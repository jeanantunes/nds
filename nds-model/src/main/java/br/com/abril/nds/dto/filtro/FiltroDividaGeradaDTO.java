package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroDividaGeradaDTO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Date dataMovimento;
	
	private String codigoBox;
	
	private String rota;
	
	private String roteiro;
	
	private Integer numeroCota;
	
	private TipoCobranca tipoCobranca;
	
	private PaginacaoVO paginacao;
	
	private	ColunaOrdenacao colunaOrdenacao;
	
	private List<ColunaOrdenacao> listaColunaOrdenacao;
	
	public FiltroDividaGeradaDTO() {}
	
	public FiltroDividaGeradaDTO(Date dataMovimento, String codigoBox , String rota, String roteiro,Integer numeroCota,TipoCobranca tipoCobranca ) {
		
		this.dataMovimento = dataMovimento;
		this.codigoBox = codigoBox;
		this.rota = rota;
		this.roteiro = roteiro;
		this.numeroCota = numeroCota;
		this.tipoCobranca = tipoCobranca;
	}
	
	public enum ColunaOrdenacao {

		BOX("box"),
		ROTA("rota"),
		ROTEIRO("roteiro"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		DATA_VENCIMENTO("dataVencimento"),
		VIA("via"),
		DATA_EMISSAO("dataEmissao"),
		VALOR("valor"),
		TIPO_COBRANCA("tipoCobranca");

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
	
	
	
	
}
