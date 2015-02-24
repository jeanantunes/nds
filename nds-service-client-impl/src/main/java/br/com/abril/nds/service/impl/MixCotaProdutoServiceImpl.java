package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.ClassificacaoNaoRecebidaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MixCotaProdutoServiceImpl implements MixCotaProdutoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MixCotaProdutoServiceImpl.class);

	@Autowired
	private MixCotaProdutoRepository mixCotaProdutoRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CotaRepository cotaRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProdutoRepository;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private FixacaoRepartePdvRepository fixacaoRepartePdvRepository;
	
	@Autowired
	private RepartePDVRepository repartePDVRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ClassificacaoNaoRecebidaService classificacaoNaoRecebidaService;
	
	@Autowired
	private SegmentoNaoRecebidoService segmentoNaoRecebidoService;
	
	@Autowired
	private ExcecaoSegmentoParciaisService excecaoSegmentoParciaisService;
	
	@Override
	@Transactional(readOnly = true)
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		
		Cota cota = cotaService.obterPorNumeroDaCota(filtroConsultaMixCotaDTO.getCota());
		
		if(cota==null)
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Digite uma cota válida."));
		
		boolean tipoAlternativo = cota.getTipoDistribuicaoCota() != null && cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO);
		
		if (!tipoAlternativo) {
			
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota não é do tipo Alternativo."));
		}
		
		filtroConsultaMixCotaDTO.setCotaId(cota.getId());
		
		long time = System.currentTimeMillis();
		
		List<MixCotaDTO> list =  mixCotaProdutoRepository.pesquisarPorCota(filtroConsultaMixCotaDTO);
		
		time = (System.currentTimeMillis() - time);
		
		System.out.println("TEMPO 1:"+time);
		
		return list;
	}

	@Transactional
	@Override
	public void removerMixCotaProduto(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(filtroConsultaMixCotaDTO.getId());
		mixCotaProdutoRepository.remover(mixCotaProduto);
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO) {
		return mixCotaProdutoRepository.pesquisarPorProduto(filtroConsultaMixProdutoDTO);
	}
	
	@Override
	@Transactional(readOnly=true)
	public MixCotaProdutoDTO obterPorId(Long id){
		return getMixCotaProdutoDTO(mixCotaProdutoRepository.buscarPorId(id));
	}
	
	private MixCotaProdutoDTO getMixCotaProdutoDTO(MixCotaProduto mixCotaProduto) {
		MixCotaProdutoDTO mixCotaProdutoDTO = new MixCotaProdutoDTO();
		mixCotaProdutoDTO.setId(mixCotaProduto.getId());
		mixCotaProdutoDTO.setReparteMaximo(mixCotaProduto.getReparteMaximo());
		mixCotaProdutoDTO.setReparteMedio(mixCotaProduto.getReparteMedio());
		mixCotaProdutoDTO.setReparteMinimo(mixCotaProduto.getReparteMinimo());
		
		if(mixCotaProduto.getCota()!=null){
			mixCotaProdutoDTO.setCotaId(mixCotaProduto.getCota().getId());
			if((mixCotaProduto.getCota().getPessoa()!=null)){
				mixCotaProdutoDTO.setNomeCota(mixCotaProduto.getCota().getPessoa().getNome());
				mixCotaProdutoDTO.setNumeroCota(mixCotaProduto.getCota().getNumeroCota().toString());
			}
		}
		
		Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProduto.getCodigoICD());
		
		mixCotaProdutoDTO.setNomeProduto(produto.getNomeComercial());
		mixCotaProdutoDTO.setCodigoProduto(mixCotaProduto.getCodigoICD());
		mixCotaProdutoDTO.setClassificacaoProduto(mixCotaProduto.getTipoClassificacaoProduto().getDescricao());
		
		if(mixCotaProduto.getRepartesPDV()!=null){
			for(RepartePDV repartePDV: mixCotaProduto.getRepartesPDV()){
				RepartePDVDTO repartePDVDTO = new RepartePDVDTO();
				repartePDVDTO.setReparte(repartePDV.getReparte());
			}
		}
		return mixCotaProdutoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public List<PdvDTO> obterListaPdvPorMix(Long id) {
		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(id);
		FiltroPdvDTO filtroPdvDTO = new FiltroPdvDTO();
		filtroPdvDTO.setIdCota(mixCotaProduto.getCota().getId());
		List <PdvDTO> listPDVs = pdvRepository.obterPdvPorCotaComEndereco(filtroPdvDTO.getIdCota());
		return listPDVs; 
	}

	@Override
	@Transactional
	public void excluirTodos() {
		mixCotaProdutoRepository.excluirTodos();
		
	}

	@Override
	@Transactional
	public void excluirMixPorCota(Long idCota){
		
		List<RepartePDV> repartesPDV = this.repartePDVRepository.buscarPorCota(idCota);
		
		for (RepartePDV repartePDV : repartesPDV) {
			
			this.repartePDVRepository.remover(repartePDV);
		}
		
		this.mixCotaProdutoRepository.removerPorIdCota(idCota);
	}
	
	
	@Override
	@Transactional
	public List<String> adicionarListaMixPorCota(List<MixCotaProdutoDTO> listaMixCota , Integer cotaId) {
		
		Cota cota = cotaService.obterPorNumeroDaCota(cotaId);
		
		if (cota == null) {
			
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota [" + cotaId + "] não existe."));
		}
		
		List<String> mensagens = obterValidacaoLista(listaMixCota);
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		List<TipoClassificacaoProduto> obterTodos = this.tipoClassificacaoProdutoService.obterTodos();
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
				
				if (!mixCotaProdutoDTO.isItemValido()) {
					
					continue;
				}
			
//				Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
				MixCotaProduto mixCotaProduto = new MixCotaProduto();

            // FIXME refazer... a classificação fica no ProdutoEdicao
//				if(produto.getTipoClassificacaoProduto().getDescricao().equalsIgnoreCase(mixCotaProdutoDTO.getClassificacaoProduto())){
					
					for (TipoClassificacaoProduto tcp : obterTodos) {
						if(tcp.getDescricao().equalsIgnoreCase(mixCotaProdutoDTO.getClassificacaoProduto())){
							mixCotaProduto.setTipoClassificacaoProduto(tcp);
							break;
						}
					}
					mixCotaProduto.setCodigoICD(mixCotaProdutoDTO.getCodigoICD());
					mixCotaProduto.setCota(cota);
					mixCotaProduto.setDataHora(new Date());
					mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
					mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
					mixCotaProduto.setUsuario(usuario);

					mixCotaProdutoRepository.adicionar(mixCotaProduto);

//				}
				
		}
		
		
		return mensagens;
	}

	@Override
	@Transactional
	public String obterValidacaoLinha(MixCotaProdutoDTO mixCotaProdutoDTO) {
		
		String msgValidacao = obterValidacaoPreenchimentoMix(mixCotaProdutoDTO);
		
		if (!StringUtils.isEmpty(msgValidacao)) {
			
			return msgValidacao;
		}
		
		Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
		
		if (cota == null) {
			
            return "Cota [" + mixCotaProdutoDTO.getNumeroCota() + "] não existe.";
		}
		
		msgValidacao = obterValidacaoCota(cota);
		
		List<ClassificacaoNaoRecebidaDTO> classificacoesNaoRecebidasPelaCotaList = this.classificacaoNaoRecebidaService.obterClassificacoesNaoRecebidasPelaCota(cota);
		
		if(classificacoesNaoRecebidasPelaCotaList!=null){
			for (ClassificacaoNaoRecebidaDTO classificacaoNaoRecebidaDTO : classificacoesNaoRecebidasPelaCotaList) {
				if(classificacaoNaoRecebidaDTO.getNomeClassificacao().equals(mixCotaProdutoDTO.getClassificacaoProduto()))
                    return "Cota de número " + cota.getNumeroCota() + " não recebe classificação do tipo "
                            + mixCotaProdutoDTO.getClassificacaoProduto();
			}
		}
		
		List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota = segmentoNaoRecebidoService.obterSegmentosNaoRecebidosCadastradosNaCota(cota);
		
		FiltroExcecaoSegmentoParciaisDTO filtroExcecaoSeg = new FiltroExcecaoSegmentoParciaisDTO() ;
		CotaDTO cotadto = new CotaDTO();
		cotadto.setNumeroCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
		filtroExcecaoSeg.setExcecaoSegmento(true);
		filtroExcecaoSeg.setCotaDto(cotadto);
		 
		List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCotaList = this.excecaoSegmentoParciaisService.obterProdutosRecebidosPelaCota(filtroExcecaoSeg);
		
		Produto prd = this.produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
		TipoSegmentoProduto tipoSegProd = prd.getTipoSegmentoProduto();
		
		if(!produtoService.isIcdValido(prd.getCodigoICD())){
			return "Produto ["+prd.getNomeComercial()+"]: Código ICD inválido, ajuste-o no Cadastro de Produto.";
		}
		
		loopSeg:for (SegmentoNaoRecebeCotaDTO seg : obterSegmentosNaoRecebidosCadastradosNaCota) {
			if(seg.getNomeSegmento().equals(tipoSegProd.getDescricao())){
				
				for (ProdutoRecebidoDTO prodRecebidoDTO : obterProdutosRecebidosPelaCotaList) {
					if(prodRecebidoDTO.getCodigoProduto().equals(prd.getCodigoICD()))
						continue loopSeg;
				}
				
                return "Cota [" + mixCotaProdutoDTO.getNumeroCota() + "] não recebe segmento "
                        + tipoSegProd.getDescricao() + " do produto " + mixCotaProdutoDTO.getCodigoProduto();

			}
			
		}
		
		if (!StringUtils.isEmpty(msgValidacao)) {
			
			return msgValidacao;
		}
		
		Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
		
		if (produto == null) {
			
            return "Produto [" + mixCotaProdutoDTO.getCodigoProduto() + "] não existe.";
		}
		
		
		MixCotaProduto mix = mixCotaProdutoRepository.obterMixPorCotaICDCLassificacao(cota.getId(),mixCotaProdutoDTO.getCodigoICD(),mixCotaProdutoDTO.getClassificacaoProduto());
		if (mix!=null) {
			
            return "Cota:[" + mixCotaProdutoDTO.getNumeroCota() + "," + mixCotaProdutoDTO.getNomeCota()
                    + "], Produto [" + produto.getCodigo() + ":" + produto.getNome() + "] e Classificação ["
                    + mixCotaProdutoDTO.getClassificacaoProduto() + "] já foi cadastrado.";
					
		}

        // FIXME refazer... a classificação fica no ProdutoEdicao
//		if(!produto.getTipoClassificacaoProduto().getDescricao().equalsIgnoreCase(mixCotaProdutoDTO.getClassificacaoProduto())){
//
        // return "Produto "+produto.getCodigo()+ ":"
        // +produto.getNome()+" com a classificação "+
        // mixCotaProdutoDTO.getClassificacaoProduto() +" não existe.";
//		}
		
		mixCotaProdutoDTO.setItemValido(true);
		
		return msgValidacao;
	}
	
	private String obterValidacaoCota(Cota cota) {
		
		if (!cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO)) {
		

            return "Cota não é do tipo Alternativo: [" + cota.getNumeroCota() + ":" + cota.getPessoa().getNome() + "]";
		}
		
		return null;
	}
	
	private List<String> obterValidacaoLista(List<MixCotaProdutoDTO> mixCotaProdutoDTOList) {
		
		List<String> mensagens = new ArrayList<String>(); 
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO: mixCotaProdutoDTOList) {
			
			String msg = obterValidacaoLinha(mixCotaProdutoDTO);
			
			if (!StringUtils.isEmpty(msg)) {
				mensagens.add(msg);
			}
			
		}
		
		return mensagens;
	}
	
	private List<String> obterValidacaoListaEmLote(List<MixCotaProdutoDTO> mixCotaProdutoDTOList) {
		
		List<String> mensagens = new ArrayList<String>(); 
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO: mixCotaProdutoDTOList) {
			
			String msgValidacao = obterValidacaoPreenchimentoMix(mixCotaProdutoDTO);
			
			if (!StringUtils.isEmpty(msgValidacao)) {
				
				mensagens.add(msgValidacao);
			}
			else {
				
				mixCotaProdutoDTO.setItemValido(true);
			}
			
			Cota cota = cotaService.obterPorNumeroDaCota(Integer.valueOf(mixCotaProdutoDTO.getNumeroCota()));
			List<ClassificacaoNaoRecebidaDTO> classificacoesNaoRecebidasPelaCotaList = this.classificacaoNaoRecebidaService.obterClassificacoesNaoRecebidasPelaCota(cota);
			
			for (ClassificacaoNaoRecebidaDTO classificacaoNaoRecebidaDTO : classificacoesNaoRecebidasPelaCotaList) {
				if(classificacaoNaoRecebidaDTO.getNomeClassificacao().equals(mixCotaProdutoDTO.getClassificacaoProduto())){
                    mensagens.add("Cota de número " + cota.getNumeroCota() + " não recebe classificação do tipo "
                            + mixCotaProdutoDTO.getClassificacaoProduto());
					mixCotaProdutoDTO.setItemValido(false);
					continue;
					
				}
			}
			
			
			List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota = segmentoNaoRecebidoService.obterSegmentosNaoRecebidosCadastradosNaCota(cota);
			
			FiltroExcecaoSegmentoParciaisDTO filtroExcecaoSeg = new FiltroExcecaoSegmentoParciaisDTO() ;
			CotaDTO cotadto = new CotaDTO();
			cotadto.setNumeroCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
			filtroExcecaoSeg.setExcecaoSegmento(true);
			filtroExcecaoSeg.setCotaDto(cotadto);
			
			List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCotaList = this.excecaoSegmentoParciaisService.obterProdutosRecebidosPelaCota(filtroExcecaoSeg);
			
			Produto prd = this.produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoICD());
			TipoSegmentoProduto tipoSegProd = prd.getTipoSegmentoProduto();
			
			if(!produtoService.isIcdValido(prd.getCodigoICD())){
				mensagens.add("Produto ["+prd.getNomeComercial()+"]: Código ICD inválido, ajuste-o no Cadastro de Produto.");
				mixCotaProdutoDTO.setItemValido(false);
				continue;
			}
			
			if(obterSegmentosNaoRecebidosCadastradosNaCota==null || obterSegmentosNaoRecebidosCadastradosNaCota.isEmpty()){
				mixCotaProdutoDTO.setItemValido(Boolean.TRUE);
			}else{
				
				loopSeg:for (SegmentoNaoRecebeCotaDTO seg : obterSegmentosNaoRecebidosCadastradosNaCota) {
					if(seg.getNomeSegmento().equals(tipoSegProd.getDescricao())){
						
						for (ProdutoRecebidoDTO prodRecebidoDTO : obterProdutosRecebidosPelaCotaList) {
							if(prodRecebidoDTO.getCodigoProdin().equals(mixCotaProdutoDTO.getCodigoProduto()))
								continue loopSeg;
						}
						
                        mensagens.add("Cota [" + mixCotaProdutoDTO.getNumeroCota() + "] não recebe segmento "
                                + tipoSegProd.getDescricao());
						mixCotaProdutoDTO.setItemValido(false);
						
					}
				}
			}
		}
		
		
		return mensagens;
	}
	
	@Override
	@Transactional
	public List<String> adicionarListaMixPorProduto(List<MixCotaProdutoDTO> listaMixCota , String produtoId) {
		
		List<String> mensagens = obterValidacaoLista(listaMixCota);
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		List<TipoClassificacaoProduto> obterTodos = this.tipoClassificacaoProdutoService.obterTodos();
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
			
			if (!mixCotaProdutoDTO.isItemValido()) {
				
				continue;
			}
			
			Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
			MixCotaProduto mixCotaProduto = new MixCotaProduto();
			
			for (TipoClassificacaoProduto tcp : obterTodos) {
				if(tcp.getDescricao().equalsIgnoreCase(mixCotaProdutoDTO.getClassificacaoProduto())){
					mixCotaProduto.setTipoClassificacaoProduto(tcp);
					break;
				}
			}
			
			mixCotaProduto.setCodigoICD(mixCotaProdutoDTO.getCodigoICD());
			mixCotaProduto.setUsuario(usuario);
			mixCotaProduto.setCota(cota);
			mixCotaProduto.setDataHora(new Date());
			mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
			mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
			
			mixCotaProdutoRepository.adicionar(mixCotaProduto);
		}
		
		return mensagens;
	}

	private String obterValidacaoPreenchimentoMix(MixCotaProdutoDTO mixCotaProdutoDTO) {
	
		if (StringUtils.isEmpty(mixCotaProdutoDTO.getNumeroCota())) {
			
			return obterStringMensagemValidacaoProduto(mixCotaProdutoDTO) + " dados da cota devem ser preenchidos";
		}
		
		if (!produtoService.isIcdValido(mixCotaProdutoDTO.getCodigoICD())) {
			
			return obterStringMensagemValidacaoProduto(mixCotaProdutoDTO) + " Produto com Código ICD inválido, ajuste-o no Cadastro de Produto.";
		}
	
		if (mixCotaProdutoDTO.getReparteMinimo() == null) {
			
			return obterStringMensagemValidacaoProdutoCota(mixCotaProdutoDTO) + " Reparte Minimo deve ser preenchido";
		}
	
		if (mixCotaProdutoDTO.getReparteMaximo() == null) {
			
			return obterStringMensagemValidacaoProdutoCota(mixCotaProdutoDTO) + " Reparte Maximo deve ser preenchido";
		}
	
		if (mixCotaProdutoDTO.getReparteMinimo() > mixCotaProdutoDTO.getReparteMaximo()) {
			
            return obterStringMensagemValidacaoProdutoCota(mixCotaProdutoDTO)
                    + " não pode ter o reparte maximo não pode ser menor que o minimo.";
		}
		
		return null;
	}
	
	private String obterStringMensagemValidacaoProdutoCota(MixCotaProdutoDTO mixCotaProdutoDTO) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(obterStringMensagemValidacaoProduto(mixCotaProdutoDTO));
		stringBuilder.append(obterStringMensagemValidacaoCota(mixCotaProdutoDTO));
		return stringBuilder.toString();
	}
	
	private String obterStringMensagemValidacaoCota(MixCotaProdutoDTO mixCotaProdutoDTO) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(" Cota: [").append(mixCotaProdutoDTO.getNumeroCota());
		
		if (!StringUtils.isEmpty(mixCotaProdutoDTO.getNomeCota())) {
			stringBuilder.append(",").append(mixCotaProdutoDTO.getNomeCota());
		}
		
		stringBuilder.append("]");
		
		return stringBuilder.toString();
	}
	
	private String obterStringMensagemValidacaoProduto(MixCotaProdutoDTO mixCotaProdutoDTO) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Produto: [").append(mixCotaProdutoDTO.getCodigoProduto());
		
		if (!StringUtils.isEmpty(mixCotaProdutoDTO.getNomeProduto())) {
			stringBuilder.append(",").append(mixCotaProdutoDTO.getNomeProduto());
		}
		
		stringBuilder.append("]");
		
		return stringBuilder.toString();
	}
	
	@Override
	@Transactional
	public List<String> adicionarMixEmLote(List<MixCotaProdutoDTO> mixCotaProdutoDTOList) {
		
		List<String> mensagens = obterValidacaoListaEmLote(mixCotaProdutoDTOList);
		
		List<TipoClassificacaoProduto> classificacaoList = this.tipoClassificacaoProdutoRepository.buscarTodos();
		
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : mixCotaProdutoDTOList) {
			
			
			
			if (!mixCotaProdutoDTO.isItemValido()) {
				
				continue;
			}
			

			
			Cota cota = cotaService.obterPorNumeroDaCota(Integer.valueOf(mixCotaProdutoDTO.getNumeroCota()));
			
			MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.obterMixPorCotaICDCLassificacao(cota.getId(), mixCotaProdutoDTO.getCodigoICD(),mixCotaProdutoDTO.getClassificacaoProduto());
			if (mixCotaProduto == null) {
				mixCotaProduto = new MixCotaProduto();
				mixCotaProduto.setCota(cota);
				mixCotaProduto.setCodigoICD(mixCotaProdutoDTO.getCodigoICD());
				for (TipoClassificacaoProduto classi : classificacaoList) {
					if(classi.getDescricao().equals(mixCotaProdutoDTO.getClassificacaoProduto())){
						mixCotaProduto.setTipoClassificacaoProduto(classi);
						break;
					}
				}
			}
				
			mixCotaProduto.setDataHora(new Date());
			mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
			mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
			mixCotaProduto.setUsuario(usuario);
			
			/*
			if (mixCotaProduto.getProduto() != null || mixCotaProduto.getProduto().getId() != null) {
				mixCotaProdutoRepository.merge(mixCotaProduto);
			}*/
			mixCotaProdutoRepository.saveOrUpdate(mixCotaProduto);
			
		}
		
		return mensagens;
	}
	
	@Override
	@Transactional
	public boolean gerarCopiaMix(CopiaMixFixacaoDTO copiaMix){
		
		switch (copiaMix.getTipoCopia()) {
		case COTA:

			FiltroConsultaMixPorCotaDTO fMixCota = new FiltroConsultaMixPorCotaDTO();
			fMixCota.setCota(copiaMix.getCotaNumeroOrigem());
			
			FiltroConsultaMixPorCotaDTO cotaDestinoMix = new FiltroConsultaMixPorCotaDTO();
			cotaDestinoMix.setCota(copiaMix.getCotaNumeroDestino());
	
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaMix.getCotaNumeroDestino());
			
			List<MixCotaDTO> mixCotaOrigem = pesquisarPorCota(fMixCota);
			
			List<MixCotaDTO> mixCotaDestino = pesquisarPorCota(cotaDestinoMix);
			
			List<MixCotaDTO> mixJaCadastrados = new ArrayList<>();
			
			if(mixCotaOrigem==null || mixCotaOrigem.isEmpty()){
                throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
			}
			
			if(mixCotaDestino != null){
				for (MixCotaDTO mixOrigem : mixCotaOrigem) {
					for (MixCotaDTO mixDestino : mixCotaDestino) {
						if(mixOrigem.getCodigoICD().equals(mixDestino.getCodigoICD()) && mixOrigem.getTipoClassificacaoProdutoID().compareTo(mixDestino.getTipoClassificacaoProdutoID()) == 0){
							mixJaCadastrados.add(mixOrigem);
						}
					}
				}
				
				mixCotaOrigem.removeAll(mixJaCadastrados);
				mixJaCadastrados.clear();
			}
			
			if(mixCotaOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Não há nenhum MIX válido para efetuar a cópia.");
			}
			
			for (MixCotaDTO mixCotaDTO : mixCotaOrigem) {
				mixCotaDTO.setIdCota(new BigInteger(cotaDestino.getId().toString()));
			}
					
			try {
				this.mixCotaProdutoRepository.gerarCopiaMixCota(mixCotaOrigem,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return false;
			}
			break;
		case PRODUTO:
			
			FiltroConsultaMixPorProdutoDTO fMixProduto = new FiltroConsultaMixPorProdutoDTO();
			
			Produto produtoOrigem = produtoService.obterProdutoPorCodigo(copiaMix.getCodigoProdutoOrigem());
			Produto produtoDestino = produtoService.obterProdutoPorCodigo(copiaMix.getCodigoProdutoDestino());
			
			fMixProduto.setCodigoProduto(produtoOrigem.getCodigoICD());
			
			if(!produtoService.isIcdValido(produtoDestino.getCodigoICD())){
				throw new ValidacaoException(TipoMensagem.WARNING, "Produto ["+produtoDestino.getNomeComercial()+"]: Código ICD inválido, ajuste-o no Cadastro de Produto.");
			}
			
			List<MixProdutoDTO> mixProdutoOrigem = pesquisarPorProduto(fMixProduto);
			if(mixProdutoOrigem==null || mixProdutoOrigem.isEmpty()){
                throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
			}
			
			for (MixProdutoDTO mixProdutoDTO : mixProdutoOrigem) {
				mixProdutoDTO.setCodigoICD(produtoDestino.getCodigoICD());
				mixProdutoDTO.setIdProduto(BigInteger.valueOf(produtoDestino.getId()));
			}
			
			try {
				this.mixCotaProdutoRepository.gerarCopiaMixProduto(mixProdutoOrigem,this.usuarioService.getUsuarioLogado());
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
	public BigInteger obterSomaReparteMinimoPorProdutoUsuario(Long produtoId, Long idUsuario) {
		
		return mixCotaProdutoRepository.obterSomaReparteMinimoPorProdutoUsuario(produtoId, idUsuario);
	}

	@Override
	@Transactional
	public void updateReparteMixCotaProduto(Long novoValorReparte, String tipoCampo, Long idMix) {
		MixCotaProduto mix = this.mixCotaProdutoRepository.buscarPorId(idMix);
		
		if(mix==null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
		}
		
		if(tipoCampo.equalsIgnoreCase("MAX")){
			if(mix.getReparteMinimo().compareTo(novoValorReparte)==1){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Reparte mínimo não deve ser maior que o reparte Máximo.");
			}
			mix.setReparteMaximo(novoValorReparte);
		}else if(tipoCampo.equalsIgnoreCase("MIN")){
			if(novoValorReparte.compareTo(mix.getReparteMaximo())==1){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Reparte mínimo não deve ser maior que o reparte Máximo.");
			}
			mix.setReparteMinimo(novoValorReparte);
		}
		else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao atualizar Mix.");
		}
		
		mix.setDataHora(GregorianCalendar.getInstance().getTime());
		mix.setUsuario(usuarioService.getUsuarioLogado());
		
		for(RepartePDV pdvMix : mix.getRepartesPDV()){
			pdvMix.setReparte(0);
		}
		
		this.mixCotaProdutoRepository.alterar(mix);
			
		
	}

	@Override
	public MixCotaProduto obterMixPorCotaProduto(Long cotaId, Long tipoClassifProdId, String codigoICD) {
		return mixCotaProdutoRepository.obterMixPorCotaProduto(cotaId, tipoClassifProdId, codigoICD);
	}

    @Override
    @Transactional(readOnly=true)
    public boolean verificarReparteMinMaxCotaProdutoMix(Integer numeroCota, 
            String codigoProduto, Long qtd, Long tipoClassificacaoProduto) {
        
        return this.mixCotaProdutoRepository.verificarReparteMinMaxCotaProdutoMix(
                numeroCota, codigoProduto, qtd, tipoClassificacaoProduto);
    }
}
