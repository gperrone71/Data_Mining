/**
 * 
 */
package objects;

import java.lang.reflect.Field;

import utils.NumericUtils;

/**
 * Describes and contains the statistics for the batch classifier
 * 
 * @author gperr
 *
 */
public class ClassifierStats {

	// Information describing the instance
	private int numResources;			// number of resources in this execution
	private double dbMaxX;				// maximum value for X
	private double dbMaxY;				// maximum value for Y

	// generic information
	private int iNumThreads;			// number of threads used 
	
	// Information on the unpruned solution
	private int numTasks_UP; 				// number of tasks in this execution
	private double dbTasksDensity_UP;		// density of the tasks 
	private int numSolutionsFound_UP;		// Number of solutions found by the solver
	private double dblExecutionTime_UP;		// Running time of the solver
	private int iTotServiced_UP;			// total number of tasks serviced
	private double dbTraveledDistance_UP;	// traveled distance for the best solution
	private double dbCompletionTime_UP;	// completion time for the best solution
	private double dbWaitingTime_UP;		// waiting time for the best solution
	private double dbTotalCosts_UP;		// total costs for the best solution
    
	// Information on the PRUNED solution
	private int numTasks_P; 			// number of the pruned tasks
	private double dbTasksDensity_P;	// tasks density for the pruned set of tasks
	private int numSolutionsFound_P;		// Number of solutions found by the solver
	private double dblExecutionTime_P;		// Running time of the solver
	private int iTotServiced_P;			// total number of tasks serviced
	private double dbTraveledDistance_P;	// traveled distance for the best solution
	private double dbCompletionTime_P;	// completion time for the best solution
	private double dbWaitingTime_P;		// waiting time for the best solution
	private double dbTotalCosts_P;		// total costs for the best solution
	
	// stats on differences between the two
	private double dbAbsExecTimeDiff;			// difference between the execution times (absolute)
	private double dbPerExecTimeDiff;			// difference between the execution times (%)
	private double dbAbsSrvcdTasksDiff;			// difference between the execution times (absolute)
	private double dbPerSrvcdTasksDiff;			// difference between the execution times (%)
	
	// information on the trained classifier
	private double dbPrecision;					// classifier precision
	private double dbRecall;					// classifier recall
	private double dbAbsCorrectlyClassified;		// number of correctly classified instances (absolute)
	private double dbPerCorrectlyClassified;		// number of correctly classified instances (%)	
	private double dbAbsUncorrectlyClassified;		// number of uncorrectly classified instances (absolute)
	private double dbPerUncorrectlyClassified;		// number of uncorrectly classified instances (%)	
	
	
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
	 * @return the numTasks_UP
	 */
	public int getNumTasks_UP() {
		return numTasks_UP;
	}

	/**
	 * @param numTasks_UP the numTasks_UP to set
	 */
	public void setNumTasks_UP(int numTasks_UP) {
		this.numTasks_UP = numTasks_UP;
	}

	/**
	 * @return the dbTasksDensity_UP
	 */
	public double getDbTasksDensity_UP() {
		return dbTasksDensity_UP;
	}

	/**
	 * @param dbTasksDensity_UP the dbTasksDensity_UP to set
	 */
	public void setDbTasksDensity_UP(double dbTasksDensity_UP) {
		this.dbTasksDensity_UP = dbTasksDensity_UP;
	}

	/**
	 * @return the numSolutionsFound_UP
	 */
	public int getNumSolutionsFound_UP() {
		return numSolutionsFound_UP;
	}

	/**
	 * @param numSolutionsFound_UP the numSolutionsFound_UP to set
	 */
	public void setNumSolutionsFound_UP(int numSolutionsFound_UP) {
		this.numSolutionsFound_UP = numSolutionsFound_UP;
	}

	/**
	 * @return the dblExecutionTime_UP
	 */
	public double getDblExecutionTime_UP() {
		return dblExecutionTime_UP;
	}

	/**
	 * @param dblExecutionTime_UP the dblExecutionTime_UP to set
	 */
	public void setDblExecutionTime_UP(double dblExecutionTime_UP) {
		this.dblExecutionTime_UP = dblExecutionTime_UP;
	}

	/**
	 * @return the iTotServiced_UP
	 */
	public int getiTotServiced_UP() {
		return iTotServiced_UP;
	}

	/**
	 * @param iTotServiced_UP the iTotServiced_UP to set
	 */
	public void setiTotServiced_UP(int iTotServiced_UP) {
		this.iTotServiced_UP = iTotServiced_UP;
	}

	/**
	 * @return the dbTraveledDistance_UP
	 */
	public double getDbTraveledDistance_UP() {
		return dbTraveledDistance_UP;
	}

