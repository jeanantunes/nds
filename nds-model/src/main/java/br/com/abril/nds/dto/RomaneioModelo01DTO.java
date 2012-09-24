package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RomaneioModelo01DTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8142125302068788378L;

	private String roteiro;
	
	private Long entregaBox;
	
	private String rota;
	
	private Date dataGeracao;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private BigDecimal reparteTotal;
	
	private List<RomaneioDTO> itens;
	
	private String nomeProduto0;
	
	private String nomeProduto1;
	
	private String nomeProduto2;
	
	private String nomeProduto3;
	
	private String nomeProduto4;
	
	private String nomeProduto5;

	public String getRoteiro() {
		return roteiro;
	}

	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}

	public Long getEntregaBox() {
		return entregaBox;
	}

	public void setEntregaBox(Long entregaBox) {
		this.entregaBox = entregaBox;
	}

	public String getRota() {
		return rota;
	}

	public void setRota(String rota) {
		this.rota = rota;
	}

	public Date getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public List<RomaneioDTO> getItens() {
		return itens;
	}

	public void setItens(List<RomaneioDTO> itens) {
		this.itens = itens;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public BigDecimal getReparteTotal() {
		return reparteTotal;
	}

	public void setReparteTotal(BigDecimal reparteTotal) {
		this.reparteTotal = reparteTotal;
	}

	public String getNomeProduto0() {
		return nomeProduto0;
	}

	public void setNomeProduto0(String nomeProduto0) {
		this.nomeProduto0 = nomeProduto0;
	}

	public String getNomeProduto1() {
		return nomeProduto1;
	}

	public void setNomeProduto1(String nomeProduto1) {
		this.nomeProduto1 = nomeProduto1;
	}

	public String getNomeProduto2() {
		return nomeProduto2;
	}

	public void setNomeProduto2(String nomeProduto2) {
		this.nomeProduto2 = nomeProduto2;
	}

	public String getNomeProduto3() {
		return nomeProduto3;
	}

	public void setNomeProduto3(String nomeProduto3) {
		this.nomeProduto3 = nomeProduto3;
	}

	public String getNomeProduto4() {
		return nomeProduto4;
	}

	public void setNomeProduto4(String nomeProduto4) {
		this.nomeProduto4 = nomeProduto4;
	}

	public String getNomeProduto5() {
		return nomeProduto5;
	}

	public void setNomeProduto5(String nomeProduto5) {
		this.nomeProduto5 = nomeProduto5;
	}
}