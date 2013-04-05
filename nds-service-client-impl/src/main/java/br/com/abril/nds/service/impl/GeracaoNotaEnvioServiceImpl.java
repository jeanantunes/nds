package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.abril.nds.model.cadastro.desconto.Desconto;
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
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.util.Intervalo;

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

	@Transactional
	public List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro) {

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
		
		if("EMITIDAS".equals(filtro.getExibirNotasEnvio())) {
			return cotaRepository.obterDadosCotasComNotaEnvioEmitidasCount(filtro);
		} else if("AEMITIR".equals(filtro.getExibirNotasEnvio())) {
			return cotaRepository.obterDadosCotasComNotaEnvioAEmitirCount(filtro);
		} else {
			return cotaRepository.obterDadosCotasComNotaEnvioEmitidasEAEmitirCount(filtro);
		}
		
	}

	private List<ItemNotaEnvio> gerarItensNotaEnvio(
			List<EstudoCota> listaEstudoCota, Cota cota, List<MovimentoEstoqueCota> listaMovimentoEstoqueCota) {

		List<ItemNotaEnvio> listItemNotaEnvio = new ArrayList<ItemNotaEnvio>();

		gerarItensNEMovimento(listaMovimentoEstoqueCota, cota, listItemNotaEnvio);
		
		gerarItensNEEstudo(listaEstudoCota, cota, listItemNotaEnvio);
		
		sortItensByProdutoNome(listItemNotaEnvio);
		
		return listItemNotaEnvio;
	}

	/**
	 * Gera itens de nota de envio a partir do movimentos de estoque que não possuem estudo.
	 * 
	 * @param listaMovimentoEstoqueCota
	 * @param cota
	 * @param listItemNotaEnvio
	 */
	private void gerarItensNEMovimento(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, Cota cota,
			List<ItemNotaEnvio> listItemNotaEnvio) {
		
        if (listaMovimentoEstoqueCota == null || listaMovimentoEstoqueCota.isEmpty()){
			
			return;
		}
					
		for(MovimentoEstoqueCota mec : listaMovimentoEstoqueCota) {
			
			ItemNotaEnvio itemNotaEnvio = getItemNE(listItemNotaEnvio, mec.getProdutoEdicao());
			
			ProdutoEdicao produtoEdicao = mec.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			itemNotaEnvio.setProdutoEdicao(produtoEdicao);
			itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNome());
			
			BigDecimal valorDesconto = itemNotaEnvio.getDesconto();
			
			if(valorDesconto == null) {
			
				Desconto desconto = 
						descontoService.obterDescontoPorCotaProdutoEdicao(mec.getLancamento(), cota, produtoEdicao);
				
				valorDesconto = (desconto != null && desconto.getValor() != null) ? 
						desconto.getValor() : BigDecimal.ZERO;
			}
			
			itemNotaEnvio.setDesconto(valorDesconto);
			
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
	 */
	private void gerarItensNEEstudo(List<EstudoCota> listaEstudoCota,
			Cota cota, List<ItemNotaEnvio> listItemNotaEnvio) {
		
		if (listaEstudoCota == null || listaEstudoCota.isEmpty()){
			
			return;
		}
		
		for (EstudoCota estudoCota : listaEstudoCota) {

			if (estudoCota.getItemNotaEnvios()!=null && !estudoCota.getItemNotaEnvios().isEmpty()) {
				
				listItemNotaEnvio.addAll(estudoCota.getItemNotaEnvios());
				
				continue;
			}
			
			ProdutoEdicao produtoEdicao = estudoCota.getEstudo().getProdutoEdicao();

			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			BigInteger quantidadeResultante = BigInteger.ZERO;
			
			for(RateioDiferenca rateioDiferenca : estudoCota.getRateiosDiferenca()) {
				
				for(MovimentoEstoqueCota mec : estudoCota.getMovimentosEstoqueCota()) {
					
					if(mec.getEstudoCota().getId() == rateioDiferenca.getEstudoCota().getId()) {
						
						if(rateioDiferenca.getDiferenca().getTipoDiferenca().equals(TipoDiferenca.FALTA_DE)
								|| rateioDiferenca.getDiferenca().getTipoDiferenca().equals(TipoDiferenca.FALTA_EM)) {
							
							quantidadeResultante = quantidadeResultante.add(rateioDiferenca.getQtde().negate());
							
						} else {
							
							quantidadeResultante = quantidadeResultante.add(rateioDiferenca.getQtde());
							
						}
					}
				}
			}

			Date data = estudoCota.getEstudo().getLancamento().getDataLancamentoDistribuidor();
			
			Long idCota = cota.getId();
			
			BigInteger qtdEstorno = this.movimentoEstoqueCotaRepository.obterQtdMovimentoCotaPorTipoMovimento(data,idCota,GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
			
			BigInteger qtdRateio = this.movimentoEstoqueCotaRepository.obterQtdMovimentoCotaPorTipoMovimento(data,idCota,GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE);
			
			quantidadeResultante = quantidadeResultante.subtract((qtdEstorno==null)?BigInteger.ZERO: qtdEstorno);
			
			quantidadeResultante = quantidadeResultante.add( (qtdRateio== null)?BigInteger.ZERO: qtdRateio);
			
			
			Desconto percentualDesconto = this.descontoService
					.obterDescontoPorCotaProdutoEdicao(estudoCota.getEstudo()
							.getLancamento(), cota, produtoEdicao);

			BigInteger quantidade = quantidadeResultante.add(estudoCota.getQtdeEfetiva());

			ItemNotaEnvio itemNotaEnvio = criarNovoItemNotaEnvio(
					estudoCota,
					produtoEdicao,
					precoVenda,
					((percentualDesconto != null && percentualDesconto
							.getValor() != null) ? percentualDesconto
							.getValor() : BigDecimal.ZERO), 
					quantidade);
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
	
	private ItemNotaEnvio criarNovoItemNotaEnvio(EstudoCota estudoCota,
			ProdutoEdicao produtoEdicao, BigDecimal precoVenda,
			BigDecimal percentualDesconto, BigInteger quantidade) {

		ItemNotaEnvio itemNotaEnvio = new ItemNotaEnvio();

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
	public NotaEnvio visualizar(Integer numeroCota, Long idRota,
								String chaveAcesso, Integer codigoNaturezaOperacao,
								String descricaoNaturezaOperacao, Date dataEmissao,
								Intervalo<Date> periodo, List<Long> listaIdFornecedores) {

		PessoaJuridica pessoaEmitente = this.distribuidorRepository.juridica();

		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);

		Long idCota = cota.getId();
		
		PDV pdvPrincipal = this.pdvRepository.obterPDVPrincipal(cota.getId());

		if (idRota == null) {
			
			idRota = this.getIdRotaCota(pdvPrincipal, cota);
		}
		
		IdentificacaoDestinatario destinatarioAtualizado = this.carregaDestinatario(cota, idRota, pdvPrincipal);

		NotaEnvio notaEnvio = criarNotaEnvio(destinatarioAtualizado, chaveAcesso,
											 codigoNaturezaOperacao, descricaoNaturezaOperacao, dataEmissao,
											 pessoaEmitente);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaSemEstudoPor(idCota, periodo, listaIdFornecedores, 
																						  GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE);
		
		List<EstudoCota> listaEstudosCota = this.estudoCotaRepository
				.obterEstudosCotaParaNotaEnvio(Arrays.asList(idCota), periodo,
						listaIdFornecedores);

		List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(
				listaEstudosCota, cota, listaMovimentoEstoqueCota);

		if (listaItemNotaEnvio.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não é possível gerar Nota de Envio para a Cota "
							+ cota.getNumeroCota());
		}

		int sequencia = 0;

		for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {

			if (itemNotaEnvio.getItemNotaEnvioPK() == null) {
				
				itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio,++sequencia));
			}
		}

		notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);

		return notaEnvio;
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
				
				if (novasNotasEnvio.contains(notaEnvio)) {
					
					itensNotasEnvioExistentes.add(ine);
				}	
				else{

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
				
				itemNotaEnvioRepository.adicionar(itemNotaEnvio);
				
				if(itemNotaEnvio.getMovimentosProdutoSemEstudo() != null && !itemNotaEnvio.getMovimentosProdutoSemEstudo().isEmpty()) {
					
					for(MovimentoEstoqueCota mec : itemNotaEnvio.getMovimentosProdutoSemEstudo()) {
						
						mec.setItemNotaEnvio(itemNotaEnvio);
						
						this.movimentoEstoqueCotaRepository.merge(mec);
					}
				}
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
	 */
	private void getNotaEnvioCota(PessoaJuridica pessoaEmitente,
			                           Long idCota, 
				                       Long idRota, 
				                       List<NotaEnvio> notasEnvio, 
				                       Date dataEmissao, 
				                       Intervalo<Date> periodo,
	                                   List<Long> listaIdFornecedores, 
	                                   String chaveAcesso,
				                       Integer codigoNaturezaOperacao, 
				                       String descricaoNaturezaOperacao,
				                       List<EstudoCota> listaEstudosCota){

		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if (cota == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada!");
		}
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaSemEstudoPor(idCota, periodo, listaIdFornecedores, 
																						  GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE);
		
		List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(listaEstudosCota, cota, listaMovimentoEstoqueCota);

		if (listaItemNotaEnvio==null || listaItemNotaEnvio.isEmpty()) {

			return;
		}
		
		PDV pdvPrincipal = this.pdvRepository.obterPDVPrincipal(cota.getId());

		if (idRota == null) {
			
			idRota = this.getIdRotaCota(pdvPrincipal, cota);
		}
		
		IdentificacaoDestinatario destinatarioAtualizado = this.carregaDestinatario(cota, idRota, pdvPrincipal);
		
		this.atualizarNotasEnvio(notasEnvio, listaItemNotaEnvio, destinatarioAtualizado);

		NotaEnvio notaEnvio = null;
		
		if(listaItemNotaEnvio != null && listaItemNotaEnvio.size() > 0) {

			notaEnvio = criarNotaEnvio(destinatarioAtualizado,
					                   chaveAcesso,
					                   codigoNaturezaOperacao, descricaoNaturezaOperacao, dataEmissao,
					                   pessoaEmitente);
	
			notaEnvioRepository.adicionar(notaEnvio);
			
			this.atualizaMovimentosEstoqueItemNotaEnvio(notaEnvio, listaItemNotaEnvio);
	
			notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);
	
			notaEnvio = notaEnvioRepository.merge(notaEnvio);
		}
	}
	
	/**
	 * Separa as Notas de Envio por Cota
	 * @param estudosCotas
	 * @return Map<Long,List<EstudoCota>>
	 */
	private Map<Long,List<EstudoCota>> getMapEstudosCota(List<EstudoCota> estudosCotas){
		
		Map<Long,List<EstudoCota>> estudosCota = new HashMap<Long, List<EstudoCota>>();

        List<EstudoCota> estudos = new ArrayList<EstudoCota>();
    	
        for (EstudoCota estudo : estudosCotas){
    		
        	estudos = estudosCota.get(estudo.getCota().getId());
    		
        	estudos = estudos==null?new ArrayList<EstudoCota>():estudos;
    		
        	estudos.add(estudo);
			
        	estudosCota.put(estudo.getCota().getId(), estudos);
    	}

		return estudosCota;
	}
	
	/**
	 * Gera Notas de Envio para as Cotas
	 * @param idCotas
	 * @param idRota
	 * @param chaveAcesso
	 * @param codigoNaturezaOperacao
	 * @param descricaoNaturezaOperacao
	 * @param dataEmissao
	 * @param periodo
	 * @param listaIdFornecedores
	 * @return List<NotaEnvio>
	 */
	private List<NotaEnvio> gerar(List<Long> idCotas, Long idRota, String chaveAcesso,
								  Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao,
			                      Date dataEmissao, Intervalo<Date> periodo,
			                      List<Long> listaIdFornecedores) {
		
		PessoaJuridica pessoaEmitente = this.distribuidorRepository.juridica();
		
		List<EstudoCota> listaEstudosCotas = this.estudoCotaRepository
				.obterEstudosCotaParaNotaEnvio(idCotas, periodo,
						listaIdFornecedores);
		
		List<NotaEnvio> notasEnvio = new ArrayList<>();
		
		Map<Long, List<EstudoCota>> mapEstudosCota = this.getMapEstudosCota(listaEstudosCotas);
		
		for (Long idCota:idCotas){

			this.getNotaEnvioCota(pessoaEmitente,
                                  idCota,  
								  idRota, 
								  notasEnvio, 
								  dataEmissao, 
								  periodo, 
								  listaIdFornecedores, 
								  chaveAcesso, 
								  codigoNaturezaOperacao, 
								  descricaoNaturezaOperacao, 
								  mapEstudosCota.get(idCota));
		}
		
		return notasEnvio;
	}


	/**
	 * Cria nova nota de Envio
	 * @param destinatarioAtualizado
	 * @param chaveAcesso
	 * @param codigoNaturezaOperacao
	 * @param descricaoNaturezaOperacao
	 * @param dataEmissao
	 * @param pessoaEmitente
	 * @return NotaEnvio
	 * @throws ValidacaoException
	 */
	private NotaEnvio criarNotaEnvio(IdentificacaoDestinatario destinatarioAtualizado,
									 String chaveAcesso, Integer codigoNaturezaOperacao,
									 String descricaoNaturezaOperacao, Date dataEmissao,
									 PessoaJuridica pessoaEmitente) throws ValidacaoException {
		
		NotaEnvio notaEnvio = new NotaEnvio();

		notaEnvio.setEmitente(carregarEmitente(pessoaEmitente));
		notaEnvio.setDestinatario(destinatarioAtualizado);
		notaEnvio.setChaveAcesso(chaveAcesso);
		notaEnvio.setCodigoNaturezaOperacao(codigoNaturezaOperacao);
		notaEnvio.setDescricaoNaturezaOperacao(descricaoNaturezaOperacao);
		notaEnvio.setDataEmissao(dataEmissao);
		
		return notaEnvio;
	}

	/**
	 * @param distribuidor
	 * @return
	 * @throws ValidacaoException
	 */
	private IdentificacaoEmitente carregarEmitente(PessoaJuridica pessoaEmitente)
			throws ValidacaoException {

		IdentificacaoEmitente emitente = new IdentificacaoEmitente();

		// Corrigido devo ao fato da tabela pessoa gravar os documentos com
		// máscara, embora o campo documento da tabela nota_envio esperar apenas
		// 14 caracteres
		String documento = pessoaEmitente.getDocumento().replaceAll(
				"[-+.^:,/]", "");

		emitente.setDocumento(documento);
		emitente.setNome(pessoaEmitente.getNome());
		emitente.setPessoaEmitenteReferencia(pessoaEmitente);
		emitente.setInscricaoEstadual(pessoaEmitente.getInscricaoEstadual());

		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository
				.obterEnderecoPrincipal();

		if (enderecoDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do distribuidor não encontrada!");
		}

		try {
			emitente.setEndereco(cloneEndereco(enderecoDistribuidor
					.getEndereco()));
		} catch (Exception exception) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do distribuidor!");
		}

		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository
				.obterTelefonePrincipal();

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

	private IdentificacaoDestinatario carregaDestinatario(Cota cota, Long idRota, PDV pdvPrincipalCota) {
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

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Endereço do PDV principal da cota " + cota.getNumeroCota() + " não encontrado!");
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
	public List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro,
			List<Long> idCotasSuspensasAusentes) {

		List<NotaEnvio> listaNotaEnvio = new ArrayList<NotaEnvio>();
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(SituacaoCadastro.ATIVO);
		situacoesCadastro.add(SituacaoCadastro.SUSPENSO);

		List<Long> listaIdCotas = this.cotaRepository.obterIdCotasEntre(
				filtro.getIntervaloCota(), filtro.getIntervaloBox(),
				situacoesCadastro, filtro.getIdRoteiro(), filtro.getIdRota(),
				null, null, null, null);

		if (idCotasSuspensasAusentes != null) {
			listaIdCotas.addAll(idCotasSuspensasAusentes);
		}

		validarRoteirizacaoCota(filtro, listaIdCotas);
		
		listaNotaEnvio = this.gerar(listaIdCotas, filtro.getIdRota(), null,
				null, null, filtro.getDataEmissao(),
				filtro.getIntervaloMovimento(), filtro.getIdFornecedores());

		return listaNotaEnvio;
	}

	private void validarRoteirizacaoCota(FiltroConsultaNotaEnvioDTO filtro,
			List<Long> listaIdCotas) {
		List<String> cotasSemRoteirizacao = new ArrayList<String>();
		
		for (Long idCota : listaIdCotas) {
			
			Cota cota = cotaRepository.buscarPorId(idCota);

			Long idRota = filtro.getIdRota();

			if (idRota == null) {

				Long idRoteiro = null;

				if(cota.getBox() != null) {
					
					List<Roteiro> roteiros = cota.getBox().getRoteirizacao().getRoteiros();

					if(roteiros != null) {
						for (Roteiro r : roteiros) {
		
							if (!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {
		
								idRoteiro = r.getId();
							}
						}
					}

					try {
						idRota = (Long) cota.getBox().getRoteirizacao()
								.getRoteiro(idRoteiro).getRotas().get(0).getId();
					} catch (Exception e) {
						if(cotasSemRoteirizacao.size() == 0) {
							cotasSemRoteirizacao.add("Cota(s) com problemas de Roteirização:");
						}
						StringBuilder cotaSemRoteirizacao = new StringBuilder("Cota: "+ cota.getNumeroCota() +" / "+ cota.getPessoa().getNome());
						cotasSemRoteirizacao.add(cotaSemRoteirizacao.toString());
					}
					
				}
			}
			
		}
		
		if(cotasSemRoteirizacao.size() > 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, cotasSemRoteirizacao);
		}
	}

}