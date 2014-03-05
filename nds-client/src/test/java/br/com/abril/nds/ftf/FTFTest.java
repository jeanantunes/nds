package br.com.abril.nds.ftf;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.service.FTFService;

public class FTFTest {

	public static void main(String[] args) {
		
		
		String configLocation = "c:/workspace-22-marco-13/NDS/nds-client/src/main/resources/applicationContext-local.xml";
		
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(configLocation);
		
		FTFService bean = applicationContext.getBean(FTFService.class);
		
//		FTFReportDTO gerarFtf = bean.gerarFtf(null, 7l);
		
		
//		FTFEnvTipoRegistro08 retornoFTFEnvTipoRegistro08 = bean.retornoFTFEnvTipoRegistro08();
		
		
//		CotaService bean = applicationContext.getBean(CotaService.class);
//		Cota cota = bean.obterPorCpfCnpj("01666323845");
		
//		System.out.println(cota);
//		File f = new File("192.168.1.14/txt/armaria-nam.txt");
//		  
//		  try {
//		   BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//		   bw.write("Samuel here!!!");
//		   bw.flush();
//		   bw.close();
//		   
//		   /*BufferedReader br = new BufferedReader(new FileReader(f));
//		   while(br.ready()){
//		    System.out.println(br.readLine());
//		   }*/
//		  } catch (FileNotFoundException e) {
//		   e.printStackTrace();
//		  } catch (IOException e) {
//		   // TODO Auto-generated catch block
//		   e.printStackTrace();
//		  }
	}
}
