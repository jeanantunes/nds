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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

	@OneToMany(mappedBy="parametroCobrancaCota")
	private Set<FormaCobranca> formasCobrancaCota = new HashSet<FormaCobranca>();
	
	@OneToOne
	@JoinColumn(name = "COTA_ID", unique = true)
	private Cota cota;
	
	@Column(name = "VALOR_MINIMO_COBRANCA", precision=18, scale=4)
	private BigDecimal valorMininoCobranca;
	
	@Column(name = "FATOR_VENCIMENTO")
	private Integer fatorVencimento;
	
	@Column(name = "UNIFICA_COBRANCA")
	private Boolean unificaCobranca;
	
	@Column(name = "DEVOLVE_ENCALHE")
	private Boolean devolveEncalhe;
	
	@Embedded
	private PoliticaSuspensao politicaSuspensao;
	
	@ManyToOne
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedorPadrao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public BigDecimal getValorMininoCobranca() {
		return valorMininoCobranca;
	}

	public void setValorMininoCobranca(BigDecimal valorMininoCobranca) {
		this.valorMininoCobranca = valorMininoCobranca;
	}

	public Integer getFatorVencimento() {
		return fatorVencimento;
	}

	public void setFatorVencimento(Integer fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}
	
	public PoliticaSuspensao getPoliticaSuspensao() {
		return politicaSuspensao;
	}
	
	public void setPoliticaSuspensao(PoliticaSuspensao politicaSuspensao) {
		this.politicaSuspensao = politicaSuspensao;
	}

	public Set<FormaCobranca> getFormasCobrancaCota() {
		return formasCobrancaCota;
	}

	public void setFormasCobrancaCota(Set<FormaCobranca> formasCobrancaCota) {
		this.formasCobrancaCota = formasCobrancaCota;
	}
	
	public Fornecedor getFornecedorPadrao() {
		return fornecedorPadrao;
	}
	
	public void setFornecedorPadrao(Fornecedor fornecedorPadrao) {
		this.fornecedorPadrao = fornecedorPadrao;
	}

	public Boolean isUnificaCobranca() {
		return unificaCobranca;
	}

	public void setUnificaCobranca(Boolean unificaCobranca) {
		this.unificaCobranca = unificaCobranca;
	}

	public Boolean isDevolveEncalhe() {
		return devolveEncalhe;
	}

	public void setDevolveEncalhe(Boolean devolveEncalhe) {
		this.devolveEncalhe = devolveEncalhe;
	}
}
