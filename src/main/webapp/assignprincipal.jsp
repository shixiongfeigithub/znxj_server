<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
</head>
<body>
<%--<%@ include file="/WEB-INF/pages/common/navigation.jsp"%>--%>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript">
    function formSubmit(){
        var count=$("#operatorid").val();
        if(count==0){
            alert("操作员不能为空");
            return false;
        }

        $.ajax({
            url:"assignprincipal",
            type:"post",
            data:$("#form").serialize(),
            success: function(data){
                if(data>0){
                    alert("任务指定责任人成功");
                    showexceptionreport();
                }else {
                    alert("指定责任人失败");
                    return false;
                }
            }
        });
    }

    function showexceptionreport(){
        window.location="showexceptionreport?page=1";
    }
</script>
<div class="ch-container">
    <div class="row">
        <div id="content"class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i> 分配巡检异常处理责任人
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="" method="post" id="form">
                                <input type="hidden"  name="reportid" id="reportid" value="${reportcontent.reportid}">
                                <input type="hidden"  name="reportcontentid" id="reportcontentid" value="${reportcontent.id}">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >巡检项名称:</label>
                                            ${reportcontent.checkname}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="operatorid">责任人:</label>
                                            <select name="operatorid" id="operatorid" class="form-control" required>
                                                <c:forEach items="${operationuserList}" var="user">
                                                    <option value="${user.id}">${user.realname}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="exceptiontype">异常类型:</label>
                                            <select name="exceptiontype" id="exceptiontype" class="form-control" required>
                                                <c:forEach items="${exceptiontypeList}" var="type">
                                                    <option ${exceptionhandlerinfo.exceptiontype eq type.name?'selected':''} value="${type.name}">${type.name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="exceptionlever">异常等级:</label>
                                            <select name="exceptionlever" id="exceptionlever" class="form-control" required>
                                                <c:forEach items="${levertype1List}" var="lever">
                                                    <option ${exceptionhandlerinfo.exceptionlever eq lever.name ?'selected':''} value="${lever.name}">${lever.name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><input id="save" type="button" class="btn btn-primary" value="保存" onclick="formSubmit()">
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
<script type="text/javascript">
</script>
</body>
</html>
