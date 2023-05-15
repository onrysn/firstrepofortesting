package com.mobit;

import com.mobit.DialogResult;

public interface IDialogCallback {

	void DialogResponse(int msg_id, DialogResult result);
}
