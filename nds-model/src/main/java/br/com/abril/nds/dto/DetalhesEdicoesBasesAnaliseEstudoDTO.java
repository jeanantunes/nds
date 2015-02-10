package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;

public class DetalhesEdicoesBasesAnaliseEstudoDTO implements Serializable {

	private static final long serialVersionUID = 4069202676819644871L;
	
	private BigInteger reparte;
	
	private BigInteger venda;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private String dataLancamento;
	
	private String ordemExibicao;

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarDataPTBR(dataLancamento);
	}

	public String getOrdemExibicao() {
		return ordemExibicao;
	}

	public void setOrdemExibicao(String ordemExibicao) {
		this.ordemExibicao = ordemExibicao;
	}

}
