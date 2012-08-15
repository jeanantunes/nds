package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.GrupoService;

@Service
public class GrupoServiceImpl implements GrupoService {
	

	@Autowired
	private GrupoRepository grupoRepository;

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
			
			gruposDTO.add(new GrupoCotaDTO(grupo.getId(), grupo.getNome(), dias.toString()));
			
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
}
