package wbriese.loco2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BottomBarController {

	@FXML 
	private Label displacementLabel;
	
	@FXML
	private Label drfLabel;
	
	@FXML
	private Label drmLabel;
	
	@FXML
	private Label draLabel;
	
	@FXML
	private Label trimLabel;
	
	@FXML
	private Label listLabel;
	
	@FXML
	private Label GMLabel;


@FXML
private void initialize() {
	//bind label to Hydrostatic values
	String format="%.2f";
	Hydrostatic hydrostatic=Hydrostatic.getInstance();
	displacementLabel.textProperty().bind(hydrostatic.displacement.asString());
	drfLabel.textProperty().bind(hydrostatic.draftForwardM.asString(format));
	drmLabel.textProperty().bind(hydrostatic.draftMidM.asString(format));
	draLabel.textProperty().bind(hydrostatic.draftAftM.asString(format));
	draLabel.textProperty().bind(hydrostatic.draftAftM.asString(format));
	
	trimLabel.textProperty().bind(hydrostatic.trim.asString(format));
	listLabel.textProperty().bind(hydrostatic.list.asString(format));
	GMLabel.textProperty().bind(hydrostatic.gm.asString(format));
	
	
	
	}

}