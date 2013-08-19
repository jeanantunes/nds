package br.com.abril.nds.model.cadastro.pdv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.AssociacaoEndereco;

/**
 * Entidade para associação de endereço ao PDV
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "ENDERECO_PDV")
@SequenceGenerator(name="ENDERECO_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class EnderecoPDV extends AssociacaoEndereco {
	
	@Id
	@GeneratedValue(generator = "ENDERECO_PDV_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PDV_ID")
	private PDV pdv;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public PDV getPdv() {
		return pdv;
	}

	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}
	
}
