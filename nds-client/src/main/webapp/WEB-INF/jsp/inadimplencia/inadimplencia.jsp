<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript"	src="${pageContext.request.contextPath}/scripts/jquery-dateFormat/jquery.dateFormat-1.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/inadimplencia.js"></script>

<title>NDS - Novo Distrib</title>


<script language="javascript" type="text/javascript">
var pesquisaCotaInadimplencia = new PesquisaCota();
$(function(){
	inadimplenciaController.init();
});
</script>

</head>

<body>

<form id="form-detalhes">
<div id="dialog-detalhes" title="Detalhe da Divida">     
</div>
</form>
<form id="form-detalhes-comissao">
<div id="dialog-detalhes-comissao" title="Detalhe da Divida - Comiss&#259;o">     
</div>
</form>
    <div class="areaBts">
    	<div class="area">
    		<span class="bt_arq">
                
<!-- EXCEL -->
<a href="${pageContext.request.contextPath}/inadimplencia/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">

				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>

				<span class="bt_arq">

<!-- PDF -->
<a href="${pageContext.request.contextPath}/inadimplencia/exportar?fileType=PDF" rel="tipsy" title="Imprimir">

				
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /></a>

				</span>
    	</div>
    </div>
    <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
   	    <legend> Hist&oacutericos de Inadimpl&ecircncias</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="72">Per&Iacuteodo de:</td>
              <td width="108" >

<!-- DATA DE -->
<input type="text" name="datepickerDe" id="idDataDe" style="width:80px;" />
            
              </td>
              <td width="28" >At&eacute:
              </td>
              <td width="106" >

<!-- DATA ATE --> 
<input type="text" name="datepickerAte" id="idDataAte" style="width:80px;" />
              
              </td>
              <td width="36">Cota:</td>
              <td width="93">

<!-- NUM COTA -->
<input type="text" name="idNumCota" id="idNumCota" style="width:80px;" 
	onchange="pesquisaCotaInadimplencia.pesquisarPorNumeroCota('#idNumCota', '#idNomeCota');"/></td>
              
              <td width="45">Nome:</td>
              <td width="193">

<!-- NOME COTA -->
<input type="text" name="idNomeCota" id="idNomeCota" style="width:180px;" 
	onkeyup="pesquisaCotaInadimplencia.autoCompletarPorNome('#idNomeCota');" /></td>
            
              <td width="48">Status:</td>
              <td width="170">
              
 
 <!-- COMBO STATUS -->             
 <select name="select" id="idStatusCota" style="width:90px;">
  <option value="none">Selecione...</option>
  <c:forEach items="${itensStatus}" var="status" varStatus="index">
  	 <option value="${status.key}">${status.value}</option>
  </c:forEach>
       
 </select>
 			</td>
            </tr>
            <tr>
              <td colspan="2">
<!-- SITUACAO -->              
<a href="javascript:;" id="selDivida">Situa&ccedil&atildeo da D&iacutevida:</a>
              
              <div class="menu_dividas" style="display:none;border: 1px solid #CCC;position: absolute;background: white;z-index: 10;">

<!-- SITUACAO DIVIDA  TODAS -->                

			<table>
				<tr>
					
                
				
				<td>
					<span class="bt_sellAll">
<input type="checkbox" id="idSelecaoTodos" name="Todos2" onclick="selecionarTodos(this);" style="float:left;"/>
					</span>
				</td>
				<td>
					<span class="bt_sellAll">
<label for="selDivida">Selecionar Todas</label>
					</span>
				</td>
								
				</tr>
				<tr>
				
<!-- SITUACAO DIVIDA - ABERTAS -->
				<td>
<input id="idDividaEmAberto" name="checkgroup_menu_divida" onclick="verifyCheck_2()" type="checkbox"/>
                </td>
                <td>
                <label for="emaberto">Em Aberto</label>
                </td>
                
                </tr>
                <tr>
                
                <td>
<!-- SITUACAO DIVIDA - NEGOCIADA -->
<input id="idDividaNegociada" name="checkgroup_menu_divida" onclick="verifyCheck_2()" type="checkbox"/>
				</td>
				<td>
                <label for="negociada">Negociada</label>
                </td>
                
                </tr>
                <tr>
                
                <td>
<!-- SITUACAO DIVIDA - PAGA -->
<input id="idDividaPaga" name="checkgroup_menu_divida" onclick="verifyCheck_2()" type="checkbox"/>
				</td>
				<td>
                <label for="paga">Paga</label>
                </td>
                
				</tr>
			</table>
                
           </div>
           
           
           </td>
           
           
              <td colspan="2" >
                
              </td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td><span class="bt_novos"><a href="javascript:;" onclick="inadimplenciaController.cliquePesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="fieldGrid">
       	  <legend> Cotas Inadimplentes</legend>
        <div class="grids" style="display:none;">
			<table class="inadimplenciaGrid"></table>
          	
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="23%">&nbsp;</td>
                <td width="8%"><strong>Qtde Cotas:</strong></td>
                <td width="5%">
                	<div id="idQtde">0</div>  
                </td>
                <td width="2%">&nbsp;</td>
                <td width="10%"><strong>Divida Total R$:</strong></td>
                <td width="9%">
                	<div id="idTotal">0,00</div>  
                </td>
                <td width="43%">&nbsp;</td>
              </tr>
            </table>

            
            
            
            
           
        
</div>
		
      </fieldset>
      

</body>