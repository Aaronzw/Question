/**

 @Name：搜索结果列表
 @Author：庄巍

 */
layui.define(['element', 'form','laypage','jquery','laytpl','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common
        ,layer=layui.layer;
    //statr 分页

    $(function () {
        var that=this;
        that.keyWord=$("input[name='key']").val();
        initQuestion();
        initUser();
    })
    function initQuestion(){
        var qTotals=$("input[name='questionTotals']").val();
        var that=this;
        that.qCur=1;
        that.qPageSize=3;
        that.keyWord=$("input[name='key']").val();
        laypage.render({
            elem: 'question-page',
            count:Number(qTotals),
            curr: that.qCur,
            limit: that.qPageSize,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);
                var question_id=$("input[name='questionId']").val();
                common.ajax("/search/question/request",{
                    "limit":that.qPageSize,
                    "offset":obj.curr,
                    "keyWord":that.keyWord,
                },function (res) {
                    if(res.code=="0"){
                        $(".js-search-question-list").html("");
                        $.each(res.data,function (index, item) {
                            console.log(item.question.title);
                            var html='<div class="item" style="margin-bottom: 0;padding-top: 0px">\n' +
                                '                                <div class="item-box  layer-photos-demo1 layer-photos-demo">\n' +
                                '                                    <div class="question_info">\n' +
                                '                                        <h3>\n' +
                                '                                            <a class="question_title" href="/question/'+item.question.id+'">\n' +
                                item.question.title+
                                '                                            </a>\n' +

                                '                                        </h3>\n' +
                                '<p>'+item.question.content+'</p>'+
                                '                                    </div>\n' +
                                '                                    <div class="ans_author_info">\n' +
                                '                                        <div class="author_head pull-right">\n' +
                                '                                            <a href="/user/'+item.user.id+'">\n' +
                                '                                                <img src="'+item.user.headUrl+'" class="author-head-img">\n' +
                                '                                            </a>\n' +
                                '                                        </div>\n' +
                                '                                        <span class="author_info"><a href="/user/'+item.user.id+'">'+item.user.name+'</a> </span>\n' +
                                '                                        <h5 class="answer_date p">提问于：<span>'+item.question.createdDate+'</span></h5>\n' +
                                '                                    </div>\n' +
                                '                                </div>\n' +
                                '                            </div>';
                            $(".js-search-question-list").append(html);
                            $(window).scrollTop(0);
                        });
                    }else {
                        layer.msg("ajax请求失败");
                    }
                })
            }
        });
    }
    function initUser(){
        var uTotals=$("input[name='userTotals']").val();
        var that=this;
        that.uCur=1;
        that.uPageSize=3;
        that.keyWord=$("input[name='key']").val();
        laypage.render({
            elem: 'user-page',
            count:Number(uTotals),
            curr: that.uCur,
            limit: that.uPageSize,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);
                common.ajax("/search/user/request",{
                    "limit":that.qPageSize,
                    "offset":obj.curr,
                    "keyWord":that.keyWord,
                },function (res) {
                    console.log(res);
                    if(res.code=="0"){
                        $(".js-search-user-list").html("");
                        $.each(res.data,function (index, item) {
                            console.log(item);
                            var html='<li class="item">\n' +
                                '                                <div class="letter-info">\n' +
                                '                                    <div class="l-operate-bar">\n' +
                                '                                        <a class="layui-btn layui-btn-sm" data-sender-id="'+item.user.id+'" href="/sendMsgTo/'+item.user.id+'">联系TA</a>\n' +
                                '                                        <span class="js-replacebtn" data-userid="'+item.user.id+'"> ' +
                                '                                           <a class="layui-btn layui-btn-sm follow-btn js-follow" href="javascript:void(0);">关注TA\n </a>' +
                                '                                        </span>\n' +
                                '                                    </div>\n' +
                                '                                </div>\n' +
                                '                                <div class="chat-headbox">\n' +
                                '                                    <a class="list-head" href="/user/'+item.user.id+'">\n' +
                                '                                        <img alt="头像" src="'+item.user.headUrl+'">\n' +
                                '                                    </a>\n' +
                                '                                </div>\n' +
                                '                                <div class="letter-detail">\n' +
                                '                                    <a href="/user/'+item.user.id+'" class="letter-name level-color-7">'+item.user.name+'</a>\n' +
                                '                                    <p class="letter-brief">' +
                                '                                         <a href="javacvript:;" style="font-size: small;color: gray">粉丝'+item.followerCnt+'</a>' +
                                '                                         <a href="javacvript:;" style="font-size: small;color: gray">关注'+item.followeeCnt+'</a> ' +
                                '                                    </p>\n' +
                                '                                </div>\n' +
                                '                            </li>';
                            $(".js-search-user-list").append(html);
                            $(window).scrollTop(0);
                        });
                    }else {
                        layer.msg("ajax请求失败");
                    }
                })
            }
        });
    }

    //输出msg_list接口
    exports('result', {});
});
