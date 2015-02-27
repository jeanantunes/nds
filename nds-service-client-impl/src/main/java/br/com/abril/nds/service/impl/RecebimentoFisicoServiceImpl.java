package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.enums.CodigoErro;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.vo.ValidacaoVO;

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
	private NaturezaOperacaoRepository tipoNotaFiscalRepository;
	
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

	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;

	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
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
					notaFiscal.setStatusRecebimento(null);
					notaFiscalRepository.merge(notaFiscal);
				}else{
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "O Recebimento ja foi confirmado, não é possível alterar os dados do mesmo.");
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
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "O Recebimento ja foi confirmado, não é possível alterar os dados do mesmo.");
			}
		}
	}
	
	/**
     * Verifica se a nota fiscal já existe 
     * (combinação numero, serie e cnpj do emitente ou numeroNotaEnvio)
     * 
     * @param notaFiscal
     */
	@Transactional(readOnly=true)
	public void validarExisteNotaFiscal(NotaFiscalEntradaFornecedor notaFiscal) {
		
		if ( recebimentoFisicoRepository.existeNotaFiscal(
				notaFiscal.getNumero(), 
				notaFiscal.getSerie(), 
				notaFiscal.getFornecedor().getJuridica().getCnpj(),
				notaFiscal.getNumeroNotaEnvio()) ) {
			
			if(notaFiscal.getNumeroNotaEnvio()==null) {
	            throw new ValidacaoException(TipoMensagem.WARNING,
	                    "Não é possível receber a nota fiscal! Existe outra nota fiscal com o mesmo número, série e emitente (cnpj).");
				
			} else {
	            throw new ValidacaoException(TipoMensagem.WARNING,
	                    "Não é possível receber a nota fiscal! Existe outra nota fiscal com o mesmo numero nota envio.");
				
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
	public void confirmarRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual, Boolean pularValidacao){
		
		if(!pularValidacao)			
			verificarValorDaNota(recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId()), notaFiscal.getValorBruto());
		
		notaFiscal.setDataRecebimento(this.distribuidorService.obterDataOperacaoDistribuidor());
		
		NaturezaOperacao naturezaOperacao = tipoNotaFiscalRepository.obterNaturezaOperacao(distribuidorService.obter().getTipoAtividade(), TipoEmitente.FORNECEDOR, TipoDestinatario.DISTRIBUIDOR, TipoOperacao.ENTRADA, false, false, false);
		
		notaFiscal.setNaturezaOperacao(naturezaOperacao);
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		
		notaFiscal.setUsuario(usuarioLogado);
		
		inserirDadosRecebimentoFisico(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId());

		if(listaItemRecebimentoFisico == null || listaItemRecebimentoFisico.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não existem itens para confirmação de recebimento físico nesta nota fiscal");
		}
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItemRecebimentoFisico) {
			
			inserirMovimentoEstoque(usuarioLogado, recebimentoFisicoDTO);
			
			atualizarLancamento(recebimentoFisicoDTO, dataAtual, usuarioLogado);
			
		}
		
		alterarRecebimentoFisicoParaConfirmado(usuarioLogado, notaFiscal, dataAtual);	
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public ValidacaoVO validarDescontoProduto(Origem origemNota, List<RecebimentoFisicoDTO> listaItensNota) {
		
		if (!Origem.INTERFACE.equals(origemNota)){
			
			return null;
		}

		List<String> mensagens = new ArrayList<>();
		
		for (RecebimentoFisicoDTO item : listaItensNota) {

			ProdutoEdicao produtoEdicao = this.produtoEdicaoService.buscarPorID(item.getIdProdutoEdicao());

			BigDecimal percentualProduto = null;
			if(!produtoEdicao.getOrigem().equals(Origem.INTERFACE)) {
				
				percentualProduto = produtoEdicao.getDesconto();
				
			} else {
				DescontoLogistica descontoLogistica = produtoEdicao.getDescontoLogistica() != null ? 
					produtoEdicao.getDescontoLogistica() : produtoEdicao.getProduto().getDescontoLogistica();
					
				percentualProduto = descontoLogistica == null ? null : descontoLogistica.getPercentualDesconto();
			}

			if (percentualProduto == null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, 
						"Produto " + produtoEdicao.getProduto().getNome() 
								   + " [Cod.: " + produtoEdicao.getProduto().getCodigo() 
								   + " Ed.: " + produtoEdicao.getNumeroEdicao() + "]"
								   + " sem desconto cadastrado!");
			}
			
			if (BigDecimalUtil.neq(percentualProduto, item.getPercentualDesconto())) {
				
				mensagens.add(" [Cod.:" + produtoEdicao.getProduto().getCodigo() 
							 + " Ed.: " + produtoEdicao.getNumeroEdicao() + "]"
							 + " - Cadastro: " + percentualProduto.setScale(2, RoundingMode.HALF_EVEN) + "%"
							 + " / Nota: "  + item.getPercentualDesconto().setScale(2, RoundingMode.HALF_EVEN) + "%");
			}
		}
		
		if (!mensagens.isEmpty()) {
			
			mensagens.add(0, "Os Produtos abaixo poossuem descontos diferentes entre o cadastrado e a nota.");
			
			return new ValidacaoVO(TipoMensagem.WARNING, mensagens);
		}
		
		return null;
	}
	
	        /**
     * Método que compara o valor bruto da nota com a soma dos valores dos itens
     * 
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
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Valor Bruto da Nota não corresponde a soma dos valores Totais do Item da Nota");
		}
	}

	@Override
	@Transactional
	public List<NaturezaOperacao> obterTiposNotasFiscais(TipoOperacao tipoOperacao) {
		
		return tipoNotaFiscalRepository.obterNaturezasOperacoes(tipoOperacao);
		
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
	public List<NaturezaOperacao> obterListaTipoNotaFiscal(TipoOperacao tipoOperacao) {
		return tipoNotaFiscalRepository.obterNaturezasOperacoes();
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
		
		
		notaFiscal.setUsuario(usuarioLogado);
		
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
     * Insere os dados de uma nova nota fiscal, incluindo informações de
     * diferença e lançamento.
     * 
     * @param usuarioLogado
     * @param notaFiscal
     * @param listaItensNota
     */
	private void inserirDadosNovaNotaFiscal(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual) {
		
		notaFiscal.setUsuario(usuarioLogado);
		
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
        valorDesconto = valorDesconto.setScale(4, RoundingMode.HALF_EVEN);
		
		for( RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota ){
			
			ProdutoEdicao produtoEdicao = produtoEdicaoServiceImpl.buscarPorID(recebimentoFisicoDTO.getIdProdutoEdicao());
			
			BigDecimal desconto = new BigDecimal(BigInteger.ZERO, new MathContext(18));
            desconto = desconto.setScale(4, RoundingMode.HALF_EVEN);
			
			if(produtoEdicao != null && produtoEdicao.getOrigem() != null && produtoEdicao.getOrigem().equals(Origem.MANUAL)) {
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
			
		} else {

			throw new ValidacaoException(TipoMensagem.WARNING,
 "Lançamento não encontrado para o produto: "
                + produtoEdicao.getProduto().getNome() + " edição: " + produtoEdicao.getNumeroEdicao());
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
     * Alteração de Data e Satatus em Recebimento Fisico.
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
		
		boolean tratarRepartePromocional = verificarTratamentoRepartePromocional(
				recebimentoFisicoDTO.getIdProdutoEdicao());
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.RECEBIMENTO_FISICO);

		movimentoEstoqueService.gerarMovimentoEstoque(
				recebimentoFisicoDTO.getIdItemRecebimentoFisico(),
				recebimentoFisicoDTO.getIdProdutoEdicao(), 
				usuarioLogado.getId(), 
				recebimentoFisicoDTO.getRepartePrevisto(),
				tipoMovimento);
		
		boolean indDiferenca = verificarDiferencaExistente(recebimentoFisicoDTO.getRepartePrevisto(), recebimentoFisicoDTO.getQtdFisico());

		if(indDiferenca) 
			gerarDiferenca(usuarioLogado, recebimentoFisicoDTO);
		
		if(tratarRepartePromocional)
			tratarRepartePromocional(usuarioLogado, recebimentoFisicoDTO);
	}
	
	        /**
     * Retorna {@code true} caso deva ser gerado um movimento de estoque
     * relativo a reparte promocional para o produtoEdicao em questão.
     * 
     * Caso este produtoEdicao possua reparte promocional e ainda não tenha sido
     * gerado um movimento de reparte promocional para o mesmo, então será
     * retornado {@code true}.
     * 
     * @param idProdutoEdicao
     * @param idItemRecebimentoFisico
     * 
     * @return boolean
     */
	private boolean verificarTratamentoRepartePromocional(Long idProdutoEdicao) {
		
		ProdutoEdicao pe = produtoEdicaoService.buscarPorID(idProdutoEdicao);
		
		if(pe == null || pe.getLancamentos() == null || pe.getLancamentos().isEmpty())
			return false;

		Lancamento lancamento = pe.getLancamentos().iterator().next();
		
		if(lancamento.getRepartePromocional() == null || lancamento.getRepartePromocional().intValue() == 0)
			return false;
		
		List<Long> movimentosRepPromocionalSemEstorno = movimentoEstoqueService.obterMovimentosRepartePromocionalSemEstornoRecebimentoFisico(
				idProdutoEdicao,
				GrupoMovimentoEstoque.ESTORNO_REPARTE_PROMOCIONAL, 
				GrupoMovimentoEstoque.ESTORNO_RECEBIMENTO_FISICO);
		
		
		if(movimentosRepPromocionalSemEstorno != null && !movimentosRepPromocionalSemEstorno.isEmpty()) {
			return false;
		}
		
		
		return true;
		
		
	}
	
	private void tratarRepartePromocional(Usuario usuarioLogado,
			RecebimentoFisicoDTO recebimentoFisicoDTO) {
		
		ProdutoEdicao pe = produtoEdicaoService.buscarPorID(recebimentoFisicoDTO.getIdProdutoEdicao());
				
		Lancamento lancamento = pe.getLancamentos().iterator().next();
					
		TipoMovimentoEstoque tipoMovimento = 
		tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
			GrupoMovimentoEstoque.ESTORNO_REPARTE_PROMOCIONAL);
		
		try {
			movimentoEstoqueService.gerarMovimentoEstoque(
					recebimentoFisicoDTO.getIdItemRecebimentoFisico(),
					recebimentoFisicoDTO.getIdProdutoEdicao(), 
					usuarioLogado.getId(), 
					lancamento.getRepartePromocional(),
					tipoMovimento);
		} catch(ValidacaoException e) {
			
			if(CodigoErro.SALDO_ESTOQUE_DISTRIBUIDOR_INSUFICIENTE.equals(e.getCodigoErro())) {
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
                        " Não é possível realizar o recebimento do do produto [" + pe.getProduto().getCodigo()
								+ " - " + pe.getProduto().getNomeComercial() + " - " 
								+ pe.getNumeroEdicao() 
								+ "]. O valor de reparte promocional ultrapassa a quantidade que esta sendo recebida.");
				
			} else {
				
				throw e;
				
			}
			
		}
		
		
		
		
	}

	private void gerarDiferenca(Usuario usuarioLogado,RecebimentoFisicoDTO recebimentoFisicoDTO) {
		
		Diferenca diferenca = obterDiferencaDeItemRecebimentoFisico(usuarioLogado, recebimentoFisicoDTO);
		diferenca = diferencaEstoqueService.lancarDiferencaAutomatica(diferenca, TipoEstoque.LANCAMENTO);
		
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

	@Override
	@Transactional
	public void excluirNota(Long id) {
		
		if(id == null) 
            throw new ValidacaoException(TipoMensagem.ERROR, "Chave inválida.");
		
		NotaFiscalEntrada nota = notaFiscalRepository.buscarPorId((id));
		
		if(nota == null) 
            throw new ValidacaoException(TipoMensagem.ERROR, "Nota não encontrada.");
						
		validarNotaComProdutosExpedidos(nota);	
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(nota.getDataRecebimento()!= null && DateUtil.obterDiferencaDias(nota.getDataRecebimento(), dataOperacao) != 0)
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "A data de recebimento é diferente da data de operação atual.");
		
		if( !nota.getItens().isEmpty() && nota.getItens().get(0).getRecebimentoFisico() != null ) {
			RecebimentoFisico recebimento = nota.getItens().get(0).getRecebimentoFisico().getRecebimentoFisico();
			recebimento.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
			recebimentoFisicoRepository.alterar(recebimento);
		} else {
            throw new ValidacaoException(TipoMensagem.ERROR, "Esta nota não possui recebimento físico confirmado.");
		}
		
		verificarFaltasESobras(nota);
		
		desfazerMovimentosEstoque(nota.getItens());
		
		if(nota.getOrigem().equals(Origem.INTERFACE)) {
			
			limparNotaFiscalEntrada(nota, true);
						
		} else {
			
			limparNotaFiscalEntrada(nota, false);
		}
	}

	private Map<Long, BigInteger> getMapaItensNotaFiscalEntrada(NotaFiscalEntrada nota){
		
		Map<Long, BigInteger> mapa = new HashMap<>(); 
		List<ItemNotaFiscalEntrada> itens = nota.getItens();
		
		for(ItemNotaFiscalEntrada item : itens) {
			
			ProdutoEdicao pe = item.getProdutoEdicao();
			
			if(pe == null) {
				continue;
			}
			
			mapa.put(pe.getId(), item.getQtde());
		}
		
		return mapa;
		
		
	}
	
	        /**
     * Valida se algum item da nota possui lançamento expedi
     * 
     * @param nota
     */
	private void validarNotaComProdutosExpedidos(NotaFiscalEntrada nota) {
		
		List<Long> idsProdutoEdicaoExpedidos = notaFiscalRepository.pesquisarItensNotaExpedidos(nota.getId());
		
		if(idsProdutoEdicaoExpedidos == null || idsProdutoEdicaoExpedidos.isEmpty()) {
			return;
		}
		
		Map<Long, BigInteger> mapaItensNotaFiscal = getMapaItensNotaFiscalEntrada(nota);
		
		for(Long idProdutoEdicao : idsProdutoEdicaoExpedidos) {
			
			BigInteger qtdeEstoque 	= estoqueProdutoRepository.buscarQtdEstoqueProdutoEdicao(idProdutoEdicao);
		
			BigInteger qtdeItemNota = mapaItensNotaFiscal.get(idProdutoEdicao);
			
			if(qtdeEstoque.compareTo(qtdeItemNota)<0) {
				throw new ValidacaoException(TipoMensagem.ERROR, "A nota possui produto(s) expedido(s).");
			}
			
		}
		
	}

	private void verificarFaltasESobras(NotaFiscalEntrada nota) {
		
		List<ItemNotaFiscalEntrada> itens = nota.getItens();
		
		for(ItemNotaFiscalEntrada itemOriginal : itens) {
		
			ItemRecebimentoFisico itemRecebimento = itemOriginal.getRecebimentoFisico();
			
			Diferenca diferenca = itemRecebimento.getDiferenca();
						
			if (diferenca!=null && diferenca.getStatusConfirmacao().equals(StatusConfirmacao.CONFIRMADO))
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Há diferença(s) gerada(s) e confirmada(s) para essa nota. Não é possível excluí-la.");
				
			
		}
	}

	private void desfazerMovimentosEstoque(List<ItemNotaFiscalEntrada> itens) {
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.RECEBIMENTO_FISICO);

		TipoMovimentoEstoque tipoMovimentoEstorno = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ESTORNO_RECEBIMENTO_FISICO);
		
		TipoMovimentoEstoque tipoMovimentoEstornoRepartePromocional = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ESTORNO_REPARTE_PROMOCIONAL);
		
		for(ItemNotaFiscalEntrada item : itens) {
			
			MovimentoEstoque movimento = movimentoEstoqueService.obterMovimentoEstoqueDoItemNotaFiscal(
					item.getId(),
					tipoMovimento);
			
			MovimentoEstoque movimentoEstornoRepartePromocional = movimentoEstoqueService.obterMovimentoEstoqueDoItemNotaFiscal(
					item.getId(),
					tipoMovimentoEstornoRepartePromocional);
			
			if(movimento==null)
				continue;
			
			BigInteger qtdeRepartePromocional = BigInteger.ZERO;
			
			if(movimentoEstornoRepartePromocional != null)
				qtdeRepartePromocional = movimentoEstornoRepartePromocional.getQtde();
			
			
			movimentoEstoqueService.gerarMovimentoEstoque(
					null,
					item.getProdutoEdicao().getId(), 
					movimento.getUsuario().getId(), 
					movimento.getQtde().subtract(qtdeRepartePromocional),
					tipoMovimentoEstorno);
			
			
			desfazerLigacaoMovimentosEstoqueComItemRecebimentoFisico(movimento, movimentoEstornoRepartePromocional );
			
		}
	}
	
	private void desfazerLigacaoMovimentosEstoqueComItemRecebimentoFisico(MovimentoEstoque ... movimentos) {
		for(MovimentoEstoque m : movimentos){
			
			if(m == null) {
				continue;
			}
			
			m.setItemRecebimentoFisico(null);
			movimentoEstoqueRepository.alterar(m);
			
		}
	}
	
	private void limparNotaFiscalEntrada(NotaFiscalEntrada nota, boolean isFromInterface) {
						
		List<ItemNotaFiscalEntrada> itens = nota.getItens();
		
		for(ItemNotaFiscalEntrada itemOriginal : itens) {
			
			itemOriginal.setNCMProduto(null);
			itemOriginal.setCFOPProduto(null);
			itemOriginal.setUnidadeProduto(null);
			itemOriginal.setCSTProduto(null);
			itemOriginal.setCSOSNProduto(null);
			itemOriginal.setBaseCalculoProduto(null);
			itemOriginal.setAliquotaICMSProduto(null);
			itemOriginal.setValorICMSProduto(null);
			itemOriginal.setAliquotaIPIProduto(null);
			itemOriginal.setValorIPIProduto(null);	
			itemOriginal.setUsuario(null);
			itemOriginal.setOrigem(null);
			
			ItemRecebimentoFisico itemRecebimento = itemOriginal.getRecebimentoFisico();
			
			Diferenca diferenca = itemRecebimento.getDiferenca();
									
			if (diferenca!=null)
				diferencaEstoqueService.excluirLancamentoDiferenca(diferenca.getId());
															
			Lancamento lancamento = lancamentoRepository.obterLancamentoPorItemRecebimento(itemRecebimento.getId());
			
			if(lancamento != null) {
				
				lancamento.getRecebimentos().remove(itemOriginal.getRecebimentoFisico());
				
				lancamentoRepository.alterar(lancamento);
			}
			
			itemOriginal.setRecebimentoFisico(null);
			
			itemNotaFiscalRepository.alterar(itemOriginal);
		
			itemRecebimentoFisicoRepository.remover(itemRecebimento);
			
		}		
		
		if(isFromInterface) {
			
			nota.setOrigem(Origem.INTERFACE);
			nota.setStatusNotaFiscal(StatusNotaFiscalEntrada.NAO_RECEBIDA);
			nota.setEmitida(true);	
			nota.setStatusRecebimento(null);
			nota.setDataRecebimento(null);
		
			notaFiscalRepository.alterar(nota);
		
		} else {

			RecebimentoFisico recebimento = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(nota.getId());
			
			recebimentoFisicoRepository.remover(recebimento);
			
			notaFiscalRepository.remover(nota);	
		}
		
	}
}
