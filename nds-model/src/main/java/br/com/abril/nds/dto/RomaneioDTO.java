package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RomaneioDTO implements Serializable {

	private static final long serialVersionUID = -5072658433127977634L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nome;
	
	@Export(label = "Endereço", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String logradouro;
	
	@Export(label = "Bairro", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String bairro;
	
	@Export(label = "Cidade", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String cidade;
	
	@Export(label = "UF", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String uf;
	
	@Export(label = "Telefone", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String numeroTelefone;
	
	public RomaneioDTO() {}
	
	public RomaneioDTO(Integer numeroCota, String nome, String logradouro,
			String bairro, String cidade, String uf, String numeroTelefone) {
		super();
		this.numeroCota = numeroCota;
		this.nome = nome;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.numeroTelefone = numeroTelefone;
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
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}
}
