package br.com.abril.nds.model.financeiro;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.TipoBaixaCobranca;

@Entity
@Table(name = "BOLETO_ANTECIPADO")
@SequenceGenerator(name="BOLETO_ANTECIPADO_SEQ", initialValue = 1, allocationSize = 1)
public class BoletoAntecipado {

    @Id
	@GeneratedValue(generator = "BOLETO_ANTECIPADO_SEQ")
	@Column(name = "ID")
	private Long id;
    
    @Column(name="DATA", nullable = false)
    private Date data;
    
    @Column(name="DATA_VENCIMENTO", nullable = false)
	private Date dataVencimento;
    
    @Column(name="DATA_PAGAMENTO", nullable = true)
   	private Date dataPagamento;
    
    @Column(name="NOSSO_NUMERO", nullable = false)
    private String nossoNumero;
    
    @Column(name="DIGITO_NOSSO_NUMERO", nullable = true)
	private String digitoNossoNumero;

	@Column(name="VALOR", nullable = true, precision=18, scale=4)
	private BigDecimal valor;
	
	@Column(name="VALOR_PAGO", nullable = true, precision=18, scale=4)
	private BigDecimal valorPago;

    @Column(name="VALOR_DESCONTO", nullable = false, precision=18, scale=4)
	private BigDecimal valorDesconto;

	@ManyToOne(optional = true)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
    
    @OneToOne(optional = false)
    @JoinColumn(name="CHAMADA_ENCALHE_COTA_ID")
    private ChamadaEncalheCota chamadaEncalheCota;
	    
	@ManyToOne
	@JoinColumn(name="FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = true)
	private StatusDivida status;
	
	@Column(name="VIAS")
	private Integer vias;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	protected TipoCobranca tipoCobranca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_BAIXA")
	protected TipoBaixaCobranca tipoBaixa;
	
	@Column(name = "VALOR_JUROS")
	private BigDecimal valorJuros;
	
	@Column(name = "VALOR_MULTA")
	private BigDecimal valorMulta;
	
	@OneToOne(optional = true)
	@JoinColumn(name="MOVIMENTO_FINANCEIRO_COTA_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroCota;
	
	@Embedded
	private EmissaoBoletoAntecipado emissaoBoletoAntecipado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	/**
	 * @return the valorDesconto
	 */
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ChamadaEncalheCota getChamadaEncalheCota() {
		return chamadaEncalheCota;
	}

	public void setChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		this.chamadaEncalheCota = chamadaEncalheCota;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public StatusDivida getStatus() {
		return status;
	}

	public void setStatus(StatusDivida status) {
		this.status = status;
	}

	public Integer getVias() {
		return vias;
	}

	public void setVias(Integer vias) {
		this.vias = vias;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	/**
	 * @return the tipoBaixa
	 */
	public TipoBaixaCobranca getTipoBaixa() {
		return tipoBaixa;
	}

	/**
	 * @param tipoBaixa the tipoBaixa to set
	 */
	public void setTipoBaixa(TipoBaixaCobranca tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	/**
	 * @return the valorJuros
	 */
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	/**
	 * @param valorJuros the valorJuros to set
	 */
	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	/**
	 * @return the valorMulta
	 */
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	/**
	 * @param valorMulta the valorMulta to set
	 */
	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public MovimentoFinanceiroCota getMovimentoFinanceiroCota() {
		return movimentoFinanceiroCota;
	}

	public void setMovimentoFinanceiroCota(
			MovimentoFinanceiroCota movimentoFinanceiroCota) {
		this.movimentoFinanceiroCota = movimentoFinanceiroCota;
	}

	public EmissaoBoletoAntecipado getEmissaoBoletoAntecipado() {
		return emissaoBoletoAntecipado;
	}

	public void setEmissaoBoletoAntecipado(
			EmissaoBoletoAntecipado emissaoBoletoAntecipado) {
		this.emissaoBoletoAntecipado = emissaoBoletoAntecipado;
	}
}
