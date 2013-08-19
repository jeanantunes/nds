package br.com.abril.nds.model.cadastro.pdv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@SequenceGenerator(name="TIPO_ESTABELECIMENTO_ASSOCIACAO_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class TipoEstabelecimentoAssociacaoPDV  extends CodigoDescricao {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "TIPO_ESTABELECIMENTO_ASSOCIACAO_PDV_SEQ")
	private Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
