/**
 * 
 */
package objects;

import java.lang.reflect.Field;

import utils.NumericUtils;

/**
 * Describes and contains the statistics for the solution
 * 
 * @author gperr
 *
 */
public class SolStats {

	private int numResources;			// number of resources in this execution
	private int numTasks; 				// number of tasks in this execution
	private double dbMaxX;				// maximum value for X
	private double dbMaxY;				// maximum value for Y
	private double dbTasksDensity;		// density of the tasks 
	
	private int numSolutionsFound;		// Number of solutions found by the solver
	private double dblExecutionTime;		// Running time of the solver
	private int iNumThreads;			// number of threads used 
	
	// information on the best solution found
	private int iTotServices;			// total number of tasks serviced
	private double dbTraveledDistance;	// traveled distance for the best solution
	private double dbTransportTime;		// transport time for the best solution
	private double dbCompletionTime;	// completion time for the best solution
	private double dbWaitingTime;		// waiting time for the best solution
	private double dbTotalCosts;		// total costs for the best solution
	private double iNumDeliveries;		// number of deliveries
    
	
	/**
	 * Returns a string formatted as .csv with the data members names
	 * 
	 * @return	String	string with all data members names separated by a ";"
	 */
	public String getHeaderString() {
		String str = "";

		// get total number of fields for this class
		int numDM = this.getClass().getDeclaredFields().length;
		
		// gets the list of fields for this class
		Field[] fields = this.getClass().getDeclaredFields();
		
		// parse the fields and creates the return string accordingly
		for (int i = 0; i < numDM; i++ )
				str += (fields[i].getName() + ";");
						
		return str.substring(0, str.length()-1);
	
	}

	/**
	 * Returns a string formatted as .csv with contents of the data members for the instance
	 * 
	 * @return	String	string with all data members values separated by a ";"
	 */
	public String toString() {
		
		NumericUtils.setDefaultFormat();
		
		String str = "";
		
		// get total number of fields for this class
		int numDM = this.getClass().getDeclaredFields().length;
		
		// gets the list of fields for this class
		Field[] fields = this.getClass().getDeclaredFields();
		
		try {

			// parse the fields and creates the return string accordingly
			for (int i = 0; i < numDM; i++ )			
					str += (fields[i].get(this) + ";");			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		str = str.replace('.', ',');		// replace dots with commas as decimal separators
		
		return str.substring(0, str.length()-1);
	}


	
	/**
	 * @return the numSolutionsFound
	 */
	public int getNumSolutionsFound() {
		return numSolutionsFound;
	}
	/**
	 * @param numSolutionsFound the numSolutionsFound to set
	 */
	public void setNumSolutionsFound(int numSolutionsFound) {
		this.numSolutionsFound = numSolutionsFound;
	}
	/**
	 * @return the numResources
	 */
	public int getNumResources() {
		return numResources;
	}
	
	/**
	 * @param numResources the numResources to set
	 */
	public void setNumResources(int numResources) {
		this.numResources = numResources;
	}
	/**
	 * @return the numTasks
	 */
	public int getNumTasks() {
		return numTasks;
	}
	/**
	 * @param numTasks the numTasks to set
	 */
	public void setNumTasks(int numTasks) {
		this.numTasks = numTasks;
	}


	/**
	 * @return the dbTraveledDistance
	 */
	public double getDbTraveledDistance() {
		return dbTraveledDistance;
	}

	/**
	 * @param dbTraveledDistance the dbTraveledDistance to set
	 */
	public void setDbTraveledDistance(double dbTraveledDistance) {
		this.dbTraveledDistance = dbTraveledDistance;
	}

	/**
	 * @return the dbTransportTime
	 */
	public double getDbTransportTime() {
		return dbTransportTime;
	}

	/**
	 * @param dbTransportTime the dbTransportTime to set
	 */
	public void setDbTransportTime(double dbTransportTime) {
		this.dbTransportTime = dbTransportTime;
	}

	/**
	 * @return the dbCompletionTime
	 */
	public double getDbCompletionTime() {
		return dbCompletionTime;
	}

	/**
	 * @param dbCompletionTime the dbCompletionTime to set
	 */
	public void setDbCompletionTime(double dbCompletionTime) {
		this.dbCompletionTime = dbCompletionTime;
	}

	/**
	 * @return the dbWaitingTime
	 */
	public double getDbWaitingTime() {
		return dbWaitingTime;
	}

	/**
	 * @param dbWaitingTime the dbWaitingTime to set
	 */
	public void setDbWaitingTime(double dbWaitingTime) {
		this.dbWaitingTime = dbWaitingTime;
	}

	/**
	 * @return the dbTotalCosts
	 */
	public double getDbTotalCosts() {
		return dbTotalCosts;
	}

	/**
	 * @param dbTotalCosts the dbTotalCosts to set
	 */
	public void setDbTotalCosts(double dbTotalCosts) {
		this.dbTotalCosts = dbTotalCosts;
	}

	/**
	 * @return the iNumDeliveries
	 */
	public double getiNumDeliveries() {
		return iNumDeliveries;
	}

	/**
	 * @param iNumDeliveries the iNumDeliveries to set
	 */
	public void setiNumDeliveries(double iNumDeliveries) {
		this.iNumDeliveries = iNumDeliveries;
	}

	/**
	 * @return the dblExecutionTime
	 */
	public double getDblExecutionTime() {
		return dblExecutionTime;
	}

	/**
	 * @param dblExecutionTime the dblExecutionTime to set
	 */
	public void setDblExecutionTime(double dblExecutionTime) {
		this.dblExecutionTime = dblExecutionTime;
	}

	/**
	 * @return the iNumThreads
	 */
	public int getiNumThreads() {
		return iNumThreads;
	}

	/**
	 * @param iNumThreads the iNumThreads to set
	 */
	public void setiNumThreads(int iNumThreads) {
		this.iNumThreads = iNumThreads;
	}

	/**
	 * @return the dbMaxX
	 */
	public double getDbMaxX() {
		return dbMaxX;
	}

	/**
	 * @param dbMaxX the dbMaxX to set
	 */
	public void setDbMaxX(double dbMaxX) {
		this.dbMaxX = dbMaxX;
	}

	/**
	 * @return the dbMaxY
	 */
	public double getDbMaxY() {
		return dbMaxY;
	}

	/**
	 * @param dbMaxY the dbMaxY to set
	 */
	public void setDbMaxY(double dbMaxY) {
		this.dbMaxY = dbMaxY;
	}

	/**
	 * @return the dbTasksDensity
	 */
	public double getDbTasksDensity() {
		return dbTasksDensity;
	}

	/**
	 * @param dbTasksDensity the dbTasksDensity to set
	 */
	public void setDbTasksDensity(double dbTasksDensity) {
		this.dbTasksDensity = dbTasksDensity;
	}

	/**
	 * @return the iTotServices
	 */
	public int getiTotServices() {
		return iTotServices;
	}

	/**
	 * @param iTotServices the iTotServices to set
	 */
	public void setiTotServices(int iTotServices) {
		this.iTotServices = iTotServices;
	}

}
