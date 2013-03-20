package br.com.abril.nds.util.xls;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import br.com.abril.nds.util.upload.KeyValue;
import br.com.abril.nds.util.upload.XlsUploaderUtils;

public class UploaderXlsUtilTest {

	public static void main(String[] args) throws URISyntaxException {

		URL url = ClassLoader.getSystemResource("test.xlsx");
		List<KeyValue> list = XlsUploaderUtils.returnKeyValueFromXls(new File(url.toURI()));
		
		System.out.println("## retornando todos os valores que contem a chave cotaID");
		for (KeyValue keyValue : list) {
			if (keyValue.getKey().equals("cotaID")) {
				System.out.println(keyValue.getValue());
			}
		}
		
		System.out.println("\n\n## retornando todos os valores que contem a chave nomeCota");
		for (KeyValue keyValue : list) {
			if (keyValue.getKey().equals("nomeCota")) {
				System.out.println(keyValue.getValue());
			}
		}
		
		System.out.println("\n\n## retornando todos os objetos ##");
		for (KeyValue keyValue : list) {
			System.out.println("chave: " + keyValue.getKey());
			System.out.println("valor: " + keyValue.getValue());
		}
	}
}