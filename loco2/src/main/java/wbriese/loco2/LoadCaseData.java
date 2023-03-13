package wbriese.loco2;

import java.io.FileNotFoundException;
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

public class LoadCaseData {

	private JsonObject loadCaseJsonObject;
	private static LoadCaseData loadCaseDataInstance = null;
	
	private LoadCaseData () {
		loadCaseJsonObject = loadJsonObjectFromFile();
	}
	
	private JsonObject loadJsonObjectFromFile() {
		
		JsonObject loadedJsonObject=null;
		
		try {
			  // the stream holding the file content
			  InputStream is = getClass().getClassLoader().getResourceAsStream("loadcase2.json");

		
			loadedJsonObject=(JsonObject) Jsoner.deserialize(new InputStreamReader(is));
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loadedJsonObject;
	}

	public static LoadCaseData getInstance() {
		if (loadCaseDataInstance==null) loadCaseDataInstance = new LoadCaseData();
		return loadCaseDataInstance;
	}
	
	public String getShipName () {
		return (String) loadCaseJsonObject.get("shipname");
	}
	
	public int getImo () {
		return ((BigDecimal) loadCaseJsonObject.get("imo")).intValue();
	}
	
	public Collection<Map<String,Object>> getWeightList () {
		return (Collection<Map<String,Object>>) loadCaseJsonObject.get("weightlist");
	}
	
	public Collection<Map<String,Object>> getTankList () {
		return (Collection<Map<String,Object>>) loadCaseJsonObject.get("tanklist");
	}
	
}
	
	
