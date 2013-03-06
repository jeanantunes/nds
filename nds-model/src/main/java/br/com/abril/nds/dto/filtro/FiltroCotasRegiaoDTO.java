package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCotasRegiaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 2433782322485644813L;
	
	private Long id;
	
	private String nomeCota;
	
	private String cepInicial;
	
	private String cepFinal;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCepInicial() {
		return cepInicial;
	}

	public void setCepInicial(String cepInicial) {
		this.cepInicial = cepInicial;
	}

	public String getCepFinal() {
		return cepFinal;
	}

	public void setCepFinal(String cepFinal) {
		this.cepFinal = cepFinal;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
}
