package wbriese.loco2;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;


public class ShipData {
	
	private JsonObject jsonObject=null;
	private static ShipData shipDataInstance=null;

	private ShipData() {
		jsonObject=loadShipDatafromFile();
	}
	
	public static ShipData getInstance() {
		if (shipDataInstance==null) ShipData.shipDataInstance=new ShipData();
		return ShipData.shipDataInstance;
	}
	
	public double getHeight() {
		return ((BigDecimal)jsonObject.get("height")).doubleValue();
	}
	
	public double getBeam() {
		return ((BigDecimal)jsonObject.get("beam")).doubleValue();
	}

	public double getLoa() {
		return ((BigDecimal)jsonObject.get("loa")).doubleValue();
	}
	
	public int getImo () {
		return ((BigDecimal)jsonObject.get("imo")).intValue();
	}
	
	
	public double getLpp () {
		return ((BigDecimal) jsonObject.get("lpp")).doubleValue();
	}
	
	public String getVersion() {
		return (String) jsonObject.get("version");
	}


	public String getSvgtop() {
		return (String) jsonObject.get("svgtop");
	}


	public String getSvgside() {
		return (String) jsonObject.get("svgSide");
	}


	public String getSvgmidship() {
		return (String) jsonObject.get("svgmidship");
	}


	public int getShipID() {
		return ((BigDecimal) jsonObject.get("shipID")).intValue();
	}


	public Collection<Map<String, Object>> getHydrostatics() {
		return ((Collection<Map<String,Object>>) jsonObject.get("hydrostatics"));
	}


	public Collection<Map<String, Object>> getLightshipList() {
		return (Collection<Map<String,Object>>)jsonObject.get("lightshiplist");
	}


	public Collection<Map<String, Object>> getTankList() {
		return (Collection<Map<String,Object>>)jsonObject.get("tanks");
	}

	
	
	private JsonObject loadShipDatafromFile() {
		
		JsonObject loadedData=null;
		
		try 
		{
			  InputStream is = getClass().getClassLoader().getResourceAsStream("data2.json");
			  System.out.println(is);
			  loadedData=(JsonObject) Jsoner.deserialize(new InputStreamReader(is));
			  is.close();
			
		}
		catch (IOException | JsonException e) 
		{
			e.printStackTrace();
		}
		
		return loadedData;
	}
	
	
	
}
