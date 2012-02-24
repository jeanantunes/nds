package br.com.abril.nds.controllers.estoque;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalCota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO.ColunaOrdenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta de Notas Fiscais. 
 * 
 * @author Discover Technology
 *
 */
@Resource
public class ConsultaNotasController {
	
	/**
	 * Indicador para nota recebida.
	 */
	private static int NOTA_RECEBIDA = 1;
	
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;

	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Path("/consultaNotas")
	public void index() {
		preencherCombos();
	}

	@Path("/consultaNotas/pesquisarNotas")
	public void pesquisarNotas(FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal, int isNotaRecebida,
							   String sortorder, String sortname, int page, int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroConsultaNotaFiscal.setPaginacao(paginacao);

		filtroConsultaNotaFiscal.setColunaOrdenacao(ColunaOrdenacao.valueOf(sortname));
		
		List<NotaFiscal> listaNotasFiscais =
			notaFiscalService.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

		if (isNotaRecebida > 0) {
			filtroConsultaNotaFiscal.setNotaRecebida(isNotaRecebida == NOTA_RECEBIDA);
		}

		TableModel<NotaFiscal> tableModel = getTableModelNotasFiscais(listaNotasFiscais);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	private void preencherCombos() {

		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores();

		List<TipoNotaFiscal> tiposNotaFiscal = tipoNotaFiscalService.obterTiposNotasFiscais();

		result.include("fornecedores", fornecedores);
		result.include("tiposNotaFiscal", tiposNotaFiscal);		
	}
	
	private TableModel<NotaFiscal> getTableModelNotasFiscais(List<NotaFiscal> listaNotasFiscais) {

		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		for (NotaFiscal notaFiscal : listaNotasFiscais) {

			CellModel cellModel = 
					new CellModel(notaFiscal.getId().intValue(), notaFiscal.getNumero(), 
						simpleDateFormat.format(notaFiscal.getDataEmissao()), 
						simpleDateFormat.format(notaFiscal.getDataExpedicao()), 
						notaFiscal.getTipoNotaFiscal().getDescricao(), 
						notaFiscal.getJuridica().getRazaoSocial(), 
						StatusNotaFiscal.RECEBIDA.equals(notaFiscal.getStatusNotaFiscal()) ? "*" : " " );

			listaCellModels.add(cellModel);
		}

		TableModel<NotaFiscal> tableModel = new TableModel<NotaFiscal>();
		tableModel.setPage(1);
		tableModel.setTotal(100);
		tableModel.setRows(listaCellModels.toArray(new CellModel[listaCellModels.size()]));

		return tableModel;
	}
	
	public List<NotaFiscal> getNotasFiscais() {
		
		List<NotaFiscal> listaNotaFiscais = new ArrayList<NotaFiscal>();
		
		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("descricao");
		
		PessoaJuridica juridica = new PessoaJuridica();
		juridica.setRazaoSocial("razaoSocial");
		
		NotaFiscalCota notaFiscal = new NotaFiscalCota();
		notaFiscal.setId(1L);
		notaFiscal.setChaveAcesso("1231231231");
		notaFiscal.setNumero("333549887");
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.RECEBIDA);
		notaFiscal.setJuridica(juridica);
		listaNotaFiscais.add(notaFiscal);
		
		notaFiscal = new NotaFiscalCota();
		notaFiscal.setId(2L);
		notaFiscal.setChaveAcesso("1231231231");
		notaFiscal.setNumero("333549887");
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.RECEBIDA);
		notaFiscal.setJuridica(juridica);
		listaNotaFiscais.add(notaFiscal);
		
		notaFiscal = new NotaFiscalCota();
		notaFiscal.setId(3L);
		notaFiscal.setChaveAcesso("1231231231");
		notaFiscal.setNumero("333549887");
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.RECEBIDA);
		notaFiscal.setJuridica(juridica);
		listaNotaFiscais.add(notaFiscal);
		
		notaFiscal = new NotaFiscalCota();
		notaFiscal.setId(4L);
		notaFiscal.setChaveAcesso("1231231231");
		notaFiscal.setNumero("333549887");
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.RECEBIDA);
		notaFiscal.setJuridica(juridica);
		listaNotaFiscais.add(notaFiscal);
		
		notaFiscal = new NotaFiscalCota();
		notaFiscal.setId(5L);
		notaFiscal.setChaveAcesso("1231231231");
		notaFiscal.setNumero("333549887");
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDataExpedicao(new Date());
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.RECEBIDA);
		notaFiscal.setJuridica(juridica);
		listaNotaFiscais.add(notaFiscal);
		
		return listaNotaFiscais;
	}
}
