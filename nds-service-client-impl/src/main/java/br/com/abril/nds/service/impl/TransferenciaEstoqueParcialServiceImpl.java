package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.service.TransferenciaEstoqueParcialService;
import br.com.abril.nds.util.BigIntegerUtil;

@Service
public class TransferenciaEstoqueParcialServiceImpl implements TransferenciaEstoqueParcialService {
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Override
	@Transactional(readOnly = true)
	public BigInteger buscarQuantidadeParaTransferencia(String codigoProduto, Long numeroEdicao) {
		
		if (codigoProduto == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Código de Produto inválido.");
		}
		
		if (numeroEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Número de Edição inválido.");
		}
		
		ProdutoEdicao produtoEdicao = 
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				codigoProduto, numeroEdicao.toString());
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto/Edição inválido.");
		}
		
		BigInteger qtdeEstoqueParaTransferencia =
			this.estoqueProdutoRespository.buscarQtdeEstoqueParaTransferenciaParcial(produtoEdicao.getId());
		
		return qtdeEstoqueParaTransferencia == null ? BigInteger.ZERO : qtdeEstoqueParaTransferencia;
	}
	
	@Override
	@Transactional
	public void transferir(String codigoProduto, 
						   Long numeroEdicaoOrigem, 
						   Long numeroEdicaoDestino, 
						   Long idUsuario) {
		
		validarParametrosTransferencia(codigoProduto, numeroEdicaoOrigem, numeroEdicaoDestino);

		ProdutoEdicao produtoEdicaoOrigem = 
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				codigoProduto, numeroEdicaoOrigem.toString());
		
		ProdutoEdicao produtoEdicaoDestino = 
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				codigoProduto, numeroEdicaoDestino.toString());
		
		validarProdutosTransferencia(produtoEdicaoOrigem, produtoEdicaoDestino);
		
		fecharLancamentoOrigem(produtoEdicaoOrigem);
		
		efetuarMovimentacaoTransferencia(
			produtoEdicaoOrigem.getEstoqueProduto(), 
				produtoEdicaoDestino.getId(), idUsuario);
	}
	
	private void efetuarMovimentacaoTransferencia(EstoqueProduto estoqueProdutoOrigem,
											 	  Long idProdutoEdicaoDestino,
												  Long idUsuario) {
		
		Long idProdutoEdicaoOrigem = estoqueProdutoOrigem.getProdutoEdicao().getId();

		BigInteger qtdeEstoqueParaTransferencia =
			this.estoqueProdutoRespository.buscarQtdeEstoqueParaTransferenciaParcial(idProdutoEdicaoOrigem);
		
		validarQuantidadeTransferencia(qtdeEstoqueParaTransferencia);
		
		movimentarEstoque(
			idProdutoEdicaoOrigem, estoqueProdutoOrigem.getQtde(), 
				GrupoMovimentoEstoque.TRANSFERENCIA_PARCIAL_SAIDA_LANCAMENTO, idUsuario);
		
		movimentarEstoque(
			idProdutoEdicaoOrigem, estoqueProdutoOrigem.getQtdeSuplementar(), 
				GrupoMovimentoEstoque.TRANSFERENCIA_PARCIAL_SAIDA_SUPLEMENTAR, idUsuario);
		
		movimentarEstoque(
			idProdutoEdicaoOrigem, estoqueProdutoOrigem.getQtdeDevolucaoEncalhe(), 
				GrupoMovimentoEstoque.TRANSFERENCIA_PARCIAL_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE, idUsuario);
		
		movimentarEstoque(
			idProdutoEdicaoOrigem, estoqueProdutoOrigem.getQtdeDanificado(), 
				GrupoMovimentoEstoque.TRANSFERENCIA_PARCIAL_SAIDA_PRODUTOS_DANIFICADOS, idUsuario);
		
		movimentarEstoque(
			idProdutoEdicaoDestino, qtdeEstoqueParaTransferencia, 
				GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_ESTOQUE_PARCIAIS, idUsuario);
	}
	
	private void validarQuantidadeTransferencia(BigInteger qtdTransferencia) {
		
		if (qtdTransferencia == BigInteger.ZERO) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição de origem sem estoque cadastrado.");
		}
	}
	
	private void movimentarEstoque(Long idProdutoEdicaoOrigem,
								   BigInteger qtde,
								   GrupoMovimentoEstoque grupoMovimentoEstoque,
								   Long idUsuario) {
		
		if (qtde == null || !BigIntegerUtil.isMaiorQueZero(qtde)) {
			return;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);
			
		if(tipoMovimentoEstoque == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Movimento não encontrado.");
		}
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicaoOrigem, idUsuario, qtde, tipoMovimentoEstoque);
	}

	private void fecharLancamentoOrigem(ProdutoEdicao produtoEdicaoOrigem) {
		
		List<Lancamento> lancamentos = 
			this.lancamentoRepository.obterLancamentosDaEdicao(produtoEdicaoOrigem.getId());
		
		if (lancamentos == null || lancamentos.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há lançamento para o produto/edição de origem.");
		}
		
		for (Lancamento lancamentoOrigem : lancamentos){
		    
		    lancamentoOrigem.setStatus(StatusLancamento.FECHADO);
	        
	        this.lancamentoRepository.alterar(lancamentoOrigem);
		}
	}

	private void validarProdutosTransferencia(ProdutoEdicao produtoEdicaoOrigem,
											  ProdutoEdicao produtoEdicaoDestino) {
		
		if (produtoEdicaoOrigem == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto/Edição de Origem não encontrado.");
		}
		
		if (produtoEdicaoDestino == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto/Edição de Destino não encontrado.");
		}
		
		if (produtoEdicaoOrigem.getEstoqueProduto() == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto/Edição de Origem sem estoque cadastrado.");
		}
		
		//FIXME: removido para fins de homologacao
		/*
		if (!produtoEdicaoDestino.isParcial()) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto/Edição de Destino deve ser Parcial.");
		}
		*/
	}

	private void validarParametrosTransferencia(String codigoProduto,
												Long numeroEdicaoOrigem, 
												Long numeroEdicaoDestino) {
		
		if (codigoProduto == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Código do Produto inválido.");
		}
		
		if (numeroEdicaoOrigem == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Número de Edição de Destino inválido.");
		}
		
		if (numeroEdicaoDestino == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Número de Edição de Destino inválido.");
		}
	}
	
}
