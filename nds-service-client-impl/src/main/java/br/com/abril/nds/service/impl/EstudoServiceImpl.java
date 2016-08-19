package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.EstudoGeradoPreAnaliseDTO;
import br.com.abril.nds.model.planejamento.EstudoPDV;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusEstudo;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoPDVRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.DateUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 */
@Service
public class EstudoServiceImpl implements EstudoService {
    public static final Logger LOGGER = LoggerFactory.getLogger(EstudoServiceImpl.class);
	
	@Autowired
	private EstudoGeradoRepository estudoGeradoRepository;
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private EstudoCotaGeradoRepository estudoCotaGeradoRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private LancamentoService lancamentoService;
    
    @Autowired
    private AnaliseParcialService analiseParcialService;
    
    @Autowired
    private HistogramaPosEstudoFaixaReparteService histogramaPosEstudoFaixaReparteService;
    
    @Autowired
    private EstudoPDVRepository estudoPDVRepository;
    
    @Autowired
    private MixCotaProdutoRepository mixCotaProdRepository;
    
    @Autowired
    private FixacaoReparteRepository fixacaoReparteRepository;

	@Transactional(readOnly = true)
	@Override
	public EstudoGerado obterEstudo(Long id) {
		return this.estudoGeradoRepository.buscarPorId(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public EstudoGerado obterEstudoSql(Long id) {
		return this.estudoGeradoRepository.obterEstudoSql(id);
	}
	@Transactional(readOnly = true)
	@Override
	public EstudoGeradoPreAnaliseDTO obterEstudoPreAnalise(Long id) {
		return this.estudoGeradoRepository.obterEstudoPreAnalise(id);
	}


	@Override
	@Transactional
	public void gravarEstudo(EstudoGerado estudo) {
	    
	    for (EstudoCotaGerado estudoCota : estudo.getEstudoCotas()) {
			estudoCota.setEstudo(estudo);
	    }
	    
	    if(estudo.getReparteMinimo() == null){
	    	estudo.setReparteMinimo(BigInteger.ZERO);
	    }
	    
	    estudoGeradoRepository.adicionar(estudo);
	}

	/**
	 * Verifica se existem edicões bases para estudo
	 * 
	 * @param estudoId
	 * @return boolean
	 */
	private boolean isEdicoesBaseEstudo(Long estudoId){
		
        List<Long> listaIdEdicaoBaseEstudo = histogramaPosEstudoFaixaReparteService.obterIdEdicoesBase(Long.valueOf(estudoId));
		
		boolean isEdicoesBase = (listaIdEdicaoBaseEstudo !=null && !listaIdEdicaoBaseEstudo.isEmpty());
		
		return isEdicoesBase;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId, Long codigoProduto, Long numeroEdicao) {
		
		boolean isEdicoesBase = this.isEdicoesBaseEstudo(estudoId);
		
		ResumoEstudoHistogramaPosAnaliseDTO analiseDTO = estudoGeradoRepository.obterResumoEstudo(estudoId, isEdicoesBase);
		
		if(codigoProduto!= null && numeroEdicao!= null){
			final Integer reparte = lancamentoService.obterRepartePromocionalEdicao(codigoProduto, numeroEdicao);
			analiseDTO.setQtdRepartePromocional(BigIntegerUtil.valueOfInteger(reparte));
		}
	
	    EstudoGerado estudo = estudoGeradoRepository.buscarPorId(estudoId);
	    
	    if (estudo == null || !estudo.isLiberado() ) // se  estudo nao foi liberado, recalcular
		    analiseDTO.setAbrangenciaEstudo(analiseParcialService.calcularPercentualAbrangencia(estudoId));  
		
		return analiseDTO;
	}

	@Override
	@Transactional
	public void excluirEstudo(long id) {
		this.estudoGeradoRepository.removerPorId(id);
	}

    @Transactional(readOnly = true)
    public EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO) {

	return this.estudoGeradoRepository.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudoVO);
    }

