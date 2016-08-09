package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.AtualizacaoEstoqueGFS;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.repository.AtualizacaoEstoqueGFSRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.ExtratoEdicaoService;
import br.com.abril.nds.util.Util;


@Service
public class ExtratoEdicaoServiceImpl implements ExtratoEdicaoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtratoEdicaoServiceImpl.class);
	 
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private AtualizacaoEstoqueGFSRepository atualizacaoEstoqueGFSRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private LancamentoDiferencaRepository lancamentoDiferencaRepository;
	
	@Transactional
	@Override
	public InfoGeralExtratoEdicaoDTO obterInfoGeralExtratoEdicao(FiltroExtratoEdicaoDTO filtroExtratoEdicao) {
		
		if (filtroExtratoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro para a consulta de extrato edição não pode ser nulo.");
		}
		
		filtroExtratoEdicao.setGruposExcluidos(obterGruposMovimentoEstoqueExtratoEdicao());
		
		filtroExtratoEdicao.setCodigoProduto(Util.padLeft(filtroExtratoEdicao.getCodigoProduto(), "0", 8));
		
		List<ExtratoEdicaoDTO> listaExtratoEdicao = movimentoEstoqueRepository.obterListaExtratoEdicao(filtroExtratoEdicao, StatusAprovacao.PENDENTE);
		
		if (listaExtratoEdicao.isEmpty()){
			
			return null;
		}
		
		ProdutoEdicao produtoEdicao = 
			this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				filtroExtratoEdicao.getCodigoProduto(), filtroExtratoEdicao.getNumeroEdicao());
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Produto Edição não encontrado.");
		}

		for(int i = 0; i < listaExtratoEdicao.size(); i++) {	
			
			ExtratoEdicaoDTO itemExtratoEdicao = listaExtratoEdicao.get(i);
			
			if (itemExtratoEdicao.getIdLancamentoDiferenca() != null) {
				
				this.processarItemExtratoEdicaoDiferenca(itemExtratoEdicao);
			}
			
			BigInteger qtdEntrada = itemExtratoEdicao.getQtdEdicaoEntrada() == null ? BigInteger.ZERO : itemExtratoEdicao.getQtdEdicaoEntrada();
			
			BigInteger qtdSaida = itemExtratoEdicao.getQtdEdicaoSaida() == null ? BigInteger.ZERO : itemExtratoEdicao.getQtdEdicaoSaida();
			
			BigInteger qtdeParcial = qtdEntrada.subtract(qtdSaida);
		
			if (i > 0) {

				itemExtratoEdicao.setQtdParcial(
					listaExtratoEdicao.get(i - 1).getQtdParcial().add(qtdeParcial));
				
			} else {
			
				itemExtratoEdicao.setQtdParcial(qtdeParcial);
			}
		}
			
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = new InfoGeralExtratoEdicaoDTO();

		infoGeralExtratoEdicao.setSaldoTotalExtratoEdicao(listaExtratoEdicao.get(listaExtratoEdicao.size() - 1).getQtdParcial());
		
		this.processarAtualizacaoEstoqueGFS(produtoEdicao.getId(), listaExtratoEdicao);
	
		infoGeralExtratoEdicao.setListaExtratoEdicao(listaExtratoEdicao);
		
		return infoGeralExtratoEdicao;
	}
	
	private void processarItemExtratoEdicaoDiferenca(ExtratoEdicaoDTO itemExtratoEdicao) {

		LancamentoDiferenca lancamentoDiferenca =
			this.lancamentoDiferencaRepository.buscarPorId(
				itemExtratoEdicao.getIdLancamentoDiferenca());
		
		
		
		Diferenca diferenca = lancamentoDiferenca.getDiferenca();
	
		if ( diferenca != null ) {
			
			String novaDescricao = itemExtratoEdicao.getDescMovimento();
			if (!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferenca.getTipoDirecionamento())
					&& !diferenca.getTipoDiferenca().isAlteracaoReparte()
					&& !(diferenca.getTipoDiferenca().equals(TipoDiferenca.FALTA_EM_DIRECIONADA_COTA) 
							|| diferenca.getTipoDiferenca().equals(TipoDiferenca.SOBRA_EM_DIRECIONADA_COTA))) {
				
				novaDescricao = novaDescricao + " COTA";
			}
			
			StatusIntegracao statusIntegracao = (lancamentoDiferenca.getMovimentoEstoque()!= null)
													?lancamentoDiferenca.getMovimentoEstoque().getStatusIntegracao()
															:null;
	
			if (!diferenca.getTipoEstoque().equals(TipoEstoque.GANHO) 
					&& !diferenca.getTipoEstoque().equals(TipoEstoque.PERDA)
					&& (!StatusIntegracao.NAO_INTEGRAR.equals(statusIntegracao) 
							&& !StatusIntegracao.ENCALHE.equals(statusIntegracao)
							&& !StatusIntegracao.FORA_DO_PRAZO.equals(statusIntegracao))) {
	
				novaDescricao = novaDescricao + " (Pendente de Aprovação no GFS)";
			}
			itemExtratoEdicao.setDescMovimento(novaDescricao);
		} else {
			LOGGER.error("ERRO.. REGISTRO DIFERENCA NAO ENCONTRADO PARA TABELA LANCAMENTO DIFERENCA ID="+lancamentoDiferenca.getId());
		  }
		
		
	}
	
	private void processarAtualizacaoEstoqueGFS(Long idProdutoEdicao,
												List<ExtratoEdicaoDTO> listaExtratoEdicao) {
		
		List<AtualizacaoEstoqueGFS> atualizacoesEstoqueGFS =
			this.atualizacaoEstoqueGFSRepository.obterPorProdutoEdicao(idProdutoEdicao);
		
		if (atualizacoesEstoqueGFS == null 
				|| atualizacoesEstoqueGFS.isEmpty()) {
			
			return;
		}

		for (AtualizacaoEstoqueGFS atualizacaoEstoqueGFS : atualizacoesEstoqueGFS) {
			
			int indiceParaUtilizacao = 
				this.obterIndiceDaListaParaAtualizacaoEstoqueGFS(
					listaExtratoEdicao, atualizacaoEstoqueGFS);
			
			MovimentoEstoque movimentoEstoque = atualizacaoEstoqueGFS.getMovimentoEstoque();
			
			boolean isAprovadoGFS = 
				StatusIntegracao.LIBERADO.equals(movimentoEstoque.getStatusIntegracao());
			
			ExtratoEdicaoDTO itemExtratoEdicaoMovimento = null;
			
			if (isAprovadoGFS) {
				
				itemExtratoEdicaoMovimento = 
					this.gerarRegistroAprovacaoAtualizacaoEstoqueGFS(
						listaExtratoEdicao, atualizacaoEstoqueGFS, ++indiceParaUtilizacao);
				
			} else {
				
				itemExtratoEdicaoMovimento = 
					this.atualizarRegistroReprovacaoAtualizacaoEstoqueGFS(
						listaExtratoEdicao, atualizacaoEstoqueGFS, indiceParaUtilizacao);
			}

			this.gerarRegistroAtualizacaoEstoqueGFS(
				listaExtratoEdicao, atualizacaoEstoqueGFS, 
					itemExtratoEdicaoMovimento, ++indiceParaUtilizacao);
		}
	}
	
	private void gerarRegistroAtualizacaoEstoqueGFS(List<ExtratoEdicaoDTO> listaExtratoEdicao,
													AtualizacaoEstoqueGFS atualizacaoEstoqueGFS,
													ExtratoEdicaoDTO itemExtratoEdicaoMovimento,
													int indiceParaUtilizacao) {
		
		ExtratoEdicaoDTO itemExtratoEdicaoAtualizacao = new ExtratoEdicaoDTO();
		
		BigInteger totalRecebimentoFisico = this.calcularTotalRecebimentoFisico(listaExtratoEdicao);
		
		BigInteger totalEnvioCota = this.calcularTotalEnvioCota(listaExtratoEdicao);
		
		itemExtratoEdicaoAtualizacao.setDataMovimento(
			atualizacaoEstoqueGFS.getDataAtualizacao());
		
		itemExtratoEdicaoAtualizacao.setDescMovimento("ATUALIZAÇÃO");

		itemExtratoEdicaoAtualizacao.setQtdEdicaoEntrada(
			totalRecebimentoFisico.add(itemExtratoEdicaoMovimento.getQtdEdicaoEntrada()));
	
		itemExtratoEdicaoAtualizacao.setQtdEdicaoSaida(
			totalEnvioCota.add(itemExtratoEdicaoMovimento.getQtdEdicaoSaida()));
		
		listaExtratoEdicao.add(indiceParaUtilizacao, itemExtratoEdicaoAtualizacao);
	}
	
	private ExtratoEdicaoDTO atualizarRegistroReprovacaoAtualizacaoEstoqueGFS(
															List<ExtratoEdicaoDTO> listaExtratoEdicao,
															AtualizacaoEstoqueGFS atualizacaoEstoqueGFS,
															int indiceParaUtilizacao) {
		
		ExtratoEdicaoDTO itemExtratoEdicaoMovimento = listaExtratoEdicao.get(indiceParaUtilizacao);
		
		MovimentoEstoque movimentoEstoque = atualizacaoEstoqueGFS.getMovimentoEstoque();
		
		Diferenca diferenca = atualizacaoEstoqueGFS.getDiferenca();
		
		TipoDiferenca tipoDiferenca = diferenca.getTipoDiferenca();
		
		if (tipoDiferenca.isFalta()) {
			
			itemExtratoEdicaoMovimento.setQtdEdicaoEntrada(BigInteger.ZERO);
			
			itemExtratoEdicaoMovimento.setQtdEdicaoSaida(
				movimentoEstoque.getQtde().negate());

		} else {
			
			itemExtratoEdicaoMovimento.setQtdEdicaoSaida(BigInteger.ZERO);
			
			itemExtratoEdicaoMovimento.setQtdEdicaoEntrada(
				movimentoEstoque.getQtde());
		}
		
		String novaDescricao = movimentoEstoque.getTipoMovimento().getDescricao();

		itemExtratoEdicaoMovimento.setDescMovimento(novaDescricao + " (Reprovado pelo GFS)");
		
		return itemExtratoEdicaoMovimento;
	}
	
	private ExtratoEdicaoDTO gerarRegistroAprovacaoAtualizacaoEstoqueGFS(
														List<ExtratoEdicaoDTO> listaExtratoEdicao, 
														AtualizacaoEstoqueGFS atualizacaoEstoqueGFS,
														int indiceParaUtilizacao) {
		
		ExtratoEdicaoDTO itemExtratoEdicaoMovimento = new ExtratoEdicaoDTO();
		
		MovimentoEstoque movimentoEstoque = atualizacaoEstoqueGFS.getMovimentoEstoque();
		
		Diferenca diferenca = atualizacaoEstoqueGFS.getDiferenca();
		
		boolean diferencaDirecionadaParaCota =
			!TipoDirecionamentoDiferenca.ESTOQUE.equals(diferenca.getTipoDirecionamento());
		
		TipoDiferenca tipoDiferenca = diferenca.getTipoDiferenca();
		
		itemExtratoEdicaoMovimento.setDataMovimento(
			atualizacaoEstoqueGFS.getDataAtualizacao());

		if (tipoDiferenca.isFalta()) {
			
			if (diferencaDirecionadaParaCota) {
				
				itemExtratoEdicaoMovimento.setQtdEdicaoSaida(
					movimentoEstoque.getQtde().negate());
			}
			
			itemExtratoEdicaoMovimento.setQtdEdicaoEntrada(
				movimentoEstoque.getQtde().negate());

		} else {
			
			itemExtratoEdicaoMovimento.setQtdEdicaoSaida(
				movimentoEstoque.getQtde());
		}
		
		String novaDescricao = movimentoEstoque.getTipoMovimento().getDescricao();
		
		if (diferencaDirecionadaParaCota) {
			
			novaDescricao = novaDescricao + " COTA";
		}
		
		itemExtratoEdicaoMovimento.setDescMovimento(novaDescricao + " (Aprovado pelo GFS)");
			
		listaExtratoEdicao.add(indiceParaUtilizacao, itemExtratoEdicaoMovimento);
		
		return itemExtratoEdicaoMovimento;
	}
	
	private int obterIndiceDaListaParaAtualizacaoEstoqueGFS(List<ExtratoEdicaoDTO> listaExtratoEdicao, 
										   			 		AtualizacaoEstoqueGFS atualizacaoEstoqueGFS) {
		
		int indiceParaUtilizacao = 0;
		
		for (int indice = 0; indice < listaExtratoEdicao.size(); indice++) {

			Long idMovimentoAtualizacao = atualizacaoEstoqueGFS.getMovimentoEstoque().getId();

			ExtratoEdicaoDTO extratoEdicao = listaExtratoEdicao.get(indice);

			if (idMovimentoAtualizacao.equals(extratoEdicao.getIdMovimento()) &&
					atualizacaoEstoqueGFS.getDataAtualizacao().compareTo(extratoEdicao.getDataMovimento()) >= 0) {

				indiceParaUtilizacao = indice;
			}
		}

		return indiceParaUtilizacao;
	}
	
	private BigInteger calcularTotalRecebimentoFisico(List<ExtratoEdicaoDTO> listaExtratoEdicao) {
		
		List<ExtratoEdicaoDTO> itensExtratoEdicaoRecebimentoFisico =
			this.filtrarItensExtratoEdicaoPorGrupo(
				listaExtratoEdicao, GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
		
		BigInteger total = BigInteger.ZERO;
		
		for (ExtratoEdicaoDTO itemExtratoEdicao : itensExtratoEdicaoRecebimentoFisico) {
			
			total = total.add(itemExtratoEdicao.getQtdEdicaoEntrada());
		}
		
		return total;
	}
	
	private BigInteger calcularTotalEnvioCota(List<ExtratoEdicaoDTO> listaExtratoEdicao) {
		
		List<ExtratoEdicaoDTO> itensExtratoEdicaoRecebimentoFisico =
			this.filtrarItensExtratoEdicaoPorGrupo(
				listaExtratoEdicao, GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		
		BigInteger total = BigInteger.ZERO;
		
		for (ExtratoEdicaoDTO itemExtratoEdicao : itensExtratoEdicaoRecebimentoFisico) {
			
			total = total.add(itemExtratoEdicao.getQtdEdicaoSaida());
		}
		
		return total;
	}
	
	private List<ExtratoEdicaoDTO> filtrarItensExtratoEdicaoPorGrupo(
														List<ExtratoEdicaoDTO> listaExtratoEdicao,
														GrupoMovimentoEstoque grupoMovimentoEstoque) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque =
			this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);
		
		if (tipoMovimentoEstoque == null) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Tipo de Movimento de Estoque inexistente.");
		}
		
		List<ExtratoEdicaoDTO> itensExtratoEdicaoDoGrupo = new ArrayList<>();
		
		for (ExtratoEdicaoDTO itemExtratoEdicao : listaExtratoEdicao) {
			
			if (tipoMovimentoEstoque.getId().equals(itemExtratoEdicao.getIdTipoMovimento())) {
				
				itensExtratoEdicaoDoGrupo.add(itemExtratoEdicao);
			}
		}
		
		return itensExtratoEdicaoDoGrupo;
	}
	
	@Override
	public List<GrupoMovimentoEstoque> obterGruposMovimentoEstoqueExtratoEdicao() {
		
		List<GrupoMovimentoEstoque> grupos = new ArrayList<GrupoMovimentoEstoque>();

		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE);
		
		return grupos;
	}
	
	@Transactional
	public ProdutoEdicao obterProdutoEdicao(String codigoProduto, Long numeroEdicao) {
		
		Produto produto = new Produto();
		produto.setCodigo(codigoProduto);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(numeroEdicao);
		
		List<ProdutoEdicao> listaProdutoEdicao = produtoEdicaoRepository.obterListaProdutoEdicao(produto, produtoEdicao);
		
		if(listaProdutoEdicao != null && listaProdutoEdicao.size() == 1) {
			return listaProdutoEdicao.get(0);
		}
		
		return null;
		
	}
	
	@Transactional
	public String obterRazaoSocialFornecedorDeProduto(String codigoProduto) {
		
		codigoProduto = Util.padLeft(codigoProduto, "0", 8);
		
		List<Fornecedor> listaFornecedor =
			fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, null);
		
		if(listaFornecedor != null && listaFornecedor.size() == 1) {
			
			Fornecedor fornecedor = listaFornecedor.get(0);
			
			String razao = (fornecedor.getJuridica() != null) ? fornecedor.getJuridica().getRazaoSocial() : "" ;
			
			return razao;
			
		}
		
		
		return "";
	}

	
	
}
