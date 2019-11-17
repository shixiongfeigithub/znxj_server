<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <style>
        .fileinput-button {
            position: relative;
            display: inline-block;
            overflow: hidden;
        }

        .fileinput-button input{
            position:absolute;
            right: 0px;
            top: 0px;
            opacity: 0;
            -ms-filter: 'alpha(opacity=0)';
            font-size: 200px;
        }
        .selected{background: #35A7E7;
            color: white;}
    </style>
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
                                <i class="glyphicon glyphicon-globe"></i> 巡检异常处理详情
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="" method="post" >
                            <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline" colspan="2">
                                            <label class="control-label" >巡检项名称:</label>
                                            ${exceptionhandlerinfo.checkname}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >复核员姓名:</label>
                                            ${exceptionhandlerinfo.username}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >处理责任人:</label>
                                            ${exceptionhandlerinfo.operatorname}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >上报时间:</label>
                                            <sdf:formatDate value="${exceptionhandlerinfo.reporttime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >指派时间:</label>
                                            <sdf:formatDate value="${exceptionhandlerinfo.appointedtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >关闭时间:</label>
                                            <sdf:formatDate value="${exceptionhandlerinfo.exceptionclosetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >处理状态:</label>
                                            <c:if test="${exceptionhandlerinfo.exceptionstate == 0}">待处理</c:if>
                                            <c:if test="${exceptionhandlerinfo.exceptionstate == 1}">已关闭</c:if>
                                            <c:if test="${exceptionhandlerinfo.exceptionstate == 2}">处理中</c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >异常类型:</label>
                                            ${exceptionhandlerinfo.exceptiontype}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >异常等级:</label>
                                            ${exceptionhandlerinfo.exceptionlever}

                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >异常上报状态:</label>
                                            <c:if test="${exceptionhandlerinfo.uploadstate == 0}">未上报</c:if>
                                            <c:if test="${exceptionhandlerinfo.uploadstate == 1}">上报成功</c:if>
                                            <c:if test="${exceptionhandlerinfo.uploadstate == 2}">上报失败</c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" >上报时间:</label>
                                            <sdf:formatDate value="${exceptionhandlerinfo.uploadtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline" colspan="2">
                                            <span>描述:</span>
                                            <textarea class="form-control" rows="3"
                                                      cols="150" readonly="readonly">${exceptionhandlerinfo.descontent}</textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline" colspan="2">
                                            <span>图片:</span>
                                            <c:forEach items="${imgList}" var="imgurl" varStatus="status">
                                                <img src='http://${ip}/report/${imgurl}' width="50" height="100">
                                            </c:forEach>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><input type="reset" class="btn btn-primary white" value="返回" onclick="javascript:history.go(-1);"></td>
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
