package br.com.abril.nds.model.ftf;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FTFParameters {

	@Column(name = "CODIGO_ESTABELECIMENTO_EMISSOR")
	private String codigoEstabelecimentoEmissor;
    
	@Column(name = "CNPJ_ESTABELECIMENTO_EMISSOR")
	private String cnpjEstabelecimentoEmissor;
	
	@Column(name = "CODIGO_LOCAL")
    private String codigoLocal;
    
	@Column(name = "CODIGO_CENTRO_EMISSOR")
    private String codigoCentroEmissor;

	public String getCodigoEstabelecimentoEmissor() {
		return codigoEstabelecimentoEmissor;
	}

	public void setCodigoEstabelecimentoEmissor(String codigoEstabelecimentoEmissor) {
		this.codigoEstabelecimentoEmissor = codigoEstabelecimentoEmissor;
	}

	public String getCnpjEstabelecimentoEmissor() {
		return cnpjEstabelecimentoEmissor;
	}

	public void setCnpjEstabelecimentoEmissor(String cnpjEstabelecimentoEmissor) {
		this.cnpjEstabelecimentoEmissor = cnpjEstabelecimentoEmissor;
	}

	public String getCodigoLocal() {
		return codigoLocal;
	}

	public void setCodigoLocal(String codigoLocal) {
		this.codigoLocal = codigoLocal;
	}

	public String getCodigoCentroEmissor() {
		return codigoCentroEmissor;
	}

	public void setCodigoCentroEmissor(String codigoCentroEmissor) {
		this.codigoCentroEmissor = codigoCentroEmissor;
	}
}
