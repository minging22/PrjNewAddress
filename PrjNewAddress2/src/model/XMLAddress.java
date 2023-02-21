package model;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;

public class XMLAddress {
   private static String serviceKey = "LQ9IIe7cd36qJ9zbdb%2BHrTc0mwC%2FL7A5J%2BhFEZEgBxfUHpg3eU6gM%2FVu57Vl45%2Fzz89pCZ0fwycKERerftBSTg%3D%3D";
   
   public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
      
      String searchText = "롯데백화점";
      
      StringBuilder urlBuilder = new StringBuilder("http://openapi.epost.go.kr/postal/retrieveNewZipCdService/retrieveNewZipCdService/getNewZipCdList"); /*URL*/
      urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
      urlBuilder.append("&" + URLEncoder.encode("srchwrd","UTF-8") + "=" + URLEncoder.encode(searchText, "UTF-8")); /*검색어*/
      urlBuilder.append("&" + URLEncoder.encode("countPerPage","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*페이지당 출력될 개수를 지정(최대50)*/
      urlBuilder.append("&" + URLEncoder.encode("currentPage","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*출력될 페이지 번호*/
      
      URL url = new URL(urlBuilder.toString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");
      System.out.println("Response code: " + conn.getResponseCode());
      BufferedReader rd;
      if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {
         rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
         sb.append(line);
      }
      rd.close();
      conn.disconnect();
      System.out.println(sb.toString());
      
      
      //-----------------------------------------------------------------
      // xml parsing xml 자르기
      String xml = sb.toString();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder         builder = factory.newDocumentBuilder(); // import 후 add throw
      
      // Document : xml 문서 자체
      // Element  : <시작tag>텍스트</끝tag>
      // Node     : xml을 구성하는 항목
      // 종류     : Element   : <zipNo>16285</zipNo>
      //            Attribute : version="1=0"
      //            Text      : 경기도 수원시 장안구 수성로262번길 65 (영화동, 부전빌딩)           
      // NodeList : node 배열
      // Document               document = builder.parse("src/data/addr.xml"); 
      Document               document = builder.parse(new InputSource(new StringReader(xml))); 
      
      // 필요한 Element를 찾는다
      NodeList               childList = document.getElementsByTagName("newZipCdList");
      
      for (int i = 0; i < childList.getLength(); i++) {
    	  Node    node = childList.item(i);
    	  if(node.getNodeType() == Node.ELEMENT_NODE) {
    		  System.out.println(node.getNodeName());
    		  System.out.println(node.getTextContent());
    	  } else {
    		  System.out.println("공백입니다");
    	  }
	}
      
      
   }


}