    @Transactional
    public List<Long> salvarDivisao(EstudoGerado estudoOriginal, List<EstudoGerado> listEstudo, DivisaoEstudoDTO divisaoEstudo) {

		List<Long> listIdEstudoAdicionado = null;

		if (listEstudo != null && !listEstudo.isEmpty()) {

			// 2 estudo para ser salvo
			EstudoGerado segundoEstudo = listEstudo.get(1);

			// verificando existencia de lancamentos na data de lancamento
			// informada em tela para produto_edicao do estudo original
			Lancamento lancamentoSegundoEstudo = this.lancamentoRepository
					.buscarPorDataLancamentoProdutoEdicao(segundoEstudo
							.getDataLancamento(), segundoEstudo
							.getProdutoEdicao().getId());

            if (lancamentoSegundoEstudo == null) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados lançamentos para data " + DateUtil.formatarDataPTBR(segundoEstudo.getDataLancamento()));
			}
			
			listIdEstudoAdicionado = new ArrayList<Long>();
            Integer quantidadeReparte = (divisaoEstudo.getQuantidadeReparte() == null) ? 0 : divisaoEstudo.getQuantidadeReparte();

			List<EstudoCotaGerado> listEstudoCota = this.estudoCotaGeradoRepository.obterEstudoCotaPorEstudo(estudoOriginal);

			int iEstudo = 0;
            HashMap<Long, BigInteger> diffEstudosMap = new HashMap<Long, BigInteger>();

			for (EstudoGerado estudo : listEstudo) {

				estudo.setDataLancamento(null);
				Set<EstudoCotaGerado> setEstudoCota = new HashSet<EstudoCotaGerado>();
				
				for (EstudoCotaGerado ec : listEstudoCota) {
					
					EstudoCotaGerado estudoCota = (EstudoCotaGerado) SerializationUtils.clone(ec);
					estudoCota.setId(null);
					estudoCota.setEstudo(estudo);

					//nao dividir reparte menor que o informado em tela
                    if (estudoCota.getReparte() != null && estudoCota.getReparte().compareTo(new BigInteger(quantidadeReparte.toString())) > 0) {
						//Primeiro estudo gerado
                        if (iEstudo == 0) {
							BigDecimal toDivide = null;
							BigDecimal i = new BigDecimal(estudoCota.getReparte());
							toDivide = new BigDecimal(divisaoEstudo.getPercentualDivisaoPrimeiroEstudo()).divide(new BigDecimal("100"));
                            BigInteger bigInteger = i.multiply(toDivide).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
							estudoCota.setReparte(bigInteger);
							
							diffEstudosMap.put(ec.getId(), ec.getReparte().subtract(bigInteger));
							
							/*BigDecimal bd  = new BigDecimal(3);
							BigDecimal toDivide  = new BigDecimal(50).divide(new BigDecimal("100"));
							System.out.println(bd.multiply(toDivide).setScale(0,BigDecimal.ROUND_HALF_UP));*/
							
                        } else {
							//Segundo estudo gerado
//							toDivide = new BigDecimal(divisaoEstudo.getPercentualDivisaoSegundoEstudo()).divide(new BigDecimal("100"));
							estudoCota.setReparte(diffEstudosMap.get(ec.getId()));
						}
					
						setEstudoCota.add(estudoCota);
						
                    } else {
						
                        if (iEstudo == 1) {
							estudoCota.setReparte(BigInteger.ZERO);
						}
						setEstudoCota.add(estudoCota);
					}
				}

				BigInteger somarReparteParaEstudo = BigInteger.ZERO;
				for (EstudoCotaGerado estudoCota : setEstudoCota) {
                    BigInteger r = estudoCota.getReparte() == null ? BigInteger.ZERO : estudoCota.getReparte();
                    somarReparteParaEstudo = somarReparteParaEstudo.add(r);
					estudoCota.setQtdeEfetiva(estudoCota.getReparte());
					estudoCota.setQtdePrevista(estudoCota.getReparte());
				}
				estudo.setQtdeReparte(somarReparteParaEstudo);
				estudo.setEstudoCotas(setEstudoCota);

				// Estudo 1 possui o mesmo lancamentoID do estudo original
				if (iEstudo == 0) {
					estudo.setLancamentoID(estudoOriginal.getLancamentoID());
                } else {
					estudo.setLancamentoID(lancamentoSegundoEstudo.getId());
				}
				
				
				this.estudoGeradoRepository.adicionar(estudo);
				listIdEstudoAdicionado.add(estudo.getId());

				iEstudo++;

			}

		}
		
