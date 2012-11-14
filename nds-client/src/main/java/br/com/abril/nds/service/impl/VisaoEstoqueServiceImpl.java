package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
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
			if (DateUtil.isHoje(filtro.getDataMovimentacao())) {
				list = visaoEstoqueRepository.obterVisaoEstoqueDetalhe(filtro);
			} else {
				list = visaoEstoqueRepository.obterVisaoEstoqueDetalheHistorico(filtro);
			}
		}
		
		BigDecimal precoCapa = BigDecimal.ZERO;
		BigDecimal qtde = BigDecimal.ZERO;
		
		for (VisaoEstoqueDetalheDTO dto: list) {
			
			if (dto.getPrecoCapa() == null || dto.getQtde() == null) {
				
				continue;
			}
			
			precoCapa = CurrencyUtil.converterValor(dto.getPrecoCapa());
			qtde = CurrencyUtil.converterValor(dto.getQtde());
			dto.setValor(precoCapa.multiply(qtde));
		}
		
		return list;
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
		
		if (invetarioAtualizar == null 
				|| invetarioAtualizar.isEmpty()) {
			
			return;
		}

		Distribuidor distribuidor = distribuidorService.obter();
		
		for (VisaoEstoqueTransferenciaDTO dto : invetarioAtualizar) {
			
			if (dto.getQtde() == null) {
				
				continue;
			}

			Diferenca diferenca = new Diferenca();

			ProdutoEdicao produtoEdicao = 
				this.produtoEdicaoRepository.buscarPorId(dto.getProdutoEdicaoId());
			
			if (produtoEdicao == null) {
				
				throw new ValidacaoException(
					TipoMensagem.ERROR, "Não foi encontrado o produto/edição para inventário de estoque!");
			}

			BigInteger qtdeDiferenca = new BigInteger(dto.getQtde().toString());
			
			diferenca.setProdutoEdicao(produtoEdicao);
			diferenca.setQtde(qtdeDiferenca.abs());
			diferenca.setResponsavel(usuario);
			
			if (BigInteger.ZERO.compareTo(qtdeDiferenca) < 0) {
				
				diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_EM);
				
			} else {
				
				diferenca.setTipoDiferenca(TipoDiferenca.FALTA_EM);
			}

			diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
			diferenca.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
			diferenca.setTipoEstoque(tipoEstoque);
			diferenca.setAutomatica(true);
			diferenca.setDataMovimento(distribuidor.getDataOperacao());
			
			this.diferencaEstoqueRepository.adicionar(diferenca);
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
