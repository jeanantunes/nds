package br.com.abril.nds.ftfutil.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.ftfutil.FTFParser;

public class BuildFTF {

	
	public static void main(String[] args) throws Exception {
		
		List<PessoaDTO> list = new ArrayList<PessoaDTO>();
		
		PessoaDTO pessoaDTO = new PessoaDTO();
		pessoaDTO.setCpf("0578696,2903");
		pessoaDTO.setRg("776765,20");
		pessoaDTO.setNome("Tiago");
		pessoaDTO.setEmail("luistiago.andrighetto@gmail.com");
		
		list.add(pessoaDTO);
		
		pessoaDTO = new PessoaDTO();
		pessoaDTO.setCpf("0423");
		pessoaDTO.setRg("83274732424");
		pessoaDTO.setNome("Joao");
		pessoaDTO.setEmail("joao@gmail.com");
		
		list.add(pessoaDTO);
		
		pessoaDTO = new PessoaDTO();
		pessoaDTO.setCpf("87423743333");
		pessoaDTO.setRg("23432452");
		pessoaDTO.setNome("Jose");
		pessoaDTO.setEmail("jose@gmail.com");
		
		list.add(pessoaDTO);
		
		FTFParser ftfParser =  new FTFParser();
		
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C:\\Tiago\\testeFTF.txt")));
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C:\\testes\\testeFTF.txt")));
		
		for (PessoaDTO dto:list) {
			ftfParser.parseFTF(dto, bw);
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		
		
	}
}
