package br.com.abril.nds.util.xls;

<<<<<<< HEAD
=======
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
>>>>>>> DGBti/fase2
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import br.com.abril.nds.util.xls.CotaTemplateDTO;

import br.com.abril.nds.util.upload.XlsMapper;
<<<<<<< HEAD

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
		URL url = ClassLoader.getSystemResource("cota-template.xlsx");
		List<CotaTemplateDTO> lista = null;//XlsUploaderUtils.getBeanListFromXls(CotaTemplateDTO.class, new File(url.toURI()));
=======
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

public class UploaderXlsUtilTest {


    private static URL url = ClassLoader.getSystemResource("cota-template.xlsx");

    /**
     * @param args
     * @throws URISyntaxException
     * 
     *             Para o funcionamento desse serviço, deve-se anotar o DTO(ou qualquer bean que desejar) com a anotação
     *             {@link XlsMapper} Nessa anotação, você basicamente vai fazer o mapeamento, dizendo qual coluna do XLS,
     *             corresponde o seu field do seu bean. Vejam o exemplo no Bean de teste criado {@link CotaXlsDTO}
     * 
     */
    public static void main(String[] args) throws URISyntaxException {
	
	List<CotaTemplateDTO> lista = XlsUploaderUtils.getBeanListFromXls(CotaTemplateDTO.class, getUploadedFile());
>>>>>>> DGBti/fase2
		for (CotaTemplateDTO cotaTemplateDTO : lista) {
		    System.out.println(cotaTemplateDTO.getCodigoProduto());
		    System.out.println(cotaTemplateDTO.getNumeroCota());
		    System.out.println(cotaTemplateDTO.getReparteMinimo());
		    System.out.println(cotaTemplateDTO.getReparteMaximo());
		}
    }
    
    public static UploadedFile getUploadedFile() {
	return new UploadedFile() {

	    @Override
	    public String getFileName() {
		try {
		    return url.toURI().toString();
		} catch (URISyntaxException e) {
		    e.printStackTrace();
		}
		return "";
	    }

	    @Override
	    public InputStream getFile() {
		try {
		    return new FileInputStream(url.toURI().getPath());
		} catch (FileNotFoundException | URISyntaxException e) {
		    e.printStackTrace();
		}
		return null;
	    }

	    @Override
	    public String getContentType() {
		return "multipart/form-data";
	    }
	};
    }
}