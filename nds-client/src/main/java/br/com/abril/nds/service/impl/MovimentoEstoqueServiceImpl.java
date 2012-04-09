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
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.MovimentoEstoqueService;

@Service
public class MovimentoEstoqueServiceImpl implements MovimentoEstoqueService {

	@Autowired
	EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	EstudoCotaRepository estudoCotaRepository; 
	
	@Autowired
	CotaRepository cotaRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	ProdutoEdicaoRepository produtoEdicaoRepository;

	@Override
	@Transactional
	public void gerarMovimentoEstoqueDeExpedicao(Date dataLancamento, Long idProdutoEdicao, Long idUsuario) {
		
		TipoMovimentoEstoque tipoMovimento = 
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		
		TipoMovimentoEstoque tipoMovimentoCota =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		List<EstudoCota> listaEstudoCota = estudoCotaRepository.
				obterEstudoCotaPorDataProdutoEdicao(dataLancamento, idProdutoEdicao);
		
		BigDecimal total = new BigDecimal(0);
		
		for( EstudoCota estudoCota:listaEstudoCota ) {
			
			gerarMovimentoCota(dataLancamento,idProdutoEdicao,estudoCota.getCota().getId(),
					idUsuario, estudoCota.getQtdeEfetiva(),tipoMovimentoCota);
			
			total = total.add(estudoCota.getQtdeEfetiva());
		}
		
		gerarMovimentoEstoque(dataLancamento, idProdutoEdicao, idUsuario, total, tipoMovimento);
	}
	
	@Override
	@Transactional
	public void enviarSuplementarCotaAusente(Date data, Long idCota,List<MovimentoEstoqueCota> listaMovimentoCota){
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
			
			TipoMovimentoEstoque tipoMovimentoCota =
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		
		if(listaMovimentoCota != null){
			
			for(MovimentoEstoqueCota movimentoCota : listaMovimentoCota){
				
				if(movimentoCota.getData() != null &&  movimentoCota.getProdutoEdicao()!=null
						&& movimentoCota.getUsuario() != null
						&&  movimentoCota.getQtde() != null ){
					
					gerarMovimentoEstoque(movimentoCota.getData(), movimentoCota.getProdutoEdicao().getId(), movimentoCota.getUsuario().getId(), movimentoCota.getQtde(), tipoMovimento);
				
					gerarMovimentoCota(movimentoCota.getData(), movimentoCota.getProdutoEdicao().getId(), movimentoCota.getCota().getId(),movimentoCota.getUsuario().getId(), movimentoCota.getQtde(), tipoMovimentoCota);
			
				}
			}
		
		}
	}
	
	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigDecimal quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {
		

		ItemRecebimentoFisico itemRecebimentoFisico = 
			itemRecebimentoFisicoRepository.obterItemPorDataLancamentoIdProdutoEdicao(dataLancamento, idProdutoEdicao);
				
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
		
		if (estoqueProduto == null) {
			
			estoqueProduto = new EstoqueProduto();
			
			estoqueProduto.setProdutoEdicao(produtoEdicao);
			
			estoqueProduto.setQtde(BigDecimal.ZERO);
			
			this.estoqueProdutoRespository.adicionar(estoqueProduto);
		}
		
		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		movimentoEstoque.setEstoqueProduto(estoqueProduto);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);		
		movimentoEstoque.setData(new Date());
		movimentoEstoque.setUsuario(usuarioRepository.buscarPorId(idUsuario));
		movimentoEstoque.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoque.setQtde(quantidade);
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica()) {		
			
			movimentoEstoque.setStatus(StatusAprovacao.APROVADO);
		}
		
		if (StatusAprovacao.APROVADO.equals(movimentoEstoque.getStatus())) {
		
			BigDecimal novaQuantidade;
			
			if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
				
				 if(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())) {
					 
					 novaQuantidade = estoqueProduto.getQtdeSuplementar().add(quantidade);
					 
					 estoqueProduto.setQtdeSuplementar(novaQuantidade);
					 
				 } else {
					 
					 novaQuantidade = estoqueProduto.getQtde().add(quantidade);
					 
					 estoqueProduto.setQtde(novaQuantidade);
					 
				 }
				
				 
			} else {
				
				novaQuantidade = estoqueProduto.getQtde().subtract(quantidade); 
				
				estoqueProduto.setQtde(novaQuantidade);	
				
				if(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque())) {
					 
					novaQuantidade = estoqueProduto.getQtde().subtract(quantidade); 
					
					estoqueProduto.setQtde(novaQuantidade);	
					 
				 } else {
					 
					 novaQuantidade = estoqueProduto.getQtde().subtract(quantidade); 
						
					 estoqueProduto.setQtde(novaQuantidade);	
					 
				 }
			}
		
			estoqueProdutoRespository.alterar(estoqueProduto);
		}
		
		movimentoEstoqueRepository.adicionar(movimentoEstoque);
		
		return movimentoEstoque;
	}
	
	@Override
	@Transactional
	public void gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigDecimal quantidade, TipoMovimentoEstoque tipoMovimentoEstoque) {
						
		EstoqueProdutoCota estoqueProdutoCota = 
			estoqueProdutoCotaRepository.buscarEstoquePorProdutoECota(idProdutoEdicao, idCota);
				
		if (estoqueProdutoCota == null) {
			
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
		
		movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoqueCota.setCota(estoqueProdutoCota.getCota());
		movimentoEstoqueCota.setData(new Date());
		movimentoEstoqueCota.setEstoqueProdutoCota(estoqueProdutoCota);
		movimentoEstoqueCota.setProdutoEdicao(estoqueProdutoCota.getProdutoEdicao());
		movimentoEstoqueCota.setQtde(quantidade);
		movimentoEstoqueCota.setUsuario(usuarioRepository.buscarPorId(idUsuario));
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica()) {	
			
			movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
		}
		
		if (StatusAprovacao.APROVADO.equals(movimentoEstoqueCota.getStatus())) {
		
			BigDecimal novaQuantidade;
			
			if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {
				
				 novaQuantidade = estoqueProdutoCota.getQtdeRecebida().add(quantidade); 
				 estoqueProdutoCota.setQtdeRecebida(novaQuantidade);
				 
			} else {
				
				 novaQuantidade = estoqueProdutoCota.getQtdeRecebida().subtract(quantidade); 
				 estoqueProdutoCota.setQtdeRecebida(novaQuantidade);			
			}
			
			estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
		}
		
		movimentoEstoqueCotaRepository.adicionar(movimentoEstoqueCota);
	}

}
