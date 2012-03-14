<head>
<script language="javascript" type="text/javascript">
function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:490,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				},
				
			}
		});
	};
	
function popup_encalhe() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encalhe" ).dialog({
			resizable: false,
			height:460,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};

function popup_encalhe_2() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encalhe_2" ).dialog({
			resizable: false,
			height:460,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};

		
function popup_contaCorrente() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-conta" ).dialog({
			resizable: false,
			height:340,
			width:660,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};	
function popup_encargos() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encargos" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};	
</script>
<style type="text/css">
  fieldset { width:auto!important; }
  </style>
</head>

<body>

<div id="dialog-encargos" title="Detalhes do Encargo">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        <br />

        <label><strong>Juros R$:</strong></label>  <label>999,99</label>
        <br />
        <br clear="all" />

        <label><strong>Multa R$:</strong></label>  <label>999,99</label>

        

    </fieldset>

</div>


<div id="dialog-conta" title="Detalhe Tipo de Movimento">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="encalhes_3Grid"></table>
        <div style="float:right; margin-right:30px;">Total: <strong>R$ 999.999,99</strong></div>
        <br clear="all" />
        <span class="bt_arquivo"><a href="javascript:;" onclick="popup();">Arquivo</a></span>
            
            <span class="bt_imprimir"><a href="javascript:;" onclick="popup();">Imprimir</a></span>

    </fieldset>

</div>


<div id="dialog-encalhe_2" title="Venda de Encalhe">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="encalhes_2Grid"></table>
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <table width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
              <tr>
                <td width="109"><strong>Total R$:</strong></td>
                <td width="53"><strong>Dinap:</strong></td>
                <td width="92" align="right">999.999,99</td>
                <td width="10">&nbsp;</td>
              </tr>
              <tr>
                <td height="23" align="right"></td>
                <td><strong>FC:</strong></td>
                <td align="right">999.999,99</td>
                <td>&nbsp;</td>
              </tr>
            </table>


    </fieldset>

</div>


<div id="dialog-encalhe" title="Encalhe da Cota">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="encalhesGrid"></table>
        
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <table width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
              <tr>
                <td width="109"><strong>Total R$:</strong></td>
                <td width="53"><strong>Dinap:</strong></td>
                <td width="92" align="right">999.999,99</td>
                <td width="10">&nbsp;</td>
              </tr>
              <tr>
                <td height="23" align="right"></td>
                <td><strong>FC:</strong></td>
                <td align="right">999.999,99</td>
                <td>&nbsp;</td>
              </tr>
            </table>

    </fieldset>

</div>




<div id="dialog-novo" title="Consignados">
     
	<fieldset>
    	<legend>Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="consignadoGrid"></table>
        
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <table width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
              <tr>
                <td width="109"><strong>Total R$:</strong></td>
                <td width="53"><strong>Dinap:</strong></td>
                <td width="92" align="right">999.999,99</td>
                <td width="10">&nbsp;</td>
              </tr>
              <tr>
                <td height="23" align="right"></td>
                <td><strong>FC:</strong></td>
                <td align="right">999.999,99</td>
                <td>&nbsp;</td>
              </tr>
            </table>

            
                   
        

    </fieldset>

</div>





<div class="corpo">
  <div class="header">
  	<div class="sub-header">
    	<div class="logo"><img src="../images/logo_sistema.png" width="109" height="67" alt="Picking Eletrônico"  /></div>
        
        <div class="titAplicacao">
        	<h1>Treelog S/A. Logística e Distribuição - SP</h1>

			<h2>CNPJ: 00.000.000/00001-00</h2>
            <h3>Distrib vs.1</h3>
        </div>
        
        <div class="usuario"><a href="../index.htm"><img src="../images/bt_sair.jpg" alt="Sair do Sistema" title="Sair do Sistema" width="63" height="27" border="0" align="right" /></a>
            <br clear="all" />
          <span>Usuário: Junior Fonseca</span>
          <span>
          <script type="text/javascript" language="JavaScript">
          diaSemana();
          </script>

          </span>
        </div>
    
    </div>
  
  </div>
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Pessoa < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Conta-Corrente</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="32">Cota:</td>
              <td colspan="3"><input type="text" name="textfield2" id="textfield2" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
                <td width="36">Nome:</td>
                <td width="263"><input type="text" name="textfield" id="textfield" style="width:230px;"/></td>
                <td width="72">&nbsp;</td>
                <td width="283">&nbsp;</td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Conta-Corrente Selecionado</legend>
        <div class="grids" style="display:none;">
          <strong>26 1335 - CGB Distribuidora de Jornais e Revista</strong>
          <br />

       	  <table class="contaGrid"></table>
        
                	<span class="bt_novos" title="Negociar Divida"><a href="javascript:;"><img src="../images/ico_negociar.png" hspace="5" border="0" />Negociar Divida</a></span>

                    
                    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
</div>
            
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
	$(".contaGrid").flexigrid({
			url : '../xml/contacorrente-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vlr. Postergado',
				name : 'vlrpostergado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'NA',
				name : 'na',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Consignado',
				name : 'consignadoaVencer',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe	',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda Encalhe',
				name : 'vendaEncalhe',
				width : 90,
				sortable : true,
				align : 'right',
			}, {
				display : 'Déb/Cred.',
				name : 'encalhe',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encargos',
				name : 'encargos',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pendente',
				name : 'pendente',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 105,
				sortable : true,
				align : 'right'
			}],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
		$(".consignadoGrid").flexigrid({
			url : '../xml/consignado-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Reparte Sugerido',
				name : 'reparte',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Final',
				name : 'reparteFinal',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Motivo',
				name : 'motivo',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 40,
				sortable : true,
				align : 'right'
			}],
			sortname : "Nome",
			sortorder : "asc",
			width : 800,
			height : 200
		});
		
		$(".encalhesGrid").flexigrid({
			url : '../xml/encalhes-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'valor',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 75,
				sortable : true,
				align : 'right',
			}],
			sortname : "Nome",
			sortorder : "asc",
			width : 800,
			height : 200
		});
		
		$(".encalhes_2Grid").flexigrid({
			url : '../xml/encalhes_2-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço de Capa R$',
				name : 'valor',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Box',
				name : 'box',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 70,
				sortable : true,
				align : 'right',
			}],
			sortname : "Nome",
			sortorder : "asc",
			width : 800,
			height : 200
		});
		
		$(".encalhes_3Grid").flexigrid({
			url : '../xml/encalhes_3-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Tipo Movimento',
				name : 'tipoMovimento',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Observação',
				name : 'Observacao',
				width : 290,
				sortable : true,
				align : 'left'
			}],
			width : 600,
			height : 120
		});
</script>
</body>
</html>
