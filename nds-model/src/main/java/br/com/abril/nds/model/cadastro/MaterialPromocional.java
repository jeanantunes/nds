package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * Entidade que representa os materiais promocionais
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "MATERIAL_PROMOCIONAL")
@SequenceGenerator(name="MATERIAL_PROMOCIONAL_SEQ", initialValue = 1, allocationSize = 1)
public class MaterialPromocional extends CodigoDescricao {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "MATERIAL_PROMOCIONAL_SEQ")
	private Long id; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
