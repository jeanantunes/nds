/**
 * 
 */
package br.com.abril.nds.model.cadastro.garantia;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import br.com.abril.nds.model.cadastro.CaucaoLiquida;

/**
 * @author Diego Fernandes
 *
 */
@Entity
public class CotaGarantiaCaucaoLiquida extends CotaGarantia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2374130596840933128L;
	
	

	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="COTA_GARANTIA_CAUCAO_LIQUIDA_ID")
	private List<CaucaoLiquida> caucaoLiquidas;


	/**
	 * @return the caucaoLiquidas
	 */
	public List<CaucaoLiquida> getCaucaoLiquidas() {
		return caucaoLiquidas;
	}


	/**
	 * @param caucaoLiquidas the caucaoLiquidas to set
	 */
	public void setCaucaoLiquidas(List<CaucaoLiquida> caucaoLiquidas) {
		this.caucaoLiquidas = caucaoLiquidas;
	}
	
	
	

}
