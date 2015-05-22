package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Extratificacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("_rev")
	private String _rev;
	
	private String tipoDocumento;
	
	private String codigoDistribuidor;
	
	@JsonSerialize
	@JsonDeserialize
	private Date dataCriacao;
	
	@JsonSerialize
	@JsonDeserialize
	private Date dataOperacao;

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
	
	public void setDataOperacao(String dataOperacao) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			this.dataOperacao = sdf.parse(dataOperacao);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}