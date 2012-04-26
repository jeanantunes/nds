package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.repository.ClusterPDVRepository;

public class ClusterPDVRepositoryImpl extends AbstractRepository<ClusterPDV, Long> implements ClusterPDVRepository {
	
	public ClusterPDVRepositoryImpl() {
		super(ClusterPDV.class);
	}
}
