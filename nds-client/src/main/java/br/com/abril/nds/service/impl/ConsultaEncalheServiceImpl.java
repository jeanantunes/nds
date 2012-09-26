package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ConsultaEncalheService;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConsultaEncalheService#pesquisarEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@Transactional
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Integer qtdeRegistrosConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdConsultaEncalhe(filtro);
		
		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		info.setQtdeConsultaEncalhe(qtdeRegistrosConsultaEncalhe);
		
		return info;
	}

	@Transactional
	public InfoConsultaEncalheDetalheDTO pesquisarEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro) {
		
		InfoConsultaEncalheDetalheDTO info = new InfoConsultaEncalheDetalheDTO();
		
		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Integer qtdeRegistrosConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalheDetalhe(filtro);
		
		Date dataOperacao = filtro.getDataMovimento();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(filtro.getIdProdutoEdicao());
		
		info.setListaConsultaEncalheDetalhe(listaConsultaEncalheDetalhe);
		
		info.setQtdeConsultaEncalheDetalhe(qtdeRegistrosConsultaEncalheDetalhe);
		
		info.setDataOperacao(dataOperacao);
		
		info.setProdutoEdicao(produtoEdicao);
		
		return info;
	}
	
}
