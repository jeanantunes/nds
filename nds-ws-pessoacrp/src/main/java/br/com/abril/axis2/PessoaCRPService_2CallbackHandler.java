
/**
 * PessoaCRPService_2CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package br.com.abril.axis2;

    /**
     *  PessoaCRPService_2CallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class PessoaCRPService_2CallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public PessoaCRPService_2CallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public PessoaCRPService_2CallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for obterBloqueio method
            * override this method for handling normal response from obterBloqueio operation
            */
           public void receiveResultobterBloqueio(
                    v1.pessoadetalhe.ebo.abril.types.ObterBloqueioResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterBloqueio operation
           */
            public void receiveErrorobterBloqueio(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for adicionarAlterarCliente method
            * override this method for handling normal response from adicionarAlterarCliente operation
            */
           public void receiveResultadicionarAlterarCliente(
                    v1.pessoadetalhe.ebo.abril.types.AdicionarAlterarClienteResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from adicionarAlterarCliente operation
           */
            public void receiveErroradicionarAlterarCliente(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obterContaCorrente method
            * override this method for handling normal response from obterContaCorrente operation
            */
           public void receiveResultobterContaCorrente(
                    v1.pessoadetalhe.ebo.abril.types.ObterContaCorrenteResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterContaCorrente operation
           */
            public void receiveErrorobterContaCorrente(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obterProtocolo method
            * override this method for handling normal response from obterProtocolo operation
            */
           public void receiveResultobterProtocolo(
                    v1.pessoadetalhe.ebo.abril.types.ObterProtocoloResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterProtocolo operation
           */
            public void receiveErrorobterProtocolo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obterPapel method
            * override this method for handling normal response from obterPapel operation
            */
           public void receiveResultobterPapel(
                    v1.pessoadetalhe.ebo.abril.types.ObterPapelResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterPapel operation
           */
            public void receiveErrorobterPapel(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obterFormaPagto method
            * override this method for handling normal response from obterFormaPagto operation
            */
           public void receiveResultobterFormaPagto(
                    v1.pessoadetalhe.ebo.abril.types.ObterFormaPagtoResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterFormaPagto operation
           */
            public void receiveErrorobterFormaPagto(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obterDadosFiscais method
            * override this method for handling normal response from obterDadosFiscais operation
            */
           public void receiveResultobterDadosFiscais(
                    v1.pessoadetalhe.ebo.abril.types.ObterDadosFiscaisResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterDadosFiscais operation
           */
            public void receiveErrorobterDadosFiscais(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obterContratante method
            * override this method for handling normal response from obterContratante operation
            */
           public void receiveResultobterContratante(
                    v1.pessoadetalhe.ebo.abril.types.ObterContratanteResponseElementDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obterContratante operation
           */
            public void receiveErrorobterContratante(java.lang.Exception e) {
            }
                


    }
    