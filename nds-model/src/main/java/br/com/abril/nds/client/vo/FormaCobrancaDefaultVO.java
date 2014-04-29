package br.com.abril.nds.client.vo;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class FormaCobrancaDefaultVO {
	
	private Long idBanco;
	
	private String nomeBanco;
	
	private String descricaoTipoCobranca;
	
	private TipoCobranca tipoCobranca;

	public FormaCobrancaDefaultVO() { }

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getDescricaoTipoCobranca() {
		return descricaoTipoCobranca;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
		this.descricaoTipoCobranca = tipoCobranca.getDescricao();
	}
}
