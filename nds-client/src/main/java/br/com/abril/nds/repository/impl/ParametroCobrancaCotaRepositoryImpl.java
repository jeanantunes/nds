package br.com.abril.nds.repository.impl;
import org.springframework.stereotype.Repository;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;

@Repository
public class ParametroCobrancaCotaRepositoryImpl extends AbstractRepository<ParametroCobrancaCota,Long> implements ParametroCobrancaCotaRepository  {

	
	/**
	 * Construtor padrão
	 */
	public ParametroCobrancaCotaRepositoryImpl() {
		super(ParametroCobrancaCota.class);
	}
	
}
