package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.GrupoService;

@Service
public class GrupoServiceImpl implements GrupoService {
	

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Override
	@Transactional
	public List<GrupoCotaDTO> obterTodosGrupos() {
		
		List<GrupoCota> grupos = grupoRepository.buscarTodos();
		
		List<GrupoCotaDTO> gruposDTO = new ArrayList<GrupoCotaDTO>();

		for(GrupoCota grupo: grupos) {
						
			StringBuilder dias = null;
			
			for(DiaSemana dia :  grupo.getDiasRecolhimento()){
				if(dias == null)
					dias = new StringBuilder();					
				else
					dias.append(" - ");
				
				dias.append(dia.getDescricaoDiaSemana());
			}
			
			GrupoCotaDTO dto = new GrupoCotaDTO();
			dto.setIdGrupo(grupo.getId());
			dto.setNome(grupo.getNome());
			dto.setRecolhimento(dias.toString());
			dto.setTipoCota(grupo.getTipoCota());
			dto.setTipoGrupo(grupo.getTipoGrupo());
			
			gruposDTO.add(dto);			
		}
		
		return gruposDTO;
	}

	@Transactional
	@Override
	public void excluirGrupo(Long idGrupo) {
		
		GrupoCota grupo = grupoRepository.buscarPorId(idGrupo);
		
		//grupo.setDiasRecolhimento(null);
		
		//grupoRepository.alterar(grupo);
		
		grupoRepository.remover(grupo);
	}

	@Override
	public List<CotaTipoDTO> obterCotaPorTipo(TipoCota tipoCota) {

		return cotaRepository.obterCotaPorTipo(tipoCota);
	}
}
