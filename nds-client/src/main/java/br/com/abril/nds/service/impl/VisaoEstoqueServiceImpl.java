package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.service.VisaoEstoqueService;

@Service
public class VisaoEstoqueServiceImpl implements VisaoEstoqueService{

	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;
	
	@Override
	public List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {
		
		return null;
	}


}
