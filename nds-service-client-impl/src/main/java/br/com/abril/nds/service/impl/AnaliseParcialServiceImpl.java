package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseEstudoNormal_E_ParcialDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.AnaliseParcialExportXLSDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.DataLancamentoPeriodoEdicoesBasesDTO;
import br.com.abril.nds.dto.DetalhesEdicoesBasesAnaliseEstudoDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ReparteFixacaoMixWrapper;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaLiberacaoEstudo;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.RepartePdvService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.export.FileExporter.FileType;

@Service
public class AnaliseParcialServiceImpl implements AnaliseParcialService {

    private static final int QTDE_PARCIAIS_BASE = 3;

    @Autowired
    private AnaliseParcialRepository analiseParcialRepository;

    @Autowired
    private EstudoGeradoRepository estudoGeradoRepository;

    @Autowired
    private CotaRepository cotaRepository;

    @Autowired
    private RepartePdvService repartePdvService;

    @Autowired
    private FixacaoReparteRepository fixacaoReparteRepository;

    @Autowired
    private MixCotaProdutoRepository mixCotaProdutoRepository;

    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;
    
    @Autowired
	private EstudoService estudoService;
    
    @Autowired
    private EstudoCotaGeradoRepository estudoCotaGerado;
    
    @Autowired
    private EstudoGeradoRepository estudoGerado;
    
    @Autowired 
    private UsuarioService usuarioService;
    
    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    private Map<String, String> mapClassificacaoCota;

    @Override
    @Transactional
    public EstudoCotaGerado buscarPorId(Long id) {
    	if(id== null){
    		return null;
    	}
    	EstudoCotaGerado estudo = new EstudoCotaGerado();
        estudo.setEstudo(estudoGeradoRepository.buscarPorId(id));
        return estudo;
    }
    
    @Override
    @Transactional(readOnly=true)
    public AnaliseEstudoNormal_E_ParcialDTO buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
    	
    	queryDTO.setIdUltimoLancamento(lancamentoRepository.getIdUltimoLancamentoFechado(queryDTO.getCodigoProduto()));
    	
    	List<AnaliseParcialDTO> lista = analiseParcialRepository.buscaAnaliseParcialPorEstudo(queryDTO);
    	
    	List<AnaliseParcialExportXLSDTO> listaXLS = new ArrayList<>();
    	
		BigInteger total_qtdCotas = BigInteger.ZERO;
	    BigInteger total_somatorioReparteSugerido = BigInteger.ZERO;
	    BigInteger total_somatorioUltimoReparte = BigInteger.ZERO;
	    Map<String, BigInteger> mapTotaisEd = new HashMap<>();
    	
