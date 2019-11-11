<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript">
        function delainterface(id){
            var isDel=confirm('确定删除吗？');
            if (isDel!=true) {
                return false;
            }else {
                $.ajax({
                    url: "delinterface?id=" + id,
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
            window.location="showallinterface?page="+$("#page").val();
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
                                    <i class="glyphicon glyphicon-globe"></i> 接口配置管理列表
                                </h2>
                        </div>
                        <div class="box-content">
                            <div class="form-inline" style="float: right;">
                                <shiro:hasPermission name="add:interface">
                                    <a href="toaddinterface" id="button" class="btn btn-primary"style="margin-left:250px;">添加接口</a>
                                </shiro:hasPermission>
                            </div>
                        </div>
                            <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable" >
                                <tr style="text-align: center">
                                    <th>操作</th>
                                    <th>厂区</th>
                                    <th>ip</th>
                                    <th>端口号</th>
                                    <th>接口类型</th>
                                    <th>状态</th>
                                    <th>创建时间</th>
                                </tr>
                                <c:forEach items="${pageBean.list}" var="engine">
                                    <tr>
                                        <td>
                                            <shiro:hasPermission name="upd:interface">
                                                <a href="findbyinterfaceid?id=${engine.id}&page=${pageBean.currentPage}"><i class="glyphicon glyphicon-edit red "></i></a>
                                            </shiro:hasPermission>
                                            <shiro:hasPermission name="del:interface">
                                                <a href="javascript:void(0)" onclick="delainterface('${engine.id}')"><i class="glyphicon glyphicon-trash"></i></a>
                                            </shiro:hasPermission>
                                        </td>
                                        <td>${engine.sitename}</td>
                                        <td>${engine.ip}</td>
                                        <td>${engine.port}</td>
                                        <td>
                                            <c:if test="${engine.enginetype==1}">苏州接口公司</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${engine.state==0}">无效</c:if>
                                            <c:if test="${engine.state==1}">有效</c:if>
                                        </td>
                                        <td><sdf:formatDate value="${engine.createtime}" pattern="yyyy-MM-dd HH:mm:ss"></sdf:formatDate></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        <div style="height: 50px;width: 500px;text-align: center;margin-left: 300px;">
                            <a href="showallcreatettime?page=1&name=${name}">第一页</a>
                            <c:if test="${pageBean.currentPage>1}">
                                <a href="showallcreatettime?page=${pageBean.currentPage-1}">上一页</a>
                            </c:if>

                            <c:if test="${pageBean.currentPage<pageBean.totalPage}">
                                <a href="showallcreatettime?page=${pageBean.currentPage+1}">下一页</a>
                            </c:if>

                            <a href="showallcreatettime?page=${pageBean.totalPage}">最后一页</a>

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
