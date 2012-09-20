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

/**
 * Modelo que representa um serviço de entrega.
 * 
 * @author Discover Technology
 *
 */
@Entity
@Table(name = "TIPO_ENTREGA")
@SequenceGenerator(name="TP_ENTREGA_SEQ", initialValue = 1, allocationSize = 1)
public class TipoEntrega implements Serializable  {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7761256033518939114L;
	
	@Id
	@GeneratedValue(generator = "TP_ENTREGA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DESCRICAO_TIPO_ENTREGA", nullable = false)
	private DescricaoTipoEntrega descricaoTipoEntrega;
	
	@Column(name = "TAXA_FIXA", nullable = true)
	private BigDecimal taxaFixa;
	
	@Column(name = "PERCENTUAL_FATURAMENTO", nullable = true)
	private Float percentualFaturamento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "BASE_CALCULO", nullable = true)
	private BaseCalculo baseCalculo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERIODICIDADE", nullable = false)
	private Periodicidade periodicidade;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DIA_SEMANA", nullable = true)
	private DiaSemana diaSemana;
	
	@Column(name = "DIA_MES", nullable = true, length = 2)
	private Integer diaMes;
	
	/**
	 * Construtor.
	 */
	public TipoEntrega() {
		
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the descricaoTipoEntrega
	 */
	public DescricaoTipoEntrega getDescricaoTipoEntrega() {
		return descricaoTipoEntrega;
	}

	/**
	 * @param descricaoTipoEntrega the descricaoTipoEntrega to set
	 */
	public void setDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		this.descricaoTipoEntrega = descricaoTipoEntrega;
	}

	/**
	 * @return the taxaFixa
	 */
	public BigDecimal getTaxaFixa() {
		return taxaFixa;
	}

	/**
	 * @param taxaFixa the taxaFixa to set
	 */
	public void setTaxaFixa(BigDecimal taxaFixa) {
		this.taxaFixa = taxaFixa;
	}

	/**
	 * @return the percentualFaturamento
	 */
	public Float getPercentualFaturamento() {
		return percentualFaturamento;
	}

	/**
	 * @param percentualFaturamento the percentualFaturamento to set
	 */
	public void setPercentualFaturamento(Float percentualFaturamento) {
		this.percentualFaturamento = percentualFaturamento;
	}

	/**
	 * @return the baseCalculo
	 */
	public BaseCalculo getBaseCalculo() {
		return baseCalculo;
	}

	/**
	 * @param baseCalculo the baseCalculo to set
	 */
	public void setBaseCalculo(BaseCalculo baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	/**
	 * @return the periodicidade
	 */
	public Periodicidade getPeriodicidade() {
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(Periodicidade periodicidade) {
		this.periodicidade = periodicidade;
	}

	/**
	 * @return the diaSemana
	 */
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	/**
	 * @param diaSemana the diaSemana to set
	 */
	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * @return the diaMes
	 */
	public Integer getDiaMes() {
		return diaMes;
	}

	/**
	 * @param diaMes the diaMes to set
	 */
	public void setDiaMes(Integer diaMes) {
		this.diaMes = diaMes;
	}

}
