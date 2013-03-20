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
@Table(name = "FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioConsolidadoReparte implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_SEQ")
    @Column(name = "ID")
    private Long id;

	@Column(name = "VALOR_REPARTE")
	private BigDecimal valorReparte;
	
	@Column(name = "VALOR_SOBRAS")
	private BigDecimal valorSobras;
	
	@Column(name = "VALOR_FALTAS")	
	private BigDecimal valorFaltas;
	
	@Column(name = "VALOR_TRANSFERIDO")
	private BigDecimal valorTransferido;
	
	@Column(name = "VALOR_DISTRIBUIDO")
	private BigDecimal valorDistribuido;
	
	@Column(name = "SOBRA_DISTRIBUIDA")
	private BigDecimal valorSobraDistribuida;
	
	@Column(name = "VALOR_DIFERENCA")
	private BigDecimal valorDiferenca;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@OneToMany(mappedBy = "fechamentoDiarioConsolidadoReparte")
	private List<FechamentoDiarioLancamentoReparte> lancamentosReparte;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorReparte() {
		return valorReparte;
	}

	public void setValorReparte(BigDecimal valorReparte) {
		this.valorReparte = valorReparte;
	}

	public BigDecimal getValorSobras() {
		return valorSobras;
	}

	public void setValorSobras(BigDecimal valorSobras) {
		this.valorSobras = valorSobras;
	}

	public BigDecimal getValorFaltas() {
		return valorFaltas;
	}

	public void setValorFaltas(BigDecimal valorFaltas) {
		this.valorFaltas = valorFaltas;
	}

	public BigDecimal getValorTransferido() {
		return valorTransferido;
	}

	public void setValorTransferido(BigDecimal valorTransferido) {
		this.valorTransferido = valorTransferido;
	}

	public BigDecimal getValorDistribuido() {
		return valorDistribuido;
	}

	public void setValorDistribuido(BigDecimal valorDistribuido) {
		this.valorDistribuido = valorDistribuido;
	}


	public BigDecimal getValorSobraDistribuida() {
		return valorSobraDistribuida;
	}

	public void setValorSobraDistribuida(BigDecimal valorSobraDistribuida) {
		this.valorSobraDistribuida = valorSobraDistribuida;
	}

	public BigDecimal getValorDiferenca() {
		return valorDiferenca;
	}

	public void setValorDiferenca(BigDecimal valorDiferenca) {
		this.valorDiferenca = valorDiferenca;
	}

	public List<FechamentoDiarioLancamentoReparte> getLancamentosReparte() {
		return lancamentosReparte;
	}

	public void setLancamentosReparte(
			List<FechamentoDiarioLancamentoReparte> lancamentosReparte) {
		this.lancamentosReparte = lancamentosReparte;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}
	
	
}
