package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.CotaUnificacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
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
	
	@Autowired
	private ParametroCobrancaCotaRepository parametroCobrancaCotaRepository;
	
	/**
	 * Salva/Altera Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotasCentralizadas
	 */
	@Transactional
	@Override
	public void salvarCotaUnificacao(Integer numeroCotaCentralizadora, 
			                         List<Integer> numeroCotasCentralizadas){
		
		this.validarCotasCentralizadas(numeroCotasCentralizadas, numeroCotaCentralizadora);
		
		if (numeroCotaCentralizadora == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Infome a cota centralizadora.");
		}
		
		CotaUnificacao cotaUnificacao = this.obterCotaUnificacaoPorCotaCentralizadora(numeroCotaCentralizadora);
		
		//caso cota já seja unificadora
		if (cotaUnificacao != null){
			
			//remove cotas não centralizadas
			cotaUnificacao.getCotas().clear();
		} else {
			
			cotaUnificacao = new CotaUnificacao();
			cotaUnificacao.setCota(this.cotaRepository.obterPorNumeroDaCota(numeroCotaCentralizadora));
			cotaUnificacao.setDataUnificacao(new Date());
		}
		
		//adiciona novas cotas na centralização
		for (Integer numeroCota : numeroCotasCentralizadas) {
			
			Cota novaCotaCentralizada = this.cotaRepository.obterPorNumeroDaCota(numeroCota);
			novaCotaCentralizada.setParametroCobranca(null);
			cotaRepository.merge(novaCotaCentralizada);
			
			cotaUnificacao.adicionarCota(novaCotaCentralizada);
			
			//apaga forma de cobrança da cota que acaba de ser unificada
			ParametroCobrancaCota p = this.parametroCobrancaCotaRepository.obterParametroCobrancaCotaPorCota(numeroCota);
			
			if (p != null) {
				
				this.parametroCobrancaCotaRepository.alterar(p);
			}
		}
		
		if (cotaUnificacao.getId() == null){
			
			this.cotaUnificacaoRepository.adicionar(cotaUnificacao);
		} else {
			
			this.cotaUnificacaoRepository.alterar(cotaUnificacao);
		}
	}

	private void validarCotasCentralizadas(List<Integer> numeroCotasCentralizadas,
			Integer numeroCotaCentralizadora) {
		
		if (numeroCotasCentralizadas != null && !numeroCotasCentralizadas.isEmpty()){
			
			List<String> msgs = new ArrayList<String>();
			
			CotaUnificacao cotaUnificacao = null;
			
			for (Integer numeroCota : numeroCotasCentralizadas){
				
				cotaUnificacao =
					this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizada(numeroCota);
				
				if (cotaUnificacao != null && 
						!cotaUnificacao.getCota().getNumeroCota().equals(numeroCotaCentralizadora)){
					
					msgs.add("Cota " + numeroCota + " já está centralizada na cota " + 
					cotaUnificacao.getCota().getNumeroCota());
				}
				
				if (this.cotaUnificacaoRepository.verificarCotaUnificadora(numeroCota)){
					
					msgs.add("Cota " + numeroCota + " já é centralizadora");
				}
			}
			
			if (!msgs.isEmpty()){
				
				throw new ValidacaoException(TipoMensagem.WARNING, msgs);
			}
		}
	}

	/**
	 * Obtem Cota Unificacao por Cota Centralizadora
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	private CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Integer numeroCota) {
		
        CotaUnificacao cotaUnificacao = 
        	this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(
        		numeroCota);
		
		return cotaUnificacao;
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora) {
		
		if (numeroCotaCentralizadora == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota unificadora é obrigatório.");
		}
		
		return this.cotaUnificacaoRepository.obterCotasCentralizadas(
				numeroCotaCentralizadora);
	}

	@Transactional(readOnly=true)
	@Override
	public List<CotaUnificacaoDTO> obterCotasUnificadas() {
		
		List<CotaUnificacaoDTO> ret = new ArrayList<CotaUnificacaoDTO>();
		
		List<Integer> numCotas = 
			this.cotaUnificacaoRepository.buscarNumeroCotasUnificadoras();
		
		for (Integer numeroCota : numCotas){
			
			CotaUnificacaoDTO c = new CotaUnificacaoDTO();
			c.setNumeroCota(numeroCota);
			ret.add(c);
			
			List<CotaVO> cotasUnificadas = 
				this.cotaUnificacaoRepository.obterCotasCentralizadas(numeroCota);
			
			c.setCotas(cotasUnificadas);
		}
		
		return ret;
	}

	@Transactional(readOnly=true)
	@Override
	public CotaVO obterCota(Integer numeroCota, boolean edicao) {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (!edicao){
			if (this.cotaUnificacaoRepository.verificarCotaUnificadora(numeroCota)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "A cota "+ numeroCota +" já é unificadora.");
			}
			
			if (this.cotaUnificacaoRepository.verificarCotaUnificada(numeroCota)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "A cota "+ numeroCota +" já está unificada.");
			}
		}
		
		return this.cotaRepository.obterDadosBasicosCota(numeroCota);
	}

	@Transactional
	@Override
	public void removerCotaUnificacao(Integer numeroCota) {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		CotaUnificacao cotaUnificacao = 
			this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(numeroCota);
		
		
		this.cotaUnificacaoRepository.remover(cotaUnificacao);
	}
}
