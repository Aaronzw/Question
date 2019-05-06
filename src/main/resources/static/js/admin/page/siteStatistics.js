/**

 @Name：管理后台-网站统计界面
 @Author：庄巍
 */
layui.define(['element', 'form','laypage','jquery','laytpl','common','table'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common
        ,layer=layui.layer
        ,table=layui.table;
    $(function () {
        initList();
    });
    function initList(){
        common.ajax("/requestStatisticsDatas",{},function (res) {
            console.log(res)
            if(res.code=="0"){
                var userCnt=res.userCnt;
                var adminCnt=res.adminCnt;
                var rootCnt=res.rootCnt;
                var quesCnt=res.questionCnt;
                var commentCnt=res.commentCnt;
                var answerCnt=res.answerCnt;
                $(".js-user-cnt").html(userCnt);
                $(".js-admin-cnt").html(adminCnt);
                $(".js-root-cnt").html(rootCnt);
                $(".js-ques-cnt").html(quesCnt);
                $(".js-ans-cnt").html(answerCnt);
                $(".js-comment-cnt").html(commentCnt);
            }else {
                layer.msg("请求数据失败");
            }
        })

    }

    //输出接口
    exports('siteStatistics', {});
});
