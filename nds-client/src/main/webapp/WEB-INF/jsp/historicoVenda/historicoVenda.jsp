
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="scripts/flexGridService.js"></script>
<script type="text/javascript" src="scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="scripts/historicoVenda.js" ></script>
<script language="javascript" type="text/javascript">

$(function() {
	historicoVendaController.init();
});

function filtroReparte(){
	$('.filtroQtdeReparte').show();
	$('.filtroQtdeVenda').hide();
	$('.filtroComponentes').hide();
	$('.filtroCotas').hide();
	$('.filtroPercVenda').hide();
	}
function filtroVenda(){
	$('.filtroQtdeReparte').hide();
	$('.filtroQtdeVenda').show();
	$('.filtroComponentes').hide();
	$('.filtroCotas').hide();
	$('.filtroPercVenda').hide();
	}
function filtroComponentes(){
	$('.filtroQtdeReparte').hide();
	$('.filtroQtdeVenda').hide();
	$('.filtroComponentes').show();
	$('.filtroCotas').hide();
	$('.filtroPercVenda').hide();
	}
function filtroCotas(){
	$('.filtroQtdeReparte').hide();
	$('.filtroQtdeVenda').hide();
	$('.filtroComponentes').hide();
	$('.filtroCotas').show();
	$('.filtroPercVenda').hide();
	}
function filtroPercVenda(){
	$('.filtroQtdeReparte').hide();
	$('.filtroQtdeVenda').hide();
	$('.filtroComponentes').hide();
	$('.filtroCotas').hide();
	$('.filtroPercVenda').show();
	}
	
	
</script>
<style type="text/css">
.filtroQtdeReparte, .filtroQtdeVenda, .filtroComponentes, .filtroCotas, .filtroPercVenda{display:none;}
</style>


<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
	<img id="imagemCapaEdicao" width="235" height="314" />
</div>

