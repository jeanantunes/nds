package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.service.ContasAPagarService;







@Service
public class ContasAPagarServiceImpl implements ContasAPagarService{
	
	
	@Autowired 
	private ContasAPagarRepository contasAPagarRepository;

	@Transactional
	@Override
	public List<ContasAPagarConsultaProdutoDTO> pesquisaProdutoContasAPagar(
			String codigoProduto, Long edicao) {
		return contasAPagarRepository.pesquisaProdutoContasAPagar(codigoProduto, edicao);
	}



}
