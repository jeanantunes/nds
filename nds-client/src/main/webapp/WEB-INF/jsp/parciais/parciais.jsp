<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parciais.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript">
	var pesquisaProdutoParciais = new PesquisaProduto(ParciaisController.workspace);
</script>
		
<style type="text/css">
#dialog-detalhes fieldset{width:880px!important;}

#dialog-detalhes fieldset ul{}

#dialog-detalhes fieldset li{float:left; margin-right:10px; margin-left:0px; margin-bottom:5px; line-height:20px;}
#dialog-detalhes .flexigrid{width:880px!important;}
#dialog-novo fieldset{width:250px!important;}

#dialog-edit-produto, #dialog-detalhe-venda, #dialog-novo-manual{display:none;}

</style>

<form id="idParciaisEditProduto">

	<div id="dialog-edit-produto" title="Dados do Produto">
		
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-edit-produto" name="messageDialog"/>
	</jsp:include>

	<table width="412" border="0" cellpadding="2" cellspacing="1" class="filtro">
		<tr>
	    	<td width="129">Código:</td>
	        <td width="272">
				<input id="parcial-codigoProdutoEd" name="textfield2" type="text" style="width:80px;" value="" disabled="disabled" />
			</td>
		</tr>
	    <tr>
	    	<td>Produto:</td>
	        <td>
				<input id="parcial-nomeProdutoEd" name="textfield7" type="text" style="width:250px;" value="" disabled="disabled"/>
			</td>
		</tr>
        <tr>
          <td>Edição:</td>
          <td><input id="parcial-numEdicaoEd" name="textfield" type="text" style="width:80px;" value="" disabled="disabled"/></td>

        </tr>
        <tr>
        	<td>Preço Capa R$:</td>
          	<td>
          		<input id="parcial-precoCapaEd" name="textfield3" type="text" style="width:80px; text-align:right" value="" disabled="disabled"/>
          	</td>
        </tr>
        <tr>
        	<td>Fornecedor:</td>
          	<td>
				<input id="parcial-nomeFornecedorEd" name="textfield4" type="text" style="width:250px;" value="" disabled="disabled"/>
			</td>
        </tr>
        <tr>
     	<td>Data Lançamento:</td>
          	<td>
				<input id="parcial-dataLancamentoEd" type="text" style="width:80px;" />
 			</td>
        </tr>
        <tr>
       		<td>Data Recolhimento:</td>
          	<td>
				<input id="parcial-dataRecolhimentoEd" type="text"  style="width:80px;" />
 			</td>
        </tr>
	</table>
</div>
</form>

<form id="idParciaisExcluir">
	<div id="dialog-excluir" title="Excluir Parcial">
	  <p>Confirma a exclusão deste Período?</p>
	</div>
</form>

<form id="idParciaisDiaUtil">
	<div id="dialog-edit-lancamento-dia-util" title="Parcial">
	  <p><span id="mensagemAlertaDiaUtil"></span></p>
	</div>
</form>



