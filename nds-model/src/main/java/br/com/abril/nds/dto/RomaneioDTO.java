package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;

@Exportable
public class RomaneioDTO implements Serializable {


	/** */
	private static final long serialVersionUID = 6721772921124214158L;
	
	@Export(label = "Cota", alignment = Alignment.LEFT, exhibitionOrder = 2, widthPercent = 15)
	@Footer(label = "Total de Entregas", type = FooterType.COUNT, alignWithHeader="Endereço")
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.LEFT, exhibitionOrder = 3, widthPercent = 25, xlsAutoSize = true)
	private String nome;

	@Export(label = "Endereço", alignment = Alignment.LEFT, exhibitionOrder = 4, widthPercent = 45, xlsAutoSize = true)
	private String endereco;
	
	private String numeroTelefone;
	
	private String cota;
	
	private Long idCota;
	
	private Long idBox;
	private Long codigoBox;
	private String nomeBox;
	
	private Long idRoteiro;
	private String nomeRoteiro;
	
	private Long idRota;
	private String nomeRota;
	
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
	
	@Export(label = "Nº. NE", alignment = Alignment.LEFT, exhibitionOrder = 1, widthPercent = 15)
	private Long numeroNotaEnvio;
	
	private String cep;
	
	private Long idPDV;
	
	public RomaneioDTO() {}
	
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
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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

	public void setPacote(Number pacote) {
		this.pacote = (pacote == null 
				? BigInteger.ZERO
				: BigInteger.valueOf(pacote.longValue()));
	}

	public BigInteger getQuebra() {
		return quebra;
	}

	public void setQuebra(Number quebra) {
		this.quebra = (quebra == null 
				? BigInteger.ZERO
				: BigInteger.valueOf(quebra.longValue()));
	}

	public BigInteger getReparteTotal() {
		return reparteTotal;
	}

	public void setReparteTotal(Number reparteTotal) {
		this.reparteTotal = (reparteTotal == null
				? BigInteger.ZERO
				: BigInteger.valueOf(reparteTotal.longValue()));
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

	public Long getCodigoBox() {
		return codigoBox;
	}

	public void setCodigoBox(Long codigoBox) {
		this.codigoBox = codigoBox;
	}

	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	public String getNomeRoteiro() {
		return nomeRoteiro;
	}

	public void setNomeRoteiro(String nomeRoteiro) {
		this.nomeRoteiro = nomeRoteiro;
	}

	public String getNomeRota() {
		return nomeRota;
	}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}

	public Long getIdBox() {
		return idBox;
	}

	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	public Long getIdRoteiro() {
		return idRoteiro;
	}

	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = Util.adicionarMascaraCEP(cep);
	}

	public Long getIdPDV() {
		return idPDV;
	}

	public void setIdPDV(Long idPDV) {
		this.idPDV = idPDV;
	}
	
	
	
}