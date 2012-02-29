package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
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
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
		
		if (lancamento == null){
			throw new IllegalArgumentException("Lançamento não encontrado.");
		}
		
		if (novaData.equals(lancamento.getDataLancamentoDistribuidor()) 
				|| novaData.before(lancamento.getDataLancamentoDistribuidor())){
			throw new IllegalArgumentException("Nova data deve ser maior que a data de lançamento atual.");
		}
		
		if (lancamento.getStatus().equals(StatusLancamento.EXPEDIDO)){
			throw new IllegalArgumentException("Produto já expedido não pode sofrer furo.");
		}
		
		lancamento.setDataLancamentoDistribuidor(novaData);
		lancamento.setStatus(StatusLancamento.FURO);
		
		FuroProduto furoProduto = new FuroProduto();
		furoProduto.setData(new Date());
		furoProduto.setLancamento(lancamento);
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(idProdutoEdicao);
		furoProduto.setProdutoEdicao(produtoEdicao);
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);
		furoProduto.setUsuario(usuario);
		this.furoProdutoRepository.adicionar(furoProduto);
		
		this.lancamentoRepository.alterar(lancamento);
	}

}
