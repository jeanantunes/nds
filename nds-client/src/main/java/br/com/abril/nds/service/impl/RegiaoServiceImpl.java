package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.repository.RegiaoRepository;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.distribuicao.regiao}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class RegiaoServiceImpl implements RegiaoService {
	
	@Autowired
	private RegiaoRepository regiaoRepository;

	@Autowired
	private RegistroCotaRegiaoRepository registroCotaRegiaoRepository;
	
	@Override
	@Transactional
	public void salvarRegiao(Regiao regiao) {
		//validarParametrosObrigatoriosRegiao(regiao);
		regiaoRepository.adicionar(regiao);
		}
	
	@Transactional(readOnly=true)
	@Override
	public List<RegiaoDTO> buscarRegiao() {
		return regiaoRepository.buscarRegiao();
	}
	
	/**
	 * Valida as informações referente ao cadasto de uma nova cota.
	 * 
	 * @param objRegiao
	 */
//	private void validarParametrosObrigatoriosRegiao(Regiao regiao) {
//		
//		List<String> mensagensValidacao = new ArrayList<String>();
//		
//		if (regiao.getNomeRegiao() == null) {
//			mensagensValidacao.add("O preenchimento do campo [nome] é obrigatório!");
//		}
//	}

	@Override
	@Transactional
	public List<RegiaoCotaDTO> carregarCotasRegiao(Long id) {		
		return regiaoRepository.carregarCotasRegiao(id);
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
		
//		registroCotaRegiaoRepository.removerPorId(id);
		
	}
	
	@Override
	@Transactional
	public List<RegiaoCotaDTO> buscarPorCEP(FiltroCotasRegiaoDTO filtro) {
		
		return regiaoRepository.buscarPorCEP(filtro);
	}

	@Override
	@Transactional
	public void addCotaNaRegiao(RegistroCotaRegiao registro) {
		registroCotaRegiaoRepository.adicionar(registro);
	}

	@Override
	@Transactional
	public Regiao obterRegiaoPorId(Long idRegiao) {
		if (idRegiao == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Id da cota não pode ser nulo.");
		}
		
		return this.regiaoRepository.buscarPorId(idRegiao);
	}
	
	
	
	
	
	//implementar nome as cotas
	//String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
	//percorrer lista e setar dentro a lista de cotas por regiao...as

}

