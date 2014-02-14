package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.model.envio.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.envio.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.Intervalo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@Service
public class GeracaoNotaEnvioServiceImpl implements GeracaoNotaEnvioService {

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private DescontoService descontoService;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;

	@Autowired
	private NotaEnvioRepository notaEnvioRepository;

	@Autowired
	private ItemNotaEnvioRepository itemNotaEnvioRepository;

	@Autowired
	private RotaRepository rotaRepository;

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;

	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;
	
	@Autowired
	private CotaAusenteRepository cotaAusenteRepository;
	
	@Autowired
	private RoteiroRepository roteiroRepository;
	
	// Trava para evitar duplicidade ao gerar notas de envio por mais de um usuario simultaneamente
	// O HashMap suporta os mais detalhes e pode ser usado futuramente para restricoes mais finas
	private static final Map<String, Object> TRAVA_GERACAO_NE = new HashMap<>();

	@Transactional
	public List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro) {
		
		 		
		this.validarRoteirizacaoCota(filtro,this.getIdsCotaIntervalo(filtro));
			
		if (filtro.getIdRoteiro() != null) {
			filtro.setFiltroRoteiroEspecial(TipoRoteiro.ESPECIAL.equals(this.roteiroRepository.buscarPorId(filtro.getIdRoteiro()).getTipoRoteiro()));
		}

		if("EMITIDAS".equals(filtro.getExibirNotasEnvio())) {
			return cotaRepository.obterDadosCotasComNotaEnvioEmitidas(filtro);
		} else if("AEMITIR".equals(filtro.getExibirNotasEnvio())) {
			return cotaRepository.obterDadosCotasComNotaEnvioAEmitir(filtro);
		} else {
			return cotaRepository.obterDadosCotasComNotaEnvioEmitidasEAEmitir(filtro);
		}
	}

	@Override
	@Transactional
	public Integer buscaCotasNotasDeEnvioQtd(FiltroConsultaNotaEnvioDTO filtro) {
		
		this.validarRoteirizacaoCota(filtro,this.getIdsCotaIntervalo(filtro));
	
		if (filtro.getIdRoteiro() != null) {
			filtro.setFiltroRoteiroEspecial(TipoRoteiro.ESPECIAL.equals(this.roteiroRepository.buscarPorId(filtro.getIdRoteiro()).getTipoRoteiro()));
		}

		if("EMITIDAS".equals(filtro.getExibirNotasEnvio())) {
			return cotaRepository.obterDadosCotasComNotaEnvioEmitidasCount(filtro);
		} else if("AEMITIR".equals(filtro.getExibirNotasEnvio())) {
			return cotaRepository.obterDadosCotasComNotaEnvioAEmitirCount(filtro);
		} else {
			return cotaRepository.obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(filtro);
		}
		
	}

	private List<ItemNotaEnvio> gerarItensNotaEnvio(
			List<EstudoCota> listaEstudoCota, Cota cota, List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, Intervalo<Date> periodo, Map<String, DescontoDTO> descontos) {

		List<ItemNotaEnvio> listItemNotaEnvio = new ArrayList<ItemNotaEnvio>();

		gerarItensNEMovimento(listaMovimentoEstoqueCota, cota, listItemNotaEnvio, descontos);
		
		gerarItensNEEstudo(listaEstudoCota, cota, listItemNotaEnvio, periodo, descontos);
		
		sortItensByProdutoNome(listItemNotaEnvio);
		
		return listItemNotaEnvio;
	}

	/**
	 * Obtém Quantidade de Movimentos de Saida x Entrada por Produto
	 * Considera Movimentos de grupos especificos para apurar após Expedição
	 * @param periodo
	 * @param idCota
	 * @return Map<Long, BigInteger>
	 */
	public Map<Long, BigInteger> obtemQuantidadeMovimentosPorProdutoAposExpedicao(Intervalo<Date> periodo, Long idCota){
		
		GrupoMovimentoEstoque[] gruposMovimentoEstoque   = {
														      GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO,
														      GrupoMovimentoEstoque.FALTA_DE_COTA,
														      GrupoMovimentoEstoque.FALTA_EM_COTA,
														      GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE,
														      GrupoMovimentoEstoque.SOBRA_DE_COTA,
														      GrupoMovimentoEstoque.SOBRA_EM_COTA,
											                  GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE,
											                  GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE
														   };
		
		Map<Long, BigInteger> mapProdutos = 
				this.movimentoEstoqueCotaRepository.obterQtdMovimentoCotaPorTipoMovimento(periodo,
																						  idCota,
																						  gruposMovimentoEstoque); 
	
		return mapProdutos;
	}

	/**
	 * Gera itens de nota de envio a partir do movimentos de estoque que não possuem estudo.
	 * 
	 * @param listaMovimentoEstoqueCota
	 * @param cota
	 * @param listItemNotaEnvio
	 * @param descontos TODO
	 */
	private void gerarItensNEMovimento(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, Cota cota,
			List<ItemNotaEnvio> listItemNotaEnvio, Map<String, DescontoDTO> descontos) {
		
        if (listaMovimentoEstoqueCota == null || listaMovimentoEstoqueCota.isEmpty()){
			
			return;
		}
					
		for(MovimentoEstoqueCota mec : listaMovimentoEstoqueCota) {
			
			Lancamento lancamento = mec.getLancamento();
			
			ProdutoEdicao produtoEdicao = mec.getProdutoEdicao();
			
			if (lancamento.getEstudo() == null) {
				
				throw new ValidacaoException(
					TipoMensagem.ERROR, "Produto: " + produtoEdicao + " não possui estudo.");
			}
			
			ItemNotaEnvio itemNotaEnvio = getItemNE(listItemNotaEnvio, mec.getProdutoEdicao());
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			itemNotaEnvio.setSequenciaMatrizLancamento(lancamento.getSequenciaMatriz());
			
			for(EstudoCota ec : lancamento.getEstudo().getEstudoCotas()) {
				if(ec.getCota().getNumeroCota().equals(mec.getCota().getNumeroCota()) 
						&& ec.getEstudo().getProdutoEdicao().getId().equals(mec.getProdutoEdicao().getId())) {
					itemNotaEnvio.setEstudoCota(ec);
					break;
				}
			}
			
			itemNotaEnvio.setProdutoEdicao(produtoEdicao);
			itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNome());
			
			DescontoDTO descontoDTO = null;
			try {
				descontoDTO = descontoService.obterDescontoPor(descontos, cota.getId(), produtoEdicao.getProduto().getFornecedor().getId(), produtoEdicao.getProduto().getId(), produtoEdicao.getId());
			} catch (Exception e) {

				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao obter desconto: Cota: "+ cota.getNumeroCota() +" / Produto: "+ produtoEdicao.getProduto().getCodigo() +" - "+ produtoEdicao.getNumeroEdicao());
			}	
			
			if(descontoDTO == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Cota/Produto sem desconto: Cota: "+ cota.getNumeroCota() +" / Produto: "+ produtoEdicao.getProduto().getCodigo() +" - "+ produtoEdicao.getNumeroEdicao());
			}
			
			itemNotaEnvio.setDesconto(descontoDTO.getValor());
			
			BigInteger quantidade;
			
			quantidade = itemNotaEnvio.getReparte();
			
			if(quantidade == null)
				quantidade = BigInteger.ZERO;
			
			quantidade = quantidade.add(mec.getQtde());
			
			itemNotaEnvio.setReparte(quantidade);
			
			itemNotaEnvio.setPrecoCapa(precoVenda);
			
			List<MovimentoEstoqueCota> movimentosProdutoSemEstudo = itemNotaEnvio.getMovimentosProdutoSemEstudo();
			
			if(movimentosProdutoSemEstudo == null)
				movimentosProdutoSemEstudo = new ArrayList<MovimentoEstoqueCota>();
			
			movimentosProdutoSemEstudo.add(mec);
			
			itemNotaEnvio.setMovimentosProdutoSemEstudo(movimentosProdutoSemEstudo);
			
			listItemNotaEnvio.add(itemNotaEnvio);
		}
		
	}

	/**
	 * Método que verifica se ja existe um itemNotaEnvio para um determinado produto.
	 * caso não exista retorna uma nova instancia de ItemNotaEnvio.
	 *             
	 * @param listItemNotaEnvio
	 * @param produtoEdicao
	 * @return
	 */
	private ItemNotaEnvio getItemNE(List<ItemNotaEnvio> listItemNotaEnvio,
			ProdutoEdicao produtoEdicao) {
		
		ItemNotaEnvio itemNE = new ItemNotaEnvio();
		
		for(ItemNotaEnvio item : listItemNotaEnvio) {
			
			if(item.getProdutoEdicao().getId().equals(produtoEdicao.getId())) {
				itemNE = item;
				break;
			}
		}
		
		return itemNE;
	}

	/**
	 * Gera itens de Nota de Envio a partir dos Estudos Cota
	 * 
	 * @param listaEstudoCota
	 * @param cota
	 * @param listItemNotaEnvio
	 * @param descontos TODO
	 */
	private void gerarItensNEEstudo(List<EstudoCota> listaEstudoCota,
			Cota cota, List<ItemNotaEnvio> listItemNotaEnvio, Intervalo<Date> periodo, Map<String, DescontoDTO> descontos) {
		
		if (listaEstudoCota == null || listaEstudoCota.isEmpty()){
			
			return;
		}
		
		//Movimentos de Entrada e Saida para recalcular após Expedicão.
		Map<Long, BigInteger> mapProdutos = this.obtemQuantidadeMovimentosPorProdutoAposExpedicao(periodo, cota.getId());
		
		
		for (EstudoCota estudoCota : listaEstudoCota) {
			
			//Verifica se Estudo ja possui itens de Nota de Envio.
			if (estudoCota.getItemNotaEnvios()!=null && !estudoCota.getItemNotaEnvios().isEmpty()) {
				
				listItemNotaEnvio.addAll(estudoCota.getItemNotaEnvios());
				
				continue;
			}
			
			ProdutoEdicao produtoEdicao = estudoCota.getEstudo().getProdutoEdicao();

			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			BigInteger quantidadeResultante = BigInteger.ZERO;
			
			quantidadeResultante = mapProdutos.get(produtoEdicao.getId());
			
			DescontoDTO descontoDTO = null;
			try {
				
				descontoDTO = descontoService.obterDescontoPor(descontos, cota.getId(), produtoEdicao.getProduto().getFornecedor().getId(), produtoEdicao.getProduto().getId(), produtoEdicao.getId());
			} catch (Exception e) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao obter desconto: Cota: "+ cota.getNumeroCota() +" / Produto: "+ produtoEdicao.getProduto().getCodigo() +" - "+ produtoEdicao.getNumeroEdicao());
			}	
			
			if(descontoDTO == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Cota/Produto sem desconto: Cota: "+ cota.getNumeroCota() +" / Produto: "+ produtoEdicao.getProduto().getCodigo() +" - "+ produtoEdicao.getNumeroEdicao());
			}

			if(quantidadeResultante == null) {
			
			    quantidadeResultante = BigInteger.ZERO;
			}
			
			BigInteger quantidade = quantidadeResultante.add(estudoCota.getQtdeEfetiva() == null ? BigInteger.ZERO : estudoCota.getQtdeEfetiva());

			
			ItemNotaEnvio itemNotaEnvio = null;
			
			for(ItemNotaEnvio item : listItemNotaEnvio) {
				if(item.getProdutoEdicao().getId().equals(produtoEdicao.getId())) {
					itemNotaEnvio = item;
					break;
				}
				
				if(item.getEstudoCota() != null) {
					Integer sequenciaMatrizLancamento = item.getEstudoCota().getEstudo().getLancamento().getSequenciaMatriz();
					item.setSequenciaMatrizLancamento(sequenciaMatrizLancamento);
				}
			}
			
			boolean itemExistente = itemNotaEnvio != null;
			
			//Cria novo item nota caso o Estudo ainda não possua
			itemNotaEnvio = criarNovoItemNotaEnvio(itemNotaEnvio, estudoCota,
																 produtoEdicao,
																 precoVenda,
																 ((descontoDTO != null && descontoDTO.getValor() != null) ? 
																		 descontoDTO.getValor() : BigDecimal.ZERO)
																 , quantidade);
			
			if(estudoCota.getEstudo() != null && estudoCota.getEstudo().getLancamento() != null) {
				itemNotaEnvio.setSequenciaMatrizLancamento(estudoCota.getEstudo().getLancamento().getSequenciaMatriz());
			}
			
			if(!itemExistente)
				listItemNotaEnvio.add(itemNotaEnvio);
					
		}
	}

	private void sortItensByProdutoNome(List<ItemNotaEnvio> listItemNotaEnvio) {
		Collections.sort(listItemNotaEnvio, new Comparator<ItemNotaEnvio>(){
			@Override
			public int compare(ItemNotaEnvio o1, ItemNotaEnvio o2) {

				return getNomeProdutoEdicao(o1).compareTo(getNomeProdutoEdicao(o2));
			}

		});
	}

	private String getNomeProdutoEdicao(ItemNotaEnvio itemNotaEnvio) {
		
		String nomeProduto = "";
		
		if (itemNotaEnvio.getProdutoEdicao() != null) {
			if(itemNotaEnvio.getProdutoEdicao().getProduto() != null) {
				nomeProduto = itemNotaEnvio.getProdutoEdicao().getProduto().getNome();
			}
		}
		
		return nomeProduto;
	}
	
	private ItemNotaEnvio criarNovoItemNotaEnvio(ItemNotaEnvio itemNotaEnvio, EstudoCota estudoCota,
			ProdutoEdicao produtoEdicao, BigDecimal precoVenda,
			BigDecimal percentualDesconto, BigInteger quantidade) {

		if(itemNotaEnvio == null)
			itemNotaEnvio = new ItemNotaEnvio();

		itemNotaEnvio.setProdutoEdicao(produtoEdicao);
		itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNome());
		itemNotaEnvio.setDesconto(percentualDesconto);
		itemNotaEnvio.setReparte(quantidade);
		itemNotaEnvio.setPrecoCapa(precoVenda);
		itemNotaEnvio.setEstudoCota(estudoCota);

		return itemNotaEnvio;
	}

	/**
	 * Obtem idRota do PDV principal da Cota e caso não encontre obtem do box da cota
	 * @param pdvPrincipal
	 * @param cota
	 * @return Long idRota
	 */
	private Long getIdRotaCota(PDV pdvPrincipal, Cota cota){

		Long idRota = null;

		for (RotaPDV r : pdvPrincipal.getRotas()){
			
			if (!r.getRota().getRoteiro().getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)){
				
				idRota = r.getRota().getId();
				
				break;
			}
		}

		if (idRota == null) {

			if(cota.getBox() != null) {
				
				List<Roteiro> roteiros = cota.getBox().getRoteirizacao().getRoteiros();
				
				Roteiro roteiro = null;

				for (Roteiro r : roteiros) {
	
					if (!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {
	
						roteiro = r;
						
						break;
					}
				}
	
				idRota = (Long) roteiro.getRotas().get(0).getId();
			}
		}	
		
		return idRota;
	}
	
	@Override
	@Transactional(readOnly = false)
	public List<NotaEnvio> visualizar(FiltroConsultaNotaEnvioDTO filtro) {
		
		List<Long> listaIdCotas = this.getIdsCotaIntervalo(filtro);

		validarRoteirizacaoCota(filtro, listaIdCotas);
		
		PessoaJuridica pessoaEmitente = this.distribuidorRepository.juridica();
		
		List<EstudoCota> listaEstudosCotas = 
				this.estudoCotaRepository.obterEstudosCotaParaNotaEnvio(listaIdCotas, filtro.getIntervaloMovimento(),filtro.getIdFornecedores(), filtro.getExibirNotasEnvio());
		
		List<NotaEnvio> notasEnvio = new ArrayList<>();
		
		Map<Long, List<EstudoCota>> mapEstudosCota = this.getMapEstudosCota(listaEstudosCotas);
		
		Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao(null, null);
		
		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository.obterEnderecoPrincipal();
		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository.obterTelefonePrincipal();
		
		for (Long idCota : listaIdCotas) {

			this.gerarNotaEnvioParaVisualizacao(pessoaEmitente,
				                                idCota,  
				                                filtro, 
												notasEnvio,
												null, null, null, 
												mapEstudosCota.get(idCota),
												descontos, enderecoDistribuidor, telefoneDistribuidor);
		}
		
		return notasEnvio;
	}

	private void gerarNotaEnvioParaVisualizacao(PessoaJuridica pessoaEmitente,
											    Long idCota, 
											    FiltroConsultaNotaEnvioDTO filtro,
											    List<NotaEnvio> notasEnvio, 
											    String chaveAcesso,
											    Integer codigoNaturezaOperacao, 
											    String descricaoNaturezaOperacao,
											    List<EstudoCota> listaEstudosCota,
											    Map<String, DescontoDTO> descontos, 
											    EnderecoDistribuidor enderecoDistribuidor, 
											    TelefoneDistribuidor telefoneDistribuidor) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if (cota == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada!");
		}
		
		IdentificacaoDestinatario destinatarioAtualizado = this.obterDestinatarioAtualizado(cota, filtro.getIdRota(), filtro.getIntervaloMovimento());
		
		List<ItemNotaEnvio> listaItemNotaEnvio = 
				this.processarNotasDeEnvioGeradas(cota, filtro, notasEnvio, listaEstudosCota, destinatarioAtualizado, descontos);

		NotaEnvio notaEnvio = null;
		
		if(listaItemNotaEnvio != null && listaItemNotaEnvio.size() > 0) {

			notaEnvio = criarNotaEnvio(destinatarioAtualizado,
					                   chaveAcesso,
					                   codigoNaturezaOperacao, descricaoNaturezaOperacao, filtro.getDataEmissao(),
					                   pessoaEmitente, enderecoDistribuidor, telefoneDistribuidor);
	
			int sequencia = 0;

			for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {

				if (itemNotaEnvio.getItemNotaEnvioPK() == null) {
					
					itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio, ++sequencia));
				}
			}

			notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);
	
			notasEnvio.add(notaEnvio);
		}
		
		for(NotaEnvio item : notasEnvio){
			item.getListaItemNotaEnvio().isEmpty();
			
			Collections.sort(item.getListaItemNotaEnvio(), new Comparator<ItemNotaEnvio>(){
				@Override
				public int compare(ItemNotaEnvio o1, ItemNotaEnvio o2) {
				    
						if(o1 != null && o1.getSequenciaMatrizLancamento() != null && o2 != null && o2.getSequenciaMatrizLancamento() != null) {
				    	    return o1.getSequenciaMatrizLancamento().compareTo(o2.getSequenciaMatrizLancamento());
				    	} else if ((o1.getProdutoEdicao() != null && o1.getProdutoEdicao().getProduto() != null)
				    		&& (o2.getProdutoEdicao() != null && o2.getProdutoEdicao().getProduto() != null)) {
	    						o1.getProdutoEdicao().getProduto().getNome().compareTo(o2.getProdutoEdicao().getProduto().getNome());
	    				}
	    							    	
				    	return 0;
				}
				
			});
		}
		
	}
	
	/**
	 * Atualiza os itens e o Dados do Destinatario das Notas de Envio
	 * @param novasNotasEnvio
	 * @param listaItemNotaEnvio
	 * @param destinatarioAtualizado
	 */
	private void atualizarNotasEnvio(List<NotaEnvio> novasNotasEnvio, 
		                             List<ItemNotaEnvio> listaItemNotaEnvio, 
		                             IdentificacaoDestinatario destinatarioAtualizado){

		List<ItemNotaEnvio> itensNotasEnvioExistentes = new ArrayList<ItemNotaEnvio>();
		
		for(ItemNotaEnvio ine : listaItemNotaEnvio) {
			
			ItemNotaEnvioPK itemNotaEnvioPK = ine.getItemNotaEnvioPK();
 
			NotaEnvio notaEnvio = (itemNotaEnvioPK == null) ? null : itemNotaEnvioPK.getNotaEnvio();
			
			if (notaEnvio != null){
				
				itensNotasEnvioExistentes.add(ine);
				
				if (!novasNotasEnvio.contains(notaEnvio)) {
					
					notaEnvio.setDestinatario(destinatarioAtualizado);

					novasNotasEnvio.add(notaEnvio);
				}
			}
		}
		
		listaItemNotaEnvio.removeAll(itensNotasEnvioExistentes);
	}
	
	/**
	 * Define sequencia para cada ItemNotaEnvio e atualiza MovimentoEstoqueCota sem estudo com cada ItemNotaEnvio
	 * @param notaEnvio
	 * @param listaItemNotaEnvio
	 */
	private void atualizaMovimentosEstoqueItemNotaEnvio(NotaEnvio notaEnvio, List<ItemNotaEnvio> listaItemNotaEnvio){
		
		int sequencia = 0;
		
		for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {
			
			if(itemNotaEnvio.getItemNotaEnvioPK() == null) {
				
				itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio, ++sequencia));
				
				Integer seqMatrizlancamento = 0;
				
				if(itemNotaEnvio.getMovimentosProdutoSemEstudo() != null && !itemNotaEnvio.getMovimentosProdutoSemEstudo().isEmpty()) {
					
					for(MovimentoEstoqueCota mec : itemNotaEnvio.getMovimentosProdutoSemEstudo()) {
						
						
						mec.setItemNotaEnvio(itemNotaEnvio);
						
						if (mec.getLancamento() != null){
							
							seqMatrizlancamento = mec.getLancamento().getSequenciaMatriz();
						}

						this.movimentoEstoqueCotaRepository.merge(mec);
					}
					
				} else {
					
					seqMatrizlancamento = itemNotaEnvio.getEstudoCota().getEstudo().getLancamento().getSequenciaMatriz();
				}
				
				itemNotaEnvio.setSequenciaMatrizLancamento(seqMatrizlancamento);

				itemNotaEnvioRepository.adicionar(itemNotaEnvio);
			}
		}
	}

	/**
	 * Gera Nota de envio da Cota
	 * @param pessoaEmitente
	 * @param idCota
	 * @param idRota
	 * @param notasEnvio
	 * @param dataEmissao
	 * @param periodo
	 * @param listaIdFornecedores
	 * @param chaveAcesso
	 * @param codigoNaturezaOperacao
	 * @param descricaoNaturezaOperacao
	 * @param listaEstudosCota
	 * @param descontos TODO
	 * @param enderecoDistribuidor TODO
	 * @param telefoneDistribuidor TODO
	 */
	private void getNotaEnvioCota(PessoaJuridica pessoaEmitente,
		                          Long idCota, 
		                          FiltroConsultaNotaEnvioDTO filtro, 
			                      List<NotaEnvio> notasEnvio, 
                                  String chaveAcesso,
			                      Integer codigoNaturezaOperacao, 
			                      String descricaoNaturezaOperacao,
			                      List<EstudoCota> listaEstudosCota, 
			                      Map<String, DescontoDTO> descontos, 
			                      EnderecoDistribuidor enderecoDistribuidor, 
			                      TelefoneDistribuidor telefoneDistribuidor) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if (cota == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada!");
		}
		
		IdentificacaoDestinatario destinatarioAtualizado = this.obterDestinatarioAtualizado(cota, filtro.getIdRota(), filtro.getIntervaloMovimento());
		if(destinatarioAtualizado == null)
			return;// Caso retorne null pular próxima cota pois não encontrou endereço PDV p uma cota sem movimentoEstudo 
		
		List<ItemNotaEnvio> listaItemNotaEnvio =
			this.processarNotasDeEnvioGeradas(
				cota, filtro, notasEnvio, listaEstudosCota, destinatarioAtualizado, descontos);

		if(listaItemNotaEnvio != null && listaItemNotaEnvio.size() > 0) {

			NotaEnvio notaEnvio = criarNotaEnvio(destinatarioAtualizado, chaveAcesso, codigoNaturezaOperacao, 
								                 descricaoNaturezaOperacao, filtro.getDataEmissao(), pessoaEmitente, enderecoDistribuidor, telefoneDistribuidor);
	
			notaEnvioRepository.adicionar(notaEnvio);
			
			this.atualizaMovimentosEstoqueItemNotaEnvio(notaEnvio, listaItemNotaEnvio);
	
			notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);
	
			notaEnvio = notaEnvioRepository.merge(notaEnvio);
			
			notasEnvio.add(notaEnvio);
			
		}  
	}
	
	/*
	 * Retorna uma lista com os movimentos estoque cota filtrados, onde os movimentos estoque cota não tiveram itens de nota de envio gerados
	 */
	private List<MovimentoEstoqueCota> filtraItensSemItemNotaEnvioGerado(List<MovimentoEstoqueCota> itens){
		
		if(itens == null || itens.isEmpty()){
			return null;
		}
		
		Predicate<MovimentoEstoqueCota> movimentoEstoqueCotaPredicate = new Predicate<MovimentoEstoqueCota>() {
			  public boolean apply(MovimentoEstoqueCota mvCota) {
			    return mvCota.getItemNotaEnvio() == null ;
			  }
		};
		
		 Collection<MovimentoEstoqueCota> filteredCollection = 
			Collections2.filter(itens, movimentoEstoqueCotaPredicate);
		
		 if (filteredCollection != null) {
			 return  Lists.newArrayList(filteredCollection);
		 } else {
			 return new ArrayList<>();
		 }
	}
	
	/*
	 * Efetua o processamento das notas de envio já geradas
	 */
	private List<ItemNotaEnvio> processarNotasDeEnvioGeradas(Cota cota, 
								FiltroConsultaNotaEnvioDTO filtro,
								List<NotaEnvio> notasEnvio, 
								List<EstudoCota> listaEstudosCota,
								IdentificacaoDestinatario destinatarioAtualizado,
								Map<String, DescontoDTO> descontos) {
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaSemEstudoPor(
					cota.getId(), filtro.getIntervaloMovimento(), filtro.getIdFornecedores(),
					GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE);
		
		listaMovimentoEstoqueCota = this.filtraItensSemItemNotaEnvioGerado(listaMovimentoEstoqueCota);
		
		List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(listaEstudosCota, cota, listaMovimentoEstoqueCota, filtro.getIntervaloMovimento(), descontos);

		if (listaItemNotaEnvio==null || listaItemNotaEnvio.isEmpty()) {

			return null;
		}
		
		this.atualizarNotasEnvio(notasEnvio, listaItemNotaEnvio, destinatarioAtualizado);
		
		return listaItemNotaEnvio;
	}
	
	private IdentificacaoDestinatario obterDestinatarioAtualizado(Cota cota, Long idRota, Intervalo<Date> periodo){
		
		PDV pdvPrincipal = this.pdvRepository.obterPDVPrincipal(cota.getId());

		if (idRota == null) {
			
			idRota = this.getIdRotaCota(pdvPrincipal, cota);
		}
		
		return this.carregaDestinatario(cota, idRota, pdvPrincipal, periodo);
	}
	
	/**
	 * Separa as Notas de Envio por Cota
	 * @param estudosCotas
	 * @return Map<Long,List<EstudoCota>>
	 */
	private Map<Long,List<EstudoCota>> getMapEstudosCota(List<EstudoCota> estudosCotas){
		
		Map<Long,List<EstudoCota>> estudosCota = new HashMap<Long, List<EstudoCota>>();

        List<EstudoCota> estudos = null;
    	
        for (EstudoCota estudo : estudosCotas){
    		
        	estudos = estudosCota.get(estudo.getCota().getId());
    		
        	estudos = estudos == null ? new ArrayList<EstudoCota>() : estudos;
    		
        	estudos.add(estudo);
			
        	estudosCota.put(estudo.getCota().getId(), estudos);
    	}

		return estudosCota;
	}

	/**
	 * Ordena a lista de Notas de Envio por Roteirização. 
	 * 
	 * @param notasEnvio - lista de notas de envio
	 * 
	 * @return List<NotaEnvio>
	 */
	private List<NotaEnvio> ordenarNotasEnvioPorRoteirizacao(List<NotaEnvio> notasEnvio) {
		
		HashMap<Integer, List<NotaEnvio>> mapaNotasEnvioPorCota = 
			new HashMap<Integer, List<NotaEnvio>>();
		
		for (NotaEnvio ne : notasEnvio) {
			
			Integer numeroCota = ne.getDestinatario().getNumeroCota();
			
			List<NotaEnvio> nes =
				mapaNotasEnvioPorCota.get(numeroCota);
			
			if (nes == null) {
				
				nes = new ArrayList<NotaEnvio>();
			}
			
			nes.add(ne);
			
			mapaNotasEnvioPorCota.put(numeroCota, nes);
		}
		
		List<Integer> numerosCotaOrdenadosPelaRoteirizacao = this.roteirizacaoRepository.obterNumerosCotaOrdenadosRoteirizacao();
		
		List<NotaEnvio> notasEnvioOrdenadas = new ArrayList<>();
		
		for (Integer numeroCota : numerosCotaOrdenadosPelaRoteirizacao) {
			
			if (mapaNotasEnvioPorCota.containsKey(numeroCota)) {
			
				notasEnvioOrdenadas.addAll(mapaNotasEnvioPorCota.get(numeroCota));
			}
		}
		
		return notasEnvioOrdenadas;
	}

	/**
	 * Gera Notas de Envio para as Cotas
	 * @param idCotas
	 * @param filtro
	 * @param chaveAcesso
	 * @param codigoNaturezaOperacao
	 * @param descricaoNaturezaOperacao
	 */
	private List<NotaEnvio> gerar(List<Long> idCotas, FiltroConsultaNotaEnvioDTO filtro, String chaveAcesso,
								  Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao) {
		
		if(idCotas == null || idCotas.size() == 0)
			return null;
			
		PessoaJuridica pessoaEmitente = this.distribuidorRepository.juridica();
		
		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository.obterEnderecoPrincipal();
		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository.obterTelefonePrincipal();
		
		List<EstudoCota> listaEstudosCotas = this.estudoCotaRepository.obterEstudosCotaParaNotaEnvio(idCotas, filtro.getIntervaloMovimento(), filtro.getIdFornecedores(), filtro.getExibirNotasEnvio());
		
		List<NotaEnvio> notasEnvio = new ArrayList<>();
		
		Map<Long, List<EstudoCota>> mapEstudosCota = this.getMapEstudosCota(listaEstudosCotas);
		
		Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao(null, null);
		
		for (Long idCota : idCotas) {

			this.getNotaEnvioCota(pessoaEmitente,
                                  idCota,  
                                  filtro, 
								  notasEnvio, 
								  chaveAcesso, 
								  codigoNaturezaOperacao, 
								  descricaoNaturezaOperacao, 
								  mapEstudosCota.get(idCota), 
								  descontos, 
								  enderecoDistribuidor, 
								  telefoneDistribuidor);
		}
		
		return this.ordenarNotasEnvioPorRoteirizacao(notasEnvio);
	}

	/**
	 * Cria nova nota de Envio
	 * @param destinatarioAtualizado
	 * @param chaveAcesso
	 * @param codigoNaturezaOperacao
	 * @param descricaoNaturezaOperacao
	 * @param dataEmissao
	 * @param pessoaEmitente
	 * @param enderecoDistribuidor TODO
	 * @param telefoneDistribuidor TODO
	 * @return NotaEnvio
	 * @throws ValidacaoException
	 */
	private NotaEnvio criarNotaEnvio(IdentificacaoDestinatario destinatarioAtualizado,
									 String chaveAcesso, Integer codigoNaturezaOperacao,
									 String descricaoNaturezaOperacao, Date dataEmissao,
									 PessoaJuridica pessoaEmitente, EnderecoDistribuidor enderecoDistribuidor, 
									 TelefoneDistribuidor telefoneDistribuidor) throws ValidacaoException {
		
		NotaEnvio notaEnvio = new NotaEnvio();

		notaEnvio.setEmitente(carregarEmitente(pessoaEmitente, enderecoDistribuidor, telefoneDistribuidor));
		notaEnvio.setDestinatario(destinatarioAtualizado);
		notaEnvio.setChaveAcesso(chaveAcesso);
		notaEnvio.setCodigoNaturezaOperacao(codigoNaturezaOperacao);
		notaEnvio.setDescricaoNaturezaOperacao(descricaoNaturezaOperacao);
		notaEnvio.setDataEmissao(dataEmissao);
		
		return notaEnvio;
	}

	/**
	 * @param enderecoDistribuidor TODO
	 * @param telefoneDistribuidor TODO
	 * @param distribuidor
	 * @return
	 * @throws ValidacaoException
	 */
	private IdentificacaoEmitente carregarEmitente(PessoaJuridica pessoaEmitente, EnderecoDistribuidor enderecoDistribuidor, TelefoneDistribuidor telefoneDistribuidor)
			throws ValidacaoException {

		if (enderecoDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Endereço principal do distribuidor não encontrado!");
		}
		
		IdentificacaoEmitente emitente = new IdentificacaoEmitente();

		// Corrigido devo ao fato da tabela pessoa gravar os documentos com
		// máscara, embora o campo documento da tabela nota_envio esperar apenas
		// 14 caracteres
		String documento = pessoaEmitente.getDocumento().replaceAll("[-+.^:,/]", "");

		emitente.setDocumento(documento);
		emitente.setNome(pessoaEmitente.getNome());
		emitente.setPessoaEmitenteReferencia(pessoaEmitente);
		emitente.setInscricaoEstadual(pessoaEmitente.getInscricaoEstadual());

		try {
			emitente.setEndereco(cloneEndereco(enderecoDistribuidor.getEndereco()));
		} catch (Exception exception) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao adicionar o endereço do distribuidor!");
		}

		if (telefoneDistribuidor != null) {
			Telefone telefone = telefoneDistribuidor.getTelefone();
			telefoneRepository.detach(telefone);
			telefone.setId(null);
			telefone.setPessoa(null);
			telefoneRepository.adicionar(telefone);
			emitente.setTelefone(telefone);
		}
		return emitente;
	}

	private IdentificacaoDestinatario carregaDestinatario(Cota cota, Long idRota, PDV pdvPrincipalCota, Intervalo<Date> periodo) {
		
	    IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
	    
	    destinatario.setNumeroCota(cota.getNumeroCota());
	    
	    destinatario.setDocumento(cota.getPessoa().getDocumento());
	 	
		EnderecoPDV enderecoPdv = pdvPrincipalCota!=null?pdvPrincipalCota.getEnderecoEntrega():null;
		
		if (enderecoPdv == null) {
		
			for (EnderecoPDV ePdv : pdvPrincipalCota.getEnderecos()){
			    
				if (ePdv.isPrincipal()){
				    
					enderecoPdv = ePdv;
				}
			}
		}

		if (enderecoPdv == null) {

			/*
			 * Verifica se exite movimento e estudo para a cota na data
			 * 
			 * Caso exista o sistema deve apresentar erro por falta de endereço PDV
			 */
			if(existeMovimentoEstudoCotaData(cota, periodo)){
				
				throw new ValidacaoException(TipoMensagem.WARNING,
						"Endereço do PDV principal da cota " + cota.getNumeroCota() + " não encontrado!");
			}else{
				return null;
			}
		}

		try {
			
			destinatario.setEndereco(cloneEndereco(enderecoPdv.getEndereco()));
		} catch (CloneNotSupportedException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Erro ao adicionar o endereço do Emitente!");
		}

		if (cota.getPessoa() instanceof PessoaJuridica) {
			
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			
			destinatario.setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
			
			destinatario.setDocumento(pessoaJuridica.getCnpj());
		}
		else if (cota.getPessoa() instanceof PessoaFisica) {
			
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			
			destinatario.setInscricaoEstadual(pessoaFisica.getRg());
			
			destinatario.setDocumento(pessoaFisica.getCpf());
		}
		
		destinatario.setNome(cota.getPessoa().getNome());
		destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());

		Telefone telefone = telefoneCotaRepository.obterTelefonePrincipalCota(cota.getId());
		
		if (telefone!=null){
		
			telefoneRepository.detach(telefone);
			
			telefone.setId(null);
			
			telefone.setPessoa(null);
			
			telefoneRepository.adicionar(telefone);
			
			destinatario.setTelefone(telefone);
		}
		
		if(cota.getBox() != null) {
			
			destinatario.setCodigoBox(cota.getBox().getCodigo());
			destinatario.setNomeBox(cota.getBox().getNome());
		}

		if (idRota != null) {
			
			Rota rota = rotaRepository.buscarPorId(idRota);
			
			if (rota == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR,"Rota não encontrada!");
			}
			
			destinatario.setCodigoRota(rota.getId().toString());
			destinatario.setDescricaoRota(rota.getDescricaoRota());
		}
		return destinatario;
	}

	private boolean existeMovimentoEstudoCotaData(Cota cota, Intervalo<Date> periodo) {
		List<EstudoCota> obterEstudoCota = estudoCotaRepository.obterEstudoCota(cota.getId(), periodo.getDe(), periodo.getAte());
		
		GrupoMovimentoEstoque[] gruposMovimentoEstoque   = {
			      GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO,
			      GrupoMovimentoEstoque.FALTA_DE_COTA,
			      GrupoMovimentoEstoque.FALTA_EM_COTA,
			      GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE,
			      GrupoMovimentoEstoque.SOBRA_DE_COTA,
			      GrupoMovimentoEstoque.SOBRA_EM_COTA,
		          GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE,
		          GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE
			   };
		
		Map<Long, BigInteger> obterQtdMovimentoCotaPorTipoMovimento = movimentoEstoqueCotaRepository.obterQtdMovimentoCotaPorTipoMovimento(periodo, cota.getId(), gruposMovimentoEstoque);
		
		if((obterEstudoCota != null && obterEstudoCota.size() > 0) ||
				(obterQtdMovimentoCotaPorTipoMovimento != null && !obterQtdMovimentoCotaPorTipoMovimento.isEmpty())){
			return true;
		}
		
		return false;
	}

	private Endereco cloneEndereco(Endereco endereco)
			throws CloneNotSupportedException {
		Endereco novoEndereco = endereco.clone();
		enderecoRepository.detach(novoEndereco);
		novoEndereco.setId(null);
		novoEndereco.setPessoa(null);
		if (novoEndereco.getCep() != null) {
			novoEndereco.setCep(novoEndereco.getCep().replace("-", ""));
		}
		if (novoEndereco.getCodigoUf() == null
				&& novoEndereco.getCodigoCidadeIBGE() != null) {
			if (novoEndereco.getCodigoCidadeIBGE().toString().length() > 2) {
				novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE()
						.toString().substring(0, 2));
			} else {
				novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE()
						.toString());
			}
		}
		enderecoRepository.adicionar(novoEndereco);
		return novoEndereco;
	}

	@Override
	@Transactional
	public List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro) {
		
		List<NotaEnvio> listaNotaEnvio = null;

		if(TRAVA_GERACAO_NE != null && TRAVA_GERACAO_NE.get("neCotasSendoGeradas") != null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Notas de envio sendo geradas por outro usuário, tente novamente mais tarde.");
		}
		
		TRAVA_GERACAO_NE.put("neCotasSendoGeradas", true);
		
		try {
			
			List<Long> listaIdCotas = this.getIdsCotaIntervalo(filtro);
			
			List<Long> idCotasAusentes =
				this.cotaAusenteRepository.obterIdsCotasAusentesNoPeriodo(
					filtro.getIntervaloMovimento());
			
			if (idCotasAusentes != null) {
				
				listaIdCotas.removeAll(idCotasAusentes);
			}
	
			validarRoteirizacaoCota(filtro, listaIdCotas);
			
			listaNotaEnvio = this.gerar(listaIdCotas, filtro, null,null, null);
		} finally {
			TRAVA_GERACAO_NE.remove("neCotasSendoGeradas");
		}
		
		return listaNotaEnvio;
	}

	private void validarRoteirizacaoCota(FiltroConsultaNotaEnvioDTO filtro,List<Long> listaIdCotas) {
		
		List<String> cotasSemRoteirizacao = new ArrayList<String>();
		
		for (Long idCota : listaIdCotas) {
			
			Cota cota = cotaRepository.buscarPorId(idCota);

			if(cota.getBox() == null) {
				
				if(cotasSemRoteirizacao.size() == 0) {
					cotasSemRoteirizacao.add("Cota(s) com problemas de Roteirização:");
				}
				StringBuilder cotaSemRoteirizacao = new StringBuilder("Cota: "+ cota.getNumeroCota() +" / "+ cota.getPessoa().getNome());
				cotasSemRoteirizacao.add(cotaSemRoteirizacao.toString());
			}
		}
		
		if(cotasSemRoteirizacao.size() > 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, cotasSemRoteirizacao);
		}
	}
	
	private List<Long> getIdsCotaIntervalo(FiltroConsultaNotaEnvioDTO filtro){
		
		List<SituacaoCadastro> situacoesCadastro = 
				Arrays.asList(SituacaoCadastro.ATIVO,SituacaoCadastro.SUSPENSO);
		
		List<Long> listaIdCotas = 
				this.cotaRepository.obterIdCotasEntre(filtro.getIntervaloCota(), filtro.getIntervaloBox(),
													  situacoesCadastro, filtro.getIdRoteiro(), filtro.getIdRota(),
													  null, null, null, null);
		return listaIdCotas;
	}
}
