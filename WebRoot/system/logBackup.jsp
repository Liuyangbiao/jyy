<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/"; %>
<html>
    <head>
        <title>工程信息管理</title>
        <link type="text/css" href="../css/jquery.autocomplete.css" rel="stylesheet" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/jquery.css" rel="stylesheet" type="text/css">
        <link href="../css/themes/jquery-ui-1.8.11.custom.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="../css/themes/ui.multiselect.css" />
        <link href="../css/base.css" rel="stylesheet" type="text/css">
        <link href="../css/uploadify.css" rel="stylesheet" type="text/css" />
        <link href="../css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
        <link href="../css/skin-vista/ui.dynatree.css" rel="stylesheet" type="text/css" />
        <script src="../js/jquery-1.5.min.js" type="text/javascript"> </script>
        <script src="../js/jquery-ui-1.8.10.custom.min.js" type="text/javascript"> </script>
        <script src="../js/jquery.dynatree.js" type="text/javascript"></script>
        <script src="../js/jquery.cookie.js" type="text/javascript"></script>
        <script src="../js/jquery.jqGrid.js" type="text/javascript"></script>
        <script src="../js/jquery.form.js" type="text/javascript"> </script>
        <script src="../js/rowedex4.js" type="text/javascript"></script>
        <script src="../js/jquery.timepicker.js" type="text/javascript"></script>
        <script src="../js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
        <script type="text/javascript" src="../js/jquery.autocomplete.js"> </script>
        <script type="text/javascript" src="../js/popShortcut.js"> </script>
        <script type="text/javascript" src="js/logBackup.js"></script>
        <script src="../js/jquery.maskedinput.js" type="text/javascript"></script>
      <script type="text/javascript">
            $(function(){
                var mId = "${param.menuId}";
                imgDisplay(mId);
            });
        </script>
    </head>
    <body>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="contert2">
            <tr>
                <td class="conttop2">
                    <div class="top2left">
                    </div>
                    <div class="top2right">
                    </div>
                    <div class="top2middle">
                        <span class="bottonszkj2" id="img1"><a href="#"><img src="../images/szkj_botton.gif" onClick="topwin(${param.menuId })"></a></span><span class="bottonszkj2" id="img2"><a href="#"><img src="../images/kjysz_botton.gif"></a></span><span class="topico2"><img src="../images/Accordion_ico03.gif"></span><h3>日志备份</h3>
                    </div>
                </td>
            </tr>
           	<tr>
				<td class="contmiddle2">
					<table width="900" border="0" cellspacing="0" cellpadding="0"
						style="font-size: 12px; font-weight: bold;">
						<tr>
							<td>
								<div id="tabs" align="center">
									<div id="tabs-1">
										<form action="" method="post" id="autoForm" name="autoForm">
										<table width="900" border="0" cellpadding="0" cellspacing="5"
											style="font-size: 12px;">
											<tr>
												<td width="10%" align="right">
													<strong style="font-size: 12px;"> 备份状态: </strong>
												</td>
												<td width="25%">
													<span><img id="backupImage" src="../images/onError.png"/>
													<input type="checkbox" id="status" name="status" value="1" /><strong id="statusName">启用</strong></span>
												</td>
												<td width="13%" align="right">
													<strong style="font-size: 12px;"> 备份路径: </strong>
												</td>
												<td width="52%">
													<strong><span id="backupWay"></span></strong>
												</td>
											</tr>
											<tr>
												<td align="right" valign="top">
													<strong style="font-size: 12px;"> 备份范围: </strong>
												</td>
												<td>
													<select id="autoScope" name="scope" multiple="multiple" style="width:150px;">
													</select>
												</td>
												<td align="right" valign="top">
													<strong style="font-size: 12px;"> 备份周期: </strong>
												</td>
												<td valign="top">
													<table style="font-size: 12px;">
														<tr>
															<td width="200"><div id="slider"></div></td>
															<td width="150">&nbsp;&nbsp;
																<span id="backupValue" style="width:20px;"></span>
																<input type="hidden" id="backupAmount" name="amount" />
																<input type="radio" id="backupCircle" name="circle" value="d" checked="checked" onclick="changeValue('d')" />天
																<input type="radio" id="backupCircle" name="circle" value="m" onclick="changeValue('m')"/>月
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong style="font-size: 12px;"> 备份时间: </strong>
												</td>
												<td colspan="3">
													<select id="backupTime" name="time" style="width:150px;">
														<option value="00:00">00:00</option><option value="01:00">01:00</option>
														<option value="02:00">02:00</option><option value="03:00">03:00</option>
														<option value="04:00">04:00</option><option value="05:00">05:00</option>
														<option value="06:00">06:00</option><option value="07:00">07:00</option>
														<option value="08:00">08:00</option><option value="09:00">09:00</option>
														<option value="10:00">10:00</option><option value="11:00">11:00</option>
														<option value="12:00">12:00</option><option value="13:00">13:00</option>
														<option value="14:00">14:00</option><option value="15:00">15:00</option>
														<option value="16:00">16:00</option><option value="17:00">17:00</option>
														<option value="18:00">18:00</option><option value="19:00">19:00</option>
														<option value="20:00">20:00</option><option value="21:00">21:00</option>
														<option value="22:00">22:00</option><option value="23:00">23:00</option>
													</select>
												</td>
											</tr>
										</table>
										<table width="900" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td height="22" align="right">
													<button class="chaxunbutton" onclick="autoBackup()">
														<span style="font-size: 14px;">设置</span>
													</button>
												</td>
											</tr>
										</table>
										</form>
										<div class="bianxian"></div>
										<form action="" method="post" id="handForm">
										<table width="900" border="0" cellpadding="0" cellspacing="5"
											style="font-size: 12px;">
											<tr>
												<td width="10%" align="right" valign="top">
													<strong style="font-size: 12px;"> 备份范围: </strong>
												</td>
												<td width="25%">
													<select id="handScope" name="scope" multiple="multiple" style="width:150px;">
													</select>
												</td>
												<td width="13%" align="right" valign="top">
													<strong style="font-size: 12px;"> 备份路径: </strong>
												</td>
												<td width="52%" valign="top">
													<strong><span id="backupWaya"></span></strong>
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong style="font-size: 12px;"> 备份时间: </strong>
												</td>
												<td colspan="3">
													<input type="text" id="timeFrom" name="timeFrom" />&nbsp;&nbsp;到&nbsp;&nbsp;<input type="text" id="timeTo" name="timeTo" />
													<span id="timeError" style="color:red"></span>
												</td>
											</tr>
										</table>
										<table width="900" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td height="22" align="right">
													<button class="chaxunbutton" onclick="handBackup()">
														<span style="font-size: 14px;">设置</span>
													</button>
												</td>
											</tr>
										</table>
										</form>
										<div class="bianxian"></div>
										<div align="left">
											<!-- Jqgrid -->
						                    <table id="rowedtable">
						                    </table>
						                    <div id="prowed4">
						                    </div>
										</div>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
            <tr>
                <td class="contbottom2">..
                    <div class="bottom2left">
                    </div>
                    <div class="bottom2right">
                    </div>
                    <div class="bottom2middle">
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>
