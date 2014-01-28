package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.GrupoService;

@Service
public class GrupoServiceImpl implements GrupoService {
	

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	@Transactional
	public List<GrupoCotaDTO> obterTodosGrupos(String sortname, String sortorder) {
		
		List<GrupoCota> grupos = grupoRepository.obterGrupos(sortname, sortorder);
		
		List<GrupoCotaDTO> gruposDTO = new ArrayList<GrupoCotaDTO>();

		for(GrupoCota grupo: grupos) {
			
			GrupoCotaDTO dto = new GrupoCotaDTO();
			
			dto.setDiasSemana(new ArrayList<DiaSemana>());
			
			StringBuilder dias = montarStringDiasRecolhimento(grupo.getDiasRecolhimento(), dto);
						
			dto.setIdGrupo(grupo.getId());
			dto.setNome(grupo.getNome()==null? "":grupo.getNome());
			dto.setRecolhimento(dias.toString());
			dto.setTipoCota(grupo.getTipoCota());
			dto.setTipoGrupo(grupo.getTipoGrupo());
			
			gruposDTO.add(dto);			
		}
		
		return gruposDTO;
	}

	private StringBuilder montarStringDiasRecolhimento(Set<DiaSemana> diasRecolhimento, GrupoCotaDTO dto) {
		
		StringBuilder dias = null;
		
		List<DiaSemana> diasOrdenados = new ArrayList<DiaSemana>(diasRecolhimento);
		
		Collections.sort(diasOrdenados);
		
		for(DiaSemana dia : diasOrdenados){
			if(dias == null)
				dias = new StringBuilder();					
			else
				dias.append(" - ");
			
			dto.getDiasSemana().add(dia);
			dias.append(dia.getDescricaoDiaSemana());
		}
		
		return dias;
	}

	@Transactional
	@Override
	public Integer countTodosGrupos() {
		
		return grupoRepository.countTodosGrupos();
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
	public void salvarGrupoCotas(Long idGrupo, Set<Long> idCotas, String nome,
			List<DiaSemana> diasSemana, TipoCaracteristicaSegmentacaoPDV tipoCota) {
		
		this.validarNomeGrupo(nome, idGrupo);
		
		Set<Cota> cotas = this.validarCotaJaPertenceGrupo(idGrupo, idCotas, null);
		
		GrupoCota grupo;
				
		if(idGrupo == null) {
			grupo = new GrupoCota();
			grupo.setDataCadastro(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		} else {
			grupo = grupoRepository.buscarPorId(idGrupo);
		}
		
		grupo.setNome(nome);
		grupo.setDiasRecolhimento(new HashSet<DiaSemana>(diasSemana));
		grupo.setTipoGrupo(TipoGrupo.TIPO_COTA);
		grupo.setTipoCota(tipoCota);
		grupo.setCotas(cotas);
		
		grupoRepository.merge(grupo);
	}

	@Override
	@Transactional
	public void salvarGrupoMunicipios(Long idGrupo, Set<String> municipios,
			String nome, List<DiaSemana> diasSemana) {
		
		this.validarNomeGrupo(nome, idGrupo);
		
		this.validarCotaJaPertenceGrupo(idGrupo, null, municipios);
		
		GrupoCota grupo;
		
		if(idGrupo == null) {
			grupo = new GrupoCota();
			grupo.setDataCadastro(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		} else {
			grupo = grupoRepository.buscarPorId(idGrupo);
		}
		
		grupo.setNome(nome);
		grupo.setDiasRecolhimento(new HashSet<DiaSemana>(diasSemana));
		grupo.setTipoGrupo(TipoGrupo.MUNICIPIO);
		grupo.setMunicipios(municipios);
		
		grupoRepository.merge(grupo);
	}

	private void validarNomeGrupo(String nome, Long idGrupo) {
		
		if (nome == null || nome.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um nome válido para o grupo!");
		}
		
		if (this.grupoRepository.existeGrupoCota(nome, idGrupo)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Já existe um grupo cadastrado com o nome: " + nome + "!");
		}
	}
	
	private Set<Cota> validarCotaJaPertenceGrupo(Long idGrupo, Set<Long> idCotas, Set<String> municipios){
		
		List<String> msgs = new ArrayList<>();
		
		Set<Cota> cotas = null;
		
		if (idCotas != null && !idCotas.isEmpty()){
			
			cotas = new HashSet<Cota>();
			for(Long id : idCotas) {
				
				Cota cota = cotaRepository.buscarPorId(id);
				
				String nomeGrupo = grupoRepository.obterNomeGrupoPorCota(id, idGrupo);
				
				if (nomeGrupo == null){
					
					if (cota.getEnderecoPrincipal() != null){
						
						nomeGrupo = this.grupoRepository.obterNomeGrupoPorMunicipio(
							cota.getEnderecoPrincipal().getEndereco().getCidade(), null);
					}
				}
				
				if(nomeGrupo != null){
					msgs.add("Cota " + cota.getNumeroCota() + " já pertence ao grupo '" + nomeGrupo + "'.");
				}
				cotas.add(cota);
			}
		}
		
		if (municipios != null && !municipios.isEmpty()){
			
			for (String municipio : municipios){
				
				String nomeGrupo = this.grupoRepository.obterNomeGrupoPorMunicipio(municipio, idGrupo);
				
				if (nomeGrupo == null){
					
					List<Long> cotasNoMunicipio = this.cotaRepository.obterIdsCotasPorMunicipio(municipio);
					
					for (Long idCota : cotasNoMunicipio){
						
						nomeGrupo = this.grupoRepository.obterNomeGrupoPorCota(idCota, idGrupo);
						
						if(nomeGrupo != null){
							msgs.add("Cota " + this.cotaRepository.buscarNumeroCotaPorId(idCota) + 
							" (municipio de " + municipio + ")" +
							" já pertence ao grupo '" + nomeGrupo + "'.");
						}
					}
				} else {
					msgs.add("Já existe grupo ('"+ nomeGrupo +"') para o municipio " + municipio);
				}
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
		
		return cotas;
	}

	@Override
	@Transactional
	public Set<String> obterMunicipiosDoGrupo(Long idGrupo) {

		return this.grupoRepository.obterMunicipiosCotasGrupo(idGrupo);
	}

	@Override
	@Transactional
	public Set<Long> obterCotasDoGrupo(Long idGrupo) {
		
		return this.grupoRepository.obterIdsCotasGrupo(idGrupo);
	}
}