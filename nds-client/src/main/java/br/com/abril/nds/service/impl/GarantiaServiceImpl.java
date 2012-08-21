package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.repository.GarantiaRepository;
import br.com.abril.nds.service.GarantiaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class GarantiaServiceImpl implements GarantiaService {

	@Autowired
	private GarantiaRepository garantiaRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<Garantia> obterGarantiasFiador(Long idFiador, Set<Long> idsIgnorar) {
		
		return this.garantiaRepository.obterGarantiasFiador(idFiador, idsIgnorar);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Garantia buscarGarantiaPorId(Long idGarantia){
		return this.garantiaRepository.buscarPorId(idGarantia);
	}
	
	public void salvarGarantias(List<Garantia> listaGarantias, Fiador fiador){
		
		if (listaGarantias != null && !listaGarantias.isEmpty()){
			
			for (Garantia garantia : listaGarantias){
				this.validarDadosGarantia(garantia);
				
				if (fiador != null){
					garantia.setFiador(fiador);
				}
				
				if (garantia.getId() == null){
					
					this.garantiaRepository.adicionar(garantia);
				} else {
					
					this.garantiaRepository.alterar(garantia);
				}
			}
		}
	}
	
	public void removerGarantias(Set<Long> idsGarantias){
		
		if (idsGarantias != null && !idsGarantias.isEmpty()){
			
			this.garantiaRepository.removerGarantias(idsGarantias);
		}
	}
	
	private void validarDadosGarantia(Garantia garantia){
		
		List<String> msgsValidacao = new ArrayList<String>();
		
		if (garantia == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Garantia é obrigatória.");
		}
		
		if (garantia.getValor() == null){
			msgsValidacao.add("Valor é obrigatório.");
		}
		
		if (garantia.getDescricao() == null || garantia.getDescricao().trim().isEmpty()){
			msgsValidacao.add("Descrição é obrigatório.");
		}
		
		if (!msgsValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgsValidacao));
		}
	}

	@Override
	@Transactional
	public void removerGarantiasPorFiador(Long idFiador) {
		
		this.garantiaRepository.removerGarantiasPorFiador(idFiador);
	}
}