$(document).ready(function () {
    init();
    function init(){
        var that = this;
        var sUrl = '/question/requestMore' ;
        var qid=$("input[name='questionId']").val();
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "questionId":qid,
                "offset":1,
                "limit":5},
        }).done(function (result) {
            if(result.code=="0"){
                var totals=result.totals;
                $("#paginate").pagination({
                    pageSize:5,
                    total: totals,
                    pageIndex:0,
                });
                var data=result.data;
                $(".comment-list").html("");
                if(data.length>0){
                    $.each(data,function (index,item) {
                        console.log(item.comment)
                        console.log(item.user)
                        var html='<div class="question-answer-wrap">\n' +
                            '            <div class="item-answer" data-answer-id=1>\n' +
                            '                <div class="votebar"><!--赞同和反对按钮，赞同数-->\n' +
                            '                    <div class="zm-votebar js-vote" data-id="4">\n' +
                            '                        <button class="up js-like" title="赞同">\n' +
                            '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <span class="count js-voteCount">0</span>\n' +
                            '                            <!--<span class="label sr-only">赞同</span>-->\n' +
                            '                        </button>\n' +
                            '\n' +
                            '                        <button class="down js-dislike" title="反对">\n' +
                            '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <!--<span class="label sr-only">反对，不会显示你的姓名</span>-->\n' +
                            '                        </button>\n' +
                            '                    </div>\n' +
                            '                </div>\n' +
                            '                <div class="answer-head" ><!--名字 头像 赞同数-->\n' +
                            '                    <div class="answer-author-info">\n' +
                            '                        <a class="avatar_link">\n' +
                            '                            <img src="'+item.user.headUrl+'" class="follow-avatar"></a>\n' +
                            '                        </a>\n' +
                            '\n' +
                            '                        <a class="author-link" href="/user/'+item.user.id+'">\n' +
                            item.user.name+'\n' +
                            '                        </a>,\n' +
                            '                        <span title="爱生活" class="intro">\n' +
                            '                            爱生活\n' +
                            '                        </span>\n' +
                            '                    </div>\n' +
                            '                    <div class="vote-count-info" data-vote-count=8>\n' +
                            '                        <a href class="more texts">\n' +
                            '                            <span class="vote-info-text">\n' +
                            '                                <span class="js-vote-count">'+item.likeCount+'</span>人觉得赞同\n' +
                            '                            </span>\n' +
                            '                        </a>\n' +
                            '                    </div>\n' +
                            '                </div>\n' +
                            '                <div class="answer-text"><!--正文-->\n' +
                            item.comment.content+'\n' +
                            '                </div>\n' +
                            '                <div class="answer-actions"><!--发表于  评论数-->\n' +
                            '                    <a class="meta-panel">\n' +
                            '                        <a class="anwswer-date">发布于'+item.comment.createdDate+'</a>\n' +
                            '                        <a name="addComment" class="commentCount">\n' +
                            '                            <i class="z-icon-comment"></i>\n' +
                            '                            0条评论\n' +
                            '                        </a>\n' +
                            '                    </a>\n' +
                            '                </div>\n' +
                            '            </div>\n' +
                            '            <!--<div class=""></div>-->\n' +
                            '            <!--<div class=""></div>-->\n' +
                            '        </div>\n' +
                            '        <hr/>';
                        $(".comment-list").append(html);
                    })
                }
                $("#paginate").on("pageClicked", function (event, data) {
                    //分页按钮点击事件
                    requestDataForPage(data.pageIndex);
                    $(window).scrollTop(0);
                });
            }else {
                alert("请求数据出错 "+result.msg)
            }
        }).fail(function () {
            console.log("fail")
        }).always(function () {
            console.log("always")
        });
    }

    function requestDataForPage(offset) {
        var that = this;
        var sUrl = '/question/requestMore' ;
        var qid=$("input[name='questionId']").val();
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "questionId":qid,
                "offset":offset+1,
                "limit":5},
        }).done(function (result) {
            console.log(result.data)
            if(result.code=="0"){
                var data=result.data
                $(".comment-list").html("");
                if(data.length>0){
                    $.each(data,function (index,item) {
                        // console.log(item.comment)
                        // console.log(item.user)
                        var html='<div class="question-answer-wrap">\n' +
                            '            <div class="item-answer" data-answer-id=1>\n' +
                            '                <div class="votebar"><!--赞同和反对按钮，赞同数-->\n' +
                            '                    <div class="zm-votebar js-vote" data-id="4">\n' +
                            '                        <button class="up js-like" title="赞同">\n' +
                            '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <span class="count js-voteCount">0</span>\n' +
                            '                            <!--<span class="label sr-only">赞同</span>-->\n' +
                            '                        </button>\n' +
                            '\n' +
                            '                        <button class="down js-dislike" title="反对">\n' +
                            '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <!--<span class="label sr-only">反对，不会显示你的姓名</span>-->\n' +
                            '                        </button>\n' +
                            '                    </div>\n' +
                            '                </div>\n' +
                            '                <div class="answer-head" ><!--名字 头像 赞同数-->\n' +
                            '                    <div class="answer-author-info">\n' +
                            '                        <a class="avatar_link">\n' +
                            '                            <img src="'+item.user.headUrl+'" class="follow-avatar"></a>\n' +
                            '                        </a>\n' +
                            '\n' +
                            '                        <a class="author-link" href="/user/'+item.user.id+'">\n' +
                            item.user.name+'\n' +
                            '                        </a>,\n' +
                            '                        <span title="爱生活" class="intro">\n' +
                            '                            爱生活\n' +
                            '                        </span>\n' +
                            '                    </div>\n' +
                            '                    <div class="vote-count-info" data-vote-count=8>\n' +
                            '                        <a href class="more texts">\n' +
                            '                            <span class="vote-info-text">\n' +
                            '                                <span class="js-vote-count">'+item.likeCount+'</span>人觉得赞同\n' +
                            '                            </span>\n' +
                            '                        </a>\n' +
                            '                    </div>\n' +
                            '                </div>\n' +
                            '                <div class="answer-text"><!--正文-->\n' +
                            item.comment.content+'\n' +
                            '                </div>\n' +
                            '                <div class="answer-actions"><!--发表于  评论数-->\n' +
                            '                    <a class="meta-panel">\n' +
                            '                        <a class="anwswer-date">发布于'+item.comment.createdDate+'</a>\n' +
                            '                        <a name="addComment" class="commentCount">\n' +
                            '                            <i class="z-icon-comment"></i>\n' +
                            '                            0条评论\n' +
                            '                        </a>\n' +
                            '                    </a>\n' +
                            '                </div>\n' +
                            '            </div>\n' +
                            '            <!--<div class=""></div>-->\n' +
                            '            <!--<div class=""></div>-->\n' +
                            '        </div>\n' +
                            '        <hr/>';
                        $(".comment-list").append(html);
                    })
                }
            }else {
                alert("请求数据出错 "+result.msg)
            }


        }).fail(function () {
            console.log("fail")
        }).always(function () {
            console.log("always")
        });
    }
    /*发表回答ajax post*/
    $(".js-submitAnswer").on("click",function () {
        var content=$(".js-addComment").val();
        var  qid=$('input[name="questionId"]').val();
        var userId=$('input[name="uid"]').val();
        if(content==""){
            alert("回答不能为空！");
            return
        }
        var flag=false;
        if(flag){
            return false;
        }
        flag=true;

        $.ajax({
            url: '/comment/add',
            type: 'post',
            data: {"questionId":qid,
                    "content":content,
                    "userId":userId},
            dataType: 'json'
        }).done(function (oResult) {
            //alert("done")
            //未登陆，跳转到登陆页面
            console.log(oResult);
            if(oResult.code==0){
                alert("发表回答成功！");
                window.location.reload()
                $(window).scrollTop(0);
            }else {
                alert("发表回答失败！"+oResult.msg);
                window.location.reload()
            }
        }).fail(function () {
            console.log("fail");
            alert('出现错误，请重试');
            window.location.reload();
        }).always(function () {
            console.log("always");
            bSubmit = false;
        });
    });
    /*发表问题弹框*/
    $(".js-submitQuestion").on("click",function () {
        var title=$(".js-addTitle").val();
        var content=$(".js-addContent").val();
        if(title==""){
            alert("问题标题不能为空！");
            return
        }
        var flag=false;
        if(flag){
            return false;
        }
        flag=true;

        $.ajax({
            url: '/question/add',
            type: 'post',
            data: {"title":title,"content":content},
            dataType: 'json'
        }).done(function (oResult) {
            // 未登陆，跳转到登陆页面
            console.log(oResult);
            if(oResult.code==0){
                alert("发表问题成功！");
                window.location.href="/question/"+oResult.question_id;
            }else {
                alert("发表问题失败！"+oResult.msg);
                window.location.reload()
            }
        }).fail(function () {
            alert('出现错误，请重试');
            window.location.reload();
        }).always(function () {
            bSubmit = false;
        });
    });

});


