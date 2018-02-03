package com.prohua.viewstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Deep on 2017/4/24 0024.
 */

public class CommentTextView extends android.support.v7.widget.AppCompatTextView {

    private boolean click = true;
    private Context mContext;
    private CommentModel commentModel;

    public CommentModel getCommentModel() {
        return commentModel;
    }

    public void setCommentModel(CommentModel commentModel) {
        this.commentModel = commentModel;
    }

    public CommentTextView(Context context) {
        super(context);
        mContext = context;
    }

    public CommentTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CommentTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    /**
     * 设置回复的可点击文本
     *
     * @param model
     * @return
     */
    public void setReply(CommentModel model) {
        commentModel = model;
        SpannableString spanableInfo;
        String str;
        if (model.getReplyUid() != 0) {
            str = model.getReviewUName() + " 回复 " + model.getReplyUName() + "：" + model.getReviewContent();
            spanableInfo = new SpannableString(str);
            spanableInfo.setSpan(new ReviewClickable(ReviewClickListener),
                    0, model.getReviewUName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanableInfo.setSpan(new ReplyClickable(ReplyClickListener),
                    model.getReviewUName().length()+4, model.getReviewUName().length() + 4 + model.getReplyUName().length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanableInfo.setSpan(new ContentClickable(ContentClickListener),
                    model.getReviewUName().length() + 4 + model.getReplyUName().length() + 1,
                    str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            str = model.getReviewUName() + "：" + model.getReviewContent();
            spanableInfo = new SpannableString(str);
            spanableInfo.setSpan(new ReviewClickable(ReviewClickListener),
                    0, model.getReviewUName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanableInfo.setSpan(new ContentClickable(ContentClickListener),
                    model.getReviewUName().length() + 1,
                    str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setHighlightColor(getResources().getColor(android.R.color.transparent));
        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spanableInfo);
        commentModel = model;
    }

    private View.OnClickListener ReviewClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "点击了回复者"+commentModel.getReviewUName(), Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener ReplyClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "点击了被回复者"+commentModel.getReplyUName(), Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener ContentClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "点击了正文"+commentModel.getReviewContent(), Toast.LENGTH_SHORT).show();
        }
    };

    class ReviewClickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public ReviewClickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
        }
    }

    class ReplyClickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public ReplyClickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
        }
    }

    class ContentClickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public ContentClickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            if(click) {
                mListener.onClick(v);
            } else {
                click = true;
            }
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.GRAY);
        }

    }
}
