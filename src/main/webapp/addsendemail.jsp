<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript">
        function selectallrole(obj,checkboxname){
            if($(obj).prop("checked")){
                $("input:checkbox[name='"+checkboxname+"']").each(function() {
                    $(this).prop("checked",true);
                });
            }else{
                $("input:checkbox[name='"+checkboxname+"']").each(function() {
                    $(this).prop("checked",false);
                });
            }
        }
    </script>
</head>
<body>
<%--<%@ include file="/WEB-INF/pages/common/navigation.jsp"%>--%>
<%--<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>--%>
<!-- topbar ends -->
<div class="ch-container">
    <div class="row">

        <%--<%@ include file="/WEB-INF/pages/common/menu.jsp"%>--%>
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i> 发送邮件设置
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="addsendemail" method="post">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline"><label class="control-label" for="type">选择发送类型:</label>
                                            <select name="type" id="type" class="form-control">
                                                <option value="0">日报</option>
                                                <option value="1">周报</option>
                                                <option value="2">月报</option>
                                                <option value="3">巡检异常</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline"><label class="control-label" for="taskid">选择发送任务:</label>
                                            <select name="taskid" class="form-control" id="taskid">
                                                <c:forEach items="${taskplaninfos}" var="task" varStatus="taskstatus">
                                                    <option value="${task.id}">${task.customid }</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <%--<tr>
                                        <td class="form-inline">
                                            <label class="control-label">
                                                <input type="checkbox" name="sendexception" id="sendexception" style="width: 15px;height: 15px;">是否巡检异常推送</label>
                                        </td>
                                    </tr>--%>
                                    
                                    <%--<script type="text/javascript">
                                        $(function () {
                                            $("#sendexception").click(function () {
                                                if($("#sendexception").is(":checked")){
                                                    $("#sendexception").attr("value","1");
                                                }else{
                                                    $("#sendexception").attr("value","0");
                                                }
                                            });
                                        });
                                    </script>--%>
                                    <tr>
                                        <td><h4>选择发送人:<input type="checkbox" name="role" id="role" onclick="selectallrole(this,'selectedpersion')" style="width: 16px;height: 16px;"> 全选/全不选</h4><br/>
                                            <c:forEach items="${contactinfos}" var="c" varStatus="status">
                                                <span style="text-align: left;display:inline-block; width:200px;height:30px;">
                                                <input name="selectedpersion" type="checkbox"  value="${c.id}" style="width: 16px;height: 16px;">${c.name }</span>
                                                <c:if test="${status.count%5==0}">
                                                    <br>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="email">邮箱账号：</label>
                                            <input type="email" name="email" class="form-control" id="email">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="pwd">邮箱授权码或密码：</label>
                                            <input type="password" name="pwd" class="form-control" id="pwd">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline"><label class="control-label" for="state">状态:</label>
                                            <select name="state" id="state" class="form-control">
                                                <option value="0">激活</option>
                                                <option value="1">暂停</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="smtpAddress">SMTP_ADDRESS：</label>
                                            <input type="text" name="smtpAddress" class="form-control" id="smtpAddress" placeholder="如：smtp.exmail.qq.com">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="smtpPort">SMTP_PORT：</label>
                                            <input type="text" name="smtpPort" class="form-control" id="smtpPort" placeholder="如：465">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><input type="submit" class="btn btn-primary" value="保存">
                                            <input type="reset" class="btn btn-primary white" value="取消" onclick="javascript:history.go(-1);"></td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>





<tr>

    <c:forEach items="${listBabys}" var="itemx"  varStatus="status">

    <td>

        <a href="javascript:recselectAct(${itemx.id});">${itemx.babyname}</a>
    </td>
    <c:if test="${status.count%6==0}">
</tr>
<tr>
    </c:if>

    </c:forEach>
</tr>
</body>
</html>
