package br.com.abril.nfe.homologacao;

public class NfeInutilizacaoSoapProxy implements br.com.abril.nfe.homologacao.NfeInutilizacaoSoap {
  private String _endpoint = null;
  private br.com.abril.nfe.homologacao.NfeInutilizacaoSoap nfeInutilizacaoSoap = null;
  
  public NfeInutilizacaoSoapProxy() {
    _initNfeInutilizacaoSoapProxy();
  }
  
  public NfeInutilizacaoSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initNfeInutilizacaoSoapProxy();
  }
  
  private void _initNfeInutilizacaoSoapProxy() {
    try {
      nfeInutilizacaoSoap = (new br.com.abril.nfe.homologacao.NfeInutilizacaoLocator()).getNfeInutilizacaoSoap();
      if (nfeInutilizacaoSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)nfeInutilizacaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)nfeInutilizacaoSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (nfeInutilizacaoSoap != null)
      ((javax.xml.rpc.Stub)nfeInutilizacaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.abril.nfe.homologacao.NfeInutilizacaoSoap getNfeInutilizacaoSoap() {
    if (nfeInutilizacaoSoap == null)
      _initNfeInutilizacaoSoapProxy();
    return nfeInutilizacaoSoap;
  }
  
  public java.lang.String nfeInutilizacaoNF(java.lang.String nfeCabecMsg, java.lang.String nfeDadosMsg) throws java.rmi.RemoteException{
    if (nfeInutilizacaoSoap == null)
      _initNfeInutilizacaoSoapProxy();
    return nfeInutilizacaoSoap.nfeInutilizacaoNF(nfeCabecMsg, nfeDadosMsg);
  }
  
  
}