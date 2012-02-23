package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.FuroProdutoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.FuroProdutoService;

@Service
public class FuroProdutoServiceImpl implements FuroProdutoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FuroProdutoRepository furoProdutoRepository;
	
	@Transactional
	@Override
	public void efetuarFuroProduto(Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario) {		
		if (idProdutoEdicao == null){
			throw new IllegalArgumentException("Id produto edição é obrigatório.");
		}
		
		if (idLancamento == null){
			throw new IllegalArgumentException("Lançamento é obrigatório.");
		}
		
		if (novaData == null){
			throw new IllegalArgumentException("Data Lançamento é obrigatório.");
		}
		
		if (idUsuario == null){
			throw new IllegalArgumentException("Id usuário é obrigatório.");
		}
		
		//TODO tem q ter registro aqui mesmo?
		FuroProduto furoProduto = new FuroProduto();
		furoProduto.setData(new Date());
		Lancamento lancamento = new Lancamento();
		lancamento.setId(idLancamento);
		furoProduto.setLancamento(lancamento);
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(idProdutoEdicao);
		furoProduto.setProdutoEdicao(produtoEdicao);
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);
		furoProduto.setUsuario(usuario);
		this.furoProdutoRepository.adicionar(furoProduto);
		
		//editar registro na tabela de lançamento com nova data e zerar reparte
		this.lancamentoRepository.atualizarLancamento(idLancamento, novaData);
		
		//TODO criar registro no estoque (movimento_estoque)
	}

}