		return listIdEstudoAdicionado;
	}

	@Override
	@Transactional
	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo) {
		this.estudoGeradoRepository.setIdLancamentoNoEstudo(idLancamento, idEstudo);
	}

	@Transactional
	public EstudoGerado criarEstudo(ProdutoEdicao produtoEdicao,BigInteger quantidadeReparte,
	        Date dataLancamento, Long lancamentoId){
		
		Date dataOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		EstudoGerado estudo = new EstudoGerado();
		
		estudo.setDataCadastro(dataOperacao);
		estudo.setDataLancamento(dataLancamento);
		estudo.setProdutoEdicao(produtoEdicao);
		estudo.setQtdeReparte(quantidadeReparte);
		estudo.setReparteDistribuir(quantidadeReparte);
		estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
		estudo.setLancamentoID(lancamentoId);
		estudo.setLiberado(false);
		
		return estudoGeradoRepository.merge(estudo);
	}
	
	@Transactional
    public Estudo atualizarEstudo(Long estudoId, BigInteger reparteAtualizar) {
        
	    Estudo estudo = this.estudoRepository.buscarPorId(estudoId);
	    
	    estudo.setQtdeReparte(
            BigIntegerUtil.soma(estudo.getQtdeReparte(), reparteAtualizar));
	    
	    estudo.setReparteDistribuir(
            BigIntegerUtil.soma(estudo.getReparteDistribuir(), reparteAtualizar));
	    
        return this.estudoRepository.merge(estudo);
    }
	
	@Transactional
	public Estudo liberar(Long idEstudoGerado) {

		EstudoGerado estudoGerado = this.estudoGeradoRepository.obterParaAtualizar(idEstudoGerado);
		
		Lancamento lancamento = 
			this.lancamentoRepository.obterParaAtualizar(estudoGerado.getLancamentoID());
		
		if (lancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Não há lançamento para este estudo.");
		}
		
		if (lancamento.getEstudo() != null || estudoGerado.isLiberado()) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um estudo liberado para este lançamento.");
		}
		
		estudoGerado.setLiberado(true);
		
		this.estudoGeradoRepository.alterar(estudoGerado);
		
		Estudo estudo = new Estudo();
		
		try {
			
			PropertyUtils.copyProperties(estudo, estudoGerado);
			
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		
		Set<EstudoCota> estudoCotas = new HashSet<>();
		
		for (EstudoCotaGerado estudoCotaGerado : estudoGerado.getEstudoCotas()) {

			BigInteger qtdeEfetiva = estudoCotaGerado.getQtdeEfetiva();
			
			if (qtdeEfetiva != null && !BigInteger.ZERO.equals(qtdeEfetiva)) {
			
				estudoCotas.add(new EstudoCota(estudoCotaGerado, estudo));
			}
		}
		
		estudo.setLancamentos(null);
		
		estudo.setEstudoCotas(estudoCotas);
		
		this.estudoRepository.saveOrUpdate(estudo);
	
		lancamento.setEstudo(estudo);
		
		this.lancamentoRepository.alterar(lancamento);
		
		return estudo;
	}
	
    @Override
    @Transactional
    public void gravarDadosVendaMedia(Long estudoId, DistribuicaoVendaMediaDTO distribuicaoVendaMedia) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> campos = new HashMap<>();
        String valueAsString = null;
        try {
            valueAsString = mapper.writeValueAsString(distribuicaoVendaMedia);
        } catch (IOException e) {
            LOGGER.info("Serialization error.", e);
        }
        campos.put("DADOS_VENDA_MEDIA", valueAsString);
        estudoRepository.alterarPorId(estudoId, campos);
        estudoGeradoRepository.alterarPorId(estudoId, campos);
    }
    
    @Override
    @Transactional
    public void gerarEstudoPDV(final EstudoGerado estudo, final Cota cota, final BigInteger reparte) {
		
    	PDV pdvPrincipal = cota.getPDVPrincipal();
    	
    	List<PDV> pdvsSecundarios = cota.getPDVSecundarios();
    	
    	EstudoPDV estudoPDVPrincipal = this.gerarEstudoPDV(estudo, cota, pdvPrincipal);
    	
    	final boolean naoNecessitaReprocessamento = estudoPDVPrincipal.getId() == null || (pdvsSecundarios == null || pdvsSecundarios.isEmpty());
    	
    	if (naoNecessitaReprocessamento) {
    		
    		estudoPDVPrincipal.setReparte(reparte);
    		
    		estudoPDVRepository.merge(estudoPDVPrincipal);
    		
    		return;
    	}
    	
    	this.reprocessarEstudosPDV(estudo, cota, reparte, pdvsSecundarios,estudoPDVPrincipal);
	}

	private void reprocessarEstudosPDV(final EstudoGerado estudo,
			final Cota cota, final BigInteger reparte,
			List<PDV> pdvsSecundarios, EstudoPDV estudoPDVPrincipal) {
		
		BigInteger quantidadeTotalRepartePdvs = estudoPDVRepository.obterTotalReparte(estudo, cota);
    	
		BigInteger diferenca = reparte.subtract(quantidadeTotalRepartePdvs);
		
		final boolean naoHaDiferenca = diferenca.compareTo(BigInteger.ZERO) == 0;
		
		if (naoHaDiferenca) {
			
			return;
		}
		
		final boolean haDiferencaPositiva = diferenca.compareTo(BigInteger.ZERO) > 0;
		
		if (haDiferencaPositiva) {
			
			estudoPDVPrincipal.setReparte(estudoPDVPrincipal.getReparte().add(diferenca));
    		
    		estudoPDVRepository.merge(estudoPDVPrincipal);
    		
    		return;
    		
		} else {
			
			this.processarDiferencaNegativaEstudosPDV(estudo, cota, pdvsSecundarios,
					estudoPDVPrincipal, diferenca);
		}
	}

	private void processarDiferencaNegativaEstudosPDV(final EstudoGerado estudo,
			final Cota cota, List<PDV> pdvsSecundarios,
			EstudoPDV estudoPDVPrincipal, BigInteger diferenca) {
		
		diferenca = diferenca.abs();
		
		for(PDV pdvSecundario : pdvsSecundarios){
			
			EstudoPDV estudoPDVSecundario = 
				estudoPDVRepository.buscarPorEstudoCotaPDV(estudo, cota, pdvSecundario);
			
			while (diferenca.longValue() > 0) {
				
				if (!estudoPDVSecundario.getReparte().equals(BigInteger.ZERO)) {
					
					estudoPDVSecundario.setReparte(estudoPDVSecundario.getReparte().subtract(BigInteger.ONE));
					
				} else {
					
					break;
				}
				
				diferenca = diferenca.subtract(BigInteger.ONE);
			}
			
			estudoPDVRepository.merge(estudoPDVSecundario);
		}
		
		final boolean aindaHaDiferenca = diferenca.compareTo(BigInteger.ZERO) > 0;
		
		if (aindaHaDiferenca) {
			
			estudoPDVPrincipal.setReparte(estudoPDVPrincipal.getReparte().subtract(diferenca));
			
			estudoPDVRepository.merge(estudoPDVPrincipal);
		}
	}
    
    @Override
    @Transactional
    public EstudoPDV gerarEstudoPDV(final EstudoGerado estudo, final Cota cota, final PDV pdv, final BigInteger reparte) {
		
    	EstudoPDV estudoPDV = this.gerarEstudoPDV(estudo, cota,pdv);
    	
    	estudoPDV.setReparte(reparte);
		
		return this.estudoPDVRepository.merge(estudoPDV);
	}
    
    private EstudoPDV gerarEstudoPDV(final EstudoGerado estudo, final Cota cota, final PDV pdv) {
    	
    	EstudoPDV estudoPDV = estudoPDVRepository.buscarPorEstudoCotaPDV(estudo, cota, pdv);

		if (estudoPDV == null) {
			
		    estudoPDV = new EstudoPDV();
		    
		    estudoPDV.setEstudo(estudo);
		    estudoPDV.setCota(cota);
		    estudoPDV.setPdv(pdv);
		    estudoPDV.setReparte(BigInteger.ZERO);
		}
		
		return estudoPDV;
    }

	@Override
	@Transactional
	public EstudoCotaGerado obterEstudoCotaGerado(Long cotaId, Long estudoId) {
		return estudoCotaGeradoRepository.obterEstudoCotaGerado(cotaId, estudoId);
	}

	@Override
	@Transactional
	public void criarRepartePorPDV(Long id) {
		
		EstudoGerado estudo = estudoGeradoRepository.buscarPorId(id);
		Integer qtdRepartePDVDefinido = 0;
		
		for (EstudoCotaGerado estudoCota : estudo.getEstudoCotas()) {
			
			if(estudoCota.getQuantidadePDVS() != null && estudoCota.getQuantidadePDVS() > 1){

				switch (estudoCota.getClassificacao()) {
				
				case "FX":

					FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorProdutoCotaClassificacao(estudoCota.getCota(), estudo.getProdutoEdicao().getProduto().getCodigoICD(), estudo.getProdutoEdicao().getTipoClassificacaoProduto());
					
					for (FixacaoRepartePdv fixacaoPDV : fixacaoReparte.getRepartesPDV()) {
						if(fixacaoPDV.getRepartePdv() != null && fixacaoPDV.getRepartePdv() > 0){
							++qtdRepartePDVDefinido;
						}
					}
					
					if(qtdRepartePDVDefinido > 1){
						
						for (FixacaoRepartePdv fixPdv : fixacaoReparte.getRepartesPDV()) {
							for (PDV pdv : estudoCota.getCota().getPdvs()) {
								if(pdv.getId().equals(fixPdv.getPdv().getId())){
									
									BigInteger reparte = BigIntegerUtil.valueOfInteger(fixPdv.getRepartePdv());

									this.gerarEstudoPDV(estudoCota.getEstudo(), estudoCota.getCota(), pdv, reparte);
									
								}
							}
						}
						
					}
					
					break;
					
				case "MX":
					
					MixCotaProduto mixCotaProduto = mixCotaProdRepository.obterMixPorCotaProduto(estudoCota.getCota().getId(), estudo.getProdutoEdicao().getTipoClassificacaoProduto().getId(), estudo.getProdutoEdicao().getProduto().getCodigoICD());
					
					for (RepartePDV pdvMix : mixCotaProduto.getRepartesPDV()) {
						if(pdvMix.getReparte() != null && pdvMix.getReparte() > 0){
							++qtdRepartePDVDefinido;
						}
					}
					
					if(qtdRepartePDVDefinido > 1){
	
						for (RepartePDV pdvMix : mixCotaProduto.getRepartesPDV()) {
						
							for (PDV pdv : estudoCota.getCota().getPdvs()) {
								
								if(pdv.getId().equals(pdvMix.getPdv().getId())){
									BigInteger reparte = BigIntegerUtil.valueOfInteger(pdvMix.getReparte());
									this.gerarEstudoPDV(estudoCota.getEstudo(), estudoCota.getCota(), pdv, reparte);
								}
								
							}
						}
					}
					break;
				}
				
			}
			
		}
		
	}
	
	@Transactional
	@Override
	public Long countEstudosPorLancamento (Long idlancamento, Date dtLancamento){
		
		return estudoGeradoRepository.countEstudoGeradoParaLancamento(idlancamento, dtLancamento);
		
	}

}
