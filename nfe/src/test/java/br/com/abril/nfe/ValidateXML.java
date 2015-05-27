package br.com.abril.nfe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import br.com.abril.nfe.util.ErrorHandler;
import br.com.abril.nfe.util.FileToString;
import br.com.abril.nfe.util.StringToInputStream;


/**
 * <b>Classe responsável por validar arquivos XML x template XSD.</b>
 *
 * @author Dilnei Cunha.
 */
public class ValidateXML {

    /**
     * <b>Método que faz a validação de arquivos XML.</b>
     *
     * @param File - File XML que devera ser validada.
     * @param xsdFullFileName - Caminho até o arquivo XSD responsável por conter
     * as regras da validação.
     *
     * @return boolean
     */
    public boolean validate(File file, String xsdFullFileName) {
        boolean validate = false;
        try {
            validate = validate(FileToString.readFileAsString(file.getAbsolutePath()), xsdFullFileName);
        } catch (IOException ex) {
            validate = false;
            ex.printStackTrace();
        }
        return validate;
    }

    /**
     * <b>Método que faz a validação de arquivos XML.</b>
     *
     * @param xmlStr - String XML que devera ser validada.
     * @param xsdFullFileName - Caminho até o arquivo XSD responsável por conter
     * as regras da validação.
     *
     * @return boolean
     */
    public boolean validate(String xmlStr, String xsdFullFileName) {

        // Crio a fabrica.
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        // Habilito suporte a namespace. 
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(true);

        // Atributos para validação.
        documentBuilderFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        documentBuilderFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsdFullFileName);

        // Crio uma builder para obter o Document de um .xml
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return false;
        }

        // Guardo os erros de validação. 
        ErrorHandler errorHandler = new ErrorHandler();
        documentBuilder.setErrorHandler(errorHandler);

        // Declaro as variaveis a serem utilizadas.
        Document document = null;

        // Transfromando em Stram de dados.
        InputStream is = new StringToInputStream().inputStreamStr(xmlStr);

        try {
            // Primeiro parse.
            document = documentBuilder.parse(is);
        } catch (SAXParseException sAXParseException) {
            sAXParseException.printStackTrace();
            return false;
        } catch (SAXException sAXException) {
            sAXException.printStackTrace();
            return false;
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return false;
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            return false;
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // carrega um WXS schema, representada por uma instacia Schema.
        Source schemaFile = new StreamSource(new File(xsdFullFileName));
        Schema schema = null;
        try {
            schema = schemaFactory.newSchema(schemaFile);
        } catch (SAXException ex) {
            ex.printStackTrace();
            return false;
        }

        // Cria um objeto ValidationHandler que pode ser usado para validar uma instancia document.
        Validator validator = schema.newValidator();

        // Indica o objeto que irá tratar os error. Observe que ao encontrar
        // um erro, este é simplesmente guardado e processo de validação continua.
        try {
            // Efetua a validação propriamente.
            validator.validate(new DOMSource(document));
        } catch (Exception e) {
            // Se algum erro foi encontrado, apresenta-o.
            if (!errorHandler.handlerList.isEmpty()) {
                for (String error : errorHandler.handlerList) {
                    System.out.println(error);
                }
            }
            return false;
        }
        return true;
    }
}