    	if (queryDTO.getModoAnalise() != null && queryDTO.getModoAnalise().equalsIgnoreCase("PARCIAL")) {
    	    
    		final boolean parcialPossuiRedistribuicao = 
    				analiseParcialRepository.verificarRedistribuicaoNoPeriodoParcial(queryDTO.getEstudoId(), queryDTO.getNumeroParcial());
    		
            queryDTO.setEdicoesBase(analiseParcialRepository.carregarEdicoesBaseEstudoParcial(queryDTO.getEstudoId(), queryDTO.getNumeroParcial(), parcialPossuiRedistribuicao));
            
            List<EdicoesProdutosDTO> baseUtilizadas = getBasesUtilizadas(queryDTO);
            
            for (AnaliseParcialDTO item : lista) {
                
            	item.setDescricaoLegenda(traduzClassificacaoCota(item.getLeg()));
                List<EdicoesProdutosDTO> temp = new ArrayList<>();
                int contadorParciais = 1;
                
                List<EdicoesProdutosDTO> edicoesProdutoPorCota = this.getEdicoesProdutoPorCota(queryDTO.getEdicoesBase(), item.getCota(), baseUtilizadas);
                
                for (EdicoesProdutosDTO edicoesProdutosDTO : edicoesProdutoPorCota) {
                    if (edicoesProdutosDTO.isParcial()) {

                        temp.add(edicoesProdutosDTO);
                        
                        if (contadorParciais++ >= QTDE_PARCIAIS_BASE) {
                            break;
                        }
                    }
                    
                }
                
                this.completarParciaisBase(temp, QTDE_PARCIAIS_BASE);
                
                EdicoesProdutosDTO edicoesProdutosAcumulado = this.getReparteVendaAcumulado(edicoesProdutoPorCota);
                
                if (edicoesProdutosAcumulado != null) {
                
                    temp.add(edicoesProdutosAcumulado);
                }
                
                item.setEdicoesBase(temp);
                
                item.setUltimoReparte(this.getUltimoReparte(temp));
                
                for (int i = 0; i<temp.size(); i++) {
                	temp.get(i).setOrdemExibicao(i);
                	
                	putMapSomatorioTotaisEdicao(mapTotaisEd, temp.get(i));
        		}
                
                total_somatorioUltimoReparte = somatorioUltimoReparte(total_somatorioUltimoReparte, item);
                
                total_somatorioReparteSugerido = somatorioReparteSugerido(total_somatorioReparteSugerido, item);
   
                total_qtdCotas = BigIntegerUtil.soma(total_qtdCotas, BigInteger.ONE);
                
            }
            
        } else {
            if (queryDTO.getEdicoesBase() == null) {
            	List<EdicoesProdutosDTO> edicoesBaseList = analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoId());
            	List<EdicoesProdutosDTO> edicaoDoEstudoOrigem = null;
            	
            	if(queryDTO.getEstudoOrigem()!=null && queryDTO.getEstudoOrigem().compareTo(0l) ==1) {
            		edicaoDoEstudoOrigem = analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoOrigem());
//            		if(edicaoDoEstudoOrigem.size()==6){
//            			edicaoDoEstudoOrigem.remove(edicaoDoEstudoOrigem.size()-1);
//            		}
            	}
            	if(edicaoDoEstudoOrigem!=null && edicaoDoEstudoOrigem.size()>0){
            		List<EdicoesProdutosDTO> listConcat = new ArrayList<EdicoesProdutosDTO>();
            		listConcat.addAll(edicaoDoEstudoOrigem);
//            		listConcat.addAll(edicoesBaseList);
            		queryDTO.setEdicoesBase(listConcat);
            	}else{
            		queryDTO.setEdicoesBase(edicoesBaseList);
            	}
            } else {
        		carregarInformacoesEdicoesBase(queryDTO.getEdicoesBase());
            }

            List<Long> idsProdutoEdicao = new LinkedList<>();
            for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                idsProdutoEdicao.add(edicao.getProdutoEdicaoId());
            }

            List<Long> listaCotasId = new ArrayList<>();

            for (AnaliseParcialDTO cota : lista) {
            	listaCotasId.add((long) cota.getCotaId());
            }
            
            // Tratamento para Filtro por Percentual de Venda. Necessário após paginação do GRID
            if(queryDTO.possuiOrdenacaoPlusFiltro() && queryDTO.possuiPercentualDeVenda()){
            	listaCotasId = analiseParcialRepository.obterCotasDentroDoPercentualReparteFiltro(listaCotasId, idsProdutoEdicao, queryDTO);
            	
            	List<AnaliseParcialDTO> cotasForaDoFiltro = new ArrayList<>();
            	
            	for (AnaliseParcialDTO cota : lista) {
					if(!listaCotasId.contains(new Long(cota.getCotaId()))){
						cotasForaDoFiltro.add(cota);
					}
				}
            	lista.removeAll(cotasForaDoFiltro);
            }
            // ---- 
            
            Map<Integer, List<EdicoesProdutosDTO>> cotasComVenda;
            
            if(listaCotasId.isEmpty() || idsProdutoEdicao.isEmpty()){
            	cotasComVenda = new HashMap<>(); 	
            }else{
            	cotasComVenda = analiseParcialRepository.buscaHistoricoDeVendaTodasCotas(listaCotasId, idsProdutoEdicao);
            }
            
            for (AnaliseParcialDTO item : lista) {
                item.setDescricaoLegenda(traduzClassificacaoCota(item.getLeg()));
                
                List<EdicoesProdutosDTO> edicoesComVenda = new LinkedList<>();
                Map<Integer, EdicoesProdutosDTO> edicoesProdutosDTOMap = new HashMap<>();
                Integer ordemExibicaoHelper = 0;

                item.setEdicoesBase(new LinkedList<EdicoesProdutosDTO>());

                if(idsProdutoEdicao.size() > 0) {
                	Collection<? extends EdicoesProdutosDTO> buscaHistoricoDeVendas = buscaHistoricoDeVendas(item.getCotaId(), idsProdutoEdicao, cotasComVenda);
//                	Collection<? extends EdicoesProdutosDTO> buscaHistoricoDeVendas = buscaHistoricoDeVendas(item.getCotaId(), idsProdutoEdicao);
                    edicoesComVenda.addAll(buscaHistoricoDeVendas);

                    for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                        for (EdicoesProdutosDTO ed : edicoesComVenda) {
                            if (ed.getProdutoEdicaoId().equals(edicao.getProdutoEdicaoId())) {
                                BeanUtils.copyProperties(edicao, ed, new String[] {"reparte", "venda"});
                                if (ed.getOrdemExibicao() == null) {
                                    ed.setOrdemExibicao(ordemExibicaoHelper++);
                                }
                                edicoesProdutosDTOMap.put(ed.getOrdemExibicao(), ed);
                                putMapSomatorioTotaisEdicao(mapTotaisEd, ed);
                            }
                        }
                    }
                }
                
                item.setEdicoesBase(new LinkedList<EdicoesProdutosDTO>(edicoesProdutosDTOMap.values()));
                
                total_somatorioUltimoReparte = somatorioUltimoReparte(total_somatorioUltimoReparte, item);
                
                total_somatorioReparteSugerido = somatorioReparteSugerido(total_somatorioReparteSugerido, item);
   
                total_qtdCotas = BigIntegerUtil.soma(total_qtdCotas, BigInteger.ONE);
                
            }
        }
    	
    	if(queryDTO.getFile() != null && queryDTO.getFile() == FileType.XLS){
    		
    		Map<Integer, AnaliseParcialExportXLSDTO> dadosCotasPdv = analiseParcialRepository.buscarDadosPdvParaXLS(queryDTO);
    		
    		for (AnaliseParcialDTO cota : lista) {
				AnaliseParcialExportXLSDTO dadosPDV = dadosCotasPdv.get(cota.getCotaId());
				
				if(dadosPDV == null){
					continue;
				}
				
		    	dadosPDV.setCota(cota.getCota());
		    	dadosPDV.setClassificacao(cota.getClassificacao());
		    	dadosPDV.setNome(cota.getNome());
		    	dadosPDV.setNpdv(cota.getNpdv());
		    	dadosPDV.setReparteSugerido(cota.getReparteSugerido());
		    	dadosPDV.setLeg(cota.getLeg());
		    	dadosPDV.setJuramento(cota.getJuramento());
		    	dadosPDV.setMediaVenda(cota.getMediaVenda());
		    	dadosPDV.setUltimoReparte(cota.getUltimoReparte());
		    	dadosPDV.setEdicoesBase(cota.getEdicoesBase());
				
				listaXLS.add(dadosPDV);
				
			}
    		
    	}
    	
    	AnaliseEstudoNormal_E_ParcialDTO dto = new AnaliseEstudoNormal_E_ParcialDTO();
    	
    	dto.setAnaliseParcialDTO(lista);
    	
    	if(queryDTO.getFile() != null && queryDTO.getFile() == FileType.XLS){
    		dto.setAnaliseParcialXLSDTO(listaXLS);
    	}
    	
    	dto.setTotal_qtdCotas(total_qtdCotas);
    	dto.setTotal_somatorioUltimoReparte(total_somatorioUltimoReparte);
    	dto.setTotal_somatorioReparteSugerido(total_somatorioReparteSugerido);
    	
    	formatarTotaisReparteVendaEdicao(mapTotaisEd, dto);
    	
        return dto;
    }

	private BigInteger somatorioReparteSugerido(BigInteger total_somatorioReparteSugerido, AnaliseParcialDTO item) {
		if(BigIntegerUtil.isMaiorQueZero(item.getReparteSugerido())){
			total_somatorioReparteSugerido = BigIntegerUtil.soma(total_somatorioReparteSugerido, item.getReparteSugerido());
		}
		return total_somatorioReparteSugerido;
	}

	private BigInteger somatorioUltimoReparte(BigInteger total_somatorioUltimoReparte, AnaliseParcialDTO item) {
		if(BigIntegerUtil.isMaiorQueZero(item.getUltimoReparte())){
			total_somatorioUltimoReparte = BigIntegerUtil.soma(total_somatorioUltimoReparte, item.getUltimoReparte());
		}
		return total_somatorioUltimoReparte;
	}

	private void putMapSomatorioTotaisEdicao(Map<String, BigInteger> mapTotaisEd, EdicoesProdutosDTO ed) {
		if(mapTotaisEd.get("reparte_"+ed.getOrdemExibicao()) == null){
			mapTotaisEd.put("reparte_"+ed.getOrdemExibicao(), ed.getReparte());
			mapTotaisEd.put("venda_"+ed.getOrdemExibicao(), ed.getVenda());
		}else{
			mapTotaisEd.put("reparte_"+ed.getOrdemExibicao(), BigIntegerUtil.soma(mapTotaisEd.get("reparte_"+ed.getOrdemExibicao()) , ed.getReparte()));
			mapTotaisEd.put("venda_"+ed.getOrdemExibicao(), BigIntegerUtil.soma(mapTotaisEd.get("venda_"+ed.getOrdemExibicao()), ed.getVenda()));
		}
	}

	private void formatarTotaisReparteVendaEdicao(Map<String, BigInteger> mapTotaisEd, AnaliseEstudoNormal_E_ParcialDTO dto) {
		List<BigInteger> totalReparte = new ArrayList<>();
    	List<BigInteger> totalVenda = new ArrayList<>();
    	
    	for (int i = 0; i < (mapTotaisEd.size()/2); i++) {
			totalReparte.add(mapTotaisEd.get("reparte_"+i));
			totalVenda.add(mapTotaisEd.get("venda_"+i));
		}
    	
    	dto.setReparteTotalEdicao(totalReparte);
    	dto.setVendaTotalEdicao(totalVenda);
	}

	private List<EdicoesProdutosDTO> getBasesUtilizadas(AnaliseParcialQueryDTO queryDTO) {
		
		List<EdicoesProdutosDTO> baseUtilizadas = new ArrayList<>();
		
		Integer qtdBases = analiseParcialRepository.qtdBasesNoEstudo(queryDTO.getEstudoId(), true);
		
		if(qtdBases > 0){
			baseUtilizadas = analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoId());
		}else{
			baseUtilizadas = analiseParcialRepository.carregarPeriodosAnterioresParcial(queryDTO.getEstudoId(), false);
			
			if(baseUtilizadas.size() == 0){
				baseUtilizadas = analiseParcialRepository.carregarPeriodosAnterioresParcial(queryDTO.getEstudoId(), true);
			}
		}
		
		return baseUtilizadas;
	}

    private BigInteger getUltimoReparte(List<EdicoesProdutosDTO> temp) {
		
    	if(temp!= null && !temp.isEmpty()){
    		
    		return temp.get(0).getReparte();
    	}
    	
		return null;
	}

	private void completarParciaisBase(List<EdicoesProdutosDTO> temp, int quantidadeParciaisBase) {
        
        for (int i = temp.size(); i < quantidadeParciaisBase; i++) {
            
            temp.add(new EdicoesProdutosDTO());
        }
    }
    
    private EdicoesProdutosDTO getReparteVendaAcumulado(List<EdicoesProdutosDTO> edicoesProdutoPorCota) {
        
        if (edicoesProdutoPorCota.isEmpty()) {
            
            return null;
        }
        
        BigInteger reparte = BigInteger.ZERO;
        BigInteger venda = BigInteger.ZERO;
        
        for (EdicoesProdutosDTO edicoesProdutosDTO : edicoesProdutoPorCota) {
            
            reparte = BigIntegerUtil.soma(reparte, edicoesProdutosDTO.getReparte());
            venda = BigIntegerUtil.soma(venda, edicoesProdutosDTO.getVenda());
        }

        EdicoesProdutosDTO edicoesProdutosAcumulado = new EdicoesProdutosDTO();
        
        edicoesProdutosAcumulado.setReparte(BigDecimal.valueOf(reparte.doubleValue()));
        edicoesProdutosAcumulado.setVenda(BigDecimal.valueOf(venda.doubleValue()));
        
        return edicoesProdutosAcumulado;
    }

    private List<EdicoesProdutosDTO> getEdicoesProdutoPorCota(List<EdicoesProdutosDTO> edicoesProdutosDTO, Integer numeroCota, List<EdicoesProdutosDTO> bases) {
        
        List<EdicoesProdutosDTO> edicoesProdutoPorCota = new ArrayList<>();
        
        List<EdicoesProdutosDTO> edicoesTemp = new ArrayList<>();
        
        for (EdicoesProdutosDTO edicaoProdutoDTO : edicoesProdutosDTO) {
            
            if (edicaoProdutoDTO.getNumeroCota().equals(numeroCota)) {
                
                edicoesProdutoPorCota.add(edicaoProdutoDTO);
            }
        }
        
        if(edicoesProdutoPorCota.size() != bases.size()){
        	
        	edicoesTemp.addAll(edicoesProdutoPorCota);
        	edicoesProdutoPorCota.clear();
        	
        	for (int i = 0; i < bases.size(); i++) {
				
        		boolean contem = false;
				EdicoesProdutosDTO fake = new EdicoesProdutosDTO();
				
        		for (EdicoesProdutosDTO edicaoCota : edicoesTemp) {
        			
					if(bases.get(i).getPeriodo() != null){
						if(bases.get(i).getPeriodo().equalsIgnoreCase(edicaoCota.getPeriodo())){
							edicoesProdutoPorCota.add(edicaoCota);
							contem = true;
						}else{
							fake.setCodigoProduto(edicaoCota.getCodigoProduto());
							fake.setEdicao(edicaoCota.getEdicao());
							fake.setNumeroCota(edicaoCota.getNumeroCota());
							fake.setParcial(edicaoCota.isParcial());
							fake.setProdutoEdicaoId(edicaoCota.getProdutoEdicaoId());
						}
					}
				}
        		
        		if(!contem){
        			
        			fake.setReparte(BigDecimal.ZERO);
        			fake.setVenda(BigDecimal.ZERO);
        			fake.setPeriodo(bases.get(i).getPeriodo());
        			fake.setParcial(true);
        			
        			edicoesProdutoPorCota.add(fake);
        		}
			}
        }
        
        return edicoesProdutoPorCota;
    }
    
    private void carregarInformacoesEdicoesBase(List<EdicoesProdutosDTO> edicoesBase) {
        for (EdicoesProdutosDTO edicao : edicoesBase) {
            edicao.setEdicaoAberta(produtoEdicaoRepository.isEdicaoAberta(edicao.getProdutoEdicaoId()));
        }
    }

