package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
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
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MixCotaProdutoServiceImpl implements MixCotaProdutoService {
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
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private FixacaoRepartePdvRepository fixacaoRepartePdvRepository;
	
	@Autowired
	private RepartePDVRepository repartePDVRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	

	@Override
	@Transactional(readOnly = true)
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		
		Cota cota = cotaService.obterPorNumeroDaCota(filtroConsultaMixCotaDTO.getCota());
		
		boolean tipoAlternativo = cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO);
		
		if (!tipoAlternativo) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Cota não é do tipo Alternativo."));
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
		if(mixCotaProduto.getProduto() !=null){
			mixCotaProdutoDTO.setProdutoId(mixCotaProduto.getProduto().getId());
			mixCotaProdutoDTO.setNomeProduto(mixCotaProduto.getProduto().getNome());
			mixCotaProdutoDTO.setCodigoProduto(mixCotaProduto.getProduto().getCodigo());
			mixCotaProdutoDTO.setClassificacaoProduto(mixCotaProduto.getProduto().getTipoClassificacaoProduto().getDescricao());
		}
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
		mixCotaProdutoRepository.removerPorIdCota(idCota);
	}
	
	
	@Override
	@Transactional
	public List<String> adicionarListaMixPorCota(List<MixCotaProdutoDTO> listaMixCota , Integer cotaId) {
		
		Cota cota = cotaService.obterPorNumeroDaCota(cotaId);
		
		if (cota == null) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota ["+ cotaId +"] não existe."));
		}
		
		List<String> mensagens = obterValidacaoLista(listaMixCota);
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
				
				if (!mixCotaProdutoDTO.isItemValido()) {
					
					continue;
				}
			
				Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
				MixCotaProduto mixCotaProduto = new MixCotaProduto();
				mixCotaProduto.setProduto(produto);
				mixCotaProduto.setCota(cota);
				mixCotaProduto.setDataHora(new Date());
				mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
				mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
				mixCotaProduto.setUsuario(usuario);
				
				mixCotaProdutoRepository.adicionar(mixCotaProduto);
		}
		
		
		return mensagens;
	}

	private boolean validaPreenchimentoMixPorCota(MixCotaProdutoDTO mixCotaProdutoDTO) {
		return StringUtils.isNotBlank(mixCotaProdutoDTO.getCodigoProduto()) 
				&& StringUtils.isNotBlank(mixCotaProdutoDTO.getNomeProduto()) 
				&& mixCotaProdutoDTO.getReparteMinimo() != null 
				&& mixCotaProdutoDTO.getReparteMaximo() != null;
	}
	
	
	@Override
	public String obterValidacaoLinha(MixCotaProdutoDTO mixCotaProdutoDTO) {
		
		String msgValidacao = obterValidacaoPreenchimentoMix(mixCotaProdutoDTO);
		
		if (!StringUtils.isEmpty(msgValidacao)) {
			
			return msgValidacao;
		}
		
		Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
		
		if (cota == null) {
			
			return "Cota ["+mixCotaProdutoDTO.getNumeroCota()+"] não existe.";
		}
		
		msgValidacao = obterValidacaoCota(cota);
		
		if (!StringUtils.isEmpty(msgValidacao)) {
			
			return msgValidacao;
		}
		
		Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
		
		if (produto == null) {
			
			return "Produto ["+mixCotaProdutoDTO.getCodigoProduto()+"] não existe.";
		}
		
		
		if (mixCotaProdutoRepository.existeMixCotaProdutoCadastrado(produto.getId(), cota.getId())) {
			
			return "Produto ["+produto.getCodigo()+":"+produto.getNome()+"], Cota:["+mixCotaProdutoDTO.getNumeroCota()+","+mixCotaProdutoDTO.getNomeCota()+"] já foi cadastrado."; 
					
		}
		
		mixCotaProdutoDTO.setItemValido(true);
		
		return msgValidacao;
	}
	
	private String obterValidacaoCota(Cota cota) {
		
		if (!cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO)) {
		

			return "Cota não é do tipo Alternativo: ["+cota.getNumeroCota()+":"+cota.getPessoa().getNome()+"]";
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
			
		}
		
		return mensagens;
	}
	
	@Override
	@Transactional
	public List<String> adicionarListaMixPorProduto(List<MixCotaProdutoDTO> listaMixCota , String produtoId) {
		
		List<String> mensagens = obterValidacaoLista(listaMixCota);
		
		Produto produto = produtoService.obterProdutoPorCodigo(produtoId);
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
			
			if (!mixCotaProdutoDTO.isItemValido()) {
				
				continue;
			}
			
			Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
			
			MixCotaProduto mixCotaProduto = new MixCotaProduto();
			mixCotaProduto.setUsuario(usuario);
			mixCotaProduto.setProduto(produto);
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
		
		if (StringUtils.isEmpty(mixCotaProdutoDTO.getCodigoProduto())) {
			
			return obterStringMensagemValidacaoCota(mixCotaProdutoDTO) + " dados do produto devem ser preenchidos";
		}
	
		if (mixCotaProdutoDTO.getReparteMinimo() == null) {
			
			return obterStringMensagemValidacaoProdutoCota(mixCotaProdutoDTO) + " Reparte Minimo deve ser preenchido";
		}
	
		if (mixCotaProdutoDTO.getReparteMaximo() == null) {
			
			return obterStringMensagemValidacaoProdutoCota(mixCotaProdutoDTO) + " Reparte Maximo deve ser preenchido";
		}
	
		if (mixCotaProdutoDTO.getReparteMinimo() > mixCotaProdutoDTO.getReparteMaximo()) {
			
			return obterStringMensagemValidacaoProdutoCota(mixCotaProdutoDTO) + " não pode ter o reparte maximo não pode ser menor que o minimo.";
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
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : mixCotaProdutoDTOList) {
			
			if (!mixCotaProdutoDTO.isItemValido()) {
				
				continue;
			}
			
			Cota cota = cotaService.obterPorNumeroDaCota(new Integer(mixCotaProdutoDTO.getNumeroCota()));
			Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
				
			MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.obterMixPorCotaProduto(cota.getId(), produto.getId());
			if (mixCotaProduto == null) {
				mixCotaProduto = new MixCotaProduto();
				mixCotaProduto.setProduto(produto);
				mixCotaProduto.setCota(cota);
			}
				
			mixCotaProduto.setDataHora(new Date());
			mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
			mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
			mixCotaProduto.setUsuario(usuario);
			
			if (mixCotaProduto.getProduto() != null || mixCotaProduto.getProduto().getId() != null) {
				mixCotaProdutoRepository.merge(mixCotaProduto);
			}
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
			
	
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaMix.getCotaNumeroDestino());
			
			List<MixCotaDTO> mixCotaOrigem = pesquisarPorCota(fMixCota);
			if(mixCotaOrigem==null || mixCotaOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
			}
			for (MixCotaDTO mixCotaDTO : mixCotaOrigem) {
				mixCotaDTO.setIdCota(new BigInteger(cotaDestino.getId().toString()));
			}
					
			try {
				this.mixCotaProdutoRepository.gerarCopiaMixCota(mixCotaOrigem,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			break;
		case PRODUTO:
			
			FiltroConsultaMixPorProdutoDTO fMixProduto = new FiltroConsultaMixPorProdutoDTO();
			
			Produto produtoDestino = produtoService.obterProdutoPorCodigo(copiaMix.getCodigoProdutoDestino());
			fMixProduto.setCodigoProduto(copiaMix.getCodigoProdutoOrigem());
			
			List<MixProdutoDTO> mixProdutoOrigem = pesquisarPorProduto(fMixProduto);
			if(mixProdutoOrigem==null || mixProdutoOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
			}
			
			for (MixProdutoDTO mixProdutoDTO : mixProdutoOrigem) {
				mixProdutoDTO.setIdProduto(new BigInteger(produtoDestino.getId().toString()));
			}
			
			try {
				this.mixCotaProdutoRepository.gerarCopiaMixProduto(mixProdutoOrigem,this.usuarioService.getUsuarioLogado());
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

	private boolean validaMixEmLote(MixCotaProdutoDTO mixCotaProdutoDTO) {
		return StringUtils.isNotBlank(mixCotaProdutoDTO.getCodigoProduto())
				&& StringUtils.isNotBlank(mixCotaProdutoDTO.getNumeroCota()) 
				&& mixCotaProdutoDTO.getReparteMinimo() != null
				&& mixCotaProdutoDTO.getReparteMaximo() != null;
	}
}
