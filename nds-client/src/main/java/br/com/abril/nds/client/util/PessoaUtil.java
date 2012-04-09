package br.com.abril.nds.client.util;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

/**
 * Classe utilitária para complexidades envolvendo a entidade Pessoa.
 * 
 * @author Discover Technology
 *
 */
public class PessoaUtil {
	
	public static final String SUFIXO_PESSOA_FISICA = " (PF)";
	
	public static final String SUFIXO_PESSOA_JURIDICA = " (PJ)";
	
	/**
	 * Obtém o nome de exibição de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * 
	 * @param pessoa - pessoa
	 * 
	 * @return Nome para exibição
	 */
	public static String obterNomeExibicaoPeloTipo(Pessoa pessoa) {

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
	
	/**
	 * Remove o sufixo do tipo de pessoa.
	 * 
	 * @param nomeExibicao - nome de exibicao
	 * 
	 * @return Nome de exibição sem o sufixo de tipo de pessoa
	 */
	public static String removerSufixoDeTipo(String nomeExibicao) {
		
		if (nomeExibicao != null && !nomeExibicao.trim().isEmpty()) {
			
			if (nomeExibicao.contains(SUFIXO_PESSOA_FISICA)) {
				
				return nomeExibicao.replace(SUFIXO_PESSOA_FISICA, "");
				
			} else if (nomeExibicao.contains(SUFIXO_PESSOA_JURIDICA)) {
				
				return nomeExibicao.replace(SUFIXO_PESSOA_JURIDICA, "");
			}
		}
		
		return nomeExibicao;
	}

}
