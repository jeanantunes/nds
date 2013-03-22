package br.com.abril.nds.util.xls;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import br.com.abril.nds.util.upload.XlsMapper;
import br.com.abril.nds.util.upload.XlsUploaderUtils;

public class UploaderXlsUtilTest {

	/**
	 * @param args
	 * @throws URISyntaxException
	 * 
	 * Para o funcionamento desse serviço, deve-se anotar o DTO(ou qualquer bean que desejar) com a anotação {@link XlsMapper}
	 * Nessa anotação, você basicamente vai fazer o mapeamento, dizendo qual coluna do XLS, corresponde o seu field do seu bean.
	 * Vejam o exemplo no Bean de teste criado {@link CotaXlsDTO}
	 * 
	 */
	public static void main(String[] args) throws URISyntaxException {
		URL url = ClassLoader.getSystemResource("test.xlsx");
		List<CotaXlsDTO> lista = XlsUploaderUtils.getBeanListFromXls(CotaXlsDTO.class, new File(url.toURI()));
		for (CotaXlsDTO cota : lista) {
			System.out.println(cota.getIdCota());
			System.out.println(cota.getNomeCota());
		}
	}
}