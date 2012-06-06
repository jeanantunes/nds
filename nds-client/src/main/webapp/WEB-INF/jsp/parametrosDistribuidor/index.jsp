<head>
<title>Parâmetros do Distribuidor</title>
<style type="text/css">
	#dialog-confirm{display:none;}
	label{width:auto!important;}
	#dialog-pesq-fornecedor fieldset {width:450px!important;}
	.forncedoresSel{display:none;}
	#dialog-pesq-fornecedor{display:none;}
	.forncedores ul{margin:0px; padding:0px;}
	.forncedores li{display:inline;}
	.forncedoresSel, .editorSel {
	    padding: 0px!important;
	}
</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.image.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.link.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.table.js"></script>
<script language="javascript" type="text/javascript">

function popup_confirm() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:160,
			width:400,
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
	
function popup_pesq_fornecedor() {
	
		$( "#dialog-pesq-fornecedor" ).dialog({
			resizable: false,
			height:300,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$( ".forncedoresSel" ).css('display','table-cell');
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

function mostra_funcionalidades(){
	$('.funcionalidades').show();
}

function removeFornecedor(){
	$( ".forncedoresSel" ).fadeOut('fast');
}

$(function() {
	$( "#tabDistribuidor" ).tabs();
});

$(document).ready(function() {
		$('#editor').wysiwyg();
		$('#editor').wysiwyg({controls:"font-family,italic,|,undo,redo"});
		$('#editor_1').wysiwyg();
		$('#editor_1').wysiwyg({controls:"font-family,italic,|,undo,redo"});
});
</script>
</head>

<body>

<form name="form" id="form" method="post">

<div id="dialog-pesq-fornecedor" title="Selecione os Fornecedores">
	<fieldset>
		<legend>Selecione um ou mais Fornecedores</legend>
	    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
          <c:forEach items="${fornecedores}" var="fornecedor">
          	<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
     	  </c:forEach>
	    </select>
	</fieldset>
</div>

<div id="dialog-confirm" title="Salvar Parâmetro do Distribuidor">
	<p>Confirma os Parâmetros do Distribuidor?</p>
</div>
<div class="corpo">
    <div class="container">	
    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Parâmetros do Distribuidor < evento > com < status >.</b></p>
	</div>
    <fieldset class="classFieldset">
   	    <legend>Parâmetros do Distribuidor</legend>
        <div id="tabDistribuidor">
			<ul>
				<li><a href="#tabOperacao">Operação</a></li>
				<li><a href="#tabEmissao">Fiscal / Emissão de Documentos</a></li>
				<li><a href="#tabContratos">Contratos e Garantias</a></li>
			       <li><a href="#tabNegociacao">Negociação</a></li>
			       <li><a href="#tabAprovacao">Aprovação</a></li>
			</ul>
			<div id="tabOperacao">
				<fieldset style="width:440px!important; margin-bottom:5px; float:left;">
		                <legend>Dias de Operação:</legend>
		                <table width="441" border="0" cellpadding="0" cellspacing="1">
		                  <tr class="header_table">
		                    <td>Fornecedor</td>
		                    <td align="center">Lançamento</td>
		                    <td align="center">Recolhimento</td>
		                  </tr>
		                  <tr class="class_linha_1">
		                    <td width="141">
		                    	<select name="selectFornecedoresLancamento[]" size="5" multiple="multiple" id="selectFornecedoresLancamento" style="width:130px; height:100px">
                    		  		<c:forEach items="${fornecedores}" var="fornecedor">
		                      			<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
		                      		</c:forEach>
		                    	</select>
		                    </td>
		                    <td width="157" align="center">
		                    	<select name="selectDiasLancamento[]" size="5" multiple="multiple" id="selectDiasLancamento" style="width:130px; height:100px">
			                      <option value="1">Domingo</option>
			                      <option value="2">Segunda-feira</option>
			                      <option value="3">Terça-feira</option>
			                      <option value="4">Quarta-feira</option>
			                      <option value="5">Quinta-feira</option>
			                      <option value="6">Sexta-feira</option>
			                      <option value="7">Sábado</option>
		                    	</select>
		                    </td>
		                    <td width="139" align="center">
		                    	<select name="selectDiasRecolhimento[]" size="5" multiple="multiple" id="selectDiasRecolhimento" style="width:130px; height:100px">
			                      <option value="1">Domingo</option>
			                      <option value="2">Segunda-feira</option>
			                      <option value="3">Terça-feira</option>
			                      <option value="4">Quarta-feira</option>
			                      <option value="5">Quinta-feira</option>
			                      <option value="6">Sexta-feira</option>
			                      <option value="7">Sábado</option>
		                    	</select>
		                    </td>
		                  </tr>
		                  <tr>
		                    <td>&nbsp;</td>
		                    <td align="center">&nbsp;</td>
		                    <td width="139" align="center"><span class="bt_add"><a href="javascript:;" onclick="form.action='${pageContext.request.contextPath}/administracao/parametrosDistribuidor/gravarDiasDistribuidorFornecedor'; form.submit()" >Incluir Novo</a></span></td>
		                  </tr>
		                </table>
		                <br />
		                <table width="441" border="0" cellpadding="0" cellspacing="1">
		                  <tr class="header_table">
		                    <td>Fornecedor</td>
		                    <td align="center">Lançamento</td>
		                    <td align="center">Recolhimento</td>
		                    <td align="center">&nbsp;</td>
		                  </tr>
                		  <c:forEach items="${listaDiaOperacaoFornecedor}" var="registroDiaOperacaoFornecedor">
			                  <tr class="class_linha_1">
			                    <td width="139">${registroDiaOperacaoFornecedor.fornecedor.juridica.nomeFantasia}</td>
			                    <td width="144" align="center">${registroDiaOperacaoFornecedor.diasLancamento}</td>
			                    <td width="125" align="center">${registroDiaOperacaoFornecedor.diasRecolhimento}</td>
			                    <td width="28" align="center"><a href="${pageContext.request.contextPath}/administracao/parametrosDistribuidor/excluirDiasDistribuicaoFornecedor?codigoFornecedor=${registroDiaOperacaoFornecedor.fornecedor.id}" ><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="15" height="15" alt="Excluir" /></a></td>
			                  </tr>
                   		  </c:forEach>
		                </table>
		              </fieldset>
			          <fieldset style="width:410px!important; margin-bottom:5px;">
			                <legend>Recolhimento</legend>
			            <table width="398" border="0" cellspacing="0" cellpadding="0">
			              <tr>
			                <td width="197">&nbsp;</td>
			                <td colspan="11">&nbsp;</td>
			              </tr>
			              <tr>
			                <td>Aceita Encalhe Juramentada:</td>
			                <td width="22"><input name="input12" type="checkbox" value="" /></td>
			                <td width="15">&nbsp;</td>
			                <td width="21">&nbsp;</td>
			                <td width="16">&nbsp;</td>
			                <td width="20">&nbsp;</td>
			                <td width="16">&nbsp;</td>
			                <td width="21">&nbsp;</td>
			                <td width="13">&nbsp;</td>
			                <td width="20">&nbsp;</td>
			                <td width="15">&nbsp;</td>
			                <td width="22">&nbsp;</td>
			              </tr>
			              <tr>
			                <td>Dias de Recolhimento:</td>
			                <td><input name="input14" type="checkbox" value="" /></td>
			                <td>1º</td>
			                <td><input name="input15" type="checkbox" value="" /></td>
			                <td>2º</td>
			                <td><input name="input16" type="checkbox" value="" /></td>
			                <td>3º</td>
			                <td><input name="input49" type="checkbox" value="" /></td>
			                <td>4º</td>
			                <td><input name="input50" type="checkbox" value="" /></td>
			                <td>5º</td>
			                <td>Dias</td>
			              </tr>
			              <tr>
			                <td>Permite Recolher dias Posteriores:</td>
			                <td><input name="input17" type="checkbox" value="" /></td>
			                <td colspan="10">Limite CE Próxima Semana</td>
			              </tr>
			              <tr>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			                <td>&nbsp;</td>
			              </tr>
			            </table>
			            <table width="390" border="0" cellspacing="1" cellpadding="0">
			              <tr class="header_table">
			                <td align="center">&nbsp;</td>
			                <td align="center">Recebimento</td>
			                <td align="center">Encalhe</td>
			              </tr>
			              <tr>
			                <td width="123" align="center" class="class_linha_1">Conferência Cega</td>
			                <td width="115" align="center"><input name="input13" type="checkbox" value="" /></td>
			                <td width="148" align="center"><input name="input23" type="checkbox" value="" /></td>
			              </tr>
			            </table>
			          </fieldset>
		      		  <fieldset style="width:410px!important; margin-bottom:5px;">
		                <legend>Capacidade de Manuseio </legend>
		                <table width="390" border="0" cellspacing="1" cellpadding="0">
		                  <tr class="header_table">
		                    <td align="center">&nbsp;</td>
		                    <td align="center">Lançamento</td>
		                    <td align="center">Recolhimento</td>
		                  </tr>
		                  <tr>
		                    <td width="123" align="center" class="class_linha_1">Exes. Homem/ Hora</td>
		                    <td width="115" align="center"><input type="text" name="textfield10" id="textfield10" style="width:40px; text-align:center;" /></td>
		                    <td width="148" align="center"><input type="text" name="textfield11" id="textfield11" style="width:40px; text-align:center;" /></td>
		                  </tr>
		                </table>
		              </fieldset>     
			          <fieldset style="width:410px!important; margin-bottom:5px;">
			            <legend>Reutilização de Código de Cota</legend>
			            <table width="390" border="0" cellspacing="1" cellpadding="0">
			              <tr>
			                <td width="222" align="left">Reutilização de Código de Cota Inativa:</td>
			                <td width="40" align="center"><input name="textfield10" type="text" id="textfield10" style="width:40px; text-align:center;" value="06" /></td>
			                <td width="124" align="left"> &nbsp;( meses )</td>
			              </tr>
			            </table>
			          </fieldset>
		              <fieldset style="width:440px!important; margin-bottom:5px;">
		                <legend>Parciais / Matriz de Lançamento</legend>
		                <label>Relançamento de Parciais em D+: </label>
		                <select name="select18" size="1" multiple="multiple" id="select18" style="width:50px; height:19px;">
		                  <option selected="selected">2</option>
		                  <option>3</option>
		                  <option>4</option>
		                  <option>5</option>
		                  <option>6</option>
		                  <option>7</option>
		                  <option>8</option>
		                  <option>9</option>
		                  <option>10</option>
		                  <option>11</option>
		                  <option>12</option>
		                  <option>13</option>
		                  <option>14</option>
		                  <option>15</option>
		                  <option>16</option>
		                  <option>17</option>
		                  <option>18</option>
		                  <option>19</option>
		                  <option>20</option>
		                </select>
		              </fieldset>
			</div>
			<div id="tabEmissao">
				<fieldset style="width:450px!important; margin-bottom:5px;">
		              <legend>Fiscal</legend>
		              <table width="426" border="0" cellspacing="0" cellpadding="0">
		                <tr>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td align="right">&nbsp;</td>
		                  <td align="right">&nbsp;</td>
		                  <td colspan="4"><strong>Distribuidor</strong></td>
		                </tr>
		                <tr>
		                  <td width="20"><input name="input48" type="checkbox" value="" /></td>
		                  <td width="97"> Obrigação Fiscal</td>
		                  <td width="20"><input name="input29" type="checkbox" value="" /></td>
		                  <td width="98">Regime Especial</td>
		                  <td width="20"><input type="radio" name="radio" id="radio" value="radio" /></td>
		                  <td width="101">Prestador Serviço</td>
		                  <td width="20"><input type="radio" name="radio" id="radio2" value="radio" /></td>
		                  <td width="50">Mercantil</td>
		                </tr>
		                <tr>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td>&nbsp;</td>
		                  <td colspan="3">&nbsp;</td>
		                </tr>
		              </table>
	            </fieldset>
	            <fieldset style="width:450px!important;">
		           	<legend>Emissão de Documentos</legend>
		                <table width="430" border="0" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td>Utiliza:</td>
		                    <td align="center">Impressão</td>
		                    <td align="center">E-mail</td>
		                  </tr>
		                  <tr>
		                    <td width="203">Slip</td>
		                    <td width="120" align="center"><input name="input4" type="checkbox" value="" /></td>
		                    <td width="107" align="center"><input name="input19" type="checkbox" value="" /></td>
		                  </tr>
		                  <tr>
		                    <td>Boleto</td>
		                    <td align="center"><input name="input18" type="checkbox" value="" /></td>
		                    <td align="center"><input name="input21" type="checkbox" value="" /></td>
		                  </tr>
		                  <tr>
		                    <td>Boleto + Slip</td>
		                    <td align="center"><input name="input2" type="checkbox" value="" /></td>
		                    <td align="center"><input name="input20" type="checkbox" value="" /></td>
		                  </tr>
		                  <tr>
		                    <td>Recibo</td>
		                    <td align="center"><input name="input27" type="checkbox" value="" /></td>
		                    <td align="center"><input name="input22" type="checkbox" value="" /></td>
		                  </tr>
		                  <tr>
		                    <td>Nota de Envio</td>
		                    <td align="center"><input name="input40" type="checkbox" value="" /></td>
		                    <td align="center"><input name="input41" type="checkbox" value="" /></td>
		                  </tr>
		                  <tr>
		                    <td>Chamada de Encalhe</td>
		                    <td align="center"><input name="input43" type="checkbox" value="" /></td>
		                    <td align="center"><input name="input42" type="checkbox" value="" /></td>
		                  </tr>
		                </table>
	            </fieldset>
	            <fieldset style="width:320px!important; margin-bottom:5px;">
		           	  <legend>Impressão NE</legend>
		                <table width="325" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="26"><input type="radio" name="radio5" id="radio5" value="radio5" /></td>
		                        <td width="93"><a href="${pageContext.request.contextPath}/Devolucao/ce_modelo_1.htm" target="_blank">Modelo 1</a></td>
		                        <td width="20"><input type="radio" name="radio5" id="radio6" value="radio5" /></td>
		                        <td width="287"><a href="${pageContext.request.contextPath}/Devolucao/ce_modelo_2.html" target="_blank">Modelo 2</a></td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
	                    </table>
	                    <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                      <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="input6" type="checkbox" value="" /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="input3" type="checkbox" value="" /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input30" type="checkbox" value="" /></td>
		                        <td>Falta em</td>
		                        <td><input name="input5" type="checkbox" value="" /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
						</table>
			  	</fieldset>
	            <fieldset style="width:320px!important; margin-bottom:5px;">
		               	<legend>Impressão NECA / Danfe</legend>
		                <table width="325" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="20"><input type="radio" name="radio5" id="radio5" value="radio5" /></td>
		                        <td width="79"><a href="${pageContext.request.contextPath}/Devolucao/ce_modelo_1.htm" target="_blank">Modelo 1</a></td>
		                        <td width="20"><input type="radio" name="radio5" id="radio6" value="radio5" /></td>
		                        <td width="75"><a href="${pageContext.request.contextPath}/Devolucao/ce_modelo_3.htm" target="_blank">Modelo 2</a></td>
		                        <td width="20"><input type="radio" name="radio5" id="radio7" value="radio5" /></td>
		                        <td width="111"><a href="javascript:;" target="_blank">Danfe</a></td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
		                    <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                      <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="input6" type="checkbox" value="" /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="input3" type="checkbox" value="" /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input30" type="checkbox" value="" /></td>
		                        <td>Falta em</td>
		                        <td><input name="input5" type="checkbox" value="" /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                </table>
	              </fieldset>
	              <fieldset style="width:320px!important; margin-bottom:5px;">
		           	  	<legend>Impressão CE</legend>
		                <table width="325" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="26"><input type="radio" name="radio5" id="radio5" value="radio5" /></td>
		                        <td width="93"><a href="${pageContext.request.contextPath}/Devolucao/ce_modelo_1.htm" target="_blank">Modelo 1</a></td>
		                        <td width="20"><input type="radio" name="radio5" id="radio6" value="radio5" /></td>
		                        <td width="287"><a href="${pageContext.request.contextPath}/Devolucao/ce_modelo_2.html" target="_blank">Modelo 2</a></td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
	                    </table>
	                    <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                      <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="input6" type="checkbox" value="" /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="input3" type="checkbox" value="" /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input30" type="checkbox" value="" /></td>
		                        <td>Falta em</td>
		                        <td><input name="input5" type="checkbox" value="" /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		              	</table>
	  			  </fieldset>
	              <br clear="all"  />
			</div>
			<div id="tabContratos">
				<fieldset style="width:420px!important; margin-bottom:5px;">
		               	<legend>Condições de Contratação:</legend>
		                <table width="392" border="0" cellspacing="1" cellpadding="1">
		                  <tr>
		                    <td>Utiliza Contrato com as Cotas?</td>
		                    <td><input name="input31" type="checkbox" value="" /></td>
		                  </tr>
		                  <tr>
		                    <td width="190">Prazo do Contrato (em meses ):</td>
		                    <td width="195"><input type="text" name="textfield" id="textfield"  style="width:80px;" disabled="disabled"/></td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">&nbsp;</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">Informações complementares do Contrato:</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2"><textarea name="editor" rows="4" id="editor" style="width:350px;"></textarea></td>
		                  </tr>
		                </table>
                </fieldset>
                <fieldset style="width:420px!important; margin-bottom:5px;">
		                <legend>Procuração</legend>
		                <table width="393" border="0" cellspacing="1" cellpadding="1">
		                  <tr>
		                    <td><input name="input32" type="checkbox" value="" /></td>
		                    <td width="386" align="left">Utiliza procuração para  Entregadores?</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">&nbsp;</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2">Informações complementares da Procuração:</td>
		                  </tr>
		                  <tr>
		                    <td colspan="2"><textarea name="editor_1" rows="4" id="editor_1" style="width:150px;"></textarea></td>
		                  </tr>
		                </table>
                </fieldset>
                <br clear="all" />
	            <fieldset style="width:870px!important; margin-bottom:5px;">
		               	<legend>Garantia</legend>
		                    <table width="300" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="170">Utiliza Garantia para PDVs?</td>
		                        <td width="25"><input name="" type="checkbox" value="" /></td>
		                        <td width="35">&nbsp;</td>
		                        <td width="20">&nbsp;</td>
		                        <td width="50">&nbsp;</td>
		                      </tr>
		                    </table>
		               <table width="780" border="0" cellspacing="1" cellpadding="1">
		                 <tr>
		                   <td width="20"><input name="input33" type="checkbox" value="" /></td>
		                   <td width="201"> Cheque Caução</td>
		                   <td width="104"><input name="date1" type="text" style="float:left; width:60px;" id="date1" /></td>
		                        <td width="20"><input name="input38" type="checkbox" value="" /></td>
		                        <td width="105">Fiador</td>
		                        <td width="78"><input name="dateFiador" type="text" style="float:left; width:60px;" id="dateFiador" /></td>
		                        <td width="20"><input name="input34" type="checkbox" value="" /></td>
		                        <td width="144">Imóvel</td>
		                        <td width="60"><input name="date2" type="text" style="float:left; width:60px;" id="date2" /></td>
		                 </tr>
		                 <tr>
		                   <td><input name="input35" type="checkbox" value="" /></td>
		                   <td>Caução Líquida</td>
		                   <td><input name="date3" type="text" style="float:left; width:60px;" id="date3" /></td>
		                        <td><input name="input39" type="checkbox" value="" /></td>
		                        <td>Nota Promissória</td>
		                        <td><input name="dateCaucao" type="text" style="float:left; width:60px;" id="dateCaucao" /></td>
		                        <td><input name="input36" type="checkbox" value="" /></td>
		                        <td>Antecedência da Validade</td>
		                        <td><input name="date4" type="text" style="float:left; width:60px;" id="date4" /></td>
		                 </tr>
		                 <tr>
		                   <td><input name="input37" type="checkbox" value="" /></td>
		                   <td>Indicador  reajuste de caução líquida</td>
		                   <td><input name="" type="text" style="width:60px;" /></td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                 </tr>
		            </table>
		
	            </fieldset> 
			</div>
		    <div id="tabNegociacao">
				<fieldset style="width:400px!important; margin-bottom:5px;">
		               	<legend>Negociação de Dividas</legend>
		                  <table width="393" border="0" cellspacing="1" cellpadding="1">
		                      <tr>
		                        <td colspan="2"><label>Sugere suspensão quando atingir</label><input name="input45" type="text" style="width:30px; " /> 
		                        &nbsp;boletos 
		                        ou R$
		                          <input name="input46" type="text" style="width:60px; text-align:right;" value="0,00" />
		                        </td>
		                      </tr>
		                      <tr>
		                        <td colspan="2">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td width="259"><label>Parcelamento de Divida:</label></td>
		                        <td width="127"><input name="input24" type="checkbox" value="" /></td>
		                      </tr>
		                      <tr>
		                        <td><label>Negociação em até x parcelas:</label></td>
		                        <td><input name="input47" type="text" style="width:40px;" /></td>
		                      </tr>
		                      <tr>
		                        <td><label>Permite pagamento de dividas divergente:</label></td>
		                        <td><input name="input25" type="checkbox" value="" /></td>
		                      </tr>
		                  </table>
                </fieldset>
			</div>
		    <div id="tabAprovacao">
				<fieldset style="width:470px!important; margin-bottom:5px;">
		               	<legend>Aprovação</legend>
		                    <table width="450" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td width="23"><input name="input28" type="checkbox" value="" /></td>
		                        <td width="190">Utiliza Controle de Aprovação?</td>
		                        <td width="20">&nbsp;</td>
		                        <td width="178">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
		                    <table width="451" border="0" cellspacing="0" cellpadding="0">
		                      <tr>
		                        <td colspan="6">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="20"><input name="input8" type="checkbox" value="" /></td>
		                        <td width="143">Débitos e Créditos</td>
		                        <td width="20"><input name="input7" type="checkbox" value="" /></td>
		                        <td width="133">Negociação</td>
		                        <td width="20"><input name="input11" type="checkbox" value="" /></td>
		                        <td width="115">Ajuste de Estoque</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input10" type="checkbox" value="" /></td>
		                        <td>Postergação de Cobrança</td>
		                        <td><input name="input" type="checkbox" value="" /></td>
		                        <td>Devolução Fornecedor</td>
		                        <td><input name="input44" type="checkbox" value="" /></td>
		                        <td>Recibo</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input9" type="checkbox" value="" onchange="mostra_funcionalidades();" /></td>
		                        <td>Faltas e Sobras</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
		                    <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                      <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="input6" type="checkbox" value="" /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="input3" type="checkbox" value="" /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input30" type="checkbox" value="" /></td>
		                        <td>Falta em</td>
		                        <td><input name="input5" type="checkbox" value="" /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
		                    </table>
                </fieldset>
	            <fieldset style="width:300px!important; margin-bottom:5px;">
		              <legend>Prazo de Follow up</legend>
		           	  <table width="280" border="0" cellspacing="0" cellpadding="0">
		                <tr>
		                  <td width="240">Aviso préveio para vencímento de contratos (dias)</td>
		                  <td width="40"><input name="dateCaucao2" type="text" id="dateCaucao2" style="float:left; width:40px;" value="15" /></td>
		                </tr>
		              </table>
		              <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                	  <tr>
		                        <td colspan="4">&nbsp;</td>
		                      </tr>
		                      <tr>
		                        <td colspan="4">Para as Funcionalidades:</td>
		                      </tr>
		                      <tr>
		                        <td width="23"><input name="input6" type="checkbox" value="" /></td>
		                        <td width="123">Falta de</td>
		                        <td width="20"><input name="input3" type="checkbox" value="" /></td>
		                        <td width="108">Sobra de</td>
		                      </tr>
		                      <tr>
		                        <td><input name="input30" type="checkbox" value="" /></td>
		                        <td>Falta em</td>
		                        <td><input name="input5" type="checkbox" value="" /></td>
		                        <td>Sobra em</td>
		                      </tr>
		                      <tr>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                        <td>&nbsp;</td>
		                      </tr>
	                    </table>
                </fieldset>
                <fieldset style="width:300px!important; margin-bottom:5px;">
	               	<legend>Aviso Prévio para Validade de Garantia</legend>
		           	  <table width="280" border="0" cellspacing="0" cellpadding="0">
		                <tr>
		                  <td width="240">Aviso prévio para vencimento de garantias (dias).</td>
		                  <td width="40"><input name="dateCaucao2" type="text" id="dateCaucao2" style="float:left; width:40px;" value="15" /></td>
		                </tr>
		              </table>
		              <table width="274" border="0" cellspacing="0" cellpadding="0" class="funcionalidades" style="display:none;">
		                  <tr>
	                        <td colspan="4">&nbsp;</td>
	                      </tr>
	                      <tr>
	                        <td colspan="4">Para as Funcionalidades:</td>
	                      </tr>
	                      <tr>
	                        <td width="23"><input name="input6" type="checkbox" value="" /></td>
	                        <td width="123">Falta de</td>
	                        <td width="20"><input name="input3" type="checkbox" value="" /></td>
	                        <td width="108">Sobra de</td>
	                      </tr>
	                      <tr>
	                        <td><input name="input30" type="checkbox" value="" /></td>
	                        <td>Falta em</td>
	                        <td><input name="input5" type="checkbox" value="" /></td>
	                        <td>Sobra em</td>
	                      </tr>
	                      <tr>
	                        <td>&nbsp;</td>
	                        <td>&nbsp;</td>
	                        <td>&nbsp;</td>
	                        <td>&nbsp;</td>
	                      </tr>
	                  </table>
                </fieldset>
			</div>
	   		<br clear="all" />
		</div>
	</fieldset>
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_confirm();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Salvar</a></span>
      <div class="linha_separa_fields">&nbsp;</div>
    </div>
</div> 

</form>

</body>
</html>
