<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/emissaoBandeira.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>


<style type="text/css">
  #dialog-pesq-fornecedor fieldset, #dialog-pesq-editor fieldset {width:450px!important;}
  .box_field{width:200px;}
  fieldset label {width: auto; margin-bottom: 0px!important;
}
	.forncedoresSel{display:none;}
	.editorSel{display:none;}
	#dialog-pesq-fornecedor, #dialog-pesq-editor, #dialog-pallets{display:none;}
.selFornecedores ul, .selEditores ul{ margin:0px; padding:0px;}
.selFornecedores li, .selEditores li{display:inline; list-style:none;}
  </style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">


<div id="dialog-pallets" title="Pallets">
<fieldset>
	<legend>Informe a quantidade de pallets</legend>
    N&uacute;mero de Pallets: <input id="numeroPallets" type="text" style="width:40px; margin-left:10px; text-align:center;" />

</fieldset>
</div>



<div id="dialog-pesq-editor" title="Selecionar Editor">
<fieldset>
	<legend>Selecione um ou mais Editores</legend>
    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
      <option>Abril</option>
      <option>Globo</option>
    </select>

</fieldset>
</div>
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
<div class="corpo">

   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Suspens&atilde;o < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Consulta  Bandeira</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="52">Semana:</td>
    <td width="778"><input type="text" id="senama" style="width:100px;" /></td>
    <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="emissaoBandeiraController.pesquisar();">Pesquisar</a></span></td>
    </tr>
  </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:noneA;">
       <fieldset class="classFieldset">
       	  <legend> Bandeiras Pesquisadas</legend>
        
		  <table class="bandeirasRcltoGrid"></table>
          <span class="bt_novos" title="Imprimir Bandeira"><a href="javascript:;" onclick="emissaoBandeiraController.imprimirBandeira();" rel="bandeira"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Bandeiras</a></span>
<!--imprimir_bandeira.htm target="_blank"-->

		<span class="bt_novos" title="Imprimir Relat&oacute;rio"><a href="javascript:;" onclick="emissaoBandeiraController.imprimirArquivo('PDF')" target="_blank"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Relat&oacute;rio</a></span>
        
        <span class="bt_novos" title="Bandeira Manual"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0" />Bandeira Manual</a></span>
     
         


		
       </fieldset>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>

        

    
    </div>
</div> 
</form>
</body>
	
	<script type="text/javascript">
		$(function(){
			emissaoBandeiraController.init();
		});
		
	</script>
