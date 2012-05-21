<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script language="javascript" type="text/javascript">


$(function() {
	$(".dadosDividaGrid").flexigrid({
		preProcess: getDataFromResultDividas,
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigo',
			width : 60,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nome',
			width : 440,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data Emissão',
			name : 'dataEmissao',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data Vencimento',
			name : 'dataVencimento',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Valor Divida R$',
			name : 'valor',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Detalhes',
			name : 'acao',
			width : 50,
			sortable : false,
			align : 'center',
		}, {
			display : '',
			name : 'check',
			width : 20,
			sortable : false,
			align : 'center',
		}],
		sortname : "Nome",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});
});	






cont = 0;
function selecionarTodos(){
	for (var i=0;i<document.formularioListaDividas.elements.length;i++) {
	     var x = document.formularioListaDividas.elements[i];
	     if (x.name == 'checkbox') {
	         x.checked = document.formularioListaDividas.selTodos.checked;
	     }    
	}
	
	if (cont == 0){   
		var elem = document.getElementById("textoSelTodos");
		elem.innerHTML = "Desmarcar todos";
		cont = 1;
	} 
	
	else {
		var elem = document.getElementById("textoSelTodos");
		elem.innerHTML = "Marcar todos";
		cont = 0;
	}
}






function mostrarGridConsulta() {
	
	/*PASSAGEM DE PARAMETROS*/
	$(".dadosDividaGrid").flexOptions({
		/*METODO QUE RECEBERA OS PARAMETROS*/
		url: "<c:url value='/financeiro/divida/buscaDividas' />",
		params: [
		         {name:'numCota', value:$("#numCota").val()},
		         {name:'dataVencimento', value:$("#filtroDataVencimento").val()}
		        ] ,
		        newp: 1
	});
	
	/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
	$(".dadosDividaGrid").flexReload();
	
	$(".grids").show();
}	


function getDataFromResultDividas(resultado) {
	
	//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
	if (resultado.mensagens) {
		exibirMensagemDialog(
			resultado.mensagens.tipoMensagem, 
			resultado.mensagens.listaMensagens
		);
		$(".grids").hide();
		return resultado;
	}	
	
	$.each(resultado.rows, function(index, row) {
		
		var detalhes = '<a href="javascript:;" onclick="popup_detalhes(' + row.cell.codigo + ');" style="cursor:pointer">' +
				 	   '<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0px" />' +
					   '</a>';	
					   
		var checkBox = '<input type="checkbox" name="checkbox" />';	
		
		row.cell.acao = detalhes;
	    row.cell.check = checkBox;
	});
	
	$(".grids").show();
	
	return resultado;
}