	/**
	 * @param dbTraveledDistance_UP the dbTraveledDistance_UP to set
	 */
	public void setDbTraveledDistance_UP(double dbTraveledDistance_UP) {
		this.dbTraveledDistance_UP = dbTraveledDistance_UP;
	}

	/**
	 * @return the dbCompletionTime_UP
	 */
	public double getDbCompletionTime_UP() {
		return dbCompletionTime_UP;
	}

	/**
	 * @param dbCompletionTime_UP the dbCompletionTime_UP to set
	 */
	public void setDbCompletionTime_UP(double dbCompletionTime_UP) {
		this.dbCompletionTime_UP = dbCompletionTime_UP;
	}

	/**
	 * @return the dbWaitingTime_UP
	 */
	public double getDbWaitingTime_UP() {
		return dbWaitingTime_UP;
	}

	/**
	 * @param dbWaitingTime_UP the dbWaitingTime_UP to set
	 */
	public void setDbWaitingTime_UP(double dbWaitingTime_UP) {
		this.dbWaitingTime_UP = dbWaitingTime_UP;
	}

	/**
	 * @return the dbTotalCosts_UP
	 */
	public double getDbTotalCosts_UP() {
		return dbTotalCosts_UP;
	}

	/**
	 * @param dbTotalCosts_UP the dbTotalCosts_UP to set
	 */
	public void setDbTotalCosts_UP(double dbTotalCosts_UP) {
		this.dbTotalCosts_UP = dbTotalCosts_UP;
	}

	/**
	 * @return the numTasks_P
	 */
	public int getNumTasks_P() {
		return numTasks_P;
	}

	/**
	 * @param numTasks_P the numTasks_P to set
	 */
	public void setNumTasks_P(int numTasks_P) {
		this.numTasks_P = numTasks_P;
	}

	/**
	 * @return the dbTasksDensity_P
	 */
	public double getDbTasksDensity_P() {
		return dbTasksDensity_P;
	}

	/**
	 * @param dbTasksDensity_P the dbTasksDensity_P to set
	 */
	public void setDbTasksDensity_P(double dbTasksDensity_P) {
		this.dbTasksDensity_P = dbTasksDensity_P;
	}

	/**
	 * @return the numSolutionsFound_P
	 */
	public int getNumSolutionsFound_P() {
		return numSolutionsFound_P;
	}

	/**
	 * @param numSolutionsFound_P the numSolutionsFound_P to set
	 */
	public void setNumSolutionsFound_P(int numSolutionsFound_P) {
		this.numSolutionsFound_P = numSolutionsFound_P;
	}

	/**
	 * @return the dblExecutionTime_P
	 */
	public double getDblExecutionTime_P() {
		return dblExecutionTime_P;
	}

	/**
	 * @param dblExecutionTime_P the dblExecutionTime_P to set
	 */
	public void setDblExecutionTime_P(double dblExecutionTime_P) {
		this.dblExecutionTime_P = dblExecutionTime_P;
	}

	/**
	 * @return the iTotServiced_P
	 */
	public int getiTotServiced_P() {
		return iTotServiced_P;
	}

	/**
	 * @param iTotServiced_P the iTotServiced_P to set
	 */
	public void setiTotServiced_P(int iTotServiced_P) {
		this.iTotServiced_P = iTotServiced_P;
	}

	/**
	 * @return the dbTraveledDistance_P
	 */
	public double getDbTraveledDistance_P() {
		return dbTraveledDistance_P;
	}

	/**
	 * @param dbTraveledDistance_P the dbTraveledDistance_P to set
	 */
	public void setDbTraveledDistance_P(double dbTraveledDistance_P) {
		this.dbTraveledDistance_P = dbTraveledDistance_P;
	}

	/**
	 * @return the dbCompletionTime_P
	 */
	public double getDbCompletionTime_P() {
		return dbCompletionTime_P;
	}

	/**
	 * @param dbCompletionTime_P the dbCompletionTime_P to set
	 */
	public void setDbCompletionTime_P(double dbCompletionTime_P) {
		this.dbCompletionTime_P = dbCompletionTime_P;
	}

	/**
	 * @return the dbWaitingTime_P
	 */
	public double getDbWaitingTime_P() {
		return dbWaitingTime_P;
	}

	/**
	 * @param dbWaitingTime_P the dbWaitingTime_P to set
	 */
	public void setDbWaitingTime_P(double dbWaitingTime_P) {
		this.dbWaitingTime_P = dbWaitingTime_P;
	}

	/**
	 * @return the dbTotalCosts_P
	 */
	public double getDbTotalCosts_P() {
		return dbTotalCosts_P;
	}

