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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.TipoClassificacaoEstudoCota;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CopiaProporcionalDeDistribuicaoService;
import br.com.abril.nds.util.StringUtil;

@Service
public class CopiaProporcionalDeDistribuicaoServiceImpl implements CopiaProporcionalDeDistribuicaoService {
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private DistribuicaoRepository distribuicaoRepository;
	
	
	@Override
	@Transactional(readOnly = true)
	public ProdutoDistribuicaoVO obterProdutoDistribuicaoPorEstudo(BigInteger idEstudo) {
	
		if (idEstudo == null || idEstudo.intValue() == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING,"O código do estudo deve ser informado!");
		}
		
		return distribuicaoRepository.obterProdutoDistribuicaoPorEstudo(idEstudo);
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