<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/acessoRapido.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">


	function mostrar(){
	$(".grids").show();
}	
$(function() {
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAte" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerMovDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerMovAte" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
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
		};

$(document).ready(function(){	
	$("#selFornecedor").click(function() {
		$(".menu_fornecedor").show().fadeIn("fast");
	})

	$(".menu_fornecedor").mouseleave(function() {
		$(".menu_fornecedor").hide();
	});
	
})

$(document).ready(function(){	
	$("#selProdutos").click(function() {
		$(".menu_produtos").show().fadeIn("fast");
	})

	$(".menu_produtos").mouseleave(function() {
		$(".menu_produtos").hide();
	});
	
})



</script>

</head>

<body>
<div class="areaBts">
	<div class="area">
		 <span class="bt_arq"><a href="javascript:;" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
         <span class="bt_arq"><a href="../nota_envio.html" target="_blank" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>
	</div>
</div>
<div class="linha_separa_fields">&nbsp;</div>
 <fieldset class="fieldFiltro">
   <legend> Pesquisar NF-e</legend>
   <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="82">Tipo de Nota:</td>
    <td width="209"><select name="select3" id="select3" style="width:200px; font-size:11px!important">
      <option>NFe de Remessa em Consignação</option>
      <option>NFe de Remessa para Distribuição</option>
      <option>NFe de venda</option>
      <option>Nota de Envio</option>
      <option>NECA - Nota de Envio com Chave de Acesso</option>
    </select></td>
    <td width="97">Data Movimento:</td>
    <td width="238"><input name="datepickerMovDe" type="text" id="datepickerMovDe" style="width:76px;"/>
&nbsp;&nbsp;Até&nbsp;
<input name="datepickerMovAte" type="text" id="datepickerMovAte" style="width:76px;"/></td>
    <td width="83">Data Emissão:</td>
    <td width="210"><input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;"/></td>
  </tr>
  <tr>
    <td>Roteiro:</td>
    <td><select name="select" id="select" style="width:200px; font-size:11px!important">
      <option>Selecione...</option>
    </select></td>
    <td>Rota:</td>
    <td><select name="select2" id="select2" style="width:200px; font-size:11px!important">
      <option>Selecione...</option>
    </select></td>
    <td>Tipo Emissão:</td>
    <td><select name="select5" id="select5" style="width:210px; font-size:11px!important">
      <option selected="selected">Selecione...</option>
      <option>Normal</option>
      <option>Emissão Contingência sem conexão</option>
      <option>Emissão Contingência SCAN</option>
      <option>Emissão Contingência Prévia</option>
      <option>Emissão Contingência Auxiliar FS-DA </option>
    </select></td>
    </tr>
  <tr>
    <td>Cota de:</td>
    <td><input type="text" style="width:80px;"/>
&nbsp;Até&nbsp;
<input type="text" style="width:80px;"/></td>
    <td>Intervalo Box:</td>
    <td><input type="text" style="width:76px;"/>
&nbsp;Até &nbsp;
<input type="text" style="width:76px;"/></td>
    <td>Fornecedor:</td>
    <td><a href="#" id="selFornecedor">Clique e Selecione o Fornecedor</a>
      <div class="menu_fornecedor" style="display:none;"> <span class="bt_sellAll">
        <input type="checkbox" id="sel" name="Todos1" onclick="checkAll_fornecedor();" style="float:left;"/>
        <label for="sel">Selecionar Todos</label>
        </span> <br clear="all" />
        <input id="dinap" name="checkgroup_menu" onclick="verifyCheck_1()" type="checkbox"/>
        <label for="dinap">Dinap</label>
        <br clear="all" />
        <input name="checkgroup_menu" onclick="verifyCheck_1()" id="fc" type="checkbox"/>
        <label for="fc">FC</label>
      </div></td>
    </tr>
  <tr>
    <td>Produtos:</td>
    <td><a href="#" id="selProdutos">Clique e Selecione os Produtos</a>
      <div class="menu_produtos" style="display:none;"> <span class="bt_sellAll">
        <input type="checkbox" id="sel" name="Todos1" onclick="checkAll_produtos();" style="float:left;"/>
        <label for="sel">Selecionar Todos</label>
        </span> <br clear="all" />
        <input id="cacao" name="checkgroup_menu" onclick="verifyCheck_1()" type="checkbox"/>
        <label for="cacao">Cascão</label>
        <br clear="all" />
        <input name="checkgroup_menu" onclick="verifyCheck_1()" id="veja" type="checkbox"/>
        <label for="veja">Veja</label>
      </div></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
  </tr>
  <td colspan="3">
    </td>
  </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="fieldGrid">
       	  <legend>Impressão  NF-e</legend>
        <div class="grids" style="display:none;">
		  <table class="impressaoGrid"></table>
         
        <span class="bt_sellAll" style="float:right;" id="btSel"><label for="sel" style="margin-top:-5px;">Selecionar Todos</label><input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:left; margin-right:30px;"/></span>
		</div>
		
      </fieldset>

<script>
	$(".impressaoGrid").flexigrid({
			url : '../xml/impressao_nfe-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 465,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'vlrTotal',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. R$',
				name : 'totalDesc',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Impressão',
				name : 'impressao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : ' ',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});
		
		
		$(".cotasSuspensasGrid").flexigrid({
			url : '../xml/gerar_nfe_2-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 570,
			height : 180
		});
</script>

</body>
</html>
