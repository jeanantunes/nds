package br.com.abril.nds.integracao.model.canonic;

import java.util.Date;

public class IntegracaoDocument {

	private String _id;
	private String _rev;
	private String tipoDocumento;
	private String subTipoDocumento;
	private String nomeArquivo;
	private Integer linhaArquivo;
	private Date dataHoraExtracao;
	private String nomeUsuarioExtracao;
	private String erro;
	private Date horaDeCriacao = new Date();
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_rev() {
		return _rev;
	}
	public void set_rev(String _rev) {
		this._rev = _rev;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getSubTipoDocumento() {
		return subTipoDocumento;
	}
	public void setSubTipoDocumento(String subTipoDocumento) {
		this.subTipoDocumento = subTipoDocumento;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public Integer getLinhaArquivo() {
		return linhaArquivo;
	}
	public void setLinhaArquivo(Integer linhaArquivo) {
		this.linhaArquivo = linhaArquivo;
	}
	public Date getDataHoraExtracao() {
		return dataHoraExtracao;
	}
	public void setDataHoraExtracao(Date dataHoraExtracao) {
		this.dataHoraExtracao = dataHoraExtracao;
	}
	public String getNomeUsuarioExtracao() {
		return nomeUsuarioExtracao;
	}
	public void setNomeUsuarioExtracao(String nomeUsuarioExtracao) {
		this.nomeUsuarioExtracao = nomeUsuarioExtracao;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
	/**
	 * @return the horaDeCriacao
	 */
	public Date getHoraDeCriacao() {
		return horaDeCriacao;
	}
	/**
	 * @param horaDeCriacao the horaDeCriacao to set
	 */
	public void setHoraDeCriacao(Date horaDeCriacao) {
		this.horaDeCriacao = horaDeCriacao;
	}
}
