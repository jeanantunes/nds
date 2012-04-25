package br.com.abril.nds.model.cadastro.pdv;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.CodigoDescricao;

/**
 * Entidade para os tipos de estabelecimentos
 * que em que um PDV pode estar associado.
 * 
 * Indica os tipos de estabelecimentos em que um 
 * PDV pode estar "inserido" 
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "TIPO_ESTABELECIMENTO_ASSOCIACAO_PDV")
public class TipoEstabelecimentoAssociacaoPDV  extends CodigoDescricao {
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
