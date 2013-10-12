package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.PdvDTO;
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
import br.com.abril.nds.model.cadastro.pdv.PDV;
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
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;

@Service
public class FixacaoReparteServiceImpl implements FixacaoReparteService {

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
	
	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO filtroConsultaFixacaoProdutoDTO) {
		
		Produto produto = produtoService.obterProdutoPorCodigo(filtroConsultaFixacaoProdutoDTO.getCodigoProduto());
        filtroConsultaFixacaoProdutoDTO.setCodigoProduto(produto.getCodigoICD());

        return fixacaoReparteRepository.obterFixacoesRepartePorProduto(filtroConsultaFixacaoProdutoDTO);
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
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorProduto(
			FiltroConsultaFixacaoProdutoDTO filtroProduto) {
		List<FixacaoReparteDTO> resultado = null;
		if(filtroProduto != null && filtroProduto.getCodigoProduto()!=null){
			Produto produto = produtoRepository.obterProdutoPorCodigoProdin(filtroProduto.getCodigoProduto());
			 resultado = estoqueProdutoCotaRepository.obterHistoricoEdicaoPorProduto(produto) ;
		}
		return resultado; 
	}
	
	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorCota(
			FiltroConsultaFixacaoCotaDTO filtroCota) {
		Cota cota = cotaRepository.obterPorNumeroDaCota(new Integer(filtroCota.getCota()));
		List<FixacaoReparteDTO> resultado = estoqueProdutoCotaRepository.obterHistoricoEdicaoPorCota(cota, filtroCota.getCodigoProduto()) ;
		return resultado; 
	}
	
	@Override
	@Transactional
	public FixacaoReparte adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = getFixacaoRepartePorDTO(fixacaoReparteDTO);
		
		if(fixacaoReparte.getId() != null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Já existe fixação para esta cota[" +
					fixacaoReparteDTO.getCotaFixadaString() +
					"] e produto[" + fixacaoReparteDTO.getProdutoFixado() + "].");
		}
		
		fixacaoReparteRepository.adicionar(fixacaoReparte);
		
		fixarTudoNoPDVPrincipal(fixacaoReparte);
		
		return fixacaoReparte;
	}

	private void fixarTudoNoPDVPrincipal(FixacaoReparte fixacaoReparte) {
		List<PDV> pdvs = fixacaoReparte.getCotaFixada().getPdvs();
		
		if (pdvs != null && pdvs.size() > 0) {
			PDV pdv = pdvs.get(0);
            FixacaoRepartePdv fixacaoRepartePdv = fixacaoRepartePdvRepository.obterPorFixacaoReparteEPdv(fixacaoReparte, pdv);
			if(fixacaoRepartePdv == null) {
				fixacaoRepartePdv = new FixacaoRepartePdv();
			}
			
			fixacaoRepartePdv.setFixacaoReparte(fixacaoReparte);
			fixacaoRepartePdv.setPdv(pdv);
			fixacaoRepartePdv.setRepartePdv(fixacaoReparte.getQtdeExemplares());

            fixacaoRepartePdvRepository.merge(fixacaoRepartePdv);
		}
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
		List<FixacaoRepartePdv> repartes = fixacaoRepartePdvRepository.obterFixacaoRepartePdvPorFixacaoReparte(fixacaoReparte);
		
		for (FixacaoRepartePdv fixacaoRepartePdv : repartes) {
			fixacaoRepartePdvRepository.remover(fixacaoRepartePdv);
		}
		fixacaoReparteRepository.remover(fixacaoReparte);
	}
	
	private FixacaoReparte getFixacaoRepartePorDTO(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte;
		Cota cota = cotaRepository.obterPorNumeroDaCota(fixacaoReparteDTO.getCotaFixada().intValue());
		Produto produto = produtoService.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado());

        // Esta validação nao faz mais sentido como está... talvez varrer todas as possiveis edições involvidas?
		validaStatusProduto(fixacaoReparteDTO, produto);
		
		// obter o id da base
		fixacaoReparte = fixacaoReparteRepository.buscarPorProdutoCota(cota, produto.getCodigoICD());
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

        Long classificacaoProdutoId = NumberUtils.toLong(fixacaoReparteDTO.getClassificacaoProduto());
        if (classificacaoProdutoId.compareTo(NumberUtils.LONG_ZERO) > 0) {
            TipoClassificacaoProduto tipoClassificacaoProduto = tipoClassificacaoProdutoRepository.buscarPorId(classificacaoProdutoId);
            fixacaoReparte.setClassificacaoProdutoEdicao(tipoClassificacaoProduto);
        }

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
		
		case CONFIRMADO:
		case FECHADO:
			throw new ValidacaoException(TipoMensagem.WARNING,"Não permitido a fixação devido ao status das edições.");
			
		case BALANCEADO:
		case BALANCEADO_RECOLHIMENTO:
		case CANCELADO:
		case EM_BALANCEAMENTO:
		case EM_BALANCEAMENTO_RECOLHIMENTO:
		case ESTUDO_FECHADO:
		case EXCLUIDO_RECOLHIMENTO:
		case EXPEDIDO:
		case FURO:
		case PLANEJADO:
		case RECOLHIDO:
			
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
        if (StringUtils.isBlank(produto.getCodigoICD())) {
            return true; //Não deixar fixar sem um codigo ICD cadastrado.
        }
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

			FiltroConsultaFixacaoCotaDTO fMixCota = new FiltroConsultaFixacaoCotaDTO();
			fMixCota.setCota(copiaDTO.getCotaNumeroOrigem().toString());
			
	
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaDTO.getCotaNumeroDestino());
			
			List<FixacaoReparteDTO> mixCotaOrigem = fixacaoReparteRepository.obterFixacoesRepartePorCota(fMixCota);
			if(mixCotaOrigem==null || mixCotaOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Fixação de Reparte não encontrada para cópia.");
			}
			for (FixacaoReparteDTO mixCotaDTO : mixCotaOrigem) {
				mixCotaDTO.setCotaFixadaId(cotaDestino.getId());
			}
					
			try {
				fixacaoReparteRepository.gerarCopiaPorCotaFixacaoReparte(mixCotaOrigem,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			break;
		case PRODUTO:
			
			FiltroConsultaFixacaoProdutoDTO fMixProduto = new FiltroConsultaFixacaoProdutoDTO();
			
			Produto produtoDestino = produtoService.obterProdutoPorCodigo(copiaDTO.getCodigoProdutoDestino());
			fMixProduto.setCodigoProduto(copiaDTO.getCodigoProdutoOrigem());
			
			List<FixacaoReparteDTO> listFixacaoReparteOrigemDTO = fixacaoReparteRepository.obterFixacoesRepartePorProduto(fMixProduto);
			
			if(listFixacaoReparteOrigemDTO==null || listFixacaoReparteOrigemDTO.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Fixação de Reparte não encontrada para cópia.");
			}
			
			for (FixacaoReparteDTO fixacaoReparteDTO : listFixacaoReparteOrigemDTO) {
				fixacaoReparteDTO.setCodigoProduto(produtoDestino.getCodigoICD());
			}
			
			try {
				fixacaoReparteRepository.gerarCopiaPorProdutoFixacaoReparte(listFixacaoReparteOrigemDTO,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			break;

		default:
			break;
		}
		return true;
	
	}

}
