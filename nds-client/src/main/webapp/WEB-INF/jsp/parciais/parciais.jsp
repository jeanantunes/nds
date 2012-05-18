<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parciais.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">

var PARCIAIS = new Parciais('${pageContext.request.contextPath}');

function popup() {
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:200,
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
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
			height:200,
			width:350,
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
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
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
	function popup_detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:550,
			width:820,
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
	
	function popup_edit_produto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-edit-produto" ).dialog({
			resizable: false,
			height:360,
			width:500,
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
 
$(function() {
		$( "#datepicker_1" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepicker_2" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataInicial" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataFinal" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
$(function() {
	$("#nomeProduto").autocomplete({source: ""});
});

		
</script>
<style type="text/css">
#dialog-detalhes fieldset{width:750px!important;}
#dialog-detalhes fieldset ul{}
#dialog-detalhes fieldset li{float:left; margin-right:10px; margin-left:0px; margin-bottom:5px; line-height:20px;}
#dialog-novo fieldset{width:250px!important;}
#dialog-edit-produto{display:none;}
</style>


<div id="dialog-edit-produto" title="Dados do Produto">
        <table width="412" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="129">Código:</td>
              <td width="272"><input name="textfield2" type="text" id="textfield2" style="width:80px;" value="3456789" disabled="disabled" /></td>
            </tr>
            <tr>
              <td>Produto:</td>
              <td><input name="textfield7" type="text" id="textfield7" style="width:250px;" value="Mônica" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Edição:</td>
              <td><input name="textfield" type="text" id="textfield" style="width:80px;" value="4556" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Preço Capa R$:</td>
              <td><input name="textfield3" type="text" id="textfield3" style="width:80px; text-align:right" value="4,85" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Fornecedor:</td>
              <td><input name="textfield4" type="text" id="textfield4" style="width:250px;" value="Dinap" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Data Lançamento:</td>
              <td><input type="text" id="datepicker_1" style="width:80px;" /></td>
            </tr>
            <tr>
              <td>Data Recolhimento:</td>
              <td><input type="text" id="datepicker_2" style="width:80px;" /></td>
            </tr>
          </table>
</div>

<div id="dialog-excluir" title="Excluir Parcial">
  <p>Confirma a exclusão desta Parcial?</p>
</div>

<div id="dialog-detalhes" title="Parcial">
<fieldset>
	<legend>Dados da Parcial</legend>
    <table width="740" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="81"><strong>Código:</strong></td>
              <td width="94">44433</td>
              <td width="132"><strong>Produto:</strong></td>
              <td width="194"> Turma da Mônica</td>
              <td width="81"><strong>Edição:</strong></td>
              <td width="127">4345</td>
            </tr>
            <tr>
              <td><strong>Fornecedor:</strong></td>
              <td>FC</td>
              <td><strong>Data Lançamento:</strong></td>
              <td>12/02/2012</td>
              <td><strong>Data Final:</strong></td>
              <td>10/04/2012</td>
            </tr>
          </table>
          
</fieldset>
<br />
<br clear="all" />
<br />

<fieldset>
	<legend>Parciais</legend>
    <table class="parciaispopGrid"></table>
</fieldset>


<br />
<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
 <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" alt="Incluir Períodos" />Incluir Períodos</a></span>


</div>



<div id="dialog-novo" title="Nova Parcial">
     <fieldset>
     	<legend>Novo Período</legend>
        <table width="236" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="103">PEB:</td>
            <td width="126"><input name="" type="text" style="width:80px;" /> dias</td>
          </tr>
          <tr>
            <td>Qtde. Períodos:</td>
            <td><input name="" type="text" style="width:80px;" /></td>
          </tr>
        </table>
        
     </fieldset>
   
 </div>


<div class="corpo">
 
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Parcial < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Parciais</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="46">Código:</td>
              <td colspan="3">
<!-- Código -->
<input id="codigoProduto" name="codigoProduto" style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#nomeProduto', null , false);" />
				</td>
                <td width="51">Produto:</td>
                <td width="163">
                
<!-- Nome Produto -->                
<input id="nomeProduto" type="text" name="nomeProduto"  style="width: 150px;" maxlength="255"
					       onkeyup="produto.autoCompletarPorNomeProduto('#nomeProduto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#nomeProduto', null, false);"/>
					    	   
				</td>
                <td width="56">Edição:</td>
                <td width="148">

<!-- Numero Edição -->                
<input id="edicaoProduto"  type="text" name="edicoes" style="width:80px;"/></td>

              <td width="67">Fornecedor:</td>
              <td colspan="2">
       
<!-- Fornecedores -->
<select id="idFornecedor" name="idFornecedor" style="width:200px;">
    <option value="-1"  selected="selected">Todos</option>
    <c:forEach items="${listaFornecedores}" var="fornecedor">
      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
    </c:forEach>
</select>
       
       		</td>
            </tr>
            <tr>
              <td>Período:</td>
              <td colspan="3">

<!-- Data de -->              
<input id="dataInicial" type="text" name="dataInicial" style="width:80px;"/></td>

              <td>Até:</td>
              <td>

<!-- Data até -->
<input id="dataFinal" type="text" name="dataFinal" style="width:80px;"/></td>

              <td>Status:</td>
              <td>
              
<!-- Status -->              
<select id="status" name="select2" style="width:140px;">
  <option selected="selected" value="">Todos</option>
   <c:forEach items="${listaStatus}" var="status">
      		<option value="${status.key}">${status.value}</option>	
    </c:forEach>
</select>
				</td>
              <td>&nbsp;</td>
              <td width="137">&nbsp;</td>
              <td width="119"><span class="bt_pesquisar">
              
<!-- Pesquisar -->
<a href="javascript:;" onclick=" PARCIAIS.cliquePesquisar();">Pesquisar</a>
		
			</span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

<!-- PESQUISA DE LANCAMENTOS PARCIAIS -->
<fieldset id="painelLancamentos" class="classFieldset" style="display:block">
	       	  <legend>Parciais Cadastradas</legend>
	       
	        	
	        	<table class="parciaisGrid"></table>
	            
	<div id="exportacao">
	            
	            <span class="bt_novos" title="Gerar Arquivo">
	            
	<!-- ARQUIVO EXCEL -->
	<a href="${pageContext.request.contextPath}/parciais/exportar?fileType=XLS">
			
			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
			</span>
	
			<span class="bt_novos" title="Imprimir">
	
		
	<!-- IMPRIMIR PDF -->	
	<a href="${pageContext.request.contextPath}/parciais/exportar?fileType=PDF">
	
			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
			</span>
	        
	</div>           
           
</fieldset>


<!-- PESQUISA DE PERIODOS PARCIAIS -->
<fieldset id="painelPeriodos" class="classFieldset" style="display:none">
	       	  <legend>Períodos Cadastrados</legend>
	               	
	        	<table align="center" class="periodosGrid"></table>
	            
	<div id="exportacaoPeriodos">
	            
	            <span class="bt_novos" title="Gerar Arquivo">
	            
	<!-- ARQUIVO EXCEL -->
	<a href="${pageContext.request.contextPath}/parciais/exportarPeriodos?fileType=XLS">
			
			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
			</span>
	
			<span class="bt_novos" title="Imprimir">
	
		
	<!-- IMPRIMIR PDF -->	
	<a href="${pageContext.request.contextPath}/parciais/exportarPeriodos?fileType=PDF">
	
			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
			</span>
	        
	</div>           
	           
</fieldset>

      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>

$(function() {	
	
	$(".parciaisGrid").flexigrid($.extend({},{
		colModel : [ {
			display : 'Data Lancto',
			name : 'dataLancamento',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data Recolhimento',
			name : 'dataRecolhimento',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Código',
			name : 'codigoProduto',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numEdicao',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Fornecedor',
			name : 'nomeFornecedor',
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status',
			name : 'statusParcial',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 80,
			sortable : false,
			align : 'center'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
})); 	

$(".grids").show();	


$(".periodosGrid").flexigrid($.extend({},{
	colModel : [ {
			display : 'Período',
			name : 'periodo',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data Lançamento',
			name : 'dataLancamento',
			width : 130,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data Recolhimento',
			name : 'dataRecolhimento',
			width : 130,
			sortable : true,
			align : 'center'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Encalhe',
			name : 'encalhe',
			width : 80,
			sortable : true,
			align : 'center'
		}, {
			display : 'Vendas',
			name : 'vendas',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Venda Acumulada',
			name : 'vendaAcumulada',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : '% Venda',
			name : 'percVenda',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 75,
			sortable : false,
			align : 'center'
		}],
		sortname : "periodo",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	})); 

$(".parciaispopGrid").flexigrid($.extend({},{
	colModel : [ {
			display : 'Período',
			name : 'periodo',
			width : 40,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data Lançamento',
			name : 'dtLancamento',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data Recolhimento',
			name : 'dtRecolhimento',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 45,
			sortable : true,
			align : 'center'
		}, {
			display : 'Encalhe',
			name : 'encalhe',
			width : 45,
			sortable : true,
			align : 'center'
		}, {
			display : 'Vendas',
			name : 'venda',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Venda Acumulada',
			name : 'vendaAcumulada',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : '% Venda',
			name : 'percVenda',
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 65,
			sortable : false,
			align : 'center'
		}],
		sortname : "periodo",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 750,
		height : 200
	})); 

});
</script>
