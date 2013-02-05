package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.RegiaoRepository;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;
import br.com.abril.nds.service.RegiaoService;


public class RegiaoServiceImpl implements RegiaoService  {
	
	@Autowired
	private RegiaoRepository regiaoRepository;

//	@Autowired
//	private TipoSegmentoProdutoRepository segmento;
	
	@Autowired
	private RegistroCotaRegiaoRepository registroCotaRegiaoRepository;
	
	@Override
	@Transactional
	public void salvarRegiao(Regiao regiao) {
		regiaoRepository.adicionar(regiao);
		}
	
	@Transactional(readOnly=true)
	@Override
	public List<RegiaoDTO> buscarRegiao() {
		return regiaoRepository.buscarRegiao();
	}
	
	@Override
	@Transactional
	public List<RegiaoCotaDTO> carregarCotasRegiao(FiltroCotasRegiaoDTO filtro) {		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro n�o deve ser nulo.");
		return registroCotaRegiaoRepository.carregarCotasRegiao(filtro);
	}

	@Override
	@Transactional
	public void excluirRegiao(Long id) {
		Regiao regiao = this.regiaoRepository.buscarPorId(id);
		
		this.regiaoRepository.remover(regiao);
	}

	@Override
	@Transactional
	public void excluirRegistroCotaRegiao(Long id) {
		RegistroCotaRegiao registro = this.registroCotaRegiaoRepository.buscarPorId(id); 
		registroCotaRegiaoRepository.remover(registro);
		
	}
	
	@Override
	@Transactional
	public List<RegiaoCotaDTO> buscarPorCEP(FiltroCotasRegiaoDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro n�o deve ser nulo.");
		
		return registroCotaRegiaoRepository.buscarPorCEP(filtro);
	}

	@Override
	@Transactional
	public void addCotaNaRegiao(RegistroCotaRegiao registro) {
		registroCotaRegiaoRepository.adicionar(registro);
	}

	@Override
	@Transactional
	public Regiao obterRegiaoPorId(Long idRegiao) {
		return this.regiaoRepository.buscarPorId(idRegiao);
	}

	@Override
	@Transactional
	public List<TipoSegmentoProduto> carregarSegmentos() {
		//return segmento.buscarTodos();
		return null;
	}


	@Override
	@Transactional
	public void alterarRegiao(Regiao regiao) {
		regiaoRepository.merge(regiao);
	}
	
	
	//implementar nome as cotas
	//String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
	//percorrer lista e setar dentro a lista de cotas por regiao...as
	
	
}
