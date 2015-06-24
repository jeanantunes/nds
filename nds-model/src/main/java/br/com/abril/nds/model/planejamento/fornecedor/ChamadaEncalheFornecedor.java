package br.com.abril.nds.model.planejamento.fornecedor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.integracao.StatusIntegracaoNFE;

/**
 * Chamada de Encalhe do Fornecedor para retorno
 * dos produtos recolhidos pelo Distribuidor
 * 
 */
@Entity
@Table(name = "CHAMADA_ENCALHE_FORNECEDOR")
@SequenceGenerator(name="CH_ENC_FORNECEDOR_SEQ", initialValue = 1, allocationSize = 1)
public class ChamadaEncalheFornecedor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "CH_ENC_FORNECEDOR_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "CODIGO_DISTRIBUIDOR")
    private Long codigoDistribuidor;

    @ManyToOne
    @JoinColumn(name = "FORNECEDOR_ID")
    private Fornecedor fornecedor;

    @Column(name = "NUM_CHAMADA_ENCALHE", unique = true, nullable = false)
    private Long numeroChamadaEncalhe;

    @Column(name = "TIPO_CHAMADA_ENCALHE", nullable = false)
    private Integer tipoChamadaEncalhe;

    @ManyToOne(optional = true)
    @JoinColumn(name = "CFOP_ID")
    private CFOP cfop;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO", nullable = false)
    private Date dataEmissao;

    @Column(name = "ANO_REFERENCIA")
    private Integer anoReferencia;

    @Column(name = "NUMERO_SEMANA", nullable = false)
    private Integer numeroSemana;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENCIMENTO", nullable = false)
    private Date dataVencimento;

    @Column(name = "CONTROLE")
    private Long controle;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LIMITE_RECOLHIMENTO")
    private Date dataLimiteRecebimento;

    @Column(name = "STATUS", nullable = false)
    private String status;
    
    /**
     * Status da semana (fechada ou não) do sistema NDS (e não do Prodin)
     */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_CE_NDS")
    private StatusCeNDS statusCeNDS = StatusCeNDS.ABERTO;
    
    @Column(name = "CODIGO_PREENCHIMENTO", nullable = false)
    private String codigoPreenchimento;

    @Column(name = "TOTAL_VENDA_APURADA")
    private BigDecimal totalVendaApurada;

    @Column(name = "TOTAL_CREDITO_APURADO")
    private BigDecimal totalCreditoApurado;

    @Column(name = "TOTAL_VENDA_INFORMADA")
    private BigDecimal totalVendaInformada;

    @Column(name = "TOTAL_CREDITO_INFORMADO")
    private BigDecimal totalCreditoInformado;

    @Column(name = "TOTAL_MARGEM_INFORMADO")
    private BigDecimal totalMargemInformado;

    @Column(name = "TOTAL_MARGEM_APURADO")
    private BigDecimal totalMargemApurado;

    @Column(name = "NOTA_VALORES_DIVERSOS")
    private BigDecimal notaValoresDiversos;

    @OneToMany(mappedBy = "chamadaEncalheFornecedor")
    @Cascade(value = CascadeType.ALL)
    private List<ItemChamadaEncalheFornecedor> itens = new ArrayList<ItemChamadaEncalheFornecedor>();
    
    @Enumerated(EnumType.STRING)
	@Column(name = "STATUS_INTEGRACAO")
    private StatusIntegracao statusIntegracao;
    
    @Enumerated(EnumType.STRING)
	@Column(name = "STATUS_INTEGRACAO_NFE")
    private StatusIntegracaoNFE statusIntegracaoNFE;
    
    /**
     * Será atribuido a data de Operação do Distribuidor
     */
    @Column(name="DATA_FECHAMENTO_NDS")
    @Temporal(TemporalType.DATE)
    private Date dataFechamentoNDS;
    
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
     * @return the codigoDistribuidor
     */
    public Long getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    /**
     * @param codigoDistribuidor the codigoDistribuidor to set
     */
    public void setCodigoDistribuidor(Long codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }

    /**
     * @return the fornecedor
     */
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return the numeroChamadaEncalhe
     */
    public Long getNumeroChamadaEncalhe() {
        return numeroChamadaEncalhe;
    }

    /**
     * @param numeroChamadaEncalhe the numeroChamadaEncalhe to set
     */
    public void setNumeroChamadaEncalhe(Long numeroChamadaEncalhe) {
        this.numeroChamadaEncalhe = numeroChamadaEncalhe;
    }

    /**
     * @return the tipoChamadaEncalhe
     */
    public Integer getTipoChamadaEncalhe() {
        return tipoChamadaEncalhe;
    }

    /**
     * @param tipoChamadaEncalhe the tipoChamadaEncalhe to set
     */
    public void setTipoChamadaEncalhe(Integer tipoChamadaEncalhe) {
        this.tipoChamadaEncalhe = tipoChamadaEncalhe;
    }

    /**
     * @return the cfop
     */
    public CFOP getCfop() {
        return cfop;
    }

    /**
     * @param cfop the cfop to set
     */
    public void setCfop(CFOP cfop) {
        this.cfop = cfop;
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
     * @return the anoReferencia
     */
    public Integer getAnoReferencia() {
        return anoReferencia;
    }

    /**
     * @param anoReferencia the anoReferencia to set
     */
    public void setAnoReferencia(Integer anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    /**
     * @return the numeroSemana
     */
    public Integer getNumeroSemana() {
        return numeroSemana;
    }

    /**
     * @param numeroSemana the numeroSemana to set
     */
    public void setNumeroSemana(Integer numeroSemana) {
        this.numeroSemana = numeroSemana;
    }
    
    /**
     * @return the dataVencimento
     */
    public Date getDataVencimento() {
        return dataVencimento;
    }

    /**
     * @param dataVencimento the dataVencimento to set
     */
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    /**
     * @return the controle
     */
    public Long getControle() {
        return controle;
    }

    /**
     * @param controle the controle to set
     */
    public void setControle(Long controle) {
        this.controle = controle;
    }

    /**
     * @return the dataLimiteRecebimento
     */
    public Date getDataLimiteRecebimento() {
        return dataLimiteRecebimento;
    }

    /**
     * @param dataLimiteRecebimento the dataLimiteRecebimento to set
     */
    public void setDataLimiteRecebimento(Date dataLimiteRecebimento) {
        this.dataLimiteRecebimento = dataLimiteRecebimento;
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
     * @return the codigoPreenchimento
     */
    public String getCodigoPreenchimento() {
        return codigoPreenchimento;
    }

    /**
     * @param codigoPreenchimento the codigoPreenchimento to set
     */
    public void setCodigoPreenchimento(String codigoPreenchimento) {
        this.codigoPreenchimento = codigoPreenchimento;
    }

    /**
     * @return the totalVendaApurada
     */
    public BigDecimal getTotalVendaApurada() {
        return totalVendaApurada;
    }

    /**
     * @param totalVendaApurada the totalVendaApurada to set
     */
    public void setTotalVendaApurada(BigDecimal totalVendaApurada) {
        this.totalVendaApurada = totalVendaApurada;
    }

    /**
     * @return the totalCreditoApurado
     */
    public BigDecimal getTotalCreditoApurado() {
        return totalCreditoApurado;
    }

    /**
     * @param totalCreditoApurado the totalCreditoApurado to set
     */
    public void setTotalCreditoApurado(BigDecimal totalCreditoApurado) {
        this.totalCreditoApurado = totalCreditoApurado;
    }

    /**
     * @return the totalVendaInformada
     */
    public BigDecimal getTotalVendaInformada() {
        return totalVendaInformada;
    }

    /**
     * @param totalVendaInformada the totalVendaInformada to set
     */
    public void setTotalVendaInformada(BigDecimal totalVendaInformada) {
        this.totalVendaInformada = totalVendaInformada;
    }

    /**
     * @return the totalCreditoInformado
     */
    public BigDecimal getTotalCreditoInformado() {
        return totalCreditoInformado;
    }

    /**
     * @param totalCreditoInformado the totalCreditoInformado to set
     */
    public void setTotalCreditoInformado(BigDecimal totalCreditoInformado) {
        this.totalCreditoInformado = totalCreditoInformado;
    }

    /**
     * @return the totalMargemInformado
     */
    public BigDecimal getTotalMargemInformado() {
        return totalMargemInformado;
    }

    /**
     * @param totalMargemInformado the totalMargemInformado to set
     */
    public void setTotalMargemInformado(BigDecimal totalMargemInformado) {
        this.totalMargemInformado = totalMargemInformado;
    }

    /**
     * @return the totalMargemApurado
     */
    public BigDecimal getTotalMargemApurado() {
        return totalMargemApurado;
    }

    /**
     * @param totalMargemApurado the totalMargemApurado to set
     */
    public void setTotalMargemApurado(BigDecimal totalMargemApurado) {
        this.totalMargemApurado = totalMargemApurado;
    }

    /**
     * @return the notaValoresDiversos
     */
    public BigDecimal getNotaValoresDiversos() {
        return notaValoresDiversos;
    }

    /**
     * @param notaValoresDiversos the notaValoresDiversos to set
     */
    public void setNotaValoresDiversos(BigDecimal notaValoresDiversos) {
        this.notaValoresDiversos = notaValoresDiversos;
    }
    
    /**
     * @return the itens
     */
    public List<ItemChamadaEncalheFornecedor> getItens() {
        return itens;
    }

    /**
     * @param itens the itens to set
     */
    public void setItens(List<ItemChamadaEncalheFornecedor> itens) {
        this.itens = itens;
    }
    
	public StatusCeNDS getStatusCeNDS() {
		return statusCeNDS;
	}

	public void setStatusCeNDS(StatusCeNDS statusCeNDS) {
		this.statusCeNDS = statusCeNDS;
	}
	
	public StatusIntegracao getStatusIntegracao() {
		return statusIntegracao;
	}

	public void setStatusIntegracao(StatusIntegracao statusIntegracao) {
		this.statusIntegracao = statusIntegracao;
	}

	/**
	 * @return the statusIntegracaoNFE
	 */
	public StatusIntegracaoNFE getStatusIntegracaoNFE() {
		return statusIntegracaoNFE;
	}

	/**
	 * @param statusIntegracaoNFE the statusIntegracaoNFE to set
	 */
	public void setStatusIntegracaoNFE(StatusIntegracaoNFE statusIntegracaoNFE) {
		this.statusIntegracaoNFE = statusIntegracaoNFE;
	}

	public Date getDataFechamentoNDS() {
		return dataFechamentoNDS;
	}

	public void setDataFechamentoNDS(Date dataFechamentoNDS) {
		this.dataFechamentoNDS = dataFechamentoNDS;
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
                + ((numeroChamadaEncalhe == null) ? 0 : numeroChamadaEncalhe
                        .hashCode());
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
        ChamadaEncalheFornecedor other = (ChamadaEncalheFornecedor) obj;
        if (numeroChamadaEncalhe == null) {
            if (other.numeroChamadaEncalhe != null) {
                return false;
            }
        } else if (!numeroChamadaEncalhe.equals(other.numeroChamadaEncalhe)) {
            return false;
        }
        return true;
    }
  
}
