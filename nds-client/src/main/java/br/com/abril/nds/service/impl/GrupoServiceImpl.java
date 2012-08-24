package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.LocalidadeRepository;
import br.com.abril.nds.service.GrupoService;

@Service
public class GrupoServiceImpl implements GrupoService {
	

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private LocalidadeRepository localidadeRepository;
	
	@Override
	@Transactional
	public List<GrupoCotaDTO> obterTodosGrupos() {
		
		List<GrupoCota> grupos = grupoRepository.buscarTodos();
		
		List<GrupoCotaDTO> gruposDTO = new ArrayList<GrupoCotaDTO>();

		for(GrupoCota grupo: grupos) {
			
			GrupoCotaDTO dto = new GrupoCotaDTO();
			
			dto.setDiasSemana(new ArrayList<DiaSemana>());
			
			StringBuilder dias = null;
						
			for(DiaSemana dia :  grupo.getDiasRecolhimento()){
				if(dias == null)
					dias = new StringBuilder();					
				else
					dias.append(" - ");
				
				dto.getDiasSemana().add(dia);
				dias.append(dia.getDescricaoDiaSemana());
			}
						
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
		
		grupoRepository.remover(grupo);
	}

	@Override
	@Transactional
	public List<CotaTipoDTO> obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV tipoCota, Integer page, Integer rp, String sortname, String sortorder) {

		return cotaRepository.obterCotaPorTipo(tipoCota, page, rp, sortname, sortorder);
	}

	@Override
	@Transactional
	public int obterCountCotaPorTipo(TipoCaracteristicaSegmentacaoPDV tipoCota) {
		return cotaRepository.obterCountCotaPorTipo(tipoCota);
	}
	
	@Override
	@Transactional
	public int obterCountQtdeCotaMunicipio() {
		return cotaRepository.obterCountQtdeCotaMunicipio();
	}

	@Override
	@Transactional
	public List<MunicipioDTO> obterQtdeCotaMunicipio(Integer page, Integer rp,
			String sortname, String sortorder) {
		return cotaRepository.obterQtdeCotaMunicipio(page, rp, sortname, sortorder);
	}
	
	@Override
	@Transactional
	public void salvarGrupoCotas(Long idGrupo, List<Long> idCotas, String nome,
			List<DiaSemana> diasSemana, TipoCaracteristicaSegmentacaoPDV tipoCota) {
		
		HashSet<Cota> cotas = new HashSet<Cota>();
		
		for(Long id : idCotas) {
			cotas.add(cotaRepository.buscarPorId(id));
		}
		
		GrupoCota grupo;
				
		if(idGrupo == null)
			grupo = new GrupoCota();
		else
			grupo = grupoRepository.buscarPorId(idGrupo);
			
		grupo.setId(idGrupo);
		grupo.setNome(nome);
		grupo.setDiasRecolhimento(new HashSet<DiaSemana>(diasSemana));
		grupo.setTipoGrupo(TipoGrupo.TIPO_COTA);
		grupo.setTipoCota(tipoCota);
		grupo.setCotas(cotas);
		
		grupoRepository.merge(grupo);
	}

	@Override
	@Transactional
	public void salvarGrupoMunicipios(Long idGrupo, List<Long> idMunicipios,
			String nome, List<DiaSemana> diasSemana) {
		
		HashSet<Localidade> municipios = new HashSet<Localidade>();
		
		for(Long id : idMunicipios) {
			municipios.add(localidadeRepository.buscarPorId(id));
		}
		
		GrupoCota grupo;
		
		if(idGrupo == null)
			grupo = new GrupoCota();
		else
			grupo = grupoRepository.buscarPorId(idGrupo);
						
		grupo.setId(idGrupo);
		grupo.setNome(nome);
		grupo.setDiasRecolhimento(new HashSet<DiaSemana>(diasSemana));
		grupo.setTipoGrupo(TipoGrupo.MUNICIPIO);
		grupo.setMunicipios(municipios);
		
		grupoRepository.merge(grupo);
	}

	@Override
	@Transactional
	public List<Long> obterMunicipiosDoGrupo(Long idGrupo) {

		GrupoCota grupo = grupoRepository.buscarPorId(idGrupo);
		
		List<Long> ids = new ArrayList<Long>();
				
		for(Localidade local : grupo.getMunicipios())
			ids.add(local.getId());
				
		return ids;
	}

	@Override
	@Transactional
	public List<Long> obterCotasDoGrupo(Long idGrupo) {
		GrupoCota grupo = grupoRepository.buscarPorId(idGrupo);
		
		List<Long> ids = new ArrayList<Long>();
		
		for(Cota cota : grupo.getCotas())
			ids.add(cota.getId());
				
		return ids;
	}
}
