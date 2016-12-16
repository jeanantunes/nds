package br.com.abril.nds.util.export.cobranca.registrada.builders;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.util.TirarAcento;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistro01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroBradesco01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroItau01;

@Component
public class PopularSacadoBuilder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CODIGO_INSCRICAO_FISICA = "01";
    
    private static final String CODIGO_INSCRICAO_JURIDICA = "02";
	
	public static void popularSacadoCobrana(CobRegEnvTipoRegistroBradesco01 registro01, Boleto boleto) {
		
		final Cota cota = boleto.getCota(); 
		
		final Pessoa pessoaSacado = cota.getPessoa();
		
		Endereco enderecoSacado = cota.getEnderecoPrincipal().getEndereco();
		
		//DADOS DO SACADO
        String nomeSacado = null;
        
        String documentoSacado = null;
        
        if (pessoaSacado instanceof PessoaFisica) {
        	
            nomeSacado = ((PessoaFisica) pessoaSacado).getNome();
            documentoSacado = ((PessoaFisica) pessoaSacado).getCpf();
        	registro01.setCodigoInscricaoSacado(CODIGO_INSCRICAO_FISICA);

        }
        if (pessoaSacado instanceof PessoaJuridica) {
        	registro01.setCodigoInscricaoSacado(CODIGO_INSCRICAO_JURIDICA);
            nomeSacado = ((PessoaJuridica) pessoaSacado).getRazaoSocial();
            documentoSacado = ((PessoaJuridica) pessoaSacado).getCnpj();
        }
        
        registro01.setNumeroCNPJCPF(documentoSacado.replace(".", "").replace("-", "").replace("/", ""));
                
        registro01.setNomeSacado(TirarAcento.removerAcentuacao(nomeSacado));
        
        registro01.setBracos01("");
        
        EnderecoSacadoBuilder.enderecoSacado(registro01, enderecoSacado, nomeSacado);
	}
	
	public static void popularSacadoCobrana(CobRegEnvTipoRegistro01 registro01, Boleto boleto) {
		
		final Cota cota = boleto.getCota(); 
		
		final Pessoa pessoaSacado = cota.getPessoa();
		
		Endereco enderecoSacado = cota.getEnderecoPrincipal().getEndereco();
		
		//DADOS DO SACADO
        String nomeSacado = null;
        
        String documentoSacado = null;
        
        if (pessoaSacado instanceof PessoaFisica) {
        	
            nomeSacado = ((PessoaFisica) pessoaSacado).getNome();
            documentoSacado = ((PessoaFisica) pessoaSacado).getCpf();
        	registro01.setCodigoInscricaoSacado(CODIGO_INSCRICAO_FISICA);

        }
        if (pessoaSacado instanceof PessoaJuridica) {
        	registro01.setCodigoInscricaoSacado(CODIGO_INSCRICAO_JURIDICA);
            nomeSacado = ((PessoaJuridica) pessoaSacado).getRazaoSocial();
            documentoSacado = ((PessoaJuridica) pessoaSacado).getCnpj();
        }
        
        registro01.setNumeroCNPJCPF(documentoSacado.replace(".", "").replace("-", "").replace("/", ""));
                
        registro01.setNomeSacado(TirarAcento.removerAcentuacao(nomeSacado));
        
        EnderecoSacadoBuilder.enderecoSacado(registro01, enderecoSacado, nomeSacado);
	}
	
	public static void popularSacadoCobrana(CobRegEnvTipoRegistroItau01 registro01, Boleto boleto) {
		
		final Cota cota = boleto.getCota(); 
		
		final Pessoa pessoaSacado = cota.getPessoa();
		
		Endereco enderecoSacado = cota.getEnderecoPrincipal().getEndereco();
		
		//DADOS DO SACADO
        String nomeSacado = null;
        
        String documentoSacado = null;
        
        if (pessoaSacado instanceof PessoaFisica) {
        	
            nomeSacado = ((PessoaFisica) pessoaSacado).getNome();
            documentoSacado = ((PessoaFisica) pessoaSacado).getCpf();
        	registro01.setCodigoInscricaoSacado(CODIGO_INSCRICAO_FISICA);

        }
        if (pessoaSacado instanceof PessoaJuridica) {
        	registro01.setCodigoInscricaoSacado(CODIGO_INSCRICAO_JURIDICA);
            nomeSacado = ((PessoaJuridica) pessoaSacado).getRazaoSocial();
            documentoSacado = ((PessoaJuridica) pessoaSacado).getCnpj();
        }
        
        registro01.setNumeroCNPJCPF(documentoSacado.replace(".", "").replace("-", "").replace("/", ""));
                
        registro01.setNomeSacado(TirarAcento.removerAcentuacao(nomeSacado));
        
        registro01.setBracos01("");
        
        EnderecoSacadoBuilder.enderecoSacado(registro01, enderecoSacado, nomeSacado);
	}
}
