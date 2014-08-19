package br.com.abril.nds.model.movimentacao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

//DTO transformado em entidade para melhorar desempenho na impressão do slip + boleto em massa
//foi evitado qualquer tipo de relacionamento que poderia comprometer o desempenho da funcionalidade
//evite usar essa entidade para outras consultas

@Entity
@Table(name="SLIP")
@SequenceGenerator(name="SLIP_SEQ", initialValue = 1, allocationSize = 1)
public class Slip {

    @Id
    @Column(name="ID")
    @GeneratedValue(generator = "SLIP_SEQ")
    private Long id;
    
    @Column(name="NUMERO_COTA", nullable=false)
	private Integer numeroCota;
	
    @Column(name="NOME_COTA")
	private String nomeCota;
	
    @Column(name="VALOR_TOTAL_PAGAR", precision=18, scale=4)
	private BigDecimal valorTotalPagar;
	
    @Column(name="VALOR_TOTAL_ENCALHE", precision=18, scale=4)
	private BigDecimal valorTotalEncalhe;
	
    @Column(name="TOTAL_PRODUTOS")
	private BigInteger totalProdutos;
	
    @Column(name="VALOR_SLIP", precision=18, scale=4)
	private BigDecimal valorSlip;
	
    @Column(name="VALOR_DEVIDO", precision=18, scale=4)
	private BigDecimal valorDevido;
	
    @Column(name="VALOR_ENCALHE_DIA", precision=18, scale=4)
	private BigDecimal valorEncalheDia;
	
    @Column(name="VALOR_LIQUIDO_DEVIDO", precision=18, scale=4)
	private BigDecimal valorLiquidoDevido;
	
    @Column(name="TOTAL_PRODUTO_DIA")
	private BigInteger totalProdutoDia;
	
    @Column(name="CE_JORNALEIRO")
	private Long ceJornaleiro;
	
    @Column(name="DATA_CONFERENCIA", nullable=false)
	private Date dataConferencia;
	
    @Column(name="CODIGO_BOX")
	private String codigoBox;
	
    @Column(name="DESCRICAO_ROTEIRO")
	private String descricaoRoteiro;
	
    @Column(name="DESCRICAO_ROTA")
	private String descricaoRota;
	
    @Column(name="NUM_SLIP")
	private Long numSlip;

    @OneToMany(mappedBy="slip", cascade=CascadeType.ALL)
    private List<ProdutoEdicaoSlip> listaProdutoEdicaoSlip;
	
    @OneToMany(mappedBy="slip", cascade=CascadeType.ALL)
	private List<DebitoCreditoCota> listaComposicaoCobranca;
	
    @OneToMany(mappedBy="slip", cascade=CascadeType.ALL)
	private List<DebitoCreditoCota> listaResumoCobranca;
	
	@Column(name="VALOR_TOTAL_DESCONTO", precision=18, scale=4)
	private BigDecimal valorTotalDesconto;
	
	@Column(name="VALOR_TOTAL_SEM_DESCONTO", precision=18, scale=4)
	private BigDecimal valorTotalSemDesconto;
	
	@Column(name="PAGAMENTO_PENDENTE", precision=18, scale=4)
	private BigDecimal pagamentoPendente;
	
	@Column(name="VALOR_CREDITO_DIF", precision=18, scale=4)
	private BigDecimal valorCreditoDif;
	
	@Column(name="VALOR_TOTAL_REPARTE", precision=18, scale=4)
	private BigDecimal valorTotalReparte;
	
	@Transient
	private Map<String, Object> parametersSlip;
	
	/**
	 * Obtém numeroCota
	 *
	 * @return Integer
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * Atribuí numeroCota
	 * @param numeroCota 
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * Obtém nomeCota
	 *
	 * @return String
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * Atribuí nomeCota
	 * @param nomeCota 
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * Obtém valorTotalPagar
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotalPagar() {
		return valorTotalPagar;
	}

	/**
	 * Atribuí valorTotalPagar
	 * @param valorTotalPagar 
	 */
	public void setValorTotalPagar(BigDecimal valorTotalPagar) {
		this.valorTotalPagar = valorTotalPagar;
	}

	/**
	 * Obtém valorTotalEncalhe
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotalEncalhe() {
		return valorTotalEncalhe;
	}

	/**
	 * Atribuí valorTotalEncalhe
	 * @param valorTotalEncalhe 
	 */
	public void setValorTotalEncalhe(BigDecimal valorTotalEncalhe) {
		this.valorTotalEncalhe = valorTotalEncalhe;
	}

	/**
	 * Obtém totalProdutos
	 *
	 * @return BigInteger
	 */
	public BigInteger getTotalProdutos() {
		return totalProdutos;
	}

	/**
	 * Atribuí totalProdutos
	 * @param totalProdutos 
	 */
	public void setTotalProdutos(BigInteger totalProdutos) {
		this.totalProdutos = totalProdutos;
	}

	/**
	 * Obtém valorSlip
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorSlip() {
		return valorSlip;
	}

	/**
	 * Atribuí valorSlip
	 * @param valorSlip 
	 */
	public void setValorSlip(BigDecimal valorSlip) {
		this.valorSlip = valorSlip;
	}

	/**
	 * Obtém valorDevido
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorDevido() {
		return valorDevido;
	}

	/**
	 * Atribuí valorDevido
	 * @param valorDevido 
	 */
	public void setValorDevido(BigDecimal valorDevido) {
		this.valorDevido = valorDevido;
	}

