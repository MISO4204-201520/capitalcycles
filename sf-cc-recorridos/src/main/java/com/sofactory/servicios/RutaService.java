package com.sofactory.servicios;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import com.sofactory.entidades.Posicion;
import com.sofactory.negocio.interfaces.RutaBeanLocal;

@Path("rutas")
public class RutaService {

	@EJB
	private RutaBeanLocal rutaBeanLocal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("status")
	public Response getStatus() {
		return Response.ok(
				"{\"status\":\"Service RUTA Service is running...\"}").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("puntos")
	public Response getPuntos() {
		String response = null;
		try {
			List<Posicion> list = rutaBeanLocal.encontrarTodos(Posicion.class);
			response = toJSONString(list);
		} catch (Exception err) {
			response = "{\"status\":\"401\","
					+ "\"message\":\"No content found \""
					+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
			return Response.status(401).entity(response).build();
		}
		return Response.ok(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("ruta/{dirInicial}/{dirFinal}")
	public Response getRoute(@PathParam("dirInicial") String dirInicial, 
							 @PathParam("dirFinal") String dirFinal ) {
	
        double lat1, lat2, lng1, lng2;
        
        System.out.println("Impresion de Puntos de una Ruta");
        System.out.println("Dirección Inicial " + dirInicial);
        System.out.println("Dirección Final " + dirFinal);
        
        try{
            GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBEelLTtoKYBwxxjM9nlkipXI4bO4BOK3g");
            GeocodingResult[] results =  GeocodingApi.geocode(context,
                dirInicial).await();
            
            System.out.println("Dirección 1:\t" + results[0].formattedAddress);               
            System.out.println("Latitud 1:\t" + results[0].geometry.location.lat);               
            System.out.println("Longitud 1:\t" + results[0].geometry.location.lng); 

            lat1 = results[0].geometry.location.lat;
            lng1 = results[0].geometry.location.lng;
            
            results =  GeocodingApi.geocode(context,
                dirFinal).await();
            
            System.out.println("Dirección 2:\t" + results[0].formattedAddress);               
            System.out.println("Latitud 2:\t" + results[0].geometry.location.lat);               
            System.out.println("Longitud 2:\t" + results[0].geometry.location.lng); 

            lat2 = results[0].geometry.location.lat;
            lng2 = results[0].geometry.location.lng;    
            
            String origen = lat1 + "," + lng1;
            String destino = lat2 + "," + lng2;
            
            DirectionsRoute[] ruta = DirectionsApi.getDirections(context, origen, destino).await();
           
            for(int i=0; i < ruta[0].legs[0].steps.length; i++ )
            {
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].htmlInstructions.toString());  
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].startLocation.toString());               
                System.out.println("Punto Final P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].endLocation.toString()); 
                System.out.println("");
            }
            
            //DirectionsRoute[] ruta = DirectionsApi.getDirections(context, "Calle 50 16 26, Bogota", "Calle 93 15 27, Bogota").await();
            
            //Bicicleta
            ruta = DirectionsApi.newRequest(context)
                .mode(TravelMode.TRANSIT)
                //.avoid(RouteRestriction.HIGHWAYS, RouteRestriction.TOLLS, RouteRestriction.FERRIES)
                //.units(Unit.METRIC)
                //.region("us")
                .origin(origen)
                .destination(destino).await();            
            System.out.println("EN BICICLETA");
             for(int i=0; i < ruta[0].legs[0].steps.length; i++ )
            {
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].htmlInstructions.toString());  
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].startLocation.toString());               
                System.out.println("Punto Final P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].endLocation.toString()); 
                System.out.println("");
            }           
            
            System.out.println("FIN"); 
        }catch(Exception e )
        {
            System.out.println(e.toString());  
        }

		return Response.ok(
							"{\"status\":\"Puntos que conforman una Ruta desde: " + dirInicial +
							" hasta " + dirFinal + "...\"}").build();
    }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("writeruta/{dirInicial}/{dirFinal}")
	public Response setRoute(@PathParam("dirInicial") String dirInicial, 
							 @PathParam("dirFinal") String dirFinal ) {
	
        double lat1, lat2, lng1, lng2;
        
        System.out.println("Insert de Puntos de una Ruta");
        System.out.println("Dirección Inicial " + dirInicial);
        System.out.println("Dirección Final " + dirFinal);
        
        try{
            GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBEelLTtoKYBwxxjM9nlkipXI4bO4BOK3g");
            GeocodingResult[] results =  GeocodingApi.geocode(context,
                dirInicial).await();
            
            System.out.println("Dirección 1:\t" + results[0].formattedAddress);               
            System.out.println("Latitud 1:\t" + results[0].geometry.location.lat);               
            System.out.println("Longitud 1:\t" + results[0].geometry.location.lng); 

            lat1 = results[0].geometry.location.lat;
            lng1 = results[0].geometry.location.lng;
            
            results =  GeocodingApi.geocode(context,
                dirFinal).await();
            
            System.out.println("Dirección 2:\t" + results[0].formattedAddress);               
            System.out.println("Latitud 2:\t" + results[0].geometry.location.lat);               
            System.out.println("Longitud 2:\t" + results[0].geometry.location.lng); 

            lat2 = results[0].geometry.location.lat;
            lng2 = results[0].geometry.location.lng;    
            
            String origen = lat1 + "," + lng1;
            String destino = lat2 + "," + lng2;
            
            DirectionsRoute[] ruta = DirectionsApi.getDirections(context, origen, destino).await();
           
            for(int i=0; i < ruta[0].legs[0].steps.length; i++ )
            {
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].htmlInstructions.toString());  
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].startLocation.toString());               
                System.out.println("Punto Final P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].endLocation.toString());
                System.out.println("");  

                Posicion posicion = new Posicion();
                //posicion.setId((long) 101+i);
                String[] splitPosicion = ruta[0].legs[0].steps[i].startLocation.toString().split(",");
                posicion.setLatitud(new BigDecimal(splitPosicion[0]));
                posicion.setLongitud(new BigDecimal(splitPosicion[1]));
                
            	// Create a new Gson object that could parse all passed in elements
        		GsonBuilder gsonBuilder = new GsonBuilder();
        		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        		Gson gson = gsonBuilder.create();
        		String returnCode = "200";
        		
        		try {
        			rutaBeanLocal.insertar(posicion);
        			returnCode = "{"
        					+ "\"href\":\"http://localhost:8080/BookWebService/rest/bookservice/book/"+posicion.getLatitud().toString()+","+posicion.getLongitud().toString()+"\","
        					+ "\"message\":\"New posicion successfully created.\""
        					+ "}";
        		} catch (Exception err) {
        			err.printStackTrace();
        			returnCode = "{\"status\":\"500\","+
        					"\"message\":\"Resource not created.\""+
        					"\"developerMessage\":\""+err.getMessage()+"\""+
        					"}";
        			return  Response.status(500).entity(returnCode).build(); 

        		}
        		
            }
            
            //DirectionsRoute[] ruta = DirectionsApi.getDirections(context, "Calle 50 16 26, Bogota", "Calle 93 15 27, Bogota").await();
            
            //Bicicleta
            ruta = DirectionsApi.newRequest(context)
                .mode(TravelMode.TRANSIT)
                //.avoid(RouteRestriction.HIGHWAYS, RouteRestriction.TOLLS, RouteRestriction.FERRIES)
                //.units(Unit.METRIC)
                //.region("us")
                .origin(origen)
                .destination(destino).await();            
            System.out.println("EN BICICLETA");
             for(int i=0; i < ruta[0].legs[0].steps.length; i++ )
             {
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].htmlInstructions.toString());  
                System.out.println("Punto Inicial P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].startLocation.toString());               
                System.out.println("Punto Final P " + (i+1) + ":\t" + ruta[0].legs[0].steps[i].endLocation.toString()); 
                System.out.println("");
             }           
            
            System.out.println("FIN"); 
        }catch(Exception e )
        {
            System.out.println(e.toString());  
        }
        
		return Response.ok(
							"{\"status\":\"Puntos que conforman una Ruta desde: " + dirInicial +
							" hasta " + dirFinal + "...\"}").build();
    }
	
	public String toJSONString(Object object) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // ISO8601 /
																	// UTC
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
	}	
}