package com.generador.capitalcycles.sofactory.generador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.generador.capitalcycles.sofactory.archivo.ManejadorArchivos;

public class GeneradorPropiedades {

	private Map<String,Boolean> features;
	private Map<String,Boolean> fearuteExcludes;
	private List<String> configuracion;

	public GeneradorPropiedades(File xmlFeatures, File config) 
			throws SAXException, IOException, ParserConfigurationException{
		features = new HashMap<String,Boolean>();
		fearuteExcludes = new HashMap<String,Boolean>();
		configuracion = new ArrayList<String>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFeatures);
		configuracion = ManejadorArchivos.obtenerConfigs(config);
		procesar(doc);
	}

	private void procesar(Document xmlFeatures) throws IOException{
		xmlFeatures.getDocumentElement().normalize();
		NodeList nList = xmlFeatures.getElementsByTagName("struct");

		comparar(nList, "");
		
		ManejadorArchivos.generarArchivoPropieddades(features, ManejadorArchivos.TipoPropiedades.FEATURES);
		
		obtenerFeaturesExcludes();
		
		ManejadorArchivos.generarArchivoPropieddades(fearuteExcludes, ManejadorArchivos.TipoPropiedades.FEATURES_EXCLUDES);
	}

	private void comparar(NodeList nList, String prefijo){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String featureName = eElement.getAttribute("name");
				String property = prefijo.concat("."+featureName.toLowerCase());
				
				if (!".".equals(property)){
					features.put(property.substring(2), configuracion.contains(featureName));
				}
				
				comparar(eElement.getChildNodes(), property);
			}
		}
	}

	private void obtenerFeaturesExcludes(){
		for (Entry<String, Boolean> entry : features.entrySet()) {
			if (!entry.getKey().equals("proyectobicicletas")){
				fearuteExcludes.put(entry.getKey().replace("proyectobicicletas.", "").concat("excludes")
						, !entry.getValue());
			}
		}
	}
	
}
