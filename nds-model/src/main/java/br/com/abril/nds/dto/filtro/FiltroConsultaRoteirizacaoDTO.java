package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroConsultaRoteirizacaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -9092440426651299469L;
	
	private Long idBox;
	
	private Long idRota;
	
	private Long idRoteiro;
	
	private Integer numeroCota;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		BOX("nomeBox"),
		ROTA("descricaoRota"),
		ROTEIRO("descricaoRoteiro"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nome"),
		QNT_COTA("qntCotas");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}
	
	public FiltroConsultaRoteirizacaoDTO() {}

	public FiltroConsultaRoteirizacaoDTO(Long boxId, Long idRoteiro,Long rotaId, Integer numeroCota) {
		
		this.numeroCota = numeroCota;
		this.idBox = boxId;
		this.idRoteiro = idRoteiro;
		this.idRota = rotaId;
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
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
}
