package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO com as informações de identificação da Chamada de Encalhe do Fornecedor
 * para recolhimento dos produtos do Distribuidor
 * 
 * @author francisco.garcia
 * 
 */
public class IdentificacaoChamadaEncalheFornecedorDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer tipoCE;
    
    private Long codigo;
    
    private Long numeroCE;
    
    private Long controle;

    private String codigoNaturezaOperacao;
    
    private String descricaoNaturezaOperacao;

    private Date dataVencimento;
    
    private Date dataEmissao;
    
    private Integer numeroSemana;
    
    private Date dataLimiteChegada;


    public IdentificacaoChamadaEncalheFornecedorDTO(Integer tipoCE, Long codigo,
            Long numeroCE, Long controle, String codigoNaturezaOperacao,
            String descricaoNaturezaOperacao, Date dataVencimento,
            Date dataEmissao, Integer numeroSemana, Date dataLimiteChegada) {
        this.tipoCE = tipoCE;
        this.codigo = codigo;
        this.numeroCE = numeroCE;
        this.controle = controle;
        this.codigoNaturezaOperacao = codigoNaturezaOperacao;
        this.descricaoNaturezaOperacao = descricaoNaturezaOperacao;
        this.dataVencimento = dataVencimento;
        this.dataEmissao = dataEmissao;
        this.numeroSemana = numeroSemana;
        this.dataLimiteChegada = dataLimiteChegada;
    }


    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public Integer getTipoCE() {
        return tipoCE;
    }
    
    /**
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }


    /**
     * @return the numeroCE
     */
    public Long getNumeroCE() {
        return numeroCE;
    }


    /**
     * @return the controle
     */
    public Long getControle() {
        return controle;
    }


    /**
     * @return the codigoNaturezaOperacao
     */
    public String getCodigoNaturezaOperacao() {
        return codigoNaturezaOperacao;
    }


    /**
     * @return the descricaoNaturezaOperacao
     */
    public String getDescricaoNaturezaOperacao() {
        return descricaoNaturezaOperacao;
    }


    /**
     * @return the dataVencimento
     */
    public Date getDataVencimento() {
        return dataVencimento;
    }


    /**
     * @return the dataEmissao
     */
    public Date getDataEmissao() {
        return dataEmissao;
    }


    /**
     * @return the numeroSemana
     */
    public Integer getNumeroSemana() {
        return numeroSemana;
    }


    /**
     * @return the dataLimiteChegada
     */
    public Date getDataLimiteChegada() {
        return dataLimiteChegada;
    }

}
