package wbriese.loco2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;


//Helper class to provide interpolate functions for Hydrostatic class
public class HydrostaticTables {
	
	private TreeSet<Map<String,Object>> hydrostatics;
	
		
	//Comparator is used so that the TreeSet functions floor and ceiling will return the tables above and below the requested trim
	private  Comparator<Map<String,Object>> trimComparator = new Comparator<>() {
		public int compare(Map<String,Object> trimList1,Map<String,Object> trimList2) {
			return ((BigDecimal) trimList1.get("trim")).compareTo((BigDecimal) trimList2.get("trim"));
		}
	};
	
	//Comparator is used to find floor and ceiling of Displacement Value
	private  Comparator<Map<String,BigDecimal>> dispComparator = new Comparator<>() {
				public int compare(Map<String,BigDecimal> hydrostaticRow1,Map<String,BigDecimal> hydrostaticRow2) {
					return ((BigDecimal) hydrostaticRow1.get("disp")).compareTo((BigDecimal) hydrostaticRow2.get("disp"));
				}
			};
			
			public HydrostaticTables (Collection<Map<String,Object>> hydroTables) {
				hydrostatics=new TreeSet<>(trimComparator);
				hydrostatics.addAll(hydroTables);
				
			}
	
public  Map<String,BigDecimal> interpolate( double trim, double disp) {
		
		// Find the hydrostatic tables for the trim values above (TrimCeiling) and below (trimFloor) requested trim
		Map<String,Object> floorTrimTable= (Map<String, Object>) hydrostatics.floor(Map.of("trim", new BigDecimal(trim)));
		Map<String,Object> ceilingTrimTable= (Map<String, Object>) hydrostatics.ceiling(Map.of("trim", new BigDecimal(trim)));
		
		// calculate the linear factor p= (trim-TRIMfloor)/(TRIMceiling-TRIMfloor) between the 2 tabular trim values
		BigDecimal trimFrac;
		if (floorTrimTable.equals(ceilingTrimTable)) trimFrac=new BigDecimal(0);
		else trimFrac= new BigDecimal((trim-((BigDecimal)floorTrimTable.get("trim")).doubleValue())/(((BigDecimal)ceilingTrimTable.get("trim")).doubleValue()-((BigDecimal)floorTrimTable.get("trim")).doubleValue()));
	
		
		//calculate hydrostatic and GZ values for the highest trim below the requested trim
		Map<String,BigDecimal> floorTrimInterpolated;
		floorTrimInterpolated=interpolateDisplacement ((ArrayList<Map<String, BigDecimal>>) floorTrimTable.get("hydrovalues"),disp);
		floorTrimInterpolated.putAll(interpolateDisplacement ((ArrayList<Map<String, BigDecimal>>) floorTrimTable.get("crosscurves"),disp));
		
		//calculate hydrostatic and GZ values for the lowest trim above the requested trim
		 Map<String,BigDecimal> ceilingTrimInterpolated;
		ceilingTrimInterpolated=interpolateDisplacement ((ArrayList<Map<String, BigDecimal>>) ceilingTrimTable.get("hydrovalues"),disp);
		ceilingTrimInterpolated.putAll(interpolateDisplacement ((ArrayList<Map<String, BigDecimal>>) ceilingTrimTable.get("crosscurves"),disp));
				
		//interpolate the interpolated values a second time given the correct trim Factor
		return floorTrimInterpolated.entrySet().stream().collect(Collectors.toMap(el->el.getKey(),el->el.getValue().add(trimFrac.multiply(ceilingTrimInterpolated.get(el.getKey()).subtract(el.getValue())))));
	}

	
	private  Map<String,BigDecimal> interpolateDisplacement (ArrayList<Map<String, BigDecimal>> table,double disp) {
		
		 NavigableSet<Map<String,BigDecimal>> sortedTable= new TreeSet<Map<String,BigDecimal>>(dispComparator);
		sortedTable.addAll(table);
		
		//Get the  hydrostatic values closest to requested Displacement
		Map<String,BigDecimal> FloorDisp = sortedTable.floor(Map.of("disp",new BigDecimal(disp)));	
		Map<String,BigDecimal> CeilingDisp = sortedTable.ceiling(Map.of("disp",new BigDecimal(disp)));	

		System.out.println("disp"+disp);
		System.out.println("FloorDisp"+FloorDisp);
		
		BigDecimal DispFrac;
		if (FloorDisp.equals(CeilingDisp)) DispFrac=new BigDecimal(0);
		else DispFrac=new BigDecimal((disp-((BigDecimal)FloorDisp.get("disp")).doubleValue())/(((BigDecimal)CeilingDisp.get("disp")).doubleValue()-((BigDecimal)FloorDisp.get("disp")).doubleValue()));
										
		//calculate the Displacement-interpolated hydrostatic values 
		Map<String,BigDecimal> interpolatedValues=FloorDisp.entrySet().stream().collect(Collectors.toMap(el->el.getKey(),el->el.getValue().add(DispFrac.multiply(CeilingDisp.get(el.getKey()).subtract(el.getValue())))));
		
		return interpolatedValues;
	}
}
