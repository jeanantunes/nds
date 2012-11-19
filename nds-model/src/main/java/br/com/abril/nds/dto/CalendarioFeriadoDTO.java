package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoFeriado;

public class CalendarioFeriadoDTO implements Serializable, Comparable<CalendarioFeriadoDTO> {

	private Date dataFeriado;
	
	private TipoFeriado tipoFeriado;
	
	private String localidade;
	
	private String ufSigla;
	
	private String nomeCidade;
	
	private String diaSemana;
	
	private boolean indRepeteAnualmente;
	
	private boolean indOpera;
	
	private boolean indEfetuaCobranca;
	
	private String descricaoFeriado;
	
	
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
