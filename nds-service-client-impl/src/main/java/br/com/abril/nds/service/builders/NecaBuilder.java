package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ItemImpressaoNfe;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.NotaEnvio;

public class NecaBuilder  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 777530822917552572L;

	/**
	 * Carrega os dados principais da DANFE
	 * 
	 * @param nfeImpressao
	 * @param nfe
	 * @param notaEnvio
	 */
	public static void carregarNEDadosPrincipais(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		//FIXME: Alterado o ordenador por motivos de performance
		List<ItemNotaEnvio> lista = new ArrayList<ItemNotaEnvio>(notaEnvio.getListaItemNotaEnvio());
		Collections.sort(lista, new Comparator<ItemNotaEnvio>() {

			public int compare(ItemNotaEnvio o1, ItemNotaEnvio o2) {
				if(o1 != null && o2 != null && o1.getEstudoCota() != null && o2.getEstudoCota() != null
						&& o1.getEstudoCota().getEstudo() != null && o2.getEstudoCota().getEstudo() != null) {
					if(o1.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime() < o2.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime()){
						return -1;
					}
					if(o1.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime() > o2.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime()){
						return 1;
					}
				}
				if(o1 != null && o2 != null && o1.getEstudoCota() != null) {
					return -1;
				}
				return 0;
			}
			
		});
		
		Date dataLancamento = null;
		
		if(lista.get(0) != null 
				&& lista.get(0).getEstudoCota() != null 
				&& lista.get(0).getEstudoCota().getEstudo() != null
				&& lista.get(0).getEstudoCota().getEstudo().getDataLancamento() != null) {
			dataLancamento = lista.get(0).getEstudoCota().getEstudo().getDataLancamento();
		} else {
			// dataLancamento = this.notaEnvioRepository.obterMenorDataLancamentoPorNotaEnvio(notaEnvio.getNumero());
		}
		
		Long numeroNF 	    		= notaEnvio.getNumero();
		String chave 				= notaEnvio.getChaveAcesso();
		Date dataEmissao 			= notaEnvio.getDataEmissao();

		BigDecimal valorLiquido  	= BigDecimal.ZERO;
		
		for(ItemNotaEnvio ine : notaEnvio.getListaItemNotaEnvio()) {
            valorLiquido = valorLiquido.add(ine.getPrecoCapa());
		}
		
		BigDecimal valorDesconto	= BigDecimal.ZERO;

		String ambiente 	= ""; //TODO obter campo
		String versao		= ""; //TODO obter campo

		nfeImpressao.setNumeroNF(numeroNF);
		nfeImpressao.setDataEmissao(dataEmissao);
		nfeImpressao.setAmbiente(ambiente);
		nfeImpressao.setChave(chave);
		nfeImpressao.setVersao(versao);
		nfeImpressao.setValorLiquido(valorLiquido);
		nfeImpressao.setValorDesconto(valorDesconto);
		nfeImpressao.setDataLancamento(dataLancamento);
	}
	
	/**
	 * Carrega os dados do emissor na DANFE
	 * 
	 * @param danfe
	 * @param notaEnvio
	 */
	public static void carregarNEDadosEmissor(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		Pessoa pessoaEmitente = notaEnvio.getEmitente().getPessoaEmitenteReferencia();

		boolean indPessoaJuridica = false;

		if(pessoaEmitente instanceof PessoaJuridica) {

			indPessoaJuridica = true;

		} 

		String documento 	= notaEnvio.getEmitente().getDocumento();
		Endereco endereco 	= notaEnvio.getEmitente().getEndereco();
		Telefone telefone 	= notaEnvio.getEmitente().getTelefone();

		String emissorNome 							 = notaEnvio.getEmitente().getNome();

		String emissorFantasia 						 = notaEnvio.getEmitente().getNome();
		String emissorInscricaoEstadual 			 = notaEnvio.getEmitente().getInscricaoEstadual();

		String emissorCNPJ 							 = "";

		if(indPessoaJuridica) {
			emissorCNPJ = documento;
		} 

		String emissorLogradouro 	=	"";
		String emissorNumero 		=   "";
		String emissorBairro 		=   "";
		String emissorMunicipio 	=   "";
		String emissorUF 			=   "";
		String emissorCEP 			=   "";

		if(endereco!=null) {

			emissorLogradouro 	= endereco.getTipoLogradouro() +" "+ endereco.getLogradouro();
			emissorNumero 		= endereco.getNumero().toString();
			emissorBairro 		= endereco.getBairro();
			emissorMunicipio 	= endereco.getCidade();
			emissorUF 			= endereco.getUf();
			emissorCEP 			= endereco.getCep();

		}

		String emissorTelefone 		= "";

		if(telefone != null) {
			String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			emissorTelefone = ddd + phone;	
		}


		emissorCEP = tratarCep(emissorCEP);
		emissorTelefone = tratarTelefone(emissorTelefone);

		nfeImpressao.setEmissorCNPJ(emissorCNPJ);
		nfeImpressao.setEmissorNome(emissorNome);
		nfeImpressao.setEmissorFantasia(emissorFantasia);
		nfeImpressao.setEmissorInscricaoEstadual(emissorInscricaoEstadual);
		nfeImpressao.setEmissorLogradouro(emissorLogradouro);
		nfeImpressao.setEmissorNumero(emissorNumero);
		nfeImpressao.setEmissorBairro(emissorBairro);
		nfeImpressao.setEmissorMunicipio(emissorMunicipio);
		nfeImpressao.setEmissorUF(emissorUF);
		nfeImpressao.setEmissorCEP(emissorCEP);
		nfeImpressao.setEmissorTelefone(emissorTelefone);

	}
	
	/**
	 * Carrega os dados de destinatario na DANFE.
	 * 
	 * @param nfeImpressao
	 * @param nfe
	 * @param notaEnvio
	 */
	public static void carregarNEDadosDestinatario(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		String documento 			= notaEnvio.getDestinatario().getDocumento();
		Integer codigoBox			= notaEnvio.getDestinatario().getCodigoBox();
		String nomeBox				= notaEnvio.getDestinatario().getNomeBox();
		String codigoRota			= notaEnvio.getDestinatario().getCodigoRota();
		String descricaoRota 		= notaEnvio.getDestinatario().getDescricaoRota();
		
		Endereco endereco = notaEnvio.getDestinatario().getEndereco();
		Telefone telefone = notaEnvio.getDestinatario().getTelefone();

		String destinatarioCNPJ = documento;
		String destinatarioNome 				= notaEnvio.getDestinatario().getNome();
		String destinatarioInscricaoEstadual 	= notaEnvio.getDestinatario().getInscricaoEstadual();

		String destinatarioLogradouro 			= "";
		String destinatarioNumero 				= "";
		String destinatarioComplemento 			= "";
		String destinatarioBairro 				= "";
		String destinatarioMunicipio 			= "";
		String destinatarioUF 					= "";
		String destinatarioCEP 					= "";
		String destinatarioTelefone 			= "";

		if(endereco != null) {

			destinatarioLogradouro 	= endereco.getTipoLogradouro() +" "+ endereco.getLogradouro();
			destinatarioNumero		= endereco.getNumero()!=null?endereco.getNumero().toString():"";
			destinatarioComplemento	= endereco.getComplemento();
			destinatarioBairro		= endereco.getBairro();
			destinatarioMunicipio	= endereco.getCidade();
			destinatarioUF			= endereco.getUf();
			destinatarioCEP			= endereco.getCep();

		}

		if(telefone != null) {

			String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			destinatarioTelefone = ddd + phone;

		}

		destinatarioCEP = tratarCep(destinatarioCEP);

		destinatarioTelefone = tratarTelefone(destinatarioTelefone);

		nfeImpressao.setDestinatarioCNPJ(destinatarioCNPJ);
		nfeImpressao.setDestinatarioNome(destinatarioNome);
		nfeImpressao.setDestinatarioInscricaoEstadual(destinatarioInscricaoEstadual);
		nfeImpressao.setDestinatarioLogradouro(destinatarioLogradouro);
		nfeImpressao.setDestinatarioNumero(destinatarioNumero);
		nfeImpressao.setDestinatarioComplemento(destinatarioComplemento);
		nfeImpressao.setDestinatarioBairro(destinatarioBairro);
		nfeImpressao.setDestinatarioMunicipio(destinatarioMunicipio);
		nfeImpressao.setDestinatarioUF(destinatarioUF);
		nfeImpressao.setDestinatarioCEP(destinatarioCEP);
		nfeImpressao.setDestinatarioTelefone(destinatarioTelefone);
		nfeImpressao.setDestinatarioCodigoBox(codigoBox);
		nfeImpressao.setDestinatarioNomeBox(nomeBox);
		nfeImpressao.setDestinatarioCodigoRota(codigoRota);
		nfeImpressao.setDestinatarioDescricaoRota(descricaoRota);
		nfeImpressao.setNumeroCota(notaEnvio.getDestinatario().getNumeroCota());
		
	}
	
	public static void carregarNEDadosItens(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		final List<ItemImpressaoNfe> listaItemImpressaoNfe = new ArrayList<ItemImpressaoNfe>();
        
        final List<ItemNotaEnvio> itensNotaEnvio =  notaEnvio.getListaItemNotaEnvio();
        
        boolean temLancamentoComFuroDeProduto = false;
        String codigoProduto 		= "";
        String descricaoProduto 	= "";
        Long produtoEdicao 			= null;
        BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
        BigDecimal valorTotalProduto 	= BigDecimal.ZERO;
        BigDecimal valorDescontoProduto = BigDecimal.ZERO;
        
        Collections.sort(itensNotaEnvio, new Comparator<ItemNotaEnvio>(){
            @Override
            public int compare(final ItemNotaEnvio o1, final ItemNotaEnvio o2) {
                if (o1 != null && o2 != null) {
                    if(o1 != null && o1.getSequenciaMatrizLancamento() != null && o2 != null && o2.getSequenciaMatrizLancamento() != null) {
                        return o1.getSequenciaMatrizLancamento().compareTo(o2.getSequenciaMatrizLancamento());
                    } else if ((o1.getProdutoEdicao() != null && o1.getProdutoEdicao().getProduto() != null)
                            && (o2.getProdutoEdicao() != null && o2.getProdutoEdicao().getProduto() != null)) {
                        o1.getProdutoEdicao().getProduto().getNome().compareTo(o2.getProdutoEdicao().getProduto().getNome());
                    }
                }
                return 0;
            }
            
        });
        
        
        for(final ItemNotaEnvio itemNotaEnvio : itensNotaEnvio) {
            
            codigoProduto 		= itemNotaEnvio.getCodigoProduto().toString();
            descricaoProduto 	= (itemNotaEnvio.getFuroProduto()==null) ? itemNotaEnvio.getPublicacao() :itemNotaEnvio.getPublicacao()+" (1) ";
            produtoEdicao		= itemNotaEnvio.getProdutoEdicao().getNumeroEdicao();
            
            valorUnitarioProduto = itemNotaEnvio.getPrecoCapa();
            valorDescontoProduto = itemNotaEnvio.getDesconto().divide(new BigDecimal("100"));
            valorTotalProduto	 = itemNotaEnvio.getPrecoCapa().multiply(new BigDecimal(itemNotaEnvio.getReparte()));
            
            final ItemImpressaoNfe item = new ItemImpressaoNfe();
            
            item.setCodigoProduto(codigoProduto);
            item.setDescricaoProduto(descricaoProduto);
            item.setProdutoEdicao(produtoEdicao);
            item.setQuantidadeProduto(new BigDecimal(itemNotaEnvio.getReparte().toString()));
            item.setValorUnitarioProduto(valorUnitarioProduto);
            item.setValorTotalProduto(valorTotalProduto);
            item.setValorDescontoProduto(valorTotalProduto.subtract(valorTotalProduto.multiply(valorDescontoProduto)));
            item.setSequencia(itemNotaEnvio.getSequenciaMatrizLancamento());
            item.setCodigoBarra(itemNotaEnvio.getProdutoEdicao().getCodigoDeBarras());
            
            listaItemImpressaoNfe.add(item);
            
            if(itemNotaEnvio.getFuroProduto()!= null){
            	temLancamentoComFuroDeProduto = true;
            }

        }

		nfeImpressao.setItensImpressaoNfe(listaItemImpressaoNfe);

	}
	
	private static String tratarCep(String cep) {

		if(cep == null) {
			return "          ";
		}

		if(cep.length() == 8) {
			return cep;
		}

		int qtdDigitosFaltantes = 8 - cep.length();

		while(--qtdDigitosFaltantes >= 0) {
			cep = cep + " ";
		}

		return cep;

	}
	
	private static String tratarTelefone(String telefone) {

		if(telefone == null) {
			return "          ";
		}

		if(telefone.length() == 8) {
			return telefone;
		}

		if(telefone.length() == 10) {
			return telefone;
		}

		int qtdDigitosFaltantes =  10 - telefone.length();

		while(--qtdDigitosFaltantes >= 0) {
			telefone = telefone + " ";
		}

		return telefone;

	}
}		
