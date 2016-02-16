package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroConsultaRoteirizacaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -9092440426651299469L;
	
	private Long idBox;
	
	@Export(label = "Box", exhibitionOrder = 1)
	private String nomeBox;
	
	private Long idRoteiro;
	
	@Export(label = "Roteiro", exhibitionOrder = 2)
	private String nomeRoteiro;
	
	private Long idRota;
	
	@Export(label = "Rota", exhibitionOrder = 3)
	private String nomeRota;
	
	@Export(label = "Cota", exhibitionOrder = 4)
	private Integer numeroCota;
	
	@Export(label = "Nome", exhibitionOrder = 5)
	private String nomeCota;
	
	private Long idRoteirizacao;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		BOX("nomeBox"),
		ROTA("descricaoRota"),
		ROTEIRO("descricaoRoteiro"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nome"),
		QNT_COTA("qntCotas"),
		ROTEIRIZACAO("roteirizacao");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
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
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
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
	 * @return the nomeRoteiro
	 */
	public String getNomeRoteiro() {
		return nomeRoteiro;
	}

	/**
	 * @param nomeRoteiro the nomeRoteiro to set
	 */
	public void setNomeRoteiro(String nomeRoteiro) {
		this.nomeRoteiro = nomeRoteiro;
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
	 * @return the nomeRota
	 */
	public String getNomeRota() {
		return nomeRota;
	}

	/**
	 * @param nomeRota the nomeRota to set
	 */
	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
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
     * @return the idRoteirizacao
     */
    public Long getIdRoteirizacao() {
        return idRoteirizacao;
    }

    /**
     * @param idRoteirizacao the idRoteirizacao to set
     */
    public void setIdRoteirizacao(Long idRoteirizacao) {
        this.idRoteirizacao = idRoteirizacao;
    }
	
}
