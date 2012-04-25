package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ChamadaoRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.ChamadaoService;

/**
 * Classe de implementação de serviços referentes
 * ao chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Service
public class ChamadaoServiceImpl implements ChamadaoService {

	@Autowired
	protected ChamadaoRepository chamadaoRepository;

	@Autowired
	protected ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	protected ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Autowired
	protected LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected CotaRepository cotaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaChamadaoDTO obterConsignados(FiltroChamadaoDTO filtro) {
		
		ConsultaChamadaoDTO consultaChamadaoDTO = new ConsultaChamadaoDTO();
		consultaChamadaoDTO.setListaConsignadoCotaChamadaoDTO(this.chamadaoRepository.obterConsignadosParaChamadao(filtro));
		consultaChamadaoDTO.setResumoConsignadoCotaChamadao(this.obterResumoConsignados(filtro));
		
		if (consultaChamadaoDTO.getResumoConsignadoCotaChamadao() != null){
			consultaChamadaoDTO.getResumoConsignadoCotaChamadao().setQtdProdutosTotal(this.obterTotalConsignados(filtro));
		}
		
		return consultaChamadaoDTO;
	}
	
	private Long obterTotalConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterTotalConsignadosParaChamadao(filtro);
	}
	
	private ResumoConsignadoCotaChamadaoDTO obterResumoConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterResumoConsignadosParaChamadao(filtro);
	}
	
	@Override
	@Transactional
	public void confirmarChamacao(List<Long> listaLancamento, Integer numeroCota,
								  Date dataChamadao, boolean chamarTodos) {
		
		Lancamento lancamento = null;
		
		for (Long idLancamento : listaLancamento) {
		
			lancamento = lancamentoRepository.buscarPorId(idLancamento);
		
			ChamadaEncalhe chamadaEncalhe = new ChamadaEncalhe();  
				//chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(produtoEdicao,infoEncalheDTO.getDataAntecipacao());
			
			chamadaEncalhe.setFinalRecolhimento(dataChamadao);
			chamadaEncalhe.setInicioRecolhimento(dataChamadao);
			chamadaEncalhe.setProdutoEdicao(lancamento.getProdutoEdicao());
			chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
			
			chamadaEncalhe = chamadaEncalheRepository.merge(chamadaEncalhe);
			
			ChamadaEncalheCota chamadaEncalheCota = null;
			
			Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
			
			chamadaEncalheCota = new ChamadaEncalheCota();
			chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
			chamadaEncalheCota.setConferido(Boolean.FALSE);
			chamadaEncalheCota.setCota(cota);
			
			//TODO:
			chamadaEncalheCota.setQtdePrevista(BigDecimal.ZERO);
			
			chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
		}
	}
	
}
