<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/roteirizacao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<style>
label{ vertical-align:super;}
.linha_separa_fields{width:880px!important;}
#dialog-roteirizacao fieldset{margin-right:0px!important; margin-bottom:2px;}
#dialog-roteiro fieldset, #dialog-rota fieldset, #dialog-excluir fieldset, #dialog-transfere-cotas fieldset, #dialog-transfere-rota fieldset{width:350px!important;}
</style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">

<form id="form-transfere-rota">
<div id="dialog-transfere-rota" title="Transferir Rotas" style="display:none;">
	<fieldset>
    	<legend>Transferir Rotas para:</legend>
        <p>Pesquise ou Digite o nome de um Novo Roteiro para estas Rotas.</p>
        <input name="roteiroTranferenciaNome" type="text" id="roteiroTranferenciaNome" onkeyup="roteirizacao.autoCompletarRoteiroPorNome('#roteiroTranferenciaNome',roteirizacao.selecionaRoteiroTranferencia)" style="width:300px; float:left; margin-bottom:5px;" />
        <input name="roteiroTranferenciaSelecionadoId" type="hidden" id="roteiroTranferenciaSelecionado"  />
        <input name="roteiroTranferenciaSelecionadoNome" type="hidden" id="roteiroTranferenciaSelecionadoNome"  />
        <a href="javascript:;" onclick="roteirizacao.exibiRoteiroNovoTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
        <br clear="all" />
        <div class="roteiroNovo" style="display:none;">
        <a href="javascript:;" onclick="roteirizacao.escondeRoteiroNovoTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" border="0" align="right" /></a>
        <br clear="all" />
        <label>Box:</label>
        <select name="boxRoteiroTranferencia" id="boxRoteiroTranferencia" style="width:230px;  float:left; margin-bottom:5px;">
            <option>Selecione...</option>
           <c:forEach var="box" items="${listaBox}">
				<option value="${box.key}">${box.value}</option>
			</c:forEach>
        </select>
        <br clear="all" />

        <label>Ordem:</label>
        <input name="ordemRoteiroTranferencia" id="ordemRoteiroTranferencia" type="text" style="width:225px; float:left; margin-bottom:5px;" />    
        <br clear="all" />
        <label>Roteiro Especial:</label>
        <input type="checkbox" name="tipoRoteiroTranferencia" value="Especial" id="tipoRoteiroTranferencia" onclick="roteirizacao.roteiroEspecial()"  />
        <br clear="all" />  
</div>
    </fieldset>
</div>
</form>

<form id="form-transfere-cotas">
<div id="dialog-transfere-cotas" title="Transferir Cotas" style="display:none;">
	<fieldset>
    	<legend>Transferir Cotas para:</legend>
        <p>Pesquise ou Digite o nome de uma Nova Rota para estas Cotas.</p>
        <input name="lstRotaTranferencia" type="text" id="lstRotaTranferencia"  onkeyup="roteirizacao.autoCompletarRotaPorNome('#lstRotaTranferencia')" style="width:300px; float:left; margin-bottom:5px;" />
        <a href="javascript:;"   onclick="roteirizacao.exibiRotaNovaTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
        <div class="rotaNovaTransferencia" style="display:none;">
	        <a href="javascript:;" onclick="roteirizacao.escondeRotaNovaTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" border="0" align="right" /></a>
	        <br clear="all" />
	        <label>Ordem:</label>
	        <input name="ordemRotaTranferencia" id="ordemRotaTranferencia" type="text" style="width:225px; float:left; margin-bottom:5px;" />    
        <br clear="all" />  
</div>
    </fieldset>
</div>
</form>

<form id="form-excluir-rotas">
<div id="dialog-excluir-rotas" title="Rotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclus&atilde;o destas Rotas deste Roteiro</p>
    </fieldset>
</div>
</form>

<form id="form-excluir-cotas">
<div id="dialog-excluir-cotas" title="Cotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclus&atilde;o destas Cotas desta Rota?</p>
    </fieldset>
</div>
</form>

