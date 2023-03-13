package wbriese.loco2;


import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

public class StoresController {
	
	@FXML
	private Spinner<Double> storeMassSpinner;
	
	@FXML
	private Spinner<Double> lcgSpinner;
	
	@FXML
	private Spinner<Double> vcgSpinner;
	
	@FXML
	private Spinner<Double> tcgSpinner;
	
	@FXML
	private SVGPath sideViewSVG;

	@FXML
	private Group group;

	@FXML
	private Line line;

	@FXML
	private Rectangle rectangle;

	
	@FXML
	private Label drfLabel;
	
	@FXML
	private Label drmLabel;
	
	@FXML
	private Label draLabel;
	
	@FXML
	private void initialize() {
		
		Hydrostatic hydrostatic=Hydrostatic.getInstance();
		ShipData shipData = ShipData.getInstance();
		
		//bind label to Hydrostatic values
		String format="%.2f";
	
		drfLabel.textProperty().bind(hydrostatic.draftForwardM.asString(format));
		drmLabel.textProperty().bind(hydrostatic.draftMidM.asString(format));
		draLabel.textProperty().bind(hydrostatic.draftAftM.asString(format));
		
		
		//Binde die STORE Masse an die eingegebenen Werte im User Interface
		ObservableList<Mass> masslist=MassList.getInstance();
		Mass storeMass=masslist.filtered(mass->mass.getMassCategory().equals(MassCategory.STORE)).get(0);
		storeMass.massProperty().unbind();
		
		storeMassSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,5000,storeMass.getMass(),0.1));
		storeMass.massProperty().bind(storeMassSpinner.getValueFactory().valueProperty());
		
		lcgSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,shipData.getLoa(),storeMass.getX(),0.1));
		storeMass.xProperty().bind(lcgSpinner.valueProperty());
		
		vcgSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,shipData.getHeight(),storeMass.getZ(),0.01));
		storeMass.zProperty().bind(vcgSpinner.valueProperty());
		
		tcgSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-shipData.getBeam()/2,shipData.getBeam()/2,storeMass.getY(),0.01));
		storeMass.yProperty().bind(tcgSpinner.valueProperty());
		
		
		sideViewSVG.setContent(hydrostatic.getSideView() );
		sideViewSVG.getTransforms().add(new Scale(0.005,0.005,0.0));
			
		
		line.setStartX(-10000);
		line.startYProperty().bind(hydrostatic.draftMidM.multiply(-1000));
				
		line.setEndX(shipData.getLpp()*1000+10000);
		line.endYProperty().bind(hydrostatic.draftMidM.multiply(-1000));
		
	
		
		line.getTransforms().add(new Scale(0.005,0.005,0.0));
		line.setStrokeWidth(250);
		
		rectangle.xProperty().bind(storeMass.xProperty().multiply(1000).subtract(1000));
		rectangle.yProperty().bind(storeMass.zProperty().multiply(-1000).add(1000));
		rectangle.getTransforms().add(new Scale(0.005,0.005,0.0));
		
		rectangle.setStrokeWidth(250);
		rectangle.setHeight(2000);
		rectangle.setWidth(2000);
		
		
		
		DoubleBinding trimAngle = new DoubleBinding() { 
			{
				this.bind(hydrostatic.draftAftM,hydrostatic.draftForwardM);	
			}

			@Override
			protected double computeValue() {
				return 180.0/Math.PI*Math.atan2(hydrostatic.draftForwardM.subtract(hydrostatic.draftAftM).get(), shipData.getLpp());
			}
		};
		
		//Rotate ship
		
		Rotate rotate=new Rotate();
		rotate.angleProperty().bind(trimAngle);
		rotate.pivotXProperty().bind(hydrostatic.lcf.multiply(1000.0));
		rotate.pivotYProperty().bind(hydrostatic.draftMidM.multiply(-1000.0));
		
		sideViewSVG.getTransforms().add(rotate);
		rectangle.getTransforms().add(rotate);
		
		}
	
	
	
}
