package wbriese.loco2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tank extends Mass {
	
	 //Comparator is used to find floor and ceiling rows to corresponding Weight Value
	private static Comparator<Map<String,BigDecimal>> weightComparator = new Comparator<>() {
		public int compare(Map<String,BigDecimal> tankRow1,Map<String,BigDecimal> tankRow2) {
			return ((BigDecimal) tankRow1.get("vol")).compareTo((BigDecimal) tankRow2.get("vol"));
		}
	};
	
	private final SimpleStringProperty svg = 
			new SimpleStringProperty (this,"tankSVG","");
	
	//fill of tanks in %
	private final SimpleDoubleProperty fill =
			 new SimpleDoubleProperty(this, "fill",0);
	
	private final SimpleDoubleProperty level =
			 new SimpleDoubleProperty(this, "level",0);
	private final SimpleDoubleProperty levelMax =
			 new SimpleDoubleProperty(this, "levelMax",0);
	private final SimpleDoubleProperty volume =
			 new SimpleDoubleProperty(this, "volume",0);
	private final SimpleDoubleProperty volumeMax =
			 new SimpleDoubleProperty(this, "volumeMax",0);
	private final SimpleDoubleProperty density =
			 new SimpleDoubleProperty(this, "density",1.025);
	private final SimpleBooleanProperty full =
			 new SimpleBooleanProperty(this, "full",false);
	
	private NavigableSet<Map<String,BigDecimal>> tankTable;

public Tank (String identifier,String svg,MassCategory category, BigDecimal mass, BigDecimal density, Collection<Map<String,BigDecimal>> tankTable, BigDecimal xa, BigDecimal xf) {
	super(identifier,category,mass.doubleValue(),0.0,0.0,0.0,0.0,xa.doubleValue(),xf.doubleValue());//(identifier,category,mass,x,y,z,fsm);
	this.svg.set(svg);
	this.density.set(density.doubleValue());
	this.tankTable= new TreeSet<Map<String,BigDecimal>>(weightComparator);
	this.tankTable.addAll(tankTable);
	
	this.volume.set(mass.doubleValue()/density.doubleValue());
	Map<String,BigDecimal> tankValues=interpolateTankPropertiesViaWeight(mass);
	this.level.set(tankValues.get("h").doubleValue());
	this.fill.set(tankValues.get("fill").doubleValue());
	super.setX(tankValues.get("lcg").doubleValue());
	super.setY(tankValues.get("tcg").doubleValue());
	super.setZ(tankValues.get("vcg").doubleValue());
	super.setFsm(tankValues.get("fsm").doubleValue());
	
	
	
	this.volumeMax.set(this.tankTable.pollLast().get("vol").doubleValue());
	
}
	
public Map<String,BigDecimal> interpolateTankPropertiesViaWeight(BigDecimal mass) {
	return interpolateTankPropertiesViaVolume(new BigDecimal(mass.doubleValue()/density.get()));
}

private Map<String,BigDecimal> interpolateTankPropertiesViaVolume (BigDecimal volume) {
	
	//Get the  hydrostatic values closest to requested Displacement
	Map<String,BigDecimal> floorVolume = (Map<String,BigDecimal>) tankTable.floor(Map.of("vol",volume));	
	Map<String,BigDecimal> ceilingVolume =(Map<String,BigDecimal>) tankTable.ceiling(Map.of("vol",volume));	

	System.out.println("Volume to be interpolated:"+volume);
	System.out.println(floorVolume);
	System.out.println(ceilingVolume);
	
	double volumeFrac;
	if (floorVolume.equals(ceilingVolume)) volumeFrac=0.0;
	else volumeFrac=volume.doubleValue()-(floorVolume.get("vol")).doubleValue()/((ceilingVolume.get("vol")).doubleValue()-floorVolume.get("vol").doubleValue());
									
	//calculate the Displacement-interpolated hydrostatic values 
	Map<String,BigDecimal> interpolatedValues=floorVolume.entrySet().stream().collect(Collectors.toMap(el->el.getKey(),el->el.getValue().add(new BigDecimal(volumeFrac).multiply(ceilingVolume.get(el.getKey()).subtract(el.getValue())))));
	return interpolatedValues ;
	
}

public final SimpleStringProperty svgProperty() {
	return this.svg;
}


public final String getSvg() {
	return this.svgProperty().get();
}


public final void setSvg(final String svg) {
	this.svgProperty().set(svg);
}


public final SimpleDoubleProperty fillProperty() {
	return this.fill;
}


public final double getFill() {
	return this.fillProperty().get();
}


public final void setFill(final double fill) {
	this.fillProperty().set(fill);
}


public final SimpleDoubleProperty levelProperty() {
	return this.level;
}


public final double getLevel() {
	return this.levelProperty().get();
}


public final void setLevel(final double level) {
	this.levelProperty().set(level);
}


public final SimpleDoubleProperty levelMaxProperty() {
	return this.levelMax;
}


public final double getLevelMax() {
	return this.levelMaxProperty().get();
}


public final void setLevelMax(final double levelMax) {
	this.levelMaxProperty().set(levelMax);
}


public final SimpleDoubleProperty volumeProperty() {
	return this.volume;
}


public final double getVolume() {
	return this.volumeProperty().get();
}


public final void setVolume(final double volume) {
	
	Map<String,BigDecimal> tankValues=this.interpolateTankPropertiesViaVolume(new BigDecimal(volume));
	updateTankValues(tankValues);
	
}


private void updateTankValues(Map<String, BigDecimal> tankValues) {
	
	super.setFsm(tankValues.get("fsm").doubleValue());
	super.setMass(tankValues.get("vol").doubleValue()*this.getDensity());
	super.setX(tankValues.get("lcg").doubleValue());
	super.setY(tankValues.get("tcg").doubleValue());
	super.setZ(tankValues.get("vcg").doubleValue());
	this.volume.set(tankValues.get("vol").doubleValue());
	this.level.set(tankValues.get("h").doubleValue());
	this.fill.set(tankValues.get("fill").doubleValue());
}

public final SimpleDoubleProperty volumeMaxProperty() {
	return this.volumeMax;
}


public final double getVolumeMax() {
	return this.volumeMaxProperty().get();
}


public final void setVolumeMax(final double volumeMax) {
	this.volumeMaxProperty().set(volumeMax);
}


public final SimpleDoubleProperty densityProperty() {
	return this.density;
}


public final double getDensity() {
	return this.densityProperty().get();
}


public final void setDensity(final double density) {
	this.densityProperty().set(density);
}


public final SimpleBooleanProperty fullProperty() {
	return this.full;
}


public final boolean isFull() {
	return this.fullProperty().get();
}


public final void setFull(final boolean full) {
	this.fullProperty().set(full);
}





}