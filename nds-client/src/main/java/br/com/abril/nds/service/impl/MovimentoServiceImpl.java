package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.DominioTipoMovimento;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.MovimentoCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.MovimentoService;

@Service
public class MovimentoServiceImpl implements MovimentoService{

	@Autowired
	EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	MovimentoCotaRepository movimentoCotaRepository;
	
	@Autowired
	EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	EstudoCotaRepository estudoCotaRepository; 
	
	@Autowired
	CotaRepository cotaRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TipoMovimentoRepository tipoMovimentoRepository;
	
	@Autowired
	ProdutoEdicaoRepository produtoEdicaoRepository;

	@Override
	@Transactional
	public void gerarMovimentoEstoqueDeExpedicao(Date dataLancamento, Long idProdutoEdicao, Long idUsuario) {
		
		
		List<EstudoCota> listaEstudoCota = estudoCotaRepository.
				obterEstudoCotaPorDataProdutoEdicao(dataLancamento, idProdutoEdicao);
		
		BigDecimal total = new BigDecimal(0);
		
		for( EstudoCota estudoCota:listaEstudoCota ) {
			
			gerarMovimentoCota(dataLancamento,idProdutoEdicao,estudoCota.getCota().getId(),
					idUsuario, estudoCota.getQtdeEfetiva());
			
			total = total.add(estudoCota.getQtdeEfetiva());
		}
		
		gerarMovimentoEstoque(dataLancamento, idProdutoEdicao, idUsuario, total);
	}
	
	@Override
	@Transactional
	public void gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigDecimal quantidade) {
		
		TipoMovimento tipoMovimento = tipoMovimentoRepository.buscarTipoMovimento(
				TipoOperacao.SAIDA, DominioTipoMovimento.ENVIO_JORNALEIRO);
		
		ItemRecebimentoFisico itemRecebimentoFisico = 
				itemRecebimentoFisicoRepository.obterItemPorDataLancamentoIdProdutoEdicao(dataLancamento, idProdutoEdicao);
				
		EstoqueProduto estoqueProduto =  estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
		
		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		movimentoEstoque.setEstoqueProduto(estoqueProduto);
		movimentoEstoque.setProdutoEdicao(itemRecebimentoFisico.getItemNotaFiscal().getProdutoEdicao());		
		movimentoEstoque.setDataInclusao(new Date());
		movimentoEstoque.setUsuario(usuarioRepository.buscarPorId(idUsuario));
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setQtde(quantidade);
		
		if(tipoMovimento.isAprovacaoAutomatica()) {			
			movimentoEstoque.setStatus(StatusAprovacao.APROVADO);
		}
				
		BigDecimal novaQuantidade = estoqueProduto.getQtde().subtract(quantidade); 
		estoqueProduto.setQtde(novaQuantidade);
				
		movimentoEstoqueRepository.adicionar(movimentoEstoque);
		estoqueProdutoRespository.alterar(estoqueProduto);
	}
	
	@Override
	@Transactional
	public void gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigDecimal quantidade) {
		
		TipoMovimento tipoMovimento = tipoMovimentoRepository.buscarTipoMovimento(
				TipoOperacao.ENTRADA, DominioTipoMovimento.RECEBIMENTO_REPARTE);
				
		EstoqueProdutoCota estoqueProdutoCota =  estoqueProdutoCotaRepository.buscarEstoquePorProdutoECota(idProdutoEdicao, idCota);
				
		if(estoqueProdutoCota == null) {
			
			ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
			Cota cota = cotaRepository.buscarPorId(idCota);
			
			estoqueProdutoCota = new EstoqueProdutoCota();
			estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
			estoqueProdutoCota.setQtdeDevolvida(new BigDecimal(0));
			estoqueProdutoCota.setQtdeRecebida(new BigDecimal(0));
			estoqueProdutoCota.setCota(cota);
			
			estoqueProdutoCotaRepository.adicionar(estoqueProdutoCota);
		}
				
		MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
		movimentoEstoqueCota.setTipoMovimento(tipoMovimento);
		movimentoEstoqueCota.setCota(estoqueProdutoCota.getCota());
		movimentoEstoqueCota.setDataInclusao(new Date());
		movimentoEstoqueCota.setEstoqueProdutoCota(estoqueProdutoCota);
		movimentoEstoqueCota.setProdutoEdicao(estoqueProdutoCota.getProdutoEdicao());
		movimentoEstoqueCota.setQtde(quantidade);
		movimentoEstoqueCota.setUsuario(usuarioRepository.buscarPorId(idUsuario));
		
		if(tipoMovimento.isAprovacaoAutomatica()) {			
			movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
		}
		
		BigDecimal novaQuantidade = estoqueProdutoCota.getQtdeRecebida().add(quantidade); 
		estoqueProdutoCota.setQtdeRecebida(novaQuantidade);
				
		movimentoCotaRepository.adicionar(movimentoEstoqueCota);
		estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
	}


}
