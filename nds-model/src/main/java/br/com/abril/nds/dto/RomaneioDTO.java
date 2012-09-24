package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

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
	
	private String cota;
	
	private Long idCota;
	
	private Long idRota;
	
	private Long qtdCotas;
	
	private BigInteger pacote;
	
	private BigInteger quebra;
	
	private BigInteger reparteTotal;
	
	private BigInteger qtdProduto0;
	
	private BigInteger qtdProduto1;
	
	private BigInteger qtdProduto2;
	
	private BigInteger qtdProduto3;
	
	private BigInteger qtdProduto4;
	
	private BigInteger qtdProduto5;
	
	private Long numeroNotaEnvio;
	
	public RomaneioDTO() {}
	
	public RomaneioDTO(Integer numeroCota, String nome, String logradouro,
			String bairro, String cidade, String uf, String numeroTelefone, Long idCota) {
		super();
		this.numeroCota = numeroCota;
		this.nome = nome;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.numeroTelefone = numeroTelefone;
		this.idCota = idCota;
	}
	
	public RomaneioDTO(String logradouro, String bairro, String cidade, String uf) {
		super();
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
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

	public String getCota() {
		return cota;
	}

	public void setCota(String cota) {
		this.cota = cota;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	public Long getQtdCotas() {
		return qtdCotas;
	}

	public void setQtdCotas(Long qtdCotas) {
		this.qtdCotas = qtdCotas;
	}

	public BigInteger getPacote() {
		return pacote;
	}

	public void setPacote(BigInteger pacote) {
		this.pacote = pacote;
	}

	public BigInteger getQuebra() {
		return quebra;
	}

	public void setQuebra(BigInteger quebra) {
		this.quebra = quebra;
	}

	public BigInteger getReparteTotal() {
		return reparteTotal;
	}

	public void setReparteTotal(BigInteger reparteTotal) {
		this.reparteTotal = reparteTotal;
	}

	public BigInteger getQtdProduto1() {
		return qtdProduto1;
	}

	public void setQtdProduto1(BigInteger qtdProduto1) {
		this.qtdProduto1 = qtdProduto1;
	}

	public BigInteger getQtdProduto2() {
		return qtdProduto2;
	}

	public void setQtdProduto2(BigInteger qtdProduto2) {
		this.qtdProduto2 = qtdProduto2;
	}

	public BigInteger getQtdProduto3() {
		return qtdProduto3;
	}

	public void setQtdProduto3(BigInteger qtdProduto3) {
		this.qtdProduto3 = qtdProduto3;
	}

	public BigInteger getQtdProduto4() {
		return qtdProduto4;
	}

	public void setQtdProduto4(BigInteger qtdProduto4) {
		this.qtdProduto4 = qtdProduto4;
	}

	public BigInteger getQtdProduto5() {
		return qtdProduto5;
	}

	public void setQtdProduto5(BigInteger qtdProduto5) {
		this.qtdProduto5 = qtdProduto5;
	}

	public BigInteger getQtdProduto0() {
		return qtdProduto0;
	}

	public void setQtdProduto0(BigInteger qtdProduto0) {
		this.qtdProduto0 = qtdProduto0;
	}

	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}
}