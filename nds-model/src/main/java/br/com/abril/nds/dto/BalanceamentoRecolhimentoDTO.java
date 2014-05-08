package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * DTO com dados do balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4498486203318581867L;
	
	private TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento;

	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	private long mediaRecolhimentoDistribuidor;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoDeOutraSemana;
	
	private List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados;
	
	/**
	 * Construtor padrão.
	 */
	public BalanceamentoRecolhimentoDTO() {
		
	}

	/**
	 * @return the matrizRecolhimento
	 */
	public TreeMap<Date, List<ProdutoRecolhimentoDTO>> getMatrizRecolhimento() {
		return matrizRecolhimento;
	}

	/**
	 * @param matrizRecolhimento the matrizRecolhimento to set
	 */
	public void setMatrizRecolhimento(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		this.matrizRecolhimento = matrizRecolhimento;
	}
	
	/**
	 * @param matrizRecolhimento the matrizRecolhimento to add
	 */
	public void addMatrizRecolhimento(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
	
		for(Entry<Date, List <ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()){
			if(this.matrizRecolhimento.containsKey(entry.getKey())){
				this.matrizRecolhimento.get(entry.getKey()).addAll(entry.getValue());
			}else{
				Map<Date, List <ProdutoRecolhimentoDTO>> map = new HashMap<>();
				map.put(entry.getKey(), entry.getValue());
				this.matrizRecolhimento.putAll(map);
			}
		}
	}

	/**
	 * @return the capacidadeRecolhimentoDistribuidor
	 */
	public BigInteger getCapacidadeRecolhimentoDistribuidor() {
		return capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
	 */
	public void setCapacidadeRecolhimentoDistribuidor(
			BigInteger capacidadeRecolhimentoDistribuidor) {
		this.capacidadeRecolhimentoDistribuidor = capacidadeRecolhimentoDistribuidor;
	}
	
	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to add
	 */
	public void addCapacidadeRecolhimentoDistribuidor(
			BigInteger capacidadeRecolhimentoDistribuidor) {
		this.capacidadeRecolhimentoDistribuidor = capacidadeRecolhimentoDistribuidor;
	}
	
	/**
	 * @return the capacidadeRecolhimentoDistribuidor
	 */
	public long getMediaRecolhimentoDistribuidor() {
		return mediaRecolhimentoDistribuidor;
	}

	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
	 */
	public void setMediaRecolhimentoDistribuidor(
			long mediaRecolhimentoDistribuidor) {
		this.mediaRecolhimentoDistribuidor = mediaRecolhimentoDistribuidor;
	}
	
	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to add
	 */
	public void addMediaRecolhimentoDistribuidor(
			long mediaRecolhimentoDistribuidor) {
		this.mediaRecolhimentoDistribuidor = this.mediaRecolhimentoDistribuidor+mediaRecolhimentoDistribuidor;
	}

	/**
	 * @return the produtosRecolhimentoNaoBalanceados
	 */
	public List<ProdutoRecolhimentoDTO> getProdutosRecolhimentoNaoBalanceados() {
		return produtosRecolhimentoNaoBalanceados;
	}

	/**
	 * @param produtosRecolhimentoNaoBalanceados the produtosRecolhimentoNaoBalanceados to set
	 */
	public void setProdutosRecolhimentoNaoBalanceados(
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados) {
		this.produtosRecolhimentoNaoBalanceados = produtosRecolhimentoNaoBalanceados;
	}

	
	/**
	 * @param produtosRecolhimentoNaoBalanceados the produtosRecolhimentoNaoBalanceados to set
	 */
	public void addProdutosRecolhimentoNaoBalanceados(
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados) {
		this.produtosRecolhimentoNaoBalanceados.addAll(produtosRecolhimentoNaoBalanceados);
	}
	
    public List<ProdutoRecolhimentoDTO> getProdutosRecolhimentoDeOutraSemana() {
        return produtosRecolhimentoDeOutraSemana;
    }
    
    public void setProdutosRecolhimentoDeOutraSemana(List<ProdutoRecolhimentoDTO> produtosRecolhimentoDeOutraSemana) {
        this.produtosRecolhimentoDeOutraSemana = produtosRecolhimentoDeOutraSemana;
    }
    
    public void addProdutosRecolhimentoDeOutraSemana(
        List<ProdutoRecolhimentoDTO> produtosRecolhimentoDeOutraSemana) {
        this.produtosRecolhimentoDeOutraSemana.addAll(produtosRecolhimentoDeOutraSemana);
    }

    /**
	 * @return the cotasOperacaoDiferenciada
	 */
	public List<CotaOperacaoDiferenciadaDTO> getCotasOperacaoDiferenciada() {
		return cotasOperacaoDiferenciada;
	}

	/**
	 * @param cotasOperacaoDiferenciada the cotasOperacaoDiferenciada to set
	 */
	public void setCotasOperacaoDiferenciada(
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		this.cotasOperacaoDiferenciada = cotasOperacaoDiferenciada;
	}

	/**
	 * @param cotasOperacaoDiferenciada the cotasOperacaoDiferenciada to add
	 */
	public void addCotasOperacaoDiferenciada(
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		this.cotasOperacaoDiferenciada.addAll(cotasOperacaoDiferenciada);
	}
	/**
	 * @return the produtosRecolhimentoAgrupados
	 */
	public List<ProdutoRecolhimentoDTO> getProdutosRecolhimentoAgrupados() {
		return produtosRecolhimentoAgrupados;
	}

	/**
	 * @param produtosRecolhimentoAgrupados the produtosRecolhimentoAgrupados to set
	 */
	public void setProdutosRecolhimentoAgrupados(
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados) {
		this.produtosRecolhimentoAgrupados = produtosRecolhimentoAgrupados;
	}
	
	/**
	 * @param produtosRecolhimentoAgrupados the produtosRecolhimentoAgrupados to add
	 */
	public void addProdutosRecolhimentoAgrupados(
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados) {
		this.produtosRecolhimentoAgrupados.addAll(produtosRecolhimentoAgrupados);
	}
	
}
