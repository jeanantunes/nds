<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">
body{font-size:12px!important;}
h1{font-size:20px;}
h2{font-size:25px;}
p{margin:0px; padding:0px; font-size:11px;}
.capas tr{border:1px solid #000;}
.box_rel{line-height:15px!important; background:#fff;}
.box_dados{line-height:30px!important; font-size:16px; font-weight:bold;}

</style>
<script language="javascript" type="text/javascript">
function imprimir(){
	$( "#btImpressao", BaseController.workspace ).hide();
	window.print();
}
</script>

<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/mapaAbastecimento.js"></script> --%>

</head>

<body>
	
	
	
	
	
	
	<div style="position:absolute;	margin:0; padding:0; width:100%; height:100%; background:rgba(0,0,0,0.5); text-align:center">
		<div class="ajuste-de-warning" style="position:absolute; width: 530px; background: #ffd056; margin:0 auto; -moz-border-radius: 5px; -webkit-border-radius: 5px; border-radius:5px">
			<div style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border-radius:5px; margin:2px; font-size:13px; font-weight:bold; padding:6px;">
				<p>As seguintes cotas não tem roteirização:</p>
			</div>
			
			<div style="background:	#fff762; margin:3px; -moz-border-bottom-radius: 5px; -webkit-border-bottom-radius: 5px; border-bottom-radius:5px; margin:2px; padding:6px;">
				<p>
				<c:forEach items="${result}" var="cotasSerRoteirizacao">
					${cotasSerRoteirizacao} <br />
				</c:forEach>
			</p>
			</div>
			
		</div>
	</div>
	
	
	
	
	
	<script language="javascript" type="text/javascript">
		$(document).ready(function(){
			var widthDiv = $(".ajuste-de-warning").width();
			var heightDiv = $(".ajuste-de-warning").height();
			
			var widthTela = screen.width;
			var heightTela = screen.height;
			
			$(".ajuste-de-warning").css('left', (widthTela / 2) - (widthDiv / 2));
			$(".ajuste-de-warning").css('top', (heightTela / 2) - (heightDiv * 4));
			
			
			console.log(widthDiv);
		});
	</script>
	
	
	
	
<table width="800" border="0" align="center" cellpadding="3" cellspacing="0" style="border:1px solid #000; margin-bottom:5px;">
  <tr>
  <td width="121" height="21" align="center">
    	<span>
    		<span class="">
    		<img src="${pageContext.request.contextPath}/administracao/parametrosDistribuidor/getLogo?number=${pageContext.request.requestedSessionId}" border="0" height="70" width="110" />
    		</span>
    	</span>
    </td>
    <td width="269" align="center" valign="middle"><h3>${nomeDistribuidor}</h3></td>
    <td width="408" align="right" valign="middle"><h1>Mapa de Abastecimento&nbsp;</h1></td>
    </tr>
  <tr>
    <td colspan="3" align="center" valign="middle"></td>
  </tr>
</table>

<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
            <tr class="class_linha_3">
              <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000;"><strong>Publicação</strong></td>
              <td class="relatorios" style="padding-left:5px; border-right:1px solid #000; border-top:1px solid #000;"><strong>Edição</strong></td>
              <td width="560" rowspan="2" align="center" style=" border-right:1px solid #000; border-bottom:1px solid #000; border-top:1px solid #000;"><strong>BOX</strong></td>
            </tr>
            <tr class="class_linha_3">
              <td width="151" class="relatorios" style="padding-left:5px; border-bottom:1px solid #000; border-left:1px solid #000;"><strong>Código</strong></td>
              <td width="89" style="padding-left:5px; border-bottom:1px solid #000; border-right:1px solid #000;" class="relatorios"><strong>Preço Capa</strong></td>
            </tr>
<%--          <c:forEach items="${produtosMapa}" var="produto" varStatus="status"> --%>
            
<%--             <tr class="class_linha_${status.index%2==0 ? 1:2}"> --%>
<%--               <td style="border-left:1px solid #000;padding-left:5px; ">${produto.nomeProduto}</td> --%>
<%--               <td style="border-right:1px solid #000;padding-left:5px; ">${produto.numeroEdicao}</td> --%>
<!--               <td rowspan="2"> -->
<!--               <table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:63px;"> -->
<!--                 <tr class="box_rel"> -->
                
<%--                    <c:forEach items="${produto.boxQtde}" var="box" varStatus="statusBox">                 --%>
<%--                  		<td width="60" align="center" style="border-right:1px solid #000; border-bottom:1px solid #000;">${box.key}</td> --%>
<%--                   </c:forEach> --%>
                  
<!--                   <td width="80" align="center" style=" border-right:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td> -->
<!--                 </tr> -->
<!--                 <tr class="box_dados"> -->
<%--                   <c:forEach items="${produto.boxQtde}" var="box" varStatus="statusBox">                 --%>
<%--                  		<td align="center" style=" border-bottom:1px solid #000; border-right:1px solid #000;">${box.value eq 0 ? '': box.value}</td> --%>
<%--                   </c:forEach> --%>
                  
<%--                   <td align="center"style=" border-bottom:1px solid #000; border-right:1px solid #000;">${produto.totalReparte}</td> --%>
<!--                 </tr> -->
                
<!--               </table> -->
              
<!--               </td> -->
              
<!--             </tr> -->

<%--             <tr class="class_linha_${status.index%2==0 ? 1:2}"> --%>
<%--               <td style="padding-left:5px;  border-left:1px solid #000; border-bottom:1px solid #000;">${produto.codigoProduto}</td> --%>
<%--               <td style="padding-left:5px; border-right:1px solid #000; border-bottom:1px solid #000;"><strong> R$</strong> ${produto.precoCapa}</td> --%>
<!--             </tr> -->
           
		
<%--          </c:forEach> --%>
         
</table>

<!-- 	<div style="position:absolute;	margin:0; padding:0; width:100%; height:100%; background:rgba(0,0,0,0.5);"> -->
<!-- 		<div id="effectWarning" class="ui-state-highlight ui-corner-all" style="display: block;blackground:#79693f;"> -->
<!-- 			<div id="msgHeader"> -->
<!-- 				As cotas n�o tem roteiriza��o: -->
<!-- 				<h3 class="msgTitle"> -->
<!-- 					<span class="ui-icon ui-icon-info" style="float: left; margin-right: 1.3em;">	</span> -->
<!-- 					<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 5px; margin-top: 5px;"> -->
<!-- 					<a class="ui-icon ui-icon-close" onclick="esconde(false, $('#effectWarning'));" href="javascript:;"> </a> -->
<!-- 					</span> -->
<!-- 					As cotas n�o tem roteiriza��o: -->
<!-- 				</h3> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->


</body>
</html>