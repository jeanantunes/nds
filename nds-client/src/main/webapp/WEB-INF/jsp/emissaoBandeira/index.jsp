<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/emissaoBandeira.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>


<style type="text/css">
  #dialog-pesq-fornecedor fieldset, {width:450px!important;}
  .box_field{width:200px;}
  fieldset label {width: auto; margin-bottom: 0px!important;
}
	.forncedoresSel{display:none;}
	.editorSel{display:none;}
	#dialog-pesq-fornecedor, #dialog-pallets{display:none;}
.selFornecedores ul, .selEditores ul{ margin:0px; padding:0px;}
.selFornecedores li, .selEditores li{display:inline; list-style:none;}
  </style>
</head>

<body>

    <div class="areaBts">
		<div class="area">
			<span class="bt_arq">
			    <a title="Imprimir Bandeira" href="javascript:;" onclick="emissaoBandeiraController.imprimirBandeira();" rel="bandeira">
			        <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
                </a>
			</span> 
			
			<span class="bt_arq"> 
				<a title="Imprimir Relatório"  href="javascript:;" onclick="emissaoBandeiraController.imprimirArquivo('PDF')">
				    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>

			<span class="bt_novos"> 
				<a title="Bandeira Manual"  href="javascript:;" onclick="emissaoBandeiraController.bandeiraManual()"  >
				    <img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0" />
                </a>
			</span>
		</div>
	</div>
    <br/>
    <br/>
    <br/>

<form id="form-dialog-pallets">
	<div id="dialog-pallets" title="Pallets e Data de Envio">
	<fieldset>
		<legend>Informe a qtd. de pallets e data de envio</legend>
	    <table>
	    	<tr>
	    		<td>N&uacute;mero de Pallets:</td>
	    		<td>
	    			<input id="numeroPallets" type="text" style="width:40px; margin-left:10px; text-align:center;" />
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td>Data de Envio:</td>
	    		<td>
	    			<input id="dataEnvio" type="text" style="width:70px; margin-left:10px;" />
	    		</td>
	    	</tr>
	    </table>
	</fieldset>
	</div>
</form>

<div id="dialog-pesq-fornecedor" title="Selecionar Fornecedor">
<fieldset>
	<legend>Selecione um ou mais Fornecedores</legend>
    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
      <option>Dinap</option>
      <option>FC</option>
      <option>Treelog</option>
    </select>

</fieldset>
</div>
<div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Suspens&atilde;o < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   	    <legend> Consulta Bandeira</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		    <td width="60">Data Emiss&atilde;o:</td>
		    <td width="178">
		    	<input type="text" maxlength="6" name="emissaoBandeiras-dataEmissao" id="emissaoBandeiras-dataEmissao" />
		    </td>
		    <td width="52">Fornecedor:</td>
		    <td width="200" colspan="2">
		    	<select id="fornecedor" style="width:100px;">
		    		<c:forEach items="${fornecedores}" var="fornecedor">
		    			<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
		    		</c:forEach>
		    	</select>
		    </td>
		    
		  </tr>
		  <tr>
		    <td width="60">Numero da Nota De:</td>
		    <td width="178" colspan="2">
		    	<input type="text" maxlength="6" name="emissaoBandeiras-numero-nota-de" id="emissaoBandeiras-numero-nota-de" />
		    	 At&eacute;
		    	<input type="text" maxlength="6" name="emissaoBandeiras-numero-nota-ate" id="emissaoBandeiras-numero-nota-ate" />
		    </td>
		    
		    <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="emissaoBandeiraController.pesquisar();">Pesquisar</a></span></td>
		  </tr>
  		</table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
       <fieldset class="classFieldset">
       	  <legend> Bandeiras Pesquisadas</legend>
        
		  <table class="bandeirasRcltoGrid"></table>

       </fieldset>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
   
</div>
</body>
	
	<script type="text/javascript">
		$(function(){
			emissaoBandeiraController.init();
		});
		
	</script>
