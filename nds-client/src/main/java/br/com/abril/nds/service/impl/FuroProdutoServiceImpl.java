package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.FuroProdutoService;

@Service
public class FuroProdutoServiceImpl implements FuroProdutoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Transactional
	@Override
	public void efetuarFuroProduto(Long idProdutoEdicao, Long idLancamento, Date novaData) {		
		if (idProdutoEdicao == null){
			throw new IllegalArgumentException("Id produto edição é obrigatório.");
		}
		
		if (idLancamento == null){
			throw new IllegalArgumentException("Lançamento é obrigatório.");
		}
		
		if (novaData == null){
			throw new IllegalArgumentException("Data Lançamento é obrigatório.");
		}
		
		//editar registro na tabela de lançamento com nova data e zerar reparte
		this.lancamentoRepository.atualizarLancamento(idLancamento, novaData);
		
		//TODO criar registro no estoque (movimento_estoque)
	}

}
