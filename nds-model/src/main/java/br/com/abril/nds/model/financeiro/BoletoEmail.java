package br.com.abril.nds.model.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author luiz.marcili
 * @version 1.0
 * @created 02-mar-2012 09:25:00
 */
@Entity
@Table(name = "BOLETO_EMAIL")
@SequenceGenerator(name="BOLETO_EMAIL_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BoletoEmail {
	
	@Id
	@GeneratedValue(generator = "BOLETO_EMAIL_SEQ")
	@Column(name = "ID")
	protected Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "COBRANCA_ID")
	protected Cobranca cobranca;

	public Cobranca getCobranca() {
		return cobranca;
	}

	public void setCobranca(Cobranca cobranca) {
		this.cobranca = cobranca;
	}
}