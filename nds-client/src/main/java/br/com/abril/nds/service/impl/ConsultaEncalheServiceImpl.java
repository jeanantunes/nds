package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.ConsultaEncalheService;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Transactional
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		return getInfoMockada();
		
//		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
//		
//		TipoMovimentoEstoque tipoMovimentoEstoque = 
//				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
//					GrupoMovimentoEstoque.ENVIO_ENCALHE);
//		
//		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro, tipoMovimentoEstoque.getId());
//		
//		Integer qtdeRegistrosConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdConsultaEncalhe(filtro, tipoMovimentoEstoque.getId());
//		
//		BigDecimal qtdItemProdutoEdicaoEncalhePrimeiroDia =  movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), false);
//		
//		BigDecimal qtdProdutoEdicaoEncalhePrimeiroDia = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), false);
//
//		BigDecimal qtdItemProdutoEdicaoEncalheAposPrimeiroDia =  movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), true);
//		
//		BigDecimal qtdProdutoEdicaoEncalheAposPrimeiroDia = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), true);
//
//		info.setListaConsultaEncalhe(listaConsultaEncalhe);
//		
//		info.setQtdeConsultaEncalhe(qtdeRegistrosConsultaEncalhe);
//		
//		info.setQtdExemplarDemaisRecolhimentos(qtdItemProdutoEdicaoEncalheAposPrimeiroDia);
//		
//		info.setQtdProdutoDemaisRecolhimentos(qtdProdutoEdicaoEncalheAposPrimeiroDia);
//		
//		info.setQtdExemplarPrimeiroRecolhimento(qtdItemProdutoEdicaoEncalhePrimeiroDia);
//		
//		info.setQtdProdutoPrimeiroRecolhimento(qtdProdutoEdicaoEncalhePrimeiroDia);
//		
//		return info;
	}

	private InfoConsultaEncalheDTO getInfoMockada() {
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = new LinkedList<ConsultaEncalheDTO>();
		
		int contador = 0;
		
		Long idProdutoEdicao 		= null;
		String codigoProduto 		= null;
		String nomeProduto 			= null;
		Long numeroEdicao 			= null;
		BigDecimal precoVenda 		= null;
		BigDecimal precoComDesconto = null;
		BigDecimal reparte 			= null;
		BigDecimal encalhe 			= null;
		String fornecedor			= null;
		BigDecimal total 			= null;
		Integer recolhimento 		= null;
		
		while(contador++<50) {
			
			idProdutoEdicao 		= 0L;
			codigoProduto 			= "000";
			nomeProduto 			= "Produto_";
			numeroEdicao 			= 0L;
			precoVenda 				= BigDecimal.ZERO;
			precoComDesconto 		= BigDecimal.ZERO;
			reparte 				= BigDecimal.ZERO;
			encalhe 				= BigDecimal.ZERO;
			fornecedor				= "Fornecedor_";
			total 					= BigDecimal.ZERO;
			recolhimento 			= 0;

			
			ConsultaEncalheDTO consultaEncalheDTO = new ConsultaEncalheDTO();
			
			consultaEncalheDTO.setIdProdutoEdicao(idProdutoEdicao+contador);
			consultaEncalheDTO.setCodigoProduto(codigoProduto+contador);
			consultaEncalheDTO.setNomeProduto(nomeProduto+contador);
			consultaEncalheDTO.setNumeroEdicao(numeroEdicao+contador);
			consultaEncalheDTO.setPrecoVenda(precoVenda.add(new BigDecimal(contador)));
			consultaEncalheDTO.setPrecoComDesconto(precoComDesconto.add(new BigDecimal(contador)));
			consultaEncalheDTO.setReparte(reparte.add(new BigDecimal(contador)));
			consultaEncalheDTO.setEncalhe(encalhe.add(new BigDecimal(contador)));
			consultaEncalheDTO.setFornecedor(fornecedor+contador);
			consultaEncalheDTO.setTotal(total.add(new BigDecimal(contador)));
			consultaEncalheDTO.setRecolhimento(recolhimento+contador);
			
			listaConsultaEncalhe.add(consultaEncalheDTO);
			
		}
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		info.setQtdeConsultaEncalhe(listaConsultaEncalhe.size());
		
		info.setQtdExemplarDemaisRecolhimentos(new BigDecimal(12));
		info.setQtdProdutoDemaisRecolhimentos(new BigDecimal(34));
		info.setQtdExemplarPrimeiroRecolhimento(new BigDecimal(45));
		info.setQtdProdutoPrimeiroRecolhimento(new BigDecimal(56));
		
		return info;
		
	}
	
	
}
