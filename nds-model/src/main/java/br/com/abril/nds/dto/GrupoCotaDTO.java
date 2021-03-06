package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;

public class GrupoCotaDTO implements Serializable {

	private static final long serialVersionUID = 4640422019892739724L;

	private Long idGrupo;
	private String nome;
	private String recolhimento;
	private String dataInicioVigencia;
	private String dataFimVigencia;
	private TipoGrupo tipoGrupo;
	private TipoCaracteristicaSegmentacaoPDV tipoCota;
	private List<DiaSemana> diasSemana;
	private List<Long> selecionados;
	
	public GrupoCotaDTO() {
		
	}
	
	public GrupoCotaDTO(Long idGrupo, String nome, String recolhimento) {
		super();
		this.idGrupo = idGrupo;
		this.nome = nome;
		this.recolhimento = recolhimento;
	}

	/**
	 * @return the idGrupo
	 */
	public Long getIdGrupo() {
		return idGrupo;
	}
	
	/**
	 * @param idGrupo the idGrupo to set
	 */
	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the recolhimento
	 */
	public String getRecolhimento() {
		return recolhimento;
	}

	/**
	 * @param recolhimento the recolhimento to set
	 */
	public void setRecolhimento(String recolhimento) {
		this.recolhimento = recolhimento;
	}

	/**
	 * @return the tipoGrupo
	 */
	public TipoGrupo getTipoGrupo() {
		return tipoGrupo;
	}

	/**
	 * @param tipoGrupo the tipoGrupo to set
	 */
	public void setTipoGrupo(TipoGrupo tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	/**
	 * @return the tipoCota
	 */
	public TipoCaracteristicaSegmentacaoPDV getTipoCota() {
		return tipoCota;
	}

	/**
	 * @param tipoCota the tipoCota to set
	 */
	public void setTipoCota(TipoCaracteristicaSegmentacaoPDV tipoCota) {
		this.tipoCota = tipoCota;
	}

	/**
	 * @return the diasSemana
	 */
	public List<DiaSemana> getDiasSemana() {
		return diasSemana;
	}

	/**
	 * @param diasSemana the diasSemana to set
	 */
	public void setDiasSemana(List<DiaSemana> diasSemana) {
		this.diasSemana = diasSemana;
	}

	/**
	 * @return the selecionados
	 */
	public List<Long> getSelecionados() {
		return selecionados;
	}

	/**
	 * @param selecionados the selecionados to set
	 */
	public void setSelecionados(List<Long> selecionados) {
		this.selecionados = selecionados;
	}

    
    /**
     * @return the dataInicioVigencia
     */
    public String getDataInicioVigencia() {
        return dataInicioVigencia;
    }

    
    /**
     * @param dataInicioVigencia the dataInicioVigencia to set
     */
    public void setDataInicioVigencia(String dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }

    
    /**
     * @return the dataFimVigencia
     */
    public String getDataFimVigencia() {
        return dataFimVigencia;
    }

    
    /**
     * @param dataFimVigencia the dataFimVigencia to set
     */
    public void setDataFimVigencia(String dataFimVigencia) {
        this.dataFimVigencia = dataFimVigencia;
    }
	
	
}
