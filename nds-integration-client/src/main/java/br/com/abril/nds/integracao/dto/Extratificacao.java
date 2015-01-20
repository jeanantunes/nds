package br.com.abril.nds.integracao.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Extratificacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String tipoDocumento;
	
	private String codigoDistribuidor;
	
	private Date dataCriacao;
	
	private Date dataOperacao;

	private List<ExtratificacaoItem> itens;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public List<ExtratificacaoItem> getItens() {
		return itens;
	}

	public void setItens(List<ExtratificacaoItem> itens) {
		this.itens = itens;
	}

}