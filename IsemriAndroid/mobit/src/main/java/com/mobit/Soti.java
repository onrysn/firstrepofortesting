package com.mobit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Genel on 11.10.2018.
 */

public class Soti {


    public static void setUserInfo(File file, String userId, String username, String isletme)
            throws ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();
        Element eUserInfo = document.createElement("userInfo");
        document.appendChild(eUserInfo);
        Element eUsername = document.createElement("username");
        eUsername.setTextContent(username);
        eUserInfo.appendChild(eUsername);
        Element eUserId = document.createElement("userId");
        eUserId.setTextContent(userId);
        eUserInfo.appendChild(eUserId);
        /*
        Element eIsletme = document.createElement("isletme");
        eIsletme.setTextContent(isletme);
        eUserInfo.appendChild(eIsletme);
        */
        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer aTransformer = tranFactory.newTransformer();
        Source src = new DOMSource(document);
        Result dest = new StreamResult(file);
        aTransformer.transform(src, dest);

    }
}
