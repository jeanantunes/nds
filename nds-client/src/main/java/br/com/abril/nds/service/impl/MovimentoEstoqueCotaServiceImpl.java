package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;


@Service
public class MovimentoEstoqueCotaServiceImpl implements MovimentoEstoqueCotaService {
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Transactional
	public List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque){
		return movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, idCota, grupoMovimentoEstoque);
		
	}

	@Override
	@Transactional
	public List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Integer numCota,GrupoMovimentoEstoque grupoMovimentoEstoque) {
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		return this.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), grupoMovimentoEstoque);
	}

	@Override
	@Transactional
	public List<MovimentoEstoqueCotaDTO> obterMovimentoDTOCotaPorTipoMovimento(Date data, Integer numCota, GrupoMovimentoEstoque grupoMovimentoEstoque) {
	
		List<MovimentoEstoqueCota> movimentos = this.obterMovimentoCotaPorTipoMovimento(data, numCota, grupoMovimentoEstoque);
		
		
		List<MovimentoEstoqueCotaDTO> movimentosDTO = new ArrayList<MovimentoEstoqueCotaDTO>();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		for(MovimentoEstoqueCota movimento : movimentos) {
			
			movimentosDTO.add(new MovimentoEstoqueCotaDTO(
					cota.getId(), 
					movimento.getProdutoEdicao().getId(), 
					movimento.getProdutoEdicao().getProduto().getCodigo(), 
					movimento.getProdutoEdicao().getNumeroEdicao(), 
					movimento.getProdutoEdicao().getProduto().getNome(), 
					movimento.getQtde().intValue(), 
					null));
		}
		
		return movimentosDTO;
	}

	
}
