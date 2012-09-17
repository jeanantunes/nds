package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
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


}
