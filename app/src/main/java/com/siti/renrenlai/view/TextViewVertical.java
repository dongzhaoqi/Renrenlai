package com.siti.renrenlai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewVertical extends TextView {
  
    public TextViewVertical(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }
  
    public TextViewVertical(Context context) {  
        super(context);  
    }
  
    @Override  
    public void setText(CharSequence text, BufferType type) {  
        if ("".equals(text) || text == null || text.length() == 0) {
            return;  
        }  
        int m = text.length();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < m; i++) {  
            CharSequence index = text.toString().subSequence(i, i + 1);  
            sb.append(index + "\n");  
        }  
        super.setText(sb, type);  
    }  
  
}  