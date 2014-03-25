package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaBaseHistoricoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.repository.CotaBaseRepository;
import br.com.abril.nds.service.CotaBaseService;

@Service
public class CotaBaseServiceImpl implements CotaBaseService {
	
	@Autowired
	private CotaBaseRepository cotaBaseRepository;
	
	@Override
	@Transactional(readOnly = true)
	public FiltroCotaBaseDTO obterDadosFiltro(CotaBase cotaBase, boolean obterFaturamento, boolean semCotaBase,  Integer numeroCota) {		 
		return cotaBaseRepository.obterDadosFiltro(cotaBase, obterFaturamento, semCotaBase, numeroCota);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaBaseDTO> obterCotasBases(CotaBase cotaBase, CotaBaseDTO dto) {
		return cotaBaseRepository.obterCotasBases(cotaBase, dto);
	}

	@Override
	@Transactional
	public void salvar(CotaBase cotaBase) {
		this.cotaBaseRepository.adicionar(cotaBase); 
	}

	@Override
	@Transactional(readOnly = true)
	public CotaBase obterCotaNova(Integer numeroCotaNova, Boolean ativo) {		
		return this.cotaBaseRepository.obterCotaNova(numeroCotaNova, ativo);
	}

	@Override
	@Transactional(readOnly = true)
	public FiltroCotaBaseDTO obterCotaDoFiltro(CotaBase cotaBase) {
		return this.cotaBaseRepository.obterCotaDoFiltro(cotaBase);
	}

	@Override
	@Transactional
	public void atualizar(CotaBase cotaBaseJaSalva) {
		 this.cotaBaseRepository.alterar(cotaBaseJaSalva);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaBaseHistoricoDTO> obterCotasHistorico(CotaBase cotaBase, CotaBaseDTO dto) {
		return this.cotaBaseRepository.obterCotasHistorico(cotaBase, dto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaBaseDTO> obterListaCotaPesquisaGeral(CotaBaseDTO dto){
		return this.cotaBaseRepository.obterListaCotaPesquisaGeral(dto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaBaseDTO> obterListaTelaDetalhe(CotaBase cotaBase) {
		
		if (cotaBase == null) {
			
			return null;
		}
		
		return this.cotaBaseRepository.obterListaTelaDetalhe(cotaBase);
	}

}
