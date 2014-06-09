/**
 * 
 */
package br.com.abril.nds.model.cadastro.garantia;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import br.com.abril.nds.model.cadastro.Imovel;

/**
 * @author Diego Fernandes
 *
 */
@Entity
public class CotaGarantiaImovel extends CotaGarantia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8619127785856466565L;
	

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="GARANTIA_ID",nullable=false)
	private List<Imovel> imoveis;


	/**
	 * @return the imoveis
	 */
	public List<Imovel> getImoveis() {
		return imoveis;
	}


	/**
	 * @param imoveis the imoveis to set
	 */
	public void setImoveis(List<Imovel> imoveis) {
		this.imoveis = imoveis;
	}

}
