package br.com.abril.nds.dto.auditoria;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Representa a auditoria de dados de uma determinada entidade.
 * 
 * @author Discover Technology
 *
 */
public class AuditoriaDTO implements Serializable {

	private static final long serialVersionUID = -4274656580261284880L;

	@SerializedName("_id")
	private String idAuditoria;

	@SerializedName("_rev")
	private String revisao;

	private String entidadeAuditada;

	private Object usuario;

	private Date dataAuditoria;

	private List<String> ndsStackTrace;

	private String urlAcesso;

	private List<AtributoDTO> dadosAntigos;

	private List<AtributoDTO> dadosNovos;

	private TipoOperacaoAuditoria tipoOperacaoAuditoria;
	
	public enum TipoOperacaoAuditoria {
		
		DELETE,
		UPDATE,
		INSERT
	}
	
	/**
	 * @return the idAuditoria
	 */
	public String getIdAuditoria() {
		return idAuditoria;
	}

	/**
	 * @param idAuditoria the idAuditoria to set
	 */
	public void setIdAuditoria(String idAuditoria) {
		this.idAuditoria = idAuditoria;
	}

	/**
	 * @return the revisao
	 */
	public String getRevisao() {
		return revisao;
	}

	/**
	 * @param revisao the revisao to set
	 */
	public void setRevisao(String revisao) {
		this.revisao = revisao;
	}

	/**
	 * @return the entidadeAuditada
	 */
	public String getEntidadeAuditada() {
		return entidadeAuditada;
	}

	/**
	 * @param entidadeAuditada the entidadeAuditada to set
	 */
	public void setEntidadeAuditada(String entidadeAuditada) {
		this.entidadeAuditada = entidadeAuditada;
	}

	/**
	 * @return the usuario
	 */
	public Object getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Object usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the dataAuditoria
	 */
	public Date getDataAuditoria() {
		return dataAuditoria;
	}

	/**
	 * @param dataAuditoria the dataAuditoria to set
	 */
	public void setDataAuditoria(Date dataAuditoria) {
		this.dataAuditoria = dataAuditoria;
	}

	/**
	 * @return the ndsStackTrace
	 */
	public List<String> getNdsStackTrace() {
		return ndsStackTrace;
	}

	/**
	 * @param ndsStackTrace the ndsStackTrace to set
	 */
	public void setNdsStackTrace(List<String> ndsStackTrace) {
		this.ndsStackTrace = ndsStackTrace;
	}

	/**
	 * @return the urlAcesso
	 */
	public String getUrlAcesso() {
		return urlAcesso;
	}

	/**
	 * @param urlAcesso the urlAcesso to set
	 */
	public void setUrlAcesso(String urlAcesso) {
		this.urlAcesso = urlAcesso;
	}

	/**
	 * @return the dadosAntigos
	 */
	public List<AtributoDTO> getDadosAntigos() {
		return dadosAntigos;
	}

	/**
	 * @param dadosAntigos the dadosAntigos to set
	 */
	public void setDadosAntigos(List<AtributoDTO> dadosAntigos) {
		this.dadosAntigos = dadosAntigos;
	}

	/**
	 * @return the dadosNovos
	 */
	public List<AtributoDTO> getDadosNovos() {
		return dadosNovos;
	}

	/**
	 * @param dadosNovos the dadosNovos to set
	 */
	public void setDadosNovos(List<AtributoDTO> dadosNovos) {
		this.dadosNovos = dadosNovos;
	}

	/**
	 * @return the tipoOperacaoAuditoria
	 */
	public TipoOperacaoAuditoria getTipoOperacaoAuditoria() {
		return tipoOperacaoAuditoria;
	}

	/**
	 * @param tipoOperacaoAuditoria the tipoOperacaoAuditoria to set
	 */
	public void setTipoOperacaoAuditoria(TipoOperacaoAuditoria tipoOperacaoAuditoria) {
		this.tipoOperacaoAuditoria = tipoOperacaoAuditoria;
	}
}
