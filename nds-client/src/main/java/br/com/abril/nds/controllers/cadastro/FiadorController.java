package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.cadastro.CotasAssociadasController.AssociacaoCota;
import br.com.abril.nds.controllers.cadastro.GarantiasController.GarantiaCadastrada;
import br.com.abril.nds.controllers.cadastro.SociosController.SocioCadastrado;
import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO.OrdenacaoColunaFiador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.stella.validation.CNPJValidator;
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
public class FiadorController {
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoFiador";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoFiador";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoFiador";
	
	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessaoFiador";
	
	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessaoFiador";
	
	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosExibicaoFiador";

	public static final String ID_FIADOR_EDICAO = "idFiadorEdicaoSessao";
	
	public static final String FILTRO_ULTIMA_PESQUISA_FIADOR = "filtroUltimaPesquisaFiador";
	
	private Result result;
	
	private HttpSession httpSession;
	
	private Validator validator;
	
	@Autowired
	private FiadorService fiadorService;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	public FiadorController(Result result, HttpSession httpSession, Validator validator){
		this.result = result;
		this.httpSession = httpSession;
		this.validator = validator;
	}
	
	@Path("/")
	public void index(){
		
		result.include("dataAtual", DateUtil.formatarDataPTBR(new Date()));
		
		this.limparDadosSessao();
	}
	
	
	@Post
	public void pesquisarFiador(FiltroConsultaFiadorDTO filtro, String sortorder, 
			String sortname, Integer page, Integer rp, ValidacaoVO validacaoVO){
		
		if (filtro == null){
			
			filtro = (FiltroConsultaFiadorDTO) this.httpSession.getAttribute(FILTRO_ULTIMA_PESQUISA_FIADOR);
		}
		
		if (filtro == null){
			
			filtro = new FiltroConsultaFiadorDTO();
		}
		
		OrdenacaoColunaFiador orderName = Util.getEnumByStringValue(OrdenacaoColunaFiador.values(), sortname);
		filtro.setOrdenacaoColunaFiador(orderName);
		
		if (filtro.getPaginacaoVO() == null){
			
			filtro.setPaginacaoVO(new PaginacaoVO(page, rp, sortorder));
		}
			
		if (page != null){
			
			filtro.getPaginacaoVO().setPaginaAtual(page);
		}
		
		if (rp != null){
			
			filtro.getPaginacaoVO().setQtdResultadosPorPagina(rp);
		}
		
		if (sortorder != null) {
			
			filtro.getPaginacaoVO().setOrdenacao(Util.getEnumByStringValue(Ordenacao.values(), sortorder));
		}
		
		if (filtro.getCpfCnpj() != null){
			filtro.setCpfCnpj(filtro.getCpfCnpj().replace(".", "").replace("-", "").replace("/", ""));
		}
		
		ConsultaFiadorDTO consultaFiadorDTO = this.fiadorService.obterFiadores(filtro);
		
		this.httpSession.setAttribute(FILTRO_ULTIMA_PESQUISA_FIADOR, filtro);
		
		Object[] result = new Object[2];
		result[1] = "";
		
		if (validacaoVO != null){
			
			result[0] = validacaoVO;
		}
		
		if (consultaFiadorDTO.getListaFiadores().isEmpty()){
			if (result[0] == null){
				result[0] = new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			} else {
				ValidacaoVO vo = (ValidacaoVO) result[0];
				vo.getListaMensagens().add("Nenhum registro encontrado.");
			}
			
			this.result.use(Results.json()).from(result, "result").recursive().serialize();
		} else {
			if (result[0] == null){
				result[0] = "";
			}
			
			result[1] = this.getTableModelFiadores(consultaFiadorDTO, page);
			this.result.use(Results.json()).from(result, "result").recursive().serialize();
		}
	}
	
	@Post
	public void cadastrarFiadorCpf(PessoaFisica pessoa){
		
		this.validarDadosEntradaPessoaFisica(pessoa);
		
		this.preencherDadosFiador(pessoa);
		
		ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso.");
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = (FiltroConsultaFiadorDTO) 
				this.httpSession.getAttribute(FILTRO_ULTIMA_PESQUISA_FIADOR);
		
		if (filtroConsultaFiadorDTO == null){
			filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
			filtroConsultaFiadorDTO.setCpfCnpj(pessoa.getCpf());
		}
		
		this.pesquisarFiador(filtroConsultaFiadorDTO, null, null, null, null, validacaoVO);
	}
	
