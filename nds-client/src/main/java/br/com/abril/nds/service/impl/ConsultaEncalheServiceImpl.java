package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.ConsultaEncalheService;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConsultaEncalheService#pesquisarEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@Transactional
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Integer qtdeRegistrosConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdConsultaEncalhe(filtro);
		
		BigDecimal qtdItemProdutoEdicaoEncalhePrimeiroDia =  movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, false);
		
		Integer qtdProdutoEdicaoEncalhePrimeiroDia = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, false);

		BigDecimal qtdItemProdutoEdicaoEncalheAposPrimeiroDia =  movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, true);
		
		Integer qtdProdutoEdicaoEncalheAposPrimeiroDia = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, true);

		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		info.setQtdeConsultaEncalhe(qtdeRegistrosConsultaEncalhe);
		
		info.setQtdExemplarDemaisRecolhimentos(qtdItemProdutoEdicaoEncalheAposPrimeiroDia);
		info.setQtdProdutoDemaisRecolhimentos(qtdProdutoEdicaoEncalheAposPrimeiroDia);
		
		info.setQtdExemplarPrimeiroRecolhimento(qtdItemProdutoEdicaoEncalhePrimeiroDia);
		info.setQtdProdutoPrimeiroRecolhimento(qtdProdutoEdicaoEncalhePrimeiroDia);
		
		return info;
	}

	
}
