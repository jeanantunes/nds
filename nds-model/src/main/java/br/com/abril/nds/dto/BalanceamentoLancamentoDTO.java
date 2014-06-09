package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * DTO com dados do balanceamento de lançamento.
 * 
 * @author Discover Technology
 * 
 */
public class BalanceamentoLancamentoDTO implements Serializable {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4648494827298814073L;
    
    private Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento;
    
    private BigInteger capacidadeDistribuicao;
    
    private long mediaLancamentoDistribuidor;
    
    private Date dataLancamento;
    
    private List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados;
    
    private Set<Date> datasExpedicaoConfirmada;
    
	            /**
     * Construtor padrão.
     */
    public BalanceamentoLancamentoDTO() {
        
    }
    
    /**
     * @return the matrizLancamento
     */
    public Map<Date, List<ProdutoLancamentoDTO>> getMatrizLancamento() {
        return matrizLancamento;
    }
    
    /**
     * @param matrizLancamento the matrizLancamento to set
     */
    public void setMatrizLancamento(final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
        this.matrizLancamento = matrizLancamento;
    }
    
    /**
     * @param matrizRecolhimento the matrizRecolhimento to add
     */
    public void addMatrizLancamento(final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
        
        
        for(final Entry<Date, List <ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()){
            if(this.matrizLancamento.containsKey(entry.getKey())){
                this.matrizLancamento.get(entry.getKey()).addAll(entry.getValue());
            }else{
                final Map<Date, List <ProdutoLancamentoDTO>> map = new HashMap<>();
                map.put(entry.getKey(), entry.getValue());
                this.matrizLancamento.putAll(map);
            }
        }
    }
    
    
    /**
     * @return the capacidadeDistribuicao
     */
    public BigInteger getCapacidadeDistribuicao() {
        return capacidadeDistribuicao;
    }
    
    /**
     * @param capacidadeDistribuicao the capacidadeDistribuicao to set
     */
    public void setCapacidadeDistribuicao(final BigInteger capacidadeDistribuicao) {
        this.capacidadeDistribuicao = capacidadeDistribuicao;
    }
    
    /**
     * @param capacidadeDistribuicao the capacidadeDistribuicao to add
     */
    public void addCapacidadeDistribuicao(
            final BigInteger capacidadeDistribuicao) {
        this.capacidadeDistribuicao = capacidadeDistribuicao;
    }
    
    /**
     * @return the capacidadeRecolhimentoDistribuidor
     */
    public long getMediaLancamentoDistribuidor() {
        return mediaLancamentoDistribuidor;
    }
    
    /**
     * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
     */
    public void setMediaLancamentoDistribuidor(
            final long mediaLancamentoDistribuidor) {
        this.mediaLancamentoDistribuidor = mediaLancamentoDistribuidor;
    }
    
    /**
     * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to add
     */
    public void addMediaLancamentoDistribuidor(
            final long mediaLancamentoDistribuidor) {
        this.mediaLancamentoDistribuidor = this.mediaLancamentoDistribuidor+mediaLancamentoDistribuidor;
    }
    
    
    /**
     * @return the dataLancamento
     */
    public Date getDataLancamento() {
        return dataLancamento;
    }
    
    /**
     * @param dataLancamento the dataLancamento to set
     */
    public void setDataLancamento(final Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
    
    /**
     * @return the produtosLancamentosCancelados
     */
    public List<ProdutoLancamentoCanceladoDTO> getProdutosLancamentosCancelados() {
        return produtosLancamentosCancelados;
    }
    
    /**
     * @param produtosLancamentosCancelados the produtosLancamentosCancelados to set
     */
    public void setProdutosLancamentosCancelados(
            final List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados) {
        this.produtosLancamentosCancelados = produtosLancamentosCancelados;
    }
    
    /**
     * @param produtosLancamentosCancelados the produtosLancamentosCancelados to set
     */
    public void addProdutosLancamentosCancelados(
            final List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados) {
        this.produtosLancamentosCancelados.addAll(produtosLancamentosCancelados);
    }
    
    /**
     * @return the datasExpedicaoConfirmada
     */
    public Set<Date> getDatasExpedicaoConfirmada() {
        return datasExpedicaoConfirmada;
    }
    
    /**
     * @param datasExpedicaoConfirmada the datasExpedicaoConfirmada to set
     */
    public void setDatasExpedicaoConfirmada(final Set<Date> datasExpedicaoConfirmada) {
        this.datasExpedicaoConfirmada = datasExpedicaoConfirmada;
    }
    
}
