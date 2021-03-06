package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.ChamadaEncalheImpressaoWrapper;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.CotaProdutoEmissaoCEDTO;
import br.com.abril.nds.dto.DadosImpressaoEmissaoChamadaEncalhe;
import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.NotaEnvioProdutoEdicao;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe de implementação referentes a serviços de chamada encalhe. 
 *   
 * @author Discover Technology
 */

@Service
public class ChamadaEncalheServiceImpl implements ChamadaEncalheService {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(ChamadaEncalheServiceImpl.class);
	 
	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;

	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
    protected ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private ImpressaoNFeRepository impressaoNFeRepository;
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;
	
	 @Autowired
	 private EmailService emailSerice;
	
	@Override
	@Transactional
	public List<Integer> obterCotasComOperacaoDiferenciada(FiltroEmissaoCE filtro) {
		
		if(filtro.getNumCotaDe() == null){
			filtro.setNumCotaDe(0);
		}
		
		if(filtro.getNumCotaAte() == null){
			filtro.setNumCotaAte(999999);
		}
		
		return grupoRepository.obterCotasComOperacaoDiferenciada(filtro);
	}


	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterCotasSemOperacaoDiferenciada(FiltroEmissaoCE filtro) {
		return grupoRepository.obterCotasSemOperacaoDiferenciada(filtro);
	}

	
	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro) {
		
		Intervalo<Integer> intervaloCotas = null;
		
		if (filtro.getNumCotaDe() != null && filtro.getNumCotaAte() != null) {
			intervaloCotas = new Intervalo<Integer>(filtro.getNumCotaDe(), filtro.getNumCotaAte());
		}
		
		final Intervalo<Date> intervaloDataRecolhimento = new Intervalo<Date>(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte());
		
		this.cotaService.verificarCotasSemRoteirizacao(intervaloCotas, null, intervaloDataRecolhimento);
		
		final List<CotaEmissaoDTO> listaChamadaEncalhe = chamadaEncalheRepository.obterDadosEmissaoChamadasEncalhe(filtro);
		
		if (listaChamadaEncalhe == null) {
		    
		    return new ArrayList<>();
		}
		
		/*
		 * TODO parcias removidas com encalhe zero
		
		for(int i = 0; i < listaChamadaEncalhe.size(); i++) {
			
			if(listaChamadaEncalhe.get(i).getQtdeExemplares() <= 0) {
				
				listaChamadaEncalhe.remove(i);
			}
		}
		*/
		
		
		return listaChamadaEncalhe;
	}

    /**
     * Obtem endereço do PDV principal da Cota, caso não encontre, obtem o endereço principal da Cota.
     * @param cota
     * @return Endereco
     */
	private Endereco obterEnderecoImpressaoCE(Cota cota) {
		
		Endereco endereco = null;

		if(cota.getPDVPrincipal() != null 
				&& cota.getPDVPrincipal().getEnderecoPrincipal() != null
				&& cota.getPDVPrincipal().getEnderecoPrincipal().getEndereco() != null) {
			
			return cota.getPDVPrincipal().getEnderecoPrincipal().getEndereco();
			
		} else {
			
			PDV pdvPrincipal = this.pdvRepository.obterPDVPrincipal(cota.getId());
			EnderecoPDV enderecoPdv = pdvPrincipal != null ? pdvPrincipal.getEnderecoEntrega() : null;
			
			if (enderecoPdv == null) {
				
				if(pdvPrincipal == null) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, String.format("Cota %s sem o PDV Principal", cota.getNumeroCota()));
				}
				
				for (EnderecoPDV ePdv : pdvPrincipal.getEnderecos()) {
					
					if (ePdv.isPrincipal()){
						
						enderecoPdv = ePdv;
						break;
					}
				}
			}
			
			if (enderecoPdv != null) {
				
				return enderecoPdv.getEndereco();
			}
		}
		
		for(EnderecoCota enderecoCota : cota.getEnderecos()) {
			
			if (enderecoCota.isPrincipal()) {
				
				endereco = enderecoCota.getEndereco();
				
				break;
			}
		}
		
		return endereco;
	}
	

	/**
	 * Faz a paginação da lista de produtos contida nos objos CotaEmissaoDTO para 
	 * que estes produtos sejam apresentados com quebra de página no relatório 
	 * HTML.
	 * 
	 * @param cotasEmissao
	 * 
	 * @param qtdProdutosPorPagina - Quantidade maxima de produtos por página
	 * 
	 * @param qtdMaximaProdutosComTotalizacao - Quantidade maxima de produtos
	 * em uma pagina que conseguira acomodar também a grid de totalização.
	 */
	private void paginarListaDeProdutosDasCotasEmissao(List<CotaEmissaoDTO> cotasEmissao, int qtdProdutosPorPagina, int qtdMaximaProdutosComTotalizacao) {
		
		for(CotaEmissaoDTO cota : cotasEmissao) {
			
			cota.setPaginasProduto(obterListaPaginada(cota.getProdutos(), qtdProdutosPorPagina));
			
			cota.setQuebraTotalizacaoUltimaPagina(verificarQuebraDePaginaNaTotalizacao(cota, qtdMaximaProdutosComTotalizacao));
		}
	
	}
	
	/**
	 * Verifica a quantidade produtos na ultima pagina para decidir
	 * se devera haver uma quebra de pagina ao apresentar a totalização
	 * no relatorio de emissao ce.
	 * 
	 * @param cota 
	 * 
	 * @param qtdMaximaProdutosComTotalizacao - Quantidade maxima de produtos
	 * em uma pagina que conseguira acomodar também a grid de totalização.
	 * 
	 * @return boolean
	 * 
	 */
	private boolean verificarQuebraDePaginaNaTotalizacao(CotaEmissaoDTO cota, int qtdMaximaProdutosComTotalizacao) {
		
		if(cota.getPaginasProduto() != null && !cota.getPaginasProduto().isEmpty() ) {
			
			int qtdeItensUltimaPagina = cota.getPaginasProduto().get(cota.getPaginasProduto().size()-1).size();
			
			if(qtdeItensUltimaPagina>=qtdMaximaProdutosComTotalizacao) {
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Faz a paginação da lista de capas personalizadas 
	 * (utilizando os produtos que constam na lista) para
	 * que estas capas sejam apresentadas com quebra de página
	 * no relatório HTML.
	 * 
	 * 
	 * @param cotasEmissao
	 * @param qtdCapasPorPaginas
	 */
	private void paginarListaDeCapasPersonalizadas(List<CotaEmissaoDTO> cotasEmissao, int qtdCapasPorPaginas) {
		
		for(CotaEmissaoDTO cota : cotasEmissao) {
			
			removerProdutoEmissaoDetalheNota(cota.getProdutos());
			
			List<List<ProdutoEmissaoDTO>> listaProdutosPaginados = 
					obterListaPaginada(cota.getProdutos(), qtdCapasPorPaginas);
			
			List<List<CapaDTO>> listaCapasPaginadas = new ArrayList<List<CapaDTO>>();
			
			for(List<ProdutoEmissaoDTO> paginaProduto : listaProdutosPaginados) {
				
				List<CapaDTO> paginaCapa = new ArrayList<>();
				
				for(ProdutoEmissaoDTO produto : paginaProduto) {
					
					CapaDTO capa = new CapaDTO();
					capa.setSequenciaMatriz(produto.getSequencia());
					capa.setId(produto.getIdProdutoEdicao());
					
					paginaCapa.add(capa);
					
				}
				
				listaCapasPaginadas.add(paginaCapa);
			}
			
			cota.setPaginasCapa(listaCapasPaginadas);
		}
	}
	
	/**
	 * Remove os produto que possuem detalhes da 
	 * nota de envio da lista original (não paginada)
	 * de produtos da CE.
	 * 
	 * @param produtos
	 */
	protected void removerProdutoEmissaoDetalheNota(List<ProdutoEmissaoDTO> produtos) {
		
		int tamanho = produtos.size();
	
		List<ProdutoEmissaoDTO> listaRemocao = new ArrayList<>();
		
		for(int i = 0; i < tamanho; i++) {
			
			if(produtos.get(i).isProdutoDuplicadoDetalheNota()) {
				listaRemocao.add(produtos.get(i));
			}
			
		}
		
		for(ProdutoEmissaoDTO prodRemov : listaRemocao) {
			produtos.remove(prodRemov);
		}
		
	}

	private void processarProdutosEmissaoEncalheDaCota(CotaEmissaoDTO cota, Date dataOperacaoDistribuidor, FiltroEmissaoCE filtro, List<CotaProdutoEmissaoCEDTO> produtosSupRedist) {
		
		BigDecimal vlrReparte = BigDecimal.ZERO;	
		BigDecimal vlrDesconto = BigDecimal.ZERO;
		BigDecimal vlrEncalhe = BigDecimal.ZERO;
		
		List<Long> idsProdutoEdicao = new ArrayList<>();
		
		if(cota != null && cota.getProdutos() == null) {
			
			if(filtro.isEnvioEmail() == true || filtro.isImpressao() == true){
				filtro.setValidacao(new ValidacaoVO(TipoMensagem.WARNING, "Não foram encontrados produtos para a Cota "+cota.getNumCota()));
			}else{
				throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados produtos para a Cota "+cota.getNumCota());
			}
			
			LOGGER.error("Não foram encontrados produtos para a Cota "+cota.getNumCota()+ " .Verificar se tem movimento estoque cota com reparte > 0");
		}else{
			
			for(ProdutoEmissaoDTO produtoDTO : cota.getProdutos()) {
				
				idsProdutoEdicao.add(produtoDTO.getIdProdutoEdicao());
				
				produtoDTO.setQuantidadeDevolvida((produtoDTO.getQuantidadeDevolvida() == null) ? BigInteger.ZERO : produtoDTO.getQuantidadeDevolvida());
				produtoDTO.setQuantidadeDev((produtoDTO.getQuantidadeDevolvida() == null) ? null : produtoDTO.getQuantidadeDevolvida().intValue());
				
				produtoDTO.setReparte( (produtoDTO.getReparte()==null) ? BigInteger.ZERO : produtoDTO.getReparte() );
				
				produtoDTO.setQuantidadeReparte((produtoDTO.getReparte()==null) ? 0 : produtoDTO.getReparte().intValue());
				
				produtoDTO.setVlrDesconto( (produtoDTO.getVlrDesconto() == null) ? BigDecimal.ZERO :  produtoDTO.getVlrDesconto());
				
				if(produtoDTO.getConfereciaRealizada() == true) { 
					produtoDTO.setVendido(produtoDTO.getReparte().subtract(produtoDTO.getQuantidadeDevolvida()));
				} else { 
					produtoDTO.setVendido(BigInteger.ZERO);
				}
				
				produtoDTO.setVlrVendido(CurrencyUtil.formatarValor(produtoDTO.getVlrPrecoComDesconto().multiply(BigDecimal.valueOf(produtoDTO.getVendido().longValue()))));
				
				vlrReparte = vlrReparte.add( produtoDTO.getPrecoVenda().multiply(BigDecimal.valueOf(produtoDTO.getReparte().longValue())));
				
				vlrDesconto = vlrDesconto.add(produtoDTO.getPrecoVenda().subtract(produtoDTO.getVlrPrecoComDesconto())
						.multiply(BigDecimal.valueOf(produtoDTO.getReparte().longValue())));
				
				vlrEncalhe = vlrEncalhe.add(produtoDTO.getVlrPrecoComDesconto()
						.multiply( BigDecimal.valueOf(produtoDTO.getQuantidadeDevolvida().longValue()) ));
				
				formatarLinhaExtraSupRedistCE(cota, produtoDTO, produtosSupRedist);
				
			}
		}
		
		BigDecimal vlrReparteLiquido = vlrReparte.subtract(vlrDesconto);
		
		BigDecimal totalLiquido = vlrReparteLiquido.subtract(vlrEncalhe);
		
		cota.setVlrReparte(CurrencyUtil.formatarValor(vlrReparte));
		cota.setVlrComDesconto(CurrencyUtil.formatarValorQuatroCasas(vlrDesconto));
		cota.setVlrReparteLiquido(CurrencyUtil.formatarValorQuatroCasas(vlrReparteLiquido));
		cota.setVlrEncalhe(CurrencyUtil.formatarValorQuatroCasas(vlrEncalhe));
		cota.setVlrTotalLiquido(CurrencyUtil.formatarValorQuatroCasas(totalLiquido));
				
	}

	private void formatarLinhaExtraSupRedistCE(CotaEmissaoDTO cota, ProdutoEmissaoDTO produtoEmissaoDTO, List<CotaProdutoEmissaoCEDTO> produtosSupRedist) {
		
		for(CotaProdutoEmissaoCEDTO cpece : produtosSupRedist) {
			
			if(produtoEmissaoDTO.getIdProdutoEdicao().equals(cpece.getIdProdutoEdicao()) && cota.getIdCota().equals(cpece.getIdCota())) {
				
				String descricaoQuebraRelatorioCE = cpece.getReparte() +" exs("+ DateUtil.formatarData(cpece.getDataMovimento(), Constantes.DAY_MONTH_PT_BR) +")"; //obterDescricaoQuebraRelatorioCE(notas);
				
				if(produtoEmissaoDTO.getDescricaoNotaEnvio() != null) {
					
					produtoEmissaoDTO.setDescricaoNotaEnvio(produtoEmissaoDTO.getDescricaoNotaEnvio() +" + "+ descricaoQuebraRelatorioCE);
				} else {
					produtoEmissaoDTO.setDescricaoNotaEnvio(descricaoQuebraRelatorioCE);
				}
				
			}
		}
	}
	
	/**
	 * Carrega as informações da nota de envio nos 
	 * produto de emissão CE pertinentes.
	 * 
	 * @param mapaNotaEnvioPE
	 * @param listaProdutos
	 */
	protected void carregarDadosNotaEnvioProdutoEdicao(
			Map<Long, List<NotaEnvioProdutoEdicao>> mapaNotaEnvioPE, 
			List<ProdutoEmissaoDTO> listaProdutos) {
		
		for(ProdutoEmissaoDTO produtoDTO : listaProdutos) {
			
			List<NotaEnvioProdutoEdicao> notas = mapaNotaEnvioPE.get(produtoDTO.getIdProdutoEdicao());
			
			if(notas == null || notas.isEmpty()) {
				continue;
			}

			BigInteger numeroNotaEnvio = notas.get(0).getNumeroNotaEnvio();

			if(numeroNotaEnvio!=null) {
				produtoDTO.setNotaEnvio(numeroNotaEnvio.toString());
			}
			
			String descricaoQuebraRelatorioCE = obterDescricaoQuebraRelatorioCE(produtoDTO, notas);
			
			if(descricaoQuebraRelatorioCE != null && !"".equals(descricaoQuebraRelatorioCE)){
				produtoDTO.setDescricaoNotaEnvio(descricaoQuebraRelatorioCE);
			}
		}
		
	}
	
	/**
	 * Concatena informações das nota de envio e retorna
	 * uma descrição das mesmas com a data de emissão 
	 * e a quantidade de reparte das mesmas.
	 * 
	 * @param notas
	 * 
	 * @return String
	 */
	private String obterDescricaoQuebraRelatorioCE(ProdutoEmissaoDTO produtoDTO, 
												   List<NotaEnvioProdutoEdicao> notas ) {
		
		StringBuffer descricao = new StringBuffer();
		
		String plusSignal = "";
		
		for(NotaEnvioProdutoEdicao nota : notas) {
			
			if(nota.getReparte() == null || nota.getReparte().intValue() < 1)
				continue;
			
			descricao.append(plusSignal);
			
			if(nota.getReparte()!=null) {
				
				produtoDTO.setReparte(produtoDTO.getReparte().add(nota.getReparte()));
				
				descricao.append(nota.getReparte());
				descricao.append(" exes. ");
			}
			
			if(nota.getDataConsignacao()!=null) {
				String dataConsignado = DateUtil.formatarData(nota.getDataConsignacao(), Constantes.DAY_MONTH_PT_BR);
				descricao.append("(").append(dataConsignado).append(")");
			}
			
			plusSignal = " + ";
		}
		
		return descricao.toString();
		
	}
	
	
	protected Map<Long, List<NotaEnvioProdutoEdicao>> gerarMapaProdutoEdicaoNotasEmitidas(List<NotaEnvioProdutoEdicao> listaNotaEnvioProdutoEdicao) {
		
		Map<Long, List<NotaEnvioProdutoEdicao>> mapaProdutoEdicaoNota = new HashMap<>();
		
		for(NotaEnvioProdutoEdicao nep : listaNotaEnvioProdutoEdicao) {
			
			if(mapaProdutoEdicaoNota.containsKey(nep.getIdProdutoEdicao())) {
				
				mapaProdutoEdicaoNota.get(nep.getIdProdutoEdicao()).add(nep);
				
			} else {
				
				mapaProdutoEdicaoNota.put(nep.getIdProdutoEdicao(), new ArrayList<NotaEnvioProdutoEdicao>());
				
				mapaProdutoEdicaoNota.get(nep.getIdProdutoEdicao()).add(nep);
				
			}
			
		}
		
		return mapaProdutoEdicaoNota;
		
	}
	
	/**
	 * Para apresentar (no relatório de Emissao CE - modelo 2)
	 * a linha adicional abaixo dos produtos que possuem informaçoes 
	 * relativas a nota de envio. 
	 * 
	 * @param produtos
	 */
	protected void adicionarLinhasProdutoComInformacoesNotaEnvio(List<ProdutoEmissaoDTO> produtos){
		
		int tamanho = produtos.size();
		
		for ( int i =0; i < tamanho; i++ ) {
			
			ProdutoEmissaoDTO produto = produtos.get(i);
			
			if(produto.getDescricaoNotaEnvio()!=null) {
				
				ProdutoEmissaoDTO produtoNotaDetail = new ProdutoEmissaoDTO();
				
				produtoNotaDetail.setCodigoProduto(produto.getCodigoProduto());
				produtoNotaDetail.setNomeProduto(produto.getNomeProduto());
				produtoNotaDetail.setEdicao(produto.getEdicao());
				produtoNotaDetail.setIdProdutoEdicao(produto.getIdProdutoEdicao());
				produtoNotaDetail.setDescricaoNotaEnvio(produto.getDescricaoNotaEnvio());
				produtoNotaDetail.setProdutoDuplicadoDetalheNota(true);
				
				produtos.add((i+1), produtoNotaDetail);
				tamanho++;
				i++;
			}
			
		}
		
	}
	
	
	@Override
	@Transactional
	public DadosImpressaoEmissaoChamadaEncalhe obterDadosImpressaoEmissaoChamadasEncalhe(FiltroEmissaoCE filtro) {
		
		Date dataOperacaoDistribuidor = distribuidorService.obterDataOperacaoDistribuidor();
		
		boolean apresentaCapas = (filtro.getCapa() == null) ? false : filtro.getCapa();
		
		boolean apresentaCapasPersonalizadas = (filtro.getPersonalizada() == null) ? false : filtro.getPersonalizada();
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);
		
		//verificar essa lista para ver a quantidade de exemplares
		Cota cota = null;
		
		List<CotaProdutoEmissaoCEDTO> produtosSupRedist = chamadaEncalheRepository.obterDecomposicaoReparteSuplementarRedistribuicao(filtro);
		
		List<Date> datasControleFechamentoEncalhe = fechamentoEncalheRepository.obterDatasControleFechamentoEncalheRealizado(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte());
		
		Map<Long, List<ProdutoEmissaoDTO>> mapProdutosEmissaoCota = chamadaEncalheRepository.obterProdutosEmissaoCE(filtro);
		
		// List<GrupoCota> gps = this.grupoRepository.obterListaGrupoCotaPorCotaId(cota.getId(), dataOperacaoDistribuidor);
		
		Map<Long, List<GrupoCota>> mapGPS =  this.grupoRepository.obterListaGrupoCotaPorDataOperacao(dataOperacaoDistribuidor);
		
		for(CotaEmissaoDTO dto : lista) {
			
			cota = cotaRepository.buscarPorId(dto.getIdCota());
			
			Endereco endereco = cota.getPDVPrincipal().getEnderecoPrincipal().getEndereco();
			
			if(endereco != null) {
				dto.setEndereco((endereco.getTipoLogradouro().trim()!= null ? endereco.getTipoLogradouro().toUpperCase().trim() + ": " :"")
									+ endereco.getLogradouro().toUpperCase().trim()  + ", " + (endereco.getNumero()!= null ? endereco.getNumero().trim() : ""));
				dto.setUf(endereco.getUf().trim());
				dto.setCidade(endereco.getCidade().trim());
				dto.setUf(endereco.getUf().trim());
				dto.setCep(endereco.getCep().trim());
			} else {
				endereco = cota.getEnderecoPrincipal().getEndereco();
			}
			
			if(cota.getPessoa() instanceof PessoaJuridica) {
				dto.setInscricaoEstadual(((PessoaJuridica) cota.getPessoa()).getInscricaoEstadual());
			}
			
			dto.setNumeroNome(dto.getNumCota()+ " " + ((dto.getNomeCota()!= null)?dto.getNomeCota().toUpperCase():""));
		
			if(cota.getPessoa() instanceof PessoaJuridica) {
				dto.setCnpj(Util.adicionarMascaraCNPJ(cota.getPessoa().getDocumento()));
			} else {
				dto.setCnpj(Util.adicionarMascaraCPF(cota.getPessoa().getDocumento()));
			}
												
			dto.setDataEmissao(DateUtil.formatarDataPTBR(new Date()));

			String periodoRecolhimento;
			
			if(mapGPS != null && !mapGPS.isEmpty()) {
				if (mapGPS.containsKey(dto.getIdCota())){
					
				    periodoRecolhimento = filtro.getDtRecolhimentoDe().equals(filtro.getDtRecolhimentoAte())?
					                      DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()):
					                      DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe())+" à "+DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoAte());
				}
				else{
					
					periodoRecolhimento = dto.getDataRecolhimento();
				}	
			} else {
				periodoRecolhimento = dto.getDataRecolhimento();
			}
			                 
			dto.setPeriodoRecolhimento(periodoRecolhimento);
			
			List<ProdutoEmissaoDTO> produtosEmissao = mapProdutosEmissaoCota.get(dto.getIdCota());
			
			List<Date> datasControleConferenciaEncalheCotaFinalizada = 
		        this.controleConferenciaEncalheCotaRepository.obterDatasControleConferenciaEncalheCotaFinalizada(
		        		dto.getIdCota(), 
		        		filtro.getDtRecolhimentoDe(), 
		        		filtro.getDtRecolhimentoAte());
			
			this.setApresentaQuantidadeEncalhe(datasControleFechamentoEncalhe, datasControleConferenciaEncalheCotaFinalizada, produtosEmissao, filtro, dto.getQtdGrupoCota());
			
			dto.setProdutos(produtosEmissao);
			
			processarProdutosEmissaoEncalheDaCota(dto, dataOperacaoDistribuidor, filtro, produtosSupRedist);
			
			if(TipoImpressaoCE.MODELO_2.equals(filtro.getTipoImpressao())) {
				
				adicionarLinhasProdutoComInformacoesNotaEnvio(dto.getProdutos());
				
			}
			
		}
		
		paginarListaDeProdutosDasCotasEmissao(lista, filtro.getQtdProdutosPorPagina(), filtro.getQtdMaximaProdutosComTotalizacao());
		
		DadosImpressaoEmissaoChamadaEncalhe dados = new DadosImpressaoEmissaoChamadaEncalhe();
		dados.setCotasEmissao(lista);
		
		if(apresentaCapas) {
			
			if(apresentaCapasPersonalizadas) {
				
				paginarListaDeCapasPersonalizadas(lista, filtro.getQtdCapasPorPagina());
			
			} else {
				
				dados.setCapasPaginadas(
						obterIdsCapasChamadaEncalhe(
								filtro.getDtRecolhimentoDe(), 
								filtro.getDtRecolhimentoAte(), 
								filtro.getQtdCapasPorPagina()));
			}
			
		}
		
		return dados;
		
	}
	
	private void setApresentaQuantidadeEncalhe(List<Date> datasControleFechamentoEncalhe,
                                               List<Date> datasControleConferenciaEncalheCotaFinalizada,
                                               List<ProdutoEmissaoDTO> produtosEmissao,
                                               FiltroEmissaoCE filtro, Long qtdGrupoCota) {
	 
	    for (ProdutoEmissaoDTO produtoEmissao : produtosEmissao) {
	        
	        produtoEmissao.setApresentaQuantidadeEncalhe(
                this.isApresentaQuantidadeEncalhe(
                    datasControleFechamentoEncalhe, datasControleConferenciaEncalheCotaFinalizada, produtoEmissao, filtro, qtdGrupoCota));
	    }
	}
	
	private boolean isApresentaQuantidadeEncalhe(List<Date> datasControleFechamentoEncalhe,
	                                             List<Date> datasControleConferenciaEncalheCotaFinalizada,
	                                             ProdutoEmissaoDTO produtoEmissao,
	                                             FiltroEmissaoCE filtro,
	                                             Long qtdGrupoCota) {
        
        if (!qtdGrupoCota.equals(0L)) {
            
            if((this.existeData(datasControleFechamentoEncalhe)
                    && datasControleFechamentoEncalhe.contains(produtoEmissao.getDataRecolhimento()))
                    || (this.existeData(datasControleConferenciaEncalheCotaFinalizada)
                    && datasControleConferenciaEncalheCotaFinalizada.contains(produtoEmissao.getDataRecolhimento()))) {
                
                    return true;
            } else {
                    
                return false;
            }
        } else if (DateUtil.validarDataEntrePeriodo(produtoEmissao.getDataRecolhimento(),
                                                    filtro.getDtRecolhimentoDe(),
                                                    filtro.getDtRecolhimentoAte())) {
            return true;
        } else {
            
            return false;
        }
    }
	
	private boolean existeData(List<Date> datas) {
	    
	    return (datas != null && !datas.isEmpty());
	}
	

	private static <T> List<List<T>> obterListaPaginada(List<T> listaTotal, final int qtdItensPorPagina){
		
		List<List<T>> itensPaginados = new ArrayList<List<T>>();
			
		if(listaTotal == null) {
			return itensPaginados;
		}
		
		if( listaTotal.size() <= qtdItensPorPagina) {
			itensPaginados.add(listaTotal);
			return itensPaginados;
		} 
			
		int qtdeItens = listaTotal.size();
		int qtdePaginas = (int) Math.ceil((double) qtdeItens / qtdItensPorPagina);
		int indice = 0;
		
		for (int pagina = 0; pagina < qtdePaginas; pagina++) {
			int toIndex = (indice + qtdItensPorPagina);
			if(toIndex > qtdeItens) {
				toIndex = qtdeItens;
			}
			itensPaginados.add(listaTotal.subList(indice, toIndex));
			indice = toIndex;
		}
		
		return itensPaginados;
		
	}
	

	private List<List<CapaDTO>> obterIdsCapasChamadaEncalhe(Date dtRecolhimentoDe,
			Date dtRecolhimentoAte, int qtdCapasPorPagina) {
		
		List<CapaDTO> capasChamadaEncalhe = chamadaEncalheRepository.obterIdsCapasChamadaEncalhe(dtRecolhimentoDe, dtRecolhimentoAte);
		
		return obterListaPaginada(capasChamadaEncalhe, 30);
		
	}
	
	@Override
	@Transactional
	public List<BandeirasDTO> obterBandeirasDaSemana(Integer semana, Long fornecedor, PaginacaoVO paginacaoVO) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		FiltroImpressaoNFEDTO filtro = new FiltroImpressaoNFEDTO();
		filtro.setDataEmissaoInicial(periodoRecolhimento.getDe());
		filtro.setDataEmissaoFinal(periodoRecolhimento.getAte());
		
		TipoAtividade tipoAtividade = distribuidorService.obter().getTipoAtividade();
		TipoEmitente tipoEmitente = TipoEmitente.DISTRIBUIDOR;
		TipoDestinatario tipoDestinatario = TipoDestinatario.FORNECEDOR;
		TipoOperacao tipoOperacao = TipoOperacao.SAIDA;  
		naturezaOperacaoService.obterNaturezaOperacao(tipoAtividade, tipoEmitente, tipoDestinatario, tipoOperacao);
		impressaoNFeRepository.obterNotafiscalImpressaoBandeira(filtro);
