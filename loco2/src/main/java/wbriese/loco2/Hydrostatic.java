package wbriese.loco2;


import java.math.BigDecimal;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;


// This class is a Singleton. Any class can get the Hydrostatic object via the static method Hydrostatic.getInstance().
// This class is  a listener to any change in the list of Masses. 
// It will trigger recalculation of all hydrostatic properties and has access to the hydrostatic tables.
// It offers all User Interfaces access to hydrostatic properties

public class Hydrostatic implements ListChangeListener<Mass>  {
	
	private Map<String,BigDecimal> hydrostaticValues;
	private ShipData shipData;
	private HydrostaticTables hydrostaticTables;
	private double lcg,lcgMoment,vcgMoment,tcg,tcgMoment,bg,deltaTrimA,deltaTrimF;
	
	
	public SimpleDoubleProperty displacement=new SimpleDoubleProperty(this, "displacement",0.0); 
	public SimpleDoubleProperty trim=new SimpleDoubleProperty(this, "trim",0.0);
	public SimpleDoubleProperty gm=new SimpleDoubleProperty(this,"gm",0.0);
	public SimpleDoubleProperty gmDelta=new SimpleDoubleProperty(this,"gmDelta",0.0);
	public SimpleDoubleProperty km=new SimpleDoubleProperty(this,"km",0.0);
	public SimpleDoubleProperty kg=new SimpleDoubleProperty(this,"kg",0.0);
	public SimpleDoubleProperty sumFSM=new SimpleDoubleProperty(this,"sumFSM",0.0);
	public SimpleDoubleProperty lcf=new SimpleDoubleProperty(this,"lcf",0.0);
	
	public SimpleDoubleProperty list=new SimpleDoubleProperty(this,"list",0.0);
	
	public SimpleDoubleProperty draftAftM=new SimpleDoubleProperty(this,"draftAftM",0.0);
	public SimpleDoubleProperty draftForwardM=new SimpleDoubleProperty(this,"draftForwardM",0.0);
	public SimpleDoubleProperty draftMidM=new SimpleDoubleProperty(this,"draftMidM",0.0);
		
	public SimpleDoubleProperty gz0=	new SimpleDoubleProperty(this,"gz0",0.0);
	public SimpleDoubleProperty gz10=	new SimpleDoubleProperty(this,"gz10",0.0);
	public SimpleDoubleProperty gz20=	new SimpleDoubleProperty(this,"gz20",0.0);
	public SimpleDoubleProperty gz30=	new SimpleDoubleProperty(this,"gz30",0.0);
	public SimpleDoubleProperty gz40=	new SimpleDoubleProperty(this,"gz40",0.0);
	public SimpleDoubleProperty gz50=	new SimpleDoubleProperty(this,"gz50",0.0);
	public SimpleDoubleProperty gz60=	new SimpleDoubleProperty(this,"gz60",0.0);
		
		
	//Singleton
	private static Hydrostatic hydrostatic;
	
//	public double getShipValue(String key) {
//		return shipData.getShipValues(key).doubleValue();
//	}
	

	
	
	private Hydrostatic () {
		
		//get access to ShipData
		shipData=ShipData.getInstance();
		hydrostaticTables=new HydrostaticTables(shipData.getHydrostatics());
				
		//get access to initial list of masses
		ObservableList<Mass> massList= MassList.getInstance();	
		
		//Bind this class in case of any change of masses in the ship
		MassList.bindHydrostatic(this);
		
		//calculate initial values for initial list of masses
		calculateHydrostaticProperties(massList);
	}
	
	public static Hydrostatic getInstance () {
		if (hydrostatic==null) 	Hydrostatic.hydrostatic=new Hydrostatic();
		return hydrostatic;		
	}
	
	private void calculateHydrostaticProperties (ObservableList<? extends Mass> massList) {
		
			
		displacement.set(massList.stream().map(mass->mass.getMass()).reduce( 0.0, (r,e)->r+e));
		
		lcgMoment= massList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e);
		lcg=lcgMoment/displacement.get();
		
