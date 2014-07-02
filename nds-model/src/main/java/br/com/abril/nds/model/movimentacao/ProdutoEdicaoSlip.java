package br.com.abril.nds.model.movimentacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//DTO transformado em entidade para melhorar desempenho na impressão do slip + boleto em massa
//foi evitado qualquer tipo de relacionamento que poderia comprometer o desempenho da funcionalidade
//evite usar essa entidade para outras consultas

@Entity
@Table(name="PRODUTO_EDICAO_SLIP")
@SequenceGenerator(name="PRODUTO_EDICAO_SLIP_SEQ", initialValue = 1, allocationSize = 1)
public class ProdutoEdicaoSlip implements Serializable {

	private static final long serialVersionUID = 9175726845943514469L;
	
	@Id
    @Column(name="ID")
    @GeneratedValue(generator = "PRODUTO_EDICAO_SLIP_SEQ")
	private Long id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="SLIP_ID")
	private Slip slip;

	@Column(name="ID_CHAMADA_ENCALHE")
	private Long idChamadaEncalhe;
	
	@Column(name="NOME_PRODUTO")
	private String nomeProduto;
	
	@Column(name="ID_PRODUTO_EDICAO")
	private Long idProdutoEdicao;
	
	@Column(name="NUMERO_EDICAO")
	private Long numeroEdicao;
	
	@Column(name="REPARTE")
	private BigInteger reparte;
	
	@Column(name="ENCALHE")
	private BigInteger encalhe;
	
	@Column(name="QUANTIDADE_EFETIVA")
	private BigInteger quantidadeEfetiva;
	
	@Column(name="PRECO_VENDA", precision=18, scale=4)
	private BigDecimal precoVenda;
	
	@Column(name="VALOR_TOTAL", precision=18, scale=4)
	private BigDecimal valorTotal;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_RECOLHIMENTO_DISTRIBUIDOR")
	private Date dataRecolhimentoDistribuidor;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_OPERACAO")
	private Date dataOperacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_RECOLHIMENTO")
	private Date dataRecolhimento;
	
	@Column(name="ORDINAL_DIA_CONFERENCIA_ENCALHE")
	private String ordinalDiaConferenciaEncalhe;
	
	@Column(name="QTDE_TOTAL_PRODUTOS")
	private String qtdeTotalProdutos;
	
	@Column(name="VALOR_TOTAL_ENCALHE")
	private String valorTotalEncalhe;
	
	@Column(name="DIA")
	private Integer dia;
	
	/**
	 * Obtém nomeProduto
	 *
	 * @return String
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * Atribuí nomeProduto
	 * @param nomeProduto 
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * Obtém numeroEdicao
	 *
	 * @return Long
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * Atribuí numeroEdicao
	 * @param numeroEdicao 
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * Obtém reparte
	 *
	 * @return BigInteger
	 */
	public BigInteger getReparte() {
		return reparte;
	}

	/**
	 * Atribuí reparte
	 * @param reparte 
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	/**
	 * Obtém encalhe
	 *
	 * @return BigInteger
	 */
	public BigInteger getEncalhe() {
		return encalhe;
	}

	/**
	 * Atribuí encalhe
	 * @param encalhe 
	 */
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	
	/**
	 * Obtém quantidadeEfetiva
	 *
	 * @return BigInteger
	 */
	public BigInteger getQuantidadeEfetiva() {
		return quantidadeEfetiva;
	}
	
	/**
	 * Atribuí quantidadeEfetiva
	 * @param quantidadeEfetiva 
	 */
	public void setQuantidadeEfetiva(BigInteger quantidadeEfetiva) {
		this.quantidadeEfetiva = quantidadeEfetiva;
	}

	/**
	 * Obtém precoVenda
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * Atribuí precoVenda
	 * @param precoVenda 
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * Obtém valorTotal
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * Atribuí valorTotal
	 * @param valorTotal 
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * Obtém dataRecolhimentoDistribuidor
	 *
	 * @return Date
	 */
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}

	/**
	 * Atribuí dataRecolhimentoDistribuidor
	 * @param dataRecolhimentoDistribuidor 
	 */
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}

	/**
	 * Obtém idProdutoEdicao
	 *
	 * @return Long
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * Atribuí idProdutoEdicao
	 * @param idProdutoEdicao 
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
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
	 * @return the ordinalDiaConferenciaEncalhe
	 */
	public String getOrdinalDiaConferenciaEncalhe() {
		return ordinalDiaConferenciaEncalhe;
	}

	/**
	 * @param ordinalDiaConferenciaEncalhe the ordinalDiaConferenciaEncalhe to set
	 */
	public void setOrdinalDiaConferenciaEncalhe(String ordinalDiaConferenciaEncalhe) {
		this.ordinalDiaConferenciaEncalhe = ordinalDiaConferenciaEncalhe;
	}

	/**
	 * @return the qtdeTotalProdutos
	 */
	public String getQtdeTotalProdutos() {
		return qtdeTotalProdutos;
	}

	/**
	 * @param qtdeTotalProdutos the qtdeTotalProdutos to set
	 */
	public void setQtdeTotalProdutos(String qtdeTotalProdutos) {
		this.qtdeTotalProdutos = qtdeTotalProdutos;
	}

	/**
	 * @return the valorTotalEncalhe
	 */
	public String getValorTotalEncalhe() {
		return valorTotalEncalhe;
	}

	/**
	 * @param valorTotalEncalhe the valorTotalEncalhe to set
	 */
	public void setValorTotalEncalhe(String valorTotalEncalhe) {
		this.valorTotalEncalhe = valorTotalEncalhe;
	}

	/**
	 * @return the idChamadaEncalhe
	 */
	public Long getIdChamadaEncalhe() {
		return idChamadaEncalhe;
	}

	/**
	 * @param idChamadaEncalhe the idChamadaEncalhe to set
	 */
	public void setIdChamadaEncalhe(Long idChamadaEncalhe) {
		this.idChamadaEncalhe = idChamadaEncalhe;
	}

	/**
	 * @return the dia
	 */
	public Integer getDia() {
		return dia;
	}

	/**
	 * @param dia the dia to set
	 */
	public void setDia(Integer dia) {
		this.dia = dia;
	}

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public Slip getSlip() {
        return slip;
    }

    
    public void setSlip(Slip slip) {
        this.slip = slip;
    }

}