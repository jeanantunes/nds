<head>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicao.js"></script>
	
	<script type="text/javascript">		
		
		var ${param.tela} = new Distribuicao('${param.tela}');	
		
	</script>
</head>

		<table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td>Cota:</td>
            <td>
            
 <!-- Num Cota -->
 <input id="${param.tela}numCota" disabled="disabled" type="text" style="width:100px" /></td>
            
            <td>Qtde. PDV:</td>
            <td>
            
<!-- Qtde PDV -->            
<input id="${param.tela}qtdePDV" type="text" style="width:100px" /></td>

          </tr>
          <tr>
            <td width="120">Box:</td>
            <td width="320">
            
<!-- Box -->           
<input id="${param.tela}box" disabled="disabled" type="text" style="width:100px" /></td>
           
            <td width="116">Assist. Comercial:</td>
            <td width="171">
            
<!-- Assist. Comercial -->
<input id="${param.tela}assistComercial" type="text" style="width:150px" /></td>
          
          </tr>
          <tr>
            <td>Tipo de Entrega:</td>
            <td>

<!-- Tipo de Entrega -->
<select id="${param.tela}tipoEntrega" name="select4"  style="width:155px">
	<option selected="selected">...</option>
	
	<c:forEach items="${listaTipoEntrega}" var="item">
		<option value="${item.key}">${item.value}</option>	          
	</c:forEach>                            
</select>
			
			</td>
            <td>Arrendatário:</td>
            <td>
            
<!-- Arrendatário -->
<input id="${param.tela}arrendatario" type="checkbox" name="arrendaPj2" /></td>
          
          </tr>
          <tr>
            <td valign="top">Observação:</td>
            <td colspan="3">
<!-- Observacao  -->
<textarea id="${param.tela}observacao" name="textarea" rows="4" style="width:605px"></textarea></td>
          </tr>
 	 </table>
  
	  <table width="755" border="0" cellspacing="2" cellpadding="2">
		  <tr>
		    <td width="377" valign="top">

<!-- Reparte por Ponto de Venda -->	   
<input id="${param.tela}repPorPontoVenda" name="repPtoVnda" type="checkbox" value="" /><label for="${param.tela}repPorPontoVenda">Reparte por Ponto de Venda</label>
          
          <br clear="all" />
          
<!-- Solicitação Num. Atrasados -->
<input id="${param.tela}solNumAtras" name="solNumAtrs" type="checkbox" value=""/><label for="${param.tela}solNumAtrs">Solicitação Num. Atrasados - Internet</label>
        
         <br clear="all" />
         
<!-- Recebe / Recolhe produtos parciais -->        
<input id="${param.tela}recebeRecolhe"  name="recebeRecolhe" type="checkbox" value="" /><label for="recebeRecolhe">Recebe / Recolhe produtos parciais</label>
     
     
    <br />
    </td>
    <td width="378" valign="top"><table width="373" border="0" cellspacing="2" cellpadding="2">
      <tr class="header_table">
        <td width="142" align="left">Documentos</td>
        <td width="105" align="center">Impresso</td>
        <td width="106" align="center">E-mail</td>
      </tr>
      <tr class="class_linha_1">
        <td>Nota de Envio</td>
        <td align="center">
        
<!-- Nota de Envio - Impresso -->
<input id="${param.tela}neImpresso" type="checkbox" name="checkbox2" />
		
		</td>
        <td align="center">
        
<!-- Nota de Envio - E-mail -->
<input id="${param.tela}neEmail" type="checkbox" name="checkbox5"/>

		</td>
      </tr>
	      <tr class="class_linha_2">
	        <td>Chamada de Encalhe</td>
	        <td align="center">
	        
<!-- Chamada de Encalhe - Impresso -->
<input id="${param.tela}ceImpresso" type="checkbox" name="checkbox3"/></td>
       
        <td align="center">
        
<!-- Chamada de Encalhe - E-mail -->
<input id="${param.tela}ceEmail" type="checkbox" name="checkbox6" /></td>
      
      </tr>
      <tr class="class_linha_1">
        <td>Slip</td>
        <td align="center">
        
<!-- Slip - Impresso -->
<input id="${param.tela}slipImpresso" type="checkbox" name="checkbox4"/></td>
      
        <td align="center">
        
<!-- Slip - E-mail -->
<input id="${param.tela}slipEmail" type="checkbox" name="checkbox"/></td>
      
      </tr>
    </table></td>
  </tr>
</table>

  <br />
<br />
<br />

</div>