		vcgMoment= massList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e);
		kg.set(vcgMoment/displacement.get());
		
		tcgMoment= massList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e);
		tcg=tcgMoment/displacement.get();
		
		sumFSM.set(massList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e));
				
		hydrostaticValues=hydrostaticTables.interpolate(0, displacement.get()); // to do

		
		bg=lcg-hydrostaticValues.get("LCB").doubleValue();
		
		trim.set(bg*displacement.get()/hydrostaticValues.get("MCT").doubleValue()/100.0);

		
		hydrostaticValues=hydrostaticTables.interpolate(trim.get(), displacement.get()); // to do

		
		
		km.set(hydrostaticValues.get("KMT").doubleValue());
		lcf.set(hydrostaticValues.get("LCF").doubleValue());
		
		gmDelta.set(sumFSM.get()/displacement.get());
		gm.set(this.km.get()-this.kg.get()-gmDelta.get());
		
		list.set(Math.atan(tcg/gm.get())*Math.PI/180);
		
		
		deltaTrimA=-trim.get()*lcf.get()/shipData.getLpp();
		deltaTrimF=trim.get()+deltaTrimA;
		
		draftAftM.set(hydrostaticValues.get("T").doubleValue()+deltaTrimA);
		draftForwardM.set(hydrostaticValues.get("T").doubleValue()+deltaTrimF);
		draftMidM.set(hydrostaticValues.get("T").doubleValue());
		
		gz10.set(hydrostaticValues.get("10").doubleValue());

		gz20.set(hydrostaticValues.get("20").doubleValue());
		gz30.set(hydrostaticValues.get("30").doubleValue());
		gz40.set(hydrostaticValues.get("40").doubleValue());
		gz50.set(hydrostaticValues.get("50").doubleValue());
		gz60.set(hydrostaticValues.get("60").doubleValue());
		
			
	}
	


	@Override
	public void onChanged(Change<? extends Mass> c) {
		System.out.println("Hydrostatic onChanged method called");
		calculateHydrostaticProperties(c.getList());
		
		
	}

	public String getSideView() {
		// TODO Auto-generated method stub
		return "M 0 0 L 129938 -0 L 133206 -117 L 136729 -512 L 139047 -1025 L 141015 -1722 L 142212 -2346 L 143352 -3216 L 143927 -3934 L 144264 -4689 L 144348 -5231 L 144344 -5523 L 144180 -6178 L 143932 -6478 L 143438 -6713 L 142766 -6843 L 142110 -6878 L 141234 -6872 L 140778 -6872 L 140309 -6905 L 140000 -6950 L 140000 -8800 L 140158 -9236 L 140504 -9770 L 142553 -12858 L 144500 -15792 L 144021 -16750 L 130300 -17936 L 129898 -18930 L 128700 -18930 L 127800 -17350 L 123003 -14700 L 95983 -14700 L 95392 -18827 L 95295 -22690 L 100998 -22169 L 108205 -22171 L 128172 -22695 L 130651 -21419 L 130817 -21633 L 130817 -22320 L 128234 -23738 L 95295 -23703 L 95305 -26143 L 95847 -26216 L 96047 -28134 L 95491 -28333 L 94990 -28373 L 94505 -31075 L 94681 -32491 L 92579 -34170 L 91943 -34170 L 91146 -31153 L 91150 -28838 L 91317 -28808 L 91208 -18827 L 90617 -14700 L 54733 -14700 L 54169 -18767 L 54080 -20335 L 54045 -28808 L 54205 -28808 L 54205 -31235 L 54086 -31224 L 52927 -34209 L 51974 -34180 L 50747 -30861 L 50384 -28545 L 49295 -28156 L 49504 -26180 L 50097 -26201 L 50066 -23877 L 17248 -27457 L 14641 -25729 L 14641 -25312 L 17294 -26339 L 37125 -23731 L 44283 -22979 L 50053 -22900 L 50020 -20395 L 49928 -18827 L 49367 -14700 L 24420 -14700 L 24420 -11650 L 19400 -11650 L 18147 -12091 L 14550 -14700 L 14550 -29120 L 15080 -30900 L 14500 -31900 L 12707 -31900 L 7800 -30900 L 7800 -34150 L 2000 -33650 L 2000 -33550 L 2972 -33300 L 2400 -28200 L 2400 -15700 L -2500 -15700 L -2500 -5330 L -2050 -5330 L -2050 -5480 L -2000 -5480 L -2000 -250 L 1100 -250 L 1236 -2532 L 1841 -2516 L 1841 -3384 L 1287 -3371 L 1400 -5480 L 1478 -5510 L 2092 -7266 L 2351 -7415 L 3760 -7187 L 4841 -6857 L 5552 -6331 L 5862 -5615 L 5862 -4999 L 5584 -4361 L 5117 -3854 L 4300 -3450 L 3841 -3442 L 2792 -5792 L 2908 -3393 L 1941 -3384 L 1941 -2516 L 3048 -2499 L 2804 -106 L 3849 -2457 L 4300 -2450 L 6243 -1568 L 8130 -861 L 10730 -193 L 12741 -0 L 0 -0";
	}

	
		
	}






	

