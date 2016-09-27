<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/retornoNFE.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript">
		$(function(){
			retornoNFEController.init();
			bloquearItensEdicao(retornoNFEController.workspace);
		});
	</script>

	<style type="text/css">
		#dialog-box{display:none;}
		#dialog-box fieldset{width:570px!important;}
	</style>

</head>

<body>
	
	<div id="dialog-confirmar" title="Atualização de Arquivo de Retorno NF-e?" style="display:none; ">		
		<p>Confirma a Atualiza&ccedil;&atilde;o de Arquivo de Retorno NF-e?</p>
	</div>
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
          		<a isEdicao="true" id="retornoNFEConfirmar" onclick="retornoNFEController.confirmar();" rel="tipsy" title="Confirmar Integração do Arquivo">
         			<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
         		</a>
            </span>
          	
          	<span class="bt_novos">
          		<a isEdicao="true" id="retorno-boleto-nfe" onclick="retornoNFEController.imprimirBoletoNFE();" hspace="5" rel="tipsy" title="Confirmar Gera&ccedil;&atilde;o do Boleto NF-e">
         			<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_soma_estudos.gif">
         		</a>
            </span>
          	
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	
   	<legend> Integra&ccedil;&atilde;o do Arquivo de Retorno NF-e</legend>
    
    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	    <tbody>
	    	
		    <tr>
		    	<td width="60">Data de Refer&ecirc;ncia:</td>
		        <td width="70" colspan="3">
		        	<input type="text" name="retornoNFEDataReferencia" id="retornoNFEDataReferencia" maxlength="10" class="input-date" />
				</td>
			</tr>
	    	<tr>
			
	    	<c:choose>
			    <c:when test="${tipoEmissor == 'EMISSAO_NFE_APLICATIVO_CONTRIBUINTE'}">
			       <tr>
		                <td width="300">
				        	<span class="bt_pesquisar">
				        		<a  id="retornoNFEPesquisar" href="javascript:;" onclick="retornoNFEController.pesquisar();"></a>
				        	</span>
				        </td>
					</tr>
			    </c:when>    
			    <c:otherwise>
			        <tr>
		                <td width="60"><input type="radio" name="tipoRetorno" id="autorizado" value="A" checked="checked" />Autorizado</td>
		                <td width="60"><input type="radio" name="tipoRetorno" id="cancelado" value="C"/>Cancelamento</td>
		                <td width="60"><input type="radio" name="tipoRetorno" id="rejeitado" value="R"/>Rejeitado</td>
		                <td width="300">
				        	<span class="bt_pesquisar">
				        		<a  id="retornoNFEPesquisar" href="javascript:;" onclick="retornoNFEController.pesquisar();"></a>
				        	</span>
				        </td>
					</tr>
			    </c:otherwise>
			</c:choose>
	    </tbody>
    </table>

    </fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<div class="grids" style="display:none;">
		<fieldset class="fieldGrid">
	    
	    	<legend>Arquivo retorno NFE</legend>
	        
			<div id="gridNFe" class="grids">
				<div id="retornoNfe-flexigrid-pesquisa" />
			</div>
	        
	        	<!-- 
				<p><strong>Dados do Arquivo:</strong> Encontrados <strong>XXXX</strong> Respostas</p>
	        	-->  
 	      </fieldset>
      </div>
      
<div id="modalUploadArquivoMix" title="Adicionar em Lote" style="display:none;">
 	<form action="${pageContext.request.contextPath}/financeiro/boletoAvulso/uploadArquivoLote" id="formUploadLoteMix" method="post" enctype="multipart/form-data">
 		<td width="91"><span>Banco:</span></td>
		<td>
			<select id="idBanco" name="idBanco" style="width:150px; font-size:11px!important">
				<c:forEach items="${bancos}" var="roteiro">
					<option value="${roteiro.key }">${roteiro.value }</option>
				</c:forEach>
			</select>
		</td>
 		
 		<br><br><br>
 		<hr>
 		
 		<p>Utilize o modelo de exemplo para fazer upload para o sistema: </p>
      	<p >
      		<span class="bt_novos" title="Download Modelo">
      			<a href="${pageContext.request.contextPath}/modelos/modelo_boleto_avulso.xls">
      				<img align="center" src="images/ico_excel.png" hspace="5" border="0" />Modelo de exemplo
      			</a>
      		</span>
      	</p>
      	<br><br><br>
      	<hr>
      	<p>Selecione um arquivo para upload:</p>
      	<br>
      	<p align="center">
      		<input type="file" id="excelFile" name="excelFile" style="width:200px"/>
      	</p>
     </form>
</div>
      
</body>	