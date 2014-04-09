<head>
	
	<style>
		#dialog-novo-vale-desconto td {
			font-weight: bolder;
		}
	</style>
	
</head>

<form id="formNovoValeDesconto" name="formNovoValeDesconto">
<div id="dialog-novo-vale-desconto" style="display:none">
	<div>
		
	<input type="hidden" name="valeDesconto.id" id="idValeDesconto"/>
	<input type="hidden" name="valeDesconto.idProduto" id="idProduto"/>
	<input type="hidden" name="valeDesconto.idLancamento" id="idProduto"/>
		
	<fieldset>
		<legend>Vale Desconto</legend>
       <table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
       <tr>
         <td>Código:</td>
         <td><input type="text" id="codigoNovoValeDesconto" name="valeDesconto.codigo" maxlength="20" style="width:100px" /></td>
         
       </tr>
       <tr>
           <td width="118">Nome:</td>
           <td width="237"><input  id="nomeNovoValeDesconto" type="text"  name="valeDesconto.nome" style="width:230px " /></td>
           <td width="134" style="text-align:right">Edição:</td>
           <td colspan="3">&nbsp;
           	<input id="edicaoNovoValeDesconto" maxlength="25" type="text" name="valeDesconto.numeroEdicao" id="edicao" style="width:230px" /></td>
       </tr>
       <tr>
         <td>Situação:</td>
         <td>
         	<select id="situacao" name="valeDesconto.situacao">
         		<option selected="selected" /> 
         		<c:forEach items="${situacoes}" var="situacao">
         			<option label="${situacao.descricao}" value="${situacao}" />
         		</c:forEach>
         	</select>
         </td>
       </tr>
       <tr>
         <td>Fornecedor:</td>
         <td colspan="2">
         	<select id="fornecedor" name="valeDesconto.idFornecedor">
         		<c:forEach items="${fornecedores}" var="fornecedor">
         			<option label="${fornecedor.label}" value="${fornecedor.value}" />
         		</c:forEach>
         	</select>
         </td>
       </tr>
       <tr>
         <td>Editor:</td>
         <td colspan="2">
         	<select id="editor" name="valeDesconto.idEditor">
         		<c:forEach items="${editores}" var="editor">
         			<option label="${editor.label}" value="${editor.value}" />
         		</c:forEach>
         	</select>
         </td>
       </tr>
       <tr>
         <td>Valor:</td>
         <td><input type="text" style="width:80px" id="valorValeDesconto" name="valeDesconto.valor" /></td>
         <td style="text-align:right">Código de barras:</td>
         <td colspan="3">
			&nbsp;
			<input type="text" id="codigoDeBarras" name="valeDesconto.codigoBarras" style="width:230px" /></td>
       </tr>
      </table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset>
		<legend>Data de Recolhimento</legend>
		
		<table width="754" cellpadding="2" cellspacing="2" style="text-align:left;" id="infoRecolhimentoValeDesconto">
			<tr>
				<td colspan="4">
					<input type="checkbox" name="valeDesconto.vincularRecolhimento" 
						   id="vincularRecolhimentoCuponado" /> 
					Vincular ao Recolhimento da(s) publicação(ões) Cuponada(s)?
				</td>
			</tr>
	        <tr>
		        <td>Prevista:</td>
		        <td><input type="text" id="dataRecolhimentoPrevista" name="valeDesconto.dataRecolhimentoPrevista" style="width:100px" /></td>
		         
		        <td>Real:</td>
		        <td><input type="text" id="dataRecolhimentoReal" name="valeDesconto.dataRecolhimentoReal" style="width:100px" /></td>
		        
		        <td>Semana:</td>
		        <td><input  type="text" id="semanaRecolhimento" name="valeDesconto.semana" style="width:150px" /></td>
	        </tr>
       </table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset>
		<legend>Publicações cuponadas:</legend>
		<table id="publicacoesCuponadas" class="publicacoesCuponadas"></table>
		<span class="bt_add" style="float:right">
			<a href="javascript:;" onclick="valeDescontoController.novaAssociacao();">Incluir Novo</a>
		</span>
	</fieldset>
    
	<div class="linha_separa_fields">&nbsp;</div>
	
    <fieldset>
    	<legend>Histórico:</legend>
    	<textarea rows="3" cols="100" id="valeDescontoHistorico" name="valeDesconto.historico"
				  style="margin: 0px; width: 745px; height: 46px;">
		</textarea>
    </fieldset>
     
     
    </div>
 </div>
</form>

 <jsp:include page="./novaAssociacao.jsp"></jsp:include>
    
