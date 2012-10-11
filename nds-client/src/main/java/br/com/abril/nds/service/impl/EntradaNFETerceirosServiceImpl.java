package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.service.EntradaNFETerceirosService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class EntradaNFETerceirosServiceImpl implements
		EntradaNFETerceirosService {
	
	@Autowired
	private EntradaNFETerceirosRepository entradaNFETerceirosRepository;
	
	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository ;

	@Override
	@Transactional
	public List<ConsultaEntradaNFETerceirosRecebidasDTO> buscarNFNotasRecebidas(
			FiltroEntradaNFETerceiros filtro, boolean limitar) {		 
		return this.entradaNFETerceirosRepository.buscarNFNotasRecebidas(filtro, limitar);
	}

	@Override
	@Transactional
	public Integer buscarTodasNFNotas(
			FiltroEntradaNFETerceiros filtro) {
		return this.entradaNFETerceirosRepository.buscarTotalNotas(filtro);
	}

	@Override
	@Transactional
	public List<ConsultaEntradaNFETerceirosPendentesDTO> buscarNFNotasPendentes(FiltroEntradaNFETerceiros filtro, boolean limitar) {
		List<ConsultaEntradaNFETerceirosPendentesDTO> listaAux = this.entradaNFETerceirosRepository.buscarNFNotasPendentes(filtro, limitar);
		List<ConsultaEntradaNFETerceirosPendentesDTO> listaRetorno = new ArrayList<ConsultaEntradaNFETerceirosPendentesDTO>(); 
		for(ConsultaEntradaNFETerceirosPendentesDTO dto: listaAux){
			dto.setStatus("Pendente");
			listaRetorno.add(dto);
		}
		return listaRetorno;
	}

	@Override
	@Transactional
	public List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(
			FiltroEntradaNFETerceiros filtro) {		
		List<ItemNotaFiscalPendenteDTO> listaAux =  this.entradaNFETerceirosRepository.buscarItensPorNota(filtro);
		List<ItemNotaFiscalPendenteDTO> listaRetorno =  new ArrayList<ItemNotaFiscalPendenteDTO>();
		for(ItemNotaFiscalPendenteDTO dto: listaAux){
			Long qtdDiferencaDias = DateUtil.obterDiferencaDias(dto.getDataConferenciaEncalhe(), dto.getDataChamadaEncalhe()) + 1;			
			dto.setDia(qtdDiferencaDias.toString() + "°");		
			listaRetorno.add(dto);
		}
		return listaRetorno;
	}

	@Override
	@Transactional
	public Integer buscarTodasItensPorNota(
			FiltroEntradaNFETerceiros filtro) {		
		return this.entradaNFETerceirosRepository.buscarTodasItensPorNota(filtro);
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
	public Long quantidadeItemNotaFiscalEntradaPorControleConferenciaEncalheCota(
			long idControleConferencia) {
		return itemNotaFiscalEntradaRepository
				.quantidadePorControleConferenciaEncalheCota(idControleConferencia);
	}
	
	
	
	
	
	

}
