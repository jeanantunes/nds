package br.com.abril.ftf;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class POCRetornoFTF {

	public static void main(String[] args) {/*
		String s = "/temp/DIN84088_VD_DIFC0000000001.RET";
		
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
		
		
	*/
	
		File f = new File("\\\\INFOA2003-PC\\txt\\");
		
		
		File[] listFiles = f.listFiles(new FileFilter(){

			@Override
			public boolean accept(File f) {
				
				return f.getName().toLowerCase().endsWith(".ped");
			}
			
		});
		for (File s: listFiles) {
			Calendar instance = GregorianCalendar.getInstance();
			instance.setTimeInMillis(s.lastModified());
			
			System.out.println(instance.getTime());
		}
		
	}

}