	@Post
	public void buscarPessoaCPF(String cpf, boolean isFiador, String cpfConjuge, boolean socio){
		
		List<String> dados = null;
		
		if (cpf != null){
			
			cpf = cpf.replace("-", "").replace(".", "");
			
			if (cpfConjuge != null){
				cpfConjuge = cpfConjuge.replace("-", "").replace(".", "");
			}
			
			PessoaFisica fisica = null;
			
			if (!socio){
				Fiador fiador = this.fiadorService.obterFiadorPorCPF(cpf);
				
				if (fiador != null){
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Já existe um fiador cadastrado com esse CPF.");
				}
				
			}
			
			fisica = this.pessoaService.buscarPessoaPorCPF(cpf, isFiador, cpfConjuge);
			
			if (fisica != null){
				
				if (isFiador && !socio){
					this.carregarTelefonesEnderecosPessoa(fisica.getId());
				}
				
				dados = new ArrayList<String>();
				
				dados.add(fisica.getNome());
				dados.add(fisica.getEmail());
				dados.add(Util.adicionarMascaraCPF(fisica.getCpf()));
				dados.add(fisica.getRg());
				dados.add(DateUtil.formatarDataPTBR(fisica.getDataNascimento()));
				dados.add(fisica.getOrgaoEmissor());
				dados.add(fisica.getUfOrgaoEmissor());
				dados.add(fisica.getEstadoCivil().name());
				dados.add(fisica.getSexo().name());
				dados.add(fisica.getNacionalidade());
				dados.add(fisica.getNatural());
				
				PessoaFisica conjuge = fisica.getConjuge();
				
				if (conjuge != null){
					dados.add(conjuge.getNome());
					dados.add(conjuge.getEmail());
					dados.add(Util.adicionarMascaraCPF(conjuge.getCpf()));
					dados.add(conjuge.getRg());
					dados.add(DateUtil.formatarDataPTBR(conjuge.getDataNascimento()));
					dados.add(conjuge.getOrgaoEmissor());
					dados.add(conjuge.getUfOrgaoEmissor());
					dados.add(conjuge.getSexo().name());
					dados.add(conjuge.getNacionalidade());
					dados.add(conjuge.getNatural());
				}
			}
		}
		
		this.result.use(Results.json()).from(dados == null ? "" : dados, "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private void carregarTelefonesEnderecosPessoa(Long id) {
		
		Set<Long> telRemover = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		Map<Integer, TelefoneAssociacaoDTO> telSalvar = (Map<Integer, TelefoneAssociacaoDTO>)
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telSalvar != null){
			
			for (TelefoneAssociacaoDTO dto : telSalvar.values()){
				
				telRemover.add(dto.getTelefone().getId());
			}
		}
		
		List<TelefoneAssociacaoDTO> lista = this.telefoneService.buscarTelefonesPorIdPessoa(id, telRemover);
		
		this.httpSession.setAttribute(LISTA_TELEFONES_EXIBICAO, lista);
		
		List<EnderecoAssociacaoDTO> endRemover = (List<EnderecoAssociacaoDTO>)
				this.httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		List<EnderecoAssociacaoDTO> endSalvar = (List<EnderecoAssociacaoDTO>)
				this.httpSession.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		Set<Long> idsIgnorar = new HashSet<Long>();
		
		if (endRemover != null){
			for (EnderecoAssociacaoDTO dto : endRemover){
				
				if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
					
					idsIgnorar.add(dto.getEndereco().getId());
				}
			}
		}
		
