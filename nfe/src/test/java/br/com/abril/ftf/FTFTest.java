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
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro00;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro09;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FTFTest {

	private static String filePath = "/opt/parametros_nds/ftf/saida/";
	
	@Test
	public void testarArquivo() {
		
		List<FTFBaseDTO> list = new ArrayList<FTFBaseDTO>();
		List<FTFRetornoRET> l = new ArrayList<FTFRetornoRET>();
		File file = new File(filePath + "NDS0000036248116.PED");
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			
			while(br.ready()){
				
				String line = br.readLine();
				FTFBaseDTO ftfdto = FTFParser.parseLinhaRetornoFTF(line);
				list.add(ftfdto);
			}
			

			FTFRetornoRET n = new FTFRetornoRET();
			for (FTFBaseDTO dto : list) {
				if(dto instanceof FTFRetTipoRegistro00){
					n.setTipo00((FTFRetTipoRegistro00)dto);
				}else if(dto instanceof FTFRetTipoRegistro01){
					n.getTipo01List().add((FTFRetTipoRegistro01)dto);
				}else if(dto instanceof FTFRetTipoRegistro09){
					n.setTipo09((FTFRetTipoRegistro09)dto);
				}
			}
			
			l.add(n);
			
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