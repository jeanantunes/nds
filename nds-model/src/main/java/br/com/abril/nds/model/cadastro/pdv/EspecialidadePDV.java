package br.com.abril.nds.model.cadastro.pdv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.CodigoDescricao;

/**
 * Entidade que representa a especialidade do PDV
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "ESPECIALIDADE_PDV")
@SequenceGenerator(name="ESPECIALIDADE_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class EspecialidadePDV extends CodigoDescricao {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ESPECIALIDADE_PDV_SEQ")
	private Long id; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
