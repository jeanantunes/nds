package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.service.CotaUnificacaoService;

/**
 * Classe de implementação referente ao serviço da entidade
 * {@link br.com.abril.nds.model.cadastro.CotaUnificacao}
 * 
 * @author Discover Technology
 * 
 */
@Service
public class CotaUnificacaoServiceImpl implements CotaUnificacaoService {

	@Autowired
	private CotaUnificacaoRepository cotaUnificacaoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	/**
	 * Valida na alteração se Cota já está em outra Centralização como Cota Centralizada ou Centralizadora
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCota
	 */
	@Transactional
	@Override
	public void validaAlteracaoUnificacaoCota(Long cotaUnificacaoId,
            							       Integer numeroCota){
		
		CotaUnificacao cotaUnificacao = this.obterCotaUnificacaoPorCotaCentralizadora(numeroCota);
		
        if (cotaUnificacao!=null && !cotaUnificacao.getId().equals(cotaUnificacaoId)){
			
	        throw new ValidacaoException(TipoMensagem.WARNING, "Cota "+numeroCota+" já é uma cota Centralizadora !");	
		}
		
        cotaUnificacao = this.obterCotaUnificacaoPorCotaCentralizada(numeroCota);
        		
        if (cotaUnificacao!=null && !cotaUnificacao.getId().equals(cotaUnificacaoId)){
			
	        throw new ValidacaoException(TipoMensagem.WARNING, "Cota "+numeroCota+" já é uma cota Centralizada !");	
		}
	}

	/**
	 * Valida na inclusão se Cota já está em outra Centralização como Cota Centralizada ou Centralizadora
	 * 
	 * @param numeroCota
	 */
	@Transactional
	@Override
	public void validaNovaUnificacaoCota(Integer numeroCota){
		
        if (this.isCotaCentralizadora(numeroCota)){
			
	        throw new ValidacaoException(TipoMensagem.WARNING, "Cota "+numeroCota+" já é uma cota Centralizadora !");	
		}
		
        if (this.isCotaCentralizada(numeroCota)){
			
	        throw new ValidacaoException(TipoMensagem.WARNING, "Cota "+numeroCota+" já é uma cota Centralizada !");	
		}
	}
	
	/**
	 * Salva/Altera Unificação de Cota
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotaCentralizada
	 */
	@Transactional
	@Override
	public void salvarCotaUnificacao(Long cotaUnificacaoId,
			                         Integer numeroCotaCentralizadora, 
			                         Integer numeroCotaCentralizada){
		
		CotaUnificacao cotaUnificacao = new CotaUnificacao();
		
		if (cotaUnificacaoId!=null){
			
			this.validaAlteracaoUnificacaoCota(cotaUnificacaoId, numeroCotaCentralizadora);
			
			this.validaAlteracaoUnificacaoCota(cotaUnificacaoId, numeroCotaCentralizada);
			
			cotaUnificacao = this.cotaUnificacaoRepository.buscarPorId(cotaUnificacaoId);
		}
		else{
			
			this.validaNovaUnificacaoCota(numeroCotaCentralizadora);
			
			this.validaNovaUnificacaoCota(numeroCotaCentralizada);
		}
		
		Cota cotaCentralizadora = this.cotaRepository.obterPorNumerDaCota(numeroCotaCentralizadora);
		
		Cota cotaCentralizada = this.cotaRepository.obterPorNumerDaCota(numeroCotaCentralizada);
		
		cotaUnificacao.setCota(cotaCentralizadora);
		
		cotaUnificacao.adicionarCota(cotaCentralizada);
		
		cotaUnificacao.setDataUnificacao(new Date());
		
		this.cotaUnificacaoRepository.merge(cotaUnificacao);
	}
	
	/**
	 * Salva/Altera Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotasCentralizadas
	 */
	@Transactional
	@Override
	public void salvarCotaUnificacao(Long cotaUnificacaoId,
			                         Integer numeroCotaCentralizadora, 
			                         List<Integer> numeroCotasCentralizadas){
		
        for (Integer numeroCotaCentralizada : numeroCotasCentralizadas){
        	
        	this.salvarCotaUnificacao(cotaUnificacaoId, 
        			                  numeroCotaCentralizadora, 
        			                  numeroCotaCentralizada);
        }
	}

	/**
	 * Remove Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 */
	@Transactional
	@Override
	public void removerCotaUnificacao(Long cotaUnificacaoId) {
		
        CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.buscarPorId(cotaUnificacaoId);
        
        Set<Cota> cotas = cotaUnificacao.getCotas();
		
        for (Cota c: cotas){
        	
        	c.setCotaUnificacao(null);
        	
        	this.cotaRepository.merge(c);
        }
        
        this.cotaUnificacaoRepository.remover(cotaUnificacao);
	}

	/**
	 * Verifica se Cota é Centralizadora
	 * 
	 * @param numeroCota
	 * @return boolean
	 */
	@Transactional
	@Override
	public boolean isCotaCentralizadora(Integer numeroCota) {

		CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(numeroCota);

		return cotaUnificacao!=null;
	}

	/**
	 * Verifica se Cota é Centralizada
	 * 
	 * @param numeroCota
	 * @return boolean
	 */
	@Transactional
	@Override
	public boolean isCotaCentralizada(Integer numeroCota) {
		
		CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizada(numeroCota);
		
		return cotaUnificacao!=null;
	}

	/**
	 * Verifica se Cota é Centralizadora
	 * 
	 * @param idCota
	 * @return boolean
	 */
	@Transactional
	@Override
	public boolean isCotaCentralizadora(Long idCota) {
		
		CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(idCota);

		return cotaUnificacao!=null;
	}

	/**
	 * Verifica se Cota é Centralizada
	 * 
	 * @param idCota
	 * @return boolean
	 */
	@Transactional
	@Override
	public boolean isCotaCentralizada(Long idCota) {
		
        CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizada(idCota);
		
		return cotaUnificacao!=null;
	}

	/**
	 * Obtem Cota Unificacao por Cota Centralizadora
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	@Transactional
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Integer numeroCota) {
		
        CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(numeroCota);
		
		return cotaUnificacao;
	}

	/**
	 * Obtem Cota Unificacao por Cota Centralizada
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	@Transactional
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota) {
		
		CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizada(numeroCota);
			
		return cotaUnificacao;
	}

	/**
	 * Obtem Cota Unificacao por Cota Centralizadora
	 * 
	 * @param idCota
	 * @return CotaUnificacao
	 */
	@Transactional
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Long idCota) {
		
        CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(idCota);
		
		return cotaUnificacao;
	}

	/**
	 * Obtem Cota Unificacao por Cota Centralizada
	 * 
	 * @param idCota
	 * @return CotaUnificacao
	 */
	@Transactional
	@Override
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Long idCota) {
		
        CotaUnificacao cotaUnificacao = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizada(idCota);
		
		return cotaUnificacao;
	}
}