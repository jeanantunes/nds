package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.service.EntradaNFETerceirosService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class EntradaNFETerceirosServiceImpl implements EntradaNFETerceirosService {
	
	@Autowired
	private EntradaNFETerceirosRepository entradaNFETerceirosRepository;
	
	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository ;

	@Override
	@Transactional
	public List<ConsultaEntradaNFETerceirosRecebidasDTO> consultarNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar) {		 
		return this.entradaNFETerceirosRepository.consultarNotasRecebidas(filtro, limitar);
	}

	@Override
	@Transactional
	public Integer buscarTodasNFNotas(FiltroEntradaNFETerceiros filtro) {
		return this.entradaNFETerceirosRepository.buscarTotalNotas(filtro);
	}

	@Override
	@Transactional
	public List<ConsultaEntradaNFETerceirosPendentesDTO> consultaNotasPendentesRecebimento(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		return this.entradaNFETerceirosRepository.consultaNotasPendentesRecebimento(filtro, limitar);
	}

	/**
	 * @param idControleConferencia
	 * @param orderBy
	 * @param ordenacao
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @see br.com.abril.nds.repository.NotaFiscalRepository#obtemItemNotaFiscalEntradaPorControleConferenciaEncalheCota(long, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ItemNotaFiscalEntrada> obtemItemNotaFiscalEntradaPorControleConferenciaEncalheCota(
			long idControleConferencia, String orderBy, Ordenacao ordenacao,
			Integer firstResult, Integer maxResults) {
		return itemNotaFiscalEntradaRepository
				.obtemPorControleConferenciaEncalheCota(
						idControleConferencia, orderBy, ordenacao, firstResult,
						maxResults);
	}

	/**
	 * @param idControleConferencia
	 * @return
	 * @see br.com.abril.nds.repository.NotaFiscalRepository#quantidadeItemNotaFiscalEntradaPorControleConferenciaEncalheCota(long)
	 */
	@Override
	@Transactional(readOnly=true)
	public Long quantidadeItemNotaFiscalEntradaPorControleConferenciaEncalheCota(long idControleConferencia) {
		return itemNotaFiscalEntradaRepository.quantidadePorControleConferenciaEncalheCota(idControleConferencia);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer qtdeNotasRecebidas(FiltroEntradaNFETerceiros filtro) {
		return this.entradaNFETerceirosRepository.qtdeNotasRecebidas(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer qtdeNotasPendentesRecebimento(FiltroEntradaNFETerceiros filtro) {
		return this.entradaNFETerceirosRepository.qtdeNotasPendentesRecebimento(filtro);
	}

	@Override
	@Transactional
	public List<ConsultaEntradaNFETerceirosPendentesDTO> consultaNotasPendentesEmissao(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		return this.entradaNFETerceirosRepository.consultaNotasPendentesEmissao(filtro, limitar);
	}

	@Override
	@Transactional
	public Integer qtdeNotasPendentesEmissao(FiltroEntradaNFETerceiros filtro) {
		return this.entradaNFETerceirosRepository.qtdeNotasPendentesEmissao(filtro);
	}
}
