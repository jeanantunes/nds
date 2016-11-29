package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class PickingLEDFullDTO implements Serializable {

	private static final long serialVersionUID = 5477600335480329122L;
	
	private String identificadorLinha1;
	
	private String codigoCotaLinha1;
	
	private String nomeDistribuidor;
	
	private String nomeCota;
	
	private String codigoBox;
	
	private String tipoOperacao;
	
	private String cpf;
	
	private String cnpj;
	
	private String inscricaoMunicipal;
	
	private String dataLancamento;
	
	private List<DetalhesPickingPorCotaModelo03DTO> listTrailer2;

	private String identificadorLinha3;
	
	private String codigoCotaLinha3;
	
	private String precoTotal;
	
	private String precoTotalDesconto;
	
	private String observacoes01;
	
	private String observacoes02;
	
	private String dataLancamentoLinha3;
	
	private String enderecoLED;
	
	private String codigoDeBarras;
	
	public List<DetalhesPickingPorCotaModelo03DTO> getListTrailer2() {
		return listTrailer2;
	}

	public void setListTrailer2(List<DetalhesPickingPorCotaModelo03DTO> listTrailer2) {
		this.listTrailer2 = listTrailer2;
	}

	public String getIdentificadorLinha1() {
		return identificadorLinha1;
	}

	public void setIdentificadorLinha1(String identificadorLinha1) {
		this.identificadorLinha1 = identificadorLinha1;
	}

	public String getCodigoCotaLinha1() {
		return codigoCotaLinha1;
	}

	public void setCodigoCotaLinha1(String codigoCotaLinha1) {
		this.codigoCotaLinha1 = codigoCotaLinha1;
	}

	public String getNomeDistribuidor() {
		return nomeDistribuidor;
	}

	public void setNomeDistribuidor(String nomeDistribuidor) {
		this.nomeDistribuidor = nomeDistribuidor;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getCodigoBox() {
		return codigoBox;
	}

	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getIdentificadorLinha3() {
		return identificadorLinha3;
	}

	public void setIdentificadorLinha3(String identificadorLinha3) {
		this.identificadorLinha3 = identificadorLinha3;
	}

	public String getCodigoCotaLinha3() {
		return codigoCotaLinha3;
	}

	public void setCodigoCotaLinha3(String codigoCotaLinha3) {
		this.codigoCotaLinha3 = codigoCotaLinha3;
	}

	public String getPrecoTotal() {
		return precoTotal;
	}

	public void setPrecoTotal(String precoTotal) {
		this.precoTotal = precoTotal;
	}

	public String getPrecoTotalDesconto() {
		return precoTotalDesconto;
	}

	public void setPrecoTotalDesconto(String precoTotalDesconto) {
		this.precoTotalDesconto = precoTotalDesconto;
	}

	public String getObservacoes01() {
		return observacoes01;
	}

	public void setObservacoes01(String observacoes01) {
		this.observacoes01 = observacoes01;
	}

	public String getObservacoes02() {
		return observacoes02;
	}

	public void setObservacoes02(String observacoes02) {
		this.observacoes02 = observacoes02;
	}

	public String getDataLancamentoLinha3() {
		return dataLancamentoLinha3;
	}

	public void setDataLancamentoLinha3(String dataLancamentoLinha3) {
		this.dataLancamentoLinha3 = dataLancamentoLinha3;
	}

	public String getEnderecoLED() {
		return enderecoLED;
	}

	public void setEnderecoLED(String enderecoLED) {
		this.enderecoLED = enderecoLED;
	}
	
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
	
}
