<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Treelog</title>
<link rel="stylesheet" type="text/css" href="css/NDS.css" />

<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/js/jquery-1.7.1.min.js"></script>
<script language="javascript" type="text/javascript">

	$(document).keydown(function (e) {
		if(e.which == 13) {
			$('#btEntrar')[0].click();
		}
	});

	$(document).ready(function() {
		$('input:visible:enabled:first').focus();
	});
	
</script>

</head>

<body>
<br />
<br />
<br />
<br />
<br />
<br />
<br />

<form name="form" action="<c:url value='j_spring_security_check'/>" method="POST" onload="$('#username').focus()" onsubmit="form.submit();">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <input:hidden id="logout_true" name="logout_true" />
  <tr>
    <td align="center"><img src="images/logo_sistema.png" width="110" height="70" alt="Treelog"  /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td style="padding-bottom:5px;">&nbsp;
      <div class="bg_login">
        <table width="549" border="0" align="center" cellpadding="2" cellspacing="0">
          <tr>
            <td width="28%" align="right"><img src="images/tit_novo_distrib_login.png" width="135" height="70" alt="Novo Distrib" /></td>
            <td width="65%" style="border:1px solid #d6d6d6; background:#FFF;"><table width="70%" border="0" align="center" cellpadding="2" cellspacing="2" id="tabLogin">
              <tr>
                <td>&nbsp;</td>
				<td>
					<c:if test="${not empty param.error}">
			      		<font color="red">
			        		<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
			      		</font>
    				</c:if>
    			</td>                
              </tr>
              <tr>
                <td width="22%">Login:</td>
                <td width="78%"><input type="text" style="width:200px;" name="j_username" id="username" value='<c:if test="${not empty param.error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' /></td>
              </tr>
              <tr>
                <td>Senha:</td>
                <td><input type="password"  style="width:200px;" name="j_password" id="password" /></td>
              </tr>
              <tr>
                <td height="38">&nbsp;</td>
                <td><span style="float:left;"><a href="javascript:;">Esqueceu a senha?</a></span><span class="bt" style="float:right;"><a id="btEntrar" href="javascript:;" onclick="form.submit();">ENTRAR</a></span></td>
              </tr>
            </table></td>
            <td width="7%">&nbsp;</td>
          </tr>
        </table>
    </div></td>
  </tr>
</table>
</form>
</body>
</html>
