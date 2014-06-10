package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoFeriado;

public class FiltroCadastroCalendarioDTO extends FiltroDTO implements Serializable {

	private Date data;
	private String descricao;
	private String ufSigla;
	private Long idLocalidade;
	private TipoFeriado tipoFeriado;
	private boolean indRepeteAnualmente;
	private boolean indOpera;
	private boolean indEfetuaCobranca;
	/**
	 * Obtém data
	 *
	 * @return Date
	 */
	public Date getData() {
		return data;
	}
	/**
	 * Atribuí data
	 * @param data 
	 */
	public void setData(Date data) {
		this.data = data;
	}
	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * Atribuí descricao
	 * @param descricao 
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	/**
	 * Obtém unidadeFederacao
	 *
	 * @return UnidadeFederacao
	 */
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
	 * Obtém idLocalidade
	 *
	 * @return Long
	 */
	public Long getIdLocalidade() {
		return idLocalidade;
	}
	/**
	 * Atribuí idLocalidade
	 * @param idLocalidade 
	 */
	public void setIdLocalidade(Long idLocalidade) {
		this.idLocalidade = idLocalidade;
	}
	
	
}
