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

@Service
public class CotaBaseCotaServiceImpl implements CotaBaseCotaService {

	@Autowired
	private CotaBaseCotaRepository cotaBaseCotaRepository;
	
	@Override
	@Transactional
	public void salvar(CotaBaseCota cotaBaseCota) {
		this.cotaBaseCotaRepository.adicionar(cotaBaseCota);
	}

	@Override
	@Transactional
	public Long verificarExistenciaCotaBaseCota(Cota cota) {		 
		return this.cotaBaseCotaRepository.verificarExistenciaCotaBaseCota(cota);
	}

	@Override
	@Transactional
	public boolean isCotaBaseAtiva(CotaBase cotaBase) {		 
		return this.cotaBaseCotaRepository.isCotaBaseAtiva(cotaBase);
	}

	@Override
	@Transactional
	public void desativarCotaBase(CotaBase cotaBase, Cota cotaParaDesativar) {
		CotaBaseCota cotaBaseCotaParaAtualizar = this.cotaBaseCotaRepository.desativarCotaBase(cotaBase, cotaParaDesativar);
		cotaBaseCotaParaAtualizar.setAtivo(false);
		cotaBaseCotaParaAtualizar.setDtFimVigencia(new Date());
		cotaBaseCotaRepository.alterar(cotaBaseCotaParaAtualizar);
		
		CotaBaseCota cotaBaseCotaExluida = new CotaBaseCota();
		cotaBaseCotaExluida.setCota(cotaParaDesativar);
		cotaBaseCotaExluida.setCotaBase(cotaBase);
		cotaBaseCotaExluida.setDtFimVigencia(new Date());
		cotaBaseCotaExluida.setCotaBase(cotaBase);
		cotaBaseCotaExluida.setTipoAlteracao(TipoAlteracao.EXCLUSAO);
		cotaBaseCotaRepository.adicionar(cotaBaseCotaExluida);
		
		
	}

	@Override
	@Transactional
	public Long quantidadesDeCotasAtivas(CotaBase cotaBase) {		 
		return this.cotaBaseCotaRepository.quantidadesDeCotasAtivas(cotaBase);
	}

}
