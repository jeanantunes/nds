package br.com.abril.nds.integracao.engine.test;

import java.util.Date;

import org.junit.Before;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;

public class TestJSON {
	private PDV pdv;
	
	@Before
	public void init() {
		pdv = new PDV();
		pdv.setContato("contato PDV");
		pdv.setDataInclusao(new Date());
		pdv.setEmail("paulokinho@terra.com.br");
		pdv.setId(1L);
		
		Endereco endereco = new Endereco();
		endereco.setBairro("Bairro");
		endereco.setCep("12333333");
		endereco.setId(1L);

		EnderecoPDV enderecoPDV = new EnderecoPDV();
		enderecoPDV.setPdv(pdv);
		enderecoPDV.setEndereco(endereco);

		pdv.getEnderecos().add(enderecoPDV);

		endereco = new Endereco();
		endereco.setBairro("Bairro 2");
		endereco.setCep("12333333");
		endereco.setId(2L);
		
		enderecoPDV = new EnderecoPDV();
		enderecoPDV.setPdv(pdv);
		enderecoPDV.setEndereco(endereco);
		
		pdv.getEnderecos().add(enderecoPDV);
	}
	/*
	@Test
	public void testXStream() {
		
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		
		System.out.println("\n\nXStream:\n\n");
		String xml = xstream.toXML(pdv);
		
		System.out.println(xml);
		
		PDV pdv_new = (PDV) xstream.fromXML(xml);
		
		System.out.println(pdv_new);
	}
	
//	@Test
	public void testJackson() throws Exception {		
		ObjectMapper objectMapper = new ObjectMapper();
		
		System.out.println("\n\nJACKSON:\n\n");
		System.out.println(objectMapper.writeValueAsString(pdv));
	}
	
//	@Test
	public void testFlexJson() throws Exception {		
		JSONSerializer jsonSerializer = new JSONSerializer();
		
		System.out.println("\n\nFlexJSON:\n\n");
		System.out.println(jsonSerializer.serialize(pdv));
	}
	*/
}