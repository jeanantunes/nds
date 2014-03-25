package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/fiador")
public class SociosController extends BaseController {
	
	public static final String LISTA_SOCIOS_EXIBIR_SESSAO = "listaSociosExibirSessao";

	public static final String LISTA_SOCIOS_SALVAR_SESSAO = "listaSociosSalvarSessao";
	
	public static final String LISTA_SOCIOS_REMOVER_SESSAO = "listaSociosRemoverSessao";
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private FiadorService fiadorService;
	
	private Result result;
	
	private HttpSession httpSession;
	
	private Validator validator;
	
	public class SocioCadastrado {
		private PessoaFisica pessoa;
		
		private Integer referencia;

		public PessoaFisica getPessoa() {
			return pessoa;
		}

		public void setPessoa(PessoaFisica pessoa) {
			this.pessoa = pessoa;
		}

		public Integer getReferencia() {
			return referencia;
		}

		public void setReferencia(Integer referencia) {
			this.referencia = referencia;
		}
	}
	
	public SociosController(Result result, HttpSession httpSession, Validator validator){
		
		this.result = result;
		this.httpSession = httpSession;
		this.validator = validator;
	}
	
	private void validarDadosEntrada(PessoaFisica pessoa, Long ref) {
		
		List<String> msgsValidacao = new ArrayList<String>();
		
		CPFValidator cpfValidator = new CPFValidator(true);
		
		if (pessoa == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "CPF é obrigatório.");
		}
		
		List<SocioCadastrado> listaSociosSessao = this.obterListaSociosSalvarSessao();
		
		if (!listaSociosSessao.isEmpty()){
			
			for (SocioCadastrado socioCadastrado : listaSociosSessao){
				
				if (socioCadastrado.getPessoa().getCpf().equals(pessoa.getCpf()) && 
						socioCadastrado.getReferencia() != null && 
						!socioCadastrado.getReferencia().equals(ref.intValue())){
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Sócio com o CPF " + Util.adicionarMascaraCPF(pessoa.getCpf()) + " já adicionado.");
				}
			}
		}
		
