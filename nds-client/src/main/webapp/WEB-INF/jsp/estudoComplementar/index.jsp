<div id="estudoComplementarTelaAnalise" />
<div id="estudoComplementarContent">

<script type="text/javascript" src='scripts/estudoComplementar.js'></script>

<script type="text/javascript">
function informacoesProdutoShow(link){
	$('#workspace').tabs({
		  load: function( event, ui ) {
			  if(informacoesProdutoController){
				  informacoesProdutoController.targetRecuperarEstudo="#codigoEstudo";
			  }
		  }
	}).tabs('addTab', 'Informa&ccedil;&otilde;es do Produto', link);
}
</script>

<script type="text/javascript">


$(document).ready(function () {
    $("input,select").bind("keydown", function (e) {
        if (e.keyCode == 13 ) {
            var allInputs = $("input,select");
            for (var i = 0; i < allInputs.length; i++) {
                if (allInputs[i] == this) {
                    while ((allInputs[i]).name == (allInputs[i + 1]).name) {
                        i++;
                    }

                    if ((i + 1) < allInputs.length) $(allInputs[i + 1]).focus();
                }
            }
        }
    });

    var estudoValue = $('#codigoEstudo').val();
    
    if (estudoValue) {
	    estudoComplementarController.consultarEstudo($('#codigoEstudo'));
	}
    
});