//		return chamadaEncalheRepository.obterBandeirasSemana(periodoRecolhimento, fornecedor, paginacaoVO);
		return null;
	}
	
	@Override
	@Transactional
	public Long countObterBandeirasDaSemana(Integer semana, Long fornecedor) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.countObterBandeirasNoIntervalo(periodoRecolhimento, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO, fornecedor);
	}
	
	@Override
	@Transactional
	public List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana, Long fornecedor) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.obterDadosFornecedoresParaImpressaoBandeira(periodoRecolhimento, fornecedor);
	}

	@Override
	@Transactional
	public byte[] gerarEmissaoCE(FiltroEmissaoCE filtro) throws JRException, URISyntaxException{
		
		boolean apresentaCapas = (filtro.getCapa() == null) ? false : filtro.getCapa();
		
		boolean apresentaCapasPersonalizadas = (filtro.getPersonalizada() == null) ? false : filtro.getPersonalizada();
		
		final List<ChamadaEncalheImpressaoWrapper> listaCEWrapper = this.gerarListaChamadaEncalheImpressaoWrapper(filtro);
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);
		
		// paginarListaDeProdutosDasCotasEmissao(lista, filtro.getQtdProdutosPorPagina(), filtro.getQtdMaximaProdutosComTotalizacao());
		
		if(apresentaCapas) {
			
			if(apresentaCapasPersonalizadas) {
				
				paginarListaDeCapasPersonalizadas(lista, filtro.getQtdCapasPorPagina());
		
			} else {
				
				
				obterIdsCapasChamadaEncalhe(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte(), filtro.getQtdCapasPorPagina());
								
			}
			
		}
       