<form id="idParciaisDetalhes">
	
	<div id="dialog-detalhes" title="Parcial">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-detalhes" name="messageDialog"/>
	</jsp:include>
	
	<fieldset>
		<legend>Dados da Parcial</legend>
	    <table width="740" border="0" cellpadding="2" cellspacing="1">
	            <tr>
	              <td width="81"><strong>Código:</strong></td>
	              <td width="94" id="parcial-codigoProdutoM" ></td>
	              <td width="132"><strong>Produto:</strong></td>
	              <td width="194" id="parcial-nomeProdutoM"></td>
	              <td width="81"><strong>Edição:</strong></td>
	              <td width="127" id="parcial-numEdicaoM">4345</td>
	            </tr>
	            <tr>
	              <td><strong>Fornecedor:</strong></td>
	              <td id="parcial-nomeFornecedorM"></td>
	              <td><strong>Data Lançamento:</strong></td>
	              <td id="parcial-dataLancamentoM"></td>
	              <td><strong>Data Final:</strong></td>
	              <td id="parcial-dataRecolhimentoM"></td>
	            </tr>
	          </table>
	          
	</fieldset>
	<br />
	<br clear="all" />
	<br />
	
	<fieldset style="margin-bottom:5px;">
		<legend>Parciais</legend>
    	<table class="parcial-parciaisPopGrid"></table>
	</fieldset>
	<br />
	<div id="exportacaoPeriodosModal">
		<span class="bt_novos">
			<!-- ARQUIVO EXCEL -->
			<a href="${pageContext.request.contextPath}/parciais/exportarPeriodos?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			</a>
		</span>
		
		<span class="bt_novos">
			<!-- IMPRIMIR PDF -->	
			<a href="${pageContext.request.contextPath}/parciais/exportarPeriodos?fileType=PDF" rel="tipsy" title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
			</a>
		</span>
		</div>   
		<span id="btnNovoPeriodoManualModal" class="bt_novos" title="Novo">
 			<a href="javascript:;" isEdicao="true" onclick="ParciaisController.popupInserirPeriodoManual(true);">
 				<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" alt="Incluir Período" />
 			</a>
 	 	</span>
	 	<span id="btnIncluirPeriodosModal" class="bt_novos">
	 		<a href="javascript:;" isEdicao="true" onclick="ParciaisController.popupInserirPeriodoAutomatico(true);" rel="tipsy" title="Recalcular Períodos">
	 			<img src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0" alt="Incluir Períodos" />
	 		</a>
	 	</span>	
	</div>
</form>

<form id="idParciaisNovo">
	<div id="dialog-novo" title="Nova Parcial">
	     <fieldset>
	     	<legend>Novo Período</legend>
	        <table width="236" border="0" cellspacing="1" cellpadding="1">
	          
	           <tr>
	            <td>Qtde. Períodos:</td>
	            <td>
	<!-- QTDE -->
	<input id="parcial-qtde" name="" type="text" style="width:80px;" onchange="ParciaisController.carregaPeb(this.value)" /></td>
	          </tr>
	          
	          <tr>
	            <td width="103">PEB:</td>
	            <td width="126">
	<!-- PEB -->
	<input id="parcial-peb" name="parcial-peb" type="text" style="width:80px;" readonly="readonly" disabled="disabled" /> dias</td>
	          </tr>
	         
	        </table>
	        
	     </fieldset>
	   
	 </div>
</form>

<form id="idParciaisNovoManual">
	<div id="dialog-novo-manual" title="Nova Parcial">
	     <fieldset>
	     	<legend>Novo Período</legend>
			<table width="236" border="0" cellspacing="1" cellpadding="1">

				<tr>
					<td>Data Recolhimento:</td>
					<td>
						<input id="parcial-dataRecolhimentoManual" name="" type="text" style="width: 80px;" />
					</td>
				</tr>

			</table>

		</fieldset>
	   
	 </div>