<form id="form-rota">
<div id="dialog-rota" title="Rota" style="display:none;">
	<fieldset>
    	<legend>Nova Rota</legend>
        <label>Ordem:</label>
        <input name="ordemRotaInclusao"  id="ordemRotaInclusao"  type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="nomeRotaInclusao" id="nomeRotaInclusao" type="text" style="width:200px; float:left;" />       
        <br clear="all" />
        

    </fieldset>
</div>
</form>


<form id="form-roteiro">
<div id="dialog-roteiro" title="Roteiro" style="display:none;">
<jsp:include page="../messagesDialog.jsp" /> 
	<fieldset>
    	<legend>Novo Roteiro</legend>
        <label>Box:</label>
        <select name="boxInclusaoRoteiro" id="boxInclusaoRoteiro" style="width:200px;  float:left; margin-bottom:5px;">
			<option value="" selected="selected">Selecione...</option>
			<c:forEach var="box" items="${listaBox}">
				<option value="${box.key}">${box.value}</option>
			</c:forEach>
 		</select>
        
        <br clear="all" />
        <label>Ordem:</label>
        <input name="ordemInclusaoRoteiro" id="ordemInclusaoRoteiro" type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="nomeInclusaoRoteiro" id="nomeInclusaoRoteiro"  type="text" style="width:200px; float:left;" />
         <br clear="all" />
        <label>Roteiro Especial:</label>
        <input type="checkbox" name="tipoRoteiro" value="Especial" id="tipoRoteiro" onclick="roteirizacao.roteiroEspecialNovo()"  />        
        <br clear="all" />
        

    </fieldset>
</div>
</form>

<form id="form-roteirizacao">
<div id="dialog-roteirizacao" title="Nova Roteiriza&ccedil;&atilde;o" style="display:none;">
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogRoteirizacao" name="messageDialog"/>
	</jsp:include> 
     <fieldset style="width:895px; float:left; margin-bottom:10px;">
		<legend>Roteiros</legend>
        <input name="lstRoteiros" type="text" id="lstRoteiros" style="width:240px; float:left;"  onkeyup="roteirizacao.autoCompletarRoteiroPorNome('#lstRoteiros',roteirizacao.populaDadosRoteiro)" onblur="roteirizacao.buscaRoteiroPorNome('#lstRoteiros')" />
	<span class="bt_novos" title="Nova Roteiro"><a href="javascript:;" onclick="roteirizacao.abrirTelaRoteiro();" rel="tipsy" title="Novo Roteiro"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/></a></span>
    <span style="float:right; margin-top:12px; margin-left:20px;" id="spanDadosRoteiro"><strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;</span>
   <input type="hidden" id="idRoteiroSelecionado" name="idRoteiroSelecionado"  >   
    </fieldset>
	<br clear="all" />
	<fieldset style="width:270px; float:left; margin-right:17px!important;">
		<legend>Rotas</legend>
        

        <input name="nomeRota" type="text" id="nomeRota" readonly="readonly" style="width:210px; float:left; margin-bottom:5px;" onkeyup="roteirizacao.filtroGridRotasPorNome()"  />
        <a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40);" id="botaoNovaRotaNome" rel="tipsy" title="Nova Rota" >
        	<img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" />
        </a> 
        <br />
        <table class="rotasGrid"></table>
		<span class="bt_novos" title="Nova Rota"><a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40);" id="botaoNovaRota" ><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Nova</a></span>
        <span class="bt_novos" title="Transfer&ecirc;ncia de Rota"><a href="javascript:;"  style="cursor:default; opacity:0.4; filter:alpha(opacity=40);" id="botaoTransfereciaRota"><img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/>Transferir</a></span>
        <span class="bt_novos" title="Excluir Rota"><a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40);" id="botaoExcluirRota"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0"/>Excluir</a></span>
    </fieldset>
    
    
    
    <fieldset style="width:596px; float:left; overflow:hidden;">
		<legend>Cotas da Rota </legend>
		
        <span style="float:left; margin-bottom:10px; margin-left:3px; margin-top:5px;" id="spanDadosRota"><strong> Rota Selecionada:</strong>&nbsp;&nbsp;&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;</span>
        <input name="rotaSelecionada" type="hidden"  id="rotaSelecionada" style="width:240px; float:left; margin-bottom:5px;"  />
        
		<table class="cotasRotaGrid"></table>
        <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" id="botaoCotaAusentes"  style="cursor:default; opacity:0.4; filter:alpha(opacity=40);"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"/> Adicionar</a></span>
        <span class="bt_novos" title="Transfer&ecirc;ncia de Roteiro"><a href="javascript:;" id="botaoTransferenciaRoteiro"  style="cursor:default; opacity:0.4; filter:alpha(opacity=40);"><img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/> Transferir</a></span>
        <span class="bt_novos" title="Excluir Roteiriza&ccedil;&atilde;o"><a href="javascript:;"  id="botaoExcluirRoteirizacao"  style="cursor:default; opacity:0.4; filter:alpha(opacity=40);"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0"/> Excluir</a></span>
	</fieldset>
    
    
    
    
	<div class="linha_separa_fields">&nbsp;</div>