//		return "teste Email".getBytes();
		return this.gerarDocumentoEmissaoCE(listaCEWrapper);
		
	}
	
	@Transactional
	public List<ChamadaEncalheImpressaoWrapper> gerarListaChamadaEncalheImpressaoWrapper(FiltroEmissaoCE filtro){
		
		DistribuidorDTO distribuidor = distribuidorService.obterDadosEmissao();
		
		Date dataOperacaoDistribuidor = distribuidorService.obterDataOperacaoDistribuidor();
		
		Integer numeroSemana = distribuidorService.obterNumeroSemana(filtro.getDtRecolhimentoDe());
		
		final List<ChamadaEncalheImpressaoWrapper> listaCEWrapper = new ArrayList<ChamadaEncalheImpressaoWrapper>();
		
		List<ChamadaEncalheImpressaoWrapper> listaCEWrapperImpressaoEemail = new ArrayList<ChamadaEncalheImpressaoWrapper>();
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);
		
		List<CotaProdutoEmissaoCEDTO> produtosSupRedist = chamadaEncalheRepository.obterDecomposicaoReparteSuplementarRedistribuicao(filtro);
		
		Map<Long, List<ProdutoEmissaoDTO>> mapProdutosEmissaoCota = chamadaEncalheRepository.obterProdutosEmissaoCE(filtro);
		
		Map<Long, List<GrupoCota>> mapGPS =  this.grupoRepository.obterListaGrupoCotaPorDataOperacao(dataOperacaoDistribuidor);
		
		List<Integer> numeroCotasNaoRecebemEmail = new ArrayList<>();
		
		List<Long> idCotasImpressaoEenvioEmail = new ArrayList<>();
		
		for(CotaEmissaoDTO dto : lista) {

			boolean isImpressaoEemail = false;
			
			Cota cota = cotaRepository.buscarPorId(dto.getIdCota());
			
			if((filtro.getNumCotaDe()==null && filtro.getNumCotaAte()==null) || (!filtro.getNumCotaDe().equals(filtro.getNumCotaAte()))){
				
				if(filtro.isImpressao()){
					if(cota.getParametroDistribuicao().getUtilizaDocsParametrosDistribuidor()){
						if(filtro.isDistribEnviaEmail()){
							idCotasImpressaoEenvioEmail.add(dto.getIdCota());
							isImpressaoEemail = true;
	    				}
					}else{
						if(!Util.validarBoolean(cota.getParametroDistribuicao().getChamadaEncalheImpresso())){
							continue;
	    				}else{
	    					if(Util.validarBoolean(cota.getParametroDistribuicao().getChamadaEncalheEmail())){
	    						idCotasImpressaoEenvioEmail.add(dto.getIdCota());
	    						isImpressaoEemail = true;
	    					}
	    				}
					}
				}else{
					if(filtro.isEnvioEmail()){
						if(!cota.getParametroDistribuicao().getUtilizaDocsParametrosDistribuidor()){
        					if(!Util.validarBoolean(cota.getParametroDistribuicao().getChamadaEncalheEmail())){
        						numeroCotasNaoRecebemEmail.add(cota.getNumeroCota());
    							continue;
        					}
        				}
					}
				}
			}
			
			dto.setNumeroSemana(numeroSemana);
			dto.setEmissorNome(distribuidor.getRazaoSocial());
			dto.setEmissorCNPJ(distribuidor.getCnpj());
			dto.setEmissorInscricaoEstadual(distribuidor.getInscricaoEstatual());
			dto.setEmissorCEP(distribuidor.getCep());
			dto.setEmissorMunicipio(distribuidor.getCidade());
			dto.setEmissorUF(distribuidor.getUf());
			dto.setEmissorLogradouro(distribuidor.getEndereco().trim());
			
			Endereco endereco = this.obterEnderecoImpressaoCE(cota);

			if(endereco != null) {
				dto.setEndereco( (endereco.getTipoLogradouro() != null ? endereco.getTipoLogradouro().toUpperCase().trim() + ": " :"")
									+ endereco.getLogradouro().toUpperCase().trim()  + ", " + (endereco.getNumero() != null ? endereco.getNumero() : ""));
				dto.setUf(endereco.getUf().trim());
				dto.setCidade(endereco.getCidade().trim());
				dto.setUf(endereco.getUf().trim());
				dto.setCep(endereco.getCep().trim());
				
				dto.setDestinatarioLogradouro(dto.getEndereco());
				dto.setDestinatarioMunicipio(endereco.getCidade());
				dto.setDestinatarioUF(endereco.getUf());
				dto.setDestinatarioCEP(endereco.getCep());
			}
			
			if(cota.getPessoa() instanceof PessoaJuridica) {
				dto.setInscricaoEstadual(((PessoaJuridica) cota.getPessoa()).getInscricaoEstadual());
			}
			
			dto.setNumeroNome(dto.getNumCota()+ " " + ((dto.getNomeCota()!= null)?dto.getNomeCota().toUpperCase():""));
		
			dto.setDestinatarioNome(dto.getNomeCota());
			
			if(cota.getPessoa() instanceof PessoaJuridica) {
				dto.setCnpj(Util.adicionarMascaraCNPJ(cota.getPessoa().getDocumento()));
				dto.setDestinatarioCNPJ(cota.getPessoa().getDocumento());
			} else {
				dto.setCnpj(Util.adicionarMascaraCPF(cota.getPessoa().getDocumento()));
				dto.setDestinatarioCNPJ(cota.getPessoa().getDocumento());

			}
			
			dto.setDataEmissao(DateUtil.formatarDataPTBR(new Date()));
			dto.setDestinatarioNomeBox(dto.getBox().toString());
			
			String periodoRecolhimento = null;
			
			if(mapGPS != null && !mapGPS.isEmpty()) {
				
				if (mapGPS.containsKey(dto.getIdCota())) {
					
				    periodoRecolhimento = filtro.getDtRecolhimentoDe().equals(filtro.getDtRecolhimentoAte()) ? 
					                      DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()) : 
					                      DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()) +" à "+ DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoAte());
				
				} else {
					
					periodoRecolhimento = dto.getDataRecolhimento();
				}	
			} else {
				periodoRecolhimento = filtro.getDtRecolhimentoDe().equals(filtro.getDtRecolhimentoAte()) ? 
	                      DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()) : 
	                      DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()) +" à "+ DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoAte());
			}
			
			dto.setPeriodoRecolhimento(periodoRecolhimento);
			
			List<ProdutoEmissaoDTO> produtosEmissao = mapProdutosEmissaoCota.get(dto.getIdCota());
			
			dto.setProdutos(produtosEmissao);
			
			processarProdutosEmissaoEncalheDaCota(dto, dataOperacaoDistribuidor, filtro, produtosSupRedist);
			
			if(TipoImpressaoCE.MODELO_2.equals(filtro.getTipoImpressao())) {
				
				adicionarLinhasProdutoComInformacoesNotaEnvio(dto.getProdutos());
				
			}
			
			listaCEWrapper.add(new ChamadaEncalheImpressaoWrapper(dto));
			
			if(isImpressaoEemail){
				listaCEWrapperImpressaoEemail.add(new ChamadaEncalheImpressaoWrapper(dto));
			}
			
		}
		
		if(!numeroCotasNaoRecebemEmail.isEmpty()){
			String numeroCotas = "";
			for (Integer cota : numeroCotasNaoRecebemEmail) {
				if(numeroCotas.isEmpty()){
					numeroCotas = cota.toString();
				}else{
					numeroCotas += ", "+cota;
				}
			}
			
			if(filtro.getValidacao() != null){
				filtro.getValidacao().getListaMensagens().add("\n As seguintes Cotas que não recebem CE: "+numeroCotas);
			}else{
				filtro.setValidacao(new ValidacaoVO(TipoMensagem.WARNING, "As seguintes Cotas que não recebem CE: "+numeroCotas));
			}
			
		}
		
		if(!listaCEWrapperImpressaoEemail.isEmpty()){

			Map<Long, List<ChamadaEncalheImpressaoWrapper>> mapa = agruparCEporCota(listaCEWrapperImpressaoEemail);
			
			enviarCEporEmail(mapa);
		}
		
		
		return listaCEWrapper;
	}
	
	private byte[] gerarDocumentoEmissaoCE(final List<ChamadaEncalheImpressaoWrapper> list) throws JRException, URISyntaxException {
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        
        final TipoImpressaoCE tipoImpressao = this.distribuidorService.tipoImpressaoCE();
		
		final URL diretorioReports = Util.obterDiretorioReports();
		
		String path = diretorioReports.toURI().getPath();
			
		if(tipoImpressao != null){
			
			switch (tipoImpressao) {
			
				case MODELO_1:
					
					path += "/chamada_encalhe_modelo1_wrapper.jasper";
					break;

				case MODELO_2:
					
					path += "/chamada_encalhe_modelo2_wrapper.jasper";
					break;
	
				default:
					
					break;
			}			
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao recuprar o tipo de impressão.");
		}
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
        InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
        
        if(inputStream == null) {
            inputStream = new ByteArrayInputStream(new byte[0]);
        }
        
        parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
        parameters.put("LOGO_DISTRIBUIDOR", inputStream);
        parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));
        
        return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
    }


	@Override
	@Transactional
	public void enviarEmail(FiltroEmissaoCE filtro) {
		
		Distribuidor distrib = distribuidorService.obter();
		
		if(!distribuidorService.verificarParametroDistribuidorEmissaoDocumentosEmailCheck(distrib, TipoParametrosDistribuidorEmissaoDocumento.CHAMADA_ENCALHE)){
			throw new ValidacaoException(TipoMensagem.ERROR, "CE não podem ser enviadas por e-mail, distribuidor não aceita o envio deste documento.");
		}
		
		List<ChamadaEncalheImpressaoWrapper> listaDeCEImpressao = this.gerarListaChamadaEncalheImpressaoWrapper(filtro);
		
		Map<Long, List<ChamadaEncalheImpressaoWrapper>> mapa = agruparCEporCota(listaDeCEImpressao);

		String numeroCotasSemEmail = enviarCEporEmail(mapa);
		
		String mensagem = "";
		
		if (!numeroCotasSemEmail.isEmpty()) {
			mensagem = "E-mail enviado com sucesso.\n As cotas abaixo não possuem e-mail cadastrado: \n "+ numeroCotasSemEmail +"\n";
			mensagem += "-------------------------- \n";
		}
		
		if(filtro.getValidacao() != null){
			mensagem += filtro.getValidacao().getListaMensagens();
		}
		
		if(!mensagem.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, mensagem);
		}
		
	}


	private String enviarCEporEmail(Map<Long, List<ChamadaEncalheImpressaoWrapper>> mapa) {
		String numeroCotasSemEmail = "";
		
		for (Map.Entry<Long, List<ChamadaEncalheImpressaoWrapper>> entry : mapa.entrySet()) {
			Long idCota = entry.getKey();
			List<ChamadaEncalheImpressaoWrapper> lista = entry.getValue();
			
			Cota cota = cotaService.obterPorId(idCota);
			
			lista.get(0).getEmissaoCEImpressao().get(0).getDataEmissao();
			if (cota.getPessoa().getEmail() == null) {
				if(numeroCotasSemEmail.isEmpty()){
					numeroCotasSemEmail = cota.getNumeroCota().toString();
				}else{
					numeroCotasSemEmail +=", "+ cota.getNumeroCota().toString();
				}
				continue;
			} 
			
			String[] listaDeDestinatarios = {cota.getPessoa().getEmail()};
			
			try {
				String nomeArquivo = "Chamada de Encalhe - "+DateUtil.formatarDataPTBR(new Date())+" – COTA "+cota.getNumeroCota();
				
				String assunto = "[NDS] - Emissão "+nomeArquivo;
				
				byte[] anexo = this.gerarDocumentoEmissaoCE(lista);
				
				AnexoEmail anexoPDF = new AnexoEmail(nomeArquivo, anexo, TipoAnexo.PDF);
				emailSerice.enviar(assunto, "Olá, a chamada de encalhe segue anexo.", listaDeDestinatarios, anexoPDF);
			} catch ( AutenticacaoEmailException | JRException | URISyntaxException e) {
				e.printStackTrace();
			}
			System.out.println("CHAMADA DE ENCALHE – COTA "+cota.getNumeroCota());
		}
		return numeroCotasSemEmail;
	}


	private Map<Long, List<ChamadaEncalheImpressaoWrapper>> agruparCEporCota(
			List<ChamadaEncalheImpressaoWrapper> listaDeCEImpressao) {
		Map<Long, List<ChamadaEncalheImpressaoWrapper>> mapa = new HashMap<>();
		
		for (ChamadaEncalheImpressaoWrapper chamadaEncalheImpressaoWrapper : listaDeCEImpressao) {
			List<CotaEmissaoDTO> emissaoCEImpressao = chamadaEncalheImpressaoWrapper.getEmissaoCEImpressao();
			for (CotaEmissaoDTO dto : emissaoCEImpressao) {
				Long idCota = dto.getIdCota();
				
				if (mapa.containsKey(idCota)) {
					List<ChamadaEncalheImpressaoWrapper> listaDeChamadaDeEncalhe = mapa.get(idCota);
					listaDeChamadaDeEncalhe.add(chamadaEncalheImpressaoWrapper);
					mapa.put(idCota, listaDeChamadaDeEncalhe);
				} else {
					List<ChamadaEncalheImpressaoWrapper> listaNovaDeChamadaDeEncalhe = new ArrayList<>();
					listaNovaDeChamadaDeEncalhe.add(chamadaEncalheImpressaoWrapper);
					mapa.put(idCota, listaNovaDeChamadaDeEncalhe);
				}
				
			}
		}
		return mapa;
	}
}