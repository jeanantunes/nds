<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaBoletos.js"></script>

<script type="text/javascript">

        var pesquisaCotaConsultaBoletos = new PesquisaCota(consultaBoletosController.workspace);

        $(function(){
                consultaBoletosController.init();
        });

</script>

</head>

<body>


        <div class="areaBts">
                <div class="area">
                        <span class="bt_arq">
                                <a href="${pageContext.request.contextPath}/financeiro/boletos/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
                                        <img src="${pageContext.request.contextPath}/images/ico_excel.png" border="0" />
                                </a>
                        </span>
                        
                        <span class="bt_arq">
                                <a href="${pageContext.request.contextPath}/financeiro/boletos/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
                                        <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
                                </a>
                        </span>
                </div>
        </div>
        <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
               <legend> Pesquisar Boletos por Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
            
              <td width="28">Cota:</td>
              <td width="76">
                      <input name="numCota" 
                                 id="numCota" 
                                 type="text"
                                 maxlength="11"
                                 style="width:70px; 
                                 float:left; margin-right:5px;"
                                 onchange="pesquisaCotaConsultaBoletos.pesquisarPorNumeroCota('#numCota', '#descricaoCota');" />
                          </td>
                                
                          
              <td width="30">Nome:</td>              <td width="189">
                              <input name="descricaoCota" 
                                               id="descricaoCota" 
                                               type="text" 
                                               class="nome_jornaleiro" 
                                               maxlength="255"
                                               style="width:180px;"
                                               onkeyup="pesquisaCotaConsultaBoletos.autoCompletarPorNome('#descricaoCota');" 
                                               onblur="pesquisaCotaConsultaBoletos.pesquisarPorNomeCota('#numCota', '#descricaoCota');" />
                          </td>
              

              <td width="114">Data de Vencimento:</td>
              <td width="113"><input name="dataDe" id="dataDe" type="text" style="width:80px; float:left; margin-right:5px;" /></td>
              <td width="26">Até:</td>
              <td width="113"><input name="dataAte" id="dataAte" type="text" style="width:80px; float:left; margin-right:5px;" /></td>
              <td width="44">Status:</td>
              <td width="128">
                 <select name="status" id="status" style="width:100px;">
                    <c:forEach varStatus="counter" var="status" items="${listaStatusCombo}">
                                       <option value="${status.key}">${status.value}</option>
                                    </c:forEach>
                 </select>
              </td>
              
              <td width="33"><span class="bt_novos"><a href="javascript:;" onclick="consultaBoletosController.mostrarGridConsulta();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
               <fieldset class="fieldGrid">
               
                         <legend>Boletos Cadastrados</legend>

                               <table class="boletosCotaGrid"></table>
                        
                                
              </fieldset>
            </div>
	 <iframe src="" id="download-iframe-boleto" style="display:none;"></iframe>
</body>