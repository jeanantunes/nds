package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueTransferenciaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class VisaoEstoqueServiceImpl implements VisaoEstoqueService {

	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;
	
	@Override
	@Transactional
	public List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {
		
		List<VisaoEstoqueDTO> list = new ArrayList<VisaoEstoqueDTO>();
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if (filtro.getDataMovimentacao().compareTo(dataOperacao) == 0) {

			// Busca na tabela estoque
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
		} else {

			// Busca na tabela histórico
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
		}
		
		return list;
	}

	
	@Override
	@Transactional
	public List<? extends VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
	    
		List<? extends VisaoEstoqueDetalheDTO> list = null;
		
		if (TipoEstoque.LANCAMENTO_JURAMENTADO.name().equals(filtro.getTipoEstoque())) {
		    
		    list = visaoEstoqueRepository.obterVisaoEstoqueDetalheJuramentado(filtro);
		    
		} else {
		
    		if (filtro.isBuscaHistorico()) {
    			list = visaoEstoqueRepository.obterVisaoEstoqueDetalheHistorico(filtro);
    		} else {
    			list = visaoEstoqueRepository.obterVisaoEstoqueDetalhe(filtro);
    		}
		}
		
		return list;
	}
	
	@Override
	@Transactional
	public Long obterCountVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if (filtro.getDataMovimentacao().compareTo(dataOperacao) == 0) {
			return visaoEstoqueRepository.obterCountVisaoEstoqueDetalhe(filtro);
		} else {
			return visaoEstoqueRepository.obterCountVisaoEstoqueDetalheHistorico(filtro);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public BigInteger obtemQuantidadeEstoque(long idProdutoEdicao, String tipoEstoque, Date dataMovimentacao){ 
		BigInteger qtd;
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if (dataMovimentacao.compareTo(dataOperacao) == 0) {			
			qtd = visaoEstoqueRepository.obterQuantidadeEstoque(idProdutoEdicao, tipoEstoque);
		} else {
			qtd = visaoEstoqueRepository.obterQuantidadeEstoqueHistorico(idProdutoEdicao, tipoEstoque);
		}
		
		return qtd;
	}

	@Override
	@Transactional
	public void transferirEstoque(FiltroConsultaVisaoEstoque filtro, Usuario usuario) {

		if (filtro.getListaTransferencia() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum estoque escolhido para transferência.");
		}
		
		TipoMovimentoEstoque tipoMovimentoEntrada = 
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(filtro.getGrupoMovimentoEntrada());

		if (tipoMovimentoEntrada == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de movimento de entrada não encontrado!");
		}
		
		TipoMovimentoEstoque tipoMovimentoSaida = 
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(filtro.getGrupoMovimentoSaida());

		if (tipoMovimentoSaida == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de movimento de saída não encontrado!");
		}
		
		for (VisaoEstoqueTransferenciaDTO dto : filtro.getListaTransferencia()) {
			BigInteger qtdEstoque =  this.obtemQuantidadeEstoque(dto.getProdutoEdicaoId(), filtro.getTipoEstoque(), filtro.getDataMovimentacao());
			
			if(qtdEstoque.longValue() < dto.getQtde()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Não é possivel transferir quantidade superior a do estoque disponível.");
			}

			movimentoEstoqueService.gerarMovimentoEstoque(
					dto.getProdutoEdicaoId(), 
					usuario.getId(), 
					new BigInteger(dto.getQtde().toString()), 
					tipoMovimentoEntrada);

			movimentoEstoqueService.gerarMovimentoEstoque(
					dto.getProdutoEdicaoId(), 
					usuario.getId(), 
					new BigInteger(dto.getQtde().toString()), 
					tipoMovimentoSaida);
		}
	}

	@Override
	@Transactional
	public void atualizarInventarioEstoque(List<VisaoEstoqueTransferenciaDTO> invetarioAtualizar, TipoEstoque tipoEstoque, Usuario usuario) {
		
		if (invetarioAtualizar == null || invetarioAtualizar.isEmpty()) {
			
			return;
		}

		for (VisaoEstoqueTransferenciaDTO dto : invetarioAtualizar) {
			
			if (dto.getQtde() == null) {
				
				continue;
			}

			Diferenca diferenca = new Diferenca();

			ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(dto.getProdutoEdicaoId());
			
			if (produtoEdicao == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Não foi encontrado o produto/edição para inventário de estoque!");
			}
			
			BigInteger qtdeDiferenca = new BigInteger(dto.getQtde().toString());
			
			diferenca.setProdutoEdicao(produtoEdicao);
			diferenca.setQtde(qtdeDiferenca.abs());
			
			StatusAprovacao statusAprovacao = null;
			
			if (BigInteger.ZERO.compareTo(qtdeDiferenca) < 0) {
				
				diferenca.setTipoDiferenca(TipoDiferenca.GANHO_EM);
				
				statusAprovacao = StatusAprovacao.GANHO;
				
			} else {
				
				diferenca.setTipoDiferenca(TipoDiferenca.PERDA_EM);
				
				statusAprovacao = StatusAprovacao.PERDA;
			}
			
			diferencaEstoqueService.lancarDiferencaAutomatica(diferenca, tipoEstoque, statusAprovacao, Origem.INVENTARIO);
		}
	}

}