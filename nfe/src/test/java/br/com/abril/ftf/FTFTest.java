package br.com.abril.ftf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FTFTest {

	private static String filePath = "/opt/parametros_nds/ftf/saida/";
	
	@Test
	public void testarArquivoEnvio() {
		
		List<FTFBaseDTO> list = new ArrayList<FTFBaseDTO>();
		List<FTFRetornoRET> l = new ArrayList<FTFRetornoRET>();
		File file = new File(filePath + "NDS0000036248116.PED");
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			
			while(br.ready()) {
				
				String line = br.readLine();
				FTFBaseDTO ftfdto = FTFParser.parseLinhaEnvioFTF(line);
				list.add(ftfdto);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}