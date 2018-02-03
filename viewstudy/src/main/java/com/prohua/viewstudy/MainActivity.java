package com.prohua.viewstudy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommentModel commentModel = new CommentModel();
        commentModel.setReviewUid(4564L);
        commentModel.setReviewUName("样式一");
        commentModel.setReviewContent("点击后的背景颜色(HighLightColor)属于TextView的属性");
        commentModel.setReplyUid(0L);
        final CommentTextView commentTextView = (CommentTextView) findViewById(R.id.hello_world);
        commentTextView.setReply(commentModel);
        commentTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(), "长按", Toast.LENGTH_SHORT).show();
                commentTextView.setClick(false);
                return true;
            }
        });

        CommentModel commentModel2 = new CommentModel();
        commentModel2.setReviewUid(4546L);
        commentModel2.setReviewUName("样式二");
        commentModel2.setReviewContent("点击后的背景颜色(HighLightColor)属于TextView的属性");
        commentModel2.setReplyUid(11L);
        commentModel2.setReplyUName("被回复者");
        CommentTextView commentTextView2 = (CommentTextView) findViewById(R.id.hello_world2);
        commentTextView2.setReply(commentModel2);
    }

}

