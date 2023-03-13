package wbriese.loco2;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;


public class ShipTotalController {

	@FXML
	private SVGPath sideViewSVG;
	
	@FXML
	private Line line;
	
	@FXML
	private Label lightShipMassLabel, lightShipLCG, lightShipVCG, lightShipTCG, lightShipFROB;
	
	@FXML
	private Label coverMass, coverLCG, coverVCG, coverTCG, coverFROB;
	
	@FXML
	private Label storesMassLabel, storesLCG, storesVCG, storesTCG, storesFROB;
	
	@FXML 
	private Label tanksMass, tanksLCG, tanksVCG, tanksTCG, tanksFROB;
	
	@FXML
	private Label holdWaterMass, holdWaterLCG, holdWaterVCG, holdWaterTCG, holdWaterFROB;
	
	@FXML
	private Label containerMass, containerLCG, containerVCG, containerTCG, containerFROB;
	
	@FXML
	private Label specialCargoMass, specialCargoLCG, specialCargoVCG, specialCargoTCG, specialCargoFROB;
	
	@FXML
	private Label freeInputMass, freeInputLCG, freeInputVCG, freeInputTCG, freeInputFROB;
	
	@FXML
	private Label bulkMass, bulkLCG, bulkVCG, bulkTCG, bulkFROB;
	
	@FXML
	private Label totalMass, totalLCG, totalVCG, totalTCG, totalFROB;
	
	@FXML
	private Label kmLabel,kgoLabel,gmoLabel,dgmLabel,gmLabel,tLabel;
	
	@FXML
	private Label dmfLabel,dmmLabel,dmaLabel,drfLabel,drmLabel,draLabel;
		
