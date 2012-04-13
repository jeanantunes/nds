package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.ConsultaEncalheService;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConsultaEncalheService#pesquisarEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@Transactional
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro, tipoMovimentoEstoque.getId());
		
		Integer qtdeRegistrosConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdConsultaEncalhe(filtro, tipoMovimentoEstoque.getId());
		
		BigDecimal qtdItemProdutoEdicaoEncalhePrimeiroDia =  movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), false);
		
		Integer qtdProdutoEdicaoEncalhePrimeiroDia = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), false);

		BigDecimal qtdItemProdutoEdicaoEncalheAposPrimeiroDia =  movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), true);
		
		Integer qtdProdutoEdicaoEncalheAposPrimeiroDia = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, tipoMovimentoEstoque.getId(), true);

		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		info.setQtdeConsultaEncalhe(qtdeRegistrosConsultaEncalhe);
		
		info.setQtdExemplarDemaisRecolhimentos(qtdItemProdutoEdicaoEncalheAposPrimeiroDia);
		info.setQtdProdutoDemaisRecolhimentos(qtdProdutoEdicaoEncalheAposPrimeiroDia);
		
		info.setQtdExemplarPrimeiroRecolhimento(qtdItemProdutoEdicaoEncalhePrimeiroDia);
		info.setQtdProdutoPrimeiroRecolhimento(qtdProdutoEdicaoEncalhePrimeiroDia);
		
		return info;
	}

	
}
