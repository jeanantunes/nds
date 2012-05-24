<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	
	<script type="text/javascript">

		$(function() {
				$( "#tabProduto" ).tabs();
		});
		
		var PesquisaProduto = {
				
			pesquisarProdutosSuccessCallBack:function() {
				
				PesquisaProduto.pesquisarBox(PesquisaProduto.getCodigoProdutoPesquisa());
				PesquisaProduto.pesquisarFornecedor(PesquisaProduto.getCodigoProdutoPesquisa());
				
				$("#dataProgramada").val("");
				
			},
			
			pesquisarProdutosErrorCallBack: function() {
					
				PesquisaProduto.pesquisarBox(PesquisaProduto.getCodigoProdutoPesquisa());
				PesquisaProduto.pesquisarFornecedor(PesquisaProduto.getCodigoProdutoPesquisa());
				
				$("#dataProgramada").val("");
				
			},

			getCodigoProdutoPesquisa: function () {
				return  "codigoProduto=" + $("#codigoProduto").val();
			},
			
			pesquisarBox:function(data) {
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarBox' />",
						   data, PesquisaProduto.montarComboBoxs);
			},		

			montarComboBoxs:function(result) {
				var comboBoxes = "<option selected='selected'  value='-1'></option>";  
				
				comboBoxes = comboBoxes + montarComboBox(result, true);
				
				$("#box").html(comboBoxes);
			}, 

			pesquisarFornecedor:function(data){
			
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor' />",
						   data, PesquisaProduto.montarComboFornecedores);
			},

			montarComboFornecedores:function(result) {
				var comboFornecedores =  montarComboBox(result, true);
				
				$("#fornecedor").html(comboFornecedores);
			},
			
			validarEdicaoSuccessCallBack : function(){
				
				 var data = [{name:"codigoProduto",value:$("#codigoProduto").val()},
	             			 {name:"numeroEdicao",value:$("#edicao").val()},
							];
				
				 $.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada' />",
						   data, function(result) {
					 $("#dataProgramada").val(result);
				 });
			},
			
			validarEdicaoErrorCallBack: function() {
				 $("#dataProgramada").val("");
			},
		};

		$(function() {
			
			inicializar();
		});
		
		function iniciarGrid() {
			$(".produtosGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'produtoDescricao',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo Produto',
					name : 'tipoProdutoDescricao',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Editor',
					name : 'nomeEditor',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'tipoContratoFornecedor',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'PEB',
					name : 'peb',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Situa&ccedil;&atilde;o',
					name : 'situacao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'AÃ§Ã£o',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255,
				singleSelect : true
			});
		}			

		function popularCombo(data, combo) {
			opcoes = "";
			selecionado = "-1";
			$.each(data, function(i,n){
				if (n["checked"]) {
					opcoes+="<option value="+n["value"]+" selected='selected'>"+n["label"]+"</option>";
					selecionado = n["value"];
				} else {
					opcoes+="<option value="+n["value"]+">"+n["label"]+"</option>";
				}
			});
			$(combo).clear().append(opcoes);
			$(combo).val(selecionado);
		}

		function inicializar() {

			iniciarGrid();
		}

		function pesquisar() {
			
			var codigo = $("#codigoProduto").val();
			var produto = $("#produto").val();
			var periodicidade = $("#periodicidade").val();
			var fornecedor = $("#fornecedor").val();
			var editor = $("#edicao").val();
			var codigoTipoProduto = $("#comboTipoProduto").val();
			
			$(".produtosGrid").flexOptions({
				url: "<c:url value='/produto/pesquisarProdutos' />",
				params: [{name:'codigo', value: codigo },
					     {name:'produto', value: produto },
					     {name:'fornecedor', value: fornecedor },
					     {name:'editor', value: editor },
					     {name:'codigoTipoProduto', value : codigoTipoProduto}],
				newp: 1,
			});
			
			$(".produtosGrid").flexReload();
		}

		function editarProduto(id) {

		}

		function removerProduto(id) {
			
			$("#dialog-excluir").dialog( {
				resizable : false,
				height : 'auto',
				width : 450,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						$.postJSON("<c:url value='/produto/removerProduto' />", 
								   "id=" + id,
								   function(result) {
								   		
								   		$("#dialog-excluir").dialog("close");
								   		
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											
											exibirMensagem(tipoMensagem, listaMensagens);
										}
												
										$(".produtosGrid").flexReload();
								   },
								   null,
								   true
						);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		}

		function novoProduto() {

			var id = null;
			$.postJSON("<c:url value='/produto/carregarDadosProduto' />", 
					   	"id=" + id,
						function (result) {

							popularCombo(result.comboTipoProduto, $("#comboTipoProduto"));
							popularCombo(result.comboEditor, $("#comboEditor"));
							popularCombo(result.comboFornecedores, $("#comboFornecedores"));
						},
					  	null,
					   	true
				);

			$("#dialog-novo").dialog({
				resizable: false,
				height:600,
				width:950,
				modal: true,
				buttons: {
					"Confirmar": function() {

						$("#dialog-novo").dialog( "close" );
				   		$(".produtosGrid").flexReload();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		}
		
		function executarPreProcessamento(resultado) {

			$.each(resultado.rows, function(index, row) {
				
				var linkAprovar = '<a href="javascript:;" onclick="editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
						     	  	'<img title="Editar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;
			});
				
			$(".grids").show();
			
			return resultado;
		}
		
	</script>

	<style>
		label { 
			vertical-align:super; 
		}
		
		#dialog-novo label { 
			width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px; 
		}
		
		.ui-tabs .ui-tabs-panel {
		   padding: 6px!important;
		}
	</style>

</head>

<body>

	<div id="dialog-excluir" title="Excluir Produto">
		<p>Confirma a exclus&atilde;o deste Produto?</p>
	</div>

	<div id="dialog-novo" title="Incluir Novo Produto">
     
		<div id="tabProduto">
			<ul>
				<li><a href="#tabProduto-1">Dados B&aacute;sicos</a></li>
				<li><a href="#tabProduto-2">Informa&ccedil;&otilde;es Adicionais</a></li>
				<li><a href="#tabProduto-3">Segmenta&ccedil;&atilde;o</a></li>
			</ul>

			<div id="tabProduto-1">
				<table width="800" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="135"><strong>Código:</strong></td>
						<td width="260"><input type="text" name="textfield" id="textfield" style="width:130px;" /></td>
						<td width="142"><strong>Produto:</strong></td>
						<td width="250"><input type="text" name="textfield2" id="textfield2" style="width:250px;" /></td>
					</tr>
					<tr>
						<td><strong>Editor:</strong></td>
						<td>
							<select name="comboEditor" id="comboEditor" style="width:210px;" >
							</select>
						</td>
						<td><strong>Fornecedor:</strong></td>
						<td>
							<select name="comboFornecedores" id="comboFornecedores" style="width:200px;" >
							</select>
						</td>
					</tr>
					<tr>
						<td><strong> Slogan do Produto:</strong></td>
						<td colspan="3"><input type="text" name="textfield" id="textfield4" style="width:657px;" /></td>
					</tr>
					<tr>
						<td><strong>Tipo de Desconto:</strong></td>
						<td>
							<select name="select6" id="select8" style="width:210px;" >
								<option value="0" selected="selected">Selecione...</option>
								<option>Importadas</option>
								<option>Promoções</option>
								<option>Especial Globo</option>
								<option>Magali Fome Zero</option>
							</select>
						</td>
						<td><strong>% Desconto:</strong></td>
						<td><input type="text" name="textfield4" id="textfield6" style="width:80px;" /></td>
					</tr>
					<tr>
						<td><strong>Tipo de Produto:</strong></td>
						<td>
							<select name="comboTipoProduto" id="comboTipoProduto" style="width:210px;" >
							</select>
						</td>
						<td><strong>Forma Comercialização:</strong></td>
						<td>
							<table width="229" border="0" cellspacing="1" cellpadding="1">
								<tr>
									<td width="21"><input type="radio" name="radio" id="radio3" value="radio" /></td>
									<td width="86">Consignado</td>
									<td width="21"><input type="radio" name="radio" id="radio4" value="radio" /></td>
									<td width="88">Conta Firme</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td><strong>PEB:</strong></td>
						<td><input type="text" name="textfield6" id="textfield8" style="width:80px;" /></td>
						<td><strong>Pacote Padrão:</strong></td>
						<td><input type="text" name="textfield7" id="textfield9" style="width:80px;" /></td>
					</tr>
				</table>
			</div> 
          
          <div id="tabProduto-2">
          <table width="890" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="508" valign="top">
    	
    	
        
        <fieldset style="width:505px!important; margin-bottom:5px;">
       	<legend>Outros</legend>
       	<table width="500" border="0" cellspacing="1" cellpadding="1">
       	  <tr>
        <td width="105">Periodicidade: </td>
        <td width="209"><select name="select12" id="select9" style="width:150px;" >
          <option selected="selected">Selecione...</option>
        </select></td>
        <td width="131">Parâmetros Abertos:</td>
        <td width="42"><input type="checkbox" name="checkbox4" id="checkbox3" /></td>
          </tr>
      <tr>
        <td>% Abrangência:</td>
        <td><input type="text" name="textfield" id="textfield" style="width:80px;" /></td>
        <td>Lancto Imediato:</td>
        <td><input type="checkbox" name="checkbox3" id="checkbox4" /></td>
      </tr>
      <tr>
        <td>Algoritmo:</td>
        <td><select name="select13" id="select13" style="width:150px;" >
          <option selected="selected">Selecione...</option>
          </select></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
        </table>
    </fieldset>
   
   	
	<fieldset style="width:505px!important; float:left; margin-bottom:5px;">
       	<legend>Seleção</legend>
        	<table width="500" border="0" cellspacing="1" cellpadding="1">
        	  <tr>
        	    <td width="116">Combinação 1:</td>
        	    <td width="20"><input type="checkbox" name="checkbox5" id="checkbox5" /></td>
        	    <td width="349"><select name="select6" id="select6" style="width:330px;" >
        	      <option selected="selected">Selecione...</option>
      	      </select></td>
       	      </tr>
        	  <tr>
        	    <td><table width="115" border="0" cellspacing="1" cellpadding="1">
        	      <tr>
        	        <td width="20"><input type="radio" name="radio" id="radio3" value="radio" /></td>
        	        <td width="32">E</td>
        	        <td width="20"><input type="radio" name="radio" id="radio4" value="radio" /></td>
        	        <td width="30">Ou</td>
      	        </tr>
      	      </table></td>
        	    <td><input type="checkbox" name="checkbox" id="checkbox" /></td>
        	    <td><select name="select7" id="select7" style="width:330px;" >
        	      <option>Combinação 2</option>
                </select></td>
       	      </tr>
        	  <tr>
        	    <td><table width="115" border="0" cellspacing="1" cellpadding="1">
        	      <tr>
        	        <td width="20"><input type="radio" name="radio" id="radio15" value="radio" /></td>
        	        <td width="32">E</td>
        	        <td width="20"><input type="radio" name="radio" id="radio16" value="radio" /></td>
        	        <td width="30">Ou</td>
      	        </tr>
      	      </table></td>
        	    <td><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
        	    <td><select name="select8" id="select8" style="width:330px;" >
        	      <option>Combinação 3</option>
                </select></td>
       	      </tr>
   	      </table>
    </fieldset>
    
    <fieldset style="width:505px!important; float:left; margin-bottom:5px;">
       	<legend>Características</legend>
       	<table width="500" border="0" cellspacing="1" cellpadding="1">
       	  <tr>
       	    <td width="94" height="24">Comprimento:</td>
       	    <td width="165"><input type="text" name="textfield2" id="textfield2" style="width:80px;" /></td>
       	    <td width="54">Largura:</td>
       	    <td width="174"><input type="text" name="textfield6" id="textfield6" style="width:80px;" /></td>
   	      </tr>
       	  <tr>
       	    <td height="24">Espessura:</td>
       	    <td><input type="text" name="textfield3" id="textfield4" style="width:80px;" /></td>
       	    <td>Peso:</td>
       	    <td><input type="text" name="textfield7" id="textfield7" style="width:80px;" /></td>
     	    </tr>
       	  </table>
    </fieldset>
        
	<br clear="all" />
	<br clear="all" />
    
    </td>
    <td width="20" style="width:20px;">
    <td width="362" valign="top">
    
    <fieldset style="width:323px!important; float:left; margin-bottom:5px; margin-right:0px;">
       	<legend>Tributação Fiscal</legend>
       	<table width="229" border="0" cellpadding="1" cellspacing="1">
       	  <tr>
       	    <td width="20"><input type="radio" name="radio" id="radio17" value="radio" /></td>
       	    <td width="59">Tributado</td>
       	    <td width="20"><input type="radio" name="radio" id="radio18" value="radio" /></td>
       	    <td width="37">Isento</td>
       	    <td width="20"><input type="radio" name="radio" id="radio18" value="radio" /></td>
       	    <td width="81"> Outros</td>
   	      </tr>
   	    </table>
    </fieldset>
    <br clear="all" />
    
    <fieldset style="width:323px!important; float:left; margin-bottom:5px;">
       	<legend>Situação Tributária</legend>
       	<table width="294" border="0" cellspacing="1" cellpadding="1">
       	  <tr>
        <td width="20"><input type="radio" name="radio" id="radio13" value="radio" /></td>
        <td width="267">Produto Nacional Isento ICMS/IPI</td>
        </tr>
      <tr>
        <td><input type="radio" name="radio" id="radio19" value="radio" /></td>
        <td>Produto Nacional Imune ICMS/IPI </td>
        </tr>
      <tr>
        <td><input type="radio" name="radio" id="radio20" value="radio" /></td>
        <td>Outros</td>
        </tr>
      </table>
    </fieldset>

    <br clear="all" />
    <fieldset style="width:323px!important; float:left; margin-bottom:5px; margin-right:0px!important;">
       	<legend>Regime de Recolhimento: Normal</legend>
       	<table width="319" border="0" cellspacing="1" cellpadding="1">
       	  <tr>
       	    <td width="160" height="24">Classe Histograma Analítico</td>
       	    <td width="152"><select name="select9" id="select10" style="width:150px;" >
       	      <option selected="selected">Selecione...</option>
   	        </select></td>
   	      </tr>
       	  <tr>
       	    <td height="24">% Limite  Cota Fixação:</td>
       	    <td><input type="text" name="textfield3" id="textfield4" style="width:80px;" /></td>
   	      </tr>
       	  <tr>
       	    <td height="24">% Limite Reparte Fixação:</td>
       	    <td><input type="text" name="textfield4" id="textfield5" style="width:80px;" /></td>
   	      </tr>
   	    </table>
    </fieldset>
    
    <br clear="all" />
    
    <fieldset style="width:323px!important; float:left; margin-bottom:5px; margin-right:0px!important;">
    <table width="338" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="109">Grupo Editorial:</td>
    <td width="207"><select name="select10" id="select11" style="width:200px;" >
      <option selected="selected">Selecione...</option>
    </select></td>
  </tr>
  <tr>
    <td>SubGrupo Editorial:</td>
    <td><select name="select11" id="select12" style="width:200px;" >
      <option selected="selected">Selecione...</option>
    </select></td>
  </tr>
</table>
    </fieldset>

    
    
    
    
    </td>
  </tr>
</table>
          
          
          </div>
          <div id="tabProduto-3">
          <fieldset style="width:880px!important; margin:auto auto!important;">
          <legend>Público Alvo</legend>
          <table width="786" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="402" valign="top">
            <table width="400" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="112"><strong>Classe Social:</strong></td>
            <td width="281"><select name="select12" id="select13" style="width:250px;" >
              <option selected="selected">Selecione...</option>
              </select></td>
          </tr>
          <tr>
            <td><strong>Sexo:</strong></td>
            <td><select name="select13" id="select14" style="width:250px;" >
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
          <tr>
            <td><strong>Faixa-Etária:</strong></td>
            <td><select name="select14" id="select15" style="width:250px;" >
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
        </table>

    
    </td>
    <td width="34" style="width:34px;">&nbsp;</td>
    <td width="344" valign="top">
    
            <table width="344" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="137"><strong>Forma Física:</strong></td>
            <td width="200"><select name="select12" id="select13" style="width:200px;" >
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
          <tr>
            <td><strong>Lançamento:</strong></td>
            <td><select name="select13" id="select14" style="width:200px;" >
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
          <tr>
            <td><strong>Tema Principal:</strong></td>
            <td><select name="select14" id="select15" style="width:200px;" >
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
          <tr>
            <td><strong>Tema Secundário:</strong></td>
            <td><select name="select15" id="select16" style="width:200px;" >
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
        </table>
        
        </td>
  </tr>
</table>
	</fieldset>
 <br clear="all" />
 </div>
          
     </div>
</div>


	<fieldset class="classFieldset">
		<legend> Pesquisar Produtos</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="43">C&oacute;digo:</td>
				<td width="123" >
					
			    	<input type="text" name="codigoProduto" id="codigoProduto"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   PesquisaProduto.pesquisarProdutosSuccessCallBack,
								   									   PesquisaProduto.pesquisarProdutosErrorCallBack);" />
				</td>
				
				<td width="55">Produto:</td>
				<td width="237">
					<input type="text" name="produto" id="produto" style="width: 222px;" maxlength="255"
					       onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
														    	   PesquisaProduto.pesquisarProdutosSuccessCallBack,
														    	   PesquisaProduto.pesquisarProdutosErrorCallBack);"/>
				</td>
				<td width="99">Fornecedor:</td>
				<td width="251"><input type="text" id="fornecedor" style="width:200px;"/></td>
				<td width="106">&nbsp;</td>
			</tr>
			<tr>
				<td>Editor:</td>
				<td colspan="3" >
					<input type="text" style="width:410px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
							   onchange="produto.validarNumEdicao('#codigoProduto', '#edicao', false,
							   										PesquisaProduto.validarEdicaoSuccessCallBack,
						    	   									PesquisaProduto.validarEdicaoErrorCallBack);"/>
				</td>
				<td>Tipo de Produto:</td>
				<td>
					<select id="comboTipoProduto" style="width:207px;">
						<option value="-1"></option>
						<c:forEach items="${listaTipoProduto}" var="tipoProduto" >
							<option value="${tipoProduto.id}">${tipoProduto.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<span class="bt_pesquisar">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
		<legend>Produtos Cadastrados</legend>
			<div class="grids" style="display:none;">
				<table class="produtosGrid"></table>
			</div>
	
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="novoProduto();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo
			</a>
		</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>