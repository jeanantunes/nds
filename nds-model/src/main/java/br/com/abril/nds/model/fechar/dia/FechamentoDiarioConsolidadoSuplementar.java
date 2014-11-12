package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioConsolidadoSuplementar implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_SEQ")
    @Column(name = "ID")
    private Long id;

	@Column(name = "VALOR_ESTOQUE_LOGICO")
	private BigDecimal valorEstoqueLogico;
	
	@Column(name = "VALOR_TRANSFERENCIA")
	private BigDecimal valorTransferencia;
	
	@Column(name = "VALOR_VENDAS")	
	private BigDecimal valorVendas;
	
	@Column(name = "VALOR_ALTERACAO_PRECO")	
	private BigDecimal valorAlteracaoPreco;
	
	@Column(name = "VALOR_SALDO")
	private BigDecimal valorSaldo;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@OneToMany(mappedBy = "fechamentoDiarioConsolidadoSuplementar")
	private List<FechamentoDiarioLancamentoSuplementar> lancamentosSuplementar;
	
	@OneToMany(mappedBy = "fechamentoDiarioConsolidadoSuplementar")
	private List<FechamentoDiarioMovimentoVendaSuplementar> movimentoVendaSuplemetares;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorEstoqueLogico() {
		return valorEstoqueLogico;
	}

	public void setValorEstoqueLogico(BigDecimal valorEstoqueLogico) {
		this.valorEstoqueLogico = valorEstoqueLogico;
	}

	public BigDecimal getValorTransferencia() {
		return valorTransferencia;
	}

	public void setValorTransferencia(BigDecimal valorTransferencia) {
		this.valorTransferencia = valorTransferencia;
	}

	public BigDecimal getValorVendas() {
		return valorVendas;
	}

	public void setValorVendas(BigDecimal valorVendas) {
		this.valorVendas = valorVendas;
	}
	
	public BigDecimal getValorAlteracaoPreco() {
		return valorAlteracaoPreco;
	}

	public void setValorAlteracaoPreco(BigDecimal valorAlteracaoPreco) {
		this.valorAlteracaoPreco = valorAlteracaoPreco;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}

	public List<FechamentoDiarioLancamentoSuplementar> getLancamentosSuplementar() {
		return lancamentosSuplementar;
	}

	public void setLancamentosSuplementar(
			List<FechamentoDiarioLancamentoSuplementar> lancamentosSuplementar) {
		this.lancamentosSuplementar = lancamentosSuplementar;
	}

	public List<FechamentoDiarioMovimentoVendaSuplementar> getmovimentoVendaSuplemetares() {
		return movimentoVendaSuplemetares;
	}

	public void setMovimentoVendaSuplemetares(
			List<FechamentoDiarioMovimentoVendaSuplementar> movimentoVendaSuplemetares) {
		this.movimentoVendaSuplemetares = movimentoVendaSuplemetares;
	}

	
	
}
