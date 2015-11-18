package com.generador.capitalcycles.sofactory.archivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GeneradorPropiedades {

	private Map<String,Boolean> properties;
	private List<String> configuracion;

	public GeneradorPropiedades(File xmlFeatures, File config) 
			throws SAXException, IOException, ParserConfigurationException{
		properties = new HashMap<String,Boolean>();
		configuracion = new ArrayList<String>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFeatures);
		obtenerConfigs(config);
		procesar(doc);
	}

	private void procesar(Document xmlFeatures){
		xmlFeatures.getDocumentElement().normalize();
		NodeList nList = xmlFeatures.getElementsByTagName("struct");

		comparar(nList, "");
		
		System.out.println(properties.toString());
	}

	private void comparar(NodeList nList, String prefijo){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String featureName = eElement.getAttribute("name");
				String property = prefijo.concat("."+featureName.toLowerCase());
				
				if (!".".equals(property)){
					properties.put(property.substring(2), configuracion.contains(featureName));
				}
				
				comparar(eElement.getChildNodes(), property);
			}
		}
	}

	private void obtenerConfigs(File config) throws IOException{
		FileReader fr = new FileReader(config);
		BufferedReader br = new BufferedReader(fr);
		String linea = br.readLine();
		while(null!=linea){
			configuracion.add(linea);
			linea = br.readLine();
		}

		br.close();
	}
}
