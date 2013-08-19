package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade para os endereços cadastrados do entregador
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "ENDERECO_ENTREGADOR")
@SequenceGenerator(name="ENDERECO_ENTREGADOR_SEQ", initialValue = 1, allocationSize = 1)
public class EnderecoEntregador extends AssociacaoEndereco {
	
	@Id
	@GeneratedValue(generator = "ENDERECO_ENTREGADOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ENTREGADOR_ID")
	private Entregador entregador;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Entregador getEntregador() {
		return entregador;
	}
	
	public void setEntregador(Entregador entregador) {
		this.entregador = entregador;
	}

}
