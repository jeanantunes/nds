package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaNFEEncalheTratamentoDTO implements Serializable {

	private static final long serialVersionUID = 8366815250237375585L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nome;
	
	@Export(label = "NF- e", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroNfe;
	
	@Export(label = "Serie", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String serie;
	
	@Export(label = "Chave Acesso", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String chaveAcesso;
	
	public ConsultaNFEEncalheTratamentoDTO() {}
	
	public ConsultaNFEEncalheTratamentoDTO(Integer numeroCota, String nome,
			Long numeroNfe, String serie, String chaveAcesso) {
		super();
		this.numeroCota = numeroCota;
		this.nome = nome;
		this.numeroNfe = numeroNfe;
		this.serie = serie;
		this.chaveAcesso = chaveAcesso;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getNumeroNfe() {
		return numeroNfe;
	}

	public void setNumeroNfe(Long numeroNfe) {
		this.numeroNfe = numeroNfe;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	

}