</form>

		<div class="areaBts">
			<div class="area">
				
				<span id="exportacao" style="display: none">
				
					<span class="bt_arq">
					<!-- ARQUIVO EXCEL -->
						<a href="${pageContext.request.contextPath}/parciais/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
							<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						</a>
					</span>
		
					<span class="bt_arq">
					<!-- IMPRIMIR PDF -->	
						<a href="${pageContext.request.contextPath}/parciais/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						</a>
					</span>
		        
	        	</span>
				
				<span id="exportacaoPeriodos" style="display: none">
					
					<span class="bt_arq" title="Gerar Arquivo">
						<!-- ARQUIVO EXCEL -->
						<a href="${pageContext.request.contextPath}/parciais/exportarPeriodos?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						</a>
					</span>
				
					<span class="bt_arq" title="Imprimir">
						<!-- IMPRIMIR PDF -->	
						<a href="${pageContext.request.contextPath}/parciais/exportarPeriodos?fileType=PDF">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						</a>
					</span>

				 	<span id="btnNovoPeriodoManual" class="bt_novos" title="Novo">
				 		<a href="javascript:;" isEdicao="true" onclick="ParciaisController.popupInserirPeriodoManual(false);">
				 			<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" alt="Incluir Período" />
				 		</a>
				 	</span>
				 	
					<span id="btnIncluirPeriodos" class="bt_novos" title="Recalcular">
				 		<a href="javascript:;" isEdicao="true" onclick="ParciaisController.popupInserirPeriodoAutomatico(false);">
				 			<img src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0" alt="Recalcular Períodos" />
				 		</a>
				 	</span>
						
				 </span>
		</div>
	</div>
		
		<div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Parciais</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="51">Código:</td>
              <td colspan="3">
		<!-- Código -->
		<input class="campoDePesquisa" id="parcial-codigoProduto" name="parcial-codigoProduto" style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
			onchange="pesquisaProdutoParciais.pesquisarPorCodigoProdutoAutoCompleteEdicao('#parcial-codigoProduto', '#parcial-nomeProduto', '#parcial-edicaoProduto' , false);" />
				</td>
                <td width="52">Produto:</td>
                <td width="192">
                
		<!-- Nome Produto -->                
		<input class="campoDePesquisa" id="parcial-nomeProduto" type="text" name="parcial-nomeProduto"  style="width: 150px;" maxlength="255"
					       onkeyup="pesquisaProdutoParciais.autoCompletarPorNomeProduto('#parcial-nomeProduto', false);"
					       onblur="pesquisaProdutoParciais.pesquisarPorNomeProduto('#parcial-codigoProduto', '#parcial-nomeProduto', null, false);"/>
					    	   
				</td>
                <td width="56">Edição:</td>
                <td width="163">

				<!-- Numero Edição -->                
				<input class="campoDePesquisa" id="parcial-edicaoProduto"  type="text" name="parcial-edicoes" style="width:80px;"/>
		</td>

              <td width="74">Fornecedor:</td>
              <td>
       
				<!-- Fornecedores -->
				<select class="campoDePesquisa" id="parcial-idFornecedor" name="parcial-idFornecedor" style="width:200px;">
				    <option value="-1"  selected="selected">Todos</option>
				    <c:forEach items="${listaFornecedores}" var="fornecedor">
				      	<option value="${fornecedor.key}">${fornecedor.value}</option>	
				    </c:forEach>
				</select>
       
       	      </td>
           </tr>
            <tr>
              <td>Período:</td>
              <td colspan="3">
				
				<!-- Data de -->              
				<input class="campoDePesquisa" id="parcial-dataInicial" type="text" name="parcial-dataInicial" style="width:80px;"/></td>

              <td>Até:</td>
              <td>
				
				<!-- Data até -->
				<input class="campoDePesquisa" id="parcial-dataFinal" type="text" name="parcial-dataFinal" style="width:80px;"/></td>

<!--               <td>Status:</td> -->
<!--               <td> -->
				              
<!-- 				Status               -->
<!-- 				<select class="campoDePesquisa" id="status" name="select2" style="width:140px;"> -->
<!-- 				  <option selected="selected" value="">Todos</option> -->
<%-- 				   <c:forEach items="${listaStatus}" var="status"> --%>
<%-- 				      		<option value="${status.key}">${status.value}</option>	 --%>
<%-- 				    </c:forEach> --%>
<!-- 				</select> -->
<!-- 				</td> -->
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td width="200"><span class="bt_novos"><a href="javascript:;" onclick=" ParciaisController.cliquePesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0"/></a></span></td>
            </tr>
          </table>

      </fieldset>
      	<div class="linha_separa_fields">&nbsp;</div>
		<div class="grids" style="display:none;">
		<!-- PESQUISA DE LANCAMENTOS ParciaisController -->
		<fieldset id="parcial-painelLancamentos" class="fieldGrid" style="display:none">
			     <legend>Parciais Cadastradas</legend>
			        	
			    <table class="parcial-parciaisGrid"></table>
		</fieldset>          
		</div>  


	<!-- PESQUISA DE PERIODOS PARCIAIS -->
		<fieldset id="parcial-painelPeriodos" class="fieldGrid" style="display:none">
	    	<legend>Períodos Cadastrados</legend>
	        <table class="parcial-periodosGrid"></table>
		</fieldset>

<form id="form-parciais-venda">
	<jsp:include page="parciaisDeVenda.jsp" />
</form>

<form id="form-parciais-redistribuicao">
	<jsp:include page="redistribuicao.jsp" />
</form>