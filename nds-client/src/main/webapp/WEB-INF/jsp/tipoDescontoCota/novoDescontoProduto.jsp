
<script>

var DESCONTO_PRODUTO = {
		
	popup_produto:function() {

		$( "#dialog-produto" ).dialog({
			resizable: false,
			height:320,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					DESCONTO_PRODUTO.novoDescontoProduto();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},

	novoDescontoProduto:function() {
		
		var codigo = $("#codigo").val();
		var produto = $("#produto").val();
		var edicaoProduto = $("#edicaoProduto").val();
		var descontoProduto = $("#descontoProduto").val();
		var dataAlteracaoProduto = $("#dataAlteracaoProduto").val();
		var usuarioProduto = $("#usuarioProduto").val()
		
		$.postJSON("<c:url value='/administracao/tipoDescontoCota/novoDescontoProduto'/>",
				   "codigo="+codigo+
				   "&produto="+ produto +
				   "&edicaoProduto="+ edicaoProduto +
				   "&descontoProduto="+ descontoProduto +
				   "&dataAlteracaoProduto="+ dataAlteracaoProduto +
				   "&usuarioProduto="+ usuarioProduto,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	                   pesquisar();
	               },
				   null,
				   true);
		$(".tiposDescEspecificoGrid").flexReload();		
	}

};

</script>

<div id="dialog-produto" title="Novo Tipo de Desconto Produto" style="display:none;">    
    <table width="394" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="100">Código:</td>
              <td width="283"><input type="text" name="textfield22" id="textfield22"  style="width:100px; float:left; margin-right:5px;" readonly="readonly" /><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
            </tr>
            <tr>
              <td>Produto:</td>
              <td><input type="text" name="textfield4" id="textfield4" style="width:230px;" value="" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Edição:</td>
              <td><input type="checkbox" name="checkbox" id="checkbox" onchange="mostraEdicao();" /></td>
            </tr>
            <tr>
              <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="aEdicao" style="display:none;">
                <tr>
                    <td width="27%">Edição Específica:</td>
                    <td width="18%"><input type="text" name="textfield5" id="textfield5" style="width:60px; margin-left:2px"/></td>
                    <td width="12%">ou por </td>
                    <td width="18%"><input type="text" name="textfield6" id="textfield6" style="width:60px;"/></td>
                    <td width="25%">Edições</td>
                </tr>
              </table></td>
            </tr>
            <tr>
              <td>Desconto %:</td>
              <td><input type="text" name="textfield2" id="textfield2" style="width:100px;"/></td>
            </tr>
            <tr>
              <td>Cotas:</td>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="10%"><input type="radio" name="cotas" id="cotas" value="radio" onchange="escondeCota();" /></td>
                  <td width="29%">Todas</td>
                  <td width="8%"><input type="radio" name="cotas" id="cotas" value="radio" onchange="mostraCota();" /></td>
                  <td width="53%">Específica</td>
                </tr>
              </table></td>
            </tr>
            <tr>
              <td colspan="2"><table width="100%" border="0" cellspacing="1" cellpadding="1" class="especificaCota" style="display:none;">
                <tr class="header_table">
                  <td width="34%">Cota</td>
                  <td width="66%">Nome</td>
                </tr>
                <tr class="class_linha_1">
                  <td><input type="text" name="textfield22" id="textfield22"  style="width:80px; float:left; margin-right:5px;" readonly="readonly" /><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
                  <td><input type="text" name="textfield" id="textfield"  style="width:200px;" /></td>
                </tr>
                <tr class="class_linha_2">
                  <td><input type="text" name="textfield" id="textfield3"  style="width:80px; float:left; margin-right:5px;" readonly="readonly" />
                    <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
                  <td><input type="text" name="textfield" id="textfield7"  style="width:200px;" /></td>
                </tr>
                <tr class="class_linha_1">
                  <td><input type="text" name="textfield" id="textfield8"  style="width:80px; float:left; margin-right:5px;" readonly="readonly" />
                    <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
                  <td><input type="text" name="textfield" id="textfield9"  style="width:200px;" /></td>
                </tr>
                <tr class="class_linha_2">
                  <td><input type="text" name="textfield" id="textfield10"  style="width:80px; float:left; margin-right:5px;" readonly="readonly" />
                    <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
                  <td><input type="text" name="textfield" id="textfield11"  style="width:200px;" /></td>
                </tr>
              </table></td>
            </tr>
            <tr>
              <td colspan="2">Este desconto predomina sobre os demais (geral / Específico)?
              <input type="checkbox" name="checkbox2" id="checkbox2" /></td>
            </tr>
          </table>       

    </div>
