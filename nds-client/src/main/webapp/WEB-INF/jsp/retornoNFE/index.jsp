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
         		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif"></a>
            </span>
          
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	
   	<legend> Integra&ccedil;&atilde;o do Arquivo de Retorno NF-e</legend>
    
    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	    <tbody>
		    <tr>
		    	<td width="70">Data de Refer&ecirc;ncia:</td>
		        <td width="170">
		        	<input type="text" name="retornoNFEDataReferencia" id="retornoNFEDataReferencia" maxlength="10" class="input-date" />
				</td>
				<td width="200">
		        	<span class="bt_pesquisar">
		        		<a  id="retornoNFEPesquisar" href="javascript:;" onclick="retornoNFEController.pesquisar();"></a>
		        	</span>
		        </td>
			</tr>
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
</body>	