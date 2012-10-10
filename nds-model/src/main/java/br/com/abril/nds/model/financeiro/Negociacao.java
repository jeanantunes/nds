package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	
	@OneToMany
	private List<ParcelaNegociacao> parcelas;
	
	@Column(name = "COMISSAO_PARA_SALDO_DIVIDA")
	private BigDecimal comissaoParaSaldoDivida;
	
	@Column(name = "NEGOCIACAO_AVULSA")
	private boolean negociacaoAvulsa;
	
	@Column(name = "ISENTA_ENCARGOS")
	private boolean isentaEncargos;
	
	@OneToOne
	@JoinColumn(name = "FORMA_COBRANCA_ID")
	private FormaCobranca formaCobranca;
	
	@Column(name = "ATIVAR_PAGAMENTO_APOS_PARCELA")
	private Integer ativarCotaAposParcela;
	
	@Column(name = "VALOR_DIVIDA_PAGA_COMISSAO")
	private BigDecimal valorDividaPagaComissao;

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
}