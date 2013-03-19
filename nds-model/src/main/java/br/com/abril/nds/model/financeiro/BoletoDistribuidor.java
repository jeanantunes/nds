package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
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
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;

@Entity
@Table(name = "BOLETO_DISTRIBUIDOR")
@SequenceGenerator(name="BOLETO_DISTRIBUIDOR_SEQ", initialValue = 1, allocationSize = 1)
public class BoletoDistribuidor {

    @Id
	@GeneratedValue(generator = "BOLETO_DISTRIBUIDOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
    @OneToOne(optional = false)
    @JoinColumn(name="CHAMADA_ENCALHE_FORNECEDOR_ID")
    private ChamadaEncalheFornecedor chamadaEncalheFornecedor;
    
    @Column(name="DATA_EMISSAO", nullable = false)
    private Date dataEmissao;
    
    @Column(name="DATA_VENCIMENTO", nullable = false)
	private Date dataVencimento;
    
    @Column(name="NOSSO_NUMERO_DISTRIBUIDOR", nullable = false)
    private String nossoNumeroDistribuidor;
    
    @Column(name="DIGITO_NOSSO_NUMERO_DISTRIBUIDOR", nullable = true)
	private String digitoNossoNumeroDistribuidor;
	
	@Column(name="VALOR", nullable = true)
	private BigDecimal valor;
	
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChamadaEncalheFornecedor getChamadaEncalheFornecedor() {
		return chamadaEncalheFornecedor;
	}

	public void setChamadaEncalheFornecedor(
			ChamadaEncalheFornecedor chamadaEncalheFornecedor) {
		this.chamadaEncalheFornecedor = chamadaEncalheFornecedor;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getNossoNumeroDistribuidor() {
		return nossoNumeroDistribuidor;
	}

	public void setNossoNumeroDistribuidor(String nossoNumeroDistribuidor) {
		this.nossoNumeroDistribuidor = nossoNumeroDistribuidor;
	}

	public String getDigitoNossoNumeroDistribuidor() {
		return digitoNossoNumeroDistribuidor;
	}

	public void setDigitoNossoNumeroDistribuidor(
			String digitoNossoNumeroDistribuidor) {
		this.digitoNossoNumeroDistribuidor = digitoNossoNumeroDistribuidor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	
}
