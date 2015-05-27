package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "PARAMETRO_COBRANCA_DISTRIBUICAO_COTA")
@SequenceGenerator(name="PARAMETRO_COBRANCA_DISTRIBUICAO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroCobrancaDistribuicaoCota implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5150610869351056930L;
	
	@Id
	@GeneratedValue(generator = "PARAMETRO_COBRANCA_DISTRIBUICAO_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "MODELIDADE_COBRANCA")
	private ModalidadeCobranca modalidadeCobranca;
	
	@Column(name = "POR_ENTREGA")
	private boolean porEntrega;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERIODICIDADE_COBRANCA")
	private PeriodicidadeCobranca periodicidadeCobranca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DIA_SEMANA")
	private DiaSemana diaSemanaCobranca;
	
	@Column(name = "DIA_COBRANCA")
	private Integer diaCobranca;
	
	@Column(name = "BASE_CALCULO")
    private BaseCalculo baseCalculo;
	
	@Column(name = "TAXA_FIXA", precision=18, scale=4)
	private BigDecimal taxaFixa;
	    
    @Column(name = "PERCENTUAL_FATURAMENTO", precision=18, scale=4)
    private BigDecimal percentualFaturamento;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModalidadeCobranca getModalidadeCobranca() {
		return modalidadeCobranca;
	}

	public void setModalidadeCobranca(ModalidadeCobranca modalidadeCobranca) {
		this.modalidadeCobranca = modalidadeCobranca;
	}

	public boolean isPorEntrega() {
		return porEntrega;
	}

	public void setPorEntrega(boolean porEntrega) {
		this.porEntrega = porEntrega;
	}

	public PeriodicidadeCobranca getPeriodicidadeCobranca() {
		return periodicidadeCobranca;
	}

	public void setPeriodicidadeCobranca(PeriodicidadeCobranca periodicidadeCobranca) {
		this.periodicidadeCobranca = periodicidadeCobranca;
	}

	public Integer getDiaCobranca() {
		return diaCobranca;
	}

	public void setDiaCobranca(Integer diaCobranca) {
		this.diaCobranca = diaCobranca;
	}

	public BaseCalculo getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BaseCalculo baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public BigDecimal getTaxaFixa() {
		return taxaFixa;
	}

	public void setTaxaFixa(BigDecimal taxaFixa) {
		this.taxaFixa = taxaFixa;
	}

	public BigDecimal getPercentualFaturamento() {
		return percentualFaturamento;
	}

	public void setPercentualFaturamento(BigDecimal percentualFaturamento) {
		this.percentualFaturamento = percentualFaturamento;
	}

	public DiaSemana getDiaSemanaCobranca() {
		return diaSemanaCobranca;
	}

	public void setDiaSemanaCobranca(DiaSemana diaSemanaCobranca) {
		this.diaSemanaCobranca = diaSemanaCobranca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroCobrancaDistribuicaoCota other = (ParametroCobrancaDistribuicaoCota) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
	
}