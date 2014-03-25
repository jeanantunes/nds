package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
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
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.EstudoPDV;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoPDVRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.service.RepartePdvService;

@Service
public class AnaliseParcialServiceImpl implements AnaliseParcialService {

    @Autowired
    private EstudoPDVRepository estudoPDVRepository;

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
    private InformacoesProdutoService infoProdService;
    
    @Autowired
	private EstudoService estudoService;
    
    @Autowired
    private EstudoCotaGeradoRepository estudoCotaGerado;
    
    @Autowired
    private EstudoGeradoRepository estudoGerado;
    
    
    private Map<String, String> mapClassificacaoCota;

    @Override
    @Transactional
    public EstudoCotaGerado buscarPorId(Long id) {
    	EstudoCotaGerado estudo = new EstudoCotaGerado();
        estudo.setEstudo(estudoGeradoRepository.buscarPorId(id));
        return estudo;
    }

    @Override
    @Transactional
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId) {
        return analiseParcialRepository.carregarEdicoesBaseEstudo(estudoId);
    }

    @Override
    @Transactional
    public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
        
    	List<AnaliseParcialDTO> lista = analiseParcialRepository.buscaAnaliseParcialPorEstudo(queryDTO);
        
    	if (queryDTO.getModoAnalise() != null && queryDTO.getModoAnalise().equalsIgnoreCase("PARCIAL")) {
            queryDTO.setEdicoesBase(analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoId()));
            for (AnaliseParcialDTO item : lista) {
                item.setDescricaoLegenda(traduzClassificacaoCota(item.getLeg()));
                List<EdicoesProdutosDTO> temp = new ArrayList<>();
                int contadorParciais = 0;
                for (EdicoesProdutosDTO edicoesProdutosDTO : queryDTO.getEdicoesBase()) {
                    if (edicoesProdutosDTO.isParcial()) {
                        temp.addAll(analiseParcialRepository.getEdicoesBaseParciais((long) item.getCota(), edicoesProdutosDTO.getEdicao().longValue(), edicoesProdutosDTO.getCodigoProduto(), Long.valueOf(edicoesProdutosDTO.getPeriodo())));
                        if (++contadorParciais > 2) {
                            break;
                        }
                    }
                }
                item.setEdicoesBase(temp);
            }
        } else {
            if (queryDTO.getEdicoesBase() == null) {
            	List<EdicoesProdutosDTO> edicoesBaseList = analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoId(),queryDTO.getDataLancamentoEdicao());
            	List<EdicoesProdutosDTO> edicaoDoEstudoOrigem = null;
            	
            	if(queryDTO.getEstudoOrigem()!=null && queryDTO.getEstudoOrigem().compareTo(0l) ==1) {
            		edicaoDoEstudoOrigem = analiseParcialRepository.carregarEdicoesBaseEstudo(queryDTO.getEstudoOrigem(),null);
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

            for (AnaliseParcialDTO item : lista) {
                item.setDescricaoLegenda(traduzClassificacaoCota(item.getLeg()));
                List<Long> idsProdutoEdicao = new LinkedList<>();
                List<EdicoesProdutosDTO> edicoesComVenda = new LinkedList<>();
                for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                    idsProdutoEdicao.add(edicao.getProdutoEdicaoId());
                }
                Map<Integer, EdicoesProdutosDTO> edicoesProdutosDTOMap = new HashMap<>();
                Integer ordemExibicaoHelper = 0;
                item.setEdicoesBase(new LinkedList<EdicoesProdutosDTO>());
                if(idsProdutoEdicao.size() > 0) {
                	Collection<? extends EdicoesProdutosDTO> buscaHistoricoDeVendas = buscaHistoricoDeVendas(item.getCota(), idsProdutoEdicao);
                    edicoesComVenda.addAll(buscaHistoricoDeVendas);
                		
                    if(queryDTO.getEstudoOrigem()!=null && queryDTO.getEstudoOrigem().compareTo(0l) ==1) {
                        AnaliseParcialDTO buscarReparteDoEstudo = analiseParcialRepository.buscarReparteDoEstudo(queryDTO.getEstudoOrigem(), item.getCota());

                        for (EdicoesProdutosDTO ed : edicoesComVenda) {
                            if (ed.getProdutoEdicaoId().equals(queryDTO.getEdicoesBase().get(0).getProdutoEdicaoId())) {
                                if(buscarReparteDoEstudo.getUltimoReparte()==null){
                                    edicoesComVenda.get(0).setReparte(BigDecimal.ZERO);
                                }else{
                                    edicoesComVenda.get(0).setReparte(BigDecimal.valueOf(buscarReparteDoEstudo.getUltimoReparte().longValue()));
                                }
                                break;
                            }
                        }
                    }
                		
                    for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                        for (EdicoesProdutosDTO ed : edicoesComVenda) {
                            if (ed.getProdutoEdicaoId().equals(edicao.getProdutoEdicaoId())) {
                                BeanUtils.copyProperties(edicao, ed, new String[] {"reparte", "venda"});
                                if (ed.getOrdemExibicao() == null) {
                                    ed.setOrdemExibicao(ordemExibicaoHelper++);
                                }
                                edicoesProdutosDTOMap.put(ed.getOrdemExibicao(), ed);
                            }
                        }
                    }
                }
                // tratamento para deixar as vendas zeradas em caso de edicao aberta
                for (EdicoesProdutosDTO edicao : edicoesProdutosDTOMap.values()) {
                    if (edicao.isEdicaoAberta()) {
                	    edicao.setVenda(BigDecimal.ZERO);
                    }
                }
                item.setEdicoesBase(new LinkedList<EdicoesProdutosDTO>(edicoesProdutosDTOMap.values()));
            }
        }
        return lista;
    }

    private void carregarInformacoesEdicoesBase(List<EdicoesProdutosDTO> edicoesBase) {
        for (EdicoesProdutosDTO edicao : edicoesBase) {
            edicao.setEdicaoAberta(produtoEdicaoRepository.isEdicaoAberta(edicao.getProdutoEdicaoId()));
        }
    }

    private Collection<? extends EdicoesProdutosDTO> buscaHistoricoDeVendas(int cota, List<Long> idsProdutoEdicao) {

        List<EdicoesProdutosDTO> edicoesProdutosDTOs = analiseParcialRepository.buscaHistoricoDeVendaParaCota((long) cota, idsProdutoEdicao);

        for (Long id : idsProdutoEdicao) {
            boolean idExiste = false;
            for (EdicoesProdutosDTO dto : edicoesProdutosDTOs) {
                if (id.equals(dto.getProdutoEdicaoId())) {
                    idExiste = true;
                    break;
                }
            }
            if (!idExiste) {
                edicoesProdutosDTOs.add(new EdicoesProdutosDTO(id));
            }
        }

        return edicoesProdutosDTOs;
    }

    @Override
    @Transactional
    public void atualizaClassificacaoCota(Long estudoId, Long numeroCota) {
	analiseParcialRepository.atualizaClassificacaoCota(estudoId, numeroCota);
    }
    
    @Override
    @Transactional
    public void atualizaReparte(Long estudoId, Long numeroCota, Long reparte, Long reparteDigitado) {
    	
    	this.validarDistribuicaoPorMultiplo(estudoId, reparteDigitado);
    	
        analiseParcialRepository.atualizaReparteCota(estudoId, numeroCota, reparte);
        analiseParcialRepository.atualizaReparteEstudo(estudoId, reparte);
    }
    
    private void validarDistribuicaoPorMultiplo(Long estudoId, Long reparteDigitado) {
    	
    	EstudoGerado estudoGerado = this.estudoGeradoRepository.buscarPorId(estudoId);
    	
    	if (estudoGerado.getDistribuicaoPorMultiplos() != null
    			&& estudoGerado.getDistribuicaoPorMultiplos() == 1) {
    		
    		BigInteger multiplo = 
    			new BigInteger(estudoGerado.getPacotePadrao().toString());
    		
    		BigInteger novoReparte = new BigInteger(reparteDigitado.toString());
    		
    		if (!novoReparte.mod(multiplo).equals(BigInteger.ZERO)) {
    			
    			throw new ValidacaoException(
    				TipoMensagem.WARNING, "Reparte deve ser múltiplo de " + multiplo);
    		}
    	}
    }

    @Override
    @Transactional
    public List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo) {
        return analiseParcialRepository.carregarDetalhesPdv(numeroCota, idEstudo);
    }

    @Override
    @Transactional
    public void liberar(Long id, List<CotaLiberacaoEstudo> cotas) {
    	
    	for (CotaLiberacaoEstudo cota : cotas) {
    		
    		this.validarDistribuicaoPorMultiplo(id, cota.getReparte().longValue());
    	}
    	
    	estudoService.liberar(id);
    }

    @Override
    @Transactional
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
        List<CotaQueNaoEntrouNoEstudoDTO> cotaQueNaoEntrouNoEstudoDTOList = analiseParcialRepository.buscarCotasQueNaoEntraramNoEstudo(queryDTO);
        for (CotaQueNaoEntrouNoEstudoDTO cotaQueNaoEntrouNoEstudoDTO : cotaQueNaoEntrouNoEstudoDTOList) {
            cotaQueNaoEntrouNoEstudoDTO.setDescricaoMotivo(traduzClassificacaoCota(cotaQueNaoEntrouNoEstudoDTO.getMotivo()));
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
    public void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap, String legenda, boolean manterFixa) {

    	EstudoGerado estudo = estudoGeradoRepository.buscarPorId(estudoId);
        Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);

        Map<Long, PDV> pdvMap = new HashMap<>();
        for (PDV pdv : cota.getPdvs()) {
            pdvMap.put(pdv.getId(), pdv);
        }

        for (PdvDTO pdvDTO : reparteMap) {
            PDV pdv = pdvMap.get(pdvDTO.getId());
            EstudoPDV estudoPDV = estudoPDVRepository.buscarPorEstudoCotaPDV(estudo, cota, pdv);

            if (estudoPDV == null) {
                estudoPDV = new EstudoPDV();
            estudoPDV.setEstudo(estudo);
            estudoPDV.setCota(cota);
                estudoPDV.setPdv(pdv);
            }

            estudoPDV.setReparte(BigInteger.valueOf(pdvDTO.getReparte()));
            estudoPDVRepository.merge(estudoPDV);
        }

        if (legenda.equalsIgnoreCase("FX")) {
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

        if (legenda.equalsIgnoreCase("MX")) {
            List<RepartePDVDTO> repartePDVDTOs = new ArrayList<>();
            for (PdvDTO pdvDTO : reparteMap) {
                RepartePDVDTO repartePDVDTO = new RepartePDVDTO();
                repartePDVDTO.setCodigoPdv(pdvDTO.getId());
                repartePDVDTO.setReparte(pdvDTO.getReparte());
                repartePDVDTOs.add(repartePDVDTO);
            }
            Produto produto = estudo.getProdutoEdicao().getProduto();
            MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.obterMixPorCotaProduto(cota.getId(), produto.getId());
            repartePdvService.salvarRepartesPDVMix(repartePDVDTOs, produto.getCodigo(), mixCotaProduto.getId());
        }
    }

    @Override
    @Transactional
    public CotaDTO buscarDetalhesCota(Integer numeroCota, String codigoProduto) {
        return cotaRepository.buscarCotaPorNumero(numeroCota, codigoProduto);
    }

    @Override
    @Transactional
    public List<AnaliseEstudoDetalhesDTO> historicoEdicoesBase(List<AnaliseEstudoDetalhesDTO> produtoEdicaoList) {
        List<AnaliseEstudoDetalhesDTO> list = new LinkedList<>();

        for (AnaliseEstudoDetalhesDTO dto : produtoEdicaoList) {
            AnaliseEstudoDetalhesDTO detalhesDTO;
            if (dto.isParcial()) {
                detalhesDTO = analiseParcialRepository.historicoEdicaoBase(dto.getIdProdutoEdicao(), dto.getNumeroPeriodo());
            } else {
                detalhesDTO = analiseParcialRepository.buscarDetalhesAnalise(dto.getIdProdutoEdicao());
            }
            BeanUtils.copyProperties(detalhesDTO, dto, new String[]{"idProdutoEdicao", "parcial", "numeroPeriodo", "ordemExibicao"});
            list.add(dto);
        }

        Collections.sort(list, new Comparator<AnaliseEstudoDetalhesDTO>() {
            @Override
            public int compare(AnaliseEstudoDetalhesDTO o1, AnaliseEstudoDetalhesDTO o2) {
                if (o1.isParcial()) {
                    if (o1.getDataLancamento() == null || o2.getDataLancamento() == null) {
                        return 0;
                    }
                int dt = o2.getDataLancamento().compareTo(o1.getDataLancamento());
                    if (dt != 0 || o1.getNumeroPeriodo() == null || o2.getNumeroPeriodo() == null) {
                    return dt;
                }
                    return o2.getNumeroPeriodo().compareTo(o1.getNumeroPeriodo());
                } else {
                    return o1.getOrdemExibicao().compareTo(o2.getOrdemExibicao());
                }
            }
        });

        return list;
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
	public BigDecimal obterReparteLancamentoEstudo(Long idEstudo) {
		return estudoGerado.reparteEstudoOriundoDoLancamento(idEstudo);
	}

	@Override
	public BigDecimal reparteFisicoLancamento(Long idEstudo) {
		return estudoGerado.reparteFisicoLancamento(idEstudo);
	}
}
