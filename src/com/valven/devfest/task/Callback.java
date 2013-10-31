package com.valven.devfest.task;

import org.json.JSONObject;

public interface Callback {
	public void handleResponse(JSONObject response);
}
