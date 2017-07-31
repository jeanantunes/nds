package br.com.abril.nds.service.impl;

import java.rmi.RemoteException;

import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.AttributedString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.PasswordString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityDocument;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.UsernameTokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais;
import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisElementDocument;
import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse;
import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponseElementDocument;
import v1.pessoadetalhe.ebo.abril.types.PessoaDto;
import br.com.abril.axis2.PessoaCRPService_2Stub;
import br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril;
import br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.PessoaCRPWSService;

@Service
public class PessoaCRPWSServiceImpl implements PessoaCRPWSService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PessoaCRPWSServiceImpl.class);
	
	@Autowired
	private PessoaCRPService_2Stub pessoaCRPService2Stub;
	
	@Value("${userNameWS:}")
	private String userNameWS;
	
	@Value("${passwordWS:}")
	private String passwordWS;
	
	@Value("${codSistema:}")
	private String codSistema;
	
	public PessoaDto obterDadosFiscais(Integer codTipoDoc, String numDoc){
		
		ObterDadosFiscaisElementDocument odfEl = ObterDadosFiscaisElementDocument.Factory.newInstance();
		
		ObterDadosFiscais odf = odfEl.addNewObterDadosFiscaisElement();
		odf.setCodSistema(codSistema);
		odf.setCodTipoDoc(codTipoDoc);
		odf.setNumDoc(numDoc);
		
		ObterDadosFiscaisResponseElementDocument obterDadosFiscais = null;
		try {
			int i =0/0;
			obterDadosFiscais = pessoaCRPService2Stub.obterDadosFiscais(odfEl, this.createSecurityDocElement(), this.createCabecalhoAbrilDocument());
		
		} catch (RemoteException e) {
			LOGGER.error("Não foi possivel acessar o cadastro corporativo.", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possivel acessar o cadastro corporativo.");
			
		}
		
		ObterDadosFiscaisResponse obterDadosFiscaisResponseElement = obterDadosFiscais.getObterDadosFiscaisResponseElement();
		
		System.out.println(obterDadosFiscaisResponseElement);
		return obterDadosFiscaisResponseElement.getResult();
		
	}
	
	
	private SecurityDocument createSecurityDocElement(){
		
		SecurityDocument secDoc = SecurityDocument.Factory.newInstance();
		SecurityHeaderType secHeadrType = secDoc.addNewSecurity();
		UsernameTokenType usernameToken = secHeadrType.addNewUsernameToken();
		AttributedString newUsername = usernameToken.addNewUsername();
		newUsername.set(this.userNameWS);
		
		PasswordString newPassword = usernameToken.addNewPassword();
		newPassword.set(this.passwordWS);
		
		return secDoc;
	}
	
	private CabecalhoAbrilDocument createCabecalhoAbrilDocument(){
		CabecalhoAbrilDocument caDoc = CabecalhoAbrilDocument.Factory.newInstance();
		CabecalhoAbril ca = caDoc.addNewCabecalhoAbril();
		
		return caDoc;
	}
}
