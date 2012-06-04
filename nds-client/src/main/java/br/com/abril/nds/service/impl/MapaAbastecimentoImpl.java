package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.MapaAbastecimentoService;

@Service
public class MapaAbastecimentoImpl implements MapaAbastecimentoService{

	@Autowired
	private MovimentoEstoqueCotaRepository cotaRepository;
	
	@Override
	@Transactional
	public List<AbastecimentoDTO> obterDadosAbastecimento(
			FiltroMapaAbastecimentoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

}
