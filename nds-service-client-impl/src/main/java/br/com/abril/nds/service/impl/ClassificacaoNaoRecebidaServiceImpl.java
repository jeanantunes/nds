package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.repository.ClassificacaoNaoRecebidaRepository;
import br.com.abril.nds.service.ClassificacaoNaoRecebidaService;

@Service
public class ClassificacaoNaoRecebidaServiceImpl implements	ClassificacaoNaoRecebidaService {

	@Autowired
	private ClassificacaoNaoRecebidaRepository classificacaoNaoRecebidarRepository; 
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		return classificacaoNaoRecebidarRepository.obterCotasQueRecebemClassificacao(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		return classificacaoNaoRecebidarRepository.obterCotasQueNaoRecebemClassificacao(filtro);
	}

	@Transactional
	@Override
	public void excluirClassificacaoNaoRecebida(Long id) {
		this.classificacaoNaoRecebidarRepository.removerPorId(id);
	}

}
