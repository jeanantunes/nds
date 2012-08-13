package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.repository.DescontoCotaRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto da cota
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoCotaRepositoryImpl extends AbstractRepositoryModel<DescontoCota, Long> implements DescontoCotaRepository {

	public DescontoCotaRepositoryImpl() {
		super(DescontoCota.class);
	}

	
}
