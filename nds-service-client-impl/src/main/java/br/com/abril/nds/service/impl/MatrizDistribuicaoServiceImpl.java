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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusEstudo;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MatrizDistribuicaoServiceImpl implements MatrizDistribuicaoService {

    @Autowired
    protected CalendarioService calendarioService;

    @Autowired
    protected DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private EstudoRepository estudoRepository;

    @Autowired
    private EstudoCotaRepository estudoCotaRepository;

    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final int MAX_DUPLICACOES_PERMITIDA = 3;

    @Override
    @Transactional(readOnly = true)
    public TotalizadorProdutoDistribuicaoVO obterMatrizDistribuicao(FiltroDistribuicaoDTO filtro) {

	this.validarFiltro(filtro);

	List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs = distribuicaoRepository.obterMatrizDistribuicao(filtro);

	boolean matrizFinalizada = isMatrizFinalizada(produtoDistribuicaoVOs);

	TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = getProdutoDistribuicaoVOTotalizado(produtoDistribuicaoVOs);

	totalizadorProdutoDistribuicaoVO.setMatrizFinalizada(matrizFinalizada);

	return totalizadorProdutoDistribuicaoVO;
    }

    private boolean isMatrizFinalizada(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

	if (produtoDistribuicaoVOs.isEmpty()) {

	    return false;
	}

	for (ProdutoDistribuicaoVO produtoDistribuicaoVO: produtoDistribuicaoVOs) {

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

	    throw new ValidacaoException(TipoMensagem.WARNING, "Não é permitido mais do que " + MAX_DUPLICACOES_PERMITIDA + " duplicações");
	}

	Long idLancamento = prodDistribVO.getIdLancamento().longValue();

	Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);

	Lancamento lancamentoCopy = cloneLancamento(lancamento);

	//ProdutoEdicao produtoEdicaoCopy = cloneProdutoEdicao(lancamentoCopy.getProdutoEdicao());

	//lancamentoCopy.setProdutoEdicao(produtoEdicaoCopy);
	idLancamento = lancamentoRepository.adicionar(lancamentoCopy);

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

    //    private ProdutoEdicao cloneProdutoEdicao(ProdutoEdicao produtoEdicao) {
    //
    //	ProdutoEdicao produtoEdicaoCopy = new ProdutoEdicao();
    //	BeanUtils.copyProperties(produtoEdicao, produtoEdicaoCopy);
    //	produtoEdicaoCopy.setId(null);
    //	produtoEdicaoCopy.setChamadaEncalhes(null);
    //	produtoEdicaoCopy.setHistoricoMovimentoRepartes(null);
    //	produtoEdicaoCopy.setDiferencas(null);
    //	produtoEdicaoCopy.setLancamentos(null);
    //	produtoEdicaoCopy.setMovimentoEstoques(null);
    //	Long prodEdicaoId = produtoEdicaoRepository.adicionar(produtoEdicaoCopy);
    //	produtoEdicaoCopy = produtoEdicaoRepository.buscarPorId(prodEdicaoId);
    //
    //	return produtoEdicaoCopy;	 
    //    }

    private void gravarHistoricoLancamento(Long idUsuario,  Lancamento lancamento) {

	HistoricoLancamento historicoLancamento = new HistoricoLancamento();
	historicoLancamento.setDataEdicao(new Date());
	historicoLancamento.setLancamento(lancamento);
	historicoLancamento.setTipoEdicao(TipoEdicao.INCLUSAO);
	historicoLancamento.setStatus(StatusLancamento.CONFIRMADO);
	Usuario user = usuarioRepository.buscarPorId(idUsuario);
	historicoLancamento.setResponsavel(user);
    }


    @Override
    @Transactional(readOnly = true)
    public ProdutoDistribuicaoVO obterProdutoDistribuicaoPorEstudo(BigInteger idEstudo) {

	if (idEstudo == null || idEstudo.intValue() == 0) {

	    throw new ValidacaoException(TipoMensagem.WARNING,"O código do estudo deve ser informado!");
	}

	return distribuicaoRepository.obterProdutoDistribuicaoPorEstudo(idEstudo);
    }

    private TotalizadorProdutoDistribuicaoVO getProdutoDistribuicaoVOTotalizado(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

	Integer totalEstudoGerado = 0;
	Integer totalEstudoLiberado = 0;

	for (ProdutoDistribuicaoVO prodDistVO:produtoDistribuicaoVOs) {
	    if (prodDistVO.getIdEstudo() != null) {
		if (prodDistVO.getLiberado().equals(StatusEstudo.LIBERADO.name())) {
		    totalEstudoLiberado++;
		}
		else {
		    totalEstudoGerado++;
		}
	    }
	}

	TotalizadorProdutoDistribuicaoVO totProdDistVO = new TotalizadorProdutoDistribuicaoVO();
	totProdDistVO.setListProdutoDistribuicao(produtoDistribuicaoVOs);
	totProdDistVO.setTotalEstudosGerados(totalEstudoGerado);
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

		mensagens.add("Os dados do filtro da tela devem ser informados!");
	    }

	    if (!mensagens.isEmpty()) {

		ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, mensagens);

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

	estudoRepository.liberarEstudo(idsEstudos, false);
    }

    @Override
    @Transactional
    public void excluirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	for (ProdutoDistribuicaoVO vo:produtosDistribuicao) {

	    BigInteger idLancamento = vo.getIdLancamento();

	    if (idLancamento != null && vo.getIdCopia() != null) {

		if (vo.getIdEstudo() != null && vo.getIdEstudo().intValue() > 0) {
		    removeEstudo(vo.getIdEstudo().longValue());
		}

		//excluirLinhaDuplicada(idLancamento.longValue());
	    }
	    else if (vo.getIdEstudo() != null && vo.getIdEstudo().intValue() > 0) {

		removeEstudo(vo.getIdEstudo().longValue());

	    } else {
		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existe estudo para o produto selecionado!"));
	    }
	}
    }

    private void removeEstudo(Long idEstudo) {

	Estudo estudo = estudoRepository.buscarPorId(idEstudo);
	if (!estudo.isLiberado()) {

	    estudoRepository.remover(estudo);

	} else {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Este estudo já foi liberado, não é permitido excluí-lo!"));
	}
    }

    //    private void excluirLinhaDuplicada(Long idLancamento) {
    //
    //	Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
    //
    //	//ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
    //
    //	lancamentoRepository.remover(lancamento);
    //    }

    @Override
    @Transactional
    public void finalizarMatrizDistribuicao(FiltroDistribuicaoDTO filtro, List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

	List<ProdutoDistribuicaoVO> listDistrib = produtoDistribuicaoVOs;

	Map <BigInteger, BigInteger> map = obterMapaEstudoRepartDistrib(produtoDistribuicaoVOs);

	List<String> mensagens = new ArrayList<String>();

	for (ProdutoDistribuicaoVO prodDistribVO:listDistrib) {

	    if(isItemValido(prodDistribVO, mensagens)) {

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

	    String msg = "Não existe estudo para o produto:"+produtoDistribuicaoVO.getCodigoProduto();

	    if (produtoDistribuicaoVO.getIdProdutoEdicao() != null) {

		msg += " edição:" + produtoDistribuicaoVO.getIdProdutoEdicao();
	    }

	    mensagens.add(msg); 
	    return false;
	}
	else if (!(produtoDistribuicaoVO.getLiberado() != null && (produtoDistribuicaoVO.getLiberado().equals("LIBERADO") || 
		Boolean.valueOf(produtoDistribuicaoVO.getLiberado())))) {

	    mensagens.add("Estudo "+produtoDistribuicaoVO.getIdEstudo()+" não está liberado.");
	    return false;
	}

	return true;
    }


    @Override
    @Transactional
    public void finalizarMatrizDistribuicaoTodosItens(FiltroDistribuicaoDTO filtro, List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

	TotalizadorProdutoDistribuicaoVO totProdDistribVO = obterMatrizDistribuicao(filtro);

	List<ProdutoDistribuicaoVO> listDistrib = totProdDistribVO.getListProdutoDistribuicao();

	Map <BigInteger, BigInteger> map = obterMapaEstudoRepartDistrib(produtoDistribuicaoVOs);

	List<String> mensagens = new ArrayList<String>();

	for (ProdutoDistribuicaoVO prodDistribVO:listDistrib) {

	    if(isItemValido(prodDistribVO, mensagens)) {

		validaFinalizacaoMatriz(prodDistribVO);

		if (!prodDistribVO.isItemFinalizado()) {

		    finalizaItemDistribuicao(prodDistribVO, map);
		}
	    }
	}

	if (!mensagens.isEmpty()) {

	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
	}
    }


    private void validaFinalizacaoMatriz(ProdutoDistribuicaoVO prodDistribVO) {

	if (obterQuantidadeDeLancamentosProdutoEdicaoDuplicados(prodDistribVO) > 1) {

	    throw new ValidacaoException(TipoMensagem.WARNING, "Não é permitido mais de uma edição por produto.");
	}
    }

    private Integer obterQuantidadeDeLancamentosProdutoEdicaoDuplicados(ProdutoDistribuicaoVO produtoDistribuicaoVO) {

	BigInteger count = lancamentoRepository.obterQtdLancamentoProdutoEdicaoCopiados(produtoDistribuicaoVO);

	return (count != null)?count.intValue():0;
    }

    @Override
    @Transactional
    public void reabrirMatrizDistribuicao(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

	for (ProdutoDistribuicaoVO prodDistribVO:produtoDistribuicaoVOs) {

	    reabrirItemDistribuicao(prodDistribVO.getIdLancamento().longValue());
	}
    }

    @Override
    @Transactional
    public void reabrirMatrizDistribuicaoTodosItens(FiltroDistribuicaoDTO filtro) {

	TotalizadorProdutoDistribuicaoVO totProdDistribVO = obterMatrizDistribuicao(filtro);

	if (!totProdDistribVO.isMatrizFinalizada()) {

	    throw new ValidacaoException(TipoMensagem.WARNING, "Matriz ainda não finalizada.");
	}

	List<ProdutoDistribuicaoVO> listDistrib = totProdDistribVO.getListProdutoDistribuicao();

	for (ProdutoDistribuicaoVO prodDistribVO:listDistrib) {

	    reabrirItemDistribuicao(prodDistribVO.getIdLancamento().longValue());
	}
    }

    private void finalizaItemDistribuicao(ProdutoDistribuicaoVO prodDistribVO, Map <BigInteger, BigInteger> map) {

	List<String> mensagens = new ArrayList<String>();

	if(!isItemValido(prodDistribVO, mensagens)) {

	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
	}

	Lancamento lanc = (Lancamento)distribuicaoRepository.buscarPorId(prodDistribVO.getIdLancamento().longValue());
	lanc.setDataFinMatDistrib(new Date());
	distribuicaoRepository.alterar(lanc);

	BigInteger idEstudo = prodDistribVO.getIdEstudo();

	if (map.containsKey(idEstudo)) {

	    Estudo estudo = estudoRepository.buscarPorId(idEstudo.longValue());
	    estudo.setDataAlteracao(new Date());
	    estudo.setReparteDistribuir(map.get(idEstudo));
	    estudoRepository.alterar(estudo);
	}
    }


    private Map<BigInteger, BigInteger> obterMapaEstudoRepartDistrib(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {

	Map<BigInteger, BigInteger> map = new HashMap<BigInteger, BigInteger>();

	if (produtoDistribuicaoVOs == null || produtoDistribuicaoVOs.isEmpty()) {

	    return map;
	}

	for (ProdutoDistribuicaoVO vo:produtoDistribuicaoVOs) {

	    if (vo.getIdEstudo() != null && vo.getIdEstudo().intValue() > 0) {

		map.put(vo.getIdEstudo(), vo.getRepDistrib());
	    }
	}

	return map;
    }

    private void reabrirItemDistribuicao(Long idLancamento) {

	Lancamento lanc = (Lancamento)distribuicaoRepository.buscarPorId(idLancamento);
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
	Estudo estudo = estudoRepository.obterEstudoECotasPorIdEstudo(vo.getIdEstudo());
	if ((estudo == null) || (estudo.getEstudoCotas() == null)) {
	    throw new ValidacaoException(TipoMensagem.WARNING, "Não foi possível efetuar a cópia. Estudo inexistente ou não há cotas que receberam reparte.");
	}

	estudo = criarCopiaDeEstudo(vo, estudo);
	return estudo.getId();
    }

    private Estudo obterCopiaDeEstudo(Estudo estudo, Lancamento lancamento) {

	Estudo estudoCopia = new Estudo();
	BeanUtils.copyProperties(estudo, estudoCopia, new String[] {"id", "lancamentoID", "lancamentos", "estudoCotas"});
	estudoCopia.setDataAlteracao(new Date());
	estudoCopia.setLiberado(false);
	estudoCopia.setEstudoCotas(new HashSet<EstudoCota>());
	estudoCopia.setProdutoEdicao(lancamento.getProdutoEdicao());
	estudoCopia.setLancamentoID(lancamento.getId());

	Long id = estudoRepository.adicionar(estudoCopia);
	estudoCopia = estudoRepository.buscarPorId(id);

	return estudoCopia;
    }

    private LinkedList<EstudoCota> copiarListaDeCotas(LinkedList<EstudoCota> lista, Estudo estudo, boolean isFixacao) {
	LinkedList<EstudoCota> retorno = new LinkedList<>();
	for (EstudoCota estudoCota : lista) {
	    if (estudoCota.getReparte() != null && estudoCota.getReparte().compareTo(BigInteger.ZERO) > 0) {
		EstudoCota cota = new EstudoCota();
		BeanUtils.copyProperties(estudoCota, cota, new String[] {"id", "estudo", "rateiosDiferenca", "movimentosEstoqueCota", "itemNotaEnvios"});
		cota.setEstudo(estudo);
		if (cota.getClassificacao() == null || cota.getClassificacao().isEmpty() || !isFixacao ||
			(!cota.getClassificacao().equals("FX") && !cota.getClassificacao().equals("MX") && !cota.getClassificacao().equals("MM"))) {
		    cota.setClassificacao("");
		}
		retorno.add(cota);
	    }
	}
	return retorno;
    }

    private Estudo criarCopiaDeEstudo(CopiaProporcionalDeDistribuicaoVO vo, Estudo estudo) {

	BigInteger totalFixacao = BigInteger.ZERO;
	BigInteger totalMix = BigInteger.ZERO;
	BigInteger totalReparte = BigInteger.ZERO;

	Lancamento lancamento = lancamentoRepository.buscarPorIdSemEstudo(vo.getIdLancamento());
	Estudo estudoCopia = obterCopiaDeEstudo(estudo, lancamento);
	estudoCopia.setQtdeReparte(vo.getReparteDistribuido());
	LinkedList<EstudoCota> cotas = new LinkedList<>(estudo.getEstudoCotas());

	if (cotas.isEmpty()) {
	    throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possivel efetuar a copia.");
	}

	// somar totais
	for (EstudoCota cota : cotas) {
	    if (cota.getReparte() != null) {
		if (cota.getClassificacao() != null) {
		    if (cota.getClassificacao().equals("FX")) {
			totalFixacao = totalFixacao.add(cota.getReparte());
		    } else if (cota.getClassificacao().equals("MX") || cota.getClassificacao().equals("MM")) {
			totalMix = totalMix.add(cota.getReparte());
		    }
		}
		totalReparte = totalReparte.add(cota.getReparte());
	    }
	}

	cotas = copiarListaDeCotas(cotas, estudoCopia, vo.isFixacao());

	BigInteger totalReparteFixado = totalFixacao.add(totalMix);
	BigInteger reparteDistribuir = vo.getReparteDistribuido();
	BigInteger pacotePadrao = vo.getPacotePadrao();

	// distribuicao para as cotas fixadas
	if (vo.isFixacao()) {
	    if (totalReparteFixado.compareTo(reparteDistribuir) > 0) {
		throw new ValidacaoException(TipoMensagem.WARNING, "Fixação é maior que o reparte");
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
	    for (EstudoCota cota : cotas) {
		if (!vo.isFixacao() || (!cota.getClassificacao().equals("FX") && !cota.getClassificacao().equals("MX") && !cota.getClassificacao().equals("MM"))) {
		    BigDecimal reparte = new BigDecimal(cota.getReparte()).multiply(indiceProporcional);
		    // arredondamento por pacote padrao
		    if (pacotePadrao != null && pacotePadrao.compareTo(BigInteger.ZERO) > 0) {
			reparte = reparte.divide(new BigDecimal(pacotePadrao), 0, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(pacotePadrao));
			reparte = reparte.setScale(0, BigDecimal.ROUND_HALF_UP);
		    } else {
			reparte = reparte.setScale(0, BigDecimal.ROUND_HALF_UP);
		    }
		    cota.setReparte(reparte.toBigInteger());
		    reparteDistribuir = reparteDistribuir.subtract(reparte.toBigInteger());
		}
	    }
	}

	// distribuicao de sobras caso exista
	Collections.sort(cotas, new Comparator<EstudoCota>() {
	    @Override
	    public int compare(EstudoCota ec1, EstudoCota ec2) {
		return (ec1.getReparte().compareTo(ec2.getReparte()));
	    }
	});

	BigInteger reparte = BigInteger.ONE;
	if (pacotePadrao != null && pacotePadrao.compareTo(BigInteger.ZERO) > 0) {
	    reparte = pacotePadrao;
	}
	if (reparteDistribuir.compareTo(BigInteger.ZERO) != 0) {
	    for (EstudoCota cota : cotas) {
		if (reparteDistribuir.compareTo(BigInteger.ZERO) > 0) {
		    cota.setReparte(cota.getReparte().add(reparte));
		    reparteDistribuir = reparteDistribuir.subtract(reparte);
		} else if (reparteDistribuir.compareTo(BigInteger.ZERO) < 0) {
		    cota.setReparte(cota.getReparte().subtract(reparte));
		    reparteDistribuir = reparteDistribuir.add(reparte);
		}
		if (reparteDistribuir.compareTo(BigInteger.ZERO) > 0 && reparteDistribuir.compareTo(reparte) < 0) {
		    if (cotas.size() > 0) {
			cotas.get(0).setReparte(cotas.get(0).getReparte().add(reparteDistribuir));
			reparteDistribuir = reparteDistribuir.subtract(reparteDistribuir);
		    }
		    break;
		} else if (reparteDistribuir.compareTo(BigInteger.ZERO) < 0 && reparteDistribuir.compareTo(reparte.multiply(BigInteger.valueOf(-1))) > 0) {
		    if (cotas.size() > 0) {
			cotas.get(0).setReparte(cotas.get(0).getReparte().subtract(reparteDistribuir));
			reparteDistribuir = reparteDistribuir.add(reparteDistribuir);
		    }
		    break;
		}
	    }
	}

	// salvando no banco
	for (EstudoCota cota : cotas) {
	    estudoCotaRepository.adicionar(cota);
	}
	estudoCopia.setEstudoCotas(new HashSet<EstudoCota>(cotas));
	estudoCotaRepository.inserirPrudutoBase(estudoCopia);
	return estudoCopia;
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo(BigInteger id) {

	ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo = this.distribuicaoRepository.obterMatrizDistribuicaoPorEstudo(id);
	return obterMatrizDistribuicaoPorEstudo;
    }
}
