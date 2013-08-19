package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_LICENCA_MUNICIPAL")
@SequenceGenerator(name="TIPO_LICENCA_MUNICIPAL_SEQ", initialValue = 1, allocationSize = 1)
public class TipoLicencaMunicipal extends CodigoDescricao {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "TIPO_LICENCA_MUNICIPAL_SEQ")
	private Long id; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
