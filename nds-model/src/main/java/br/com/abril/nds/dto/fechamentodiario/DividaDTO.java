package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * DTO com as informações da dívida para fechamento diário
 * 
 * @author francisco.garcia
 *
 */
@Exportable
public class DividaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long idDivida;
    
    @Export(label = "Cota", exhibitionOrder = 0)
    private Integer numeroCota;
    
    @Export(label = "Nome", exhibitionOrder = 1)
    private String nomeCota;
    
    @Export(label = "Banco", exhibitionOrder = 2)
    private String nomeBanco;
    
    @Export(label = "Conta-Corrente", exhibitionOrder = 3)
    private String contaCorrente;
    
    @Export(label = "Nosso Número", exhibitionOrder = 4)
    private String nossoNumero;
    
    private BigDecimal valor = BigDecimal.ZERO;
    
    @Export(label = "Valor R$", exhibitionOrder = 5, alignment = Alignment.RIGHT)
    private String valorFormatado = CurrencyUtil.formatarValor(BigDecimal.ZERO);
    
    private Date dataEmissao;
    
    private Date dataVencimento;
    
    @Export(label = "Dt. Vencto", exhibitionOrder = 6, alignment = Alignment.CENTER)
    private String dataVencimentoFormatada;
    
    @Export(label = "Dt. Emissão", exhibitionOrder = 6, alignment = Alignment.CENTER)
    private String dataEmissaoFormatada;
    
    private TipoCobranca formaPagamento;
    
    @Export(label = "Forma Pagto", exhibitionOrder = 7)
    private String descricaoFormaPagamento;
    
    public DividaDTO() {}
    
    public DividaDTO(Long idDivida, Integer numeroCota, String nomeCota, String nomeBanco,
            String contaCorrente, String nossoNumero, BigDecimal valor,
            Date dataVencimento, TipoCobranca formaPagamento, Date DataEmissao) {
        this.idDivida = idDivida;
        this.numeroCota = numeroCota;
        this.nomeCota = nomeCota;
        this.nomeBanco = nomeBanco;
        this.contaCorrente = contaCorrente;
        this.nossoNumero = nossoNumero;
        this.valor = valor;
        this.valorFormatado = CurrencyUtil.formatarValor(valor);
        this.dataVencimento = dataVencimento;
        this.dataVencimentoFormatada = DateUtil.formatarDataPTBR(dataVencimento);
        this.formaPagamento = formaPagamento;
        this.descricaoFormaPagamento = formaPagamento.getDescricao();
        this.dataEmissao = DataEmissao;
        this.dataEmissaoFormatada = DateUtil.formatarDataPTBR(dataEmissao);
    }

    /**
     * @return the idDivida
     */
    public Long getIdDivida() {
        return idDivida;
    }

    /**
     * @param idDivida the idDivida to set
     */
    public void setIdDivida(Long idDivida) {
        this.idDivida = idDivida;
    }

    /**
     * @return the numeroCota
     */
    public Integer getNumeroCota() {
        return numeroCota;
    }

    /**
     * @param numeroCota the numeroCota to set
     */
    public void setNumeroCota(Integer numeroCota) {
        this.numeroCota = numeroCota;
    }

    /**
     * @return the nomeCota
     */
    public String getNomeCota() {
        return nomeCota;
    }

    /**
     * @param nomeCota the nomeCota to set
     */
    public void setNomeCota(String nomeCota) {
        this.nomeCota = nomeCota;
    }

    /**
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @param nomeBanco the nomeBanco to set
     */
    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    /**
     * @return the contaCorrente
     */
    public String getContaCorrente() {
        return contaCorrente;
    }

    /**
     * @param contaCorrente the contaCorrente to set
     */
    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    /**
     * @return the nossoNumero
     */
    public String getNossoNumero() {
        return nossoNumero;
    }

    /**
     * @param nossoNumero the nossoNumero to set
     */
    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
        this.valorFormatado = CurrencyUtil.formatarValor(valor);
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
        this.dataVencimentoFormatada = DateUtil.formatarDataPTBR(dataVencimento);
    }

    /**
     * @return the formaPagamento
     */
    public TipoCobranca getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * @param formaPagamento the formaPagamento to set
     */
    public void setFormaPagamento(TipoCobranca formaPagamento) {
        this.formaPagamento = formaPagamento;
        this.descricaoFormaPagamento = formaPagamento.getDescricao();
    }

    /**
     * @return the valorFormatado
     */
    public String getValorFormatado() {
        return valorFormatado;
    }

    /**
     * @return the dataVencimentoFormatada
     */
    public String getDataVencimentoFormatada() {
        return dataVencimentoFormatada;
    }

    /**
     * @return the descricaoFormaPagamento
     */
    public String getDescricaoFormaPagamento() {
        return descricaoFormaPagamento;
    }
    
    /**
     * Method Factory para criação do {@link DividaDTO} à partir da entidade
     * Divida
     * 
     * @param divida
     *            divida para criação do DTO
     * @return {@link DividaDTO} criado à partir da da divida
     */
    public static DividaDTO fromDivida(Cobranca cobranca) {
        Cota cota = cobranca.getCota();
        String nomeCota = cota.getPessoa().getNome();
        String nomeBanco = "";
        String contaCorrente = "";
        Banco banco = cobranca.getBanco();
        if (banco != null) {
            nomeBanco = banco.getNome();
            contaCorrente = String.format("%s-%s", banco.getConta(), Util.nvl(banco.getDvConta(), ""));
        }
        DividaDTO dto = new DividaDTO(cobranca.getId(), cota.getNumeroCota(),
                nomeCota, nomeBanco, contaCorrente, cobranca.getNossoNumero(),
                cobranca.getValor(), cobranca.getDataVencimento(),
                cobranca.getTipoCobranca(), cobranca.getDataEmissao());
        return dto;
    }

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
		this.dataEmissaoFormatada = DateUtil.formatarDataPTBR(dataEmissao);
	}
}