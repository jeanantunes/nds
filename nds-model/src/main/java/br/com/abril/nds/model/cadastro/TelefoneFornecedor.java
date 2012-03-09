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
@Table(name = "TELEFONE_FORNECEDOR")
@SequenceGenerator(name="TELEFONE_FORN_SEQ", initialValue = 1, allocationSize = 1)
public class TelefoneFornecedor extends AssociacaoTelefone {

	@Id
	@GeneratedValue(generator = "TELEFONE_FORN_SEQ")
	@Column(name = "ID")
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
}