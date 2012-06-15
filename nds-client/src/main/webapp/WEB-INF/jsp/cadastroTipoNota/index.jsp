<head>
<title>Cadastro de Tipos de Notas</title>
<script language="javascript" type="text/javascript">
function pesquisar() {

	var cfop = $("#cfop").val();
	var tipoNota = $("#tipoNota").val();

	$(".tiposNotasGrid").flexOptions({
		url: "<c:url value='/administracao/cadastroTipoNota/pesquisar' />",
		params: [
	         {name:'cfop', value: cfop},
	         {name:'tipoNota', value: tipoNota}
	    ],
	    newp: 1,
	});
	
	$(".tiposNotasGrid").flexReload();
}

function popup() {
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:230,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
};
	
function popup_alterar() {
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:230,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$( "#abaPdv" ).show( );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
};
	
function popup_excluir() {
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
};
</script>
<style type="text/css">
	#dialog-box{display:none;}
	#dialog-box fieldset{width:570px!important;}
</style>
</head>

<body>
	<div id="dialog-excluir" title="Excluir Tipo de Nota">
		<p>Confirma a exclusão deste Tipo de Nota?</p>
	</div>
	<div id="dialog-novo" title="Incluir Novo Tipo de Nota">    
    	<table width="300" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="68">Código:</td>
              <td width="221"><input type="text" name="textfield22" id="textfield22" style="width:80px;" readonly="readonly" /></td>
            </tr>
            <tr>
              <td>Nome:</td>
              <td><input type="text" name="textfield23" id="textfield23" style="width:200px;"/></td>
            </tr>
            <tr>
              <td>CFOP:</td>
              <td><input type="text" name="textfield" id="textfield" style="width:200px;"/></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
            </tr>
        </table>
    </div>
	<div class="corpo">
    	<div class="container">
      		<fieldset class="classFieldset">
   	    		<legend> Pesquisar Tipo de Nota</legend>
        		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            		<tr>
              			<td width="47">CFOP:</td>
                		<td width="114"><input type="text" name="cfop" id="cfop" style="width:80px;"/></td>
		                <td width="81">Tipo de Nota:</td>
		                <td width="576"><input type="text" name="tipoNota" id="tipoNota" style="width:200px;"/></td>
		              	<td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
            		</tr>
          		</table>
      		</fieldset>
      		<div class="linha_separa_fields">&nbsp;</div>
	      	<fieldset class="classFieldset">
	       	  	<legend>Tipos de Notas Cadastradas</legend>
	        	<div class="grids" style="display:none;">
	        		<table class="tiposNotasGrid"></table>
	            	<div class="linha_separa_fields">&nbsp;</div>
	       			<span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/administracao/cadastroTipoNota/exportar?fileType=XLS"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
	             	<span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/administracao/cadastroTipoNota/exportar?fileType=PDF"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
	        	</div>
	      	</fieldset>
    	</div>
	</div> 
	<script>
		$(".tiposNotasGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'Tipo de Nota',
					name : 'nopDescricao',
					width : 600,
					sortable : true,
					align : 'left'
				},{
					display : 'CFOP Dentro UF',
					name : 'cfopEstado',
					width : 150,
					sortable : true,
					align : 'left'
				},{
					display : 'CFOP Fora UF',
					name : 'cfopOutrosEstados',
					width : 150,
					sortable : true,
					align : 'left'
				}],
				sortname : "nopDescricao",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
		});

		function executarPreProcessamento(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			mostrar();
			return resultado.tableModel;
		}
		
	</script>
</body>
</html>
