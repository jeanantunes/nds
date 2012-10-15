package br.com.abril.nds.controllers.nfe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/nfe/impressaoNFE")
public class ImpressaoNFEController {

	@SuppressWarnings("unused")
	@Autowired
	private HttpSession session;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;

	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	ImpressaoNFEService impressaoNFEService;

	@Autowired
	DistribuidorService distribuidorService;

	@Autowired
	RoteiroService roteiroService;

	@Autowired
	RotaService rotaService;

	@Autowired
	private Result result;

	@Path("/")
	@Rules(Permissao.ROLE_NFE_IMPRESSAO_NFE)
	public void index() {

		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		this.result.include("fornecedores", fornecedores);
		this.result.include("tipoNotas", tipoNotaFiscalService.carregarComboTiposNotasFiscais(TipoOperacao.SAIDA));
		this.result.include("roteiros", roteiroService.obterRoteiros());
		this.result.include("rotas", rotaService.obterRotas());
		this.result.include("tipoEmissao", TipoEmissaoNfe.values());
		this.result.include("dataAtual", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

	}

	@Post
	public void pesquisarImpressaoNFE(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname, int page, int rp){

		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		if(!filtro.isFiltroValido()) {

			List<String> listaMensagemValidacao = new ArrayList<String>();

			HashMap<String, String> erros = filtro.validarFiltro();
			for(Iterator<String> i = erros.keySet().iterator(); i.hasNext(); ) {
				listaMensagemValidacao.add( erros.get( i.next() ) );
			}

			if (!listaMensagemValidacao.isEmpty()) {
				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
				throw new ValidacaoException(validacaoVO);
			}

		}

		//notaFiscalService.obterItensNotaFiscalPor(distribuidor, cota, periodo, listaIdFornecedores, listaIdProdutos, tipoNotaFiscal)
		TableModel<CellModelKeyValue<NfeImpressaoDTO>> tableModel = new TableModel<CellModelKeyValue<NfeImpressaoDTO>>();

		List<NfeImpressaoDTO> listaNFe = notaFiscalService.buscarNFeParaImpressao(filtro);
		
		tableModel.setTotal(notaFiscalService.buscarNFeParaImpressaoTotalQtd(filtro));

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNFe));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

	/**
	 * Metodos utilitarios
	 */
	@Post
	public void carregarRotasImpressaoNFE(Long idRoteiro, String sortname, Ordenacao ordenacao) {

		List<ItemDTO<Long, String>> listaItensRotas = new ArrayList<ItemDTO<Long,String>>();

		for (Rota rota : (idRoteiro != null && idRoteiro > -1) ? rotaService.buscarRotaPorRoteiro(idRoteiro) : rotaService.obterRotas()) {

			ItemDTO<Long, String> rotaVO = new ItemDTO<Long, String>();

			rotaVO.setKey(rota.getId());
			rotaVO.setValue(rota.getDescricaoRota());

			listaItensRotas.add(rotaVO);
		}

		this.result.include("rotas", (idRoteiro != null && idRoteiro > -1) ? rotaService.buscarRotaPorRoteiro(idRoteiro) : rotaService.obterRotas());

		result.use(Results.json()).withoutRoot().from(listaItensRotas).recursive().serialize();
	}

	@Post
	public void pesquisarProdutosImpressaoNFE(String sortname, String sortorder, String codigoProduto, String nomeProduto, int page, int rp) {

		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		TableModel<CellModelKeyValue<ProdutoLancamentoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoLancamentoDTO>>();

		long start = System.currentTimeMillis();
		@SuppressWarnings("deprecation")
		List<ProdutoLancamentoDTO> listaProdutoLancamentoUnordered = impressaoNFEService.obterProdutosExpedicaoConfirmada(fornecedores, new Date(112,9,11));

		ordenarLista(sortname, sortorder, listaProdutoLancamentoUnordered);
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoRefinada = buscarProdutosNaLista(listaProdutoLancamentoUnordered, codigoProduto, nomeProduto);

		List<ProdutoLancamentoDTO> listaProdutoLancamento = removerItensDuplicados(listaProdutoLancamentoRefinada);

		long end = System.currentTimeMillis();
		System.out.printf("Total: %.3f ms%n", (end - start) / 1000d);  

		tableModel.setTotal(listaProdutoLancamento.size());

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaProdutoLancamento));

		tableModel.setPage(page);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}


	/**
	 * Metodos utilitarios
	 * 
	 * @param sortname
	 * @param sortorder
	 * @param listaProdutoLancamento
	 */
	@SuppressWarnings("unchecked")
	private void ordenarLista(String sortname, String sortorder, List<ProdutoLancamentoDTO> listaProdutoLancamento) {
		if(sortname != null && !sortname.isEmpty())
			Collections.sort(listaProdutoLancamento, new BeanComparator(sortname, new NullComparator()));

		if(sortorder != null && !sortorder.isEmpty() && sortorder.equals("desc"))
			Collections.reverse(listaProdutoLancamento);
	}

	private List<ProdutoLancamentoDTO> removerItensDuplicados(List<ProdutoLancamentoDTO> listaProdutoLancamentoUnordered) {
		Set<ProdutoLancamentoDTO> s = new TreeSet<ProdutoLancamentoDTO>(new Comparator<ProdutoLancamentoDTO>() {
			@Override
			public int compare(ProdutoLancamentoDTO o1, ProdutoLancamentoDTO o2) {
				if(o1.getCodigoProduto().equalsIgnoreCase(o2.getCodigoProduto()))
					return 0;
				if(o1.getCodigoProduto().equalsIgnoreCase(o2.getCodigoProduto()))
					return -1;
				else
					return 1;
			}
		});

		s.addAll(listaProdutoLancamentoUnordered);

		List<ProdutoLancamentoDTO> listaProdutoLancamento = new ArrayList<ProdutoLancamentoDTO>(s);

		return listaProdutoLancamento;
	}

	private List<ProdutoLancamentoDTO> buscarProdutosNaLista(List<ProdutoLancamentoDTO> listaProdutoLancamento, String codigoProduto, String nomeProduto) {

		if( StringUtils.isEmpty(codigoProduto) && StringUtils.isEmpty(nomeProduto) ) {
			return listaProdutoLancamento;
		}
		
		StringUtils.isEmpty(nomeProduto);
		
		List<ProdutoLancamentoDTO> result = new ArrayList<ProdutoLancamentoDTO>();
		
		for( ProdutoLancamentoDTO pl : listaProdutoLancamento ) {
			if( !StringUtils.isEmpty(codigoProduto) && !StringUtils.isEmpty(nomeProduto) ) {
				if( StringUtils.startsWithIgnoreCase(pl.getCodigoProduto(), codigoProduto) 
						&& StringUtils.startsWithIgnoreCase(pl.getNomeProduto(), nomeProduto) ) { 
					result.add( pl );
				}
			} else if( !StringUtils.isEmpty(codigoProduto) && StringUtils.isEmpty(nomeProduto) ) { 
				if( StringUtils.startsWithIgnoreCase(pl.getCodigoProduto(), codigoProduto) ) {
					result.add( pl );
				}
			} else if( !StringUtils.isEmpty(nomeProduto) && StringUtils.isEmpty(codigoProduto) ) { 
				if( StringUtils.startsWithIgnoreCase(pl.getNomeProduto(), nomeProduto) ) {
					result.add( pl );
				}
			}
		}
		return result;
	}

}
