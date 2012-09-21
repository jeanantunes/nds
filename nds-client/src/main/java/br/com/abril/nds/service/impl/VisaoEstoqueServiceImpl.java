package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.repository.impl.VisaoEstoqueRepositoryImpl;
import br.com.abril.nds.service.VisaoEstoqueService;


@Service
public class VisaoEstoqueServiceImpl implements VisaoEstoqueService{

	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;
	
	@Override
	@Transactional
	public List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {
		List<VisaoEstoqueDTO> list = new ArrayList<VisaoEstoqueDTO>();

		list.add(visaoEstoqueRepository.obterLancamento(filtro));
		list.add(visaoEstoqueRepository.obterLancamentoJuramentado(filtro));
		list.add(visaoEstoqueRepository.obterSuplementar(filtro));
		list.add(visaoEstoqueRepository.obterRecolhimento(filtro));
		list.add(visaoEstoqueRepository.obterProdutosDanificados(filtro));

		return list;
	}

	
	@Override
	@Transactional
	public List<? extends VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		List<? extends VisaoEstoqueDetalheDTO> list = null;
		
		if(filtro.getTipoEstoque().equals(VisaoEstoqueRepositoryImpl.LANCAMENTO)) {
			list = new ArrayList<VisaoEstoqueDetalheDTO>();
			list = visaoEstoqueRepository.obterLancamentoDetalhe(filtro);
		} else if (filtro.getTipoEstoque().equals(VisaoEstoqueRepositoryImpl.LANCAMENTO_JURAMENTADO)){
			list = new ArrayList<VisaoEstoqueDetalheJuramentadoDTO>();
			list = visaoEstoqueRepository.obterLancamentoJuramentadoDetalhe(filtro);
		}else if (filtro.getTipoEstoque().equals(VisaoEstoqueRepositoryImpl.SUPLEMENTAR)) {
			list = new ArrayList<VisaoEstoqueDetalheDTO>();
			list = visaoEstoqueRepository.obterSuplementarDetalhe(filtro);
		} else if(filtro.getTipoEstoque().equals(VisaoEstoqueRepositoryImpl.RECOLHIMENTO)) {
			list = new ArrayList<VisaoEstoqueDetalheDTO>();
			list = visaoEstoqueRepository.obterRecolhimentoDetalhe(filtro);
		} else {
			list = new ArrayList<VisaoEstoqueDetalheDTO>();
			list = visaoEstoqueRepository.obterProdutosDanificadosDetalhe(filtro);
		}

		return list;
	}


	@Override
	@Transactional
	public List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueTransferencia(FiltroConsultaVisaoEstoque filtro) {
		return null;
	}


	@Override
	@Transactional
	public List<VisaoEstoqueDetalheDTO> obterVisaoEstoqueInventario(FiltroConsultaVisaoEstoque filtro) {
		return null;
	}
}
