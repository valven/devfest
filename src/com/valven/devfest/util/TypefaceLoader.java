package com.valven.devfest.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;

public class TypefaceLoader {
	private static LruCache<String, Typeface> cache = new LruCache<String, Typeface>(10);

	public static Typeface getTypeface(Context ctx, String asset) {
		Typeface tf = cache.get(asset);
		if (tf == null){
			try {
				tf = Typeface.createFromAsset(ctx.getAssets(), asset);
			} catch (Exception e) {
				Log.w("customFont", "Could not get typeface: " + e.getMessage(), e);
			}
			cache.put(asset, tf);
		}
		return tf;
	}
}
