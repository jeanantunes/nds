package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * DTO com informações de impressão de diferenças de estoque.
 * 
 * @author Discover Technology
 *
 */
public class ImpressaoDiferencaEstoqueDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8880460525015896929L;
	
	private Long idDiferenca;
	
	private ProdutoEdicao produtoEdicao;
	
	private BigInteger qtdeFaltas;
	
	private BigInteger qtdeSobras;
	
	private List<RateioDiferencaDTO> rateios;
	
	/**
	 * Construtor padrão.
	 */
	public ImpressaoDiferencaEstoqueDTO() {
		
		
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQtdeFaltas() {
		return qtdeFaltas;
	}

	public void setQtdeFaltas(BigInteger qtdeFaltas) {
		
		if (qtdeFaltas == null || BigInteger.ZERO.compareTo(qtdeFaltas) == 0){
			
			this.qtdeFaltas = null;
		} else {
		
			this.qtdeFaltas = qtdeFaltas;
		}
	}

	public BigInteger getQtdeSobras() {
		return qtdeSobras;
	}

	public void setQtdeSobras(BigInteger qtdeSobras) {
		
		if (qtdeSobras == null || BigInteger.ZERO.compareTo(qtdeSobras) == 0){
			
			this.qtdeSobras = null;
		} else {
			
			this.qtdeSobras = qtdeSobras;
		}
	}

	public Long getIdDiferenca() {
		return idDiferenca;
	}

	public void setIdDiferenca(Long idDiferenca) {
		this.idDiferenca = idDiferenca;
	}
	
	public List<RateioDiferencaDTO> getRateios() {
		return rateios;
	}

	public void setRateios(List<RateioDiferencaDTO> rateios) {
		this.rateios = rateios;
	}

}
