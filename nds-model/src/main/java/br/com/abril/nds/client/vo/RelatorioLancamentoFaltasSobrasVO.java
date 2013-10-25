package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.ImpressaoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.RateioDiferencaDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.util.CurrencyUtil;

public class RelatorioLancamentoFaltasSobrasVO implements Serializable  {

	private static final long serialVersionUID = 5342968572475646951L;
	
	private String 	codigoProduto;
	private String 	descricaoProduto;
	private String 	numeroEdicao;
	private String 	precoCapa;
	private String 	qtdeFaltas = "";
	private String 	qtdeSobras = "";
	private List<RateioDiferencaDTO> rateios;
	
	public RelatorioLancamentoFaltasSobrasVO(ImpressaoDiferencaEstoqueDTO impressaoDiferencaEstoqueDTO) {
		
		ProdutoEdicao produtoEdicao = impressaoDiferencaEstoqueDTO.getProdutoEdicao();
		
		if (produtoEdicao != null){
			Produto produto = produtoEdicao.getProduto();
			
			this.codigoProduto = produto.getCodigo();
			this.descricaoProduto = produto.getNomeComercial();;
			this.numeroEdicao = produtoEdicao.getNumeroEdicao().toString();
			this.precoCapa = CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda());
			
			if (impressaoDiferencaEstoqueDTO.getQtdeFaltas() != null
					&& BigInteger.ZERO.compareTo(impressaoDiferencaEstoqueDTO.getQtdeFaltas()) <= 0) {
				
				this.qtdeFaltas = impressaoDiferencaEstoqueDTO.getQtdeFaltas().toString();
			}
			
			if (impressaoDiferencaEstoqueDTO.getQtdeSobras() != null
					&& BigInteger.ZERO.compareTo(impressaoDiferencaEstoqueDTO.getQtdeSobras()) <= 0) {
				
				this.qtdeSobras = impressaoDiferencaEstoqueDTO.getQtdeSobras().toString();
			}
		}
		
		this.rateios = impressaoDiferencaEstoqueDTO.getRateios();
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	public String getQtdeFaltas() {
		return qtdeFaltas;
	}

	public void setQtdeFaltas(String qtdeFaltas) {
		this.qtdeFaltas = qtdeFaltas;
	}

	public String getQtdeSobras() {
		return qtdeSobras;
	}

	public void setQtdeSobras(String qtdeSobras) {
		this.qtdeSobras = qtdeSobras;
	}

	public List<RateioDiferencaDTO> getRateios() {
		return rateios;
	}

	public void setRateios(List<RateioDiferencaDTO> rateios) {
		this.rateios = rateios;
	}	
		
}
