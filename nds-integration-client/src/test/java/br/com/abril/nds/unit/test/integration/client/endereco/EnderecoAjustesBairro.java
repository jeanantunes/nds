package br.com.abril.nds.unit.test.integration.client.endereco;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.vo.EnderecoVO;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations="file:src/test/resources/spring/applicationContext-ndsi-test.xml")
public class EnderecoAjustesBairro {

	@Autowired
	private EnderecoService enderecoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoAjustesBairro.class);
	
	@Test
	public void testAtualizaBairrosNulos() {

		List<Endereco> enderecos = enderecoService.enderecosIncompletos();
		
		StringBuffer sb = new StringBuffer();
		
		StringBuffer erro = new StringBuffer();
		
		for (Endereco endereco : enderecos) {
			
			 // String logradouro, String estado, String nome
			
			EnderecoVO enderecoRetornado = enderecoService.obterEnderecoPorCep(endereco.getCep());
			
            if (enderecoRetornado != null) {
            	sb.append(enderecoRetornado.getLogradouro())
            	.append("|")
            	.append(enderecoRetornado.getUf())
            	.append("|")
            	.append(enderecoRetornado.getLocalidade() != null ? enderecoRetornado.getLocalidade() : "")
            	.append("|")
            	.append(enderecoRetornado.getBairro())
            	.append("|")
            	.append(enderecoRetornado.getCep())
            	.append(" \r\n");
            	
            	endereco.setBairro(enderecoRetornado.getBairro());
            	
            	this.enderecoService.salvarEndereco(endereco);
            } else {
            	
            	LOGGER.debug("Endereço não encontrado pelo CEP !!");
            	erro.append("Endereço não encontrado pelo CEP: ")
            		.append(endereco.getCep())
            		.append("\n"); 
            }
		}
		
		this.salvaCollection(sb, "/home/dgb/endereco.csv");
		
		System.out.println(erro.toString());
	}

	private void salvaCollection(StringBuffer sb, String nomeArquivo) {  
		  
        try {  
            FileOutputStream oStream = new FileOutputStream(nomeArquivo); // ou usando um File com argumento  
            OutputStreamWriter osw = new OutputStreamWriter(oStream);  
            Writer writer = new BufferedWriter(osw);  
            writer.write(sb.toString());  
            writer.flush();  
            writer.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
    }  
	
}