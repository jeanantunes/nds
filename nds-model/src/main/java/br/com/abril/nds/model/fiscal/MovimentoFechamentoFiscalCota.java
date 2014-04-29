package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_COTA")
@DiscriminatorValue(value="COTA")
public class MovimentoFechamentoFiscalCota extends MovimentoFechamentoFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "COTA_ID")
	private Cota cota;

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

}