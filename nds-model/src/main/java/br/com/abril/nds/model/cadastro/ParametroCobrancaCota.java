package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETRO_COBRANCA_COTA")
@SequenceGenerator(name="PARAMETRO_COBRANCA_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroCobrancaCota implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2004849009218977821L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "PARAMETRO_COBRANCA_COTA_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "FORMA_COBRANCA_ID")
	private FormaCobranca formaCobranca;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "RECEBE_COBRANCA_EMAIL")
	private boolean recebeCobrancaEmail;
	
	@Column(name = "VALOR_MINIMO_COBRANCA")
	private BigDecimal valorMininoCobranca;
	
	@Column(name = "FATOR_VENCIMENTO")
	private int fatorVencimento;
	
	@Embedded
	private PoliticaSuspensao politicaSuspensao;
	
	@OneToMany
	private Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota = new HashSet<ConcentracaoCobrancaCota>();
	
	@Embedded
	private ContaBancaria contaBancariaCota;
	
	/**
	 * Flag indicando se a cobrança é unificada por
	 * Fornecedor
	 */
	@Column(name = "UNIFICA_COBRANCA_FORNECEDOR", nullable = false)
	private boolean unificaCobrancaFornecedor;
	
	/**
	 * Fornecedores para unificação das cobranças
	 */
	@OneToMany
	@JoinTable(name = "PARAM_COBRANCA_COTA_FORNECEDOR", joinColumns = {@JoinColumn(name = "PARAM_COBRANCA_COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FormaCobranca getFormaCobranca() {
		return formaCobranca;
	}

	public void setFormaCobranca(FormaCobranca formaCobranca) {
		this.formaCobranca = formaCobranca;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public boolean isRecebeCobrancaEmail() {
		return recebeCobrancaEmail;
	}

	public void setRecebeCobrancaEmail(boolean recebeCobrancaEmail) {
		this.recebeCobrancaEmail = recebeCobrancaEmail;
	}

	public BigDecimal getValorMininoCobranca() {
		return valorMininoCobranca;
	}

	public void setValorMininoCobranca(BigDecimal valorMininoCobranca) {
		this.valorMininoCobranca = valorMininoCobranca;
	}

	public int getFatorVencimento() {
		return fatorVencimento;
	}

	public void setFatorVencimento(int fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}
	
	public PoliticaSuspensao getPoliticaSuspensao() {
		return politicaSuspensao;
	}
	
	public void setPoliticaSuspensao(PoliticaSuspensao politicaSuspensao) {
		this.politicaSuspensao = politicaSuspensao;
	}
	
	public Set<ConcentracaoCobrancaCota> getConcentracaoCobrancaCota() {
		return concentracaoCobrancaCota;
	}
	
	public void setConcentracaoCobrancaCota(
			Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota) {
		this.concentracaoCobrancaCota = concentracaoCobrancaCota;
	}
	
	public ContaBancaria getContaBancariaCota() {
		return contaBancariaCota;
	}
	
	public void setContaBancariaCota(ContaBancaria contaBancariaCota) {
		this.contaBancariaCota = contaBancariaCota;
	}
	
	public boolean isUnificaCobrancaFornecedor() {
		return unificaCobrancaFornecedor;
	}
	
	public void setUnificaCobrancaFornecedor(boolean unificaCobrancaFornecedor) {
		this.unificaCobrancaFornecedor = unificaCobrancaFornecedor;
	}
	
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

}