<div class="corpo">
   
    <div class="container">
    
     <!--<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Segmentação Não Recebida < evento > com < status >.</b></p>
	</div-->
      <div class="grids" style="display:noneA;">
      
      <div class="porCota" style="display:noneA;">
      <div style="float:left; width:510px;">
      <fieldset class="classFieldset" style="width:480px!important;">
    <legend>Pesquisar Produto</legend>
        
     <form id="pesquisaPorProduto">
      <table width="440" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="42">Código:</td>
            <td width="60"><input type="text" name="filtro.produtoDto.codigoProduto" id="filtroCodigoProduto" style="width:60px;" /></td>
            <td width="47">Produto:</td>
            <td width="140"><input type="text" name="filtro.produtoDto.nomeProduto" id="filtroNomeProduto" style="width:140px;" /></td>
           	<!-- <td width="76">Classificação:</td>
             <td width="100">
                 <select name="filtro.idTipoClassificacaoProduto" id="selectClassificacao" style="width:200px;">
                  <option selected="selected">Selecione...</option>
                  <c:forEach items="${listaTipoClassificacao}" var="tipoClassificacao">
                  	<option value="${tipoClassificacao.key}">${tipoClassificacao.value}</option>
                  </c:forEach>
                 </select>
                </td-->
            <td width="38">Edição:</td>
            <td width="60"><input type="text" name="filtro.numeroEdicao" id="filtroNumeroEdicao" style="width:60px;" /></td>
            <td width="16"><span class="classPesquisar"><a href="javascript:;" id="pesquisaFiltroProduto">&nbsp;</a></span></td>
          </tr>
        </table>
	</form>
    </fieldset>
    
    <fieldset class="classFieldset" style="width:480px!important; margin-top:10px!important;">
    	<legend>Edições do Produto</legend>
        <table class="edicaoProdCadastradosGrid"></table>
     </fieldset>
     
     <fieldset class="classFieldset" style="width:480px!important; margin-top:10px!important;">
    	<legend>Produtos Selecionados</legend>
        <table class="edicaoSelecionadaGrid"></table>
     </fieldset>
     </div>
      <fieldset class="classFieldset" style="float:left; width:417px!important; margin-left:10px!important;">
  <legend> Pesquisar Histórico de Venda
  </legend><table width="410" border="0" cellpadding="2" cellspacing="1">
          <tr>
            <td width="45"><strong>Status:</strong></td>
            <td width="24"><input type="radio" name="status" id="radio" value="radio" checked /></td>
            <td width="101">Todas as Cotas</td>
            <td width="20"><input type="radio" name="status" id="radio2" value="radio" /></td>
            <td width="184">Cotas Ativas</td>
          </tr>
        </table>
        <table width="410" border="0" cellpadding="2" cellspacing="1">
          <tr>
            <td width="20"><input type="radio" name="filtroPor" id="radio3" value="radio" onclick="filtroReparte();" /></td>
            <td width="87"><strong>Qtde Reparte:</strong></td>
            <td width="48"><span class="filtroQtdeReparte">Inicial:</span></td>
            <td width="62"><input type="text" name="textfield4" id="textfield4" style="width:40px; text-align:center;" class="filtroQtdeReparte"  /></td>
            <td width="35"><span class="filtroQtdeReparte">Final:</span></td>
            <td width="46"><input type="text" name="textfield" id="textfield" style="width:40px; text-align:center;" class="filtroQtdeReparte"  /></td>
            <td width="15"><span class="classPesquisar filtroQtdeReparte"><a href="javascript:;"></a></span></td>
          </tr>
          <tr>
            <td><input type="radio" name="filtroPor" id="radio4" value="radio" onclick="filtroVenda();" /></td>
            <td><strong>Qtde. Venda:</strong></td>
            <td><span class="filtroQtdeVenda">Inicial:</span></td>
            <td><input type="text" name="textfield4" id="textfield4" style="width:40px; text-align:center;" class="filtroQtdeVenda"  /></td>
            <td><span class="filtroQtdeVenda">Final:</span></td>
            <td><input type="text" name="textfield" id="textfield" style="width:40px; text-align:center;" class="filtroQtdeVenda" /></td>
            <td width="15"><span class="classPesquisar filtroQtdeVenda"><a href="javascript:;"></a></span></td>
          </tr>
          <tr>
            <td><input type="radio" name="filtroPor" id="radio5" value="radio" onclick="filtroPercVenda();" /></td>
            <td><strong>% Venda:</strong></td>
            <td>
            <span class="filtroPercVenda"><input name="" type="text" style="width:30px;" />%</span>
            </td>
            <td width="15"><span class="classPesquisar filtroPercVenda"><a href="javascript:;"></a></span></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        <table width="410" border="0" cellpadding="2" cellspacing="1">
          <tr>
            <td width="20"><input type="radio" name="filtroPor" id="radio9" value="radio" onclick="filtroComponentes();" /></td>
            <td width="69"><strong>Componentes:</strong></td>
            <td width="523" colspan="10"><table border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorSegmento" style="display:noneA;">
              <tr>
                <td width="110"><select name="select3" id="select3" style="width:110px;" class="filtroComponentes">
                  <option selected="selected">Selecione...</option>
                </select></td>
                <td width="36"><span class="filtroComponentes">Elem.:</span></td>
                <td width="110"><select name="select3" id="select4" style="width:110px;" class="filtroComponentes">
                  <option selected="selected">Selecione...</option>
                </select></td>
                <td width="15"><span class="classPesquisar filtroComponentes"><a href="javascript:;">&nbsp;</a></span></td>
              </tr>
            </table></td>
          </tr>
        </table>
        <table width="410" border="0" cellpadding="2" cellspacing="1">
          <tr>
            <td width="20"><input type="radio" name="filtroPor" id="radio6" value="radio" onclick="filtroCotas();" /></td>
            <td width="27"><strong>Cota:</strong></td>
            <td colspan="2"><input type="text" name="textfield2" id="textfield2" style="width:60px;" class="filtroCotas"  /></td>
            <td width="30"><span class="filtroCotas"><strong>Nome:</strong></span></td>
            <td width="207"><input type="text" name="textfield2" id="textfield3" style="width:200px;" class="filtroCotas"  /></td>
            <td width="23"><span class="classPesquisar filtroCotas"><a href="javascript:;">&nbsp;</a></span></td>
          </tr>
  </table>
</fieldset>
      <fieldset class="classFieldset" style="width:417px!important; margin-left:10px!important; margin-top:10px!important; ">
       	  <legend>Resultado da Pesquisa</legend>

       	<table class="pesqHistoricoGrid"></table>
       	
        <span class="bt_novos" title="Analisar" style="float:right;"><a href="analise_historico.htm"><img src="images/ico_copia_distrib.gif" hspace="5" border="0" />Analisar</a></span>
        
        <span class="bt_novos" title="Cancelar" style="float:right;"><a href="javascript:;"><img src="images/ico_excluir.gif" hspace="5" border="0" />Cancelar</a></span>

      
      </fieldset>
      
      
      </div>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
$(".edicaoSelecionadaGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 45,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 140,
				sortable : true,
				align : 'left'
			},{
				display : 'Edição',
				name : 'numeroEdicao',
				width : 40,
				sortable : true,
				align : 'left'
			},{
				display : 'Período',
				name : 'periodo',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 480,
			height : 110
		});

$(".segmentoCotaGrid").flexigrid({
			dataType : 'xml',
			colModel : [ {
				display : 'Segmento',
				name : 'segmento',
				width : 260,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
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
			width : 510,
			height : 250
		});
	$(".pesqBancasGrid").flexigrid({
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 400,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
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
			width : 600,
			height : 200
		});
		
		$(".pesqHistoricoGrid").flexigrid({
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			},  {
				display : 'Nome',
				name : 'nome',
				width : 270,
				sortable : true,
				align : 'left'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 415,
			height : 140
		});
		$(".segmentosGrid").flexigrid({
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 160,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			width : 300,
			height : 235
		});
		
	$(".segmentoNaoRecebidaGrid").flexigrid({
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 115,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
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
			width : 630,
			height : 250
		});
</script>
