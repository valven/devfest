package com.valven.devfest.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.valven.devfest.R;

public class MainButton extends LinearLayout {

	public MainButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(attrs);
	}

	public MainButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs) {
		init(attrs, 0);
	}

	private void init(AttributeSet attrs, int style) {
		setBackgroundResource(R.drawable.button_bg);
		setGravity(Gravity.CENTER);
		setOrientation(VERTICAL);

		TypedArray buttonAttrs = getContext().obtainStyledAttributes(attrs,
				R.styleable.MainButton);
		TypedArray textAttrs = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomTextView);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		ImageView image = new ImageView(getContext());
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setLayoutParams(params);
		image.setImageDrawable(buttonAttrs
				.getDrawable(R.styleable.MainButton_src));

		CustomTextView text = new CustomTextView(getContext(), attrs, style);
		text.setLayoutParams(params);
		text.setText(buttonAttrs.getString(R.styleable.MainButton_text));
		text.setCustomFont(getContext(),
				textAttrs.getString(R.styleable.CustomTextView_customFont));
		text.setGravity(Gravity.CENTER_HORIZONTAL);
		buttonAttrs.recycle();
		textAttrs.recycle();

		addView(image);
		addView(text);
	}

}