	/**
	 * Obtém valorEncalheDia
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorEncalheDia() {
		return valorEncalheDia;
	}

	/**
	 * Atribuí valorEncalheDia
	 * @param valorEncalheDia 
	 */
	public void setValorEncalheDia(BigDecimal valorEncalheDia) {
		this.valorEncalheDia = valorEncalheDia;
	}

	/**
	 * Obtém totalProdutoDia
	 *
	 * @return BigInteger
	 */
	public BigInteger getTotalProdutoDia() {
		return totalProdutoDia;
	}

	/**
	 * Atribuí totalProdutoDia
	 * @param totalProdutoDia 
	 */
	public void setTotalProdutoDia(BigInteger totalProdutoDia) {
		this.totalProdutoDia = totalProdutoDia;
	}

	/**
	 * Obtém ceJornaleiro
	 *
	 * @return Long
	 */
	public Long getCeJornaleiro() {
		return ceJornaleiro;
	}

	/**
	 * Atribuí ceJornaleiro
	 * @param ceJornaleiro 
	 */
	public void setCeJornaleiro(Long ceJornaleiro) {
		this.ceJornaleiro = ceJornaleiro;
	}

	/**
	 * Obtém dataConferencia
	 *
	 * @return Date
	 */
	public Date getDataConferencia() {
		return dataConferencia;
	}

	/**
	 * Atribuí dataConferencia
	 * @param dataConferencia 
	 */
	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}

	/**
	 * Obtém codigoBox
	 *
	 * @return String
	 */
	public String getCodigoBox() {
		return codigoBox;
	}

	/**
	 * Atribuí codigoBox
	 * @param codigoBox 
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the descricaoRoteiro
	 */
	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}

	/**
	 * @param descricaoRoteiro the descricaoRoteiro to set
	 */
	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}

	/**
	 * @return the descricaoRota
	 */
	public String getDescricaoRota() {
		return descricaoRota;
	}

	/**
	 * @param descricaoRota the descricaoRota to set
	 */
	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	/**
	 * Obtém numSlip
	 *
	 * @return Long
	 */
	public Long getNumSlip() {
		return numSlip;
	}

	/**
	 * Atribuí numSlip
	 * @param numSlip 
	 */
	public void setNumSlip(Long numSlip) {
		this.numSlip = numSlip;
	}
	
	/**
	 * Obtém listaProdutoEdicaoSlipDTO
	 *
	 * @return List<ProdutoEdicaoSlipDTO>
	 */
	public List<ProdutoEdicaoSlip> getListaProdutoEdicaoSlip() {
		return listaProdutoEdicaoSlip;
	}

	/**
	 * Atribuí listaProdutoEdicaoSlipDTO
	 * @param listaProdutoEdicaoSlipDTO 
	 */
	public void setListaProdutoEdicaoSlip(
			List<ProdutoEdicaoSlip> listaProdutoEdicaoSlip) {
		this.listaProdutoEdicaoSlip = listaProdutoEdicaoSlip;
	}

	/**
	 * @return the listaComposicaoCobrancaDTO
	 */
	public List<DebitoCreditoCota> getListaComposicaoCobranca() {
		return listaComposicaoCobranca;
	}

	/**
	 * @param listaComposicaoCobrancaDTO the listaComposicaoCobrancaDTO to set
	 */
	public void setListaComposicaoCobranca(
			List<DebitoCreditoCota> listaComposicaoCobranca) {
		this.listaComposicaoCobranca = listaComposicaoCobranca;
	}

	public BigDecimal getValorTotalDesconto() {
		return valorTotalDesconto;
	}

	public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	public BigDecimal getValorTotalSemDesconto() {
		return valorTotalSemDesconto;
	}

	public void setValorTotalSemDesconto(BigDecimal valorTotalSemDesconto) {
		this.valorTotalSemDesconto = valorTotalSemDesconto;
	}

	public Map<String, Object> getParametersSlip() {
		return parametersSlip;
	}

	public void setParametersSlip(Map<String, Object> parametersSlip) {
		this.parametersSlip = parametersSlip;
	}

	/**
	 * @return the valorLiquidoDevido
	 */
	public BigDecimal getValorLiquidoDevido() {
		return valorLiquidoDevido;
	}

	/**
	 * @param valorLiquidoDevido the valorLiquidoDevido to set
	 */
	public void setValorLiquidoDevido(BigDecimal valorLiquidoDevido) {
		this.valorLiquidoDevido = valorLiquidoDevido;
	}

	public List<DebitoCreditoCota> getListaResumoCobranca() {
		return listaResumoCobranca;
	}

	public void setListaResumoCobranca(
			List<DebitoCreditoCota> listaResumoCobranca) {
		this.listaResumoCobranca = listaResumoCobranca;
	}

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public BigDecimal getPagamentoPendente() {
        return pagamentoPendente;
    }

    
    public void setPagamentoPendente(BigDecimal pagamentoPendente) {
        this.pagamentoPendente = pagamentoPendente;
    }

    
    public BigDecimal getValorCreditoDif() {
        return valorCreditoDif;
    }

    
    public void setValorCreditoDif(BigDecimal valorCreditoDif) {
        this.valorCreditoDif = valorCreditoDif;
    }

    
    public BigDecimal getValorTotalReparte() {
        return valorTotalReparte;
    }

    
    public void setValorTotalReparte(BigDecimal valorTotalReparte) {
        this.valorTotalReparte = valorTotalReparte;
    }
	
	
}