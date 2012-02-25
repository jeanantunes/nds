<head>
<script language="javascript" type="text/javascript">

//funcoes que implementao o autocomplete
function pesquisarPorCnpjFuncionario() {
	
	
	var cnpj = $("#cnpj").val();
	
	if (cnpj && cnpj.length > 0) {
		
		
		$.postJSON("<c:url value='recebimentoFisico/buscaCnpj'/>",
				   "cnpj=" + cnpj, exibirNomeFornecedor);
		
		
	}
}

function exibirNomeFornecedor(result) {
	$("#fornecedor").val(result.id);
}

//fim das funcoes que implementam autocomplete

$( "#busca" ).puts("Busca produtos por nome"); 
function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:500,
			width:410,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					nform = document.getElementById('dialognota');
					nform.submit();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:430,
			width:410,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_nota() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );		
		$( "#dialog-nota" ).dialog({
			resizable: false,
			height:320,
			width:460,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					$(".grids").show();
					nform = document.getElementById('dialognota');
					nform.submit();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:'auto',
			width:330,
			modal: true,
			buttons: {
				"Confirmar": function() {					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
  //callback function to bring a hidden box back
		function callback() {
			setTimeout(function() {
				$( "#effect:visible").removeAttr( "style" ).fadeOut();

			}, 1000 );
		};	

	function mostrar(){
	$(".grids").show();
}	
$(function() {
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAte" ).datepicker({
			showOn: "button",
			buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerLancto" ).datepicker({
			showOn: "button",
			buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerRecolhimento" ).datepicker({
			showOn: "button",
			buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerPrevisto" ).datepicker({
			showOn: "button",
			buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerMatrizDistrib" ).datepicker({
			showOn: "button",
			buttonImage: "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	function confirmar(){
		$(".dados").show();
	}
	function pesqEncalhe(){
		$(".dadosFiltro").show();
		
	}
	
	
	

	function mostrar_nfes(){

		$(".nfes").show();
		$(".bt_1").hide();

	};
</script>

<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
.nfes{display:none;}
  </style>
</head>

<body>
<div id="dialog-excluir" title="Recebimento Físico">
	<p>Confirma este Recebimento?</p>
</div>


<div id="dialog-nota" title="Recebimento Físico">
	<form id="dialognota" action="<c:url value ="/recebimentoFisico/inserirNota"/>" method="post">
	    <table width="439" cellpadding="2" cellspacing="2" style="text-align:left;">
	    <tr>
	        <td width="127">Emissão:</td>
	        <td width="296"><input type="text" name="notaFiscalFornecedor.dataEmissao" style="width:80px " id="datepickerDe" /></td>
	    </tr>
	    <tr>
	      <td>Entrada:</td>
	      <td><input type="text" name="recebimentoFisico.data" value="${dataAtual}" style="width:80px" id="datepickerAte" /></td>
	    </tr>
	    <tr>
	      <td>Valor Bruto R$:</td>
	      <td><input type="text" name="notaFiscalFornecedor.valorBruto" style="width:80px " /></td>
	    </tr>
	    <tr>
	      <td>Valor Líquido R$:</td>
	      <td><input type="text" name="notaFiscalFornecedor.valorLiquido" style="width:80px " /></td>
	    </tr>
	    <tr>
	      <td>Valor Desconto R$:</td>
	      <td><input type="text" name="notaFiscalFornecedor.valorDesconto" style="width:80px " /></td>
	    </tr>
	    <tr>
	      <td>CFOP:</td>
	      <td><input type="text" name="notaFiscalFornecedor.cfop" style="width:80px " />
	      <input type="text" style="width:190px " /></td>
	    </tr>
	     </table>    
    </form>
    
</div>


<div id="dialog-novo" title="Recebimento Físico">
    <table width="341" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="125">Código:</td>
    <td width="202"><input type="text" name="textfield6" id="textfield6" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar" title="Pesquisar"><a href="javascript:;">&nbsp;</a></span></td>
  </tr>
  <tr>
    <td width="125">Produto:</td>
    <td width="202"><input type="text" name="textfield6" id="textfield6" style="width:200px;"/></td>
  </tr>
  <tr>
    <td>Edição:</td>
    <td><input type="text" name="textfield13" id="textfield13" style="width:80px;" disabled="disabled"/></td>
  </tr>
  <tr>
    <td>Data Lançamento:</td>
    <td><input type="text" name="datepickerLancto" id="datepickerLancto" style="width:80px;"/></td>
  </tr>
  <tr>
    <td>Data Recolhimento:</td>
    <td><input type="text" name="datepickerRecolhimento" id="datepickerRecolhimento" style="width:80px;"/></td>
  </tr>
  <tr>
    <td>Preço R$:</td>
    <td><input type="text" name="textfield" id="textfield" style="width:80px;"/></td>
  </tr>
  <tr>
    <td>Peso:</td>
    <td><input type="text" name="textfield2" id="textfield2" style="width:80px;"/></td>
  </tr>
  <tr>
    <td>Pacote Padrão:</td>
    <td><input type="text" name="textfield3" id="textfield3" style="width:200px;"/></td>
  </tr>
  <tr>
    <td>Reparte Previsto:</td>
    <td><input type="text" name="textfield15" id="textfield15" style="width:80px;"/></td>
  </tr>
  <tr>
    <td>Lançamento:
    <select name="tipoLancamento" id="tipoLancamento" style="width: 250px;">
    		<option value=""></option>
			<c:forEach var="tipoLancamento" items="${listaTipoLancamento}">				
				<option value="${tipoLancamento}">${tipoLancamento}</option>
			</c:forEach>
		</select></td> 
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><span class="bt_incluir_novo" title="Incluir Nova Linha"><a href="javascript:;" onclick="popup();"><img src="images/ico_add.gif" alt="Incluir Novo" width="16" height="16" border="0" hspace="5" />Incluir Novo</a></span></td>
  </tr>
</table>
    
</div>








<div class="corpo">
  
     <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Recebimento Físico < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Recebimento Físico</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
        
        
        
  <tr>
    <td width="86">Fornecedor:</td>
    <td width="254">
    	<select name="fornecedor" id="fornecedor" style="width: 250px;">
    		<option value=""></option>
			<c:forEach var="fornecedor" items="${listafornecedores}">				
				<option value="${fornecedor.juridica.id}">${fornecedor.juridica.nomeFantasia}</option>
			</c:forEach>
		</select>
	</td>
    <td width="43" align="right">CNPJ:</td>
    <td width="136"><input id="cnpj" onblur="pesquisarPorCnpjFuncionario();" name="cnpj" style="width:130px;"/></td>
    <td width="76">Nota Fiscal:</td>
    <td width="123"><input type="text" style="width:100px;" onblur="popup_nota();"/></td>
    <td width="33">Série:</td>
    <td width="43"><input type="text" style="width:30px;"/></td>
    <td width="110"><span class="bt_pesquisar" title="Pesquisar Recebimento"><a href="<c:url value="/recebimentoFisico/pesquisa"/>" onclick="mostrar();">Pesquisar</a></span></td>
  </tr>
  <tr>
    <td height="26"><label for="eNF">É uma NF-e?</label></td>
    <td colspan="5">
      <input type="checkbox" name="checkbox8" id="eNF" onchange="mostrar_nfes();" style="float:left; margin-right:10px;" />
     <span class="nfes"> Chave de Acesso:
      <input type="text" name="textfield11" id="textfield11" style="width:120px; margin-left:10px;" onblur="popup_nota();" /></span>
     </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td></span></td>
  </tr>
        </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend>Recebimentos  Físico Cadastrados</legend>
        <div class="grids" style="display:none;">
		  <table class="notasFisicoGrid"></table>
            
            <span class="bt_incluir_novo" title="Incluir Nova Linha"><a href="javascript:;" onclick="popup();"><img src="images/ico_add_novo.gif"  border="0" hspace="5" />Novo Produto</a></span>
            
            <span class="bt_novos" title="Salvar"><a href="javascript:;"><img src="images/ico_salvar.gif" width="19" height="17" alt="Salvar"  hspace="5" border="0"/>Salvar</a></span>
            
            <span class="bt_confirmar_novo" title="Confirmar Recebimento Físico"><a href="javascript:;" onclick="popup_excluir();"><img src="images/ico_check.gif" width="16" height="16" alt="Confirmar" border="0" hspace="5" />Confirmar</a></span>
  
		</div>
              
              
            


		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

        

    
    </div>
</div> 
<script>
	$(".notasFisicoGrid").flexigrid({
			url : '../xml/notas_recebimento_fisico-xml.xml',
			dataType : 'xml',
			colModel : [{
				display : 'Código',
				name : 'codigo	',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto	',
				width : 340,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte Previsto',
				name : 'repartePrevisto',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtd. Fisico',
				name : 'qtdeFisico',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			sortname : "produto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
</script>
</body>
</html>
