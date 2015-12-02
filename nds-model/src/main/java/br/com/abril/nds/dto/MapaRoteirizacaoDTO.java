package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class MapaRoteirizacaoDTO  implements Serializable{

	private static final long serialVersionUID = -3737578545975332770L;

	@Export(label = "Box", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String nomeBox;
	
	@Export(label = "Roteiro", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String descricaoRoteiro;
	
	@Export(label = "Rota", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String descricaoRota;
	
	@Export(label = "Cota", alignment = Alignment.LEFT, exhibitionOrder = 4)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.LEFT, exhibitionOrder = 5)
	private String nome;
	
	private Long qntCotas;

	private Long idBox;
	
	private Long idRoteiro;
	
	private Long idRota;
	
	private Long idCota;
	
	private Long idRoteirizacao;
	
	private TipoBox tipobox;
	
	private List<ConsultaRoteirizacaoDTO> itens;
	
	public MapaRoteirizacaoDTO() {}

	/**
	 * Obtém nomeBox
	 *
	 * @return String
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * Atribuí nomeBox
	 * @param nomeBox 
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * Obtém descricaoRoteiro
	 *
	 * @return String
	 */
	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}

	/**
	 * Atribuí descricaoRoteiro
	 * @param descricaoRoteiro 
	 */
	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}

	/**
	 * Obtém descricaoRota
	 *
	 * @return String
	 */
	public String getDescricaoRota() {
		return descricaoRota;
	}

	/**
	 * Atribuí descricaoRota
	 * @param descricaoRota 
	 */
	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	/**
	 * Obtém numeroCota
	 *
	 * @return Integer
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * Atribuí numeroCota
	 * @param numeroCota 
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * Obtém nome
	 *
	 * @return String
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Atribuí nome
	 * @param nome 
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Obtém qntCotas
	 *
	 * @return Long
	 */
	public Long getQntCotas() {
		return qntCotas;
	}

	/**
	 * Atribuí qntCotas
	 * @param qntCotas 
	 */
	public void setQntCotas(Long qntCotas) {
		this.qntCotas = qntCotas;
	}

	/**
	 * Obtém idBox
	 *
	 * @return Long
	 */
	public Long getIdBox() {
		return idBox;
	}

	/**
	 * Atribuí idBox
	 * @param idBox 
	 */
	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	/**
	 * Obtém idRoteiro
	 *
	 * @return Long
	 */
	public Long getIdRoteiro() {
		return idRoteiro;
	}

	/**
	 * Atribuí idRoteiro
	 * @param idRoteiro 
	 */
	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	/**
	 * Obtém idRota
	 *
	 * @return Long
	 */
	public Long getIdRota() {
		return idRota;
	}

	/**
	 * Atribuí idRota
	 * @param idRota 
	 */
	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	/**
	 * Obtém idCota
	 *
	 * @return Long
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * Atribuí idCota
	 * @param idCota 
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
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

	public TipoBox getTipobox() {
		return tipobox;
	}

	public void setTipobox(TipoBox tipobox) {
		this.tipobox = tipobox;
	}

	public List<ConsultaRoteirizacaoDTO> getItens() {
		return itens;
	}

	public void setItens(List<ConsultaRoteirizacaoDTO> itens) {
		this.itens = itens;
	}
}
