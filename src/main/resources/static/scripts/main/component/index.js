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
    }
    function addQuestion() {
        $(".js-submitQuestion").on("click",function () {
            var title=$(".js-addTitle").val();
            var content=$(".js-addContent").val();
            if(title==""){
                alert("问题标题不能为空！");
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
                    window.location.reload()
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
        that.pageSize=5;
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
                console.log(oResult);
                // // 是否有更多数据
                // that.listHasNext = !!oResult.has_next && (oResult.images || []).length > 0;
                // // 更新当前页面
                // that.page++;
                // // 渲染数据
                // var sHtml = '';
                // $.each(oResult.images, function (nIndex, oImage) {
                //     var cur_page_id = (that.page - 1) * that.pageSize + nIndex + 1;
                //     sHtml_part1 = that.tpl([
                //         '<article class="mod">',
                //         '<header class="mod-hd">',
                //         '<time class="time">#{created_date}</time>',
                //         '<a href="/profile/#{user_id}" class="avatar">',
                //         '   <img src="#{head_url}">',
                //         '</a>',
                //         '<div class="profile-info">',
                //         '<a title="#{user_name}" href="/profile/#{user_id}">#{user_name}</a>',
                //         '</div>',
                //         '</header>',
                //         '<div class="mod-bd">',
                //         '<div class="img-box">',
                //         '<a href = "/image/#{id}">',
                //         '<img src="#{url}">',
                //         ' </div>',
                //         ' </div>',
                //         ' <div class="mod-ft">',
                //         '  <ul class="discuss-list">',
                //         ' <li class="more-discuss js-discuss-list">',
                //         ' <a>',
                //         '<a href = "/image/{{image.id}}">',
                //         ' <span>全部</span><span class="">#{comment_count}</span>',
                //         '<span>条评论</span></a>',
                //         '</li>',
                //         '<div class="js-discuss-list-',
                //         cur_page_id.toString(),
                //         '"> </div>'].join(''), oImage);
                //     sHtml_part2 = ' ';
                //
                //     for (var ni = 0; ni < Math.min(2,oImage.comment_count); ni++){
                //         dict = {'comment_user_username':oImage.comments[ni].comment_username, 'comment_user_id':oImage.comments[ni].user_id,
                //             'comment_content':oImage.comments[ni].content };
                //
                //         sHtml_part2 += that.tpl([
                //             '    <li>',
                //             '    <a class="_4zhc5 _iqaka" title="#{comment_user_username}" href="/profile/#{comment_user_id}" data-reactid=".0.1.0.0.0.2.1.2:$comment-17856951190001917.1">#{comment_user_username}</a>',
                //             '    <span>',
                //             '        <span>#{comment_content}</span>',
                //             '     </span>',
                //             '   </li>',
                //         ].join(''), dict);
                //     }
                //
                //     sHtml_part3 =    that.tpl([
                //         '</div>',
                //         '  </ul>',
                //         '<section class="discuss-edit">',
                //         '<a class="icon-heart-empty"></a>',
                //         '<form>',
                //         ' <input placeholder="添加评论..." id="jsCmt-',
                //         cur_page_id.toString(),
                //         '" type="text">',
                //         '<input id = "js-image-id-',
                //         cur_page_id.toString(),
                //         '" type = "text" style="display: none" value="#{id}">',
                //         '</form>',
                //         ' <button class="more-info" id="jsSubmit-',
                //         cur_page_id.toString(),
                //         '">更多选项</button>',
                //         '</section>',
                //         ' </div>',
                //
                //         ' </article>  '
                //     ].join(''), oImage);
                //
                //     sHtml += sHtml_part1 + sHtml_part2 + sHtml_part3;
                // });
                // sHtml && that.listEl.append(sHtml);
            },
            error: function () {
                alert('出现错误，请稍后重试');
            },
            always: fCb
        });
        // setTimeout(detail_index, 1000);
    }
    function requestData(oConf) {
        var that = this;
        var sUrl = '/index/renderMore' ;
            // + oConf.page + '/' + oConf.pageSize + '/';
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: {"userId":0,"offset":0,"limit":10},
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

