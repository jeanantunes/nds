package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;

/**
 * DTO com informação de diferença para funcionalidade
 * de Fechamento Diário
 *
 * @author francisco.garcia
 *
 */
public class DiferencaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String codigoProduto;
    
    private String nomeProduto;
    
    private Long numeroEdicao;
    
    private TipoDiferenca tipoDiferenca;
    
    private Long qtdeExemplar;
    
    private StatusAprovacao statusAprovacao;
    
    private BigDecimal total;
    
    private String motivo;

    /**
     * @return the codigoProduto
     */
    public String getCodigoProduto() {
        return codigoProduto;
    }

    /**
     * @param codigoProduto the codigoProduto to set
     */
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    /**
     * @return the nomeProduto
     */
    public String getNomeProduto() {
        return nomeProduto;
    }

    /**
     * @param nomeProduto the nomeProduto to set
     */
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    /**
     * @return the numeroEdicao
     */
    public Long getNumeroEdicao() {
        return numeroEdicao;
    }

    /**
     * @param numeroEdicao the numeroEdicao to set
     */
    public void setNumeroEdicao(Long numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    /**
     * @return the tipoDiferenca
     */
    public TipoDiferenca getTipoDiferenca() {
        return tipoDiferenca;
    }

    /**
     * @param tipoDiferenca the tipoDiferenca to set
     */
    public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
        this.tipoDiferenca = tipoDiferenca;
    }

    /**
     * @return the qtdeExemplar
     */
    public Long getQtdeExemplar() {
        return qtdeExemplar;
    }

    /**
     * @param qtdeExemplar the qtdeExemplar to set
     */
    public void setQtdeExemplar(Long qtdeExemplar) {
        this.qtdeExemplar = qtdeExemplar;
    }

    /**
     * @return the statusAprovacao
     */
    public StatusAprovacao getStatusAprovacao() {
        return statusAprovacao;
    }

    /**
     * @param statusAprovacao the statusAprovacao to set
     */
    public void setStatusAprovacao(StatusAprovacao statusAprovacao) {
        this.statusAprovacao = statusAprovacao;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * Constrói um DTO com informações da entidade Diferenca
     * 
     * @param diferenca
     *            entidade diferença para criação do DTO
     * @return DTO criado com as informações da entidade
     */
    public static DiferencaDTO fromDiferenca(Diferenca diferenca) {
        Objects.requireNonNull(diferenca, "Diferença não deve ser nula!");
        DiferencaDTO dto = new DiferencaDTO();
        ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
        Produto produto = produtoEdicao.getProduto();
        LancamentoDiferenca lancamento = diferenca.getLancamentoDiferenca();
        
        dto.setCodigoProduto(produto.getCodigo());
        dto.setNomeProduto(produto.getNome());
        dto.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
        dto.setQtdeExemplar(diferenca.getQtdeExemplares().longValue());
        dto.setTotal(diferenca.getValorTotalReal());
        dto.setTipoDiferenca(diferenca.getTipoDiferenca());
        if (lancamento != null) {
            dto.setStatusAprovacao(lancamento.getStatus());
            dto.setMotivo(lancamento.getMotivo());
        }
        return dto;
    }

}
