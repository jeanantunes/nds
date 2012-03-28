package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/financeiro/debitoCreditoCota")
public class DebitoCreditoCotaController {

	@Autowired
	private Result result;
	
	@Autowired
	private CotaService cotaService;

	@Autowired
	private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;

	@Path("/")
	public void index() { 

		preencherComboTipoMovimento();
	}

	private void preencherComboTipoMovimento() {

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiro = 
				this.tipoMovimentoFinanceiroService.obterTodosTiposMovimento();

		this.result.include("tiposMovimentoFinanceiro", tiposMovimentoFinanceiro);
	}

	public void buscarCotaPorNumero(Integer numeroCota) {
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Cota inexistente.");
		}

		Pessoa pessoa = cota.getPessoa();

		String nomeCota = null;

		if (pessoa instanceof PessoaFisica) {

			nomeCota = ((PessoaFisica) pessoa).getNome();

		} else if (pessoa instanceof PessoaJuridica) {

			nomeCota = ((PessoaJuridica) pessoa).getRazaoSocial();
		}

		this.result.use(Results.json()).from(nomeCota, "result").recursive().serialize();
	}
	
	@Post
	public void pesquisarDebitoCredito(FiltroDebitoCreditoDTO filtroDebitoCredito, 
									   String sortorder, String sortname, int page, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		ColunaOrdenacao colunaOrdenacao = Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname.trim());
		
		filtroDebitoCredito.setPaginacao(paginacao);
		filtroDebitoCredito.setColunaOrdenacao(colunaOrdenacao);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaService.obterMovimentosFinanceiroCota(filtroDebitoCredito);
		
		Integer quantidadeRegistros =  
				this.movimentoFinanceiroCotaService.obterContagemMovimentosFinanceiroCota(filtroDebitoCredito);
		
		TableModel<CellModel> tableModel = getTableModel(listaMovimentoFinanceiroCota);
		
		tableModel.setPage(page);
		tableModel.setTotal(quantidadeRegistros);

		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	public void removerMovimentoFinanceiroCota(Long idMovimento) {

		if (idMovimento == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "ID do movimento inválido.");
		}

		this.movimentoFinanceiroCotaService.removerMovimentoFinanceiroCota(idMovimento);

		this.result.nothing();
	}

	@Post
	public void cadastrarMovimentoFincanceiroCota(DebitoCreditoDTO debitoCredito) {
		
		this.movimentoFinanceiroCotaService.cadastrarMovimentoFincanceiroCota(debitoCredito);
		
		this.result.nothing();
	}
	
	@Post
	public void prepararMovimentoFinanceiroCotaParaEdicao(Long idMovimento) {

		if (idMovimento == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "ID do movimento inválido.");
		}

		MovimentoFinanceiroCota movimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaService.obterMovimentoFinanceiroCotaPorId(idMovimento);

		DebitoCreditoDTO debitoCredito = new DebitoCreditoDTO();

		Pessoa pessoa = movimentoFinanceiroCota.getCota().getPessoa();

		String nomeCota = pessoa instanceof PessoaJuridica ? 
						  ((PessoaJuridica) pessoa).getRazaoSocial() : ((PessoaFisica) pessoa).getNome();

		debitoCredito.setDataLancamento(formatField(movimentoFinanceiroCota.getDataCriacao()));
		debitoCredito.setTipoMovimentoFinanceiro(movimentoFinanceiroCota.getTipoMovimento());
		debitoCredito.setDataVencimento(formatField(movimentoFinanceiroCota.getData()));
		debitoCredito.setNumeroCota(movimentoFinanceiroCota.getCota().getNumeroCota());
		debitoCredito.setObservacao(movimentoFinanceiroCota.getObservacao());
		debitoCredito.setValor(movimentoFinanceiroCota.getValor());
		debitoCredito.setId(movimentoFinanceiroCota.getId());
		debitoCredito.setNomeCota(nomeCota);

		this.result.use(Results.json()).from(debitoCredito, "result").recursive().serialize();
	}

	private TableModel<CellModel> getTableModel(List<MovimentoFinanceiroCota> listaDebitoCredito) {
	
		List<CellModel> listaCellModel = new ArrayList<CellModel>();
		
		for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaDebitoCredito) {

			String dataLancamento = 
					formatField(movimentoFinanceiroCota.getDataCriacao());
			String dataVencimento = 
					formatField(movimentoFinanceiroCota.getData());
			String numeroCota = 
					formatField(movimentoFinanceiroCota.getCota().getNumeroCota());
			String tipoMovimento = 
					formatField(movimentoFinanceiroCota.getTipoMovimento().getDescricao());
			String valor = 
					formatField(movimentoFinanceiroCota.getValor());
			String observacao = 
					formatField(movimentoFinanceiroCota.getObservacao());
			
			Pessoa pessoa = movimentoFinanceiroCota.getCota().getPessoa();
			
			String nomeCota = pessoa instanceof PessoaJuridica ? 
							  ((PessoaJuridica) pessoa).getRazaoSocial() : ((PessoaFisica) pessoa).getNome();

			CellModel cellModel = new CellModel(
				movimentoFinanceiroCota.getId().intValue(),
				dataLancamento,
				dataVencimento,
				numeroCota,
				nomeCota,
				tipoMovimento,
				valor,
				observacao
			);

			listaCellModel.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		tableModel.setRows(listaCellModel);
		
		tableModel.setTotal(listaCellModel.size());

		return tableModel;
	}

	private String formatField(Object field) {
		
		if (field instanceof Date) {
			
			return field == null ? "" : DateUtil.formatarDataPTBR((Date) field);
		}
		
		return field == null ? "" : String.valueOf(field);
	}
}