		if (endSalvar != null){
			for (EnderecoAssociacaoDTO dto : endSalvar){
				
				if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
					
					idsIgnorar.add(dto.getEndereco().getId());
				}
			}
		}
		
		List<EnderecoAssociacaoDTO> listaExibir = this.enderecoService.buscarEnderecosPorIdPessoa(id, idsIgnorar);
		
		this.httpSession.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaExibir);
	}

	@Post
	public void buscarPessoaCNPJ(String cnpj){
		
		List<String> dados = null;
		
		if (cnpj != null){
			
			cnpj = cnpj.replace("-", "").replace(".", "").replace("/", "");
			
			Fiador fiador = this.fiadorService.obterFiadorPorCNPJ(cnpj);
			
			if (fiador != null){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Já existe um fiador cadastrado com esse CNPJ.");
			}
			
			PessoaJuridica juridica = this.pessoaService.buscarPessoaPorCNPJ(cnpj);
			
			if (juridica != null){
				dados = new ArrayList<String>();
				
				dados.add(juridica.getRazaoSocial());
				dados.add(juridica.getNomeFantasia());
				dados.add(juridica.getInscricaoEstadual());
				dados.add(Util.adicionarMascaraCNPJ(juridica.getCnpj()));
				dados.add(juridica.getEmail());
				
				this.carregarTelefonesEnderecosPessoa(juridica.getId());
			}
		}
		
		this.result.use(Results.json()).from(dados == null ? "" : dados, "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private void preencherDadosFiador(Pessoa pessoa) {
		
		List<Pessoa> sociosAdicionar = null;
		if (pessoa instanceof PessoaFisica){
			
			PessoaFisica p = (PessoaFisica) pessoa;
			if (p.getConjuge() != null){
				
				p.getConjuge().setEstadoCivil(EstadoCivil.CASADO);
				p.getConjuge().setCpf(p.getConjuge().getCpf().replace(".", "").replace("-", ""));
				p.getConjuge().setRg(p.getConjuge().getRg().replace("-", "").replace(".", ""));
			}
			
			p.setCpf(p.getCpf().replace(".", "").replace("-", ""));
			p.setRg(p.getRg().replace("-", "").replace(".", ""));
		} else {
			
			((PessoaJuridica) pessoa).setCnpj(((PessoaJuridica) pessoa).getCnpj().replace("-", "").replace(".", "").replace("/", ""));
			
			List<SocioCadastrado> sociosCadastrados = (List<SocioCadastrado>)
					this.httpSession.getAttribute(SociosController.LISTA_SOCIOS_SALVAR_SESSAO);
			
			if (sociosCadastrados != null && !sociosCadastrados.isEmpty()){
				
				sociosAdicionar = new ArrayList<Pessoa>();
				
				for (SocioCadastrado cadastrado : sociosCadastrados){
					sociosAdicionar.add(cadastrado.getPessoa());
					
					PessoaFisica p = (PessoaFisica) cadastrado.getPessoa();
					if (p.getConjuge() != null){
						
						p.getConjuge().setEstadoCivil(EstadoCivil.CASADO);
						p.getConjuge().setCpf(p.getConjuge().getCpf().replace(".", "").replace("-", ""));
						p.getConjuge().setRg(p.getConjuge().getRg().replace("-", "").replace(".", ""));
					}
					
					p.setCpf(p.getCpf().replace(".", "").replace("-", ""));
					p.setRg(p.getRg().replace("-", "").replace(".", ""));
				}
			}
		}
		
		Set<Long> sociosRemover = (Set<Long>) 
				this.httpSession.getAttribute(SociosController.LISTA_SOCIOS_REMOVER_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecosAdicionar = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		if (listaEnderecosAdicionar == null || listaEnderecosAdicionar.isEmpty()){
			
			List<EnderecoAssociacaoDTO> listaEnderecosExibir = (List<EnderecoAssociacaoDTO>) 
					this.httpSession.getAttribute(LISTA_ENDERECOS_EXIBICAO);
			
			if (listaEnderecosExibir == null || listaEnderecosExibir.isEmpty()){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre ao menos 1 endereço.");
			}
			
			boolean valido = false;
			
			for (EnderecoAssociacaoDTO dto : listaEnderecosExibir){
			
				if (dto.getTipoEndereco() != null){
					
					valido = true;
					break;
				}
			}
			
			
			if (!valido){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre ao menos 1 endereço.");
			}
		}
		
		List<EnderecoAssociacaoDTO> listaEnderecosRemover = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		Map<Integer, TelefoneAssociacaoDTO> listaTelefone = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (listaTelefone == null || listaTelefone.keySet().isEmpty()){
			
			List<TelefoneAssociacaoDTO> list = (List<TelefoneAssociacaoDTO>) 
					this.httpSession.getAttribute(LISTA_TELEFONES_EXIBICAO);
			
			if (list == null || list.isEmpty()){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre ao menos 1 telefone.");
			}
			
			boolean valido = false;
			
			for (TelefoneAssociacaoDTO dto : list){
			
				if (dto.getTipoTelefone() != null){
					
					valido = true;
					break;
				}
			}
			
			
			if (!valido){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre ao menos 1 telefone.");
			}
		}
		
		List<TelefoneAssociacaoDTO> listaTelefoneAdicionar = new ArrayList<TelefoneAssociacaoDTO>();
		if (listaTelefone != null){
			for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefone.values()){
				listaTelefoneAdicionar.add(telefoneAssociacaoDTO);
			}
		}
		
		Set<Long> listaTelefoneRemover = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		List<GarantiaCadastrada> listaGarantiaSessao = (List<GarantiaCadastrada>) 
				this.httpSession.getAttribute(GarantiasController.LISTA_GARANTIAS_SALVAR_SESSAO);
		
		List<Garantia> listaGarantiaAdicionar = new ArrayList<Garantia>();
		if (listaGarantiaSessao != null){
			for (GarantiaCadastrada garantiaCadastrada : listaGarantiaSessao){
				listaGarantiaAdicionar.add(garantiaCadastrada.getGarantia());
			}
		}
		
		Set<Long> listaGarantiaRemover = (Set<Long>) 
				this.httpSession.getAttribute(GarantiasController.LISTA_GARANTIAS_REMOVER_SESSAO);
		
		List<AssociacaoCota> listaCotasSessao = (List<AssociacaoCota>) 
				this.httpSession.getAttribute(CotasAssociadasController.LISTA_COTAS_ASSOCIADAS_SALVAR_SESSAO);
		
		List<Integer> listaCotasAssociar = new ArrayList<Integer>();
		if (listaCotasSessao != null){
			for (AssociacaoCota associacaoCota : listaCotasSessao){
				listaCotasAssociar.add(associacaoCota.getNumeroCota());
			}
		}
		
		Set<Long> listaCotasDesassociar = (Set<Long>) 
				this.httpSession.getAttribute(CotasAssociadasController.LISTA_COTAS_ASSOCIADAS_REMOVER_SESSAO);
		
		Fiador fiador = new Fiador();
		fiador.setId((Long) this.httpSession.getAttribute(ID_FIADOR_EDICAO));
		fiador.setPessoa(pessoa);
		
		this.fiadorService.cadastrarFiador(fiador, sociosAdicionar, sociosRemover, listaEnderecosAdicionar, 
				listaEnderecosRemover, listaTelefoneAdicionar, listaTelefoneRemover, 
				listaGarantiaAdicionar, listaGarantiaRemover, listaCotasAssociar, listaCotasDesassociar);
		
		this.limparDadosSessao();
	}

	@Post
	public void cadastrarFiadorCnpj(PessoaJuridica fiador){
		
		this.validarDadosEntradaPessoJuridica(fiador);
		
		this.preencherDadosFiador(fiador);
		
		ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso.");
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = (FiltroConsultaFiadorDTO) 
				this.httpSession.getAttribute(FILTRO_ULTIMA_PESQUISA_FIADOR);
		
		if (filtroConsultaFiadorDTO == null){
			filtroConsultaFiadorDTO = new FiltroConsultaFiadorDTO();
			filtroConsultaFiadorDTO.setCpfCnpj(fiador.getCnpj());
		}
		
		this.pesquisarFiador(filtroConsultaFiadorDTO, null, null, null, null, validacaoVO);
	}
	
	@Post
	public void cancelarCadastro(){
		
		this.limparDadosSessao();
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	public void excluirFiador(Long idFiador){
		
		this.fiadorService.excluirFiador(idFiador);
		
		ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso.");
		
		FiltroConsultaFiadorDTO filtroConsultaFiadorDTO = (FiltroConsultaFiadorDTO) 
				this.httpSession.getAttribute(FILTRO_ULTIMA_PESQUISA_FIADOR);
		
		this.pesquisarFiador(filtroConsultaFiadorDTO, null, null, null, null, validacaoVO);
	}
	
	@Post
	public void editarFiador(Long idFiador){
		
		limparDadosSessao();
		
		Fiador fiador = this.fiadorService.obterFiadorPorId(idFiador);
		
		List<String> dados = new ArrayList<String>();
		
		if (fiador.getPessoa() != null){
			
			this.httpSession.setAttribute(ID_FIADOR_EDICAO, idFiador);
			
			if (fiador.getPessoa() instanceof PessoaFisica){
				
				PessoaFisica fisica =  (PessoaFisica) fiador.getPessoa();
				
				dados.add("CPF");
				dados.add(fisica.getNome());
				dados.add(fisica.getEmail());
				dados.add(Util.adicionarMascaraCPF(fisica.getCpf()));
				dados.add(fisica.getRg());
				dados.add(DateUtil.formatarDataPTBR(fisica.getDataNascimento()));
				dados.add(fisica.getOrgaoEmissor());
				dados.add(fisica.getUfOrgaoEmissor());
				dados.add(fisica.getEstadoCivil().name());
				dados.add(fisica.getSexo().name());
				dados.add(fisica.getNacionalidade());
				dados.add(fisica.getNatural());
				
				PessoaFisica conjuge = fisica.getConjuge();
				
				if (conjuge != null){
					dados.add(conjuge.getNome());
					dados.add(conjuge.getEmail());
					dados.add(Util.adicionarMascaraCPF(conjuge.getCpf()));
					dados.add(conjuge.getRg());
					dados.add(DateUtil.formatarDataPTBR(conjuge.getDataNascimento()));
					dados.add(conjuge.getOrgaoEmissor());
					dados.add(conjuge.getUfOrgaoEmissor());
					dados.add(conjuge.getSexo().name());
					dados.add(conjuge.getNacionalidade());
					dados.add(conjuge.getNatural());
				}
			} else {
				
				PessoaJuridica pessoaJuridica = (PessoaJuridica) fiador.getPessoa();
				
				dados.add("CNPJ");
				dados.add(pessoaJuridica.getRazaoSocial());
				dados.add(pessoaJuridica.getNomeFantasia());
				dados.add(pessoaJuridica.getInscricaoEstadual());
				dados.add(Util.adicionarMascaraCNPJ(pessoaJuridica.getCnpj()));
				dados.add(pessoaJuridica.getEmail());
			}
			
			dados.add(DateUtil.formatarDataPTBR(fiador.getInicioAtividade()));
			
			this.carregarTelefonesEnderecosFiador(idFiador);
		}
		
		result.use(Results.json()).from(dados, "result").serialize();
	}
	
	private void carregarTelefonesEnderecosFiador(Long idFiador) {
		
		List<TelefoneAssociacaoDTO> lista = this.fiadorService.buscarTelefonesFiador(idFiador, null);
		
		this.httpSession.setAttribute(LISTA_TELEFONES_EXIBICAO, lista);
		
		List<EnderecoAssociacaoDTO> listaEnderecos = this.fiadorService.buscarEnderecosFiador(idFiador, null);
		
		this.httpSession.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecos);
	}

	private void limparDadosSessao(){
		this.httpSession.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
		this.httpSession.removeAttribute(SociosController.LISTA_SOCIOS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(SociosController.LISTA_SOCIOS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_EXIBICAO);
		this.httpSession.removeAttribute(GarantiasController.LISTA_GARANTIAS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(GarantiasController.LISTA_GARANTIAS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(CotasAssociadasController.LISTA_COTAS_ASSOCIADAS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(CotasAssociadasController.LISTA_COTAS_ASSOCIADAS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(ID_FIADOR_EDICAO);
	}
	
	private TableModel<CellModel> getTableModelFiadores(ConsultaFiadorDTO consulta, Integer page) {
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (Fiador fiador : consulta.getListaFiadores()) {
			
			String nomeFiador = null;
			String cpfCnpjFiador = null;
			String rgInscricaoEstadualFiador = null;
			
			if (fiador.getPessoa() instanceof PessoaFisica){
				
				PessoaFisica pessoaFisica = (PessoaFisica)fiador.getPessoa();
				
				nomeFiador = pessoaFisica.getNome();
				cpfCnpjFiador = Util.adicionarMascaraCPF(pessoaFisica.getCpf());
				rgInscricaoEstadualFiador = pessoaFisica.getRg();
			} else {
				
				PessoaJuridica pessoaJuridica = (PessoaJuridica)fiador.getPessoa();
				
				nomeFiador = pessoaJuridica.getRazaoSocial();
				cpfCnpjFiador = Util.adicionarMascaraCNPJ(pessoaJuridica.getCnpj());
				rgInscricaoEstadualFiador = pessoaJuridica.getInscricaoEstadual();
			}
			
			Telefone telefone = fiador.getPessoa().getTelefones().isEmpty() ? null : fiador.getPessoa().getTelefones().get(0);
			
			CellModel cellModel = new CellModel(
				fiador.getId().intValue(),
				fiador.getId(),
				nomeFiador,
				cpfCnpjFiador,
				rgInscricaoEstadualFiador,
				telefone,
				fiador.getPessoa().getEmail()
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(page == null ? 1 : page);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(consulta.getQuantidadeRegistros().intValue()); 
		
		return tableModel;
	}
	
	private void validarDadosEntradaPessoaFisica(PessoaFisica pessoa){
		
		List<String> msgsValidacao = new ArrayList<String>();
		
		CPFValidator cpfValidator = new CPFValidator(true);
		
		if (pessoa == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "CPF é obrigatório.");
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
			
			try{
				
				cpfValidator.assertValid(pessoa.getCpf());
			} catch(InvalidStateException e){
				
				msgsValidacao.add("CPF inválido.");
			}
		}
		
		if (pessoa.getRg() == null || pessoa.getRg().trim().isEmpty()){
			msgsValidacao.add("R.G. é obrigatório.");
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
		
		//dados do conjuge
		if (pessoa.getConjuge() != null){
			
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
				
				try{
					
					cpfValidator.assertValid(pessoa.getCpf());
				} catch(InvalidStateException e){
					
					msgsValidacao.add("CPF do conjuge inválido.");
				}
			}
			
			if (pessoa.getRg() == null || pessoa.getRg().trim().isEmpty()){
				msgsValidacao.add("R.G. do conjuge é obrigatório.");
			}
			
			if (pessoa.getDataNascimento() == null){
				msgsValidacao.add("Data Nascimento do conjuge é obrigatório.");
			}
			
			if (pessoa.getSexo() == null){
				msgsValidacao.add("Sexo do conjuge é obrigatório.");
			}
		}
		
		if (validator.hasErrors()) {
			
			for (Message message : validator.getErrors()) {
				if (message.getCategory().equals("dataNascimento")){
					msgsValidacao.add("Data de nascimento do sócio inválida.");
				}
				
				if (message.getCategory().equals("conjuge.dataNascimento")){
					msgsValidacao.add("Data de nascimento do conjuge do sócio inválida.");
				}
			}
		}
		
		if (!msgsValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgsValidacao));
		}
	}
	
	private void validarDadosEntradaPessoJuridica(PessoaJuridica pessoa){
		
		List<String> msgsValidacao = new ArrayList<String>();
		
		CNPJValidator cnpjValidator = new CNPJValidator(true);
		
		if (pessoa == null){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "CNPJ é obrigatório"));
		}
		
		if (pessoa.getRazaoSocial() == null || pessoa.getRazaoSocial().trim().isEmpty()){
			msgsValidacao.add("Razão social é obrigatório");
		}
		
		if (pessoa.getNomeFantasia() == null || pessoa.getNomeFantasia().trim().isEmpty()){
			msgsValidacao.add("Nome fantasia é obrigatório");
		}
		
		if (pessoa.getInscricaoEstadual() == null || pessoa.getInscricaoEstadual().trim().isEmpty()){
			msgsValidacao.add("Inscrição estadual é obrigatório");
		}
		
		if (pessoa.getCnpj() == null || pessoa.getCnpj().trim().isEmpty()){
			msgsValidacao.add("CNPJ é obrigatório");
		} else {
			
			try{
				
				cnpjValidator.assertValid(pessoa.getCnpj());
			} catch(InvalidStateException e){
				
				msgsValidacao.add("CNPJ inválido.");
			}
		}
		
		if (pessoa.getEmail() == null || pessoa.getEmail().trim().isEmpty()){
			msgsValidacao.add("E-mail é obrigatório");
		}
		
		if (!msgsValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgsValidacao));
		}
	}
}