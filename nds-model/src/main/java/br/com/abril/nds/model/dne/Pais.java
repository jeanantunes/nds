package br.com.abril.nds.model.dne;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;



/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_PAISES")
public class Pais implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6731085702979436569L;

	@Id
	@Column(name="SIGLA_PAIS_1", unique=true, nullable=false, length=2)
	private String sigla;

	@Column(name="ABREV_PAIS_PORTUGUES_ECT", length=36)
	private String abreviaturaPT;

	@Column(name="NOME_PAIS_FRANCES", length=72)
	private String nomePaisFrances;

	@Column(name="NOME_PAIS_INGLES", length=72)
	private String nomePaisIngles;

	@Column(name="NOME_PAIS_PORTUGUES", length=72)
	private String nomePaisPortugues;

	@Column(name="SIGLA_PAIS_2", length=3)
	private String sigla2;

	@OneToMany(mappedBy="pais")
	private List<UnidadeFederacao> unidadesFederacoes;

    public Pais() {
    }

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * Abreviatura em português
	 * @return the abreviaturaPT
	 */
	public String getAbreviaturaPT() {
		return abreviaturaPT;
	}

	/**
	 * Abreviatura em português
	 * @param abreviaturaPT the abreviaturaPT to set
	 */
	public void setAbreviaturaPT(String abreviaturaPT) {
		this.abreviaturaPT = abreviaturaPT;
	}

	/**
	 * Nome do País em francês
	 * @return the nomePaisFrances
	 */
	public String getNomePaisFrances() {
		return nomePaisFrances;
	}

	/**
	 * Nome do País em francês
	 * @param nomePaisFrances the nomePaisFrances to set
	 */
	public void setNomePaisFrances(String nomePaisFrances) {
		this.nomePaisFrances = nomePaisFrances;
	}

	/**
	 * Nome do País em inglês
	 * @return the nomePaisIngles
	 */
	public String getNomePaisIngles() {
		return nomePaisIngles;
	}

	/**
	 * Nome do País em inglês
	 * @param nomePaisIngles the nomePaisIngles to set
	 */
	public void setNomePaisIngles(String nomePaisIngles) {
		this.nomePaisIngles = nomePaisIngles;
	}

	/**
	 * Nome do País em português
	 * @return the nomePaisPortugues
	 */
	public String getNomePaisPortugues() {
		return nomePaisPortugues;
	}

	/**
	 * Nome do País em português
	 * @param nomePaisPortugues the nomePaisPortugues to set
	 */
	public void setNomePaisPortugues(String nomePaisPortugues) {
		this.nomePaisPortugues = nomePaisPortugues;
	}

	/**
	 * Sigla do País com 3 characteres<br>
	 * Exemplo:<b>BRA</b>
	 * @return the sigla2
	 */
	public String getSigla2() {
		return sigla2;
	}

	/**
	 * Sigla do País com 3 characteres<br>
	 * Exemplo:<b>BRA</b>
	 * @param sigla2 the sigla2 to set
	 */
	public void setSigla2(String sigla2) {
		this.sigla2 = sigla2;
	}

	/**
	 * @return the unidadesFederacoes
	 */
	public List<UnidadeFederacao> getUnidadesFederacoes() {
		return unidadesFederacoes;
	}

	/**
	 * @param unidadesFederacoes the unidadesFederacoes to set
	 */
	public void setUnidadesFederacoes(List<UnidadeFederacao> unidadesFederacoes) {
		this.unidadesFederacoes = unidadesFederacoes;
	}


	
}