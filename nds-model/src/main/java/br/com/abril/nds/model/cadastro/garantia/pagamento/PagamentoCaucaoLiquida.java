package br.com.abril.nds.model.cadastro.garantia.pagamento;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PAGAMENTO_CAUCAO_LIQUIDA")
@SequenceGenerator(name="PAGAMENTO_CAUCAO_LIQUIDA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public class PagamentoCaucaoLiquida implements Serializable {
	
	private static final long serialVersionUID = -688816379447249238L;

	@Id
	@GeneratedValue(generator="PAGAMENTO_CAUCAO_LIQUIDA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name="VALOR")
	private BigDecimal valor;

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
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
