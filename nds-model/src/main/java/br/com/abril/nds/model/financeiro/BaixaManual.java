package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@DiscriminatorValue(value = "MANUAL")
public class BaixaManual extends BaixaCobranca {

	@ManyToOne(optional = true)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@Column(name = "VALOR_JUROS", nullable = true)
	private BigDecimal valorJuros;

	@Column(name = "VALOR_MULTA", nullable = true)
	private BigDecimal valorMulta;
	
	@Column(name = "VALOR_DESCONTO", nullable = true)
	private BigDecimal valorDesconto;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_APROVACAO")
	private StatusAprovacao statusAprovacao;
	
	@Column(name = "OBSERVACAO")
	private String observacao;
	
	
	
	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public StatusAprovacao getStatusAprovacao() {
		return statusAprovacao;
	}

	public void setStatusAprovacao(StatusAprovacao statusAprovacao) {
		this.statusAprovacao = statusAprovacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
