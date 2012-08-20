<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/retornoNFE.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript">
		var retornoNFEController;

		$(function() {
			retornoNFEController = $.extend(true,new RetornoNFEController(), BaseController);	
									
			$("#retornoNFEDataReferencia", this.workspace).datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy'
			});	

			$("#retornoNFEDataReferencia", this.workspace).mask("99/99/9999");	
			$("#retornoNFEDataReferencia", this.workspace).val(formatDateToString(new Date()));
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
	
	<fieldset class="classFieldset">
   	
   	<legend> Integra&ccedil;&atilde;o do Arquivo de Retorno NF-e</legend>
    
    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
    <tbody>
    <tr>
    	<td width="123">Data de Refer&ecirc;ncia:</td>
        <td width="130">
        	<input type="text" 
				   name="retornoNFEDataReferencia" 
				   id="retornoNFEDataReferencia" 
				   style="width: 80px; float: left; margin-right: 5px;"
				   maxlength="10"
				   value="" />
		</td>
       
        <td width="688">
        	<span class="bt_pesquisar">
        		<a  id="retornoNFEPesquisar" href="javascript:;">Pesquisar</a>
        	</span>
        </td>
	</tr>
    </tbody>
    </table>

    </fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
    
    <legend> Integra&ccedil;&atilde;o do Arquivo de Retorno NF-e</legend>
    
        <div class="grids" style="">
        	<!-- 
			<p><strong>Dados do Arquivo:</strong> Encontrados <strong>XXXX</strong> Respostas</p>
        	-->  
        	<br>
         
          	<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
            <tbody>
            <tr>
                <td><b>Totais Lidos</b></td>
                <td align="center">&nbsp;</td>
                <td align="center">&nbsp;</td>
            </tr>
            	<tr class="header_table">
                <td width="189" align="center">Num. Total de Arquivos</td>
                <td width="202" align="center">Num. NF-e</td>
                <td width="189" align="center">Erros Consis.</td>
            </tr>
            <tr class="class_linha_1">
            	<td id="numeroArquivos" align="center">0</td>
                <td id="notasAprovadas" align="center">0</td>
                <td id="notasRejeitadas" align="center">0</td>
            </tr>
        	</tbody>
        	</table>
        <br>
		<br clear="all">
          
          <span class="bt_confirmar_novo" title="Confirmar">
          	<a id="retornoNFEConfirmar" href="javascript:;">
         	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirma</a>
          </span>
          
		</div>
        
      </fieldset>

</body>	