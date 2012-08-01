<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">

body{font-size:11px!important;}
td{padding-left:3px;padding-right:3px;}
.titulo{ color:#333; font-size:10px;!important;}
.dadosNota{ color:#000;}

.quebraPaginaEmissao{
			page-break-after: always;
			table-layout: fixed;
		}
.capaImgBox {
	display: none;
	width: 115px;
	float: left;
	border: solid 1px #000; 
	margin: 1px 2px;
}
.capaImg{
	width:110px;
	height:143px;
}
#painelCapas{
	width: 850px;
	display: block;
	margin: 0 auto;
}
</style>
<script language="javascript" type="text/javascript">
function imprimir(){
	$( "#btImpressao" ).hide();
	window.print();
}
$(function(){$('img.capaImg').load(function() {
	$(this).parent().parent().show();
});});
</script>
</head>

<body>


<c:forEach items="${cotasEmissao}" var="cotaEmissao">

<div class="quebraPaginaEmissao">



<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px; margin-top:10px;">
  <tr>
    <td width="121" rowspan="6" valign="top"><span class="logo"><img src="../images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></td>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;"><span class="titulo">Razão Social<br />
    </span></td>
    <td width="237" align="center" style="border-left:1px solid #000;"><span class="titulo" style="font-size:13px!important;"><strong>CHAMADA DE ENCALHE</strong></span></td>
  </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
    <td rowspan="2" align="center" style="border-left:1px solid #000;"><h3>Documento Número: ${cotaEmissao.idChamEncCota}</h3></td>
  </tr>
  <tr>
    <td width="172" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
    </span></td>
    <th width="25">&nbsp;</th>
    <th width="75">&nbsp;</th>
    <td width="130" align="center"><span class="titulo">CNPJ</span></td>
  </tr>
  <tr>
    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
    </span></td>
    <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
    <td style="border-left:1px solid #000;">&nbsp;</td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td align="center"><span class="titulo">UF</span></td>
    <td align="center"><span class="titulo">CEP</span></td>
    <td align="center"><span class="titulo">Inscrição Estadual</span></td>
    <td style="border-left:1px solid #000;"><span class="titulo">Natureza da Operação</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
    <td align="center" style="border-left:1px solid #000;"><span class="dadosNota">5949 REMESSA P/ DISTRIBUIÇÃO</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
    </span></td>
    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
  </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</span></td>
    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
  </tr>
  <tr>
    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço </span></td>
    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
    <td style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
  <tr>
    <td width="196" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
    <td width="196" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
    <td width="458" style="border-right:1px solid #000;"><span class="titulo">Box / Rota</span></td>
  </tr>
  <tr>
    <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} - Rota: ${cotaEmissao.codigoRota} - ${cotaEmissao.nomeRota}</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
  <tr class="class_linha_3">
    <td colspan="8" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;">&nbsp;</td>
    <td colspan="3" align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000;border-right:1px solid #000;"><strong>Qtde.</strong></td>
    <td colspan="2" align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
  </tr>
  <tr class="class_linha_3">
    <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Código</strong></td>
    <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Produto</strong></td>
    <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Seq</strong></td>
    <td align="right" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;"><strong>Desc</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Data Lancto</strong></td>
    <td class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Nota Envio</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>TR</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Reparte</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong> Devol.</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Venda</strong></td>
    <td align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Preço Desc R$</strong></td>
    <td align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;"><strong>Vlr. Venda R$</strong></td>
  </tr>
  
  <c:forEach items="${cotaEmissao.produtos}" var="produto" varStatus="status">
           
  
  <tr class="class_linha_${status.index%2==0?1:2}">
  
    <td width="58" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.codigoProduto}</td>
    <td width="135" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.nomeProduto}</td>
    <td width="49" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.edicao}</td>
    <td width="49" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px;  ">${status.index+1}</td>
    <td width="51" align="right" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; border-right:1px solid #000; ">${produto.desconto}</td>
    <td width="80" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.dataLancamento}</td>
    <td width="75" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">0028</td>
    <td width="22" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.tipoRecolhimento}</td>
    <td width="51" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.reparte}</td>
    <td width="41" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.quantidadeDevolvida}</td>
    <td width="43" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.vendido}</td>
    <td width="101" align="right" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.precoComDesconto}</td>
    <td width="95" align="right" style="border-bottom:1px solid #000;padding-left:5px; border-right:1px solid #000; ">${produto.vlrVendido}</td>
  </tr>
 
 </c:forEach>
 
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td><table width="300" border="0" align="right" cellpadding="1" cellspacing="1">
      <tr>
        <td align="right">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
      <tr>
        <td width="168" align="right"><strong>Total Bruto R$</strong></td>
        <td width="121" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
      </tr>
      <tr>
        <td align="right"><strong> Total Desconto R$</strong></td>
        <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
      </tr>
      <tr>
        <td align="right"><strong>Total Líquido R$</strong></td>
        <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
      </tr>
    </table></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
  <tr class="class_linha_3">
    <td colspan="6" align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><strong>OBSERVAÇÕES</strong></td>
  </tr>
  <tr class="class_linha_3">
    <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Vale Desconto</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;"><strong>Qtde</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Preço  Desconto R$</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Total</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Total Vale Desconto</strong></td>
  </tr>
  <tr class="class_linha_1">
    <td width="129" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">&nbsp;</td>
    <td width="129" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">&nbsp;</td>
    <td width="129" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; border-right:1px solid #000; ">&nbsp;</td>
    <td width="129" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">&nbsp;</td>
    <td width="129" align="center" style="border-bottom:1px solid #000;padding-left:5px; ">&nbsp;</td>
    <td width="205" rowspan="4" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">&nbsp;</td>
  </tr>
  <tr align="center" class="class_linha_2">
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000; border-right:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-bottom:1px solid #000;">&nbsp;</td>
  </tr>
  <tr align="center" class="class_linha_1">
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000; border-right:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-bottom:1px solid #000;">&nbsp;</td>
  </tr>
  <tr align="center" class="class_linha_2">
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-left:1px solid #000;border-bottom:1px solid #000; border-right:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
    <td style="padding-left:5px; border-bottom:1px solid #000;">&nbsp;</td>
  </tr>
