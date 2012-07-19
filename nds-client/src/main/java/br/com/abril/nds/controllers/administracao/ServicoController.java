package br.com.abril.nds.controllers.administracao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoServicoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Periodicidade;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.service.TipoEntregaService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de serviços.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/servico/cadastroServico")
public class ServicoController {

	@Autowired
	private Result result;

	@Autowired
	private TipoEntregaService tipoEntregaService;
	
	/**
	 * Método chamado assim que iniciada a tela.
	 */
	@Path("/")
	public void index() {
		
	}
	
	/**
	 * Método responsável por pesquisar serviços.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param periodicidade
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Path("/pesquisarServicos")
	public void pesquisarServicos(Long codigo, 
								  String descricao, 
								  String periodicidade,
								  String sortorder, 
								  String sortname, 
								  int page, 
								  int rp) {
		try {	
			
			descricao = this.validarDescricao(descricao);
			
			sortname = this.getSortName(sortname);
			
			int startSearch = page * rp - rp;
			
			List<TipoEntrega> listaTipoEntrega =
				this.tipoEntregaService.pesquisarTiposEntrega(
						codigo, descricao, periodicidade, 
						sortname, sortorder, startSearch, rp);
			
			int total = 
				this.tipoEntregaService.pesquisarQuantidadeTiposEntrega(
					codigo, descricao, periodicidade);
			
			this.processarServicos(listaTipoEntrega, total, page);
			
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao Pesquisar os Serviços de Entrega."));
		}
	}
	
	/**
	 * Método responsável por salvar um serviço
	 * 
	 * @param codigo
	 * @param descricao
	 * @param taxaFixa
	 * @param isento
	 * @param periodicidade
	 * @param baseCalculo
	 * @param percentualCalculo
	 */
	@Post
	public void salvarServico(Long id, String descricao, BigDecimal taxaFixa, Float percentualFaturamento,
			String baseCalculo, String periodicidadeCadastro, Integer diaSemana, Integer diaMes, String cobranca) {
		
		this.validarServico(descricao, taxaFixa, baseCalculo, percentualFaturamento, cobranca, periodicidadeCadastro, diaSemana, diaMes);
		
		String mensagem = null;
		
		try {
			
			if (id == null) {
				mensagem = "Serviço de Entrega incluído com sucesso!";
			} else {
				mensagem = "Serviço de Entrega atualizado com sucesso!";
			}			
			
			this.tipoEntregaService.salvarTipoEntrega(
				id, descricao, taxaFixa, percentualFaturamento, 
				baseCalculo, periodicidadeCadastro, diaSemana, diaMes);
		
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao salvar o Serviço de Entrega!"));
		}
		
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, mensagem), 
							"result").recursive().serialize();
	}
	
	/**
	 * Método responsável por remover um serviço.
	 * 
	 * @param id
	 */
	@Post
	public void removerServico(Long id) {
		
		try {
			
			if (id == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Serviço não encontrado.");
			}
			
			this.tipoEntregaService.removerTipoEntrega(id);
		
		} catch (UniqueConstraintViolationException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Serviço de Entrega não pode ser excluído."));	
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao excluir o Serviço de Entrega!"));
		}
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Serviço de Entrega excluído com sucesso."), 
								"result").recursive().serialize();
	}
	
	/**
	 * Método capaz de buscar um serviço pelo ID.
	 * 
	 * @param id
	 */
	@Post
	public void buscarServicoPeloCodigo(Long id) {
		
		if (id == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Serviço de Entrega não encontrado.");
		}
		
		TipoEntrega tipoEntrega = 
			this.tipoEntregaService.obterTipoEntrega(id);
		
		ResultadoServicoVO resultadoServicoVO = new ResultadoServicoVO();
		
		resultadoServicoVO.setId(tipoEntrega.getId());
		resultadoServicoVO.setCodigo(tipoEntrega.getId().toString());
		resultadoServicoVO.setDescricao(tipoEntrega.getDescricao());
		
		BigDecimal taxaFixa = tipoEntrega.getTaxaFixa();
		
		if (taxaFixa == null) {
			
			if (tipoEntrega.getPercentualFaturamento() != null && 
					tipoEntrega.getBaseCalculo() != null) {
				
				resultadoServicoVO.setTipoCobranca("PF");
				resultadoServicoVO.setBaseCalculo(tipoEntrega.getBaseCalculo().getKey());
				resultadoServicoVO.setPercentualCalculoBase(tipoEntrega.getPercentualFaturamento().toString());
			}
		} else {
			resultadoServicoVO.setTipoCobranca("TF");
			resultadoServicoVO.setTaxa(taxaFixa.toString());
		}

		Periodicidade periodicidadeContrato = tipoEntrega.getPeriodicidade();
		
		if (("S").equals(periodicidadeContrato.getValue())) {
			resultadoServicoVO.setPeriodicidade("S");
			resultadoServicoVO.setDiaSemana(tipoEntrega.getDiaSemana().getCodigoDiaSemana());
		} else if (("M").equals(periodicidadeContrato.getValue())) {
			resultadoServicoVO.setPeriodicidade("M");
			resultadoServicoVO.setDiaMes(tipoEntrega.getDiaMes());
		} else {
			resultadoServicoVO.setPeriodicidade("D");
		}
		
		this.result.use(Results.json()).from(resultadoServicoVO, "result").recursive().serialize();
	}
	
	/**
	 * Método capaz de validar um serviço.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param taxaFixa
	 * @param isento
	 * @param periodicidade
	 * @param baseCalculo
	 * @param percentualFaturamento
	 */
	private void validarServico(String descricao, BigDecimal taxaFixa, String baseCalculo, 
			Float percentualFaturamento, String cobranca, String periodicidadeCadastro, 
			Integer diaSemana, Integer diaMes) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (descricao == null || descricao.trim().isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Descrição] é obrigatório!");
		} else {
			descricao = this.validarDescricao(descricao);
		}
		
		if (cobranca == null  || cobranca.isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Cobrança] é obrigatório!");
		}
		
		if ("TF".equals(cobranca)) {

			if (taxaFixa == null || BigDecimal.ZERO.compareTo(taxaFixa) > 0) {
				listaMensagens.add("O preenchimento do campo [Taxa Fixa R$] é obrigatório!");
			}
			
		} else if ("PF".equals(cobranca)) {

			if (baseCalculo == null || baseCalculo.isEmpty()) {
				listaMensagens.add("O preenchimento do campo [Base de Cálculo] é obrigatório!");
			}
			
			if (percentualFaturamento == null || percentualFaturamento <= 0) {
				listaMensagens.add("O preenchimento do campo [(%) para cálculo sobre base] é obrigatório!");
			}

			if (percentualFaturamento != null && percentualFaturamento > 100) {
				listaMensagens.add("O campo [Percentual Faturamento] é inválido!");
			}
		}
		
		if (periodicidadeCadastro == null || periodicidadeCadastro.isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Periodicidade] é obrigatório!");
		}
		
		if (Periodicidade.SEMANAL.getValue().equals(periodicidadeCadastro)) {
			
			if (diaSemana == null || diaSemana == 0) {
				listaMensagens.add("Selecione um dia da semana.");
			}
		} else if (Periodicidade.MENSAL.getValue().equals(periodicidadeCadastro)) {
			
			if (diaMes == null || diaMes == 0) {
				listaMensagens.add("Insira um dia do mes.");
			}
		}
		
		if (listaMensagens != null && !listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
	}
	
	/**
	 * Método capaz de processar o serviço para exibir na tela.
	 * 
	 * @param listaServicos
	 */
	private void processarServicos(List<TipoEntrega> listaTipoEntrega, int total, int page) {

		List<ResultadoServicoVO> listaServicos = new ArrayList<ResultadoServicoVO>();
		
		for (TipoEntrega tipoEntrega : listaTipoEntrega) {
			
			ResultadoServicoVO resultadoServicoVO = new ResultadoServicoVO();

			resultadoServicoVO.setId(tipoEntrega.getId());
			resultadoServicoVO.setCodigo(tipoEntrega.getId().toString());
			resultadoServicoVO.setDescricao(tipoEntrega.getDescricao());
			
			BigDecimal taxaFixa = tipoEntrega.getTaxaFixa();
			
			if (taxaFixa != null) {
				resultadoServicoVO.setTaxa(CurrencyUtil.formatarValor(taxaFixa));
			} else {
				resultadoServicoVO.setTaxa("");
			}
			
			resultadoServicoVO.setBaseCalculo(tipoEntrega.getBaseCalculo() != null ? tipoEntrega.getBaseCalculo().getValue() : "");
			
			Float percentualFaturamento = tipoEntrega.getPercentualFaturamento();
			
			if(percentualFaturamento != null) {
				String value = ""+percentualFaturamento.intValue();
				resultadoServicoVO.setPercentualCalculoBase(value);
			} else {
				resultadoServicoVO.setPercentualCalculoBase("");
			}
			
			listaServicos.add(resultadoServicoVO);
		}

		TableModel<CellModelKeyValue<ResultadoServicoVO>> tableModel =
				new TableModel<CellModelKeyValue<ResultadoServicoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaServicos));
		tableModel.setPage(page);
		tableModel.setTotal(total);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private String getSortName(String sortname) {
		
		if ("codigo".equals(sortname)) {
			return "id";
		} else if ("taxa".equals(sortname)) {
			return "taxaFixa";
		} else if ("percentualCalculoBase".equals(sortname)) {
			return "percentualFaturamento";
		}
		
		return sortname;
	}

	private String validarDescricao(String descricao) {
		
		if (descricao != null && !descricao.isEmpty()) {
			
			if (descricao.contains("%")) {
				descricao = descricao.replace("%", "");
			}
			
			if (descricao.length() > 256) {
				throw new ValidacaoException(TipoMensagem.ERROR, "O campo [Descrição] excedeu o limite permitido!");
			}
		
			descricao = descricao.trim();
		}
		
		return descricao;
	}
		
}
