package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;

/**
 * Entidade que armazena as informações de {@link Diferenca}
 * processadas pelo fechamento diário
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "FECHAMENTO_DIARIO_DIFERENCA")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_DIFERENCA_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioDiferenca implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_DIFERENCA_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "FECHAMENTO_DIARIO_ID")
    private FechamentoDiario fechamentoDiario;
    
    /**
     * Data em que a diferença foi registrada no sistema
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LANCAMENTO", nullable = false)
    private Date dataLancamento;
    
    /**
     * Identificador do {@link ProdutoEdicao} da diferença 
     */
    @Column(name = "ID_PRODUTO_EDICAO", nullable = false)
    private Long idProdutoEdicao;
    
    /**
     * Código do {@link Produto} da diferença
     */
    @Column(name = "CODIGO_PRODUTO", nullable = false)
    private String codigoProduto;
    
    /**
     * Nome do {@link Produto} da diferença
     */
    @Column(name = "NOME_PRODUTO", nullable = false)
    private String nomeProduto;
    
    /**
     * Número da edição do {@link ProdutoEdicao} da diferença
     */
    @Column(name = "NUMERO_EDICAO", nullable = false)
    private Long numeroEdicao;
    
    /**
     * Tipo de diferença
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DIFERENCA", nullable = false)
    private TipoDiferenca tipoDiferenca;
    
    /**
     * Quantidade de exemplares da diferença
     */
    @Column(name = "QTDE_EXEMPLARES", nullable = false)
    private BigInteger qtdeExemplares;
    
    /**
     * Status de aprovação da diferença
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_APROVACAO")
    private StatusAprovacao statusAprovacao;
    
    /**
     * Total em R$ da diferença
     */
    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;
    
    /**
     * Motivo do status de aprovação da diferença
     */
    @Column(name = "MOTIVO_APROVACAO")
    private String motivoAprovacao;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fechamentoDiario
     */
    public FechamentoDiario getFechamentoDiario() {
        return fechamentoDiario;
    }

    /**
     * @param fechamentoDiario the fechamentoDiario to set
     */
    public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
        this.fechamentoDiario = fechamentoDiario;
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
    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    /**
     * @return the idProdutoEdicao
     */
    public Long getIdProdutoEdicao() {
        return idProdutoEdicao;
    }

    /**
     * @param idProdutoEdicao the idProdutoEdicao to set
     */
    public void setIdProdutoEdicao(Long idProdutoEdicao) {
        this.idProdutoEdicao = idProdutoEdicao;
    }

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
     * @return the qtdeExemplares
     */
    public BigInteger getQtdeExemplares() {
        return qtdeExemplares;
    }

    /**
     * @param qtdeExemplares the qtdeExemplares to set
     */
    public void setQtdeExemplares(BigInteger qtdeExemplares) {
        this.qtdeExemplares = qtdeExemplares;
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
     * @return the motivoAprovacao
     */
    public String getMotivoAprovacao() {
        return motivoAprovacao;
    }

    /**
     * @param motivoAprovacao the motivoAprovacao to set
     */
    public void setMotivoAprovacao(String motivoAprovacao) {
        this.motivoAprovacao = motivoAprovacao;
    }
    
    /**
     * Constrói a diferença processada no fechamento diário à partir da
     * {@link Diferenca}
     * 
     * @param diferenca
     *            diferença para criação do {@link FechamentoDiarioDiferenca}
     * @return {@link FechamentoDiarioDiferenca} criado à partir da diferença
     *         recebida
     */
    public static FechamentoDiarioDiferenca fromDiferenca(Diferenca diferenca) {
        FechamentoDiarioDiferenca fechamentoDiferenca = new FechamentoDiarioDiferenca();
        
        ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
        Produto produto = produtoEdicao.getProduto();

        fechamentoDiferenca.setCodigoProduto(produto.getCodigo());
        fechamentoDiferenca.setNomeProduto(produto.getNome());
        fechamentoDiferenca.setIdProdutoEdicao(produtoEdicao.getId());
        fechamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
        
        fechamentoDiferenca.setDataLancamento(diferenca.getDataMovimento());
        fechamentoDiferenca.setQtdeExemplares(diferenca.getQtdeExemplares());
        fechamentoDiferenca.setTotal(diferenca.getValorTotal());
        fechamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca());
        
        LancamentoDiferenca lancamento = diferenca.getLancamentoDiferenca();
        if (lancamento != null) {
            fechamentoDiferenca.setStatusAprovacao(lancamento.getStatus());
            fechamentoDiferenca.setMotivoAprovacao(lancamento.getMotivo());
        }
        return fechamentoDiferenca;
    }

}
