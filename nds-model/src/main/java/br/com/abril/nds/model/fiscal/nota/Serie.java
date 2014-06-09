package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_SERIE")
public class Serie implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5220063198306740536L;

	@Id
	private Integer id;
	
	@Column(length=9, name="CURRENT_VALUE")
	private Long currentValue;
	
	public Serie() {
	}
	public Serie(Integer id, Long currentValue) {
		super();
		this.id = id;
		this.currentValue = currentValue;
	}




	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the currentValue
	 */
	public Long getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(Long currentValue) {
		this.currentValue = currentValue;
	}
	
	public Long next(){
		return ++this.currentValue;
	}
	

}
