package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.verificadorFixacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class FixacaoReparteServiceImpl implements FixacaoReparteService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FixacaoReparteServiceImpl.class);


	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FixacaoReparteRepository fixacaoReparteRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProdutoRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private FixacaoRepartePdvRepository fixacaoRepartePdvRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO filtroConsultaFixacaoProdutoDTO) {
		
		Produto produto = produtoService.obterProdutoPorCodigo(filtroConsultaFixacaoProdutoDTO.getCodigoProduto());
        filtroConsultaFixacaoProdutoDTO.setCodigoProduto(produto.getCodigoICD());

        List<FixacaoReparteDTO> fixacoesPorProduto = fixacaoReparteRepository.obterFixacoesRepartePorProduto(filtroConsultaFixacaoProdutoDTO);
        
        return fixacoesPorProduto;
	}

	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(
			FiltroConsultaFixacaoCotaDTO filtroConsultaFixacaoCotaDTO) {
		List<FixacaoReparteDTO> listaFixacaoReparteDTO = new ArrayList<FixacaoReparteDTO>();
		String codigoCotaBusca = filtroConsultaFixacaoCotaDTO.getCota();
		
		if(StringUtils.isNotEmpty(codigoCotaBusca)){
			listaFixacaoReparteDTO = fixacaoReparteRepository.obterFixacoesRepartePorCota(filtroConsultaFixacaoCotaDTO);
		}
		return listaFixacaoReparteDTO;
	}

	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorProduto(FiltroConsultaFixacaoProdutoDTO filtroProduto) {
		List<FixacaoReparteDTO> resultado = null;
		
		if(filtroProduto != null && filtroProduto.getCodigoProduto()!=null){
			Produto produto = produtoService.obterProdutoPorCodigo(filtroProduto.getCodigoProduto());
			resultado = estoqueProdutoCotaRepository.obterHistoricoEdicaoPorProduto(produto, filtroProduto.getClassificacaoProduto(), filtroProduto.getCota());	
		 }
		
	return resultado; 
	}
	
	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorCota(
			FiltroConsultaFixacaoCotaDTO filtroCota) {
		Cota cota = cotaRepository.obterPorNumeroDaCota(Integer.valueOf(filtroCota.getCota()));
		List<FixacaoReparteDTO> resultado = estoqueProdutoCotaRepository.obterHistoricoEdicaoPorCota(cota, filtroCota.getCodigoProduto(), filtroCota.getClassificacaoProduto()) ;
		return resultado; 
	}
	
	@Override
	@Transactional
	public FixacaoReparte adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = getFixacaoRepartePorDTO(fixacaoReparteDTO);
		
		fixacaoReparte.setDataFixa(distribuidorService.obterDataOperacaoDistribuidor());
		
		if(fixacaoReparte.getId() != null) {
            String descricao = "-TODAS-";
            if (fixacaoReparte.getClassificacaoProdutoEdicao() != null) {
                descricao = fixacaoReparte.getClassificacaoProdutoEdicao().getDescricao();
            }
            throw new ValidacaoException(TipoMensagem.WARNING, "Já existe fixação para esta cota["
                    +
                    fixacaoReparteDTO.getCotaFixadaString() +
                    "], produto[" + fixacaoReparteDTO.getProdutoFixado()
                    + "] e classificação[" +
                    descricao + "]");
        }

        fixacaoReparteRepository.adicionar(fixacaoReparte);
		
		return fixacaoReparte;
	}
	
	@Override
	@Transactional
	public List<PdvDTO> obterListaPdvPorFixacao(Long id) {
		FixacaoReparte fixacaoReparte = this.obterFixacao(id);
		FiltroPdvDTO filtroPdvDTO = new FiltroPdvDTO();
		filtroPdvDTO.setIdCota(fixacaoReparte.getCotaFixada().getId());
        List<PdvDTO> pdvDTOList = pdvRepository.obterPDVsPorCota(filtroPdvDTO);

        for (PdvDTO pdvDTO : pdvDTOList) {
            FixacaoRepartePdv fixacaoRepartePdv = fixacaoRepartePdvRepository.obterPorFixacaoReparteEPdv(fixacaoReparte, pdvRepository.buscarPorId(pdvDTO.getId()));
            if (fixacaoRepartePdv != null) {
                pdvDTO.setReparte(fixacaoRepartePdv.getRepartePdv());
            }
        }

        return pdvDTOList;
	}
	
	@Override
	@Transactional
	public void removerFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorId(fixacaoReparteDTO.getId());
		removerFixacaoReparte(fixacaoReparte);
	}

	private void removerFixacaoReparte(FixacaoReparte fixacaoReparte) {
		List<FixacaoRepartePdv> repartes = fixacaoRepartePdvRepository.obterFixacaoRepartePdvPorFixacaoReparte(fixacaoReparte);
		
		for (FixacaoRepartePdv fixacaoRepartePdv : repartes) {
			fixacaoRepartePdvRepository.remover(fixacaoRepartePdv);
		}
		fixacaoReparteRepository.remover(fixacaoReparte);
	}
	
	@Transactional
	private FixacaoReparte getFixacaoRepartePorDTO(FixacaoReparteDTO fixacaoReparteDTO) {
		
		FixacaoReparte fixacaoReparte;
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(fixacaoReparteDTO.getCotaFixada().intValue());
		
		Produto produto = produtoService.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado());
        
		TipoClassificacaoProduto classificacaoProduto = tipoClassificacaoProdutoRepository.obterPorClassificacao(fixacaoReparteDTO.getClassificacaoProduto());
		
		

        // Esta validação nao faz mais sentido como está... talvez varrer todas
        // as possiveis edições involvidas?
		validaStatusProduto(fixacaoReparteDTO, produto);
		
		// obter o id da base
		fixacaoReparte = fixacaoReparteRepository.buscarPorProdutoCotaClassificacao(cota, produto.getCodigoICD(), classificacaoProduto);
		if(fixacaoReparte == null){
			fixacaoReparte  = new FixacaoReparte();
		}

		Usuario usuario = usuarioService.getUsuarioLogado();
		fixacaoReparte.setUsuario(usuario);
		fixacaoReparte.setCotaFixada(cota);
		fixacaoReparte.setDataHora(new Date());
		fixacaoReparte.setCodigoICD(produto.getCodigoICD());
		fixacaoReparte.setQtdeEdicoes(fixacaoReparteDTO.getQtdeEdicoes());
		fixacaoReparte.setQtdeExemplares(fixacaoReparteDTO.getQtdeExemplares());
		fixacaoReparte.setQtdeEdicoes(fixacaoReparteDTO.getQtdeEdicoes());
		fixacaoReparte.setEdicaoInicial(fixacaoReparteDTO.getEdicaoInicial());
		fixacaoReparte.setEdicaoFinal(fixacaoReparteDTO.getEdicaoFinal());
        
        if (classificacaoProduto != null) 
            fixacaoReparte.setClassificacaoProdutoEdicao(classificacaoProduto);        

		return fixacaoReparte;
	}

	private void validaStatusProduto(FixacaoReparteDTO fixacaoReparteDTO, Produto produto) {

	    if (fixacaoReparteDTO.getEdicaoInicial() != null && fixacaoReparteDTO.getEdicaoFinal() != null) {
		List<ProdutoEdicao> listProdutoEdicao = produtoEdicaoRepository.listProdutoEdicaoPorCodProdutoNumEdicoes(produto.getCodigo(), fixacaoReparteDTO.getEdicaoInicial().longValue(), fixacaoReparteDTO.getEdicaoFinal().longValue());
		for (ProdutoEdicao produtoEdicao : listProdutoEdicao) {
		    if (produtoEdicao.getLancamentos().size() > 0) {
			    statusPermitido(new ArrayList<>(produtoEdicao.getLancamentos()).get(0).getStatus());
		    }
		}
	    }

	}

	private void statusPermitido(StatusLancamento status) {
		switch (status) {
		
		case PLANEJADO:
			break;
		case CONFIRMADO:
			break;
		case EM_BALANCEAMENTO:
			break;
		case BALANCEADO:
			break;
		case FURO:
			break;

		case EXPEDIDO:
		case CANCELADO:
		case ESTUDO_FECHADO:
		case EM_BALANCEAMENTO_RECOLHIMENTO:
		case BALANCEADO_RECOLHIMENTO:
		case EM_RECOLHIMENTO:
		case RECOLHIDO:
		case FECHADO:
			throw new ValidacaoException(TipoMensagem.WARNING, "Não permitido a fixação devido ao status das edições.");
			
		default:
			break;
		}
	}
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> obterClassificacoesProduto(){
		return tipoClassificacaoProdutoRepository.obterTodos();
	}
	
	@Override
	@Transactional
	public FixacaoReparte obterFixacao(Long id) {
		return fixacaoReparteRepository.buscarPorId(id); 
	}
	
	@Override
	@Transactional
	public FixacaoReparteDTO obterFixacaoDTO(Long id){
		FixacaoReparte fixacaoReparte = obterFixacao(id);
		return getFixacaoRepartePorDTO(fixacaoReparte);
		
	}
	
	private FixacaoReparteDTO getFixacaoRepartePorDTO(FixacaoReparte fixacaoReparte) {
		FixacaoReparteDTO fixacaoReparteDTO = new FixacaoReparteDTO();
        if (fixacaoReparte != null) {
            fixacaoReparteDTO.setQtdeEdicoes(fixacaoReparte.getQtdeEdicoes());
            fixacaoReparteDTO.setQtdeExemplares(fixacaoReparte.getQtdeExemplares());
            fixacaoReparteDTO.setId(fixacaoReparte.getId());
            if (fixacaoReparte.getEdicaoInicial() != null) {
                fixacaoReparteDTO.setEdicaoInicial(fixacaoReparte.getEdicaoInicial());
            }
            if (fixacaoReparte.getEdicaoFinal() != null) {
                fixacaoReparteDTO.setEdicaoFinal((fixacaoReparte.getEdicaoFinal()));
            }
            if (fixacaoReparte.getCotaFixada() != null) {
                fixacaoReparteDTO.setCotaFixada(fixacaoReparte.getCotaFixada().getNumeroCota());
                fixacaoReparteDTO.setNomeCota(fixacaoReparte.getCotaFixada().getPessoa().getNome());
            }
            if (fixacaoReparte.getCodigoICD() != null) {
                fixacaoReparteDTO.setCodigoProduto(fixacaoReparte.getCodigoICD());
                Produto produto = produtoService.obterProdutoPorCodigo(fixacaoReparte.getCodigoICD());
                fixacaoReparteDTO.setNomeProduto(produto.getNome());
            }
            if (fixacaoReparte.getClassificacaoProdutoEdicao() != null) {
                fixacaoReparteDTO.setClassificacaoProduto(fixacaoReparte.getClassificacaoProdutoEdicao().getDescricao());
            }
        }
        return fixacaoReparteDTO;
	}

	@Transactional
	@Override
	public boolean isCotaPossuiVariosPdvs(Long idFixacao) {
		FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorId(idFixacao);
		if(fixacaoReparte.getCotaFixada() !=null){
			Long id = fixacaoReparte.getCotaFixada().getId();
			int size = pdvRepository.obterPdvPorCotaComEndereco(id).size();
			return size > 1;
		}else{
			return false;
		}
	}
	
	@Transactional
	@Override
	public void excluirFixacaoPorCota(Long idCota){
		Cota cota = cotaRepository.buscarCotaPorID(idCota);
		List<FixacaoReparte> fixacaoRepartes = fixacaoReparteRepository.buscarPorCota(cota);

		for(FixacaoReparte fr : fixacaoRepartes ){
			fixacaoRepartePdvRepository.removerFixacaoReparte(fr);
		}
		fixacaoReparteRepository.removerPorCota(cota);
	}

	@Transactional
	@Override
	public boolean isFixacaoExistente(FixacaoReparteDTO fixacaoReparteDTO) {
        Produto produto = produtoService.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado());
       /*
        if (StringUtils.isBlank(produto.getCodigoICD())) {
            return true; // Não deixar fixar sem um codigo ICD cadastrado.
        }
        */
        fixacaoReparteDTO.setProdutoFixado(produto.getCodigoICD());
        return fixacaoReparteRepository.isFixacaoExistente(fixacaoReparteDTO);
    }

	@Transactional
	@Override
	public boolean isCotaValida(FixacaoReparteDTO fixacaoReparteDTO) {
		Cota cota = cotaRepository.obterPorNumeroDaCota(fixacaoReparteDTO.getCotaFixada());
		return (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO) 
				|| cota.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO))
				&& cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.CONVENCIONAL);
	}
	
	@Transactional
	@Override
	public boolean gerarCopiafixacao(CopiaMixFixacaoDTO copiaDTO) {

		switch (copiaDTO.getTipoCopia()) {
		case COTA:

			FiltroConsultaFixacaoCotaDTO filtroConsultaFixacaoCotaDTO = new FiltroConsultaFixacaoCotaDTO();
			filtroConsultaFixacaoCotaDTO.setCota(copiaDTO.getCotaNumeroOrigem().toString());
			
			FiltroConsultaFixacaoCotaDTO cotaDestinofiltroConsultaFixacaoDTO = new FiltroConsultaFixacaoCotaDTO();
			cotaDestinofiltroConsultaFixacaoDTO.setCota(copiaDTO.getCotaNumeroDestino().toString());
			
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaDTO.getCotaNumeroDestino());
			
			List<FixacaoReparteDTO> fixacaoCotaOrigemList = fixacaoReparteRepository.obterFixacoesRepartePorCota(filtroConsultaFixacaoCotaDTO);
			
			List<FixacaoReparteDTO> fixacaoCotaDestinoList = fixacaoReparteRepository.obterFixacoesRepartePorCota(cotaDestinofiltroConsultaFixacaoDTO);
			
			List<FixacaoReparteDTO> fixacoesInvalidas = new ArrayList<>();
			
			if(fixacaoCotaDestinoList != null){
				for (FixacaoReparteDTO origemFixacaoReparteDTO : fixacaoCotaOrigemList) {
					for (FixacaoReparteDTO destinoFixacaoReparteDTO : fixacaoCotaDestinoList) {
						if(!produtoService.isIcdValido(origemFixacaoReparteDTO.getCodigoProduto())){
							fixacoesInvalidas.add(origemFixacaoReparteDTO);
							continue;
						}
						
						if(destinoFixacaoReparteDTO.getCodigoProduto() != null &&
						   origemFixacaoReparteDTO.getCodigoProduto().equals(destinoFixacaoReparteDTO.getCodigoProduto()) && 
						   origemFixacaoReparteDTO.getClassificacaoProdutoId().compareTo(destinoFixacaoReparteDTO.getClassificacaoProdutoId()) == 0){
							fixacoesInvalidas.add(origemFixacaoReparteDTO);
						}
					}
				}
				fixacaoCotaOrigemList.removeAll(fixacoesInvalidas);
				fixacoesInvalidas.clear();
			}
			
			if(fixacaoCotaOrigemList==null || fixacaoCotaOrigemList.isEmpty()){
                throw new ValidacaoException(TipoMensagem.WARNING, "Não há Fixação de Reparte válida para cópia.");
			}
			
			for (FixacaoReparteDTO fixacaoReparteDTO : fixacaoCotaOrigemList) {
				fixacaoReparteDTO.setCotaFixadaId(cotaDestino.getId());
			}
					
			try {
				fixacaoReparteRepository.gerarCopiaPorCotaFixacaoReparte(fixacaoCotaOrigemList,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return false;
			}
			break;
		case PRODUTO:

			Produto produtoDestino = produtoService.obterProdutoPorCodigo(copiaDTO.getCodigoProdutoDestino());
			Produto produtoOrigem = produtoService.obterProdutoPorCodigo(copiaDTO.getCodigoProdutoOrigem());

			FiltroConsultaFixacaoProdutoDTO filtroConsultaFixacaoProdutoDTO = new FiltroConsultaFixacaoProdutoDTO();

			filtroConsultaFixacaoProdutoDTO.setCodigoProduto(produtoOrigem.getCodigoICD());
            filtroConsultaFixacaoProdutoDTO.setClassificacaoProduto(copiaDTO.getClassificacaoProduto());

            
            FiltroConsultaFixacaoProdutoDTO filtroProdutoDestino = new FiltroConsultaFixacaoProdutoDTO();
            
            filtroProdutoDestino.setCodigoProduto(produtoDestino.getCodigoICD());
            filtroProdutoDestino.setClassificacaoProduto(copiaDTO.getClassificacaoProduto());
            
			List<FixacaoReparteDTO> listFixacaoReparteOrigemDTO = fixacaoReparteRepository.obterFixacoesRepartePorProduto(filtroConsultaFixacaoProdutoDTO);
			
			List<FixacaoReparteDTO> listFixacaoReparteDestino = fixacaoReparteRepository.obterFixacoesRepartePorProduto(filtroProdutoDestino);
			
			List<FixacaoReparteDTO> fixacoesProdutoInvalidas = new ArrayList<>();
			
			
			if(listFixacaoReparteOrigemDTO==null || listFixacaoReparteOrigemDTO.isEmpty()){
                throw new ValidacaoException(TipoMensagem.WARNING, "Não há Fixação de Reparte válida para cópia.");
			}
			
			
			if(listFixacaoReparteDestino != null){
				for (FixacaoReparteDTO origemFixacaoProduto : listFixacaoReparteOrigemDTO) {
					for (FixacaoReparteDTO destinoFixacaoProduto : listFixacaoReparteDestino) {
						
						if(!produtoService.isIcdValido(origemFixacaoProduto.getCodigoProduto())){
							fixacoesProdutoInvalidas.add(origemFixacaoProduto);
							continue;
						}
						
						if(destinoFixacaoProduto.getCodigoProduto() != null &&
						   destinoFixacaoProduto.getCotaFixadaId(). compareTo(origemFixacaoProduto.getCotaFixadaId()) == 0 &&
						   destinoFixacaoProduto.getClassificacaoProdutoId().compareTo(origemFixacaoProduto.getClassificacaoProdutoId()) == 0){
							fixacoesProdutoInvalidas.add(origemFixacaoProduto);
						}
					}
				}
				
				listFixacaoReparteOrigemDTO.removeAll(fixacoesProdutoInvalidas);
				fixacoesProdutoInvalidas.clear();
			
			}
			
			if(listFixacaoReparteOrigemDTO.isEmpty()){
                throw new ValidacaoException(TipoMensagem.WARNING, "Não há Fixação de Reparte válida para cópia.");
			}
			
			for (FixacaoReparteDTO fixacaoReparteDTO : listFixacaoReparteOrigemDTO) {
				fixacaoReparteDTO.setCodigoProduto(produtoDestino.getCodigoICD());
			}
			
			try {
				fixacaoReparteRepository.gerarCopiaPorProdutoFixacaoReparte(listFixacaoReparteOrigemDTO,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return false;
			}
			
			break;

		default:
			break;
		}
		return true;
	
	}
	
	@Transactional
	@Override
	public void verificarFixacao (Date dataOperacaoAserFechada){
		
		List<BigInteger> listaLancamentosDoDia = fixacaoReparteRepository.obterListaLancamentos(dataOperacaoAserFechada);
		
		if(listaLancamentosDoDia != null && !listaLancamentosDoDia.isEmpty()){
			this.atualizaFixacao(listaLancamentosDoDia);
		}
		
	}
	
	@Transactional
	@Override
	public void atualizaFixacao (List<BigInteger> lancamentosDoDia){ 
		
		List<verificadorFixacaoDTO> lcmtsDosProdutosQuePossueFixacao = fixacaoReparteRepository.obterLcmtsDosProdutosQuePossueFixacao(lancamentosDoDia);
		
		List<FixacaoReparte> fixacaoParaExclusao = new ArrayList<>();
		
		if(lcmtsDosProdutosQuePossueFixacao != null && !lcmtsDosProdutosQuePossueFixacao.isEmpty()){
			
			List<String> codigosICD = new ArrayList<>();
			
			for (verificadorFixacaoDTO lancamentosEICD : lcmtsDosProdutosQuePossueFixacao) {
				codigosICD.add(lancamentosEICD.getCodICDFixacao());
			}
			
			if(!codigosICD.isEmpty()){
				
				List<FixacaoReparte> fixacoesParaOsProdutosDosLancamentos = fixacaoReparteRepository.obterFixacoesParaOsProdutosDosLancamentos(codigosICD);
				
				if(!fixacoesParaOsProdutosDosLancamentos.isEmpty()){
					
					for(verificadorFixacaoDTO verificadorLcmtXFixacao : lcmtsDosProdutosQuePossueFixacao){
						
						for (FixacaoReparte fixacao : fixacoesParaOsProdutosDosLancamentos) {
							
							if (verificadorLcmtXFixacao.getCodICDFixacao().equalsIgnoreCase(fixacao.getCodigoICD())){
								
								if((fixacao.getEdicaoInicial()!=null && fixacao.getEdicaoInicial().intValue() > 0)&&(fixacao.getEdicaoFinal()!=null && fixacao.getEdicaoFinal().intValue() > 0)){
									
									BigInteger numeroEdicaoPeloLancamentoID = fixacaoReparteRepository.obterNumeroEdicaoPeloLancamentoID(verificadorLcmtXFixacao.getIdLancamento().longValue());

									if((numeroEdicaoPeloLancamentoID.intValue() == fixacao.getEdicaoFinal().longValue()) && (!fixacao.isManterFixa())){
										if(!fixacaoParaExclusao.contains(fixacao)){
											fixacaoParaExclusao.add(fixacao);
										}
										
									}else{

										BigInteger qtdEdicoesAtendidas = fixacaoReparteRepository.obterQtdDeEdicoesNoRanger(verificadorLcmtXFixacao.getCodICDFixacao(), fixacao.getEdicaoInicial(), fixacao.getEdicaoFinal());
										
										fixacao.setEdicoesAtendidas(qtdEdicoesAtendidas.intValue());
										fixacaoReparteRepository.alterar(fixacao);
										
									}
									
								}else{
									
									BigInteger qtdEdicoesPosteriores = fixacaoReparteRepository.qntdEdicoesPosterioresAolancamento(verificadorLcmtXFixacao.getCodICDFixacao(), fixacao.getDataFixa());
									
									if(qtdEdicoesPosteriores.intValue() < fixacao.getQtdeEdicoes()){
										fixacao.setEdicoesAtendidas(qtdEdicoesPosteriores.intValue());
										fixacaoReparteRepository.alterar(fixacao);
									}else{
										if(!fixacao.isManterFixa()){
	
											if(!fixacaoParaExclusao.contains(fixacao)){
												fixacaoParaExclusao.add(fixacao);
											}
											
										}else{
											fixacao.setEdicoesAtendidas(fixacao.getQtdeEdicoes());
											fixacaoReparteRepository.alterar(fixacao);
										}
									}
								}
							}
						}
					}
					
					for (FixacaoReparte fixacaoReparte : fixacaoParaExclusao) {
						fixacaoReparteRepository.remover(fixacaoReparte);
					}
				}
			}
		}
	}

	@Transactional
	@Override
	public FixacaoReparte buscarPorProdutoCotaClassificacao(Cota cota, String codigoICD, TipoClassificacaoProduto tipoClassificacaoProduto) {
		return fixacaoReparteRepository.buscarPorProdutoCotaClassificacao(cota, codigoICD, tipoClassificacaoProduto);
	}
	
}
