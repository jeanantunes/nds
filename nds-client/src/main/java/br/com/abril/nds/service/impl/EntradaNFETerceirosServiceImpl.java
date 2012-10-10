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
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.service.EntradaNFETerceirosService;
import br.com.abril.nds.util.DateUtil;

@Service
public class EntradaNFETerceirosServiceImpl implements
		EntradaNFETerceirosService {
	
	@Autowired
	private EntradaNFETerceirosRepository entradaNFETerceirosRepository;

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

}
