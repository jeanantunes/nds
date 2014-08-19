package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BonificacaoJsonDTO implements Serializable {

	private static final long serialVersionUID = 443452358594305331L;
	@JsonProperty("bonificacoes")
	private List<BonificacaoDTO> bonificacoes;

	public List<BonificacaoDTO> getBonificacoes() {
		return bonificacoes;
	}

	public void setBonificacoes(List<BonificacaoDTO> bonificacoesJson) {
		this.bonificacoes = bonificacoesJson;
	}
}
