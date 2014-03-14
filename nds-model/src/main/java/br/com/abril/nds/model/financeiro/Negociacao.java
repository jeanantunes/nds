package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.FormaCobranca;

@Entity
@SequenceGenerator(name = "NEGOCIACAO_SEQ", initialValue = 1, allocationSize = 1)
@Table(name = "NEGOCIACAO")
public class Negociacao {

	@Id
	@GeneratedValue(generator = "NEGOCIACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToMany
	@JoinTable(name = "NEGOCIACAO_COBRANCA_ORIGINARIA", 
		joinColumns = {@JoinColumn(name = "NEGOCIACAO_ID")}, 
		inverseJoinColumns = {@JoinColumn(name = "COBRANCA_ID")})
	private List<Cobranca> cobrancasOriginarias;
	
	@OneToMany(mappedBy="negociacao")
	@OrderBy("dataVencimento asc")
	private List<ParcelaNegociacao> parcelas;
	
	@Column(name = "COMISSAO_PARA_SALDO_DIVIDA", precision=18, scale=4)
	private BigDecimal comissaoParaSaldoDivida;
	
	@Column(name = "COMISSAO_ORIGINAL_COTA", precision=18, scale=4)
	private BigDecimal comissaoOriginalCota;
	
	@Column(name = "NEGOCIACAO_AVULSA")
	private boolean negociacaoAvulsa;
	
	@Column(name = "ISENTA_ENCARGOS")
	private boolean isentaEncargos;
	
	@OneToOne
	@JoinColumn(name = "FORMA_COBRANCA_ID")
	private FormaCobranca formaCobranca;
	
	@Column(name = "ATIVAR_PAGAMENTO_APOS_PARCELA")
	private Integer ativarCotaAposParcela;
	
	@Column(name = "VALOR_DIVIDA_PAGA_COMISSAO", precision=18, scale=4)
	private BigDecimal valorDividaPagaComissao;
	
	@Column(name = "VALOR_ORIGINAL", precision=18, scale=4)
	private BigDecimal valorOriginal;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO")
	private Date dataCriacao;
	
	@OneToMany
	@JoinTable(name="NEGOCIACAO_MOV_FINAN",
			joinColumns = @JoinColumn(name="NEGOCIACAO_ID"),
			inverseJoinColumns = @JoinColumn(name="MOV_FINAN_ID"))
	private List<MovimentoFinanceiroCota> movimentosFinanceiroCota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cobranca> getCobrancasOriginarias() {
		return cobrancasOriginarias;
	}

	public void setCobrancasOriginarias(List<Cobranca> cobrancasOriginarias) {
		this.cobrancasOriginarias = cobrancasOriginarias;
	}

	public List<ParcelaNegociacao> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelaNegociacao> parcelas) {
		this.parcelas = parcelas;
	}

	public BigDecimal getComissaoParaSaldoDivida() {
		return comissaoParaSaldoDivida;
	}

	public void setComissaoParaSaldoDivida(BigDecimal comissaoParaSaldoDivida) {
		this.comissaoParaSaldoDivida = comissaoParaSaldoDivida;
	}

	/**
	 * @return the comissaoOriginalCota
	 */
	public BigDecimal getComissaoOriginalCota() {
		return comissaoOriginalCota;
	}

	/**
	 * @param comissaoOriginalCota the comissaoOriginalCota to set
	 */
	public void setComissaoOriginalCota(BigDecimal comissaoOriginalCota) {
		this.comissaoOriginalCota = comissaoOriginalCota;
	}

	public boolean isNegociacaoAvulsa() {
		return negociacaoAvulsa;
	}

	public void setNegociacaoAvulsa(boolean negociacaoAvulsa) {
		this.negociacaoAvulsa = negociacaoAvulsa;
	}

	public boolean isIsentaEncargos() {
		return isentaEncargos;
	}

	public void setIsentaEncargos(boolean isentaEncargos) {
		this.isentaEncargos = isentaEncargos;
	}

	public FormaCobranca getFormaCobranca() {
		return formaCobranca;
	}

	public void setFormaCobranca(FormaCobranca formaCobranca) {
		this.formaCobranca = formaCobranca;
	}

	public Integer getAtivarCotaAposParcela() {
		return ativarCotaAposParcela;
	}

	public void setAtivarCotaAposParcela(Integer ativarCotaAposParcela) {
		this.ativarCotaAposParcela = ativarCotaAposParcela;
	}

	public BigDecimal getValorDividaPagaComissao() {
		return valorDividaPagaComissao;
	}

	public void setValorDividaPagaComissao(BigDecimal valorDividaPagaComissao) {
		this.valorDividaPagaComissao = valorDividaPagaComissao;
	}

	/**
	 * @return the valorOriginal
	 */
	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	/**
	 * @param valorOriginal the valorOriginal to set
	 */
	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<MovimentoFinanceiroCota> getMovimentosFinanceiroCota() {
		return movimentosFinanceiroCota;
	}

	public void setMovimentosFinanceiroCota(
			List<MovimentoFinanceiroCota> movimentosFinanceiroCota) {
		this.movimentosFinanceiroCota = movimentosFinanceiroCota;
	}
}