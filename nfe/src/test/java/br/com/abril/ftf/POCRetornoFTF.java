package br.com.abril.ftf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro00;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro09;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;

public class POCRetornoFTF {
	
	@Test
	public void retornoArquivoFTF() {
		
		String s = "c:/tmp/NDS0000055318019.RET";
		
		List<FTFBaseDTO> list = new ArrayList<FTFBaseDTO>();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(s)));
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
			
			System.out.println(n);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}