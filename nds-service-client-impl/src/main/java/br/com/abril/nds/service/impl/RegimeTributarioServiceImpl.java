package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.TributoAliquotaDTO;
import br.com.abril.nds.model.cadastro.RegimeTributario;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.repository.RegimeTributarioRepository;
import br.com.abril.nds.service.integracao.RegimeTributarioService;

@Service
public class RegimeTributarioServiceImpl implements RegimeTributarioService {

	@Autowired
	private RegimeTributarioRepository regimeTributarioRepository;
	
	@Override
	@Transactional
	public List<RegimeTributario> obterRegimesTributarios() {
		
		return regimeTributarioRepository.buscarTodos();
	}

	@Override
	@Transactional
	public List<TributoAliquotaDTO> obterTributosPeloRegimeTributario(Long regimeTributarioId) {

		RegimeTributario rt = regimeTributarioRepository.buscarPorId(regimeTributarioId);
		List<TributoAliquotaDTO> tributosAliquotas = new ArrayList<TributoAliquotaDTO>();
		
		for(TributoAliquota ta : rt.getTributosAliquotas()) {
			TributoAliquotaDTO tributoAliquota = new TributoAliquotaDTO();
			tributoAliquota.setId(ta.getId());
			tributoAliquota.setTipoAliquota(ta.getTipoAliquota().getDescricao());
			tributoAliquota.setTributoId(ta.getTributo().getId());
			tributoAliquota.setTributo(ta.getTributo().getNome());
			tributoAliquota.setTributoDescricao(ta.getTributo().getDescricao());
			tributoAliquota.setValor(ta.getValor());
			
			tributosAliquotas.add(tributoAliquota);
		}
		
		return tributosAliquotas;
	}

}