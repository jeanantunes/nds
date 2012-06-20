package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaNFENotasRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.repository.ConsultaNFEEncalheTratamentoNotasRecebidasRepository;
import br.com.abril.nds.service.ConsultaNFEEncalheTratamentoNotasRecebidasService;
import br.com.abril.nds.util.DateUtil;

@Service
public class ConsultaNFEEncalheTratamentoNotasRecebidasServiceImpl implements
		ConsultaNFEEncalheTratamentoNotasRecebidasService {
	
	@Autowired
	private ConsultaNFEEncalheTratamentoNotasRecebidasRepository consultaNFEEncalheTratamentoNotasRecebidasRepository;

	@Override
	@Transactional
	public List<ConsultaNFENotasRecebidasDTO> buscarNFNotasRecebidas(
			FiltroConsultaNFEEncalheTratamento filtro, String limitar) {		 
		return this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarNFNotasRecebidas(filtro, limitar);
	}

	@Override
	@Transactional
	public Integer buscarTodasNFENotasRecebidas(
			FiltroConsultaNFEEncalheTratamento filtro) {
		return this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarTotalNotasRecebidas(filtro);
	}

	@Override
	@Transactional
	public List<ConsultaNFENotasPendentesDTO> buscarNFNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro, String limitar) {
		List<ConsultaNFENotasPendentesDTO> listaAux = this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarNFNotasPendentes(filtro, limitar);
		List<ConsultaNFENotasPendentesDTO> listaRetorno = new ArrayList<ConsultaNFENotasPendentesDTO>(); 
		for(ConsultaNFENotasPendentesDTO dto: listaAux){
			dto.setStatus("Pendente");
			listaRetorno.add(dto);
		}
		return listaRetorno;
	}

	@Override
	@Transactional
	public List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(
			FiltroConsultaNFEEncalheTratamento filtro) {		
		List<ItemNotaFiscalPendenteDTO> listaAux =  this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarItensPorNota(filtro);
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
			FiltroConsultaNFEEncalheTratamento filtro) {		
		return this.consultaNFEEncalheTratamentoNotasRecebidasRepository.buscarTodasItensPorNota(filtro);
	}

}
