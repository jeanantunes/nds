package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
public class DescontoDistribuidorRepositoryImpl extends AbstractRepositoryModel<DescontoDistribuidor, Long> implements DescontoDistribuidorRepository {

	public DescontoDistribuidorRepositoryImpl() {
		super(DescontoDistribuidor.class);
	}
}
