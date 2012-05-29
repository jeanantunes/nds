package br.com.abril.nds.model.dne;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_LOGRADOUROS_NUM_LOTE")
public class Lote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686057435538527825L;

	@Id
	@SequenceGenerator(name="DNE_GU_LOGRADOUROS_NUM_LOTE_ID_GENERATOR", sequenceName="DNE_GU_LOGRADOUROS_NUM_LOTE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DNE_GU_LOGRADOUROS_NUM_LOTE_ID_GENERATOR")
	@Column(name="CHAVE_LOT_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="CEP_LOTE", length=8)
	private String cep;

	@Column(name="NUM_LOT_DNE", length=11)
	private String numero;

	@OneToMany(mappedBy="dneGuLogradourosNumLote")
	private List<Complemento> complementos;

    @ManyToOne
	@JoinColumn(name="CHAVE_LOGRADOURO_DNE", nullable=false)
	private Logradouro logradouro;

    public Lote() {
    }

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return the complementos
	 */
	public List<Complemento> getComplementos() {
		return complementos;
	}

	/**
	 * @param complementos the complementos to set
	 */
	public void setComplementos(List<Complemento> complementos) {
		this.complementos = complementos;
	}

	/**
	 * @return the logradouro
	 */
	public Logradouro getLogradouro() {
		return logradouro;
	}

	/**
	 * @param logradouro the logradouro to set
	 */
	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	
}