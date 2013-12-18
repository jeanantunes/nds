package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.CotaProdutoEmissaoCEDTO;
import br.com.abril.nds.dto.DadosImpressaoEmissaoChamadaEncalhe;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.NotaEnvioProdutoEdicao;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referentes a serviços de chamada encalhe. 
 *   
 * @author Discover Technology
 */

@Service
public class ChamadaEncalheServiceImpl implements ChamadaEncalheService {
	
	
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
	private NotaEnvioRepository notaEnvioRepository;
	
	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro) {
		
		Intervalo<Integer> intervaloCotas = null;
		
		if (filtro.getNumCotaDe() != null && filtro.getNumCotaAte() != null) {
			intervaloCotas = new Intervalo<Integer>(filtro.getNumCotaDe(), filtro.getNumCotaAte());
		}
		
		Intervalo<Date> intervaloDataRecolhimento = new Intervalo<Date>(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte());
		
		this.cotaService.verificarCotasSemRoteirizacao(intervaloCotas, null, intervaloDataRecolhimento);
		
		List<CotaEmissaoDTO> listaChamadaEncalhe = chamadaEncalheRepository.obterDadosEmissaoChamadasEncalhe(filtro);
		
		if (listaChamadaEncalhe == null) return new ArrayList<>();
		
		for(int i = 0; i < listaChamadaEncalhe.size(); i++) {
			
			if(listaChamadaEncalhe.get(i).getQtdeExemplares() <= 0) {
				
				listaChamadaEncalhe.remove(i);
			}
		}
		
		return listaChamadaEncalhe;
		
	}

    /**
     * Obtem endereço do PDV principal da Cota, caso não encontre, obtem o endereço principal da Cota.
     * @param cota
     * @return Endereco
     */
	private Endereco obterEnderecoImpressaoCE(Cota cota){
		
		Endereco endereco = null;
		
		PDV pdvPrincipal = this.pdvRepository.obterPDVPrincipal(cota.getId());

		EnderecoPDV enderecoPdv = pdvPrincipal!=null?pdvPrincipal.getEnderecoEntrega():null;
		
		if (enderecoPdv == null) {
		
			for (EnderecoPDV ePdv : pdvPrincipal.getEnderecos()){
			    
				if (ePdv.isPrincipal()){
				    
					enderecoPdv = ePdv;
				}
			}
		}

		if (enderecoPdv != null) {
			
			return enderecoPdv.getEndereco();
		}
		
		for(EnderecoCota enderecoCota : cota.getEnderecos()){
			
			if (enderecoCota.isPrincipal()){
				
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
		
		
		for(ProdutoEmissaoDTO produtoDTO : cota.getProdutos()) {
			
			idsProdutoEdicao.add(produtoDTO.getIdProdutoEdicao());
			
			if(!produtoDTO.isApresentaQuantidadeEncalhe()) {
				produtoDTO.setQuantidadeDevolvida(null);
			}
			
			produtoDTO.setReparte( (produtoDTO.getReparte()==null) ? BigInteger.ZERO : produtoDTO.getReparte() );
			
			produtoDTO.setVlrDesconto( (produtoDTO.getVlrDesconto() == null) ? BigDecimal.ZERO :  produtoDTO.getVlrDesconto());
			
			produtoDTO.setQuantidadeDevolvida(  (produtoDTO.getQuantidadeDevolvida() == null) ? BigInteger.ZERO : produtoDTO.getQuantidadeDevolvida());
			
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
				
				String descricaoQuebraRelatorioCE = cpece.getReparte() +" exes. ("+ DateUtil.formatarData(cpece.getDataMovimento(), Constantes.DAY_MONTH_PT_BR) +")"; //obterDescricaoQuebraRelatorioCE(notas);
				
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
			
			String descricaoQuebraRelatorioCE = obterDescricaoQuebraRelatorioCE(notas);
			
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
	private String obterDescricaoQuebraRelatorioCE(List<NotaEnvioProdutoEdicao> notas ) {
		
		StringBuffer descricao = new StringBuffer();
		
		String plusSignal = "";
		
		for(NotaEnvioProdutoEdicao nota : notas) {
			
			if(nota.getReparte() == null || nota.getReparte().intValue() < 1)
				continue;
			
			descricao.append(plusSignal);
			
			if(nota.getReparte()!=null) {
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
	
	private List<ProdutoEmissaoDTO> obterProdutosEmissaoCE(FiltroEmissaoCE filtro, Long idCota) {
		
		List<Date> datasControleFechamentoEncalhe = 
				fechamentoEncalheRepository.
				obterDatasControleFechamentoEncalheRealizado(
						filtro.getDtRecolhimentoDe(), 
						filtro.getDtRecolhimentoAte());
		
		List<Date> datasControleConferenciaEncalheCotaFinalizada = 
				controleConferenciaEncalheCotaRepository.
				obterDatasControleConferenciaEncalheCotaFinalizada(idCota, 
						filtro.getDtRecolhimentoDe(), 
						filtro.getDtRecolhimentoAte());

		
		return chamadaEncalheRepository.obterProdutosEmissaoCE(
				filtro,
				idCota, 
				datasControleFechamentoEncalhe, 
				datasControleConferenciaEncalheCotaFinalizada);
	
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
		
		for(CotaEmissaoDTO dto : lista) {
			
<<<<<<< HEAD
			cota = cotaRepository.obterPorNumeroDaCota( dto.getNumCota());
=======
			cota = cotaRepository.obterPorNumerDaCota(dto.getNumCota());
>>>>>>> DGB/master

			Endereco endereco = this.obterEnderecoImpressaoCE(cota);

			if(endereco != null) {
				dto.setEndereco( (endereco.getTipoLogradouro()!= null?endereco.getTipoLogradouro().toUpperCase() + ": " :"")
									+ endereco.getLogradouro().toUpperCase()  + ", " + endereco.getNumero());
				dto.setUf(endereco.getUf());
				dto.setCidade(endereco.getCidade());
				dto.setUf(endereco.getUf());
				dto.setCep(endereco.getCep());
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
			
			dto.setProdutos( obterProdutosEmissaoCE(filtro, dto.getIdCota()) );
			
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
		
		return obterListaPaginada(capasChamadaEncalhe, qtdCapasPorPagina);
		
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
		
		return chamadaEncalheRepository.obterBandeirasNoIntervalo(periodoRecolhimento, fornecedor, paginacaoVO);
	}
	
	@Override
	@Transactional
	public Long countObterBandeirasDaSemana(Integer semana) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.countObterBandeirasNoIntervalo(periodoRecolhimento);
	}
	
	@Override
	@Transactional
	public List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana) {
		
		Intervalo<Date> periodoRecolhimento = null;
		
		try {
			periodoRecolhimento = recolhimentoService.getPeriodoRecolhimento(semana);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		return chamadaEncalheRepository.obterDadosFornecedoresParaImpressaoBandeira(periodoRecolhimento);
	}
}