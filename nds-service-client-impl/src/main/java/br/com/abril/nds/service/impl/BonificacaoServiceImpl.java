package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.planejamento.Bonificacao;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.repository.BonificacaoRepository;
import br.com.abril.nds.service.BonificacaoService;

@Service
public class BonificacaoServiceImpl implements BonificacaoService {
	
	@Autowired
	private BonificacaoRepository bonificacaoRepository;
	
	@Override
	@Transactional
	public void salvarBonificacoes(EstudoGerado estudo, List<BonificacaoDTO> bonificacaoDTOs) {
		for (BonificacaoDTO bonificacaoDTO : bonificacaoDTOs) {
			bonificacaoRepository.adicionar(extrairModel(estudo, bonificacaoDTO));
		}
	}

	private Bonificacao extrairModel(EstudoGerado estudo, BonificacaoDTO bonificacaoDTO) {
		Bonificacao bonificacao = new Bonificacao();
		
		bonificacao.setBonificacao(bonificacaoDTO.getBonificacao().intValue());
		bonificacao.setComponente(bonificacaoDTO.getComponente());
		bonificacao.setElemento(bonificacaoDTO.getElemento());
		bonificacao.setEstudo(estudo);
		bonificacao.setReparteMinimo(bonificacaoDTO.getReparteMinimoBigInteger());
		bonificacao.setTodasAsCotas(bonificacaoDTO.isTodasAsCotas());
		
		return bonificacao;
	}

}
