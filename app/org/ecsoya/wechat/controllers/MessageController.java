package org.ecsoya.wechat.controllers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.mvc.Controller;
import play.mvc.Result;

public class MessageController extends Controller {

	public Result send() {
		Document body = request().body().asXml();
		NodeList contents = body.getElementsByTagName("Content");
		if (contents != null && contents.getLength() != 0) {
			Node item = contents.item(0);
			return ok(item.getTextContent());
		}
		return ok("sent");
	}
}
