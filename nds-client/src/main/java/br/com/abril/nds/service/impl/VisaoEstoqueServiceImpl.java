package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueTransferenciaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

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
	
	@Override
	@Transactional
	public List<VisaoEstoqueDTO> obterVisaoEstoque(FiltroConsultaVisaoEstoque filtro) {
		
		List<VisaoEstoqueDTO> list = new ArrayList<VisaoEstoqueDTO>();
		
		if (DateUtil.isHoje(filtro.getDataMovimentacao())) {

			// Busca na tabela estoque
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueJuramentado(filtro));
			
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
			list.add(visaoEstoqueRepository.obterVisaoEstoqueJuramentado(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoqueHistorico(filtro));
			
			filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
			list.add(visaoEstoqueRepository.obterVisaoEstoque(filtro));
		}
		
		return list;
	}

	
	@Override
	@Transactional
	public List<? extends VisaoEstoqueDetalheDTO> obterVisaoEstoqueDetalhe(FiltroConsultaVisaoEstoque filtro) {
		
		List<? extends VisaoEstoqueDetalheDTO> list = null;
		
		if (filtro.getTipoEstoque().equals(TipoEstoque.LANCAMENTO_JURAMENTADO.toString())) {
			list = visaoEstoqueRepository.obterVisaoEstoqueDetalheJuramentado(filtro);
		} else {
			list = visaoEstoqueRepository.obterVisaoEstoqueDetalhe(filtro);
		}
		
		BigDecimal precoCapa;
		BigDecimal qtde;
		
		for (VisaoEstoqueDetalheDTO dto: list) {
			
			precoCapa = CurrencyUtil.converterValor(dto.getPrecoCapa());
			qtde = CurrencyUtil.converterValor(dto.getQtde());
			dto.setValor(precoCapa.multiply(qtde));
		}
		
		return list;
	}


	@Override
	public void transferirEstoque(FiltroConsultaVisaoEstoque filtro, Usuario usuario) {
		
		for (VisaoEstoqueTransferenciaDTO dto : filtro.getListaTransferencia()) {
			
			movimentoEstoqueService.gerarMovimentoEstoque(
					dto.getProdutoEdicaoId(), 
					usuario.getId(), 
					new BigInteger(dto.getQtde().toString()), 
					null);
		}
	}
	
	@Override
	@Transactional
	public void atualizarInventarioEstoque(Map<Long, BigInteger> mapaDiferencaProduto, Usuario usuario) {
		
		if (mapaDiferencaProduto == null 
				|| mapaDiferencaProduto.isEmpty()) {
			
			return;
		}
		
		for (Map.Entry<Long, BigInteger> entry : mapaDiferencaProduto.entrySet()) {
			
			Diferenca diferenca = new Diferenca();
			
			ProdutoEdicao produtoEdicao = 
				this.produtoEdicaoRepository.buscarPorId(entry.getKey());
			
			if (produtoEdicao == null) {
				
				throw new ValidacaoException(
					TipoMensagem.ERROR, "Não foi encontrado o produto/edição para inventário de estoque!");
			}
			
			BigInteger qtdeDiferenca = entry.getValue();
			
			diferenca.setProdutoEdicao(produtoEdicao);
			diferenca.setQtde(qtdeDiferenca.abs());
			diferenca.setResponsavel(usuario);
			
			if (BigInteger.ZERO.compareTo(qtdeDiferenca) < 0) {
				
				diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_EM);
				
			} else {
				
				diferenca.setTipoDiferenca(TipoDiferenca.FALTA_EM);
			}
			
			diferenca = this.diferencaEstoqueService.lancarDiferencaAutomatica(diferenca);
			
			/* 
			 * TODO: Chamar fluxo de retorno do GFS passando a diferença (pendente de definção com a DGB).
			 * Este gerará um LancamentoDiferenca com status de PERDA ou GANHO.
			 */
		}
	}
	
	private TipoMovimentoEstoque buscaTipoMovimento() {
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE);
		
		if(tipoMovimento == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Não foi encontrado tipo de movimento de estoque para venda de encalhe!");
		}
		
		return tipoMovimento;
	}
}
