package br.com.abril.nds.model.planejamento.fornecedor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;

/**
 * Item da Chamada de Encalhe do Fornecedor para retorno
 * dos produtos recolhidos pelo Distribuidor
 * 
 */
@Entity
@Table(name = "ITEM_CHAMADA_ENCALHE_FORNECEDOR")
@SequenceGenerator(name="ITEM_CH_ENC_FORNECEDOR_SEQ", initialValue = 1, allocationSize = 1)
public class ItemChamadaEncalheFornecedor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "ITEM_CH_ENC_FORNECEDOR_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMERO_DOCUMENTO", nullable = false)
    private Long numeroDocumento;

    @ManyToOne(optional = false,fetch=FetchType.LAZY)
    @JoinColumn(name = "CHAMADA_ENCALHE_FORNECEDOR_ID")
    private ChamadaEncalheFornecedor chamadaEncalheFornecedor;

    @ManyToOne(optional = false,fetch=FetchType.LAZY)
    @JoinColumn(name = "PRODUTO_EDICAO_ID")
    private ProdutoEdicao produtoEdicao;

    @Column(name = "NUEMRO_ITEM", nullable = false)
    private Integer numeroItem;

    @Column(name = "QTDE_ENVIADA", nullable = false)
    private Long qtdeEnviada;

    @Column(name = "FORMA_DEVOLUCAO")
    @Enumerated(EnumType.STRING)
    private FormaDevolucao formaDevolucao;

    @Column(name = "CONTROLE", nullable = false)
    private Integer controle;

    @Column(name = "REGIME_RECOLHIMENTO")
    @Enumerated(EnumType.STRING)
    private RegimeRecolhimento regimeRecolhimento;

    @Column(name = "PRECO_UNITARIO")
    private BigDecimal precoUnitario;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_RECOLHIMENTO")
    private Date dataRecolhimento;

    @Column(name = "QTDE_DEVOLUCAO_APURADA")
    private Long qtdeDevolucaoApurada;

    @Column(name = "QTDE_VENDA_APURADA")
    private Long qtdeVendaApurada;

    @Column(name = "VALOR_VENDA_APURADO")
    private BigDecimal valorVendaApurado;

    @Column(name = "QTDE_DEVOLUCAO_INFORMADA")
    private Long qtdeDevolucaoInformada;

    @Column(name = "QTDE_VENDA_INFORMADA")
    private Long qtdeVendaInformada;

    @Column(name = "VALOR_VENDA_INFORMADO")
    private BigDecimal valorVendaInformado;

    @Column(name = "NUMERO_NOTA_ENVIO", nullable = false)
    private Long numeroNotaEnvio;
    
    @Column(name = "CODIGO_NOTA_ENVIO_MULTIPLA")
    private String codigoNotaEnvioMultipla;

    @Column(name = "TIPO_PRODUTO", nullable = false)
    private String tipoProduto;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "QTDE_DEVOLUCAO_PARCIAL")
    private Long qtdeDevolucaoParcial;

    @Column(name = "VALOR_MARGEM_INFORMADO")
    private BigDecimal valorMargemInformado;

    @Column(name = "VALOR_MARGEM_APURADO")
    private BigDecimal valorMargemApurado;
    
    @OneToOne
    @JoinColumn(name = "DIFERENCA_ID")
    private Diferenca diferenca;
    
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
     * @return the numeroDocumento
     */
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the chamadaEncalheFornecedor
     */
    public ChamadaEncalheFornecedor getChamadaEncalheFornecedor() {
        return chamadaEncalheFornecedor;
    }

    /**
     * @param chamadaEncalheFornecedor the chamadaEncalheFornecedor to set
     */
    public void setChamadaEncalheFornecedor(
            ChamadaEncalheFornecedor chamadaEncalheFornecedor) {
        this.chamadaEncalheFornecedor = chamadaEncalheFornecedor;
    }

    /**
     * @return the produtoEdicao
     */
    public ProdutoEdicao getProdutoEdicao() {
        return produtoEdicao;
    }

    /**
     * @param produtoEdicao the produtoEdicao to set
     */
    public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
        this.produtoEdicao = produtoEdicao;
    }

    /**
     * @return the numeroItem
     */
    public Integer getNumeroItem() {
        return numeroItem;
    }

    /**
     * @param numeroItem the numeroItem to set
     */
    public void setNumeroItem(Integer numeroItem) {
        this.numeroItem = numeroItem;
    }

    /**
     * @return the qtdeEnviada
     */
    public Long getQtdeEnviada() {
        return qtdeEnviada;
    }

    /**
     * @param qtdeEnviada the qtdeEnviada to set
     */
    public void setQtdeEnviada(Long qtdeEnviada) {
        this.qtdeEnviada = qtdeEnviada;
    }

    /**
     * @return the formaDevolucao
     */
    public FormaDevolucao getFormaDevolucao() {
        return formaDevolucao;
    }

    /**
     * @param formaDevolucao the formaDevolucao to set
     */
    public void setFormaDevolucao(FormaDevolucao formaDevolucao) {
        this.formaDevolucao = formaDevolucao;
    }

    /**
     * @return the controle
     */
    public Integer getControle() {
        return controle;
    }

    /**
     * @param controle the controle to set
     */
    public void setControle(Integer controle) {
        this.controle = controle;
    }

    /**
     * @return the regimeRecolhimento
     */
    public RegimeRecolhimento getRegimeRecolhimento() {
        return regimeRecolhimento;
    }

    /**
     * @param regimeRecolhimento the regimeRecolhimento to set
     */
    public void setRegimeRecolhimento(RegimeRecolhimento regimeRecolhimento) {
        this.regimeRecolhimento = regimeRecolhimento;
    }

    /**
     * @return the precoUnitario
     */
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    /**
     * @param precoUnitario the precoUnitario to set
     */
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
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
     * @return the qtdeDevolucaoApurada
     */
    public Long getQtdeDevolucaoApurada() {
        return qtdeDevolucaoApurada;
    }

    /**
     * @param qtdeDevolucaoApurada the qtdeDevolucaoApurada to set
     */
    public void setQtdeDevolucaoApurada(Long qtdeDevolucaoApurada) {
        this.qtdeDevolucaoApurada = qtdeDevolucaoApurada;
    }

    /**
     * @return the qtdeVendaApurada
     */
    public Long getQtdeVendaApurada() {
        return qtdeVendaApurada;
    }

    /**
     * @param qtdeVendaApurada the qtdeVendaApurada to set
     */
    public void setQtdeVendaApurada(Long qtdeVendaApurada) {
        this.qtdeVendaApurada = qtdeVendaApurada;
    }

    /**
     * @return the valorVendaApurado
     */
    public BigDecimal getValorVendaApurado() {
        return valorVendaApurado;
    }

    /**
     * @param valorVendaApurado the valorVendaApurado to set
     */
    public void setValorVendaApurado(BigDecimal valorVendaApurado) {
        this.valorVendaApurado = valorVendaApurado;
    }

    /**
     * @return the qtdeDevolucaoInformada
     */
    public Long getQtdeDevolucaoInformada() {
        return qtdeDevolucaoInformada;
    }

    /**
     * @param qtdeDevolucaoInformada the qtdeDevolucaoInformada to set
     */
    public void setQtdeDevolucaoInformada(Long qtdeDevolucaoInformada) {
        this.qtdeDevolucaoInformada = qtdeDevolucaoInformada;
    }

    /**
     * @return the qtdeVendaInformada
     */
    public Long getQtdeVendaInformada() {
        return qtdeVendaInformada;
    }

    /**
     * @param qtdeVendaInformada the qtdeVendaInformada to set
     */
    public void setQtdeVendaInformada(Long qtdeVendaInformada) {
        this.qtdeVendaInformada = qtdeVendaInformada;
    }

    /**
     * @return the valorVendaInformado
     */
    public BigDecimal getValorVendaInformado() {
        return valorVendaInformado;
    }

    /**
     * @param valorVendaInformado the valorVendaInformado to set
     */
    public void setValorVendaInformado(BigDecimal valorVendaInformado) {
        this.valorVendaInformado = valorVendaInformado;
    }

    /**
     * @return the numeroNotaEnvio
     */
    public Long getNumeroNotaEnvio() {
        return numeroNotaEnvio;
    }

    /**
     * @param numeroNotaEnvio the numeroNotaEnvio to set
     */
    public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
        this.numeroNotaEnvio = numeroNotaEnvio;
    }

    /**
     * @return the codigoNotaEnvioMultipla
     */
    public String getCodigoNotaEnvioMultipla() {
        return codigoNotaEnvioMultipla;
    }

    /**
     * @param codigoNotaEnvioMultipla the codigoNotaEnvioMultipla to set
     */
    public void setCodigoNotaEnvioMultipla(String codigoNotaEnvioMultipla) {
        this.codigoNotaEnvioMultipla = codigoNotaEnvioMultipla;
    }

    /**
     * @return the tipoProduto
     */
    public String getTipoProduto() {
        return tipoProduto;
    }

    /**
     * @param tipoProduto the tipoProduto to set
     */
    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the qtdeDevolucaoParcial
     */
    public Long getQtdeDevolucaoParcial() {
        return qtdeDevolucaoParcial;
    }

    /**
     * @param qtdeDevolucaoParcial the qtdeDevolucaoParcial to set
     */
    public void setQtdeDevolucaoParcial(Long qtdeDevolucaoParcial) {
        this.qtdeDevolucaoParcial = qtdeDevolucaoParcial;
    }

    /**
     * @return the valorMargemInformado
     */
    public BigDecimal getValorMargemInformado() {
        return valorMargemInformado;
    }

    /**
     * @param valorMargemInformado the valorMargemInformado to set
     */
    public void setValorMargemInformado(BigDecimal valorMargemInformado) {
        this.valorMargemInformado = valorMargemInformado;
    }

    /**
     * @return the valorMargemApurado
     */
    public BigDecimal getValorMargemApurado() {
        return valorMargemApurado;
    }

    /**
     * @param valorMargemApurado the valorMargemApurado to set
     */
    public void setValorMargemApurado(BigDecimal valorMargemApurado) {
        this.valorMargemApurado = valorMargemApurado;
    }

	/**
	 * @return the diferenca
	 */
	public Diferenca getDiferenca() {
		return diferenca;
	}

	/**
	 * @param diferenca the diferenca to set
	 */
	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((chamadaEncalheFornecedor == null) ? 0
                        : chamadaEncalheFornecedor.hashCode());
        result = prime * result
                + ((numeroDocumento == null) ? 0 : numeroDocumento.hashCode());
        result = prime * result
                + ((numeroItem == null) ? 0 : numeroItem.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ItemChamadaEncalheFornecedor other = (ItemChamadaEncalheFornecedor) obj;
        if (chamadaEncalheFornecedor == null) {
            if (other.chamadaEncalheFornecedor != null) {
                return false;
            }
        } else if (!chamadaEncalheFornecedor
                .equals(other.chamadaEncalheFornecedor)) {
            return false;
        }
        if (numeroDocumento == null) {
            if (other.numeroDocumento != null) {
                return false;
            }
        } else if (!numeroDocumento.equals(other.numeroDocumento)) {
            return false;
        }
        if (numeroItem == null) {
            if (other.numeroItem != null) {
                return false;
            }
        } else if (!numeroItem.equals(other.numeroItem)) {
            return false;
        }
        return true;
    }

}
