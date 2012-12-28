package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CalendarioFeriadoDTO implements Serializable, Comparable<CalendarioFeriadoDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1697469832961369870L;

	private Long idFeriado;
	
	@Export(label="Dia", exhibitionOrder=1)
	private Date dataFeriado;
	
	@Export(label="TipoFeriado", exhibitionOrder=2)
	private TipoFeriado tipoFeriado;
	
	@Export(label="Cidade", exhibitionOrder=3)
	private String localidade;
	
	private String ufSigla;

	private String nomeCidade;
	
	private String diaSemana;
	
	private boolean indRepeteAnualmente;

	private boolean indOpera;

	private boolean indEfetuaCobranca;
	
	@Export(label="Descrição", exhibitionOrder=4)
	private String descricaoFeriado;
	
	public Long getIdFeriado() {
		return idFeriado;
	}

	public void setIdFeriado(Long idFeriado) {
		this.idFeriado = idFeriado;
	}

	/**
	 * Obtém diaSemana
	 *
	 * @return String
	 */
	public String getDiaSemana() {
		return diaSemana;
	}

	/**
	 * Atribuí diaSemana
	 * @param diaSemana 
	 */
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * Obtém dataFeriado
	 *
	 * @return Date
	 */
	public Date getDataFeriado() {
		return dataFeriado;
	}

	/**
	 * Atribuí dataFeriado
	 * @param dataFeriado 
	 */
	public void setDataFeriado(Date dataFeriado) {
		this.dataFeriado = dataFeriado;
	}

	/**
	 * Obtém tipoFeriado
	 *
	 * @return TipoFeriado
	 */
	public TipoFeriado getTipoFeriado() {
		return tipoFeriado;
	}

	/**
	 * Atribuí tipoFeriado
	 * @param tipoFeriado 
	 */
	public void setTipoFeriado(TipoFeriado tipoFeriado) {
		this.tipoFeriado = tipoFeriado;
	}

	/**
	 * Obtém idLocalidade
	 *
	 * @return Long
	 */
	public String getLocalidade() {
		return localidade;
	}

	/**
	 * Atribuí idLocalidade
	 * @param localidade 
	 */
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	/**
	 * Obtém ufSigla
	 *
	 * @return String
	 */
	public String getUfSigla() {
		return ufSigla;
	}

	/**
	 * Atribuí ufSigla
	 * @param ufSigla 
	 */
	public void setUfSigla(String ufSigla) {
		this.ufSigla = ufSigla;
	}

	/**
	 * Obtém nomeCidade
	 *
	 * @return String
	 */
	public String getNomeCidade() {
		return nomeCidade;
	}

	/**
	 * Atribuí nomeCidade
	 * @param nomeCidade 
	 */
	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	/**
	 * Obtém indRepeteAnualmente
	 *
	 * @return boolean
	 */
	public boolean isIndRepeteAnualmente() {
		return indRepeteAnualmente;
	}

	/**
	 * Atribuí indRepeteAnualmente
	 * @param indRepeteAnualmente 
	 */
	public void setIndRepeteAnualmente(boolean indRepeteAnualmente) {
		this.indRepeteAnualmente = indRepeteAnualmente;
	}

	/**
	 * Obtém indOpera
	 *
	 * @return boolean
	 */
	public boolean isIndOpera() {
		return indOpera;
	}

	/**
	 * Atribuí indOpera
	 * @param indOpera 
	 */
	public void setIndOpera(boolean indOpera) {
		this.indOpera = indOpera;
	}

	/**
	 * Obtém indEfetuaCobranca
	 *
	 * @return boolean
	 */
	public boolean isIndEfetuaCobranca() {
		return indEfetuaCobranca;
	}

	/**
	 * Atribuí indEfetuaCobranca
	 * @param indEfetuaCobranca 
	 */
	public void setIndEfetuaCobranca(boolean indEfetuaCobranca) {
		this.indEfetuaCobranca = indEfetuaCobranca;
	}

	/**
	 * Obtém descricaoFeriado
	 *
	 * @return String
	 */
	public String getDescricaoFeriado() {
		return descricaoFeriado;
	}

	/**
	 * Atribuí descricaoFeriado
	 * @param descricaoFeriado 
	 */
	public void setDescricaoFeriado(String descricaoFeriado) {
		this.descricaoFeriado = descricaoFeriado;
	}
	
	@Export(label="Opera", exhibitionOrder=5)
	public String getOpera() {
		return (indRepeteAnualmente) ? "Sim":"Não";
	}

	@Export(label="Cobrança", exhibitionOrder=6)
	public String getCobranca() {
		return (indEfetuaCobranca) ? "Sim":"Não";
	}

	@Export(label="Anual", exhibitionOrder=7)
	public String getAnual() {
		return (indRepeteAnualmente) ? "Sim":"Não";
	}

		
	@Override
	public int compareTo(CalendarioFeriadoDTO outroCalendarioFeriadoDTO) {
		if (outroCalendarioFeriadoDTO.getDataFeriado().before(this.getDataFeriado())) {
			return 1;
		} else if (outroCalendarioFeriadoDTO.getDataFeriado().after(this.getDataFeriado())) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
