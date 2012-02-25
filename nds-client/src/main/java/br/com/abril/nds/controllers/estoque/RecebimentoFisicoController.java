package br.com.abril.nds.controllers.estoque;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.util.Constantes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
public class RecebimentoFisicoController {
	@Autowired
	private Result result;
	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private NotaFiscalService notaFiscalService;
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private Validator validator;

	public void recebimentoFisico(Result result,
			FornecedorService fornecedorService, PessoaService pessoaService,
			NotaFiscalService notaFiscalService,
			RecebimentoFisicoService recebimentoFisicoServer,
			PessoaJuridicaService pessoaJuridicaService, 
			Validator validator) {
		this.result = result;
		this.fornecedorService = fornecedorService;
		this.pessoaService = pessoaService;
		this.notaFiscalService = notaFiscalService;
		this.recebimentoFisicoService = recebimentoFisicoServer;
		this.pessoaJuridicaService = pessoaJuridicaService;
		this.validator = validator;
	}

	@Path("/recebimentoFisico")
	public void index() throws ParseException {

		preencherCombos();
		preencherDataEmissao();
	}

	private void preencherDataEmissao() {
		// RecebimentoFisico recebimento = new RecebimentoFisico();
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyyy");

		result.include("dataAtual", formatarDate.format(data));

	}

	// metodo para gerar uma lista com sugestao
	@Post
	public void buscaCnpj(String cnpj) {
		
		PessoaJuridica pessoaJuridica = pessoaJuridicaService.buscarPorCnpj(cnpj);
		 if(validarCnpj(pessoaJuridica, cnpj)){
			 result.use(Results.json()).from(pessoaJuridica, "result").serialize();
		 }
						
	}
	private boolean validarCnpj(PessoaJuridica pessoaJuridica, String cnpj){
		boolean isValido = true;
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (cnpj == null){
			isValido = false;
			listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			listaMensagemValidacao.add("CNPJ é obrigatório!");
		}else if(pessoaJuridica == null){
			isValido = false;
			listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			listaMensagemValidacao.add("CNPJ não encontrado!");
		}else{
			result.use(Results.json()).from(pessoaJuridica, Constantes.PARAM_MSGS).serialize();
		}
		return isValido;
	}
	
	public void preencherCombos() {
		List<Fornecedor> fornecedores = fornecedorService
				.obterFornecedoresAtivos();
		if (fornecedores != null) {
			result.include("listafornecedores", fornecedores);
		}
		
		List<String> listaTipoLancamento = new ArrayList<String>();
		for(TipoLancamento obj: TipoLancamento.values()){
			listaTipoLancamento.add(obj.name());
		}			
		result.include("listaTipoLancamento",listaTipoLancamento);
	}

	
	@Get
	public List<RecebimentoFisico> consulta() {
		return null;
	}

	@Post
	@Path("/recebimentoFisico/inserirNota")
	public void inserirNotaFiscal(NotaFiscalFornecedor notaFiscalFornecedor,
			RecebimentoFisico recebimentoFisico) throws ParseException {

		System.out.println("@@@@@@@@@@@@@@@@@@@@"
				+ notaFiscalFornecedor.getDataEmissao());
		/*
		 * validator.checking(new Validations() {{
		 * that(!"".equals(notaFiscalFornecedor.getDataEmissao()),
		 * "produto.nome", "nome.vazio"); //that(produto.getPreco() > 0,
		 * "produto.preco", "preco.invalido"); }});
		 * validator.onErrorUsePageOf(RecebimentoFisicoController
		 * .class).index();
		 */

		notaFiscalService.inserirNotaFiscal(notaFiscalFornecedor);

		notaFiscalFornecedor.setOrigem(Origem.MANUAL);

		recebimentoFisico.setNotaFiscal(notaFiscalFornecedor);

		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.PENDENTE);

		// receber o Usuario que inseriu a nota
		Usuario usuario = new Usuario();
		usuario.setId(1L);

		//recebimentoFisico.setUsuario(usuario);

		recebimentoFisicoService.adicionarRecebimentoFisico(recebimentoFisico);
		result.redirectTo("/recebimentoFisico");
	}

	@Path("recebimentoFisico/pesquisa")
	public List<RecebimentoFisicoDTO> pesquisaRecebimentoFisico(Fornecedor fornecedor, NotaFiscal notaFiscal) throws Exception {
		return recebimentoFisicoService.obterRecebimentoFisico(fornecedor.getJuridica().getCnpj(), notaFiscal.getNumero(), notaFiscal.getSerie());		
		
	}

}
