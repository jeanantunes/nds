package br.com.abril.nds.model.cadastro;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "DISTRIBUICAO_FORNECEDOR")
@SequenceGenerator(name="DIST_FORN_SEQ", initialValue = 1, allocationSize = 1)
public class DistribuicaoFornecedor {
	
	@Id
	@GeneratedValue(generator = "DIST_FORN_SEQ")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "distribuidor_id")
	private Distribuidor distribuidor;
	@ManyToOne
	@JoinColumn(name = "fornecedor_id")
	private Fornecedor fornecedor;
	@Enumerated(EnumType.STRING)
	private DiaSemana diaSemana;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}
	
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}
	
}
