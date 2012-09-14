package br.com.abril.nds.integracao.ems0197.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0197Detalhe {
	
	private String tipoRegistro;
	private String numeroCota;
	private String codProduto;
	private String numEdicao;
	private String nomeProduto;
	private String codigoDeBarrasPE;
	private String precoCustoPE;
	private String precoVendaPE;
	private String descontoPE;
	private String qtdeMEC;
	
	@Field(length = 1000, offset = 1)
	public String getTipoRegistro() {
		this.tipoRegistro="2"+"|"+this.numeroCota
							 +"|"+this.codProduto
							 +"|"+this.numEdicao
							 +"|"+this.nomeProduto
							 +"|"+this.codigoDeBarrasPE
							 +"|"+this.precoCustoPE
							 +"|"+this.precoVendaPE
							 +"|"+this.descontoPE
							 +"|"+this.qtdeMEC;

		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getCodigoCota() {
		return numeroCota;
	}
	public void setCodigoCota(String codigoCota) {
		this.numeroCota = codigoCota;
	}
	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public String getNumEdicao() {
		return numEdicao;
	}
	public void setNumEdicao(String numEdicao) {
		this.numEdicao = numEdicao;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getCodigoDeBarrasPE() {
		return codigoDeBarrasPE;
	}
	public void setCodigoDeBarrasPE(String codigoDeBarrasPE) {
		this.codigoDeBarrasPE = codigoDeBarrasPE;
	}
	public String getPrecoCustoPE() {
		return precoCustoPE;
	}
	public void setPrecoCustoPE(String precoCustoPE) {
		this.precoCustoPE = precoCustoPE;
	}
	public String getPrecoVendaPE() {
		return precoVendaPE;
	}
	public void setPrecoVendaPE(String precoVendaPE) {
		this.precoVendaPE = precoVendaPE;
	}
	public String getDescontoPE() {
		return descontoPE;
	}
	public void setDescontoPE(String descontoPE) {
		this.descontoPE = descontoPE;
	}
	public String getQtdeMEC() {
		return qtdeMEC;
	}
	public void setQtdeMEC(String qtdeMEC) {
		this.qtdeMEC = qtdeMEC;
	}

	
}