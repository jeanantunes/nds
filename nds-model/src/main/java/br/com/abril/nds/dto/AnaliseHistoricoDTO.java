package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseHistoricoDTO {

	private Integer numeroCota = 0;
	private SituacaoCadastro statusCota;
	private String statusCotaFormatado = "";
	private String nomePessoa = "";
	private Long qtdPdv = 0l;
	private Double reparteMedio;
	private Double vendaMedia;
	//private List<ProdutoEdicaoDTO> edicoes = new ArrayList<>();

	// Edição 1
	private String ed1Reparte = "0";
	private String ed1Venda = "0";

	// Edição 2
	private String ed2Reparte = "0";
	private String ed2Venda = "0";

	// Edição 3
	private String ed3Reparte = "0";
	private String ed3Venda = "0";

	// Edição 4
	private String ed4Reparte = "0";
	private String ed4Venda = "0";

	// Edição 5
	private String ed5Reparte = "0";
	private String ed5Venda = "0";

	// Edição 6
	private String ed6Reparte = "0";
	private String ed6Venda = "0";

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

	public String getStatusCotaFormatado() {
		return statusCotaFormatado;
	}

	public void setStatusCotaFormatado(String statusCotaFormatado) {
		this.statusCotaFormatado = statusCotaFormatado;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public Long getQtdPdv() {
		return qtdPdv;
	}

	public void setQtdPdv(Long qtdPdv) {
		this.qtdPdv = qtdPdv;
	}

	public Double getReparteMedio() {
		return reparteMedio;
	}

	public void setReparteMedio(Double reparteMedio) {
		this.reparteMedio = reparteMedio;
	}

	public Double getVendaMedia() {
		return vendaMedia;
	}

	public void setVendaMedia(Double vendaMedia) {
		this.vendaMedia = vendaMedia;
	}

	public String getEd1Reparte() {
		return ed1Reparte;
	}

	public String getEd1Venda() {
		return ed1Venda;
	}

	public String getEd2Reparte() {
		return ed2Reparte;
	}

	public String getEd2Venda() {
		return ed2Venda;
	}

	public String getEd3Reparte() {
		return ed3Reparte;
	}

	public String getEd3Venda() {
		return ed3Venda;
	}

	public String getEd4Reparte() {
		return ed4Reparte;
	}

	public String getEd4Venda() {
		return ed4Venda;
	}

	public String getEd5Reparte() {
		return ed5Reparte;
	}

	public String getEd5Venda() {
		return ed5Venda;
	}

	public String getEd6Reparte() {
		return ed6Reparte;
	}

	public String getEd6Venda() {
		return ed6Venda;
	}

//	public List<ProdutoEdicaoDTO> getEdicoes() {
//		return edicoes;
//	}
//
//	public void setEdicoes(List<ProdutoEdicaoDTO> edicoes) {
//		this.edicoes = edicoes;
//	}

	public void setEd1Reparte(String ed1Reparte) {
		this.ed1Reparte = ed1Reparte;
	}

	public void setEd1Venda(String ed1Venda) {
		this.ed1Venda = ed1Venda;
	}

	public void setEd2Reparte(String ed2Reparte) {
		this.ed2Reparte = ed2Reparte;
	}

	public void setEd2Venda(String ed2Venda) {
		this.ed2Venda = ed2Venda;
	}

	public void setEd3Reparte(String ed3Reparte) {
		this.ed3Reparte = ed3Reparte;
	}

	public void setEd3Venda(String ed3Venda) {
		this.ed3Venda = ed3Venda;
	}

	public void setEd4Reparte(String ed4Reparte) {
		this.ed4Reparte = ed4Reparte;
	}

	public void setEd4Venda(String ed4Venda) {
		this.ed4Venda = ed4Venda;
	}

	public void setEd5Reparte(String ed5Reparte) {
		this.ed5Reparte = ed5Reparte;
	}

	public void setEd5Venda(String ed5Venda) {
		this.ed5Venda = ed5Venda;
	}

	public void setEd6Reparte(String ed6Reparte) {
		this.ed6Reparte = ed6Reparte;
	}

	public void setEd6Venda(String ed6Venda) {
		this.ed6Venda = ed6Venda;
	}
	
	

}
