package wbriese.loco2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class Navigation  {
	
	//public static Hydrostatic hydro;
	//public static MassList massTable;
	private  BorderPane root;
	
	public Navigation(BorderPane root2) {
		this.root=root2;
	}

	public void createStartPage () {
		
		//create Menu Special
		MenuItem menuItemStores=new MenuItem("Stores");
		menuItemStores.setOnAction(e->createStoresScreen(e));
		
		MenuItem menuItemShipTotal=new MenuItem("Ship total");
		menuItemShipTotal.setOnAction(e->createShipTotalScreen(e));
		
		MenuItem menuItemSeaworthiness=new MenuItem("Seaworthiness");
		menuItemSeaworthiness.setOnAction(e->createSeaworthinessScreen(e));
		
		MenuItem menuItemCover=new MenuItem("Cover");
		
		MenuItem menuItemDraftSurvey=new MenuItem("Draft Survey");
		
		Menu special=new Menu("Special");
		special.getItems().addAll(menuItemStores,menuItemShipTotal,menuItemSeaworthiness,menuItemCover,menuItemDraftSurvey);
		
		//create Menu Free input
		MenuItem menuItemInput = new MenuItem("Input");
		menuItemInput.setOnAction(e->createFreeInputScreen(e));
		
		MenuItem menuItemInputList = new MenuItem ("List");
		menuItemInputList.setOnAction(e->createFreeInputListScreen(e));
		
		Menu freeInputMenu=new Menu("Free input");
		freeInputMenu.getItems().addAll(menuItemInput,menuItemInputList);
		
		//create Menu Tanks
		MenuItem menuItemTanksInput=new MenuItem("Input");
		menuItemTanksInput.setOnAction(e->createTanksInputListScreen(e));
		
		MenuItem menuItemTanksJournal=new MenuItem("Journal");
		menuItemTanksJournal.setOnAction(e->createTanksJournalScreen(e));
		
		MenuItem menuItemTanksList=new MenuItem("List");
		menuItemTanksList.setOnAction(e->createTanksListScreen(e));
		
		Menu menuTanks = new Menu("Tanks");
		menuTanks.getItems().addAll(menuItemTanksInput,menuItemTanksList,menuItemTanksJournal);
		
		
		//put Menu in Top Area of Borderpane
		this.root.setTop(new MenuBar(special,freeInputMenu,menuTanks));		
		
	}
	
	private Object createTanksListScreen(ActionEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object createTanksJournalScreen(ActionEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object createTanksInputListScreen(ActionEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object createFreeInputListScreen(ActionEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object createFreeInputScreen(ActionEvent e) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createShipTotalScreen(ActionEvent e) {
		try {
			VBox shipTotal=(VBox) FXMLLoader.load(getClass().getResource("ShipTotal.fxml"));
			this.root.setCenter(shipTotal); 
			this.root.setLeft(null);
			this.root.setBottom(null);
							
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void createSeaworthinessScreen(ActionEvent e) {
		try {
			HBox seaworthiness=(HBox) FXMLLoader.load(getClass().getResource("Seaworthiness.fxml"));
			this.root.setCenter(seaworthiness); 
							
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void createStoresScreen(Event e) {
		
		try {
			
			  HBox storesHBox=(HBox)FXMLLoader.load(getClass().getResource("Stores.fxml"));
			  this.root.setCenter(storesHBox);
			  
//			  Group shipView=(Group)FXMLLoader.load(getClass().getResource("ShipView.fxml"));
//			  this.root.setCenter(shipView);
//			  
			  			  
			  HBox bottomBar=(HBox)FXMLLoader.load(getClass().getResource("BottomBar.fxml"));
			  this.root.setBottom(bottomBar);
			 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
}
