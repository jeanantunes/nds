<head>
<script language="javascript" type="text/javascript">
function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:300,
			width:550,
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
			height:300,
			width:550,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					$( "#abaPdv" ).show( );
					
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

</script>
</head>

<body>

<div id="dialog-excluir" title="Excluir Tipo de Movimento">
  <p>Confirma a exclusão deste Tipo de Movimento?</p>
</div>





<div id="dialog-novo" title="Incluir Tipo de Movimento">
     
	<label><strong>Tipo de Movimento</strong></label>
    
    <table width="370" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="109">Código:</td>
              <td width="250"><input type="text" name="textfield2" id="textfield2" style="width:87px;"/></td>
            </tr>
            <tr>
              <td>Descrição:</td>
              <td><input type="text" name="textfield7" id="textfield7" style="width:250px;"/></td>
            </tr>
            <tr>
              <td>Grupo Operação:</td>
              <td><select name="select4" id="select4" style="width:100px;">
                <option>Financeiro</option>
                <option>Estoque</option>
              </select></td>
            </tr>
            <tr>
              <td>Operação:</td>
              <td><select name="select" id="select" style="width:100px;">
                <option>Débito</option>
                <option>Crédito</option>
              </select></td>
            </tr>
            <tr>
              <td>Aprovação:</td>
              <td><select name="select2" id="select2" style="width:100px;">
                <option>Sim</option>
                <option>Não</option>
              </select></td>
            </tr>
            <tr>
              <td>Incide Dívida:</td>
              <td><select name="select3" id="select3" style="width:100px;">
                <option>Sim</option>
                <option>Não</option>
              </select></td>
            </tr>
          </table>
</div>
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Tipo de Movimento < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Tipos de Movimento</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="52">Código:</td>
              <td width="95"><input type="text" name="textfield3" id="textfield3" style="width:80px;"/></td>
              <td width="122">Tipo de Movimento:</td>
              <td width="551"><input type="text" name="textfield" id="textfield" style="width:200px;"/></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Tipos de Movimento Cadastrados</legend>
        <div class="grids" style="display:none;">
       	  <table class="movimentosGrid"></table>
        </div>

            <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
	$(".movimentosGrid").flexigrid({
			url : '../xml/movimentos-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 95,
				sortable : true,
				align : 'left'
			}, {
				display : 'Descrição',
				name : 'descricao',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : 'Grupo de Operação',
				name : 'grupoOperacao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Operação',
				name : 'operacao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Aprovação',
				name : 'aprovacao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Incide na Dídiva',
				name : 'incide',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "cd",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
</script>
</body>
</html>
