<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<script type="text/javascript" src="scripts/fechamentoCEIntegracao.js"></script>
<script language="javascript" type="text/javascript">
function detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:440,
			width:650,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					
				},
			}
		});
	};
	
	function callback() {
		setTimeout(function() {
			$( "#effect:visible").removeAttr( "style" ).fadeOut();

		}, 1000 );
	};	

	function mostrar(){
		$(".grids").show();
	}	

	$(function() {
		fechamentoCEIntegracaoController.init();		
	});
	
	function confirmar(){
		$(".dados").show();
	}
	function pesqEncalhe(){
		$(".dadosFiltro").show();
	}
</script>
<style type="text/css">
  .box_field{width:200px;}
</style>
</head>

<body>

<div id="dialog-detalhes" title="Detalhes do Encalhe">
	<fieldset style="width:600px;">
    	<legend>Consulta Encalhe</legend>
    	<table width="423" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td width="91"><strong>Data Operação:</strong></td>
            <td width="168">10/06/2012</td>
            <td width="42"><strong>Código:</strong></td>
            <td width="96">43344</td>
          </tr>
          <tr>
            <td><strong>Produto:</strong></td>
            <td>Superinteressante</td>
            <td><strong>Edição:</strong></td>
            <td>4444</td>
          </tr>
        </table>
     </fieldset>
     <div class="linha_separa_fields" style="width:550px!important;">&nbsp;</div>
     <fieldset style="width:600px;">
        
		<table class="detalhesGrid"></table>
    </fieldset>
</div>


<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Suspensão < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
		<legend> Pesquisar Fechamento  CE</legend>
 	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="72">Fornecedor:</td>    
				  	<td colspan="3">
						<select id="idFornecedor" name="idFornecedor" style="width:170px;">
							<option value="-1"  selected="selected">Todos</option>
							<c:forEach items="${listaFornecedores}" var="fornecedor">
								<option value="${fornecedor.key}">${fornecedor.value}</option>	
							</c:forEach>
						</select>
					</td>
				  	<td width="52">
				  		Semana:
				  	</td>
				  	<td width="486">
				  		<input type="text" id="semana" name="semana" />
				  	</td>
				  	<td width="104">
				  		<span class="bt_pesquisar">
				  		<a href="javascript:;" onclick="mostrar();">Pesquisar</a></span>
				  	</td>
				</tr>
			</table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend> Fechamento CE</legend>
          
        <div class="grids" style="display:none;">
          <table class="fechamentoCeGrid"></table>
          
          <div class="linha_separa_fields">&nbsp;</div>
          
          
<br clear="all" />


<table width="950" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="472" valign="top">
      <span class="bt_novos" title="Fechamento"><a href="javascript:;"><img src="../images/ico_check.gif" hspace="5" border="0" />Fechamento</a></span>
      
      <span class="bt_novos" title="Reabertura"><a href="javascript:;"><img src="../images/ico_expedicao_box.gif" hspace="5" border="0" />Reabertura</a></span>
      
      <!--<span class="bt_novos" title="Resumo CE"><a href="resumo_ce.htm" target="_blank"><img src="../images/bt_expedicao.png" hspace="5" border="0" />Resumo CE</a></span>-->
      
      
      <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
      <br clear="all" /><br />


      <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <span class="bt_novos" title="Imprimir Boleto"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Boleto</a></span>
       
       <span class="bt_novos" title="Imprimir Boleto"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Boleto em Branco</a></span>
    </td>
    <td width="88" valign="top"><strong>Total Bruto R$:</strong></td>
    <td width="50" valign="top">3.014,00</td>
    <td width="106" valign="top"><strong>Total Desconto R$:</strong></td>
    <td width="49" valign="top">753,99</td>
    <td width="93" valign="top"><strong>Total Líquido R$:</strong></td>
    <td width="70" valign="top">2.260,00</td>
  </tr>
</table>

</div>
		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div> 
<script>

	$(".detalhesGrid").flexigrid({
			url : '../xml/detalhes_chamada-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Observação',
				name : 'obs',
				width : 330,
				sortable : true,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 180
		});

	
</script>
</body>
</html>
