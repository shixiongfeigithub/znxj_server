<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript">

        $(function () {
            tasknum();
        })

        function tasknum(){
            var siteid = $("#name2 option:selected")[0].value;
            var type="";
            if(siteid ==null || siteid==''){
                $("#taskid").append("<option  value='' selected>--请选择--</option>");
            }else {
                $("#taskid").empty();
                $.ajax({
                    type: "GET",
                    url: "taskbysiteandtype?searchtype=" + type + "&siteid=" + siteid,
                    success: function (task) {
                        for (var i = 0; i < task.length; i++) {
                            $("#taskid").append("<option  value='" + task[i].id + "' >" + task[i].customid + "</option>");
                        }
                    }
                })
            }
        }
        function validateForm(){
            var siteid = $("#name2").val();
            if(siteid == null || siteid == undefined || siteid == ''){
                alert("您还没有选择厂区哟！！！");
                return false;
            }

            var taskid = $("#taskid").val();
            if(taskid == null || taskid == undefined || taskid == ''){
                alert("您还没有选择任务哟！！！");
                return false;
            }

            var exceptiontype = $("#exceptiontype").val();
            if(exceptiontype==null || exceptiontype == ""){
                alert("请选择需要上传的异常类型！");
                return false;
            }

            var exceptionlever = $("#exceptionlever").val();
            if(exceptionlever==null || exceptionlever == ""){
                alert("请选择需要上传的异常等级！");
                return false;
            }

            var state = $("#uploadstate").val();
            if(state==null || state == ""){
                alert("请选择是否需要上传！");
                return false;
            }
            var contactinfo = $("#contactinfo").val();
            if(contactinfo==null || contactinfo == ""){
                alert("请选择联系人说！");
                return false;
            }


        }
    </script>
</head>
<body>
<div class="ch-container">
    <div class="row">
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i> 任务上传--添加
                            </h2>
                        </div>
                        <div class="box-content">
                            <span style="color: #9a161a">${message}</span>
                            <form action="addtaskupload" method="post" id="form2" onsubmit="return validateForm()">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="name2">厂区:</label>
                                           <select class="form-control" name="siteid" id="name2" required="required" onchange="tasknum()">
                                               <option value="">--请选择--</option>
                                               <c:forEach items="${siteareainfos}" var="site">
                                                   <option value="${site.id}">${site.customid}</option>
                                               </c:forEach>
                                           </select>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="taskid">任务:</label>
                                            <select class="form-control" id="taskid" name="taskid" >
                                                <c:choose>
                                                    <c:when test="${taskid==null and taskid==''}">
                                                        <option value="" selected>所有任务</option>
                                                    </c:when>
                                                </c:choose>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="exceptiontype">上传异常类型:</label>
                                            <select name="exceptiontype" id="exceptiontype" class="form-control" multiple required>
                                                <c:forEach items="${exceptiontypeList}" var="type">
                                                    <option value="${type.name}">${type.name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="exceptionlever">上传异常等级:</label>
                                            <select name="exceptionlever" id="exceptionlever" class="form-control" multiple required>
                                                <c:forEach items="${levertype1List}" var="lever">
                                                    <option value="${lever.name}" >${lever.name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="uploadstate">是否上传:</label>
                                            <select class="form-control"  id="uploadstate" name="uploadstate">
                                                <option selected="selected" value="1">是</option>
                                                <option value="0">否</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="contactinfo">联系人:</label>
                                            <select class="form-control" name="contactid" id="contactinfo" required="required">
                                                <c:forEach items="${contactinfoList}" var="info">
                                                    <option value="${info.id}">${info.name}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td><input type="submit" class="btn btn-primary" value="保存">
                                            <input type="button" class="btn btn-primary white" value="取消" onclick="javascript:history.go(-1);"></td>
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
</body>
</html>
