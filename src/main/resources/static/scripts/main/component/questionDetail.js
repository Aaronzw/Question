$(document).ready(function () {
    initData();
    initLikeOrDisLike();
    function initData(){
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
                        console.log(item);
                        var html='<div class="question-answer-wrap">\n' +
                            '            <div class="item-answer" data-answer-id='+item.comment.id+'>\n' +
                            '                <div class="votebar"><!--赞同和反对按钮，赞同数-->\n' +
                            '                    <div class="zm-votebar js-vote" data-id="'+item.comment.id+'">\n' ;
                        if(item.liked>0)
                            html=html+'<button class="up js-like pressed" title="赞同" data-id="'+item.comment.id+'">\n';
                        else
                            html=html+'<button class="up js-like" title="赞同" data-id="'+item.comment.id+'">\n';

                        html=html+ '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <span class="count js-voteCount">'+item.likeCount+'</span>\n' +
                            '                            <span class="label sr-only">赞同</span>\n' +
                            '                        </button>\n';
                        if(item.liked<0)
                        {
                            html+='                        <button class="down js-dislike pressed" title="反对">\n';
                        }else {
                            html+='                        <button class="down js-dislike" title="反对">\n';
                        }
                        html+= '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <span class="label sr-only">反对，不会显示你的姓名</span>\n' +
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
                            '                    <div class="vote-count-info" data-vote-count='+item.likeCount+'>\n' +
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
                            '            <div class="item-answer" data-answer-id='+item.comment.id+'>\n' +
                            '                <div class="votebar"><!--赞同和反对按钮，赞同数-->\n' +
                            '                    <div class="zm-votebar js-vote" data-id="'+item.comment.id+'">\n' ;
                        if(item.liked>0)
                            html=html+'<button class="up js-like pressed" title="赞同">\n';
                        else
                            html=html+'<button class="up js-like" title="赞同">\n';

                        html=html+ '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <span class="count js-voteCount">'+item.likeCount+'</span>\n' +
                            '                            <span class="label sr-only">赞同</span>\n' +
                            '                        </button>\n';
                        if(item.liked<0)
                        {
                            html+='                        <button class="down js-dislike pressed" title="反对">\n';
                        }else {
                            html+='                        <button class="down js-dislike" title="反对">\n';
                        }
                        html+= '                            <i class="icon vote-arrow" aria-pressed="false"></i>\n' +
                            '                            <span class="label sr-only">反对，不会显示你的姓名</span>\n' +
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
                            '                    <div class="vote-count-info" data-vote-count='+item.likeCount+'>\n' +
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

    function initLikeOrDisLike(){

        $("js-like").each(
            $(this).on('click',function (e) {
                var oEl = e.currentTarget;
                console.log(oEl==this)
            })
        );
        $(".js-dislike").click(function(){
            alert($(this).index());
        })
        // $('.js-load-more').on('click', function (oEvent) {
        //     var oEl = $(oEvent.currentTarget);
        //     var sAttName = 'data-load';
        //     // 正在请求数据中，忽略点击事件
        //     if (oEl.attr(sAttName) === '1') {
        //         return;
        //     }
        //     // 增加标记，避免请求过程中的频繁点击
        //     oEl.attr(sAttName, '1');
        //     that.renderMore(function () {
        //         // 取消点击标记位，可以进行下一次加载
        //         oEl.removeAttr(sAttName);
        //         // 没有数据隐藏加载更多按钮
        //         !that.listHasNext && oEl.hide();
        //     });
        // });
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


