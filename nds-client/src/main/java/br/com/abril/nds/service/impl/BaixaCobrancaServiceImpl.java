package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.BaixaCobrancaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.BaixaCobranca}
 * @author InfoA2
 */
@Service
public class BaixaCobrancaServiceImpl implements BaixaCobrancaService {

	@Autowired
	BaixaCobrancaRepository baixaCobrancaRepository;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.BaixaCobrancaService#buscarUltimaBaixaAutomaticaDia(java.util.Date)
	 */
	@Override
	@Transactional(readOnly=true)
	public Date buscarUltimaBaixaAutomaticaDia(Date dataOperacao) {
		return baixaCobrancaRepository.buscarUltimaBaixaAutomaticaDia(dataOperacao);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.BaixaCobrancaService#buscarDiaUltimaBaixaAutomatica()
	 */
	@Override
	@Transactional(readOnly=true)
	public Date buscarDiaUltimaBaixaAutomatica() {
		return baixaCobrancaRepository.buscarDiaUltimaBaixaAutomatica();
	}

	@Override
	@Transactional(readOnly=true)
	public List<CobrancaVO> buscarCobrancasBaixadas(FiltroConsultaDividasCotaDTO filtroConsultaDividasCota) {
		return baixaCobrancaRepository.buscarCobrancasBaixadas(filtroConsultaDividasCota);
	}

}
