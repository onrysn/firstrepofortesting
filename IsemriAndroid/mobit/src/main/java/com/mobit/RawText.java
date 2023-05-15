package com.mobit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

public class RawText extends INode {

	protected List<String> keyList = null;

	static Pattern pattern = Pattern.compile("\\{(.*?)\\}");

	public RawText() {
		keyList = new ArrayList<String>();
	}

	public final List<String> getKeyList() {
		return keyList;
	}

	@Override
	public void get(Element eElement) throws Exception {

		super.get(eElement);

		Matcher matcher = pattern.matcher(Text);
		while (matcher.find())
			keyList.add(matcher.group(1));

		return;
	}

	public String Prepare(IApplication app, PageData pd, IIslem islem) throws Exception {

		return app.rawTextPrepar(this, pd, islem);
	}

}
