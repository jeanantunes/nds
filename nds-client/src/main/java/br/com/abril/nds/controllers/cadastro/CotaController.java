package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cota")
public class CotaController {
	
	private Result result;
	
	private static final String SUFIXO_PESSOA_FISICA = " (PF)";
	
	private static final String SUFIXO_PESSOA_JURIDICA = " (PF)";
	
	@Autowired
	private CotaService cotaService;
	
	public CotaController(Result result) {
		
		this.result = result;
	}
	
	@Post
	public void pesquisarPorNumero(Integer numeroCota) {
		
		Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);

		if (cota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		} else {
			
			String nomeExibicao =
				this.obterNomeExibicaoCotaPeloTipoPessoa(cota.getPessoa());
			
			CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
			result.use(Results.json()).from(cotaVO, "result").recursive().serialize();
		}		
	}
	
	@Post
	public void autoCompletarPorNome(String nomeCota) {
		
		List<Cota> listaCotas = this.cotaService.obterCotasPorNomePessoa(nomeCota);
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaCotas != null && !listaCotas.isEmpty()) {
			
			for (Cota cota : listaCotas) {
				
				String nomeExibicao =
					this.obterNomeExibicaoCotaPeloTipoPessoa(cota.getPessoa());
					
				CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
	
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, cotaVO));
			}
		}
		
		result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void pesquisarPorNome(String nomeCota) {
		
		if (nomeCota != null && !nomeCota.trim().isEmpty()) {
			
			if (nomeCota.contains(SUFIXO_PESSOA_FISICA)) {
				
				nomeCota = nomeCota.replace(SUFIXO_PESSOA_FISICA, "");
				
			} else if (nomeCota.contains(SUFIXO_PESSOA_JURIDICA)) {
				
				nomeCota = nomeCota.replace(SUFIXO_PESSOA_JURIDICA, "");
			}
		}
		
		Cota cota = this.cotaService.obterPorNome(nomeCota);
		
		if (cota == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + nomeCota + "\" não encontrada!");
		}
		
		String nomeExibicao =
			this.obterNomeExibicaoCotaPeloTipoPessoa(cota.getPessoa());
				
		CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
		result.use(Results.json()).from(cotaVO, "result").serialize();
	}
	
	/*
	 * Obtém o nome de exibição da cota de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * 
	 * @param pessoa - pessoa
	 * 
	 * @return Nome para exibição
	 */
	private String obterNomeExibicaoCotaPeloTipoPessoa(Pessoa pessoa) {

		String nomeExibicao = "";
		
		if (pessoa != null) {
			
			if (pessoa instanceof PessoaJuridica) {
				
				String razaoSocial = ((PessoaJuridica) pessoa).getRazaoSocial();

				nomeExibicao = razaoSocial != null ? razaoSocial + SUFIXO_PESSOA_JURIDICA : razaoSocial;

				
			} else if (pessoa instanceof PessoaFisica) {
				
				String nome = ((PessoaFisica) pessoa).getNome();

				nomeExibicao = nome != null ? nome + SUFIXO_PESSOA_FISICA : nome;
			}
		}
		
		return nomeExibicao;
	}

}
