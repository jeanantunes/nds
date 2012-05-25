<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<link rel="stylesheet" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.highlight.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.accordion.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.dialog.js"></script>

<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">
	function popup_detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:400,
			width:800,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	};
</script>
<style type="text/css">
#dialog-detalhes fieldset{width:750px!important;}
</style>
</head>

<body>
	<div id="dialog-detalhes" title="Detalhes do Produto">
     <fieldset>
     	<legend>Produto: 4455 - Veja - Edição 001 - Tipo de Lançamento: Parcial</legend>
        
        <table class="detalhesVendaGrid"></table>
         <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
     </fieldset>
   
    </div>

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Parcial < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Produto</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="46">Código:</td>
              <td colspan="3"><input type="text" name="textfield5" id="textfield5" style="width:80px;"/></td>
              <td width="51">Produto:</td>
              <td width="164"><input type="text" name="publica" id="publica" style="width:150px;"/></td>
              <td width="45">Edição:</td>
              <td width="95"><input type="text" name="edicoes" id="edicoes" style="width:80px;"/></td>
              <td width="76">Fornecedor:</td>
              <td width="239"><select name="select" id="select" style="width:200px;">
                <option>Todos</option>
                <option>Dinap</option>
                <option>FC</option>
              </select></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
      <fieldset class="classFieldset">
       	  <legend>Produto: 4455 - Veja - Tipo de Lançamento: Normal</legend>
        
        	
        	<table class="parciaisGrid"></table>
            <!--<span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>-->
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
        

           
           
      </fieldset>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
	$(".parciaisGrid").flexigrid({
			url : '../xml/venda_produto-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Edição',
				name : 'edicao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Lançamento',
				name : 'dtLancto',
				width : 100,
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
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percVenda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'vendaDinheiro',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "edicao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
		
		$(".detalhesVendaGrid").flexigrid({
			url : '../xml/parciais-pop-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Período',
				name : 'periodo',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Lançamento',
				name : 'dtLancamento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dtRecolhimento',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Vendas',
				name : 'venda',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Acumulada',
				name : 'vendaAcumulada',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percVenda',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 750,
			height : 200
		});
</script>
</body>
</html>