		if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()){
			msgsValidacao.add("Nome é obrigatório.");
		}
		
		if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
			msgsValidacao.add("E-mail é obrigatório.");
		}
		
		if (pessoa.getCpf() == null || pessoa.getCpf().trim().isEmpty()){
			
			msgsValidacao.add("CPF é obrigatório.");
			
		} else {
			
			try {
				
				cpfValidator.assertValid(pessoa.getCpf());
			
			} catch(InvalidStateException e) {
				
				//Tenta validar CPF não formatado.
				cpfValidator = new CPFValidator(false);

				try {
					
					cpfValidator.assertValid(pessoa.getCpf());
				
				} catch(InvalidStateException ie) {
					
					msgsValidacao.add("CPF inválido."); 
					
				}
			}
			
		}
		
		if (pessoa.getRg() == null || pessoa.getRg().trim().isEmpty()){
			msgsValidacao.add("R.G. é obrigatório.");
		} else if (pessoa.getRg().length() >  PessoaUtil.RG_QUANTIDADE_DIGITOS) {
			
			msgsValidacao.add("Quantidade de caracteres do campo [RG] excede o maximo de " + PessoaUtil.RG_QUANTIDADE_DIGITOS + " dígitos");
			
		}
		
		if(pessoa.getOrgaoEmissor() == null || pessoa.getOrgaoEmissor().isEmpty()) {
			msgsValidacao.add("[Orgão Emissor] do RG é obrigatório.");
		}
		
		if(pessoa.getUfOrgaoEmissor() == null || pessoa.getUfOrgaoEmissor().isEmpty()) {
			msgsValidacao.add("[UF do Orgão Emissor] do RG é obrigatório.");
		}

		
		if (pessoa.getDataNascimento() == null){
			msgsValidacao.add("Data Nascimento é obrigatório.");
		}
		
		if (pessoa.getEstadoCivil() == null){
			msgsValidacao.add("Estado Civil é obrigatório.");
		}
		
		if (pessoa.getSexo() == null){
			msgsValidacao.add("Sexo é obrigatório.");
		}
		
		if (validator.hasErrors()) {
			
			for (Message message : validator.getErrors()) {

				if (message.getCategory().equals("dataNascimento")){
					msgsValidacao.add("Data de nascimento do sócio inválida.");
				}
				
			}
		}
		
		//dados do conjuge
		if (pessoa.getConjuge() != null) {
			this.validarConjugeDoSocio(pessoa, cpfValidator, msgsValidacao);
		}

		Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
		
		if (idFiador != null){
			
			String cpf = pessoa.getCpf();
			
			if (cpf != null){
				cpf = cpf.replace(".", "").replace("-", "").trim();
			}
			
			if (cpf != null){
				
				PessoaFisica pessoaFisica = this.fiadorService.buscarSocioFiadorPorCPF(idFiador, cpf);
				
				if (pessoaFisica != null) {
					
					for (int index = 0 ; index < listaSociosSessao.size() ; index++){
						
						if (listaSociosSessao.get(index).getPessoa().getId() != null &&
								listaSociosSessao.get(index).getPessoa().getId().equals(pessoaFisica.getId())){
							
							listaSociosSessao.get(index).setPessoa(pessoaFisica);
							break;
						}
					}
					
					this.httpSession.setAttribute(LISTA_SOCIOS_SALVAR_SESSAO, listaSociosSessao);
				}
			}
		}
			
		if (!msgsValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgsValidacao));
		}
	}
	
	
	@Post
	public void pesquisarSociosFiador(String sortname, String sortorder){
		
		List<SocioCadastrado> listaSocioExibir = this.obterListaSociosExibirSessao();
		
		if (listaSocioExibir!=null && listaSocioExibir.size()<=0){
		
			Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
			
			if (idFiador != null){
			
				List<PessoaFisica> listaSocios = this.pessoaService.obterSociosPorFiador(idFiador, null, null);
				
				for (PessoaFisica p : listaSocios){
					
					SocioCadastrado socioCadastrado = new SocioCadastrado();
					
					socioCadastrado.setPessoa(p);
					
					socioCadastrado.setReferencia(p.getId().intValue());
					
					listaSocioExibir.add(socioCadastrado);
				}
			}
		}	
		
		if (sortname != null) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
			
			PaginacaoUtil.ordenarEmMemoria(listaSocioExibir, ordenacao, sortname);
		}
		
		this.httpSession.setAttribute(LISTA_SOCIOS_EXIBIR_SESSAO, listaSocioExibir);
		
		this.result.use(Results.json()).from(this.getTableModelListaSocios(listaSocioExibir), "result").recursive().serialize();
	}
	
	
	
	@Post
	public void adicionarSocio(PessoaFisica pessoa, Integer referencia){
		
		this.validarDadosEntrada(pessoa, referencia == null ? null : referencia.longValue());
		
		List<SocioCadastrado> listaSociosSalvar = this.obterListaSociosSalvarSessao();
		
		List<SocioCadastrado> listaSociosExibir = this.obterListaSociosExibirSessao();
		
		if (pessoa.isSocioPrincipal()){
			
			for(SocioCadastrado item : listaSociosExibir){

				item.getPessoa().setSocioPrincipal(false);
			}
			
            for(SocioCadastrado item : listaSociosSalvar){
					
				item.getPessoa().setSocioPrincipal(false);
			}
		}
			
		if (referencia == null){
			
			SocioCadastrado novoSocioCadastrado = new SocioCadastrado();
			
			pessoa.setId(referencia == null ? null : referencia.longValue());
			
			novoSocioCadastrado.setPessoa(pessoa);
			
			novoSocioCadastrado.setReferencia(referencia == null ? (int) new Date().getTime() : referencia);

			listaSociosSalvar.add(novoSocioCadastrado);
			
			listaSociosExibir.add(novoSocioCadastrado);
		}
		else{
			
			//Procura na lista salvar, se nao achar procura na lista de exibição alterar a pessoa e o staus da lista
			
			SocioCadastrado socioCadastrado = obterSocio(listaSociosSalvar, referencia);
			
			if(socioCadastrado!= null){
				socioCadastrado.setPessoa(pessoa);
			}
			else{
				
				socioCadastrado = obterSocio(listaSociosExibir, referencia);
				
				listaSociosSalvar.add(socioCadastrado);
			}
			
			for(SocioCadastrado item : listaSociosExibir){
				if(item.getReferencia().equals(referencia)){
					item.setPessoa(pessoa);
				}
			}
		}
		
		this.httpSession.setAttribute(LISTA_SOCIOS_SALVAR_SESSAO,listaSociosSalvar);
		
		this.pesquisarSociosFiador(null, null);
	}
	
	private SocioCadastrado obterSocio(List<SocioCadastrado> listaSocios, Integer referencia){
		
		if(listaSocios == null ){
			return null;
		}
		
		for(SocioCadastrado item : listaSocios){
			if(item.getReferencia().equals(referencia)){
				return item;
			}
		}
		
		return null;
	} 
	
	@Post
	public void removerSocio(Integer referencia){
		
		List<SocioCadastrado> sociosExibir = this.obterListaSociosExibirSessao();
		
		SocioCadastrado socioCadastradoRemover = null;
		
		for (int index = 0 ; index < sociosExibir.size() ; index++){
			
			if (sociosExibir.get(index).getReferencia().equals(referencia)){
				
				socioCadastradoRemover = sociosExibir.get(index);
				
				sociosExibir.remove(socioCadastradoRemover);
				
				break;
			}
		}
		
		this.httpSession.setAttribute(LISTA_SOCIOS_EXIBIR_SESSAO, sociosExibir);
		
		Set<Long> sociosRemover = this.obterListaSociosRemoverSessao();
		
		if (socioCadastradoRemover != null && socioCadastradoRemover.getPessoa() != null && 
				socioCadastradoRemover.getPessoa().getId() != null){
			
			sociosRemover.add(socioCadastradoRemover.getPessoa().getId());
		} else {
			
			sociosRemover.add(referencia.longValue());
		}
		
		this.httpSession.setAttribute(LISTA_SOCIOS_REMOVER_SESSAO, sociosRemover);
		
		this.result.use(Results.json()).from(this.getTableModelListaSocios(sociosExibir), "result").recursive().serialize();
	}
	
	
	
	@Post
	public void editarSocio(Integer referencia){
		
		PessoaFisica socio = null;
		
		for (SocioCadastrado socioCadastrado : this.obterListaSociosSalvarSessao()){
			
			if (socioCadastrado.getReferencia().equals(referencia)){
				
				socio = socioCadastrado.getPessoa();
				
				break;
			}
		}
		
		if (socio == null){
			
			socio = this.pessoaService.buscarPessoaFisicaPorId(referencia.longValue());
		}
		
		List<String> dados = null;
		
		if (socio != null){
			
			dados = new ArrayList<String>();
			
			dados.add(socio.getNome()!=null?socio.getNome():"");
			dados.add(socio.getEmail()!=null?socio.getEmail():"");
			dados.add(socio.getCpf()!=null?Util.adicionarMascaraCPF(socio.getCpf()):"");
			dados.add(socio.getRg()!=null?socio.getRg():"");
			dados.add(socio.getDataNascimento()!=null?DateUtil.formatarDataPTBR(socio.getDataNascimento()):"");
			dados.add(socio.getOrgaoEmissor()!=null?socio.getOrgaoEmissor():"");
			dados.add(socio.getUfOrgaoEmissor()!=null?socio.getUfOrgaoEmissor():"");
			dados.add(socio.getEstadoCivil()!=null?socio.getEstadoCivil().name():"");
			dados.add(socio.getSexo()!=null?socio.getSexo().name():"");
			dados.add(socio.getNacionalidade()!=null?socio.getNacionalidade():"");
			dados.add(socio.getNatural()!=null?socio.getNatural():"");
			
			PessoaFisica conjuge = socio.getConjuge();
			
			if (conjuge != null){
				
				dados.add(conjuge.getNome()!=null?conjuge.getNome():"");
				dados.add(conjuge.getEmail()!=null?conjuge.getEmail():"");
				dados.add(conjuge.getCpf()!=null?Util.adicionarMascaraCPF(conjuge.getCpf()):"");
				dados.add(conjuge.getRg()!=null?conjuge.getRg():"");
				dados.add(conjuge.getDataNascimento()!=null?DateUtil.formatarDataPTBR(conjuge.getDataNascimento()):"");
				dados.add(conjuge.getOrgaoEmissor()!=null?conjuge.getOrgaoEmissor():"");
				dados.add(conjuge.getUfOrgaoEmissor()!=null?conjuge.getUfOrgaoEmissor():"");
				dados.add(conjuge.getSexo()!=null?conjuge.getSexo().name():"");
				dados.add(conjuge.getNacionalidade()!=null?conjuge.getNacionalidade():"");
				dados.add(conjuge.getNatural()!=null?conjuge.getNatural():"");
			}
			
			dados.add(String.valueOf(socio.isSocioPrincipal()));
		}
		
		this.result.use(Results.json()).from(dados != null ? dados : "", "result").recursive().serialize();
	}
	
	private void validarConjugeDoSocio(PessoaFisica pessoa, CPFValidator cpfValidator, List<String> msgsValidacao) {
		
		if (validator.hasErrors()) {
			
			for (Message message : validator.getErrors()) {
				
				if (message.getCategory().equals("conjuge.dataNascimento")){
					msgsValidacao.add("Data de nascimento do conjuge do sócio inválida.");
				}
				
			}
		}
		
		if (pessoa.getCpf().equals(pessoa.getConjuge().getCpf())){
			msgsValidacao.add("Fiador e conjuge devem ser pessoas diferentes.");
		}
		
		pessoa = pessoa.getConjuge();
		
		if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()){
			msgsValidacao.add("Nome do conjuge é obrigatório.");
		}
		
		if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
			msgsValidacao.add("E-mail do conjuge é obrigatório.");
		}
		
		if (pessoa.getCpf() == null || pessoa.getCpf().trim().isEmpty()){
			msgsValidacao.add("CPF do conjuge é obrigatório.");
		} else {
			
			try {
				
				cpfValidator.assertValid(pessoa.getCpf());
			
			} catch(InvalidStateException e) {
				
				//Tenta validar CPF não formatado.
				cpfValidator = new CPFValidator(false);

				try {
					
					cpfValidator.assertValid(pessoa.getCpf());
				
				} catch(InvalidStateException ie) {
					
					msgsValidacao.add("CPF do conjuge inválido."); 
					
				}
			}
			
		}
		
		if (pessoa.getRg() == null || pessoa.getRg().trim().isEmpty()){
			msgsValidacao.add("R.G. do conjuge é obrigatório.");
		} else if (pessoa.getRg().length() >  PessoaUtil.RG_QUANTIDADE_DIGITOS) {
			
			msgsValidacao.add("Quantidade de caracteres do campo [RG] do conjuge excede o maximo de " + PessoaUtil.RG_QUANTIDADE_DIGITOS + " dígitos");
			
		}
		
		if(pessoa.getOrgaoEmissor() == null || pessoa.getOrgaoEmissor().isEmpty()) {
			msgsValidacao.add("[Orgão Emissor] do RG do conjuge é obrigatório.");
		}
		
		if(pessoa.getUfOrgaoEmissor() == null || pessoa.getUfOrgaoEmissor().isEmpty()) {
			msgsValidacao.add("[UF do Orgão Emissor] do RG do conjuge é obrigatório.");
		}
		
		if (pessoa.getDataNascimento() == null){
			msgsValidacao.add("Data Nascimento do conjuge é obrigatório.");
		}
		
		if (pessoa.getSexo() == null){
			msgsValidacao.add("Sexo do conjuge é obrigatório.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private List<SocioCadastrado> obterListaSociosExibirSessao(){
		List<SocioCadastrado> lista = 
				(List<SocioCadastrado>) this.httpSession.getAttribute(LISTA_SOCIOS_EXIBIR_SESSAO);
		
		if (lista == null){
			
			lista = new ArrayList<SociosController.SocioCadastrado>();
		}
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<SocioCadastrado> obterListaSociosSalvarSessao(){
		List<SocioCadastrado> lista = 
				(List<SocioCadastrado>) this.httpSession.getAttribute(LISTA_SOCIOS_SALVAR_SESSAO);
		
		if (lista == null){
			
			lista = new ArrayList<SociosController.SocioCadastrado>();
		}
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterListaSociosRemoverSessao(){
		
		Set<Long> lista = (Set<Long>) this.httpSession.getAttribute(LISTA_SOCIOS_REMOVER_SESSAO);
		
		if(lista==null){
			
			lista = new HashSet<Long>();
		}	
		
		return lista;
	}
	
	private TableModel<CellModel> getTableModelListaSocios(List<SocioCadastrado> listaSocios) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (SocioCadastrado socioCadastrado : listaSocios) {
			
			CellModel cellModel = new CellModel(
				socioCadastrado.getReferencia(), 
				socioCadastrado.getPessoa().getNome(),
				socioCadastrado.getPessoa().isSocioPrincipal()
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size()); 

		return tableModel;
	}
}
