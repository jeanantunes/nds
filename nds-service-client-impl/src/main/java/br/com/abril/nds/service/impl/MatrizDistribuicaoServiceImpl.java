package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusEstudo;
import br.com.abril.nds.model.planejamento.TipoClassificacaoEstudoCota;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.util.StringUtil;
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
	
	@Override
	@Transactional(readOnly = true)
	public TotalizadorProdutoDistribuicaoVO obterMatrizDistribuicao(FiltroLancamentoDTO filtro) {
	
		this.validarFiltro(filtro);
		
		List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs = distribuicaoRepository.obterMatrizDistribuicao(filtro);
		
		if (isMatrizFinalizada(produtoDistribuicaoVOs)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Matriz Finalizada!");
		}
		
		return getProdutoDistribuicaoVOTotalizado(produtoDistribuicaoVOs);
	}
	
	private boolean isMatrizFinalizada(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs) {
		
		for (ProdutoDistribuicaoVO produtoDistribuicaoVO: produtoDistribuicaoVOs) {
			
			if (!produtoDistribuicaoVO.isItemFinalizado()) {
				
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	@Transactional
	public void duplicarLinhas(Long idLancamento) {
		
		 Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		 
		 Lancamento lancamentoCopy = new Lancamento();
		 BeanUtils.copyProperties(lancamento, lancamentoCopy);
		 lancamentoCopy.setId(null);
		 
		 ProdutoEdicao produtoEdicao = lancamentoCopy.getProdutoEdicao();
		 ProdutoEdicao produtoEdicaoCopy = new ProdutoEdicao();
		 BeanUtils.copyProperties(produtoEdicao, produtoEdicaoCopy);
		 produtoEdicaoCopy.setId(null);
		 
		 Long id = produtoEdicaoRepository.adicionar(produtoEdicaoCopy);
		 produtoEdicaoCopy = (ProdutoEdicao)produtoEdicaoRepository.buscarPorId(id);
		 
		 lancamentoCopy.setProdutoEdicao(produtoEdicaoCopy);
		 lancamentoRepository.adicionar(lancamentoCopy);
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
	private void validarFiltro(FiltroLancamentoDTO filtro) {
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Os dados do filtro devem ser informados!");
			
		} else {
		
			List<String> mensagens = new ArrayList<String>();
			
			if (filtro.getData() == null) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (filtro.getIdsFornecedores() == null
					|| filtro.getIdsFornecedores().isEmpty()) {
				
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
		}
		
		estudoRepository.liberarEstudo(idsEstudos, false);
	}
	
	@Override
	@Transactional
	public void excluirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao) {
		
		for (ProdutoDistribuicaoVO vo:produtosDistribuicao) {
			
			if (vo.getIdEstudo() != null) {
				Estudo estudo = estudoRepository.buscarPorId(vo.getIdEstudo().longValue());
				estudoRepository.remover(estudo);
			}
		}
	}
	
	@Override
	@Transactional
	public void finalizarMatrizDistribuicao(FiltroLancamentoDTO filtro) {
		
		TotalizadorProdutoDistribuicaoVO totProdDistribVO = obterMatrizDistribuicao(filtro);
		
		List<ProdutoDistribuicaoVO> listDistrib = totProdDistribVO.getListProdutoDistribuicao();
		
		for (ProdutoDistribuicaoVO prodDistribVO:listDistrib) {
			
//			if (prodDistribVO.getLiberado() == null || !prodDistribVO.getLiberado().equals(StatusEstudo.LIBERADO.name())) {
//				
//				throw new ValidacaoException(TipoMensagem.WARNING, "Os estudos devem estar todos liberados para a finalização da matriz.");
//			}
			
			if (!prodDistribVO.isItemFinalizado()) {
				
				finalizaItemDistribuicao(prodDistribVO.getIdLancamento().longValue());
			}
			
		}
	}
	
	@Override
	@Transactional
	public void reabrirMatrizDistribuicao(FiltroLancamentoDTO filtro) {
		
		TotalizadorProdutoDistribuicaoVO totProdDistribVO = obterMatrizDistribuicao(filtro);
		
		List<ProdutoDistribuicaoVO> listDistrib = totProdDistribVO.getListProdutoDistribuicao();
		
		if (!isMatrizFinalizada(listDistrib)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz ainda não finalizada.");
		}
		
		for (ProdutoDistribuicaoVO prodDistribVO:listDistrib) {
			
			if (prodDistribVO.isItemFinalizado()) {
				
				reabrirItemDistribuicao(prodDistribVO.getIdLancamento().longValue());
			}
			
		}
	}
	
	private void finalizaItemDistribuicao(Long idLancamento) {
		
		Lancamento lanc = (Lancamento)distribuicaoRepository.buscarPorId(idLancamento);
		lanc.setDataFinMatDistrib(new Date());
		distribuicaoRepository.alterar(lanc);
	}
	
	private void reabrirItemDistribuicao(Long idLancamento) {
		
		Lancamento lanc = (Lancamento)distribuicaoRepository.buscarPorId(idLancamento);
		lanc.setDataFinMatDistrib(null);
		distribuicaoRepository.alterar(lanc);
	}
	
	private void validarCopiaProporcionalDeDistribuicao(CopiaProporcionalDeDistribuicaoVO vo) {
		
		if (vo.getIdEstudo() == null || vo.getIdEstudo().intValue() <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Pesquise um estudo valido.");
		}
		
		if (vo.getReparteDistribuido() == null || vo.getReparteDistribuido().intValue() <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto sem valor de distribuição de reparte.");
		}
		
	}
	
	
	@Override
	@Transactional
	public Long confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO vo) {
		
		validarCopiaProporcionalDeDistribuicao(vo);
		
		Estudo estudo = estudoRepository.obterEstudoECotasPorIdEstudo(vo.getIdEstudo());
		
		Set<EstudoCota> set = estudo.getEstudoCotas();
		List<EstudoCota> cotas = obterListEstudoCotas(set);
		
		if (!cotas.isEmpty()) {
			estudo = criarCopiaDeEstudo(vo, estudo);
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorIdLancamento(vo.getIdLancamento());
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possivel efetuar a copia.");
		}
		
		estudo.setProdutoEdicao(produtoEdicao);
		estudoRepository.alterar(estudo);
		
		
		return estudo.getId();
	}
	
	private List<EstudoCota> obterListEstudoCotas(Set<EstudoCota> set) {
		
		List<EstudoCota> cotas = new ArrayList<EstudoCota>();
		
		if (set != null && !set.isEmpty()) {
			
			Iterator<EstudoCota> iterator = set.iterator();
			
			while (iterator.hasNext()) {
				
				EstudoCota estudoCota = iterator.next();
				
				if (estudoCota.getReparte() == null) {
					estudoCota.setReparte(BigInteger.ZERO);
				}
				
				if (StringUtil.isEmpty(estudoCota.getClassificacao())) {
					estudoCota.setClassificacao("");
				}
				
				cotas.add(estudoCota);
			}
			
		}
		
		return cotas;
	}
	
	private Map<String, BigInteger> obterMapClassifiqReparte(List<EstudoCota> cotas) {

		Map<String, BigInteger> mapClassifiqReparte = new HashMap<String, BigInteger>();

		BigInteger reparte = BigInteger.ZERO;

		String key = null;

		for (EstudoCota estCota : cotas) {

			key = estCota.getClassificacao();
			reparte = estCota.getReparte();

			if (mapClassifiqReparte.containsKey(key)) {
				
				reparte = reparte.add(mapClassifiqReparte.get(key));
			}

			mapClassifiqReparte.put(key, reparte);
		}
		
		return mapClassifiqReparte;
	}
	
	private Estudo obterCopiaDeEstudo(Estudo estudo) {
		
		Estudo estudoCopia = (Estudo)SerializationUtils.clone(estudo);
		estudoCopia.setId(null);
		estudoCopia.setDataAlteracao(new Date());
		estudoCopia.setEstudoCotas(new HashSet<EstudoCota>());
		
		Long id = estudoRepository.adicionar(estudoCopia);
		estudoCopia = estudoRepository.buscarPorId(id);
	
		return estudoCopia;
	}
	
	private Estudo criarCopiaDeEstudo(CopiaProporcionalDeDistribuicaoVO vo, Estudo estudo) {
		
		Estudo estudoCopia = obterCopiaDeEstudo(estudo);
		
		Set<EstudoCota> set = estudo.getEstudoCotas();
		List<EstudoCota> cotas = obterListEstudoCotas(set);
		
		if (cotas.isEmpty()) {
			return estudoCopia;
		}
		
		Map<String, BigInteger> mapReparte =  obterMapClassifiqReparte(cotas);
		
		BigInteger totalFixacao = BigInteger.ZERO;
		BigInteger repCalculado = BigInteger.ZERO;
		BigInteger repartDistrib = BigInteger.ZERO;
		BigInteger pactPadrao = BigInteger.ZERO;
		BigInteger repFinal = BigInteger.ZERO;
		BigInteger indiceRepProporcional = BigInteger.ZERO;
		
		EstudoCota cota = null;
		
		for (EstudoCota estudoCota:cotas) {
			
			cota = (EstudoCota)SerializationUtils.clone(estudoCota);
			
			repartDistrib = vo.getReparteDistribuido();
			pactPadrao = vo.getPacotePadrao();
			
			repCalculado = cota.getReparte();
			
			if (vo.isFixacao()) {
				
				totalFixacao = mapReparte.get("FX").add(mapReparte.get("MM"));
				
				if (totalFixacao.compareTo(repartDistrib) > 0) {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Fixação é maior que o reparte");
				}
				
				repartDistrib = repartDistrib.subtract(totalFixacao);
				
				repFinal = obterSomaReparteFinal(mapReparte, false, TipoClassificacaoEstudoCota.FX, TipoClassificacaoEstudoCota.MM);
				
				indiceRepProporcional =  repartDistrib.divide(repFinal);  //repartDistrib / repFinal;
				
				repCalculado = obterCalculoDistribMultiplos(repCalculado, indiceRepProporcional, pactPadrao);
				
			} else {
				
				repFinal = obterSomaReparteFinal(mapReparte);
				
				indiceRepProporcional = repartDistrib.divide(repFinal);
				
				repCalculado = obterCalculoDistribMultiplos(repCalculado, indiceRepProporcional, pactPadrao);
			}
			
			cota.setReparte(repCalculado);
			
			cota.setId(null);
			cota.setEstudo(estudoCopia);
			estudoCotaRepository.adicionar(cota);
		}
		
		BigInteger totalSoma = obterSomaReparteFinal(mapReparte, false, TipoClassificacaoEstudoCota.FX, TipoClassificacaoEstudoCota.MM);
		
		if (repartDistrib.compareTo(totalSoma) > 0) { 
			
			efetuarDistribuicaoProporcional(cotas, repartDistrib.intValue(), 1);	
		}
		else {
			
			efetuarDistribuicaoProporcional(cotas, repartDistrib.intValue(), -1);
		}
		
		return estudoCopia;
	}
	
	
	private void efetuarDistribuicaoProporcional(List<EstudoCota> cotas, Integer valorPrincipal, Integer valorRetirado) {
	
		Collections.sort(cotas, new Comparator<EstudoCota>() {

			@Override
			public int compare(EstudoCota ec1, EstudoCota ec2) {
				
				return (ec1.getReparte().compareTo(ec2.getReparte()));
			}
			
		});
		
		EstudoCota estudoCota = null;
		Integer reparte = 0;
		
		int i = cotas.size() -1;
		
		while (valorPrincipal > 0 && i >= 0) {
			
			estudoCota = cotas.get(i);
			
			if (!estudoCota.getClassificacao().equals(TipoClassificacaoEstudoCota.FX.name()) &&
					!estudoCota.getClassificacao().equals(TipoClassificacaoEstudoCota.MM.name())) {
				
				reparte = estudoCota.getReparte().intValue() + valorRetirado;
				valorPrincipal -= (valorRetirado < 0)? (valorRetirado * -1):valorRetirado;
				estudoCota.setReparte(new BigInteger(reparte.toString()));
			}
			
			i--;
		}
		
	}
	
	private BigInteger obterCalculoDistribMultiplos(BigInteger repCalculado, BigInteger indiceRepProporcional, BigInteger pactPadrao) {
		
		BigInteger porcent = BigInteger.ZERO;
		
		if ((pactPadrao != null && pactPadrao.compareTo(BigInteger.ZERO) > 0)) {
			
			porcent = repCalculado.multiply(indiceRepProporcional);
			porcent = porcent.divide(pactPadrao); 
			porcent = porcent.multiply(pactPadrao);
		}
		else {
			
			porcent = repCalculado.multiply(indiceRepProporcional);
		}
		
		return porcent;
	}
	
	
	private BigInteger obterSomaReparteFinal(Map<String, BigInteger> mapReparte, boolean comparaIgual, TipoClassificacaoEstudoCota... tipoClassificacao) {
		
		BigInteger repFinal = BigInteger.ZERO;
		
		Iterator<String> classifiqIterator = mapReparte.keySet().iterator();
		
		while (classifiqIterator.hasNext()) {
			String key = classifiqIterator.next();
			
			if(mapReparte.containsKey(key)) {
				
				if (tipoClassificacao == null || tipoClassificacao.length == 0) {
					
					repFinal = repFinal.add(mapReparte.get(key));
				}
				
				else {
					
					for (TipoClassificacaoEstudoCota tp:tipoClassificacao) {
						
						if(comparaIgual) {
							
							if (key.equals(tp.name())) {
								
								repFinal = repFinal.add(mapReparte.get(key));
							}
						} 
						else {
							
							if (!key.equals(tp.name())) {
								
								repFinal = repFinal.add(mapReparte.get(key));
							}
						}
					}
				}
			}
		}
		
		return repFinal;
	}
	
	@SuppressWarnings(value = { "all" })
	private BigInteger obterSomaReparteFinal(Map<String, BigInteger> mapReparte) {
		
		return obterSomaReparteFinal(mapReparte, false, null);
	}

	
	
	
}