function popup_baixa() {
	
	//obterDadosBaixaDividas(codigos das dividas marcadas);
	
	$( "#dialog-baixa" ).dialog({
		resizable: false,
		height:370,
		width:450,
		modal: true,
		buttons: {
			"Confirmar": function() {
				popup_confirma();
				$( this ).dialog( "close" );
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};



function popup_detalhes(codigo) {
	
	//obterDetalhesDivida(codigo);
	
	$( "#dialog-detalhes" ).dialog({
		resizable: false,
		height:370,
		width:450,
		modal: true,
		buttons: {
			"Fechar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};



function popup_confirma() {
	$( "#dialog-confirma" ).dialog({
		resizable: false,
		height:130,
		width:470,
		modal: true,
		buttons: {
			"Confirmar": function() {
				
				//baixarDividas();
				
				$( this ).dialog( "close" );
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};



$(function() {
	$( "#filtroDataVencimento" ).datepicker({
		showOn: "button",
		buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true
	});
});


</script>

<style>
    #dialog-confirma,#dialog-baixa,#dialog-detalhes{display:none;}
</style>

</head>


<div id="dialog-confirma" title="Baixa Manual de Divida">
	<p>Confirma a Baixa desta Divida?</p>
</div>


<div id="dialog-baixa" title="Baixa de Divida Manualmente">
     
	<table width="430" border="0" cellpadding="2" cellspacing="1">
	  <tr>
	    <td><strong>Cota:</strong></td>
	    <td colspan="5">9999 - José da Silva Pereira</td>
	  </tr>
	  <tr>
	    <td width="50"><strong>Emissão:</strong></td>
	    <td width="97">12/11/2011</td>
	  </tr>
	  <tr>  
	    <td width="75"><strong>Vencimento:</strong></td>
	    <td width="113">12/11/2011</td>
	  </tr>
	  <tr>  
	    <td width="74"><strong>Valor R$:</strong></td>
	    <td width="80">999.999,99</td>
	  </tr>
	</table>
	
	<br />
	
	<table width="430" border="0" cellpadding="2" cellspacing="1">
	  <tr>
	    <td width="122" align="center"><strong>Data Pagamento</strong></td>
	    <td width="8" align="center"><strong>&nbsp;&nbsp;</strong></td>
	  </tr>
	  <tr>  
	    <td width="137" align="center"><strong>Valor Desconto R$</strong></td>
	    <td width="11" align="center"><strong>&nbsp;&nbsp;</strong></td>
	  </tr>
	  <tr>    
	    <td width="126" align="center"><strong>Valor Juros R$</strong></td>
	    <td width="11" align="center"><strong>&nbsp;&nbsp;</strong></td>
	  </tr>
	  <tr>
	    <td align="center"><input type="text" name="datepickerDe1" id="datepickerAte" style="width:80px;" /></td>
	    <td align="center">&nbsp;</td>
	  </tr>
	  <tr>   
	    <td align="center"><input name="textfield9" type="text" id="textfield9" style="width:80px; text-align:right;" value="0,00" /></td>
	    <td align="center">&nbsp;</td>
	  </tr>
	  <tr>   
	    <td align="center"><input name="textfield10" type="text" id="textfield10" style="width:80px; text-align:right;" value="0,00" /></td>
	    <td align="center">&nbsp;</td>
	  </tr>
	</table>
	
</div>


<div id="dialog-detalhes" title="Detalhes da Dívida">
     
	<table width="430" border="0" cellpadding="2" cellspacing="1">
	  <tr>
	    <td><strong>Cota:</strong></td>
	    <td colspan="5">9999 - José da Silva Pereira</td>
	  </tr>
	  <tr>
	    <td width="50"><strong>Emissão:</strong></td>
	    <td width="97">12/11/2011</td>
	  </tr>
	  <tr> 
	    <td width="75"><strong>Vencimento:</strong></td>
	    <td width="113">12/11/2011</td>
	  </tr>
	  <tr>   
	    <td width="74"><strong>Valor R$:</strong></td>
	    <td width="80">999.999,99</td>
	  </tr>
	</table>
	<br />
</div>

<div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Baixa de Divida < evento > com < status >.</b></p>
	 </div>
    	
     <fieldset class="classFieldset">
   	    <legend> Pesquisar Divida</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
 
              <td width="113">Data Vencimento:</td>
              <td colspan="3">
                  <input type="text" name="filtroDataVencimento" id="filtroDataVencimento" style="width:80px;" />
              </td>
 
              <td width="29">Cota:</td>
              <td width="105">
              	<input name="numCota" 
              		   id="numCota" 
              		   type="text"
              		   maxlength="11"
              		   style="width:80px; 
              		   float:left; margin-right:5px;"
              		   onchange="cota.pesquisarPorNumeroCota('#numCota', '#descricaoCota');" />
			  </td>
				
			  <td>
			      <input name="descricaoCota" 
			      		 id="descricaoCota" 
			      		 type="text" 
			      		 class="nome_jornaleiro" 
			      		 maxlength="255"
			      		 style="width:130px;"
			      		 onkeyup="cota.autoCompletarPorNome('#descricaoCota');" 
			      		 onblur="cota.pesquisarPorNomeCota('#numCota', '#descricaoCota');" />
			  </td>
              
              <td width="198"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a></span></td>
            
            </tr>
          </table>

     </fieldset>
     
     <div class="linha_separa_fields">&nbsp;</div>
     
     <form name="formularioListaDividas" id="formularioListaDividas">
	
	     <input type="hidden" id="valorTotalHidden" />
			 
	     <fieldset class="classFieldset">
	       	 <legend>Dividas Geradas</legend>
		     
		     <div class="grids" style="display:none;">
		     
		     <table class="dadosDividaGrid"></table>
		     
		     <table width="100%" border="0" cellspacing="2" cellpadding="2">
	            <tr>
	                <td width="20%"><span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
	                <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
	                </td>
	                <td width="20%">   
	                    <span class="bt_confirmar_novo" title="Pagar Boleto"><a onclick="popup_baixa();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Pagar</a></span>
	                    <span class="bt_confirmar_novo" title="Negociar Dívida"><a href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>
	                </td>
	                
	                <td width="15%">
	                    <strong>Total Selecionado R$:</strong>
	                </td>
	                <td width="7%">
	                    10.567,00
	                </td>
	                
	                <td width="7%">
	                    <strong>Total R$:</strong>
	                </td>
	                <td width="7%">
	                    10.567,00
	                </td>
	                
	                <td width="15%">
	                
	                    <span class="checar">
	                        
	                        <label for="textoSelTodos" id="textoSelTodos">
	                            Selecionar Todos
	                        </label>
	                        
	                        <input type="checkbox" id="selTodos" name="selTodos" onclick="selecionarTodos();" style="float:left;"/>
	                    </span>
	
	                </td>
	            </tr>
	        </table>
		     
	     </fieldset>
	     
     </form>
     
     <div class="linha_separa_fields">&nbsp;</div>

</div> 

