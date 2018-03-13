package br.com.abril.nds.util.export.cobranca.registrada.builders;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.util.TirarAcento;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistro01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroBradesco01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroItau01;

@Component
public class EnderecoSacadoBuilder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static void enderecoSacado(final CobRegEnvTipoRegistro01 registro01, final Endereco enderecoSacado, final String nomeSacado) {
		//ENDERECO DO SACADO
        registro01.setEnderecoSacado(enderecoSacado.getLogradouro());
		registro01.setComplemento(enderecoSacado.getComplemento());
		registro01.setCEP(enderecoSacado.getCep());
		registro01.setCidade(enderecoSacado.getCidade());
		registro01.setUF(enderecoSacado.getUf());
		registro01.setMensagemCedenteNomeSacadorAvalista(TirarAcento.removerAcentuacao(nomeSacado.trim()));
		registro01.setPrazoProtesto("99");
		registro01.setCodigoMoeda("00");
	}
	
	public static void enderecoSacado(CobRegEnvTipoRegistroItau01 registro01, Endereco enderecoSacado, String nomeSacado) {
		//ENDERECO DO SACADO
        registro01.setEnderecoSacado(TirarAcento.removerAcentuacao(enderecoSacado.getTipoLogradouro().trim()) +". "+ TirarAcento.removerAcentuacao(enderecoSacado.getLogradouro().trim()) + "," + enderecoSacado.getNumero().trim());
        registro01.setBairro(TirarAcento.removerAcentuacao(enderecoSacado.getBairro().trim()));
		registro01.setCEP(enderecoSacado.getCep().replace("-", "").replace(".", ""));
		registro01.setCidade(TirarAcento.removerAcentuacao(enderecoSacado.getCidade()));
		registro01.setUF(enderecoSacado.getUf());
		registro01.setSacadoAvalista(TirarAcento.removerAcentuacao(nomeSacado));
		registro01.setBracos01("");
	}
	
	public static void enderecoSacado(CobRegEnvTipoRegistroBradesco01 registro01, Endereco enderecoSacado, String nomeSacado) {
		//ENDERECO DO SACADO
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(removerAcentuacao(enderecoSacado.getTipoLogradouro()))
		  .append(" ")
		  .append(removerAcentuacao(enderecoSacado.getLogradouro()))
		  .append(" ")
		  .append(enderecoSacado.getNumero().trim())
		  .append(" ")
		  .append(removerAcentuacao(enderecoSacado.getBairro()))
		  .append(" ")
		  .append(removerAcentuacao(enderecoSacado.getCidade()))
		  .append(" ")
		  .append(removerAcentuacao(enderecoSacado.getUf()));
		
        registro01.setEnderecoCompleto(sb.toString().trim());
        
		registro01.setCepCompleto(enderecoSacado.getCep().trim());
		
		registro01.setSacadoAvalista(TirarAcento.removerAcentuacao(TirarAcento.removerAcentuacao(nomeSacado).trim()));
		
	}

	private static String removerAcentuacao(String texto){
		if(texto!=null && !texto.isEmpty()){
			return TirarAcento.removerAcentuacao(texto.trim());
		}
		return " ";
	}
}
