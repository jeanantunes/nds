package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class TipoNotaFiscalServiceImpl implements TipoNotaFiscalService {

	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired 
	private SerieRepository serieRepository;
	
	@Override
	@Transactional
	public List<TipoNotaFiscal> obterTiposNotasFiscais() {
		return tipoNotaFiscalRepository.obterTiposNotasFiscais();
	}

	@Override
	@Transactional
	public TipoNotaFiscal obterPorId(Long id) {
		
		return this.tipoNotaFiscalRepository.buscarPorId(id);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoNotaFiscalService#obterTiposNotasFiscais(java.lang.String, java.lang.String, br.com.abril.nds.model.cadastro.TipoAtividade, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoNotaFiscal> obterTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		return tipoNotaFiscalRepository.obterTiposNotasFiscais(cfop, tipoNota, tipoAtividade, orderBy, ordenacao, initialResult, maxResults);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoNotaFiscalService#obterTiposNotasFiscais(java.lang.String, java.lang.String, br.com.abril.nds.model.cadastro.TipoAtividade)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoNotaFiscal> obterTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade) {
		return tipoNotaFiscalRepository.obterTiposNotasFiscais(cfop, tipoNota, tipoAtividade, null, null, null, null);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoNotaFiscalService#obterQuantidadeTiposNotasFiscais(java.lang.String, java.lang.String, br.com.abril.nds.model.cadastro.TipoAtividade)
	 */
	@Override
	@Transactional(readOnly=true)
	public Long obterQuantidadeTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade) {
		return tipoNotaFiscalRepository.obterQuantidadeTiposNotasFiscais(cfop, tipoNota, tipoAtividade);
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public Long proximoNumeroDocumentoFiscal(int serie) {		
		return serieRepository.next(serie);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(Long idDistribuidor) {

		Distribuidor distribuidor = this.distribuidorRepository.buscarPorId(idDistribuidor);
		
		if (distribuidor == null) {
			
			return null;
		}
		
		return tipoNotaFiscalRepository.obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(
				distribuidor.getTipoAtividade());
	}
}
