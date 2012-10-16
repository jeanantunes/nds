package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.service.ContasAPagarService;

@Service
public class ContasAPagarServiceImpl implements ContasAPagarService {
	
	@Autowired 
	private ContasAPagarRepository contasAPagarRepository;

	@Transactional
	@Override
	public List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro) {
		return contasAPagarRepository.pesquisarProdutos(filtro);
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalProdutoDTO pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasApagarConsultaPorDistribuidorDTO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}
}
