package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.movimentacao.MovimentoEstoqueCota;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.MovimentoCotaRepository;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class DiferencaEstoqueServiceImpl implements DiferencaEstoqueService {

	@Autowired
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Autowired
	private MovimentoCotaRepository movimentoCotaRepository;
	
	@Autowired
	private RateioDiferencaRepository rateioDiferencaRepository;
	
	@Transactional(readOnly = true)
	public List<Diferenca> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
	}

	@Transactional(readOnly = true)
	public List<Diferenca> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterDiferencas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Long obterTotalDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro) {
		
		return this.diferencaEstoqueRepository.obterTotalDiferencas(filtro);
	}
	
	@Transactional(readOnly = true)
	public boolean verificarPossibilidadeExclusao(Long idDiferenca){
		if (idDiferenca == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Id da Diferença é obrigatório.");
		}
		
		Boolean diferenca = this.diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(idDiferenca);
		
		return diferenca == null ? false : !diferenca;
	}
	
	@Transactional
	public void excluirDiferenca(List<Long> idsDiferenca){
		
		for (Long idDiferenca : idsDiferenca){
			
			Diferenca diferenca = this.diferencaEstoqueRepository.buscarPorId(idDiferenca);
			
			if (!diferenca.isAutomatica()){
				RateioDiferenca rateioDiferenca = 
						this.rateioDiferencaRepository.obterRateioDiferencaPorDiferenca(idDiferenca);
				
				if (rateioDiferenca != null){
					
					MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
					movimentoEstoqueCota.setCota(rateioDiferenca.getCota());
					movimentoEstoqueCota.setQtde(diferenca.getQtde());
					
					this.movimentoCotaRepository.adicionar(movimentoEstoqueCota);

				}
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Diferença com automática não pode ser excluída.");
			}
		}
	}
}