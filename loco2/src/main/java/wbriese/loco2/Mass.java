package wbriese.loco2;


import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Mass {
		

		private final ReadOnlyIntegerWrapper massId =
				 new ReadOnlyIntegerWrapper(this, "massId", massSequence.incrementAndGet());
		private final StringProperty massIdentifier =
				 new SimpleStringProperty(this, "massIdentifier", "");
		private final SimpleObjectProperty<MassCategory> massCategory =
				 new SimpleObjectProperty<>(this, "massCategory", null);
		private final SimpleDoubleProperty mass =
				 new SimpleDoubleProperty(this, "mass",0);
		private final SimpleDoubleProperty x =
				 new SimpleDoubleProperty(this, "x",0);
		private final SimpleDoubleProperty y =
				 new SimpleDoubleProperty(this, "y",0);
		private final SimpleDoubleProperty z =
				 new SimpleDoubleProperty(this, "z",0);
		private final SimpleDoubleProperty fsm =
				 new SimpleDoubleProperty(this, "fsm",0);
		private final SimpleDoubleProperty xa =
				 new SimpleDoubleProperty(this, "xa",0);
		private final SimpleDoubleProperty xf =
				 new SimpleDoubleProperty(this, "xf",0);
		
		public Mass() {}
		
		// Keeps track of last generated person id
		private static AtomicInteger massSequence = new AtomicInteger(0);

	
		public Mass(String identifier,MassCategory category, Double mass, Double x, Double y, Double z, Double fsm, Double xa, Double xf) {
			this.massIdentifier.set(identifier);
			this.massCategory.set(category);
			this.mass.set(mass);
			this.x.set(x);
			this.y.set(y);
			this.z.set(z);
			this.fsm.set(fsm);
			this.xa.set(xa);
			this.xf.set(xf);
				
		}

		/* massId Property */
		public final int getMassId() {
			return massId.get();
		}

		public final ReadOnlyIntegerProperty massIdProperty() {
			return massId.getReadOnlyProperty();
		}

		/* identifier Property */
		public final String getMassIdentifier() {
			return massIdentifier.get();
		}

		public final void setMassIdentifier(String identifier) {
			massIdentifier.set(identifier);
		}

		public final StringProperty massIdentifierProperty() {
			return massIdentifier;
		}

		
		/* Mass Category Property */
		public final MassCategory getMassCategory() {
			return massCategory.get();
		}

		public final void setMassCategory(MassCategory category) {
			massCategoryProperty().set(category);
		}

		public final ObjectProperty<MassCategory> massCategoryProperty() {
			return massCategory;
		}
		
	
		
		// mass Property 
		
		  public final Double getMass() { return mass.get(); }
		  
		  public final void setMass(Double mass) { massProperty().set(mass); }
		  
		  public final DoubleProperty massProperty() { return mass; }
		 
		  
		
		/* x Property */
		public final Double getX() {
			return x.get();
		}

		public final void setX(Double x) {
			xProperty().set(x);
		}

		public final DoubleProperty xProperty() {
			return x;
		}


		/* y Property */
		public final Double getY() {
			return y.get();
		}

		public final void setY(Double y) {
			yProperty().set(y);
		}

		public final DoubleProperty yProperty() {
			return y;
		}

		/* z Property */
		public final Double getZ() {
			return z.get();
		}

		public final void setZ(Double z) {
			zProperty().set(z);
		}

		public final DoubleProperty zProperty() {
			return z;
		}

		/* fsm Property */
		public final double getFsm() {
			return fsm.get();
		}

		public final void setFsm(double fsmValue) {
			fsmProperty().set(fsmValue);
		}

		public final DoubleProperty fsmProperty() {
			return fsm;
		}
			
		@Override
		public String toString() {
			return "[mass=" + massIdentifier.get() + 
			       ", mass=" + mass.get() + 
			       ", x=" + x.get() + 
			       ", y=" + y.get() + 
			       ", z=" + z.get() +"]";
		}
	}