	Hydrostatic hydrostatic;
	
@FXML
private void initialize() {
	
	hydrostatic=Hydrostatic.getInstance();
	ShipData shipData=ShipData.getInstance();
	
	// draw Ship with waterline
	sideViewSVG.setContent(hydrostatic.getSideView() );
	sideViewSVG.getTransforms().add(new Scale(0.005,0.005,0.0));
	
	// line equation: y(x)=ax + b
	double b = -hydrostatic.draftAftM.doubleValue()*1000;
	double a = (-hydrostatic.draftForwardM.doubleValue()+hydrostatic.draftAftM.doubleValue())/shipData.getLpp();
			
	line.setStartX(-10000);
	line.setStartY(line.getStartX()*a+b);
	
	line.setEndX(shipData.getLpp()*1000+10000);
	line.setEndY(line.getEndX()*a+b);
	
	line.getTransforms().add(new Scale(0.005,0.005,0.0));
	line.setStrokeWidth(250);
	
		
	// rotate ship with current trim 
	double trimAngle=180.0/Math.PI*Math.atan2(hydrostatic.draftForwardM.get()-hydrostatic.draftAftM.get(), shipData.getLpp());
	System.out.println(trimAngle);
	
	sideViewSVG.getTransforms().add(new Rotate(trimAngle,hydrostatic.lcf.doubleValue()*1000,hydrostatic.draftMidM.doubleValue()*-1000));
	line.getTransforms().add(new Rotate(trimAngle,hydrostatic.lcf.doubleValue()*1000,hydrostatic.draftMidM.doubleValue()*-1000));
	
	
	
	// Assign Data to the table
	
		ObservableList<Mass> masses = MassList.getInstance();
		String format=new String("%.2f");
		
		
		Mass lightShipMass=masses.stream().filter(m->m.getMassCategory()==MassCategory.LIGHTSHIP).findFirst().get();
		lightShipMassLabel.setText(String.format(format,lightShipMass.getMass()));
		lightShipLCG.setText(String.format(format,lightShipMass.getX()));
		lightShipVCG.setText(String.format(format,lightShipMass.getZ()));
		lightShipTCG.setText(String.format(format,lightShipMass.getY()));
		lightShipFROB.setText(String.format(format,lightShipMass.getFsm()));
		
		FilteredList<Mass> coverMassList= masses.filtered(m->(m.getMassCategory()==MassCategory.COVER||m.getMassCategory()==MassCategory.TWEENDECKS));
		double coverMassValue=coverMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		coverMass.setText(String.format(format,coverMassValue));
		coverLCG.setText(String.format(format,coverMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(coverMassValue >0 ?coverMassValue:1)));
		coverVCG.setText(String.format(format,coverMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(coverMassValue >0 ?coverMassValue:1)));
		coverTCG.setText(String.format(format,coverMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(coverMassValue >0 ?coverMassValue:1)));
		coverFROB.setText(String.format(format,coverMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));
	
		Mass storesMass=masses.stream().filter(m->m.getMassCategory()==MassCategory.STORE).findFirst().get();
		storesMassLabel.setText(String.format(format,storesMass.getMass()));
		storesLCG.setText(String.format(format,storesMass.getX()));
		storesVCG.setText(String.format(format,storesMass.getZ()));
		storesTCG.setText(String.format(format,storesMass.getY()));
		storesFROB.setText(String.format(format,storesMass.getFsm()));
		
		FilteredList<Mass> tankMassList= masses.filtered(m->(m.getMassCategory()==MassCategory.BALLASTTANK||m.getMassCategory()==MassCategory.LUBTANK||m.getMassCategory()==MassCategory.HFOTANK));
		double tankMassValue=tankMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		tanksMass.setText(String.format(format,tankMassValue));
		tanksLCG.setText(String.format(format,tankMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(tankMassValue>0?tankMassValue:1)));
		tanksVCG.setText(String.format(format,tankMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(tankMassValue>0?tankMassValue:1)));
		tanksTCG.setText(String.format(format,tankMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(tankMassValue>0?tankMassValue:1)));
		tanksFROB.setText(String.format(format,tankMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));

		FilteredList<Mass> holdWaterMassList= masses.filtered(m->m.getMassCategory()==MassCategory.HOLDWATER);
		double holdWaterMassValue=coverMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		holdWaterMass.setText(String.format(format,holdWaterMassValue));
		holdWaterLCG.setText(String.format(format,holdWaterMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(holdWaterMassValue >0 ?holdWaterMassValue:1)));
		holdWaterVCG.setText(String.format(format,holdWaterMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(holdWaterMassValue >0 ?holdWaterMassValue:1)));
		holdWaterTCG.setText(String.format(format,holdWaterMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(holdWaterMassValue >0 ?holdWaterMassValue:1)));
		holdWaterFROB.setText(String.format(format,holdWaterMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));
	
		FilteredList<Mass> containerMassList= masses.filtered(m->m.getMassCategory()==MassCategory.CONTAINER);
		double containerMassValue=coverMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		containerMass.setText(String.format(format,containerMassValue));
		containerLCG.setText(String.format(format,containerMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(containerMassValue >0 ?containerMassValue:1)));
		containerVCG.setText(String.format(format,containerMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(containerMassValue >0 ?containerMassValue:1)));
		containerTCG.setText(String.format(format,containerMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(containerMassValue >0 ?containerMassValue:1)));
		containerFROB.setText(String.format(format,containerMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));
	
		FilteredList<Mass> specialCargoMassList= masses.filtered(m->m.getMassCategory()==MassCategory.SPECIALCARGO);
		double specialCargoMassValue=coverMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		specialCargoMass.setText(String.format(format,specialCargoMassValue));
		specialCargoLCG.setText(String.format(format,specialCargoMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(specialCargoMassValue >0 ?specialCargoMassValue:1)));
		specialCargoVCG.setText(String.format(format,specialCargoMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(specialCargoMassValue >0 ?specialCargoMassValue:1)));
		specialCargoTCG.setText(String.format(format,specialCargoMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(specialCargoMassValue >0 ?specialCargoMassValue:1)));
		specialCargoFROB.setText(String.format(format,specialCargoMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));
	
		FilteredList<Mass> freeInputMassList= masses.filtered(m->m.getMassCategory()==MassCategory.CARGO);
		double freeInputMassValue=freeInputMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		freeInputMass.setText(String.format(format,freeInputMassValue));
		freeInputLCG.setText(String.format(format,freeInputMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(freeInputMassValue >0 ?freeInputMassValue:1)));
		freeInputVCG.setText(String.format(format,freeInputMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(freeInputMassValue >0 ?freeInputMassValue:1)));
		freeInputTCG.setText(String.format(format,freeInputMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(freeInputMassValue >0 ?freeInputMassValue:1)));
		freeInputFROB.setText(String.format(format,freeInputMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));

		FilteredList<Mass> bulkMassList= masses.filtered(m->m.getMassCategory()==MassCategory.BULK);
		double bulkMassValue=bulkMassList.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		bulkMass.setText(String.format(format,bulkMassValue));
		bulkLCG.setText(String.format(format,bulkMassList.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(bulkMassValue >0 ?bulkMassValue:1)));
		bulkVCG.setText(String.format(format,bulkMassList.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(bulkMassValue >0 ?bulkMassValue:1)));
		bulkTCG.setText(String.format(format,bulkMassList.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(bulkMassValue >0 ?bulkMassValue:1)));
		bulkFROB.setText(String.format(format,bulkMassList.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));

		
		double massValue=masses.stream().map(mass->mass.getMass()).reduce(0.0, (r,e)->r+e);
		
		totalMass.setText(String.format(format,massValue));
		totalLCG.setText(String.format(format,masses.stream().map(mass->mass.getMass()*mass.getX()).reduce( 0.0, (r,e)->r+e)/(massValue >0 ?massValue:1)));
		totalVCG.setText(String.format(format,masses.stream().map(mass->mass.getMass()*mass.getZ()).reduce( 0.0, (r,e)->r+e)/(massValue >0 ?massValue:1)));
		totalTCG.setText(String.format(format,masses.stream().map(mass->mass.getMass()*mass.getY()).reduce( 0.0, (r,e)->r+e)/(massValue >0 ?massValue:1)));
		totalFROB.setText(String.format(format,masses.stream().map(mass->mass.getFsm()).reduce( 0.0, (r,e)->r+e)));

		// Parameter of Stability
		format=new String("%.3f");
		kmLabel.setText(String.format(format, hydrostatic.km.get()));
		kgoLabel.setText(String.format(format, hydrostatic.kg.get()));
		gmoLabel.setText(String.format(format, hydrostatic.km.get()-hydrostatic.kg.get()));
		dgmLabel.setText(String.format(format, hydrostatic.gmDelta.get()));
		gmLabel.setText(String.format(format, hydrostatic.gm.get()));
		
		//drafts on marks and on perpendicular
		
		dmfLabel.setText(String.format(format, hydrostatic.draftForwardM.get()));
		dmmLabel.setText(String.format(format, hydrostatic.draftMidM.get()));
		dmaLabel.setText(String.format(format, hydrostatic.draftAftM.get()));
	}

	}