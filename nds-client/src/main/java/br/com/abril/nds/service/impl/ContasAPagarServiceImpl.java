package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.service.ContasAPagarService;

@Service
public class ContasAPagarServiceImpl implements ContasAPagarService{
	
	@Autowired 
	private ContasAPagarRepository contasAPagarRepository;

	@Transactional
	@Override
	public List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro) {
		return contasAPagarRepository.pesquisarProdutos(filtro);
	}

	@Transactional
	@Override
	public List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO filtro) {
		return contasAPagarRepository.pesquisarPorProduto(filtro);
	}

	@Transactional
	@Override
	public List<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}
}
