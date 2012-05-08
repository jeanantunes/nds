package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**: Forma de recebimento da cota – informação será atualizada conforme cadastro de 
 * serviços de entrega (EMS em desenvolvimento)
 * 
 * @author guilherme.morais
 *
 */
@Entity
@Table(name = "TIPO_ENTREGA")
@SequenceGenerator(name="TP_ENTREGA_SEQ", initialValue = 1, allocationSize = 1)
public class TipoEntrega implements Serializable  {
	
	private static final long serialVersionUID = 7761256033518939114L;
	
	
	@Id
	@GeneratedValue(generator = "TP_ENTREGA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
