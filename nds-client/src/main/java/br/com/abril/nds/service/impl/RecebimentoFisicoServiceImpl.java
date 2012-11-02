package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.util.TipoMensagem;

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
	private FornecedorService fornecedorService;
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
		
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
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(idNotaFiscal);
		
		if(listaItemRecebimentoFisico == null) {
			return null;
		}
		
		for(RecebimentoFisicoDTO itemRecebimento : listaItemRecebimentoFisico) {
			
			BigInteger qtdRepartePrevisto = itemRecebimento.getRepartePrevisto();
			
			BigDecimal precoCapa = itemRecebimento.getPrecoCapa();
			
			BigDecimal valorTotal = new BigDecimal(0.0D);
			
			if(qtdRepartePrevisto != null && precoCapa != null) {
				valorTotal = precoCapa.multiply( BigDecimal.valueOf( qtdRepartePrevisto.longValue() ) );
			}
			
			itemRecebimento.setValorTotal(valorTotal);
			
		}
		
		return listaItemRecebimentoFisico;		
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
	public void confirmarRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual){
		
		verificarValorDaNota(listaItensNota,notaFiscal.getValorBruto());
		
		inserirDadosRecebimentoFisico(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId());

		if(listaItemRecebimentoFisico == null || listaItemRecebimentoFisico.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens para confirmação de recebimento físico nesta nota fiscal");
		}
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItemRecebimentoFisico) {

			inserirMovimentoEstoque(usuarioLogado, recebimentoFisicoDTO);
			
			inserirLancamento(recebimentoFisicoDTO, dataAtual, usuarioLogado);
			
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
			somaValorDosItens = somaValorDosItens.add(recebimentoFisicoDTO.getValorTotal());
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
		
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);		
		
		notaFiscalRepository.merge(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
		
		if(recebimentoFisico == null){
			recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);
		}	
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota) {
			
			ItemNotaFiscalEntrada itemNota = null;
			
			if(recebimentoFisicoDTO.getIdItemNota() == null) {
				
				itemNota = inserirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual);
				
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
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		
		notaFiscal.setOrigem(Origem.MANUAL);
		
		notaFiscal.setUsuario(usuarioLogado);
		
		notaFiscalRepository.adicionar(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);	
		
		for( RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota ){
			
			ItemNotaFiscalEntrada itemNotaFiscal = inserirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual);
			
			inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNotaFiscal, recebimentoFisico);
						
		}
		
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
	 * Caso não exista um registro um registro de lançamento, este será criado e amarrado ao itemRecebimentoFisico.
	 * Caso o registro de lançamento exista, sera feito apenas a amarração deste com o itemRecebimentoFisico.
	 * 
	 * @param recebimentoFisicoDTO
	 * @param dataAtual
	 * @param usuarioLogado
	 */
	private void inserirLancamento(RecebimentoFisicoDTO recebimentoFisicoDTO, Date dataAtual, Usuario usuarioLogado) {
		
		if(Origem.INTERFACE.equals(recebimentoFisicoDTO.getOrigemItemNota())) {
			return;
		}
		//TODO : Por hora estamos usando somente a Data Lancamento como unica. Verificar se a do Distribuidor também será
		Lancamento lancamento = lancamentoRepository.obterLancamentoPorItensRecebimentoFisico(recebimentoFisicoDTO.getDataLancamento(), null, recebimentoFisicoDTO.getIdProdutoEdicao());
		
		if(lancamento != null) {
			
			if(lancamento.getRecebimentos() == null) {
				lancamento.setRecebimentos(new HashSet<ItemRecebimentoFisico>());
			}
			
			ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
			
			itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
			
			lancamento.getRecebimentos().add(itemRecebimentoFisico);
			
			lancamentoRepository.alterar(lancamento);
		
		} else {

			lancamento = new Lancamento();
			
			lancamento.setDataLancamentoDistribuidor(recebimentoFisicoDTO.getDataLancamento());
			lancamento.setDataRecolhimentoDistribuidor(recebimentoFisicoDTO.getDataRecolhimento());

			lancamento.setDataLancamentoPrevista(recebimentoFisicoDTO.getDataLancamento());
			lancamento.setDataRecolhimentoPrevista(recebimentoFisicoDTO.getDataRecolhimento());
			
			lancamento.setTipoLancamento(recebimentoFisicoDTO.getTipoLancamento());		

			lancamento.setDataCriacao(dataAtual);
		
			lancamento.setReparte(recebimentoFisicoDTO.getRepartePrevisto());		
			
			lancamento.setStatus(StatusLancamento.CONFIRMADO);
			
			lancamento.setDataStatus(dataAtual);
			
			ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
			itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
			
			lancamento.getRecebimentos().add(itemRecebimentoFisico);
			
			ProdutoEdicao produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setId(recebimentoFisicoDTO.getIdProdutoEdicao());
			
			lancamento.setProdutoEdicao(produtoEdicao);
			
			lancamentoRepository.adicionar(lancamento);
			
			inserirHistoricoLancamento(lancamento, dataAtual, usuarioLogado);
		}

		
		
		
	}
	
	/**
	 * Insere novo registro de historico de lancamento.
	 * 
	 * @param lancamento
	 * @param dataAtual
	 * @param usuarioLogado
	 */
	private void inserirHistoricoLancamento(Lancamento lancamento, Date dataAtual, Usuario usuarioLogado) {
		
		HistoricoLancamento historicoLancamento = new HistoricoLancamento();
		
		historicoLancamento.setDataEdicao(dataAtual);
		historicoLancamento.setLancamento(lancamento);
		historicoLancamento.setResponsavel(usuarioLogado);
		historicoLancamento.setStatus(lancamento.getStatus());
		historicoLancamento.setTipoEdicao(TipoEdicao.INCLUSAO);
		
		historicoLancamentoRepository.adicionar(historicoLancamento);
		
		
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
	private ItemNotaFiscalEntrada inserirItemNotaFiscal(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, RecebimentoFisicoDTO recebimentoDTO, Date dataAtual){
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoDTO.getIdProdutoEdicao());
		
		ItemNotaFiscalEntrada itemNota = new ItemNotaFiscalEntrada();		
		
		itemNota.setQtde(recebimentoDTO.getRepartePrevisto());			
		itemNota.setDataLancamento(recebimentoDTO.getDataLancamento());
		itemNota.setDataRecolhimento(recebimentoDTO.getDataRecolhimento());
		itemNota.setTipoLancamento(recebimentoDTO.getTipoLancamento());
		itemNota.setQtde(recebimentoDTO.getRepartePrevisto());
		itemNota.setPreco(recebimentoDTO.getPrecoDesconto());
		itemNota.setProdutoEdicao(produtoEdicao);
		itemNota.setUsuario(usuarioLogado);
		itemNota.setNotaFiscal(notaFiscal);
		itemNota.setOrigem(Origem.MANUAL);
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
			itemNotaFiscalEntrada.setQtde(recebimentoDTO.getRepartePrevisto());
			itemNotaFiscalEntrada.setProdutoEdicao(produtoEdicao);
			itemNotaFiscalEntrada.setUsuario(usuarioLogado);
			itemNotaFiscalEntrada.setNotaFiscal(notaFiscal);
			itemNotaFiscalEntrada.setOrigem(Origem.MANUAL);
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
		
		if(Origem.MANUAL.equals(recebimentoDTO.getOrigemItemNota())) {
			
			itemRecebimento.setQtdeFisico(recebimentoDTO.getRepartePrevisto());
			
		} else {
			
			BigInteger qtdFisico = obterQtdRecebimentoFisicoPorQtdPacoteQtdExemplar(
					recebimentoDTO.getPacotePadrao(), 
					recebimentoDTO.getQtdPacote(), 
					recebimentoDTO.getQtdExemplar());
			
			itemRecebimento.setQtdeFisico(qtdFisico);
			
		}
		
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
	
		
		boolean indDiferenca = verificarDiferencaExistente(recebimentoFisicoDTO.getRepartePrevisto(), recebimentoFisicoDTO.getQtdFisico());
		
		if(indDiferenca) {
			
			Diferenca diferenca = obterDiferencaDeItemRecebimentoFisico(usuarioLogado, recebimentoFisicoDTO);
			diferencaEstoqueService.lancarDiferencaAutomatica(diferenca);
			ItemRecebimentoFisico itemRecebimento = itemRecebimentoFisicoRepository.buscarPorId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
			itemRecebimento.setDiferenca(diferenca);
			itemRecebimentoFisicoRepository.alterar(itemRecebimento);
			
			
			
		} else {
			
			TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
			
			movimentoEstoqueService.gerarMovimentoEstoque(
					recebimentoFisicoDTO.getDataLancamento(), 
					recebimentoFisicoDTO.getIdProdutoEdicao(), 
					usuarioLogado.getId(), 
					recebimentoFisicoDTO.getRepartePrevisto(),
					tipoMovimento);
			
		}
		
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
	
}
