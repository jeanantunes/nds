package ws.impl;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.AttributedString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.PasswordString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityDocument;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.SecurityHeaderType;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0_xsd.UsernameTokenType;

import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscais;
import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisElementDocument;
import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponse;
import v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponseElementDocument;
import br.com.abril.axis2.PessoaCRPService_2Stub;
import br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbril;
import br.com.abril.canonico.v1.comum.cabecalhoabril.CabecalhoAbrilDocument;


public class Client {

	/*
	public static void main(String[] args) {
//		FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext("c:/temp/applicationContext-local.xml");
		
		PessoaCRPService2 ps = new PessoaCRPService2();
		ObterDadosFiscais odf = new ObterDadosFiscais();
		odf.setCodSistema("prodin");
		odf.setCodTipoDoc(1);
		odf.setNumDoc("61438248002509");
		
		PessoaCRPService psPt = ps.getPessoaCRPServicePt();
		
		
		((BindingProvider) psPt).getRequestContext().put(
	            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://testsoa.abril.com.br/extranet/Cadastro/PessoaCRPProxyEV1");
		
		 
		((BindingProvider) psPt).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "E43861");
		((BindingProvider) psPt).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "Abcd1234");
	
		
		psPt.obterDadosFiscais(odf).getResult().toString();
	}*/
	
	public static void main(String[] args) {
		try {
			PessoaCRPService_2Stub stub = new PessoaCRPService_2Stub("https://testsoa.abril.com.br/extranet/Cadastro/PessoaCRPProxyEV1");
			
		/*	OMFactory omFactory = OMAbstractFactory.getOMFactory();

			OMElement omSecurityElement = omFactory.createOMElement(new QName( "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse"), null);
			OMElement omusertoken = omFactory.createOMElement(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "UsernameToken", "wsse"), null);

			OMElement omuserName = omFactory.createOMElement(new javax.xml.namespace.QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Username", "wsse"), null);
			omuserName.setText("E43861");

			OMElement omPassword = omFactory.createOMElement(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Password", "wsse"), null);
			omPassword.addAttribute("Type","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText",null );
			omPassword.setText("Abcd1234");

			omusertoken.addChild(omuserName);
			omusertoken.addChild(omPassword);
			omSecurityElement.addChild(omusertoken);
			stub._getServiceClient().addHeader(omSecurityElement);
*/
			ObterDadosFiscaisElementDocument odfEl = ObterDadosFiscaisElementDocument.Factory.newInstance();
			
			ObterDadosFiscais odf = odfEl.addNewObterDadosFiscaisElement();
			odf.setCodSistema("NDS");
			odf.setCodTipoDoc(1);
			odf.setNumDoc("68252618000182");
			
			
			SecurityDocument secDoc = SecurityDocument.Factory.newInstance();
			SecurityHeaderType secHeadrType = secDoc.addNewSecurity();
			UsernameTokenType usernameToken = secHeadrType.addNewUsernameToken();
			AttributedString newUsername = usernameToken.addNewUsername();
			newUsername.set("E43861");
			
			PasswordString newPassword = usernameToken.addNewPassword();
			newPassword.set("Abcd1234");
			
			CabecalhoAbrilDocument caDoc = CabecalhoAbrilDocument.Factory.newInstance();
			
			/*XmlCursor newCursor = caDoc.newCursor();
			newCursor.toNextToken();
			newCursor.insertNamespace("cab", "http://canonico.abril.com.br/v1/Comum/CabecalhoAbril");
			newCursor.dispose();
			*/
			CabecalhoAbril ca = caDoc.addNewCabecalhoAbril();
			
			
			System.out.println(caDoc.toString());
			System.out.println(secDoc.toString());
			System.out.println(odfEl.toString());
			
			ObterDadosFiscaisResponseElementDocument obterDadosFiscais = stub.obterDadosFiscais(odfEl, secDoc, caDoc);
			
			ObterDadosFiscaisResponse obterDadosFiscaisResponseElement = obterDadosFiscais.getObterDadosFiscaisResponseElement();
			
			//PessoaDto pessoa = obterDadosFiscaisResponseElement.getResult();
			//PessoaType pessoaType = pessoa.getPessoa();
			System.out.println(obterDadosFiscaisResponseElement);
			//System.out.println(pessoaType.getNome());
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
