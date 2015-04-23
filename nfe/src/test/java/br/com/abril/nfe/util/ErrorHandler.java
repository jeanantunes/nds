package br.com.abril.nfe.util;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <b>Responsavel por registrar os erros do XML.</b>
 *
 * @author Dilnei Cunha
 */
public class ErrorHandler implements org.xml.sax.ErrorHandler {

    public final List<String> handlerList = new ArrayList<>();

    /**
     * <b>Contém as mensagens de alerta.</b>
     *
     * @param exception
     * @throws SAXException
     */
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        handlerList.add("ATENÇÃO: " + exception.getMessage());
    }

    /**
     * <b>Contém as mensagens de erro.</b>
     *
     * @param exception
     * @throws SAXException
     */
    @Override
    public void error(SAXParseException exception) throws SAXException {
        handlerList.add("ERRO: " + exception.getMessage());
    }

    /**
     * <b>Contém as mensagens de erro fatal.</b>
     *
     * @param exception
     * @throws SAXException
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        handlerList.add("ERRO FATAL: " + exception.getMessage());
    }
}