$("#codigoEstudo").keydown(function(e){
    var keyPressed;
    if (!e) var e = window.event;
    if (e.keyCode) keyPressed = e.keyCode;
    else if (e.which) keyPressed = e.which;
    var hasDecimalPoint = (($(this).val().split('.').length-1)>0);
    if ( keyPressed == 46 || keyPressed == 8 ||((keyPressed == 190||keyPressed == 110)&&(!hasDecimalPoint)) || keyPressed == 9 || keyPressed == 27 || keyPressed == 13 ||
             // Allow: Ctrl+A
            (keyPressed == 65 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (keyPressed >= 35 && keyPressed <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if (e.shiftKey || (keyPressed < 48 || keyPressed > 57) && (keyPressed < 96 || keyPressed > 105 )) {
                e.preventDefault();
            }
        }

  });



$("#reparteCota").keydown(function(e){
    var keyPressed;
    if (!e) var e = window.event;
    if (e.keyCode) keyPressed = e.keyCode;
    else if (e.which) keyPressed = e.which;
    var hasDecimalPoint = (($(this).val().split('.').length-1)>0);
    if ( keyPressed == 46 || keyPressed == 8 ||((keyPressed == 190||keyPressed == 110)&&(!hasDecimalPoint)) || keyPressed == 9 || keyPressed == 27 || keyPressed == 13 ||
             // Allow: Ctrl+A
            (keyPressed == 65 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (keyPressed >= 35 && keyPressed <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if (e.shiftKey || (keyPressed < 48 || keyPressed > 57) && (keyPressed < 96 || keyPressed > 105 )) {
                e.preventDefault();
            }
        }

  });



$("#reparteSobra").keydown(function(e){
    var keyPressed;
    if (!e) var e = window.event;
    if (e.keyCode) keyPressed = e.keyCode;
    else if (e.which) keyPressed = e.which;
    var hasDecimalPoint = (($(this).val().split('.').length-1)>0);
    if ( keyPressed == 46 || keyPressed == 8 ||((keyPressed == 190||keyPressed == 110)&&(!hasDecimalPoint)) || keyPressed == 9 || keyPressed == 27 || keyPressed == 13 ||
             // Allow: Ctrl+A
            (keyPressed == 65 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (keyPressed >= 35 && keyPressed <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if (e.shiftKey || (keyPressed < 48 || keyPressed > 57) && (keyPressed < 96 || keyPressed > 105 )) {
                e.preventDefault();
            }
        }

  });


$("#reparteDistribuicao").keydown(function(e){
    var keyPressed;
    if (!e) var e = window.event;
    if (e.keyCode) keyPressed = e.keyCode;
    else if (e.which) keyPressed = e.which;
    var hasDecimalPoint = (($(this).val().split('.').length-1)>0);
    if ( keyPressed == 46 || keyPressed == 8 ||((keyPressed == 190||keyPressed == 110)&&(!hasDecimalPoint)) || keyPressed == 9 || keyPressed == 27 || keyPressed == 13 ||
             // Allow: Ctrl+A
            (keyPressed == 65 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (keyPressed >= 35 && keyPressed <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if (e.shiftKey || (keyPressed < 48 || keyPressed > 57) && (keyPressed < 96 || keyPressed > 105 )) {
                e.preventDefault();
            }
        }

  });


function somarSobra(){
	
	var reparteDistribuicao = $('#reparteDistribuicao').val();
	
	if (!reparteDistribuicao) {
		
		reparteDistribuicao = 0;
	}
	
	$('#reparteSobra').val($("#reparteLancamento").val() - reparteDistribuicao);
};

function somarDistribuicao(){
	
	var reparteSobra = $('#reparteSobra').val();
	
	if (!reparteSobra) {
		
		reparteSobra = 0;
	}
	
	$('#reparteDistribuicao').val($("#reparteLancamento").val() - reparteSobra);
};
</script>

<div id="dialog-cancelar" title="Cancelar Operação" style="display:none;">  
    <fieldset style="width:320px!important;">
            <legend>Cancelar Operação</legend>
            <p>Confirma o Cancelamento desta Operação?</p>
    </fieldset>
</div>

<div id="dialog-novo" title="Nova Base">  
    <fieldset style="width:510px!important;">
            <legend>Pesquisa de Bases</legend>
            <table width="500" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="37">Código:</td>
                <td width="64"><input type="text" name="textfield" id="textfield" style="width:60px; text-align:center;"  /></td>
                <td width="41">Produto:</td>
                <td width="178"><input type="text" name="textfield2" id="textfield2" style="width:160px; text-align:center;"  /></td>
                <td width="41">Edição:</td>
                <td width="101"><input type="text" name="textfield" id="textfield" style="width:60px; text-align:center;"  /></td>
              </tr>
            </table>
    </fieldset>
</div>






<div class="corpo">
  <div class="bg_menu">

    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Box < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend>Estudo Complementar</legend>
   	    <input type="hidden" id="idLancamento" value="${idLancamento}" />
   	    <input type="hidden" id="idProdutoEdicao" value="${idProdutoEdicao}" />
   	    <table width="950" border="0" cellspacing="2" cellpadding="2">
          <tr>
              <td width="78"><strong>Estudo Base:</strong></td>
              <td width="102"><input type="text" name="codigoEstudo" id="codigoEstudo" style="width:70px; margin-right:5px; float:left;" maxlength="8" value="${estudoId}" onblur="estudoComplementarController.consultarEstudo(this);"/>
                <span class="classPesquisar"><a href= "javascript:;" onclick="informacoesProdutoShow('${pageContext.request.contextPath}/distribuicao/informacoesProduto')">&nbsp;</a></span></td>
              <td width="130"><strong>Estudo Complementar:</strong></td>
              <td width="64" id="idEstudoComplementar">
                
              </td>
              <td width="42"><strong>Código:</strong></td>
              <td width="42" id="idProduto"></td>
              <td width="47"><strong>Produto:</strong></td>
              <td width="158" id="nomeProdutoLabel"></td>
              <td width="38"><strong>Edição:</strong></td>
              <td width="28" id="numeroEdicao"></td>
              <td width="77"><strong>Classificação:</strong></td>
              <td width="70" id="nomeClassificacao"></td>
          </tr>
        </table>
      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
   	    <legend>Publicação</legend>
          <table width="950" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="76"><strong>Código:</strong></td>
                <td width="165" id="publicacao" ></td>
                <td width="76"><strong>Produto:</strong></td>
                <td width="243" id="publicacaoNomeProduto" ></td>
                <td width="70"><strong>Edição:</strong></td>
                <td width="100" id="publicacaoEdicao" ></td>
                <td width="70"><strong>PEB:</strong></td>
                <td width="100" id="publicacaoPEB"></td>
              </tr>
              <tr>
                <td><strong>Classificação:</strong></td>
                <td id="publicacaoClassificacao"></td>
                <td><strong>Fornecedor:</strong></td>
                <td id="publicacaoNomeFornecedor"></td>
                <td><strong>Data Lncto:</strong></td>
                <td id="publicacaoDataLncto"></td>
                <td><strong>Data Rclto:</strong></td>
                <td id="publicacaoDataRclto"></td>
              </tr>
            </table>


      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
   	    <legend>Parâmetros</legend>
          <table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
              <tr>
                <td width="62">Tipo Base:</td>
                <td width="180" ><select name="tipoSelecao" id="tipoSelecao" style="width:160px;">
                  <option selected="selected" value="0">Selecione...</option>
                  <option  value="RANKING_SEGMENTO">Ranking Segmento</option>
                  <option value="RANKING_FATURAMENTO">Ranking Faturamento</option>
                </select></td>
                <td width="106">Reparte por Cota:</td>
                <td width="78"><input name="reparteCota" type="text" id="reparteCota" style="width:60px; text-align:center;" value="2" /></td>
                <td width="84">Reparte Lncto:</td>
                <td width="70"><input name="reparteLancamento" type="text" id="reparteLancamento" style="width:60px; text-align:center;" value="${reparteDisponivel}"  readonly="readonly" /></td>
                <td width="46">Sobra:</td>
                <td width="75"><input name="reparteSobra" type="text" id="reparteSobra" style="width:60px; text-align:center;" value="" onchange="somarDistribuicao();"/></td>
                <td width="127">Reparte Distribuido:</td>
                <td width="60"><input name="reparteDistribuicao" type="text" id="reparteDistribuicao" style="width:60px; text-align:center;" value="${reparteDisponivel}" onchange="somarSobra();" o/></td>
              </tr>
              <tr>
                <td align="right"><input name="checkbox" type="checkbox" id="checkboxDistMult" onclick="$('.distrMult').toggle();" /></td>
                <td >Distribuir por Múltiplo</td>
                <td><input type="text" class="distrMult" id="distrMult" style="width:40px; text-align:center; display:none;" value="${pacotePadrao}" /></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            </table>


      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
      <span class="bt_novos"><a href="javascript:$('#workspace').tabs('remove', $('#workspace').tabs('option', 'selected'));matrizDistribuicao.tabSomarCopiarEstudos='';"><img src="${pageContext.request.contextPath}/images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />Voltar</a></span>
      <span class="bt_novos"><a href="javascript:;" onclick="$('#codigoEstudo').val('').blur();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Cancelar" hspace="5" border="0" />Cancelar</a></span>
      <span class="bt_novos"><a href="#" onclick="estudoComplementarController.gerarEstudoComplementar();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Gerar Estudo" hspace="5" border="0" />Gerar Estudo</a></span>
      <span class="bt_novos"><a href="javascript:;" onclick="estudoComplementarController.analisar()"><img src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif" alt="Confirmar" hspace="5" border="0" />Análise</a></span>
        

    
    </div>
</div> 
<script>
	$(".elemento1Grid").flexigrid({
			url : '../xml/elemento1-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Elementos',
				name : 'elementos',
				width : 245,
				sortable : true,
				align : 'left'
			}, {
				display : 'Prioridade',
				name : 'prioridade',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Forma Calc. %',
				name : 'formaCalc',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Min.',
				name : 'reparteMinimo',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 670,
			height : 130
		});
	
	$(".bonificacoesGrid").flexigrid({
			url : '../xml/criarRegiao-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Elementos',
				name : 'elementos',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Prioridade',
				name : 'prioridade',
				width : 57,
				sortable : true,
				align : 'left'
			}],
			width : 325,
			height : 110
		});
	$(".lstComponentesGrid").flexigrid({
			url : '../xml/listaComponentes-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Componentes',
				name : 'componentes',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 25,
				sortable : true,
				align : 'center'
			}],
			width : 325,
			height : 110
		});
	$(".dadosBasesGrid").flexigrid({
			url : '../xml/dadosDistrib-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda %',
				name : 'vendaPerc',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Peso',
				name : 'peso',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 690,
			height : 240
		});
</script>

