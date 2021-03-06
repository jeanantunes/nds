package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.model.cadastro.CotaBaseCota;
import br.com.abril.nds.model.cadastro.TipoAlteracao;
import br.com.abril.nds.repository.CotaBaseCotaRepository;
import br.com.abril.nds.service.CotaBaseCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class CotaBaseCotaServiceImpl implements CotaBaseCotaService {

	@Autowired
	private CotaBaseCotaRepository cotaBaseCotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	@Transactional
	public void salvar(CotaBaseCota cotaBaseCota) {
		this.cotaBaseCotaRepository.adicionar(cotaBaseCota);
	}

	@Override
	@Transactional(readOnly = true)
	public Long verificarExistenciaCotaBaseCota(Cota cota) {		 
		return this.cotaBaseCotaRepository.verificarExistenciaCotaBaseCota(cota);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isCotaBaseAtiva(CotaBase cotaBase, Integer[] numerosDeCotasBase) {		 
		return this.cotaBaseCotaRepository.isCotaBaseAtiva(cotaBase, numerosDeCotasBase);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isCotaBaseAtiva(CotaBase cotaBase) {		 
		return this.cotaBaseCotaRepository.isCotaBaseAtiva(cotaBase, null);
	}

	@Override
	@Transactional
	public void desativarCotaBase(CotaBase cotaBase, Cota cotaParaDesativar) {
		
	    final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
	    
	    final CotaBaseCota cotaBaseCotaParaAtualizar = 
	            this.cotaBaseCotaRepository.desativarCotaBase(cotaBase, cotaParaDesativar);
		cotaBaseCotaParaAtualizar.setAtivo(false);
		cotaBaseCotaParaAtualizar.setDtFimVigencia(dataOperacao);
		cotaBaseCotaRepository.alterar(cotaBaseCotaParaAtualizar);
		
		final CotaBaseCota cotaBaseCotaExluida = new CotaBaseCota();
		cotaBaseCotaExluida.setCota(cotaParaDesativar);
		cotaBaseCotaExluida.setDtFimVigencia(dataOperacao);
		cotaBaseCotaExluida.setCotaBase(cotaBase);
		cotaBaseCotaExluida.setTipoAlteracao(TipoAlteracao.EXCLUSAO);
		cotaBaseCotaExluida.setAtivo(false);
		cotaBaseCotaRepository.adicionar(cotaBaseCotaExluida);
	}

	@Override
	@Transactional(readOnly = true)
	public Long quantidadesDeCotasAtivas(CotaBase cotaBase) {		 
		return this.cotaBaseCotaRepository.quantidadesDeCotasAtivas(cotaBase);
	}

}