</div>
</form>



<!--<div id="dialog-roteirizacao" title="Nova Roteiriza&ccedil;&atilde;o" style="display:none;">
     <fieldset style="width:430px; float:left;">
		<legend>Roteiros</legend>
		<table class="roteirosGrid"></table>
        <span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="popup_roteiro();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo Roteiro</a></span>
        
	</fieldset>

	<fieldset style="width:430px; float:left; margin-left:15px;">
		<legend>Rotas</legend>
        <table class="rotasGrid"></table>
		<span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="popup_rota();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Nova Rota</a></span>
    </fieldset>
    
    
    
    <fieldset style="width:895px; float:left; margin-top:5px;">
		<legend>Cotas da Rota</legend>
		<table class="cotasRotaGrid"></table>
        <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_cotas_ausentes();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Incluir Cota</a></span>
        
	</fieldset>
    
	<div class="linha_separa_fields">&nbsp;</div>
</div>-->

	<form id="form-cotas-disponiveis">
	<div id="dialog-cotas-disponiveis" title="Cotas Dispon&icirc;veis" style="display:none;">
    
   <jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogRoteirizacaoCotaDisponivel" name="messageDialog"/>
	</jsp:include> 
	
	
    <fieldset style="width:800px; float:left;">
		<legend>Pesquisar Cotas</legend>
		<table width="800" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="42">Cota:</td>
              <td width="170"><input name="numeroCotaPesquisa" type="text" id="numeroCotaPesquisa" style="width:80px; float:left; margin-right:5px;" />
              <span class="classPesquisar"><a href="javascript:;" onclick="roteirizacao.pesquisarPvsPorCota();">&nbsp;</a></span></td>
              <td width="41">Nome:</td>
              <td colspan="4"><span  id="cotaDisponivelPesquisa"> &nbsp;</span></td>
            </tr>
            <tr>
              <td>UF:</td>
              <td><select name="comboUf" id="comboUf" onchange="roteirizacao.buscalistaMunicipio()" style="width:100px;">
                
              </select></td>
              <td>Munic.</td>
              <td><select name="comboMunicipio" id="comboMunicipio" onchange="roteirizacao.buscalistaBairro()" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td>Bairro:</td>
              <td width="168"><select name="comboBairro" id="comboBairro" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td width="36">CEP:</td>
              <td width="87"><input name="cepPesquisa" type="text" id="cepPesquisa" style="width:80px;" /></td>
              <td width="79"><span class="bt_novos"><a href="javascript:;" onclick="roteirizacao.buscarPvsPorEndereco();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>
	</fieldset>
    
    <fieldset style="width:800px; float:left; margin-top:5px;">
		<legend>Cotas Dispon&icirc;veis</legend>
		<table class="cotasDisponiveisGrid"></table>
        <table width="121" border="0" align="right" cellpadding="0" cellspacing="0">
		  <tr>
		    <td width="151" align="right"><label for="selecionaTodos" id="textoCheckAllCotas" >Marcar todos</label></td>
		    <td width="31"><input type="checkbox" name="selecionaTodos" id="selecionaTodos" onclick="roteirizacao.checarTodasCotasGrid();"/></td>
		  </tr>
		</table>
        
	</fieldset>
	<br clear="all" />
	</div>
	</form>


    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Roteiriza&ccedil;&atilde;o < evento > com < status >.</b></p>
	</div>
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos" title="Roteiriza&ccedil;&atilde;o"><a href="javascript:;" onclick="roteirizacao.popupRoteirizacao();" rel="tipsy" title="Incluir Nova Roteirização"><img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0"/></a></span>
			<span class="bt_arq" title="Gerar Arquivo"><a href="javascript:;" onclick="roteirizacao.gerarArquivoRoteirizacao('XLS');" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
    		<span class="bt_arq" title="Imprimir"><a href="javascript:;" onclick="roteirizacao.imprimirArquivo('PDF');" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>
		</div>
	</div>
    <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
   	    <legend> Pesquisar Rota / Roteiro</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="33">Box:</td>
              <td width="120">
	               <select name="boxPesquisa" id="boxPesquisa"   onchange="roteirizacao.carregarComboRoteiro()" style="width: 100px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="box" items="${listaBox}">
							<option value="${box.key}">${box.value}</option>
						</c:forEach>
				  </select>
              </td>
                <td width="54">Roteiro:</td>
                <td width="209">
                	<select name="roteiroPesquisa" id="roteiroPesquisa"  onchange="roteirizacao.carregarComboRota()" style="width: 200px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="roteiro" items="${listaRoteiro}">
							<option value="${roteiro.key}">${roteiro.value}</option>
						</c:forEach>
				    </select>
                 
                </td>
                <td width="54">Rota:</td>
                <td width="207">
					<select name="rotaPesquisa" id="rotaPesquisa" style="width: 190px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="rota" items="${listaRota}">
							<option value="${rota.key}">${rota.value}</option>
						</c:forEach>
				    </select>
                </td>
              <td width="237">&nbsp;</td>
            </tr>
            <tr>
              <td>Cota:</td>
              <td><input name="cotaPesquisa" type="text" id="cotaPesquisa" style="width:80px; float:left; margin-right:5px;" />
              <span class="classPesquisar"><a href="javascript:;" onclick="roteirizacao.pesquisarRoteirizacaoPorCota();">&nbsp;</a></span></td>
              <td>Nome:</td>
              <td><span id="nomeCotaPesquisa">&nbsp;&nbsp;&nbsp;</span></td>
              <td>&nbsp;</td>
              <td>&nbsp;
              	<label>Roteiro Especial:</label>
        		<input type="checkbox" name="tipoRoteiroPesquisa" value="Especial" id="tipoRoteiroPesquisa" onclick="roteirizacao.pesquisaComRoteiroEspecial()" />
        	 </td>	
              <td><span class="bt_novos"><a href="javascript:;" onclick="roteirizacao.pesquisarRoteirizacao();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:none;">
	  
	      <fieldset class="fieldGrid">
	      	
	      	<legend>Rotas / Roteiros Cadastrados</legend>
	       	  	
	       	<div class="gridWrapper">
	        
	        	<table class="rotaRoteirosGrid"></table>
	        
	        </div>
	        	
	      </fieldset>
      </div>
      
      <div id="dialog-detalhes" style="width: auto; min-height: 60.40000009536743px; height: auto; " class="ui-dialog-content ui-widget-content" scrolltop="0" scrollleft="0">
		<fieldset>
	    	<legend id="legendDetalhesCota">Box-Roteiro-Rota</legend>
	        
	        <table id="cotasGrid" width="347" border="0" cellspacing="1" cellpadding="1"> </table>
	
	  </fieldset>
	</div>
      
      
<script>
	
	

/* 	$(".roteirosGrid").flexigrid({
			url : '../xml/roteiros-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'C�d.',
				name : 'codigo',
				width : 35,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'box',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'detalhes',
				width : 15,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			width : 430,
			height : 130
		});


	$(".rotaRoteirosGrid").flexigrid({
			url : '../xml/rota_roteiros-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Box',
				name : 'box',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiro',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nome',
				width : 360,
				sortable : true,
				align : 'left'
			}],
			sortname : "box",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		}); */
</script>
</form>

</body>
