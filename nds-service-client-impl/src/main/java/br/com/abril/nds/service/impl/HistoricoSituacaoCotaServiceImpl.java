package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.service.HistoricoSituacaoCotaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.HistoricoSituacaoCota}
 * @author InfoA2
 */
@Service
public class HistoricoSituacaoCotaServiceImpl implements HistoricoSituacaoCotaService {

	@Autowired
	HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.HistoricoSituacaoCotaService#buscarUltimaSuspensaoCotasDia(java.util.Date)
	 */
	@Override
	@Transactional(readOnly=true)
	public Date buscarUltimaSuspensaoCotasDia(Date dataOperacao) {
		return historicoSituacaoCotaRepository.buscarUltimaSuspensaoCotasDia(dataOperacao);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.HistoricoSituacaoCotaService#buscarDataUltimaSuspensaoCotas()
	 */
	@Override
	@Transactional(readOnly=true)
	public Date buscarDataUltimaSuspensaoCotas() {
		return historicoSituacaoCotaRepository.buscarDataUltimaSuspensaoCotas();
	}
	
}
