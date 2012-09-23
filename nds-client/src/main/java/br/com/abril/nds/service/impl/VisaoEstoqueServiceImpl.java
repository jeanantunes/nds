package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class VisaoEstoqueServiceImpl implements VisaoEstoqueService {

	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;
	
	@Override
	@Transactional
	public List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {
		
		List<VisaoEstoqueDTO> list = new ArrayList<VisaoEstoqueDTO>();
		
		if (DateUtil.isHoje(filtro.getDataMovimentacao())) {

			// Busca na tabela estoque
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
		} else {

			// Busca na tabela histórico
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
		}
		
		return list;
	}

	
	@Override
	@Transactional
	public List<? extends VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		List<? extends VisaoEstoqueDetalheDTO> list = visaoEstoqueRepository.obterVisaoEstoqueDetalhe(filtro);
		
		BigDecimal precoCapa;
		BigDecimal qtde;
		
		for (VisaoEstoqueDetalheDTO dto: list) {
			
			precoCapa = CurrencyUtil.converterValor(dto.getPrecoCapa());
			qtde = CurrencyUtil.converterValor(dto.getQtde());
			dto.setValor(precoCapa.multiply(qtde));
		}
		
		return list;
	}
}
