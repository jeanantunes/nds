package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TELEFONE_COTA")
@SequenceGenerator(name="TELEFONE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class TelefoneCota extends AssociacaoTelefone {

	@Id
	@GeneratedValue(generator = "TELEFONE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
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