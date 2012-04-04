package br.com.abril.nds.controllers.cadastro;

import java.util.Date;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO.OrdenacaoColunaFiador;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/fiador")
public class FiadorController {

	public static final String ID_FIADOR_EDICAO = "idFiadorEdicaoSessao";
	
	private Result result;
	
	public FiadorController(Result result){
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		result.include("dataAtual", DateUtil.formatarDataPTBR(new Date()));
	}
	
	@Post
	public void pesquisarFiador(String nome, String cpfCnpj, String sortorder, String sortname, int page, int rp){
		
		FiltroConsultaFiadorDTO filtro = new FiltroConsultaFiadorDTO();
		filtro.setCpfCnpj(cpfCnpj);
		filtro.setNome(nome);
		filtro.setOrdenacaoColunaFiador(Util.getEnumByStringValue(OrdenacaoColunaFiador.values(), sortname));
		filtro.setPaginacaoVO(new PaginacaoVO(page, rp, sortorder));
		
		if ((nome == null || nome.trim().isEmpty()) && 
				(cpfCnpj == null || cpfCnpj.trim().isEmpty())){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nome ou CPF/CNPJ são obrigatórios.");
		}
		
		if (cpfCnpj != null){
			cpfCnpj = cpfCnpj.replace(".", "").replace("-", "");
		}
	}
	
	//public void cadastrarFiadorCpf(String nome, String email, String cpf, String rg, String dataNasc, String orgaoEmissor,
	//		String uf, String estadoCivil, String sexo, String nacionalidade, String natural){
		
	//}
	
	public void cadastrarFiadorCpf(PessoaFisica fiador, PessoaFisica conjuge){
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	public void cadastrarFiadorCnpj(PessoaJuridica fiador){
		
		result.use(Results.json()).from("", "result").serialize();
	}
}