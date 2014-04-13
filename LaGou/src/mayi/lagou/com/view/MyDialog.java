package mayi.lagou.com.view;

import mayi.lagou.com.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog {

	private Button dialogCancelButton;
	private Button dialogOkButton;
	private TextView message;
	private String smessage;

	public MyDialog(Context context) {
		super(context, R.style.MyDialog);
		this.setCanceledOnTouchOutside(true);
	}

	public MyDialog(Context context, boolean isCanTouchOut) {
		super(context, R.style.MyDialog);
		this.setCanceledOnTouchOutside(isCanTouchOut);
	}

	public MyDialog(Context context, String smessage) {
		super(context, R.style.MyDialog);
		this.smessage = smessage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.v_mydialog);
		dialogOkButton = (Button) this.findViewById(R.id.dialog_button_ok);
		dialogCancelButton = (Button) this.findViewById(R.id.dialog_button_cancel);
		message = (TextView) this.findViewById(R.id.message);
		message.setMovementMethod(ScrollingMovementMethod.getInstance());
		this.message.setText(smessage);
	}

	public void setMessage(String text) {
		message.setText(text);
		message.scrollTo(0, 0);
	}

	public void setOkBtnText(String text) {
		dialogOkButton.setText(text);
	}

	public void setCancelBtnText(String text) {
		dialogCancelButton.setText(text);
	}

	public void setOkBtnOnClickListener(android.view.View.OnClickListener mOkOnClickListener) {
		dialogOkButton.setVisibility(View.VISIBLE);
		dialogOkButton.setOnClickListener(mOkOnClickListener);
	}

	public void setCancelBtnOnClickListener(android.view.View.OnClickListener mCancelOnClickListener) {
		dialogCancelButton.setVisibility(View.VISIBLE);
		dialogCancelButton.setOnClickListener(mCancelOnClickListener);
	}

	public void showDefaultOkBtn() {
		dialogOkButton.setVisibility(View.VISIBLE);
		dialogOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyDialog.this.dismiss();
			}
		});
	}

	public void showDefaultCancelBtn() {
		dialogCancelButton.setVisibility(View.VISIBLE);
		dialogCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyDialog.this.dismiss();
			}
		});
	}

	@Override
	public void show() {
		super.show();
		dialogOkButton.setVisibility(View.GONE);
		dialogCancelButton.setVisibility(View.GONE);
	}
}