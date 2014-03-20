package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TRIBUTACAO")
public class Tributacao implements Serializable {

	public enum TributacaoTipoOperacao {
		
		ENTRADA("Entrada"),
		SAIDA("Saída"),
		ENTRADA_SAIDA("Entrada / Saída");
		
		private String descricao;
		
		TributacaoTipoOperacao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4671205416746773116L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="tributo")
	private String tributo;
	
	@Column(name="base_calculo")
	private BigDecimal baseCalculo;
	
	@Column(name="cst_a")
	private String cstA;
	
	@Column(name="cst")
	private String cst;
	
	@Column(name="valor_aliquota")
	private BigDecimal valorAliquota;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_operacao")
	private TributacaoTipoOperacao tipoOperacao;
	
	@Column(name="isento_nao_tributado")
	private boolean isentoOuNaoTributado;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTributo() {
		return tributo;
	}

	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public String getCstA() {
		return cstA;
	}

	public void setCstA(String cstA) {
		this.cstA = cstA;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}

	public BigDecimal getValorAliquota() {
		return valorAliquota;
	}

	public void setValorAliquota(BigDecimal valorAliquota) {
		this.valorAliquota = valorAliquota;
	}

	public TributacaoTipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(TributacaoTipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public boolean isIsentoOuNaoTributado() {
		return isentoOuNaoTributado;
	}

	public void setIsentoOuNaoTributado(boolean isentoOuNaoTributado) {
		this.isentoOuNaoTributado = isentoOuNaoTributado;
	}
	
}