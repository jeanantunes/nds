<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/tipoMovimento.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">

$(function() {
	definirAcaoPesquisaTeclaEnter();
});

var TM = new TipoMovimento('${pageContext.request.contextPath}', 'TM', BaseController.workspace);
TM.init();
	
</script>
</head>

<body>

<form id="form-excluir">
<div id="dialog-excluir" title="Excluir Tipo de Movimento">
  <p>Confirma a exclusão deste Tipo de Movimento?</p>
</div>
</form>




<form id="form-novo">
<div id="dialog-novo" title="Incluir Tipo de Movimento">
	
		
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-novo" name="messageDialog"/>
	</jsp:include>
     
     <fieldset>
		<legend>Tipo de Movimento</legend>
	    <table width="400" border="0" cellpadding="2" cellspacing="2">
	            <tr>
	              <td width="109">Código:</td>
	              <td width="280">
	              
	<!-- CODIGO -->
	<input id="codigoModal"  type="text" name="textfield2" style="width:87px;" disabled="disabled"/></td>
	
	
	            </tr>
	            <tr>
	              <td>Descrição:</td>
	              <td>
	
	<!-- DESCRICAO -->
	<input id="descricaoModal" type="text" name="textfield7"  style="width:250px;"/></td>
	            
	            </tr>
	            <tr>
	              <td>Grupo Operação:</td>
	              <td>
	
	<!-- GRUPO OPERACAO -->
	 <select id="grupoOperacaoModal" onchange="TM.atualizarCombosPorGrupoOperacao($(this).val());" style="width:100px;"> 
	    <option value="ESTOQUE">Estoque</option>	
	   	<option value="FINANCEIRO">Financeiro</option>	
	</select>
	              
	             </td>
	            </tr>
	            <tr>
	              <td>Operação:</td>
	              <td>
	
	<!-- OPERACAO -->
	<select id="operacaoModal" style="width:100px;"> 
		<option value="DEBITO">Débito</option>
		<option value="CREDITO">Crédito</option>
	</select>
	            
	              
	              </td>
	            </tr>
	            <tr>
	              <td>Aprovação:</td>
	              <td>
	
	<!-- APROVACAO -->
	<select id="aprovacaoModal" style="width:100px;"> 
	    <option value="SIM">Sim</option>
	    <option value="NAO">Não</option>
	</select>
	
	              
	              </td>
	            </tr>
	            <tr>
	              <td>Incide na Dívida:</td>
	              <td>
	<!-- INCIDE DIVIDA -->              
	<select id="incideDividaModal" style="width:100px;"> 
	      <option value="SIM">Sim</option>
	      <option value="NAO">Não</option>
	</select>
	              
	              </td>
	            </tr>
	          </table>
    </fieldset>
</div>
</form>
   
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Tipo de Movimento < evento > com < status >.</b></p>
	</div>
      <div class="areaBts">
      	<div class="area">
      		<span class="bt_novos" title="Novo">
				<!-- NOVO -->
				<a href="javascript:;" onclick="TM.carregarNovo();" rel="tipsy" title="Incluir Novo Tipo de Movimento"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/></a>
			</span>
      		
      	</div>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
   	    <legend> Pesquisar Tipos de Movimento</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="52">Código:</td>
              <td width="95">
              
<!-- CÓDIGO -->
<input id="codigo" class="campoDePesquisa" type="text" name="textfield3"  style="width:80px;"/></td>

              <td width="122">Tipo de Movimento:</td>
              <td width="221">
              
<!-- DESCRICAO -->
<input id="descricao" class="campoDePesquisa" type="text" name="textfield" style="width:200px;"/></td>

               <td width="434">
	              	<span class="bt_novos">
						<!-- PESQUISAR -->
						<a class="botaoPesquisar" href="javascript:;" onclick="TM.cliquePesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					</span>
				</td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldGrid">
       	  <legend>Tipos de Movimento Cadastrados</legend>
        <div class="grids" style="display:none;">
       	  <table class="movimentosGrid"></table>
        </div>

 
           
      </fieldset>

</body>
</html>
