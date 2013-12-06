package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.MathUtil;

@Service
public class RecebimentoFisicoServiceImpl implements RecebimentoFisicoService {

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private NotaFiscalEntradaRepository notaFiscalRepository;
	
	@Autowired
	private CFOPRepository cFOPRepository;
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
		
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoServiceImpl;

	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;	
	
	@Autowired
	private UsuarioService usuarioService;	
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private final BigDecimal CEM = new BigDecimal(100);
	
	/**
	* Obtem lista com dados de itemRecebimento relativos ao id de uma nota fiscal.
	* 
	* @param idNotaFiscal
	* 
	* @return List - RecebimentoFisicoDTO
	* 
	*/
	@Transactional
	public List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal) {

		return this.recebimentoFisicoRepository.obterListaItemRecebimentoFisico(idNotaFiscal);		
	}
	
	/**
	 * Cancela Recebimento Fisico e Itens de recebimento Fisico e uma Nota de origem manual com seus itens de Nota 
	 * @param idNotaFiscal
	 */
	@Transactional
	public void cancelarNotaFiscal(Long idNotaFiscal){
		
		NotaFiscalEntrada notaFiscal = notaFiscalRepository.buscarPorId(idNotaFiscal);
		
		if(notaFiscal != null){
			
			RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
			
			if(recebimentoFisico != null){
				
				if(StatusConfirmacao.PENDENTE.equals(recebimentoFisico.getStatusConfirmacao())){
						
					List<ItemRecebimentoFisico> listaItemRecebimentoFisico = itemRecebimentoFisicoRepository.obterItemPorIdRecebimentoFisico(recebimentoFisico.getId());
						
					if(listaItemRecebimentoFisico != null){
						
						for(ItemRecebimentoFisico itemRecebimentoFisico : listaItemRecebimentoFisico){
							
							itemRecebimentoFisicoRepository.remover(itemRecebimentoFisico);
						}
					}				
					recebimentoFisicoRepository.remover(recebimentoFisico);
				}else{
					throw new ValidacaoException(TipoMensagem.WARNING, "O Recebimento ja foi confirmado, não é possível alterar os dados do mesmo.");
				}
					
			}
			
			if(Origem.MANUAL.equals(notaFiscal.getOrigem())){
				
				List<ItemNotaFiscalEntrada> listaItemNotaFiscal = itemNotaFiscalRepository.buscarItensPorIdNota(idNotaFiscal);
				
				for(ItemNotaFiscalEntrada itemNotaFiscal : listaItemNotaFiscal){
					
					itemNotaFiscalRepository.remover(itemNotaFiscal);
				}
				
				notaFiscalRepository.remover(notaFiscal);				
			}
		}	
		
	}
	
	private void validarExisteNotaConfirmada(Long idNotaFiscal){
		
		if(idNotaFiscal == null){
			return;
		}
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(idNotaFiscal);
		
		if(recebimentoFisico != null){

			if(StatusConfirmacao.CONFIRMADO.equals(recebimentoFisico.getStatusConfirmacao())){
				throw new ValidacaoException(TipoMensagem.WARNING, "O Recebimento ja foi confirmado, não é possível alterar os dados do mesmo.");
			}
		}
	}
	
	/**
	 * Verifica se a nota fiscal já existe (combinação numero, serie e cnpj do emitente)
	 * @param notaFiscal
	 */
	@Transactional(readOnly=true)
	public void validarExisteNotaFiscal(NotaFiscalEntradaFornecedor notaFiscal) {
		
		if ( recebimentoFisicoRepository.existeNotaFiscal(notaFiscal.getNumero(), notaFiscal.getSerie(), notaFiscal.getFornecedor().getJuridica().getCnpj()) ) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível receber a nota fiscal! Existe outra nota fiscal com o mesmo número, série e emitente (cnpj).");
		}
		
	}
	
	/**
	 * Insere os dados do recebimento físico.
	 */
	@Transactional
	public void inserirDadosRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual){
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Nota fiscal não existente.");
		}
		
		validarExisteNotaConfirmada(notaFiscal.getId());
		
		if(listaItensNota == null || listaItensNota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens relativos a esta nota fiscal");
		}
		
		if(notaFiscal.getId() != null) {
				
			atualizarDadosNotaFiscalExistente(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
			
		} else {
			
			inserirDadosNovaNotaFiscal(usuarioLogado, notaFiscal, listaItensNota, dataAtual);			
		}		
	}
	
	/**
	 * Confirmação de RecebimentoFisico
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 * @param dataAtual
	 */
	@Transactional
	public void confirmarRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual, Boolean pularValidacao){
		
		if(!pularValidacao)			
			verificarValorDaNota(recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId()), notaFiscal.getValorBruto());
		
		notaFiscal.setDataRecebimento(this.distribuidorService.obterDataOperacaoDistribuidor());
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		
		inserirDadosRecebimentoFisico(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId());

		if(listaItemRecebimentoFisico == null || listaItemRecebimentoFisico.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens para confirmação de recebimento físico nesta nota fiscal");
		}
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItemRecebimentoFisico) {
			
			inserirMovimentoEstoque(usuarioLogado, recebimentoFisicoDTO);
			
			atualizarLancamento(recebimentoFisicoDTO, dataAtual, usuarioLogado);
			
		}
		
		alterarRecebimentoFisicoParaConfirmado(usuarioLogado, notaFiscal, dataAtual);	
		
	}
	
	@Transactional
	public void gerarMovimentosEstoqueDaNota(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual){
		
		verificarValorDaNota(recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId()),notaFiscal.getValorBruto());
		
		notaFiscal.setDataRecebimento(this.distribuidorService.obterDataOperacaoDistribuidor());
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
				
		inserirDadosRecebimentoFisico(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId());

		if(listaItemRecebimentoFisico == null || listaItemRecebimentoFisico.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens para confirmação de recebimento físico nesta nota fiscal");
		}
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItemRecebimentoFisico) {
			
			inserirMovimentoEstoque(usuarioLogado, recebimentoFisicoDTO);
			
			atualizarLancamento(recebimentoFisicoDTO, dataAtual, usuarioLogado);
			
		}
		
		alterarRecebimentoFisicoParaConfirmado(usuarioLogado, notaFiscal, dataAtual);	
		
	}	
	
	/**
	 * Método que compara o valor bruto da nota com a soma dos valores dos itens
	 * @param listaItensNota
	 * @param valorBruto
	 */
	private void verificarValorDaNota(List<RecebimentoFisicoDTO> listaItensNota, BigDecimal valorBruto) {
		
		BigDecimal somaValorDosItens = new BigDecimal(0.0);
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota) {
			somaValorDosItens = somaValorDosItens.add( recebimentoFisicoDTO.getPrecoItem().multiply( BigDecimal.valueOf( recebimentoFisicoDTO.getRepartePrevisto().doubleValue() ) ));
		}
		
		if(valorBruto == null){
			valorBruto = new BigDecimal(0.0);
		}
		
		Double parseSoma = somaValorDosItens.doubleValue();
		Double parseValorBruto = valorBruto.doubleValue();
		
		if(!parseSoma.equals(parseValorBruto)){
			throw new ValidacaoException(TipoMensagem.ERROR,"Valor Bruto da Nota não corresponde a soma dos valores Totais do Item da Nota");
		}
	}

	@Override
	@Transactional
	public List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao) {
		
		return tipoNotaFiscalRepository.obterTiposNotasFiscais(tipoOperacao);
		
	}
	
	@Transactional
	public RecebimentoFisico obterRecebimentoFisicoPorNotaFiscal(Long idNotaFiscal){
		return recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(idNotaFiscal);
	}
			
	@Transactional
	public List<CFOP> obterListaCFOP() {
		return cFOPRepository.buscarTodos();
	}
	
	@Transactional
	public List<TipoNotaFiscal> obterListaTipoNotaFiscal(TipoOperacao tipoOperacao) {
		return tipoNotaFiscalRepository.obterTiposNotasFiscais();
	}
	
	/**
	 * Obtém a pessoa jurídica através do CNPJ.
	 * 
	 * @param cnpj
	 * @return PessoaJuridica
	 */
	private PessoaJuridica obterPessoaPorCNPJ(String cnpj) {
		return pessoaJuridicaRepository.buscarPorCnpj(cnpj);
	}
	
	/**
	 * Atualiza os dados de uma nota fiscal existente.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 * @param dataAtual
	 */
	private void atualizarDadosNotaFiscalExistente(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual) {
		
		
		//notaFiscal.setDataRecebimento(new Date());
		
		//notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		
		notaFiscalRepository.merge(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
		
		if(recebimentoFisico == null){
			recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);
		}	
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota) {
			
			ItemNotaFiscalEntrada itemNota = null;
			
			if(recebimentoFisicoDTO.getIdItemNota() == null) {
				
				ProdutoEdicao produto = produtoEdicaoServiceImpl.buscarPorID(recebimentoFisicoDTO.getIdProdutoEdicao());
				
				itemNota = inserirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual,produto.getDesconto());
				
				inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNota, recebimentoFisico);
				
			} else {
				
				if(Origem.MANUAL.equals(recebimentoFisicoDTO.getOrigemItemNota())) {
					
					itemNota = atualizarItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual);
					
					if(recebimentoFisicoDTO.getIdItemRecebimentoFisico() == null) {
						
						inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNota, recebimentoFisico);
						
					} else {
						
						atualizarItemRecebimentoFisico(recebimentoFisicoDTO);
						
					}
					
				} else {
					
					itemNota = new ItemNotaFiscalEntrada();
					
					itemNota.setId(recebimentoFisicoDTO.getIdItemNota());
					
					if(recebimentoFisicoDTO.getIdItemRecebimentoFisico() == null) {
						
						inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNota, recebimentoFisico);
						
					} else {
						
						atualizarItemRecebimentoFisico(recebimentoFisicoDTO);
						
					}
					
					
				}
				
			}
		
		}
		
	}
	
	/**
	 * Insere os dados de uma nova nota fiscal, incluindo 
	 * informações de diferença e lançamento.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 */
	private void inserirDadosNovaNotaFiscal(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual) {
		
		notaFiscal.setDataExpedicao(dataAtual);
		
		String cnpj = null;
		if (notaFiscal instanceof NotaFiscalEntradaFornecedor
				&& ((NotaFiscalEntradaFornecedor) notaFiscal).getFornecedor() != null
				&& ((NotaFiscalEntradaFornecedor) notaFiscal).getFornecedor().getJuridica() != null
				&& ((NotaFiscalEntradaFornecedor) notaFiscal).getFornecedor().getJuridica().getCnpj() != null
				&& !((NotaFiscalEntradaFornecedor) notaFiscal).getFornecedor().getJuridica().getCnpj().isEmpty()) {
			cnpj = ((NotaFiscalEntradaFornecedor)notaFiscal).getFornecedor().getJuridica().getCnpj();
		}
		
		PessoaJuridica emitente = obterPessoaPorCNPJ(cnpj);
				
		if(emitente == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "CNPJ não corresponde a Pessoa Jurídica cadastrada.");
		}
		
		if (notaFiscal.getOrigem() == null){
			boolean indNotaEnvio = notaFiscal.getNumeroNotaEnvio() != null;
			
			if(indNotaEnvio) {
	
				notaFiscal.setOrigem(Origem.INTERFACE);
			} else {
				
				notaFiscal.setOrigem(Origem.MANUAL);
			}
		}
		
		notaFiscalRepository.adicionar(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);	
		
		BigDecimal valorDesconto = new BigDecimal(BigInteger.ZERO, new MathContext(18));
		valorDesconto.setScale(4,RoundingMode.HALF_EVEN);
		
		for( RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota ){
			
			ProdutoEdicao produtoEdicao = produtoEdicaoServiceImpl.buscarPorID(recebimentoFisicoDTO.getIdProdutoEdicao());
			
			BigDecimal desconto = new BigDecimal(BigInteger.ZERO, new MathContext(18));
			desconto.setScale(4,RoundingMode.HALF_EVEN);
			
			if(produtoEdicao.getOrigem().equals(Origem.MANUAL)) {
				if(produtoEdicao != null && produtoEdicao.getDesconto() != null) {
					desconto = produtoEdicao.getDesconto();
				} else {				
					//if((desconto.equals(BigDecimal.ZERO) || desconto == null) && produtoEdicao.getProduto() != null) {
					desconto = (produtoEdicao.getProduto().getDesconto() != null ? produtoEdicao.getProduto().getDesconto() : BigDecimal.ZERO);
				}
			} else {
				if(produtoEdicao != null && produtoEdicao.getDescontoLogistica() != null) {
					desconto = produtoEdicao.getDescontoLogistica().getPercentualDesconto();
				} else {				
					//if((desconto.equals(BigDecimal.ZERO) || desconto == null) && produtoEdicao.getProduto() != null) {
					desconto = (produtoEdicao.getProduto() != null 
								&& produtoEdicao.getProduto().getDescontoLogistica() != null ? 
										produtoEdicao.getProduto().getDescontoLogistica().getPercentualDesconto() : 
											BigDecimal.ZERO);
				}
			}			
			
			ItemNotaFiscalEntrada itemNotaFiscal = inserirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual, desconto);
			
			inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNotaFiscal, recebimentoFisico);
			
			if(produtoEdicao.getPrecoVenda() != null && desconto != null) {
				
				BigDecimal qntFisico =  new BigDecimal(recebimentoFisicoDTO.getQtdFisico().toString());
				
				BigDecimal precoVendaDesconto = produtoEdicao.getPrecoVenda().multiply(desconto.divide(new BigDecimal("100")));
				
				valorDesconto = valorDesconto.add(produtoEdicao.getPrecoVenda().multiply(qntFisico).subtract(precoVendaDesconto.multiply(qntFisico)));
			}

		}
		
		if(notaFiscal.getValorBruto() != null) {
			notaFiscal.setValorDesconto(notaFiscal.getValorBruto().subtract(valorDesconto));
		}
		
		notaFiscalRepository.merge(notaFiscal);
		
	}
	
	
	
	
	/**
	 * Insere um recebimento físico.
	 * 
	 * @param userLogado
	 * @param notaFiscal
	 * @param dataAtual
	 * 
	 * @return RecebimentoFisico
	 */
	private RecebimentoFisico inserirRecebimentoFisico(Usuario userLogado, NotaFiscalEntrada notaFiscal, Date dataAtual){
		
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		
		recebimentoFisico.setDataRecebimento(dataAtual);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		recebimentoFisico.setNotaFiscal(notaFiscal);
		recebimentoFisico.setRecebedor(userLogado);
		
		recebimentoFisicoRepository.adicionar(recebimentoFisico);
		
		return recebimentoFisico;	
		
	}
	
	
	/**
	 * @param recebimentoFisicoDTO
	 * @param dataAtual
	 * @param usuarioLogado
	 */
	private void atualizarLancamento(RecebimentoFisicoDTO recebimentoFisicoDTO, Date dataAtual, Usuario usuarioLogado) {
		
		if(Origem.INTERFACE.equals(recebimentoFisicoDTO.getOrigemItemNota())) {
			return;
		}
		
		Date dataLancamento = recebimentoFisicoDTO.getDataLancamento();
		Long idProdutoEdicao = recebimentoFisicoDTO.getIdProdutoEdicao();
		
		Lancamento lancamento =
			lancamentoRepository.obterLancamentoPosteriorDataLancamento(
				dataLancamento, idProdutoEdicao);
		
		if (lancamento == null) {
			
			lancamento = 
				lancamentoRepository.obterLancamentoAnteriorDataLancamento(
					dataLancamento, idProdutoEdicao);
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarPorID(recebimentoFisicoDTO.getIdProdutoEdicao());
				
		if(lancamento != null) {
			
			if(lancamento.getRecebimentos() == null) {
				lancamento.setRecebimentos(new HashSet<ItemRecebimentoFisico>());
			}
			
			ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
			
			itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
			
			lancamento.getRecebimentos().add(itemRecebimentoFisico);
			lancamento.setUsuario(usuarioLogado);
			
			lancamentoRepository.alterar(lancamento);
		
		} else if(lancamento==null && produtoEdicao.isParcial()) {
			
			LancamentoParcial lancamentoParcial  = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(recebimentoFisicoDTO.getIdProdutoEdicao());
			
			if ( lancamentoParcial == null ) {
				lancamentoParcial = new LancamentoParcial();
				lancamentoParcial.setProdutoEdicao(new ProdutoEdicao(recebimentoFisicoDTO.getIdProdutoEdicao()));
				lancamentoParcial.setStatus(StatusLancamentoParcial.PROJETADO);		
			}
			
			lancamentoParcial.setLancamentoInicial(dataLancamento);
			lancamentoParcial.setRecolhimentoFinal(recebimentoFisicoDTO.getDataRecolhimento());
			
			lancamentoParcialRepository.merge(lancamentoParcial);
			
			Usuario usuario = usuarioService.getUsuarioLogado();
						
			if(lancamentoParcial.getPeriodos().isEmpty())
				parciaisService.gerarPeriodosParcias(produtoEdicao, 1, usuario);
			
			Lancamento periodo = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
			
			periodo.setReparte(recebimentoFisicoDTO.getRepartePrevisto());
			periodo.setRepartePromocional(BigInteger.ZERO);
			periodo.setUsuario(usuario);
			
			lancamentoRepository.merge(periodo);
			
		} else {

			throw new ValidacaoException(TipoMensagem.WARNING,
				"Lançamento não encontrado para o produto: " + produtoEdicao.getProduto().getNome()
					+ " edição: " + produtoEdicao.getNumeroEdicao());
		}
		
	}
	
	private Diferenca obterDiferencaDeItemRecebimentoFisico(
			Usuario usuarioLogado,
			RecebimentoFisicoDTO recebimentoFisicoDTO) {
		
		BigInteger calculoQdeDiferenca = recebimentoFisicoDTO.getQtdFisico().subtract(recebimentoFisicoDTO.getRepartePrevisto()) ;

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoFisicoDTO.getIdProdutoEdicao());					
		
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setQtde(calculoQdeDiferenca.abs());
		diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
		diferenca.setResponsavel(usuarioLogado);
		diferenca.setProdutoEdicao(produtoEdicao);
		
		if( calculoQdeDiferenca.compareTo(BigInteger.ZERO ) < 0 ){
		
			diferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE);
			
		} else if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) > 0){						
			
			diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_DE);
			
		}
		
		return diferenca;
		
	}
	
	/**
	 * Verifica se existe divergência entre a qtdFisica e repartePrevisto.
	 * 
	 * @param repartePrevisto
	 * @param qtdFisico
	 * 
	 * @return boolean
	 */
	private boolean verificarDiferencaExistente(BigInteger repartePrevisto, BigInteger qtdFisico) {
		
		BigInteger calculoQdeDiferenca = qtdFisico.subtract(repartePrevisto); 
		
		if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) < 0) {
			
			return true;
			
		} else if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) > 0) {						

			return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * Obtém a qtde de recebimento fisico a partir da qtdPacote e qtdExemplar 
	 * (sendo que a qtdExemplar corresponde a qtd de quebra).
	 * 
	 * @param pctePadrao
	 * @param qtdPacote
	 * @param qtdExemplar
	 * 
	 * @return BigInteger
	 */
	private BigInteger obterQtdRecebimentoFisicoPorQtdPacoteQtdExemplar(int pctePadrao, BigInteger qtdPacote, BigInteger qtdExemplar) {
		
		BigInteger pacotePadrao = (pctePadrao <= 0) ? BigInteger.ONE : new BigInteger(String.valueOf(pctePadrao));
		
		qtdPacote = (qtdPacote == null) ? BigInteger.ZERO : qtdPacote;
		
		qtdExemplar = (qtdExemplar == null) ? BigInteger.ZERO : qtdExemplar;
		
		BigInteger qtdFisico = (qtdPacote.multiply(pacotePadrao)).add(qtdExemplar);
		
		return qtdFisico;
		
	}
	
	/**
	 * Insere um itemNotaFiscal.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param recebimentoDTO
	 * @param dataAtual
	 * 
	 * @return ItemNotaFiscal
	 */
	private ItemNotaFiscalEntrada inserirItemNotaFiscal(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, RecebimentoFisicoDTO recebimentoDTO, Date dataAtual, BigDecimal desconto){
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoDTO.getIdProdutoEdicao());
		
		ItemNotaFiscalEntrada itemNota = new ItemNotaFiscalEntrada();		
		
		itemNota.setQtde(recebimentoDTO.getRepartePrevisto());			
		itemNota.setDataLancamento(recebimentoDTO.getDataLancamento());
		itemNota.setDataRecolhimento(recebimentoDTO.getDataRecolhimento());
		itemNota.setTipoLancamento(recebimentoDTO.getTipoLancamento());
		
		if(notaFiscal.getOrigem().equals(Origem.MANUAL)) {			
			BigDecimal descontoDecimal = desconto.divide(CEM);			
			
			itemNota.setPreco(recebimentoDTO.getPrecoCapa());
			itemNota.setDesconto(descontoDecimal);
		} else {
			itemNota.setPreco(recebimentoDTO.getPrecoCapa());
			itemNota.setDesconto(desconto);
		}
		itemNota.setProdutoEdicao(produtoEdicao);
		itemNota.setUsuario(usuarioLogado);
		itemNota.setNotaFiscal(notaFiscal);
		
		boolean indNotaEnvio = notaFiscal.getNumeroNotaEnvio() != null;
		
		if(indNotaEnvio) {
			itemNota.setOrigem(Origem.INTERFACE);
		} else {
			itemNota.setOrigem(Origem.MANUAL);
		}
		
		itemNotaFiscalRepository.adicionar(itemNota);
				
		return itemNota;		
	}

	/**
	 * Atualiza os dados do item de nota e item de recebimento fisico.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param recebimentoDTO
	 * @param dataAtual
	 * 
	 * @return ItemNotaFiscal
	 */
	private ItemNotaFiscalEntrada atualizarItemNotaFiscal(
			Usuario usuarioLogado, 
			NotaFiscalEntrada notaFiscal, 
			RecebimentoFisicoDTO recebimentoDTO, 
			Date dataAtual){
				
			ItemNotaFiscalEntrada itemNotaFiscalEntrada = null;
			
			itemNotaFiscalEntrada = itemNotaFiscalEntradaRepository.buscarPorId(recebimentoDTO.getIdItemNota());
			
			ProdutoEdicao produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setId(recebimentoDTO.getIdProdutoEdicao());
			
			itemNotaFiscalEntrada.setQtde(recebimentoDTO.getRepartePrevisto());			
			itemNotaFiscalEntrada.setDataLancamento(recebimentoDTO.getDataLancamento());
			itemNotaFiscalEntrada.setDataRecolhimento(recebimentoDTO.getDataRecolhimento());
			itemNotaFiscalEntrada.setTipoLancamento(recebimentoDTO.getTipoLancamento());
			itemNotaFiscalEntrada.setProdutoEdicao(produtoEdicao);
			itemNotaFiscalEntrada.setUsuario(usuarioLogado);
			itemNotaFiscalEntrada.setNotaFiscal(notaFiscal);
			
			boolean indNotaEnvio = notaFiscal.getNumeroNotaEnvio() != null;
			
			if(indNotaEnvio) {
				itemNotaFiscalEntrada.setOrigem(Origem.INTERFACE);
			} else {
				itemNotaFiscalEntrada.setOrigem(Origem.MANUAL);
			}			
			
			itemNotaFiscalRepository.alterar(itemNotaFiscalEntrada);

			return itemNotaFiscalEntrada;
	}

	
	
	/**
	 * Faz a inserção de um itemRecebimento.
	 *  
	 * @param recebimentoDTO
	 * @param itemNotaFiscal
	 * @param recebimentoFisico
	 */
	private void inserirItemRecebimentoFisico(
			RecebimentoFisicoDTO recebimentoDTO, 
			ItemNotaFiscalEntrada itemNotaFiscal, 
			RecebimentoFisico recebimentoFisico) {
		
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();

		BigInteger qtdFisico = obterQtdRecebimentoFisicoPorQtdPacoteQtdExemplar(
				recebimentoDTO.getPacotePadrao(), 
				recebimentoDTO.getQtdPacote(), 
				recebimentoDTO.getQtdExemplar());
			
		itemRecebimento.setQtdeFisico(qtdFisico);
		
		itemRecebimento.setItemNotaFiscal(itemNotaFiscal);
		
		itemRecebimento.setRecebimentoFisico(recebimentoFisico);
		
		itemRecebimentoFisicoRepository.adicionar(itemRecebimento);		
	}

	/**
	 * Atualiza a qtdFisicao de um itemRecebimento
	 *  
	 * @param recebimentoDTO
	 */
	private void atualizarItemRecebimentoFisico(RecebimentoFisicoDTO recebimentoDTO) {
		
		ItemRecebimentoFisico itemRecebimento = itemRecebimentoFisicoRepository.buscarPorId(recebimentoDTO.getIdItemRecebimentoFisico());
		
		if(Origem.MANUAL.equals(recebimentoDTO.getOrigemItemNota())) {
			
			itemRecebimento.setQtdeFisico(recebimentoDTO.getRepartePrevisto());
			
		} else {
			BigInteger qtdFisico = obterQtdRecebimentoFisicoPorQtdPacoteQtdExemplar(
					recebimentoDTO.getPacotePadrao(), 
					recebimentoDTO.getQtdPacote(), 
					recebimentoDTO.getQtdExemplar());
			
			itemRecebimento.setQtdeFisico(qtdFisico);
		}
		
		
		itemRecebimentoFisicoRepository.alterar(itemRecebimento);		
	}

	
		
	/**
	 * Atualização de Data e Satatus em Recebimento Fisico.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param dataAtual
	 */
	private void alterarRecebimentoFisicoParaConfirmado(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, Date dataAtual) {
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
		
		recebimentoFisico.setDataConfirmacao(dataAtual);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		recebimentoFisico.setConferente(usuarioLogado);
		
		recebimentoFisicoRepository.alterar(recebimentoFisico);
		
	}

	/**
	 * Inserir movimento estoque e estoque de produto
	 */
	private void inserirMovimentoEstoque(
			Usuario usuarioLogado,
			RecebimentoFisicoDTO recebimentoFisicoDTO) {
					
		// Implementado por Cesar Punk Pop
		// Retirado o Else, já que o movimento sempre deve ser gerado (independente de ocorrer diferença ou não)
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.RECEBIMENTO_FISICO);

		movimentoEstoqueService.gerarMovimentoEstoque(
				recebimentoFisicoDTO.getIdProdutoEdicao(), 
				usuarioLogado.getId(), 
				recebimentoFisicoDTO.getRepartePrevisto(),
				tipoMovimento,
				distribuidorService.obterDataOperacaoDistribuidor(), 
				false);
		
		boolean indDiferenca = verificarDiferencaExistente(recebimentoFisicoDTO.getRepartePrevisto(), recebimentoFisicoDTO.getQtdFisico());

		if(indDiferenca) 
			gerarDiferenca(usuarioLogado, recebimentoFisicoDTO);
	}
	
	private void gerarDiferenca(Usuario usuarioLogado,RecebimentoFisicoDTO recebimentoFisicoDTO) {
		
		Diferenca diferenca = obterDiferencaDeItemRecebimentoFisico(usuarioLogado, recebimentoFisicoDTO);
		diferenca = diferencaEstoqueService.lancarDiferenca(diferenca, TipoEstoque.LANCAMENTO);
		
		ItemRecebimentoFisico itemRecebimento = itemRecebimentoFisicoRepository.buscarPorId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
		itemRecebimento.setDiferenca(diferenca);
		itemRecebimentoFisicoRepository.alterar(itemRecebimento);
	}
	
	@Transactional
	public void apagarItemRecebimentoItemNota(RecebimentoFisicoDTO recebimento) {
		try{
			
			
			if(recebimento.getIdItemRecebimentoFisico() != null && recebimento.getIdItemNota() != null){
				
				ItemRecebimentoFisico itemRecebimentoFisico = itemRecebimentoFisicoRepository.buscarPorId(recebimento.getIdItemRecebimentoFisico());				
				itemRecebimentoFisicoRepository.remover(itemRecebimentoFisico);
				
				ItemNotaFiscalEntrada itemNotaFiscalEntrada = itemNotaFiscalEntradaRepository.buscarPorId(recebimento.getIdItemNota());
				itemNotaFiscalEntradaRepository.remover(itemNotaFiscalEntrada);
			}
		}catch(Exception e){
			throw new ValidacaoException(TipoMensagem.ERROR, "Problema ao Excluir Itens da Nota.");
		}
	}

	@Override
	@Transactional
	public RecebimentoFisicoDTO obterRecebimentoFisicoDTO(String codigo, String edicao) {

		ProdutoEdicao produtoEdicao = 
			this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(
				StringUtils.leftPad(codigo, 8, '0'), edicao);
		
		RecebimentoFisicoDTO recebimentoFisicoDTO = null;
		
		if (produtoEdicao!=null) {
			
			recebimentoFisicoDTO = new RecebimentoFisicoDTO();
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			BigDecimal percentualDesconto = 
				this.produtoEdicaoService.obterPorcentualDesconto(produtoEdicao);
			
			BigDecimal valorDesconto = MathUtil.round( MathUtil.calculatePercentageValue( precoVenda, percentualDesconto ), 4);

			recebimentoFisicoDTO.setPrecoCapa(precoVenda.setScale(2, RoundingMode.HALF_EVEN));
			
			recebimentoFisicoDTO.setPrecoDesconto(
				precoVenda.subtract(valorDesconto).setScale(4, RoundingMode.HALF_EVEN).toString());
			
            recebimentoFisicoDTO.setRepartePrevisto(
            	produtoEdicao.getReparteDistribuido());
            
            recebimentoFisicoDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
            
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A [Edição] informada não existe para este [Produto].");
		}
		
		return recebimentoFisicoDTO;
	}
}