//    private Collection<? extends EdicoesProdutosDTO> buscaHistoricoDeVendas(int cota, List<Long> idsProdutoEdicao) {
//
//    	
//        List<EdicoesProdutosDTO> edicoesProdutosDTOs = analiseParcialRepository.buscaHistoricoDeVendaParaCota((long) cota, idsProdutoEdicao);
//
//        for (Long id : idsProdutoEdicao) {
//            boolean idExiste = false;
//            for (EdicoesProdutosDTO dto : edicoesProdutosDTOs) {
//                if (id.equals(dto.getProdutoEdicaoId())) {
//                    idExiste = true;
//                    break;
//                }
//            }
//            if (!idExiste) {
//                edicoesProdutosDTOs.add(new EdicoesProdutosDTO(id));
//            }
//        }
//
//        return edicoesProdutosDTOs;
//    }
    
    private Collection<? extends EdicoesProdutosDTO> buscaHistoricoDeVendas(int cota, List<Long> idsProdutoEdicao, Map<Integer, List<EdicoesProdutosDTO>> cotasComVenda) {

    	
  	List<EdicoesProdutosDTO> edicoesProdutosDTOs = cotasComVenda.get(cota);
  	
      for (Long id : idsProdutoEdicao) {
          boolean idExiste = false;
          
          if(edicoesProdutosDTOs != null){
          	for (EdicoesProdutosDTO dto : edicoesProdutosDTOs) {
          		if (id.equals(dto.getProdutoEdicaoId())) {
          			idExiste = true;
          			break;
          		}
          	}
          }else{
          	edicoesProdutosDTOs = new ArrayList<EdicoesProdutosDTO>();
          }
          
          if (!idExiste) {
              edicoesProdutosDTOs.add(new EdicoesProdutosDTO(id));
          }
      }

      return edicoesProdutosDTOs;
  }

    @Override
    @Transactional
    public void atualizaClassificacaoCota(Long estudoId, Integer numeroCota, String classificacaoCota) {
    	analiseParcialRepository.atualizaClassificacaoCota(estudoId, numeroCota, classificacaoCota);
    }
    
    @Override
    @Transactional
    public void atualizaReparte(Long estudoId, Integer numeroCota, Long reparte, Long reparteDigitado) {
    	
    	
    //	EstudoGerado estudoGerado = estudoService.obterEstudo(estudoId);
    	
    	
    	EstudoGerado estudoGerado = estudoService.obterEstudoSql(estudoId);
    	
		
		if(estudoGerado.isLiberado()){
    		throw new ValidacaoException(TipoMensagem.WARNING, "Estudo já liberado!");
    	}
    	
    	if(estudoGerado.getDistribuicaoPorMultiplos() != null && estudoGerado.getDistribuicaoPorMultiplos() == 1){
    		this.validarDistribuicaoPorMultiplo(estudoId, reparteDigitado, estudoGerado);
    	}
		
    	if(reparteDigitado >= 0){
    		analiseParcialRepository.atualizaReparteCota(estudoId, numeroCota, reparte);
    	}else{
    		EstudoCotaGerado estudoCota = estudoService.obterEstudoCotaGerado(numeroCota.intValue(), estudoId);
    		estudoCotaGerado.removerEstudoCotaGerado(estudoCota.getId());
    	}
    	
        analiseParcialRepository.atualizaReparteEstudo(estudoId, reparte);
    }
    
    private void validarDistribuicaoPorMultiplo(Long estudoId, Long reparteDigitado, EstudoGerado estudo) {
    		
		BigInteger multiplo = new BigInteger(estudo.getPacotePadrao().toString());
		
		BigInteger novoReparte = new BigInteger(reparteDigitado.toString());
		
		if (!novoReparte.mod(multiplo).equals(BigInteger.ZERO)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Reparte deve ser múltiplo de " + multiplo);
		}
    }

    @Override
    @Transactional
    public List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo) {
        
    	List<PdvDTO> lista = analiseParcialRepository.carregarDetalhesPdv(numeroCota, idEstudo);
    	
    	return lista;
    }

    @Override
    @Transactional
    public void liberar(Long id, List<CotaLiberacaoEstudo> cotas) {
    	
    	EstudoGerado estudoGerado = estudoService.obterEstudoSql(id);
    	
    	if(estudoGerado.getDistribuicaoPorMultiplos() != null && estudoGerado.getDistribuicaoPorMultiplos()==1){
    		for (CotaLiberacaoEstudo cota : cotas) {
    			this.validarDistribuicaoPorMultiplo(id, cota.getReparte().longValue(), estudoGerado);
    		}
    	}
    	estudoService.liberar(id);
    	
    	Lancamento lancamentoEstudo = lancamentoRepository.obterParaAtualizar(estudoGerado.getLancamentoID());
    	
    	//verificar update Lancamento - Paliativo
    	if(lancamentoEstudo.getEstudo() == null){
    		
    		Estudo estudo = new Estudo();
    		estudo.setId(id);
    		
    		lancamentoEstudo.setEstudo(estudo);
    		
    		lancamentoRepository.alterar(lancamentoEstudo);
    	}
    }

    @Override
    @Transactional
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
    	
        List<CotaQueNaoEntrouNoEstudoDTO> cotaQueNaoEntrouNoEstudoDTOList = analiseParcialRepository.buscarCotasQueNaoEntraramNoEstudo(queryDTO);
        for (CotaQueNaoEntrouNoEstudoDTO cotaQueNaoEntrouNoEstudoDTO : cotaQueNaoEntrouNoEstudoDTOList) {
        	
        	if(cotaQueNaoEntrouNoEstudoDTO.getMotivo() == null || cotaQueNaoEntrouNoEstudoDTO.getMotivo().equals("")) {
        		
        		cotaQueNaoEntrouNoEstudoDTO.setMotivo("SH");
        	}
        	
        	if(cotaQueNaoEntrouNoEstudoDTO.getMotivo().equalsIgnoreCase("MX")) {
        		
        		cotaQueNaoEntrouNoEstudoDTO.setMotivo("SM");
        	}
        	
            cotaQueNaoEntrouNoEstudoDTO.setSiglaMotivo(cotaQueNaoEntrouNoEstudoDTO.getMotivo());
            cotaQueNaoEntrouNoEstudoDTO.setMotivo(traduzClassificacaoCota(cotaQueNaoEntrouNoEstudoDTO.getMotivo()));
        }
        return cotaQueNaoEntrouNoEstudoDTOList;
    }

    @Override
    @Transactional
    public BigDecimal calcularPercentualAbrangencia(Long estudoId) {
        int cotasAtivas = cotaRepository.obterCotasAtivas();
        int cotasComReparte = estudoGeradoRepository.obterCotasComRepartePorIdEstudo(estudoId);
        return cotasAtivas == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(cotasComReparte)
                .divide(BigDecimal.valueOf(cotasAtivas), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
    }
    
    @Override
    @Transactional
    public DetalhesEdicoesBasesAnaliseEstudoDTO obterReparteEVendaTotal(String codigoProduto, Long edicao, Long idTipoClassificacao, Integer numPeriodoParcial){
    	return analiseParcialRepository.buscarReparteVendaTotalPorEdicao(codigoProduto, edicao, idTipoClassificacao, numPeriodoParcial);
    }

    @Override
    @Transactional
    public void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap, String legenda, boolean manterFixa) {

    	EstudoGerado estudo = estudoGeradoRepository.buscarPorId(estudoId);
        Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);

        Map<Long, PDV> pdvMap = new HashMap<>();
        
        for (PDV pdv : cota.getPdvs()) {
        	
            pdvMap.put(pdv.getId(), pdv);
        }

        for (PdvDTO pdvDTO : reparteMap) {
            
        	PDV pdv = pdvMap.get(pdvDTO.getId());
        	
        	if(pdvDTO.getReparte()!= null){
        		
        		estudoService.gerarEstudoPDV(estudo, cota, pdv, BigInteger.valueOf(pdvDTO.getReparte()));
        	}
        }

        if ("FX".equalsIgnoreCase(legenda)) {
            List<RepartePDVDTO> repartePDVDTOs = new ArrayList<>();
            for (PdvDTO pdvDTO : reparteMap) {
                RepartePDVDTO repartePDVDTO = new RepartePDVDTO();
                repartePDVDTO.setCodigoPdv(pdvDTO.getId());
                repartePDVDTO.setReparte(pdvDTO.getReparte());
                repartePDVDTOs.add(repartePDVDTO);
            }
            Produto produto = estudo.getProdutoEdicao().getProduto();
            FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorProdutoCotaClassificacao(cota, produto.getCodigoICD(), estudo.getProdutoEdicao().getTipoClassificacaoProduto());
            repartePdvService.salvarRepartesPDV(repartePDVDTOs, produto.getCodigo(),fixacaoReparte.getId(),manterFixa);
    }

        if ("MX".equalsIgnoreCase(legenda)) {
            List<RepartePDVDTO> repartePDVDTOs = new ArrayList<>();
            for (PdvDTO pdvDTO : reparteMap) {
                RepartePDVDTO repartePDVDTO = new RepartePDVDTO();
                repartePDVDTO.setCodigoPdv(pdvDTO.getId());
                repartePDVDTO.setReparte(pdvDTO.getReparte());
                repartePDVDTOs.add(repartePDVDTO);
            }
            Produto produto = estudo.getProdutoEdicao().getProduto();
            MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.obterMixPorCotaProduto(cota.getId(), estudo.getProdutoEdicao().getTipoClassificacaoProduto().getId(), estudo.getProdutoEdicao().getProduto().getCodigoICD());
            repartePdvService.salvarRepartesPDVMix(repartePDVDTOs, produto.getCodigo(), mixCotaProduto.getId());
        }
    }
    
    @Override
    @Transactional
    public CotaDTO buscarDetalhesCota(Integer numeroCota, String codigoProduto, Long idClassifProdEdicao) {
        return cotaRepository.buscarCotaPorNumero(numeroCota, codigoProduto, idClassifProdEdicao);
    }
    
    private String traduzClassificacaoCota(String motivo) {
        if (mapClassificacaoCota == null) {
            populaMapClassificacaoCota();
        }
        return mapClassificacaoCota.get(motivo);
    }

    private void populaMapClassificacaoCota() {
        mapClassificacaoCota = new HashMap<>();
        for (ClassificacaoCota classificacaoCota : ClassificacaoCota.values()) {
            mapClassificacaoCota.put(classificacaoCota.getCodigo(), classificacaoCota.getTexto());
        }
    }

    @Override
    @Transactional
    public Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo) {
        return analiseParcialRepository.buscarCotasPorTipoDistribuicao(tipo);
    }

    @Override
    @Transactional
    public BigInteger atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal) {
        analiseParcialRepository.atualizaReparteTotalESaldo(idEstudo, reparteTotal);
        return estudoGeradoRepository.buscarPorId(idEstudo).getSobra();
    }

	@Override
	@Transactional
	public List<EstudoCotaGerado> obterEstudosCotaGerado(Long id) {
		return estudoCotaGerado.obterEstudosCota(id);
	}

	@Override
	@Transactional
	public BigDecimal reparteFisicoOuPrevistoLancamento(Long idEstudo) {
		return estudoGerado.reparteFisicoOuPrevistoLancamento(idEstudo);
	}

	@Override
	@Transactional
	public void atualizarFixacaoOuMix(ReparteFixacaoMixWrapper wrapper) {
		
		if(wrapper.getId() == null){
    		return;
    	}
		
		Usuario usuarioLogado = this.usuarioService.getUsuarioLogado();

		if(wrapper.isFixacao()) {
			
			FixacaoReparte fixacao = fixacaoReparteRepository.buscarPorId(wrapper.getId());
			
			if(fixacao == null){
				return;
			}
			
        	this.fixacaoReparteRepository.atualizarQtdeExemplares(wrapper.getId(), wrapper.getReparte(), wrapper.getDataAtualizacao(), usuarioLogado);

        } else if (wrapper.isMix()) {
        	
        	MixCotaProduto mix = null;
        	
    		mix = mixCotaProdutoRepository.buscarPorId(wrapper.getId());
        	
        	if(mix == null){
        		return;
        	}
        	
        	boolean isPDVUnico = mixCotaProdutoRepository.verificarMixDefinidoPorPDV(wrapper.getId());
        		
    		if(isPDVUnico == true){
    			
    			this.mixCotaProdutoRepository.atualizarReparte(
    					isPDVUnico,
    					wrapper.getId(), 
    					wrapper.getReparte().longValue(), 
    					usuarioLogado, 
    					wrapper.getDataAtualizacao()
    					);
    			
    		}else{

    			if(wrapper.getReparte() < mix.getReparteMinimo() || wrapper.getReparte() > mix.getReparteMaximo()) {
    				
    				this.mixCotaProdutoRepository.atualizarReparte(
        					isPDVUnico,
        					wrapper.getId(), 
        					wrapper.getReparte().longValue(), 
        					usuarioLogado, 
        					wrapper.getDataAtualizacao()
        					);
    				
    			}
    		}
        	
        }
	}
	
	@Transactional
	@Override
	public ValidacaoException validarLiberacaoDeEstudo(Long estudoId) {

		ValidacaoException validacao = null;
		
    	EstudoGerado estudoGerado = estudoGeradoRepository.buscarPorId(estudoId);
    	
    	BigDecimal reparteFisicoOuPrevisto = reparteFisicoOuPrevistoLancamento(estudoId);
    	
    	
    	for (EstudoCotaGerado estudoCota : estudoGerado.getEstudoCotas()) {
    		if(BigIntegerUtil.isMenorQueZero(estudoCota.getReparte())){
    			validacao = new ValidacaoException(TipoMensagem.WARNING,"Há cota(s) com reparte(s) negativo(s), por favor ajustá-la(s)!");
    			break;
    		}
    	} 

    	if((reparteFisicoOuPrevisto != null)&&(estudoGerado.getQtdeReparte().compareTo(reparteFisicoOuPrevisto.toBigInteger()) > 0)) {
    		validacao =  new ValidacaoException(TipoMensagem.WARNING,"O reparte distribuido é maior que estoque disponível!");
    		return validacao;
    	}
    	
    	if((estudoGerado.getSobra() != null) && estudoGerado.getSobra().compareTo(BigInteger.ZERO) != 0){
    		validacao =  new ValidacaoException(TipoMensagem.WARNING,"Não é possível liberar estudo com saldo de reparte.");
    		return validacao;
    	}
    	
    	ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoGeradoRepository.obterResumoEstudo(estudoId, false);
    	
    	if((resumo.getSaldo() != null) && resumo.getSaldo().compareTo(BigDecimal.ZERO) != 0){
    		validacao =  new ValidacaoException(TipoMensagem.WARNING,"Não é possível liberar estudo com saldo de reparte.");
    		return validacao;
    	}
    	
    	if(validacao == null) {
    		validacao = new ValidacaoException(TipoMensagem.SUCCESS, "Operação realizada com sucesso.");
    	}
		
		return validacao;
	}
	
	@Override
	@Transactional
	public List<DataLancamentoPeriodoEdicoesBasesDTO> obterDataLacmtoPeridoEdicoesBaseParciais(Long idEstudo, Long idProdutoEdicao){
		return analiseParcialRepository.obterDataDeLacmtoPeriodoParcial(idEstudo, idProdutoEdicao);
	}
	
	@Transactional
	@Override
	public List<EdicoesProdutosDTO> carregarPeriodosAnterioresParcial(Long idEstudo, Boolean isTrazerEdicoesAbertas){
		return analiseParcialRepository.carregarPeriodosAnterioresParcial(idEstudo, isTrazerEdicoesAbertas);
	}
}