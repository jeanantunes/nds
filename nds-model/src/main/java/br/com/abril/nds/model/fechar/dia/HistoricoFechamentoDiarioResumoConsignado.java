package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_RESUMO_CONSIGNADO")
@SequenceGenerator(name = "HISTORICO_FECHAMENTO_DIARIO_RESUMO_CONSIGNADO_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioResumoConsignado implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_RESUMO_CONSIGNADO_SEQ")
    @Column(name = "ID")
    private Long id;
		
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@Column(name="TIPO_VALOR")
	@Enumerated(EnumType.STRING)
	private TipoValor tipoValor;
	
	@Column(name="VALOR_CONSIGNADO")
	private BigDecimal valorConsignado;
	
	@Column(name="VALOR_AVISTA")
	private BigDecimal valorAvista;
	
	public enum TipoValor{
		SALDO_ANTERIOR,SALDO_ATUAL,ENTRADA,SAIDA;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}

	public TipoValor getTipoValor() {
		return tipoValor;
	}

	public void setTipoValor(TipoValor tipoValor) {
		this.tipoValor = tipoValor;
	}

	public BigDecimal getValorConsignado() {
		return valorConsignado;
	}

	public void setValorConsignado(BigDecimal valorConsignado) {
		this.valorConsignado = valorConsignado;
	}

	public BigDecimal getValorAvista() {
		return valorAvista;
	}

	public void setValorAvista(BigDecimal valorAvista) {
		this.valorAvista = valorAvista;
	}
}
