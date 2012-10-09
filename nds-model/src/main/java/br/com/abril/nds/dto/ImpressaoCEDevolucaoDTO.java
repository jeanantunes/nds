package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO para a impressão da CE Devolução
 *
 * @author francisco.garcia
 *
 */
public class ImpressaoCEDevolucaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long numero;
    
    private IdentificacaoImpressaoCEDevolucaoDTO fornecedor;
    
    private IdentificacaoImpressaoCEDevolucaoDTO distribuidor;
    
    private Date dataRecolhimento;
    
    private Date dataEmissao;
    
    private List<ProdutoImpressaoCEDevolucaoDTO> produtos = new ArrayList<ProdutoImpressaoCEDevolucaoDTO>();
    
    private BigDecimal totalBruto;
    
    private BigDecimal totalDesconto;
    
    private BigDecimal totalLiquido;

    /**
     * @return the numero
     */
    public Long getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(Long numero) {
        this.numero = numero;
    }

    /**
     * @return the fornecedor
     */
    public IdentificacaoImpressaoCEDevolucaoDTO getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(IdentificacaoImpressaoCEDevolucaoDTO fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return the distribuidor
     */
    public IdentificacaoImpressaoCEDevolucaoDTO getDistribuidor() {
        return distribuidor;
    }

    /**
     * @param distribuidor the distribuidor to set
     */
    public void setDistribuidor(IdentificacaoImpressaoCEDevolucaoDTO distribuidor) {
        this.distribuidor = distribuidor;
    }

    /**
     * @return the dataRecolhimento
     */
    public Date getDataRecolhimento() {
        return dataRecolhimento;
    }

    /**
     * @param dataRecolhimento the dataRecolhimento to set
     */
    public void setDataRecolhimento(Date dataRecolhimento) {
        this.dataRecolhimento = dataRecolhimento;
    }

    /**
     * @return the dataEmissao
     */
    public Date getDataEmissao() {
        return dataEmissao;
    }

    /**
     * @param dataEmissao the dataEmissao to set
     */
    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    /**
     * @return the produtos
     */
    public List<ProdutoImpressaoCEDevolucaoDTO> getProdutos() {
        return produtos;
    }

    /**
     * @param produtos the produtos to set
     */
    public void setProdutos(List<ProdutoImpressaoCEDevolucaoDTO> produtos) {
        this.produtos = produtos;
    }

    /**
     * @return the totalBruto
     */
    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    /**
     * @param totalBruto the totalBruto to set
     */
    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    /**
     * @return the totalDesconto
     */
    public BigDecimal getTotalDesconto() {
        return totalDesconto;
    }

    /**
     * @param totalDesconto the totalDesconto to set
     */
    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    /**
     * @return the totalLiquido
     */
    public BigDecimal getTotalLiquido() {
        return totalLiquido;
    }

    /**
     * @param totalLiquido the totalLiquido to set
     */
    public void setTotalLiquido(BigDecimal totalLiquido) {
        this.totalLiquido = totalLiquido;
    }
    
    public void addProduto(ProdutoImpressaoCEDevolucaoDTO produto) {
        if(produtos == null) {
            produtos = new ArrayList<ProdutoImpressaoCEDevolucaoDTO>();
        }
        produtos.add(produto);
    }
}
