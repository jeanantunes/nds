package br.com.abril.nds.service.impl;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class RoteirizacaoServiceImpl implements RoteirizacaoService {
	
	
	@Autowired
	private RoteiroRepository roteiroRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;

	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao) {
		return roteiroRepository.buscarRoteiro(sortname, ordenacao);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRota(String sortname, Ordenacao ordenacao) {
		return rotaRepository.buscarRota(sortname, ordenacao);
	}

	@Override
	@Transactional
	public void incluirRoteiro(Roteiro roteiro) {
		roteiroRepository.adicionar(roteiro);
		roteiroRepository.atualizaOrdenacao(roteiro);
		
	}
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiroPorDescricao(String descricao,  MatchMode matchMode ){
		return  roteiroRepository.buscarRoteiroPorDescricao(descricao, matchMode);
	}
	
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaPorRoteiro(Long idRoteiro){
		return  rotaRepository.buscarRotaPorRoteiro(idRoteiro, "ordem", Ordenacao.ASC);
	}
	
	@Override
	@Transactional
	public void incluirRota(Rota rota) {
		
		rotaRepository.adicionar(rota); 
		
		rotaRepository.atualizaOrdenacao(rota);
		
	}
	
	@Override
	@Transactional
	public void  excluirListaRota(List<Long> rotasId) {
		for (Long rotaId : rotasId ){
			Rota rota = new Rota();
			rota.setId(rotaId);
			rota.setOrdem(0);
			rotaRepository.remover(rota);
		}	
		
		
	}

	@Override
	@Transactional
	public void transferirListaRota(List<Long> rotasId, Long roteiroId) {
		Roteiro roteiro  = new Roteiro();
		roteiro.setId(roteiroId);
		for (Long rotaId : rotasId ){
			Rota rota = rotaRepository.buscarPorId(rotaId);
			//Rota rota = new Rota();
			rota.setRoteiro(roteiro);
			rota.setId(rotaId);
			rota.setOrdem(0);
			rotaRepository.merge(rota);
		}	
		
	}

	@Override
	@Transactional
	public void transferirListaRotaComNovoRoteiro(List<Long> rotasId,
			Roteiro roteiro) {
		 incluirRoteiro(roteiro);
		 transferirListaRota(rotasId, roteiro.getId());
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRotas() {
		
		return rotaRepository.buscarTodos();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiros() {
		
		return roteiroRepository.buscarTodos();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Roteiro> buscarRoteiroDeBox(Long idBox){
		
		return roteiroRepository.buscarRoteiroDeBox(idBox);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaDeBox(Long idBox){
		
		return rotaRepository.buscarRotaDeBox(idBox);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota){
		
		return roteirizacaoRepository.buscarRoteirizacaoDeCota(numeroCota);
	}
	
	@Transactional(readOnly=true)
	public List<Rota> buscarRotaPorRoteiro(String descRoteiro){
		return  rotaRepository.buscarRotaDeRoteiro(descRoteiro);
	}
}