</table>
<br />
<br />


</div>
<c:if test="${withCapa}">
<div class="quebraPaginaEmissao" id=paginaCapas>	
	
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px; margin-top:10px;">
  <tr>
    <td width="121" rowspan="6" valign="top"><span class="logo"><img src="../images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></td>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;"><span class="titulo">Razão Social<br />
    </span></td>
    <td width="237" align="center" style="border-left:1px solid #000;"><span class="titulo" style="font-size:13px!important;"><strong>CHAMADA DE ENCALHE</strong></span></td>
  </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
    <td rowspan="2" align="center" style="border-left:1px solid #000;"><h3>Documento Número: ${cotaEmissao.idChamEncCota}</h3></td>
  </tr>
  <tr>
    <td width="172" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
    </span></td>
    <th width="25">&nbsp;</th>
    <th width="75">&nbsp;</th>
    <td width="130" align="center"><span class="titulo">CNPJ</span></td>
  </tr>
  <tr>
    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
    </span></td>
    <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
    <td style="border-left:1px solid #000;">&nbsp;</td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td align="center"><span class="titulo">UF</span></td>
    <td align="center"><span class="titulo">CEP</span></td>
    <td align="center"><span class="titulo">Inscrição Estadual</span></td>
    <td style="border-left:1px solid #000;"><span class="titulo">Natureza da Operação</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
    <td align="center" style="border-left:1px solid #000;"><span class="dadosNota">5949 REMESSA P/ DISTRIBUIÇÃO</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
    </span></td>
    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
  </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</span></td>
    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
  </tr>
  <tr>
    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço </span></td>
    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
    <td style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
  <tr>
    <td width="196" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
    <td width="196" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
    <td width="458" style="border-right:1px solid #000;"><span class="titulo">Box / Rota</span></td>
  </tr>
  <tr>
    <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} - Rota: ${cotaEmissao.codigoRota} - ${cotaEmissao.nomeRota}</span></td>
  </tr>
</table>

	<div id="painelCapas">	
		<div style="width: inherit; text-align: center;">
				<span class="titulo" style="font-size:11px!important;">Capas</span>			
		</div>	
	
	
	<c:if test="${personalizada}">
		
		<c:forEach items="${capas}" var="capa" varStatus="status">	
			
			<div class="capaImgBox">			
				<div style="width: inherit; text-align: center;">
					<strong>${status.index+1}</strong>				
				</div>			
				<div style="width: inherit; text-align: center;">
					<img class="capaImg" src="<c:url value='/capa/${capa.id}'></c:url>"/>
				</div>
			</div>	
			
	    </c:forEach>
    
		
	</c:if>
	
	<c:if test="${!personalizada}">
	
		<c:forEach items="${cotaEmissao.produtos}" var="produto" varStatus="status">	
			
			<div class="capaImgBox">			
				<div style="width: inherit; text-align: center;">
					<strong>${status.index+1}</strong>				
				</div>			
				<div style="width: inherit; text-align: center;">
					<img class="capaImg" src="<c:url value='/capa/${produto.idProdutoEdicao}'></c:url>"/>
				</div>
			</div>	
			
	    </c:forEach>
    
    </c:if>	
	
	
    </div>
   <br clear="all"/>
</div>
</c:if>
</c:forEach>
</body>
</html>
