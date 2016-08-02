package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.InformeLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/informeLancamento")
@Rules(Permissao.ROLE_LANCAMENTO_INFORME_LANCAMENTO)
public class InformeLancamentoController extends BaseController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	@Path("/")
	public void index() {
		result.include("fornecedores", fornecedorService.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
        result.include("data", DateUtil.formatarDataPTBR(new Date()));
		
	}
	
	@Post("/pesquisarInformeLancamento")
	public void pesquisarInformeLancamento(Long idFornecedor, Integer semanaLancamento, Calendar dataLancamento, String sortname, String sortorder, int rp, int page) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);

		Calendar dataInicioLancamento = null, dataFimLancamento = null;
        
		if ((semanaLancamento == null) && (dataLancamento == null)) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe [Semana] ou [Data Lançamento]"));
        }
		
		if (semanaLancamento != null) {
            
            final String strSemanaRecolhimento = semanaLancamento.toString();
            
            if (strSemanaRecolhimento.length() != 6) {
                
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Semana deve estar no padrão ano+semana (Ex: semana 4 de 2016, 201604)"));
            }
            
            dataInicioLancamento = Calendar.getInstance();
            dataFimLancamento = Calendar.getInstance();
            
            final Intervalo<Date> intervalo = obterDataDaSemana(strSemanaRecolhimento);
            dataInicioLancamento.setTime(intervalo.getDe());
            dataFimLancamento.setTime(intervalo.getAte());
            
        } else if (dataLancamento != null) {
            dataInicioLancamento = dataLancamento;
            dataFimLancamento = dataLancamento;
        }
		
        
        TableModel<CellModelKeyValue<InformeLancamentoDTO>> tableModel = montarTableModelInformeLancamento(idFornecedor, dataInicioLancamento, dataFimLancamento, paginacao);
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<InformeLancamentoDTO>> montarTableModelInformeLancamento (Long idFornecedor, Calendar dataInicial, Calendar dataFinal, PaginacaoVO paginacao) {
	
		List<InformeLancamentoDTO> informes = lancamentoService.buscarInformeLancamento(idFornecedor, dataInicial, dataFinal, paginacao);
	
		if (informes == null || informes.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Registros não encontrados."));
		}
	
		TableModel<CellModelKeyValue<InformeLancamentoDTO>> tableModel = new TableModel<CellModelKeyValue<InformeLancamentoDTO>>();
	
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(informes));
	
		tableModel.setPage(paginacao.getPaginaAtual());
	
		tableModel.setTotal(paginacao.getQtdResultadosTotal());
	
		return tableModel;
	}
	
	private Intervalo<Date> obterDataDaSemana(final String anoSemana) {
	        
        final Date data = obterDataBase(anoSemana, distribuidorService.obterDataOperacaoDistribuidor());
        
        final Integer semana = Integer.parseInt(anoSemana.substring(4));
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        final Date dataInicioSemana = SemanaUtil.obterDataDaSemanaNoAno(semana, distribuidorService.inicioSemanaRecolhimento().getCodigoDiaSemana(), cal.get(Calendar.YEAR));
        
        final Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
        
        final Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
        
        return periodoRecolhimento;
	        
    }
	
	private Date obterDataBase(final String anoSemana, final Date data) {
        
        final String ano = anoSemana.substring(0,4);
        final Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.YEAR, Integer.parseInt(ano));
        
        return c.getTime();
    }
	
	@Post("/atualizarProdutoEdicao")
	public void atualizarCodigoDeBarrasOuChamadaCapaProdutoEdicao(boolean isCodigoDeBarras, Long idNumeroEdicao, String novoValor){
		
		ProdutoEdicao pe = produtoEdicaoService.buscarPorID(idNumeroEdicao);
		
		if(pe == null){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Erro ao atualizar a Edição."));
		}
		
		
		if(isCodigoDeBarras){
			pe.setCodigoDeBarras(novoValor);
		}else{
			pe.setChamadaCapa(novoValor);
		}
		
		produtoEdicaoService.alterarProdutoEdicao(pe);
		
		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.SUCCESS, "Edição atualizada com sucesso!."));
		
	}
	
	@Get
	public void exportar(FileType fileType, Long idFornecedor, Integer semanaLancamento, Calendar dataLancamento) throws IOException {

		Calendar dataInicioLancamento = null, dataFimLancamento = null;
		 
		if ((semanaLancamento == null) && (dataLancamento == null)) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe [Semana] ou [Data Lançamento]"));
        }
		
		if (semanaLancamento != null) {
            
            final String strSemanaRecolhimento = semanaLancamento.toString();
            
            if (strSemanaRecolhimento.length() != 6) {
                
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Semana deve estar no padrão ano+semana (Ex: semana 4 de 2016, 201604)"));
            }
            
            dataInicioLancamento = Calendar.getInstance();
            dataFimLancamento = Calendar.getInstance();
            
            final Intervalo<Date> intervalo = obterDataDaSemana(strSemanaRecolhimento);
            dataInicioLancamento.setTime(intervalo.getDe());
            dataFimLancamento.setTime(intervalo.getAte());
            
        } else if (dataLancamento != null) {
            dataInicioLancamento = dataLancamento;
            dataFimLancamento = dataLancamento;
        }		
		
		List<InformeLancamentoDTO> informes = lancamentoService.buscarInformeLancamento(idFornecedor, dataInicioLancamento, dataFimLancamento, null);

        FileExporter.to("Informe-Lancamento", fileType).inHTTPResponse(this.getNDSFileHeader(), null, informes, InformeLancamentoDTO.class, this.httpResponse);

		result.nothing();
	}
	
}
