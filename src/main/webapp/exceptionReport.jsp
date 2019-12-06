<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>智能巡检系统</title>
    <meta name="renderer" content="webkit" />
    <style>
        #div{
            width: 1500px;
        }
       .table1{
            width: 1200px;
            height: 50px;
           margin-left: 150px;
        }
        table tr{
            height: 50px;
        }
        table tr td{
            line-height: 20px;
            text-align: center;
            vertical-align: middle;
        }
        .table2{
            width: 1200px;
            height: 20px;
            margin-left:150px;
        }
        .modal-footer{
            border-top: 1px  #ffffff !important;
        }
    </style>
</head>
<body>
<div id="div">
    <h3 style="text-align: center">${identifyingid} -- 异常报告</h3>

    <table  border="1" cellspacing="0" class="table2">
        <caption><h4>异常详情</h4></caption>
        <tr>
            <th>报告编号</th>
            <th>执行时间</th>
            <th>上报人</th>
            <th>区域</th>
            <th>设备</th>
            <th>异常项</th>
            <th>描述</th>
            <th>异常记录链接</th>
        </tr>

        <c:forEach items="${courseList}" var="content">
            <tr>
                <td>${content.report.taskcode}</td>
                <td>${content.operationtime}</td>
                <td>${content.report.worker}</td>
                <td>${content.areaname}</td>
                <td>${content.equipname}</td>
                <td>${content.checkname}</td>
                <td>${content.errcontent}</td>
                <td>
                    <c:if test="content.checktype=='状态项'">
                        <c:if test="${content.img != 'null' or content.audio != 'null' or content.video != 'null'}">
                            <a href='http://${ip}/toException?img=${content.img}&audio=${content.audio}&video=${content.video}'  target="_Blank">查看异常详情</a>
                        </c:if>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div style="height: 50px;width: 500px;text-align: center;margin-left: 500px;">
        <a href="http://${ip}/exceptionReport?page1=1&reportId=${reportId}">第一页</a>
        <c:if test="${temppagebean.curPage>1}">
            <a href="http://${ip}/exceptionReport?page1=1&reportId=${reportId}">上一页</a>
        </c:if>

        <c:if test="${temppagebean.curPage<temppagebean.pageCount}">
            <a href="http://${ip}/exceptionReport?page1=1&reportId=${reportId}">下一页</a>
        </c:if>

        <a href="http://${ip}/exceptionReport?page1=1&reportId=${reportId}">最后一页</a>

        第${temppagebean.curPage}页/共${temppagebean.pageCount}页
    </div>
</div>

<%--<div aria-hidden="false" hidden aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal1" class="modal fade in" style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999">
        <div class="modal-content" style="overflow: auto;">
            <div id="arealist">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>显示异常</h3>
                </div>
                <div class="modal-body">
                    <div id="showerror"style="height: 30%;">
                        <div style="width: 150px;float: left;height: 300px;overflow:auto;">
                            <ul class="list-group" id="errorul">

                            </ul>
                        </div>
                        <div id="pic" style="border: 1px solid #F3F3F3; width: 400px;height: 300px;float: left;">

                        </div>
                        &lt;%&ndash;<div class="clearfix" style="display: none;"></div>&ndash;%&gt;
                    </div>
                </div>
                <div class="modal-footer">
                    <a data-dismiss="modal" style="margin-top:20px" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
        </div>
    </div>
</div>--%>
</body>
</html>
