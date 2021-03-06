package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.InformacoesReparteComplementarEstudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusEstudo;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoGeracaoEstudo;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.InformacoesReparteEstudoComplementarRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MatrizDistribuicaoServiceImpl implements MatrizDistribuicaoService {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(MatrizDistribuicaoServiceImpl.class);
	  
	
	@Autowired
	protected CalendarioService calendarioService;

	@Autowired
	protected DistribuicaoRepository distribuicaoRepository;

	@Autowired
	private EstudoRepository estudoRepository;
	
	
	@Autowired
	private EstudoService estudoService;

	@Autowired
	private EstudoCotaGeradoRepository estudoCotaGeradoRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;

    @Autowired
    private EstudoGeradoRepository estudoGeradoRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
    private UsuarioService usuarioService;
	
	@Autowired
	private AnaliseParcialService analiseParcialService;
	
	@Autowired
	private EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService;
	
	@Autowired
	private InformacoesReparteEstudoComplementarRepository infoEstudoComplementarRepository;
	
	private static final int MAX_DUPLICACOES_PERMITIDA = 3;

	@Override
	@Transactional(readOnly = true)
	public TotalizadorProdutoDistribuicaoVO obterMatrizDistribuicao(FiltroDistribuicaoDTO filtro) {

		this.validarFiltro(filtro);

		List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs = distribuicaoRepository.obterMatrizDistribuicao(filtro);

		boolean matrizFinalizada = isMatrizFinalizada(produtoDistribuicaoVOs);

		TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = getProdutoDistribuicaoVOTotalizado(produtoDistribuicaoVOs);

		totalizadorProdutoDistribuicaoVO.setMatrizFinalizada(matrizFinalizada);
		
//		for (ProdutoDistribuicaoVO produtoDistribuicaoVO : totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao()) {
//			produtoDistribuicaoVO.setTotalEstudosLiberados(totalizadorProdutoDistribuicaoVO.getTotalEstudosLiberados());
//			produtoDistribuicaoVO.setTotalSemEstudo(produtoDistribuicaoVO.getTotalSemEstudo());
//		}

		return totalizadorProdutoDistribuicaoVO;
	}

	private boolean isMatrizFinalizada(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

		if (produtoDistribuicaoVOs.isEmpty()) {

			return false;
		}

		for (ProdutoDistribuicaoVO produtoDistribuicaoVO : produtoDistribuicaoVOs) {

			if (!produtoDistribuicaoVO.isItemFinalizado()) {

				return false;
			}
		}

		return true;
	}

	@Override
	@Transactional
	public void duplicarLinhas(ProdutoDistribuicaoVO prodDistribVO) {

		if (obterQuantidadeDeLancamentosProdutoEdicaoDuplicados(prodDistribVO) > MAX_DUPLICACOES_PERMITIDA) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Não é permitido mais do que " + MAX_DUPLICACOES_PERMITIDA
							+ " duplicações");
		}

		Long idLancamento = prodDistribVO.getIdLancamento().longValue();

		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);

		Lancamento lancamentoCopy = cloneLancamento(lancamento);
		lancamentoRepository.adicionar(lancamentoCopy);

		gravarHistoricoLancamento(prodDistribVO.getIdUsuario().longValue(), lancamentoCopy);
	}

	private Lancamento cloneLancamento(Lancamento lancamento) {

		Lancamento lancamentoCopy = new Lancamento();
		BeanUtils.copyProperties(lancamento, lancamentoCopy);
		lancamentoCopy.setId(null);
		lancamentoCopy.setChamadaEncalhe(null);
		lancamentoCopy.setHistoricos(null);
		lancamentoCopy.setMovimentoEstoqueCotas(null);
		lancamentoCopy.setRecebimentos(null);

		return lancamentoCopy;
	}

	// private ProdutoEdicao cloneProdutoEdicao(ProdutoEdicao produtoEdicao) {
	//
	// ProdutoEdicao produtoEdicaoCopy = new ProdutoEdicao();
	// BeanUtils.copyProperties(produtoEdicao, produtoEdicaoCopy);
	// produtoEdicaoCopy.setId(null);
	// produtoEdicaoCopy.setChamadaEncalhes(null);
	// produtoEdicaoCopy.setHistoricoMovimentoRepartes(null);
	// produtoEdicaoCopy.setDiferencas(null);
	// produtoEdicaoCopy.setLancamentos(null);
	// produtoEdicaoCopy.setMovimentoEstoques(null);
	// Long prodEdicaoId = produtoEdicaoRepository.adicionar(produtoEdicaoCopy);
	// produtoEdicaoCopy = produtoEdicaoRepository.buscarPorId(prodEdicaoId);
	//
	// return produtoEdicaoCopy;
	// }

	private void gravarHistoricoLancamento(Long idUsuario, Lancamento lancamento) {

		HistoricoLancamento historicoLancamento = new HistoricoLancamento();
		historicoLancamento.setDataEdicao(new Date());
		historicoLancamento.setLancamento(lancamento);
		historicoLancamento.setTipoEdicao(TipoEdicao.INCLUSAO);
		historicoLancamento.setStatusNovo(StatusLancamento.CONFIRMADO); // Confirmar!!
																		// Alteração
																		// feita
																		// pela
																		// F1
		Usuario user = usuarioRepository.buscarPorId(idUsuario);
		historicoLancamento.setResponsavel(user);
	}

    private TotalizadorProdutoDistribuicaoVO getProdutoDistribuicaoVOTotalizado(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

		Integer totalSemEstudo = 0;
		Integer totalEstudoLiberado = 0;

		for (ProdutoDistribuicaoVO prodDistVO : produtoDistribuicaoVOs) {
			if (prodDistVO.getIdEstudo() != null) {
				if (prodDistVO.getLiberado().equals(StatusEstudo.LIBERADO.name())) {
					
				    totalEstudoLiberado++;
				}
			} else {
			    
			    totalSemEstudo++;
			}
		}

		TotalizadorProdutoDistribuicaoVO totProdDistVO = new TotalizadorProdutoDistribuicaoVO();
		totProdDistVO.setListProdutoDistribuicao(produtoDistribuicaoVOs);
		totProdDistVO.setTotalSemEstudo(totalSemEstudo);
		totProdDistVO.setTotalEstudosLiberados(totalEstudoLiberado);

		return totProdDistVO;
	}

	/**
	 * Valida o filtro informado.
	 */
	private void validarFiltro(FiltroDistribuicaoDTO filtro) {

		if (filtro == null) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Os dados do filtro devem ser informados!");

		} else {

			List<String> mensagens = new ArrayList<String>();

			if (filtro.getData() == null) {

				mensagens
						.add("Os dados do filtro da tela devem ser informados!");
			}

			if (!mensagens.isEmpty()) {

				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING,
						mensagens);

				throw new ValidacaoException(validacaoVO);
			}
		}
	}

	@Override
	@Transactional
	public void reabrirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		List<Long> idsEstudos = new ArrayList<Long>();
	
		for (ProdutoDistribuicaoVO vo:produtosDistribuicao) {
		    if (vo.getIdEstudo() != null) {
			idsEstudos.add(vo.getIdEstudo().longValue());
		    }
	
		    if (vo.getIdLancamento() != null && vo.isItemFinalizado()) {
	
			reabrirItemDistribuicao(vo.getIdLancamento().longValue());
		    }
		}
	
		if (idsEstudos == null || idsEstudos.isEmpty()) {
	
		    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existe estudo para o(s) produto(s) selecionado!"));
		}
	
		estudoGeradoRepository.liberarEstudo(idsEstudos, false);

		estudoCotaRepository.removerEstudosCotaPorEstudos(idsEstudos);
		
		lancamentoRepository.desvincularEstudos(idsEstudos);
		
		estudoRepository.removerEstudos(idsEstudos);
		
		//estudoCotaGeradoRepository.removerEstudosCotaPorEstudos(idsEstudos);
    }

	@Override
	@Transactional
	public void excluirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		for (ProdutoDistribuicaoVO vo : produtosDistribuicao) {

			BigInteger idLancamento = vo.getIdLancamento();

			if (idLancamento != null && vo.getIdCopia() != null) {

				if (vo.getIdEstudo() != null && vo.getIdEstudo().intValue() > 0) {
					removeEstudo(vo.getIdEstudo().longValue());
				}

				// excluirLinhaDuplicada(idLancamento.longValue());
			} else if (vo.getIdEstudo() != null
					&& vo.getIdEstudo().intValue() > 0) {

				removeEstudo(vo.getIdEstudo().longValue());

			} else {
				throw new ValidacaoException(new ValidacaoVO(
						TipoMensagem.WARNING,
						"Não existe estudo para o produto selecionado!"));
			}
			
			InformacoesReparteComplementarEstudo informacoes = this.infoEstudoComplementarRepository.buscarInformacoesIdEstudo(vo.getIdEstudo().longValue());
			
			if(informacoes != null){
				this.infoEstudoComplementarRepository.remover(informacoes);
			}
			
		}
	}

    @Override
    @Transactional
	public void removeEstudo(Long idEstudo) {
        if (idEstudo > 0) {
            EstudoGerado estudo = estudoGeradoRepository.buscarPorId(idEstudo);
            if (estudo != null && !estudo.isLiberado()) {
                estudoGeradoRepository.remover(estudo);
            } else {
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
                        "Este estudo já foi liberado, não é permitido excluí-lo!"));
            }
        }
    }

	@Override
	@Transactional
	public void finalizarMatrizDistribuicao(FiltroDistribuicaoDTO filtro, List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

		List<ProdutoDistribuicaoVO> listDistrib = produtoDistribuicaoVOs;

		Map<BigInteger, BigInteger> map = obterMapaEstudoRepartDistrib(produtoDistribuicaoVOs);

		List<String> mensagens = new ArrayList<String>();

		for (ProdutoDistribuicaoVO prodDistribVO : listDistrib) {

			if (isItemValido(prodDistribVO, mensagens)) {

				if (!prodDistribVO.isItemFinalizado()) {

					finalizaItemDistribuicao(prodDistribVO, map);
				}
			}
		}

		if (!mensagens.isEmpty()) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}

	private boolean isItemValido(ProdutoDistribuicaoVO produtoDistribuicaoVO, List<String> mensagens) {

		if (produtoDistribuicaoVO.getIdEstudo() == null) {

			String msg = "Não existe estudo para o produto:"
					+ produtoDistribuicaoVO.getCodigoProduto();

			if (produtoDistribuicaoVO.getIdProdutoEdicao() != null) {

				msg += " edição:" + produtoDistribuicaoVO.getIdProdutoEdicao();
			}

			mensagens.add(msg);
			return false;
		} else if (!(produtoDistribuicaoVO.getLiberado() != null && (produtoDistribuicaoVO.getLiberado().equals("LIBERADO") || Boolean.valueOf(produtoDistribuicaoVO.getLiberado())))) {

			mensagens.add("Estudo " + produtoDistribuicaoVO.getIdEstudo()
					+ " não está liberado.");
			return false;
		}

		return true;
	}

	@Override
	@Transactional
	public void finalizarMatrizDistribuicaoTodosItens(FiltroDistribuicaoDTO filtro, List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

		TotalizadorProdutoDistribuicaoVO totProdDistribVO = obterMatrizDistribuicao(filtro);

		List<ProdutoDistribuicaoVO> listDistrib = totProdDistribVO.getListProdutoDistribuicao();

		Map<BigInteger, BigInteger> map = obterMapaEstudoRepartDistrib(produtoDistribuicaoVOs);

		List<String> mensagens = new ArrayList<String>();

		for (ProdutoDistribuicaoVO prodDistribVO : listDistrib) {

			if (isItemValido(prodDistribVO, mensagens)) {

				validaFinalizacaoMatriz(prodDistribVO);

				if (!prodDistribVO.isItemFinalizado()) {

					finalizaItemDistribuicao(prodDistribVO, map);
				}
			}
		}

		if (!mensagens.isEmpty()) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					mensagens));
		}
	}

	private void validaFinalizacaoMatriz(ProdutoDistribuicaoVO prodDistribVO) {

		if (obterQuantidadeDeLancamentosProdutoEdicaoDuplicados(prodDistribVO) > 1) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Não é permitido mais de uma edição por produto.");
		}
	}

	private Integer obterQuantidadeDeLancamentosProdutoEdicaoDuplicados(ProdutoDistribuicaoVO produtoDistribuicaoVO) {

		BigInteger count = lancamentoRepository.obterQtdLancamentoProdutoEdicaoCopiados(produtoDistribuicaoVO);

		return (count != null) ? count.intValue() : 0;
	}

	@Override
	@Transactional
	public void reabrirMatrizDistribuicao(
			List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

		for (ProdutoDistribuicaoVO prodDistribVO : produtoDistribuicaoVOs) {

			reabrirItemDistribuicao(prodDistribVO.getIdLancamento().longValue());
		}
	}

	@Override
	@Transactional
	public void reabrirMatrizDistribuicaoTodosItens(FiltroDistribuicaoDTO filtro) {

		TotalizadorProdutoDistribuicaoVO totProdDistribVO = obterMatrizDistribuicao(filtro);

		if (!totProdDistribVO.isMatrizFinalizada()) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Matriz ainda não finalizada.");
		}

		List<ProdutoDistribuicaoVO> listDistrib = totProdDistribVO.getListProdutoDistribuicao();

		for (ProdutoDistribuicaoVO prodDistribVO : listDistrib) {

			reabrirItemDistribuicao(prodDistribVO.getIdLancamento().longValue());
		}
	}

	private void finalizaItemDistribuicao(ProdutoDistribuicaoVO prodDistribVO, Map<BigInteger, BigInteger> map) {

		List<String> mensagens = new ArrayList<String>();

		if (!isItemValido(prodDistribVO, mensagens)) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					mensagens));
		}

		Lancamento lanc = (Lancamento) distribuicaoRepository.buscarPorId(prodDistribVO.getIdLancamento().longValue());
		lanc.setDataFinMatDistrib(new Date());
		distribuicaoRepository.alterar(lanc);

		BigInteger idEstudo = prodDistribVO.getIdEstudo();

		if (map.containsKey(idEstudo)) {

			EstudoGerado estudo = estudoGeradoRepository.buscarPorId(idEstudo.longValue());
			estudo.setDataAlteracao(new Date());
			estudo.setReparteDistribuir(map.get(idEstudo));
			estudoGeradoRepository.alterar(estudo);
		}

	}

	private Map<BigInteger, BigInteger> obterMapaEstudoRepartDistrib(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

		Map<BigInteger, BigInteger> map = new HashMap<BigInteger, BigInteger>();

		if (produtoDistribuicaoVOs == null || produtoDistribuicaoVOs.isEmpty()) {

			return map;
		}

		for (ProdutoDistribuicaoVO vo : produtoDistribuicaoVOs) {

			if (vo.getIdEstudo() != null && vo.getIdEstudo().intValue() > 0) {

				map.put(vo.getIdEstudo(), vo.getRepDistrib());
			}
		}

		return map;
	}

	private void reabrirItemDistribuicao(Long idLancamento) {

		Lancamento lanc = (Lancamento) distribuicaoRepository.buscarPorId(idLancamento);
		lanc.setDataFinMatDistrib(null);
		distribuicaoRepository.alterar(lanc);
	}

	@Override
	@Transactional
	public Long confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO vo) {

		if (vo.getIdEstudo() == null || vo.getIdEstudo().intValue() <= 0) {
		    throw new ValidacaoException(TipoMensagem.WARNING, "Pesquise um estudo valido.");
		}
		
		if (vo.getReparteDistribuido() == null || vo.getReparteDistribuido().intValue() <= 0) {
		    throw new ValidacaoException(TipoMensagem.WARNING, "Produto sem valor de distribuição de reparte.");
		}
		
		EstudoGerado estudo = estudoGeradoRepository.obterEstudoECotasPorIdEstudo(vo.getIdEstudo());
		
		if ((estudo == null) || (estudo.getEstudoCotas() == null)) {
		    throw new ValidacaoException(TipoMensagem.WARNING, "Não foi possível efetuar a cópia. Estudo inexistente ou não há cotas que receberam reparte.");
		}
	
		estudo = criarCopiaDeEstudo(vo, estudo);
		return estudo.getId();
    }

    private EstudoGerado obterCopiaDeEstudo(EstudoGerado estudo, Lancamento lancamento, BigInteger pacotePadrao) {
        
        EstudoGerado estudoCopia = new EstudoGerado();
        
        BeanUtils.copyProperties(estudo, estudoCopia, new String[] { "id", "lancamentoID", "lancamentos",
                "estudoCotas", "dataLancamento", "dataAlteracao"});
        
        estudoCopia.setDataAlteracao(new Date());
        estudoCopia.setLiberado(false);
        estudoCopia.setEstudoCotas(new HashSet<EstudoCotaGerado>());
        estudoCopia.setProdutoEdicao(lancamento.getProdutoEdicao());
        estudoCopia.setLancamentoID(lancamento.getId());
        estudoCopia.setIdEstudoOrigemCopia(estudo.getId());
        estudoCopia.setDataLancamento(lancamento.getDataLancamentoPrevista());
        estudoCopia.setReparteDistribuir(lancamento.getReparte());
        estudoCopia.setUsuario(this.usuarioService.getUsuarioLogado());
        estudoCopia.setDataCadastro(new Date());
        estudoCopia.setTipoGeracaoEstudo(TipoGeracaoEstudo.COPIA_PROPORCIONAL);
        
        Integer pctePadrao = lancamento.getProdutoEdicao().getPacotePadrao();
        
        if(pacotePadrao == null){
        	estudoCopia.setPacotePadrao(new BigInteger(pctePadrao.toString()));
        	estudoCopia.setDistribuicaoPorMultiplos(0);
        }
        
        Long id = this.estudoGeradoRepository.adicionar(estudoCopia);
        
        estudoCopia = estudoGeradoRepository.buscarPorId(id);
        
        return estudoCopia;
    }

    private LinkedList<EstudoCotaGerado> copiarListaDeCotas(LinkedList<EstudoCotaGerado> lista, EstudoGerado estudo, boolean isFixacao) {
		LinkedList<EstudoCotaGerado> retorno = new LinkedList<>();
	
		List<Long> idCotasQueNaoRecebemFornecedor = cotaRepository.buscarCotasQueNaoRecebeFornecedorDoProduto(estudo.getProdutoEdicao().getId());
		
		for (EstudoCotaGerado estudoCota : lista) {
			
			if(idCotasQueNaoRecebemFornecedor.contains(estudoCota.getCota().getId())){
				estudoCota.setReparte(null);
				estudoCota.setClassificacao(ClassificacaoCota.CotaNaoRecebeDesseFornecedor.getTexto());
			}
			
		    if ( SituacaoCadastro.INATIVO.equals(estudoCota.getCota().getSituacaoCadastro())) {
				LOGGER.warn("COTA INATIVA . NAO SERA INCLUIDO NO ESTUDO="+estudoCota.getCota().getNumeroCota());
		    	continue; // nao pegar cotas inativas
		     } 
			
			EstudoCotaGerado cota = new EstudoCotaGerado();
		    BeanUtils.copyProperties(estudoCota, cota, new String[] {"id", "estudo", "classificacao", "rateiosDiferenca", "movimentosEstoqueCota", "itemNotaEnvios"});
		    cota.setEstudo(estudo);
		    cota.setClassificacao("");
			    
		    if (cota.getReparte() == null) {
		    	cota.setReparte(BigInteger.ZERO);
		    }
		    
		    cota.setReparteInicial(cota.getReparte());
		    
		    if(estudoCota.getClassificacao().equals("MX") || estudoCota.getClassificacao().equals("FX")){
		    	cota.setClassificacao(estudoCota.getClassificacao());
		    } 
		    	
		    
		    retorno.add(cota);
		}
		
	return retorno;
	
    }

    private EstudoGerado criarCopiaDeEstudo(CopiaProporcionalDeDistribuicaoVO vo, EstudoGerado estudo) {

		BigInteger totalFixacao = BigInteger.ZERO;
		BigInteger totalMix = BigInteger.ZERO;
		BigInteger totalReparte = BigInteger.ZERO;
		
		List<Long> idCotasEstudo = new ArrayList<>();
		
		boolean estudosComMesmosICD = false;
	
		Lancamento lancamento = lancamentoRepository.buscarPorIdSemEstudo(vo.getIdLancamento());
		EstudoGerado estudoCopia = obterCopiaDeEstudo(estudo, lancamento, vo.getPacotePadrao());
		estudoCopia.setQtdeReparte(vo.getReparteDistribuido());
		
		LinkedList<EstudoCotaGerado> cotasSelecionadas = new LinkedList<>(estudo.getEstudoCotas());
		ProdutoEdicao edicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vo.getCodigoProduto(), vo.getNumeroEdicao().longValue());
		Map<Long, CotaEstudo> mapCotas = carregarInformacoesCotaEstudo(edicao);
		
		if(edicao.getProduto() != null && edicao.getProduto().getCodigoICD() != null && estudo.getProdutoEdicao() != null
				&& estudo.getProdutoEdicao().getProduto() != null && estudo.getProdutoEdicao().getProduto().getCodigoICD() != null){
			if(edicao.getProduto().getCodigoICD().equalsIgnoreCase(estudo.getProdutoEdicao().getProduto().getCodigoICD())){
				estudosComMesmosICD = true;
			}
		}
	
		cotasSelecionadas = copiarListaDeCotas(cotasSelecionadas, estudoCopia, vo.isFixacao());
	
		// validacoes de mix e classificacoes e segmentos nao recebidos
		LinkedList<EstudoCotaGerado> cotas = new LinkedList<>();
		for (EstudoCotaGerado cota : cotasSelecionadas) {
		    	CotaEstudo cotaEstudo = mapCotas.get(cota.getCota().getId());
		    	
		    	idCotasEstudo.add(cota.getCota().getId());
			    
		    	if (cotaEstudo != null) {
					if (cotaEstudo.getStatus() != null && cotaEstudo.getStatus().equals("SUSPENSO")) {
					    cota.setClassificacao(ClassificacaoCota.BancaSuspensa.getCodigo());
					    cota.setReparte(null);
					    continue;
					}
					if (cotaEstudo.isCotaNaoRecebeClassificacao()) {
					    cota.setClassificacao(ClassificacaoCota.BancaSemClassificacaoDaPublicacao.getCodigo());
					    cota.setReparte(null);
					    continue;
					}
					if (cotaEstudo.isCotaNaoRecebeSegmento() && !cotaEstudo.isCotaExcecaoSegmento()) {
					    cota.setClassificacao(ClassificacaoCota.CotaNaoRecebeEsseSegmento.getCodigo());
					    cota.setReparte(null);
					    continue;
					} else if (cotaEstudo.isCotaExcecaoSegmento()) {
					    cota.setClassificacao(ClassificacaoCota.CotaExcecaoSegmento.getCodigo());
					}
					if (cotaEstudo.getTipoDistribuicao() != null && 
							cotaEstudo.getTipoDistribuicao().equals(TipoDistribuicaoCota.ALTERNATIVO.name()) && 
							!cotaEstudo.isMix()) {
					    cota.setClassificacao(ClassificacaoCota.BancaMixSemDeterminadaPublicacao.getCodigo());
					    cota.setReparte(null);
					    continue;
					}
		            if (cotaEstudo.getClassificacao().equals(ClassificacaoCota.CotaNaoRecebeDesseFornecedor)) {
		                cota.setClassificacao(ClassificacaoCota.CotaNaoRecebeDesseFornecedor.getCodigo());
		                cota.setReparte(null);
		                continue;
		            }
		            cotas.add(cota);
			    }
		}
		
		// separando as cotas que passaram na validacao acima das cotas que por algum motivo nao entraram no estudo
		for (EstudoCotaGerado cota : cotas) {
		    cotasSelecionadas.remove(cota);
		}
	
		if (cotas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possivel efetuar a copia.");
		}

		// somar totais de reparte fixado e reparte minimo da cota mix
		for (EstudoCotaGerado cota : cotas) {
			
			if (cota.getReparte() != null) {
				totalReparte = totalReparte.add(cota.getReparte());
			}
			if (vo.isFixacao()) {
				CotaEstudo cotaEstudo = mapCotas.get(cota.getCota().getId());
				if (cotaEstudo != null && cotaEstudo.getClassificacao() != null) {
					if (cotaEstudo.getClassificacao().equals(ClassificacaoCota.ReparteFixado)) {
						
						cota.setReparte(cotaEstudo.getReparteFixado());
						cota.setClassificacao(ClassificacaoCota.ReparteFixado.getCodigo());
						totalFixacao = totalFixacao.add(cotaEstudo.getReparteFixado());
						
					} else if (cotaEstudo.getClassificacao().equals(
							ClassificacaoCota.CotaMix)) {
						totalMix = totalMix.add(cotaEstudo.getIntervaloMinimo());
					}
				}
			}
		}

		BigInteger totalReparteFixado = totalFixacao.add(totalMix);
		BigInteger reparteDistribuir = vo.getReparteDistribuido();
		BigInteger pacotePadrao = vo.getPacotePadrao();

		// distribuicao para as cotas fixadas
		if (vo.isFixacao()) {
			if (totalReparteFixado.compareTo(reparteDistribuir) > 0) {
				throw new ValidacaoException(TipoMensagem.WARNING,"Fixação é maior que o reparte");
			} else {
				reparteDistribuir = reparteDistribuir.subtract(totalReparteFixado);
			}
		}

		BigDecimal totalReparteNaoFixado = new BigDecimal(totalReparte.subtract(totalReparteFixado));
		BigDecimal indiceProporcional = BigDecimal.ZERO;
		
		if (totalReparteNaoFixado.compareTo(BigDecimal.ZERO) > 0) {
			indiceProporcional = new BigDecimal(reparteDistribuir).divide(totalReparteNaoFixado, 3, BigDecimal.ROUND_HALF_UP);
		}
		// distribuicao para as outras cotas
		if (reparteDistribuir.compareTo(BigInteger.ZERO) > 0) {
			for (EstudoCotaGerado cota : cotas) {
				if (!vo.isFixacao() || !(cota.getClassificacao().equals("FX") || cota.getClassificacao().equals("MX"))) {
					BigDecimal reparte = new BigDecimal(cota.getReparte())
							.multiply(indiceProporcional);
					// arredondamento por pacote padrao
					if (pacotePadrao != null && pacotePadrao.compareTo(BigInteger.ZERO) > 0) {
						reparte = reparte.divide(new BigDecimal(pacotePadrao),
								0, BigDecimal.ROUND_HALF_UP).multiply(
								new BigDecimal(pacotePadrao));
						
						reparte = reparte.setScale(0, BigDecimal.ROUND_HALF_UP);
					} else {
						reparte = reparte.setScale(0, BigDecimal.ROUND_HALF_UP);
					}
					
					cota.setReparte(reparte.toBigInteger());
					reparteDistribuir = reparteDistribuir.subtract(reparte.toBigInteger());
					
				}
			}
		}

		// verificacao dos repartes minimo e maximo para cotas mix
		for (EstudoCotaGerado cota : cotas) {
			
			BigInteger intervaloMinimo = BigInteger.ZERO;
			BigInteger intervaloMaximo = BigInteger.ZERO;
			
			CotaEstudo cotaEstudo = mapCotas.get(cota.getCota().getId());
			if (cotaEstudo != null && cotaEstudo.getClassificacao() != null) {
				if (cotaEstudo.getClassificacao().equals(ClassificacaoCota.CotaMix)) {

					cota.setClassificacao(ClassificacaoCota.CotaMix.getCodigo());
					
					if(estudosComMesmosICD){
						intervaloMinimo = cota.getReparteMinimo() != null ? cota.getReparteMinimo() : BigInteger.ZERO;
						intervaloMaximo = cota.getReparteMaximo() != null ? cota.getReparteMaximo() : BigInteger.ZERO;
					}else{ 
						intervaloMinimo = cotaEstudo.getIntervaloMinimo() != null ? cotaEstudo.getIntervaloMinimo() : BigInteger.ZERO;
						intervaloMaximo = cotaEstudo.getIntervaloMaximo() != null ? cotaEstudo.getIntervaloMaximo() : BigInteger.ZERO;
					}
					
					if (cota.getReparte() == null) {
						cota.setReparte(BigInteger.ZERO);
					}
					
					// multiplos
					if (pacotePadrao != null && pacotePadrao.compareTo(BigInteger.ZERO) > 0) {
						
						if (pacotePadrao.compareTo(intervaloMaximo) > 0) {
							
							BigInteger variacao = intervaloMaximo.subtract(cota.getReparte());

							cota.setReparte(intervaloMaximo);
							cota.setClassificacao(ClassificacaoCota.CotaMix.getCodigo());

							reparteDistribuir = reparteDistribuir.subtract(variacao);
						
						} else if (pacotePadrao.compareTo(intervaloMinimo) < 0) {
							
							BigInteger variacao = intervaloMinimo.subtract(cota.getReparte());

							cota.setReparte(intervaloMinimo);
							cota.setClassificacao(ClassificacaoCota.CotaMix.getCodigo());

							reparteDistribuir = reparteDistribuir.subtract(variacao);
						}
					
					} else { // nao multiplos
						
						if (cota.getReparte().compareTo(intervaloMinimo) < 0) {
							BigInteger variacao = intervaloMinimo.subtract(cota.getReparte());

							cota.setReparte(intervaloMinimo);
							cota.setClassificacao(ClassificacaoCota.CotaMix.getCodigo());

							reparteDistribuir = reparteDistribuir.subtract(variacao);
						
						} else if (cota.getReparte().compareTo(intervaloMaximo) > 0) {
							
							BigInteger variacao = intervaloMaximo.subtract(cota.getReparte());

							cota.setReparte(intervaloMaximo);
							cota.setClassificacao(ClassificacaoCota.CotaMix.getCodigo());

							reparteDistribuir = reparteDistribuir.add(variacao);
						}
					}
				}
			}
		}
		
		for (EstudoCotaGerado cotaSelecionada : cotasSelecionadas) {
			if(cotaSelecionada.getReparte() != null){
				cotas.add(cotaSelecionada);
			}
		}
		
		cotasSelecionadas.removeAll(cotas);
		
		BigInteger soma = BigInteger.ZERO;
		
		for (EstudoCotaGerado cota : cotas) {
			soma = soma.add(cota.getReparte());
			
			if (!vo.isFixacao() && cota.getClassificacao().equals("FX")) {
				cota.setClassificacao(ClassificacaoCota.BancaSemHistorico.getCodigo());
			}
		}
		
		reparteDistribuir = vo.getReparteDistribuido().subtract(soma);

		// distribuicao de sobras caso exista
		Collections.sort(cotas, new Comparator<EstudoCotaGerado>() {
			@Override
			public int compare(EstudoCotaGerado ec1, EstudoCotaGerado ec2) {
				return (ec2.getReparte().compareTo(ec1.getReparte()));
			}
		});

		BigInteger reparte = BigInteger.ONE;
		if (pacotePadrao != null && pacotePadrao.compareTo(BigInteger.ZERO) > 0) {
			reparte = pacotePadrao;
		}
		
		int countValorReparte = 0;
		BigInteger reparteAtual = BigInteger.ZERO;
		BigInteger reparteAnterior = BigInteger.ZERO;
		
		while ((reparteDistribuir.compareTo(BigInteger.ZERO) > 0 && reparteDistribuir
				.compareTo(reparte) >= 0)
				|| (reparteDistribuir.compareTo(BigInteger.ZERO) < 0 && reparteDistribuir
						.compareTo(reparte.negate()) <= 0)) {
			
			reparteAnterior = reparteDistribuir;
			
			for (EstudoCotaGerado cota : cotas) {
				if (!cota.getClassificacao().equals("FX")
						&& !cota.getClassificacao().equals("MX")) {
					if (reparteDistribuir.compareTo(BigInteger.ZERO) >= 0) {
						cota.setReparte(cota.getReparte().add(reparte));
						reparteDistribuir = reparteDistribuir.subtract(reparte);
					} else if (reparteDistribuir.compareTo(BigInteger.ZERO) <= 0
							&& cota.getReparte().compareTo(BigInteger.ZERO) > 0) {
						if ( cota.getReparte().intValue() >= reparte.intValue() ) {  // evitar que cota fique com reparte negativo
							cota.setReparte(cota.getReparte().subtract(reparte));
							reparteDistribuir = reparteDistribuir.add(reparte);
						}
					} else {
						break;
					}
				}

				if ((reparteDistribuir.compareTo(BigInteger.ZERO) > 0 && reparteDistribuir
						.compareTo(reparte) < 0)
						|| (reparteDistribuir.compareTo(BigInteger.ZERO) < 0 && reparteDistribuir
								.compareTo(reparte.negate()) > 0)
						|| reparteDistribuir.compareTo(BigInteger.ZERO) == 0) {
					break;
				}
			}
			
			reparteAtual = reparteDistribuir;
			
			if(reparteAnterior.compareTo(reparteAtual) == 0){
				++countValorReparte;
			}
			
			if(countValorReparte >= 3){
				break;
			}
			
		}

		if(cotasSelecionadas.size() > 0){
			cotas.addAll(cotasSelecionadas);
		}
		
		// tratar cotas ativas fora do estudo
		
		List<Cota> cotasAtivasForaDoEstudo = cotaRepository.obterCotasAtivasForaDoEstudo(idCotasEstudo);
		
		if(cotasAtivasForaDoEstudo != null && !cotasAtivasForaDoEstudo.isEmpty()){
			for (Cota cota : cotasAtivasForaDoEstudo) {
				EstudoCotaGerado estudoCota = new EstudoCotaGerado();
				
				estudoCota.setCota(cota);
				estudoCota.setClassificacao(ClassificacaoCota.SemClassificacao.getCodigo());
				estudoCota.setEstudo(estudoCopia);
				estudoCota.setReparteInicial(BigInteger.ZERO);
				estudoCota.setVendaMedia(BigDecimal.ZERO);
				estudoCota.setReparteMinimo(BigInteger.ZERO);
				estudoCota.setCotaNova(false);
				estudoCota.setMix(0);
				
				if(cota.getPdvs() != null){
					estudoCota.setQuantidadePDVS(cota.getPdvs().size());
				}else{
					estudoCota.setQuantidadePDVS(1);
				}
				
				cotas.add(estudoCota);
			}
		}
		
		
		// ~salvando no banco~ Preparando pra salvar no banco
		for (EstudoCotaGerado cota : cotas) {
			if (cota.getReparte() == null) {
				cota.setQtdePrevista(null);
				cota.setQtdeEfetiva(null);
				
				if((cota.getClassificacao() == null) || (cota.getClassificacao().equalsIgnoreCase(""))){
					cota.setClassificacao(ClassificacaoCota.BancaSemHistorico.getCodigo());
				}
				
			} else if ((cota.getReparte().compareTo(BigInteger.ZERO) == 0) && (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado.getCodigo()))) {
				cota.setQtdePrevista(null);
				cota.setQtdeEfetiva(null);
				cota.setReparte(null);
				
				if((cota.getClassificacao() == null) || (cota.getClassificacao().equalsIgnoreCase(""))){
					cota.setClassificacao(ClassificacaoCota.BancaSemHistorico.getCodigo());
				}
				
			} else {
				
				cota.setQtdeEfetiva(cota.getReparte());
				cota.setQtdePrevista(cota.getReparte());
				cota.setReparteInicial(cota.getReparte());
			}
			
		}
		
		estudoCopia.setEstudoCotas(new HashSet<EstudoCotaGerado>(cotas));
		
		List<EdicaoBaseEstudoDTO> edicoesBaseEstudo = new ArrayList<>();
		
		EdicaoBaseEstudoDTO edicaoBaseEstudoOrigem = estudoProdutoEdicaoBaseService.obterEdicoesBaseEstudoOrigemCopiaEstudo(vo.getIdEstudo());
		
		if(edicaoBaseEstudoOrigem != null){
			edicoesBaseEstudo.add(edicaoBaseEstudoOrigem);
		}

		List<EdicaoBaseEstudoDTO> edicaoBaseEstudoDTOs = estudoProdutoEdicaoBaseService.obterEdicoesBase(vo.getIdEstudo());
		
		if(edicaoBaseEstudoDTOs != null && !edicaoBaseEstudoDTOs.isEmpty()){
			edicoesBaseEstudo.addAll(edicaoBaseEstudoDTOs);	
		}
		
		if(edicoesBaseEstudo.size() > 6){
			int i = 1;
			for (EdicaoBaseEstudoDTO edicaoBaseEstudoDTO : edicoesBaseEstudo) {
				if(i <= 6 ){ // tratamento paliativo, add no máximo 6 edicoes bases
					estudoCotaGeradoRepository.inserirProdutoBase(estudoCopia.getId(), edicaoBaseEstudoDTO.getIdProdutoEdicao(), edicaoBaseEstudoDTO.getPeso().longValue(), 
							edicaoBaseEstudoDTO.isParcial(), edicaoBaseEstudoDTO.isEdicaoAberta(), edicaoBaseEstudoDTO.getPeriodoParcial());
				}
				++i;
			}
		}else{
			for (EdicaoBaseEstudoDTO edicaoBaseEstudoDTO : edicoesBaseEstudo) {
					estudoCotaGeradoRepository.inserirProdutoBase(estudoCopia.getId(), edicaoBaseEstudoDTO.getIdProdutoEdicao(), edicaoBaseEstudoDTO.getPeso().longValue(), 
							edicaoBaseEstudoDTO.isParcial(), edicaoBaseEstudoDTO.isEdicaoAberta(), edicaoBaseEstudoDTO.getPeriodoParcial());
			}
		}
		
		this.atualizarPercentualAbrangencia(estudoCopia.getId());
		
		ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoService.obterResumoEstudo(estudoCopia.getId(), null, null);
	        
	      if(resumo.getSaldo() != null){
	        	estudoCopia.setSobra(resumo.getSaldo().toBigInteger());
	        } else {
	        	estudoCopia.setSobra(BigInteger.ZERO);
	        }

      return estudoCopia;
	}

	private Map<Long, CotaEstudo> carregarInformacoesCotaEstudo(ProdutoEdicao produtoEdicao) {
		
		List<CotaEstudo> cotasEstudo = cotaRepository.getInformacoesCotaEstudo(produtoEdicao);
		Map<Long, CotaEstudo> mapCotas = new HashMap<>();
		
		for (CotaEstudo cotaEstudo : cotasEstudo) {
			if (cotaEstudo.getReparteFixado() != null && cotaEstudo.getReparteFixado().compareTo(BigInteger.ZERO) >= 0) {
				cotaEstudo.setClassificacao(ClassificacaoCota.ReparteFixado);
			}
			if (cotaEstudo.isMix()) {
				cotaEstudo.setClassificacao(ClassificacaoCota.CotaMix);
			}
			mapCotas.put(cotaEstudo.getId(), cotaEstudo);
		}
		return mapCotas;
	}

	@Override
	@Transactional(readOnly = true)
	public ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo(BigInteger id) {

		ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo = this.distribuicaoRepository
				.obterMatrizDistribuicaoPorEstudo(id);
		return obterMatrizDistribuicaoPorEstudo;
	}
	
	@Override
    @Transactional
    public void atualizarPercentualAbrangencia(Long estudoId) {

	    EstudoGerado estudoGerado = this.estudoGeradoRepository.buscarPorId(estudoId);
	    
	    estudoGerado.setAbrangencia(this.analiseParcialService.calcularPercentualAbrangencia(estudoId));
	    
	    this.estudoGeradoRepository.alterar(estudoGerado);
    }
	
}
