package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.AjusteReparteRepository;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
import br.com.abril.nds.service.AjusteReparteService;

@Service
public class AjusteReparteServiceImpl implements AjusteReparteService  {
	
	@Autowired
	private AjusteReparteRepository ajusteRepository;
	
	@Autowired
	private TipoSegmentoProdutoRepository tipoSegProdRepo;

	@Override
	@Transactional
	public void salvarAjuste(AjusteReparte ajuste) {
		ajusteRepository.adicionar(ajuste);
	}

	@Override
	@Transactional
	public List<AjusteReparteDTO> buscarCotasEmAjuste(AjusteReparteDTO dto) {
		return ajusteRepository.buscarTodasCotas(dto);
	}

	@Override
	@Transactional
	public void alterarAjuste(AjusteReparte ajuste) {
		ajusteRepository.alterar(ajuste);
	}

	@Override
	@Transactional
	public void excluirAjuste(Long idAjuste) {
		ajusteRepository.removerPorId(idAjuste);
		
	}

	@Override
	@Transactional
	public AjusteReparteDTO buscarPorIdAjuste(Long id) {
		return ajusteRepository.buscarPorIdAjuste(id);
	}

	@Override
	@Transactional
	public List<TipoSegmentoProduto> buscarTodosSegmentos() {
		return tipoSegProdRepo.buscarTodos(); 
	}

//	@Override
//	public void excluirAjustePorCota(Cota cota) {
//		ajusteRepository.removerPorCota(cota);
//		
//	}
}
