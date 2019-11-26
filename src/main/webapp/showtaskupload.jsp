<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
            var taskId='${taskid}';
            var type="";
            if(siteid ==null || siteid==''){
                $("#taskid").append("<option  value='' selected>所有任务</option>");
            }else {
                $("#taskid").empty();
                $.ajax({
                    type: "GET",
                    url: "taskbysiteandtype?searchtype=" + type + "&siteid=" + siteid,
                    success: function (task) {
                        for (var i = 0; i < task.length; i++) {
                            if (task[i].id == taskId) {
                                $("#taskid").append("<option  value='" + task[i].id + "' selected>" + task[i].customid + "</option>");
                            } else {
                                $("#taskid").append("<option  value='" + task[i].id + "' >" + task[i].customid + "</option>");
                            }
                        }
                    }
                })
            }
        }

        function deltaskupload(id){
            var isDel=confirm('确定删除吗？');
            if (isDel!=true) {
                return false;
            }else {
                $.ajax({
                    url: "deltaskupload?id=" + id,
                    type: "post",
                    datatype: "json",
                    success: function (data) {
                        if (data > 0) {
                            alert("删除成功");
                            showtask();
                        } else {
                            alert("删除失败");
                            return false;
                        }
                    }
                });
            }
        }
        function showtask(){
            window.location="showalltaskupload?page="+$("#page").val();
        }
    </script>
</head>
<body>
<input type="hidden" id="page" value="${pageBean.currentPage}">
<div class="ch-container" style="overflow: hidden">
    <div class="row">
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                                <h2>
                                    <i class="glyphicon glyphicon-globe"></i> 任务上传配置列表
                                </h2>
                        </div>
                        <div class="box-content">
                            <div class="form-inline" >
                                <form action="showalltaskupload?page=1" method="post">
                                    <label class="control-label" for="name2">厂区：</label>
                                    <select id="name2" class="form-control" name="siteid" onchange="tasknum()">
                                        <c:if test="${siteid==null}">
                                            <option value="">所有厂区</option>
                                        </c:if>
                                        <c:forEach items="${sites}" var="site">
                                            <option  value="${site.id}" ${siteid eq site.id ?'selected':''}>${site.customid}</option>
                                        </c:forEach>
                                    </select>
                                    <label class="control-label" for="taskid">任务号：</label>
                                    <select class="form-control" id="taskid" name="taskid" >
                                        <c:choose>
                                            <c:when test="${taskid==null and taskid==''}">
                                                <option value="" selected>所有任务</option>
                                            </c:when>
                                        </c:choose>
                                    </select>
                                    <input type="submit" class="btn btn-primary" value="搜索" style="margin-left: 30px;margin-top: 10px">
                                    <shiro:hasPermission name="add:taskupload">
                                        <a href="toaddtaskupload" id="button" class="btn btn-primary"style="margin-left:250px;">添加接口</a>
                                    </shiro:hasPermission>
                                </form>

                            </div>
                        </div>
                            <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable" >
                                <tr style="text-align: center">
                                    <th>操作</th>
                                    <th>厂区</th>
                                    <th>任务名</th>
                                    <th>上传异常类型</th>
                                    <th>上传异常等级</th>
                                    <th>是否上传</th>
                                    <th>联系人</th>
                                    <th>创建时间</th>
                                </tr>
                                <c:forEach items="${pageBean.list}" var="taskuploadconfig">
                                    <tr>
                                        <td>
                                            <shiro:hasPermission name="upd:taskupload">
                                                <a href="findbytaskuploadid?id=${taskuploadconfig.id}&page=${pageBean.currentPage}"><i class="glyphicon glyphicon-edit red "></i></a>
                                            </shiro:hasPermission>
                                            <shiro:hasPermission name="del:taskupload">
                                                <a href="javascript:void(0)" onclick="deltaskupload('${taskuploadconfig.id}')"><i class="glyphicon glyphicon-trash"></i></a>
                                            </shiro:hasPermission>
                                        </td>
                                        <td>${taskuploadconfig.sitename}</td>
                                        <td>${taskuploadconfig.taskname}</td>
                                        <td>${taskuploadconfig.exceptiontype}</td>
                                        <td>${taskuploadconfig.exceptionlever}</td>
                                        <td>
                                            <c:if test="${taskuploadconfig.uploadstate==0}">否</c:if>
                                            <c:if test="${taskuploadconfig.uploadstate==1}">是</c:if>
                                        </td>
                                        <td>${taskuploadconfig.contactname}</td>
                                        <td><sdf:formatDate value="${taskuploadconfig.createtime}" pattern="yyyy-MM-dd HH:mm:ss"></sdf:formatDate></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        <div style="height: 50px;width: 500px;text-align: center;margin-left: 300px;">
                            <a href="showalltaskupload?page=1&siteid=${siteid}&taskid=${taskid}">第一页</a>
                            <c:if test="${pageBean.currentPage>1}">
                                <a href="showalltaskupload?page=${pageBean.currentPage-1}&siteid=${siteid}&taskid=${taskid}">上一页</a>
                            </c:if>

                            <c:if test="${pageBean.currentPage<pageBean.totalPage}">
                                <a href="showalltaskupload?page=${pageBean.currentPage+1}&siteid=${siteid}&taskid=${taskid}">下一页</a>
                            </c:if>

                            <a href="showalltaskupload?page=${pageBean.totalPage}&siteid=${siteid}&taskid=${taskid}">最后一页</a>

                            第${pageBean.currentPage}页/共${pageBean.totalPage}页
                        </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