	/**
	 * @param dbTotalCosts_P the dbTotalCosts_P to set
	 */
	public void setDbTotalCosts_P(double dbTotalCosts_P) {
		this.dbTotalCosts_P = dbTotalCosts_P;
	}

	/**
	 * @return the dbAbsExecTimeDiff
	 */
	public double getDbAbsExecTimeDiff() {
		return dbAbsExecTimeDiff;
	}

	/**
	 * @param dbAbsExecTimeDiff the dbAbsExecTimeDiff to set
	 */
	public void setDbAbsExecTimeDiff(double dbAbsExecTimeDiff) {
		this.dbAbsExecTimeDiff = dbAbsExecTimeDiff;
	}

	/**
	 * @return the dbPerExecTimeDiff
	 */
	public double getDbPerExecTimeDiff() {
		return dbPerExecTimeDiff;
	}

	/**
	 * @param dbPerExecTimeDiff the dbPerExecTimeDiff to set
	 */
	public void setDbPerExecTimeDiff(double dbPerExecTimeDiff) {
		this.dbPerExecTimeDiff = dbPerExecTimeDiff;
	}

	/**
	 * @return the dbAbsSrvcdTasksDiff
	 */
	public double getDbAbsSrvcdTasksDiff() {
		return dbAbsSrvcdTasksDiff;
	}

	/**
	 * @param dbAbsSrvcdTasksDiff the dbAbsSrvcdTasksDiff to set
	 */
	public void setDbAbsSrvcdTasksDiff(double dbAbsSrvcdTasksDiff) {
		this.dbAbsSrvcdTasksDiff = dbAbsSrvcdTasksDiff;
	}

	/**
	 * @return the dbPerSrvcdTasksDiff
	 */
	public double getDbPerSrvcdTasksDiff() {
		return dbPerSrvcdTasksDiff;
	}

	/**
	 * @param dbPerSrvcdTasksDiff the dbPerSrvcdTasksDiff to set
	 */
	public void setDbPerSrvcdTasksDiff(double dbPerSrvcdTasksDiff) {
		this.dbPerSrvcdTasksDiff = dbPerSrvcdTasksDiff;
	}

	/**
	 * @return the dbPrecision
	 */
	public double getDbPrecision() {
		return dbPrecision;
	}

	/**
	 * @param dbPrecision the dbPrecision to set
	 */
	public void setDbPrecision(double dbPrecision) {
		this.dbPrecision = dbPrecision;
	}

	/**
	 * @return the dbRecall
	 */
	public double getDbRecall() {
		return dbRecall;
	}

	/**
	 * @param dbRecall the dbRecall to set
	 */
	public void setDbRecall(double dbRecall) {
		this.dbRecall = dbRecall;
	}

	/**
	 * @return the dbAbsCorrectlyClassified
	 */
	public double getDbAbsCorrectlyClassified() {
		return dbAbsCorrectlyClassified;
	}

	/**
	 * @param dbAbsCorrectlyClassified the dbAbsCorrectlyClassified to set
	 */
	public void setDbAbsCorrectlyClassified(double dbAbsCorrectlyClassified) {
		this.dbAbsCorrectlyClassified = dbAbsCorrectlyClassified;
	}

	/**
	 * @return the dbPerCorrectlyClassified
	 */
	public double getDbPerCorrectlyClassified() {
		return dbPerCorrectlyClassified;
	}

	/**
	 * @param dbPerCorrectlyClassified the dbPerCorrectlyClassified to set
	 */
	public void setDbPerCorrectlyClassified(double dbPerCorrectlyClassified) {
		this.dbPerCorrectlyClassified = dbPerCorrectlyClassified;
	}

	/**
	 * @return the dbAbsUncorrectlyClassified
	 */
	public double getDbAbsUncorrectlyClassified() {
		return dbAbsUncorrectlyClassified;
	}

	/**
	 * @param dbAbsUncorrectlyClassified the dbAbsUncorrectlyClassified to set
	 */
	public void setDbAbsUncorrectlyClassified(double dbAbsUncorrectlyClassified) {
		this.dbAbsUncorrectlyClassified = dbAbsUncorrectlyClassified;
	}

	/**
	 * @return the dbPerUncorrectlyClassified
	 */
	public double getDbPerUncorrectlyClassified() {
		return dbPerUncorrectlyClassified;
	}

	/**
	 * @param dbPerUncorrectlyClassified the dbPerUncorrectlyClassified to set
	 */
	public void setDbPerUncorrectlyClassified(double dbPerUncorrectlyClassified) {
		this.dbPerUncorrectlyClassified = dbPerUncorrectlyClassified;
	}



}
