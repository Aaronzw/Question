$(function () {
    var oExports = {
        initialize: fInitialize,
        encode: fEncode,
        renderMore:fRenderMore,
    };
    oExports.initialize();

    function fInitialize() {
        var that = this;
        addQuestion();
        iniClicKMoreEvent();
        initRenderAuto();
    }
    function initRenderAuto() {

    }
    function addQuestion() {
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
    }
    function iniClicKMoreEvent() {
        var that=this;
        that.page=1;
        that.pageSize=6;
        that.listHasNext=true;

        $('.js-load-more').on('click', function (oEvent) {
            var oEl = $(oEvent.currentTarget);
            var sAttName = 'data-load';
            // 正在请求数据中，忽略点击事件
            if (oEl.attr(sAttName) === '1') {
                return;
            }
            // 增加标记，避免请求过程中的频繁点击
            oEl.attr(sAttName, '1');
            fRenderMore(function () {
                // 取消点击标记位，可以进行下一次加载
                oEl.removeAttr(sAttName);
                // 没有数据隐藏加载更多按钮
                !that.listHasNext && oEl.hide();
            });
        });
    }
    function fRenderMore(fCb) {
        var that = this;
        // 没有更多数据，不处理
        if (!that.listHasNext) {
            return;
        }
        requestData({
            uid: that.uid,
            page: that.page + 1,
            pageSize: that.pageSize,
            call: function (oResult) {
                //console.log(oResult);
                that.page++;
                that.listHasNext=oResult.has_next;
                if(oResult.code==0){
                    $.each(oResult.data,function (Index, Item) {
                        // console.log(Index);
                        // console.log(Item.question);
                        var html;
                        html='<div class="feed-item" data-queestion-id="'+Item.question.id+'">'+
                            '<div class="item-left">'+
                            '<div class="avatar">'+
                            '<a title="" class="item-link-avatar" target="_blank"'+
                            'href="/user/'+Item.user.id+'">'+
                            '<img src="'+Item.user.headUrl+'" class="zm-item-img-avatar"></a>'+
                            '</div>'+
                            '<div class="avatar item-vote">'+
                            '<a class="item-vote-cnt" href="javascript:;" data-bind-votecount="">4168</a>'+
                            '</div>'+
                            '</div>'+
                            '<div class="item-main">'+
                            '<h2 class="item-title">'+
                            '<a class="question_link" target="_blank" href="/question/'+Item.question.id+'">'+Item.question.title+'</a>'+
                            '</h2>'+
                            '<div class="item-author-info">'+
                            '<a class="author-link" target="_blank" href="/user/'+Item.user.id+'">'+Item.user.name+'</a>'+
                            '，'+Item.question.createdDate+
                            '</div>'+
                            '<div class="item-question-content" data-author-name="qq" data-entry-url="/question/'+Item.question.id+'/answer/13174385">'+
                            '<div class="answer-summary">'+
                            Item.question.content+
                            '<a href="javascript:;"style="color: #1b6d85">查看全文</a>'+
                            '</div>'+

                            '<div class="whole-answer" style="display: none">'+
                            +Item.question.id+
                            '<a href="javascript:;" style="color: #1b6d85">收起</a>'+
                            '</div>'+
                            '</div>'+
                            '<div class="feed-mata">'+
                            '<div class="meta-panel">'+
                            '<a data-follow="q:link" class="follow-link zg-follow meta-item" href="javascript:;">'+
                            '<i class="z-icon-follow"></i>关注问题</a>'+
                            '<a href="#" name="addcomment" class="meta-item toggle-comment js-toggleCommentBox">'+
                            '<i class="z-icon-comment"></i>'+Item.question.commentCount+'条评论</a>'+
                            '</div>'+
                            '</div>'+
                            '</div>'+
                            '</div>'
                        $(".js-feed-list").append(html);
                    })
                }else {
                    alert(oResult.msg);
                }
            },
            error: function () {
                alert('出现错误，请稍后重试');
            },
            always: fCb
        });
    }
    function requestData(oConf) {
        var that = this;
        var sUrl = '/index/renderMore' ;
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "userId":0,
                    "offset":oConf.page,
                    "limit":oConf.pageSize},
        }).done(oConf.call).fail(oConf.error).always(oConf.always);
    }
    function fEncode(sStr, bDecode) {
        var aReplace =["&#39;", "'", "&quot;", '"', "&nbsp;", " ", "&gt;", ">", "&lt;", "<", "&amp;", "&", "&yen;", "¥"];
        !bDecode && aReplace.reverse();
        for (var i = 0, l = aReplace.length; i < l; i += 2) {
            sStr = sStr.replace(new RegExp(aReplace[i],'g'), aReplace[i+1]);
        }
        return sStr;
    };

});

