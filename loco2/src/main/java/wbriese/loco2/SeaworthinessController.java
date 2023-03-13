package wbriese.loco2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class SeaworthinessController {
	

	@FXML
	private Label gz0;	
	@FXML
	private Label gz10;	
	@FXML
	private Label gz20;
	@FXML
	private Label gz30;	
	@FXML
	private Label gz40;	
	@FXML
	private Label gz50;	
	@FXML
	private Label gz60;
	
	@FXML
	private LineChart<Number,Number> stabChart;
	
	private ObservableList<XYChart.Series<Number,Number>> stabilitySeriesList=FXCollections.<XYChart.Series<Number,Number>>observableArrayList();
	
	

	@FXML
	private void initialize() {
		//bind label to Hydrostatic values
		String format="%.2f";
		Hydrostatic hydrostatic=Hydrostatic.getInstance();
		//gz0.textProperty().bind(hydrostatic.gz0.asString(format));
		gz10.textProperty().bind(hydrostatic.gz10.asString(format));
		gz20.textProperty().bind(hydrostatic.gz20.asString(format));
		gz30.textProperty().bind(hydrostatic.gz30.asString(format));
		gz40.textProperty().bind(hydrostatic.gz40.asString(format));
		gz50.textProperty().bind(hydrostatic.gz50.asString(format));
		gz60.textProperty().bind(hydrostatic.gz60.asString(format));
		
	
		//bind chart to Hydrostatic values
		stabChart.setData(stabilitySeriesList);
		
		
	     XYChart.Series<Number,Number> gzSeries= new XYChart.Series<>(); 
		 gzSeries.getData().add(new XYChart.Data<Number,Number>(10,hydrostatic.gz10.get()));
		 gzSeries.getData().add(new XYChart.Data<Number,Number>(20,hydrostatic.gz20.get()));
		 gzSeries.getData().add(new XYChart.Data<Number,Number>(30,hydrostatic.gz30.get()));
		 gzSeries.getData().add(new XYChart.Data<Number,Number>(40,hydrostatic.gz40.get()));
		 gzSeries.getData().add(new XYChart.Data<Number,Number>(50,hydrostatic.gz50.get()));
		 gzSeries.getData().add(new XYChart.Data<Number,Number>(60,hydrostatic.gz60.get()));
		 stabilitySeriesList.add(gzSeries);
			
		
}
}
