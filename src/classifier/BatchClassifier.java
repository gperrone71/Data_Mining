/**
 * 
 */
package classifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.time.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicTableHeaderUI;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;

import dataset.GenerateDataSet;
import launchers.BatchLauncher;
import objects.*;
import problem.Solver1;

import utils.PerroUtils;

import utils.XMLUtils;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * Class implementing a batch series of training - evaluation jobs performed following configurations described in xml config file
 * 
 * @author gperr
 *
 */
public class BatchClassifier {

	private ArrayList<BatchConfig> lstConfigObj = new ArrayList<BatchConfig>();
	private String strPath;
	
	ArrayList<ClassifierStats> lstClassifierStats = new ArrayList<ClassifierStats>();			// list used to store the results of the solver's execution

	/**
	 * Parses an XML file containing a batch job and populates the list of jobs.
	 * 
	 * @param strConfigFileName	Name of the xml file that includes the list of the jobs to be added to the batch
	 * 
	 */
	public boolean NewBatchJob(String strConfigFileName) {

		strPath = BatchLauncher.returnBatchNameFromFileName(strConfigFileName);
		
		// List used to parse the xml file with the configuration items 
		List<String> lstString = new ArrayList<String>();
		
		// generates a new xml stream
		XStream xstream = new XStream();

		// security permissions for XStream
		
		// clear out existing permissions and set own ones
		xstream.addPermission(NoTypePermission.NONE);
		xstream.allowTypeHierarchy(Task.class);
		xstream.allowTypeHierarchy(Node.class);
		xstream.allowTypeHierarchy(Resource.class);
		xstream.allowTypeHierarchy(BatchConfig.class);

		// all lines from files are read and put in an arraylist
		lstString = PerroUtils.getFileToList("resources/" + strConfigFileName);
		// exit if something went wrong
		if (lstString == null)
			return false;

		// Read the xml configuration file and populates the list of the jobs to be executed
		PerroUtils.print("-BATCH LOADER for classifier-----------------------------------------------------");
		PerroUtils.print(" Config file   : " + strConfigFileName);
	
		int iStartRow = 0;
		String str = "";
		
		do {
			str = XMLUtils.returnNextXMLObject(lstString, BatchConfig.class, iStartRow);
//			PerroUtils.print(str);
			iStartRow = XMLUtils.getiEndRow();
			if (str != "") {
				// creates a new object and add it to the list		
				BatchConfig tmpCObj = (BatchConfig)xstream.fromXML(str);
				// e lo aggiungo alla lista
				lstConfigObj.add(tmpCObj);
			}
				
		} while ( (str != "") && (iStartRow < lstString.size()) ); 
			
		PerroUtils.print(" Loaded " + lstConfigObj.size() + " configuration items");
		PerroUtils.print("-** END of BATCH LOADING --------------------------------------------------------");
				
		return true;
	}
	
	/**
	 * Executes the batch classification inclusive of cross-validation, that is:
	 * 1) starts setting the first dataset as to be used for evaluation and all the others for training
	 * 2) reads accordingly the instances
	 * 3) generates a model and train it on the loaded instances
	 * 4) loads the dataset to be used as test and evaluates performances of the classifier built so far on the evaluation dataset
	 * 5) generates the stats and iterates until the all the datasets have been used for evaluation
	 *  	
	 */
	public void executeBatchClassifier() {

		List<Task> lstTasks = new ArrayList<Task>();
		List<Resource> lstResources = new ArrayList<Resource>();
		
		PerroUtils.print("\n-BATCH CLASSIFIER----------------------------------------------------------------");
		PerroUtils.print("\nStarting training phase.");

		// initial setup:
		// clear the training test flag on all batch config datasets
		for (BatchConfig tmpObj : lstConfigObj)
			tmpObj.setbGenerateTestSet(false);		
		
	    try {

	    	String strPath = "";
	    	
	    	for (BatchConfig objForCrossValidation : lstConfigObj) {
	    		// outer loop:
	    		// select the evaluation test set for the object currently loaded
	    		objForCrossValidation.setbGenerateTestSet(true);

	    		// init variables
	    		List<Task> lstPrunedTasks = new ArrayList<Task>();
	    		List<Resource> lstPrunedResources = new ArrayList<Resource>();

	    		// generate a new AttributeSelectedClassifier
			    AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
			    			    
			    // uses the first dataset to build the structure for the object that will contain the various instances
			    BatchConfig tmp = lstConfigObj.get(0);	// takes the first dataset
		    	ArffLoader initLoader = new ArffLoader();
		    	String strFirstDataSet = generateARFFFileName(tmp, lstConfigObj);
		    	
		    	PerroUtils.print(" Using dataset #0 for structure generation ("+ strFirstDataSet + ")");
				initLoader.setFile(new File(strFirstDataSet));
//			    Instances dataTrain = initLoader.getDataSet();
				Instances dataTrain = initLoader.getStructure();
				dataTrain.setClassIndex(dataTrain.numAttributes() - 1);	
		
			    String fileNameTestSet = "";
			    boolean bTestSetResourcesReturnToOrigin = false;
			    double maxX = 0;	    
			    double maxY = 0;
			    
				// main loop for training phase 		
				for (int i = 0; i < lstConfigObj.size(); i++) {
					
					BatchConfig batchObj = lstConfigObj.get(i);
					
					int iBatchObj = lstConfigObj.indexOf(batchObj);
	
					// generates the file name of the arff corresponding to the job
					String strDataSetFileName = generateARFFFileName(batchObj, lstConfigObj);			
		
					if (!batchObj.isbGenerateTestSet()) {			// skip if the dataset has to be used for test and evaluation
						
						PerroUtils.print(" Loading dataset for job #" + iBatchObj + " - " + strDataSetFileName);
			
				    	// loads the arff file corresponding to the current job
				    	ArffLoader loader = new ArffLoader();    	
						loader.setFile(new File(strDataSetFileName));
							
						// otherwise I am adding the instances to the original dataset
						PerroUtils.print(" +--- Adding instances...");
						Instances tmpInst = loader.getDataSet();
						tmpInst.setClassIndex(tmpInst.numAttributes()-1);
						dataTrain.addAll(tmpInst);
					} 
					else {
						fileNameTestSet = strDataSetFileName;
						bTestSetResourcesReturnToOrigin = batchObj.isbResReturnToStart();
						maxX = batchObj.getMaxX();
						maxY = batchObj.getMaxY();				
					}
				}
				
				PerroUtils.print(" Finished loading datasets - added " + dataTrain.numInstances() + " instances.");
				
				// end of main loop: I have all instances but the test set loaded and can build the classifier
				classifier.buildClassifier(dataTrain);
			
				// loads the test set
				PerroUtils.print("Loading " + fileNameTestSet);
				ArffLoader testLoader = new ArffLoader();
				testLoader.setFile(new File(fileNameTestSet));
				Instances testSet = testLoader.getDataSet();
				testSet.setClassIndex(testSet.numAttributes() - 1);
				
				// start evaluation
				Evaluation eval = new Evaluation(dataTrain);
				eval.evaluateModel(classifier, testSet);
				System.out.println(eval.toSummaryString("\nResults\n======\n", false));
						
	
				// Print some statistics on evaluation
				for (int i = 0; i <= 1; i++) {
					PerroUtils.print("Class " + i +": #of True Positives "+eval.numTruePositives(i) + " # of False Positives " + eval.numFalsePositives(i));
				}
				
				double confMatr[][] = eval.confusionMatrix();
				
				PerroUtils.print("\nConfusion matrix\n----------------");
				PerroUtils.print("           0     1");
				for (int i = 0; i <= 1; i++) {
					System.out.print("Class " + i + " : ");
					for (int j = 0; j <= 1; j++)
						System.out.print("["+ confMatr[i][j] + "]");
					System.out.println("");
				}
				
				PerroUtils.print("\n" + eval.toClassDetailsString());
	
				
				// now with the trained model I generate a new dataset for the test set to be solved separately
				// first of all I will load the non-classified version of the test set

//		    	String strEmptyDSName = returnFullFileNameWOExtension(fileNameTestSet) + "_TS.arff";
		    	String strEmptyDSName = returnFullFileNameWOExtension(fileNameTestSet) + ".arff";
				PerroUtils.print("*** Loading empty test set with filename :" + strEmptyDSName, true);
		    	
				ArffLoader emptyDSLoader = new ArffLoader();
		    	emptyDSLoader.setFile(new File(strEmptyDSName));
		    	Instances unlabeledTS = emptyDSLoader.getDataSet();
				unlabeledTS.setClassIndex(unlabeledTS.numAttributes() - 1);
			
				
		    	// then I have to load in memory the dataset in xml format corresponding to the test set
		    	// first of all I have to extract paths and filenames of the xml dataset
		    	String strFullName = fileNameTestSet.substring(0,  fileNameTestSet.indexOf("_stats")) + ".xml";
		    	strPath = strFullName.substring(0, strFullName.lastIndexOf('/') + 1);
		    	String strXMLFileName = strFullName.substring(strFullName.lastIndexOf('/') + 1);
		    	
		    	// finally I can load the xml file and populate the lists
				Solver1 problemSolver = new Solver1(strPath, strXMLFileName);
				lstTasks = problemSolver.getLstTasks();
				lstResources = problemSolver.getLstResources();
	
				// and using the strings generated so far save on disk stats for the generated classifier
				Charset chEnc = Charset.forName("utf-8");
				File tmpFile = new File(strPath + "model_stats_" + lstConfigObj.indexOf(objForCrossValidation) + ".txt");
				FileUtils.writeStringToFile(tmpFile, eval.toSummaryString(), chEnc);
				FileUtils.writeStringToFile(tmpFile, eval.toClassDetailsString("\nClass Details\n"), chEnc, true);		
	
				// copy the resources in the pruned version (it won't change)
				lstPrunedResources = lstResources;
	
				// populate the pruned tasks only with the tasks that are classified as schedulable
			    for (int i = 0; i < unlabeledTS.numInstances(); i++) {
			    	double clsLabel = classifier.classifyInstance(unlabeledTS.instance(i));
			    	if (clsLabel == 1)
			    		lstPrunedTasks.add(lstTasks.get(i));	    		
			    }
			    
			    PerroUtils.print("*** Created pruned dataset with " + lstPrunedTasks.size() + " tasks.", true);
			    
			    // write the XML file on disk
			    GenerateDataSet tmpGDS = new GenerateDataSet();
			    String strPrunedXMLFileName = tmpGDS.WriteDataSetOnFile(lstPrunedTasks, lstPrunedResources, strPath , "_PRUNED_" + lstConfigObj.indexOf(objForCrossValidation));
			    
			    // generate a temp ClassifierStats object and starts populating it
			    ClassifierStats tmpClassStat = new ClassifierStats();
			    tmpClassStat.setNumResources(lstResources.size());
			    tmpClassStat.setDbMaxX(maxX);
				tmpClassStat.setDbMaxY(maxY);
				tmpClassStat.setiNumThreads(24);

				// launch the solver on the original problem with the specified number of threads and stores the results			
			    SolStats tmpSolStat = new SolStats();		    
			    tmpSolStat = problemSolver.launchSolver(false, bTestSetResourcesReturnToOrigin, 24, strPath);

			    // generate the information on timestamp and hash
			    tmpClassStat.setStrFullTimeStamp(new SimpleDateFormat("dd/MM/yyyy HH.mm.ss").format(new Date()));
			    tmpClassStat.setStrTimeStampDay(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			    
			    // store the file name of the instance
			    tmpClassStat.setStrInstanceName(strPath + strXMLFileName);
			    
			    // generate the hash for this instance and save it to the statistical object
			    tmpClassStat.setStrHash(PerroUtils.CRC32Calc(strPath + strXMLFileName));
			    
			    // and copies the relevant information in the ClassifierStats object
				tmpClassStat.setNumTasks_UP(lstTasks.size());
				tmpClassStat.setDbTasksDensity_UP(lstTasks.size() / (maxX * maxY));
				tmpClassStat.setNumSolutionsFound_UP(tmpSolStat.getNumSolutionsFound());
				tmpClassStat.setDblExecutionTime_UP(tmpSolStat.getDblExecutionTime());
				tmpClassStat.setiTotServiced_UP(tmpSolStat.getiTotServices());
				tmpClassStat.setDbTraveledDistance_UP(tmpSolStat.getDbTraveledDistance());
				tmpClassStat.setDbCompletionTime_UP(tmpSolStat.getDbCompletionTime());
				tmpClassStat.setDbWaitingTime_UP(tmpSolStat.getDbWaitingTime());
				tmpClassStat.setDbTotalCosts_UP(tmpSolStat.getDbTotalCosts());
				
				
				// generates another solver object using the pruned dataset 
				Solver1 prunedProblemSolver = new Solver1(strPath, strPrunedXMLFileName);
				
				// launch the solver on the pruned problem with 7 threads and stores the results
			    tmpSolStat = prunedProblemSolver.launchSolver(false, bTestSetResourcesReturnToOrigin, 24, strPath);

			    // and copies the relevant information in the ClassifierStats object in the section for the pruned dataset
				tmpClassStat.setNumTasks_P(lstPrunedTasks.size());
				tmpClassStat.setDbTasksDensity_P(lstPrunedTasks.size() / (maxX * maxY));
				tmpClassStat.setNumSolutionsFound_P(tmpSolStat.getNumSolutionsFound());
				tmpClassStat.setDblExecutionTime_P(tmpSolStat.getDblExecutionTime());
				tmpClassStat.setiTotServiced_P(tmpSolStat.getiTotServices());
				tmpClassStat.setDbTraveledDistance_P(tmpSolStat.getDbTraveledDistance());
				tmpClassStat.setDbCompletionTime_P(tmpSolStat.getDbCompletionTime());
				tmpClassStat.setDbWaitingTime_P(tmpSolStat.getDbWaitingTime());
				tmpClassStat.setDbTotalCosts_P(tmpSolStat.getDbTotalCosts());

			    // add the stats and information on the model
				tmpClassStat.setDbPrecision(eval.weightedPrecision());
				tmpClassStat.setDbRecall(eval.weightedRecall());
				tmpClassStat.setDbAbsCorrectlyClassified(eval.correct());
				tmpClassStat.setDbPerCorrectlyClassified(eval.pctCorrect());
				tmpClassStat.setDbAbsUncorrectlyClassified(eval.incorrect());
				tmpClassStat.setDbPerUncorrectlyClassified(eval.pctIncorrect());
				
				// add the stats on the differences between the two executions
				double dbExecTimDiff = tmpClassStat.getDblExecutionTime_P()-tmpClassStat.getDblExecutionTime_UP();
				tmpClassStat.setDbAbsExecTimeDiff(dbExecTimDiff);
				tmpClassStat.setDbPerExecTimeDiff(dbExecTimDiff/tmpClassStat.getDblExecutionTime_UP());
				
				double dbServicedDiff = tmpClassStat.getiTotServiced_P()-tmpClassStat.getiTotServiced_UP();
				tmpClassStat.setDbAbsSrvcdTasksDiff(dbServicedDiff);
				tmpClassStat.setDbPerSrvcdTasksDiff(dbServicedDiff/tmpClassStat.getiTotServiced_UP());
				
				// add the temp object to the list
				lstClassifierStats.add(tmpClassStat);

	    		// de-select the evaluation test set for the object currently loaded dataset (there must be only one dataset set for evaluation per loop iteration)
	    		objForCrossValidation.setbGenerateTestSet(false);

	    	}			

			// writes the stats to disk
			classifierStatsToCSV(false, strPath, "full", lstClassifierStats);

/*	
			 String[] options = new String[2];
			 options[0] = "-t";
			 options[1] = fileNameTestSet;
			 System.out.println(Evaluation.evaluateModel(classifier, options));
			 
			for (String str : eval.getMetricsToDisplay()) {
				PerroUtils.print(str);
			}
*/			
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					    
	}
		

	/**
	 * Generates the file name of the ARFF file corresponding to the batch object being read
	 *  
	 * @param bcObj		the current batch object
	 * @param lstObj	the list containing all the batch objects (to retrieve the index)
	 * @return			the string containing the full path and filename of the ARFF file
	 */
	private String generateARFFFileName(BatchConfig bcObj, ArrayList<BatchConfig> lstObj) {
		return strPath + "/DS_"+ bcObj.getnTasks() +"_"+ bcObj.getnResources() +"_batch"+lstObj.indexOf(bcObj)+"_stats.arff";

	}
	
	/**
	 * Returns a string with the full path and file name without the extension - to be used to generate arff or xml file names
	 * 
	 * @param str String containing the full path and filename
	 * @return String string without extension
	 */
	private String returnFullFileNameWOExtension (String str) {
		return str.substring(0, str.indexOf('.'));
	}
	
	/**
	 * Generates a CSV file from the private SolStats list (containing statistics for solver's executions) and optionally prints on console its contents 
	 * 
	 *  @param boolean prtOnScreen specifies if the CSV output has to be printed on console or not
	 *  @param strFullPath full path (inclusive of final "/") where the csv file has to be written
	 *  @param strNameOfBatch name of the back configuration file for which the statistics have been generated
	 */
	private static void classifierStatsToCSV(boolean prtOnScreen, String strFullPath, String strNameOfBatch, ArrayList<ClassifierStats> lstInternalClassStatList) {
		
		List<String> strList = new ArrayList<String>();
		
		// temp stats object
		ClassifierStats tmp = lstInternalClassStatList.get(0);		// take the first object of the list in order to be sure that all fields are populated

		if (prtOnScreen)
			PerroUtils.print(tmp.getHeaderString());
		
		strList.add(tmp.getHeaderString());
		
		String strFullFileName = strFullPath+strNameOfBatch+"_stats.csv";
		
		if (prtOnScreen)
			PerroUtils.print("Writing to file: "+ strFullFileName);
		for (ClassifierStats tmp1 : lstInternalClassStatList) {
			if (prtOnScreen) 
				PerroUtils.print(tmp1.toString());
			strList.add(tmp1.toString());
		}
		
		PerroUtils.writeCSV(strFullFileName, strList);
	}

	
	/**
	 * @return the lstClassifierStats
	 */
	public ArrayList<ClassifierStats> getLstClassifierStats() {
		return lstClassifierStats;
	}

	/**
	 * @param lstClassifierStats the lstClassifierStats to set
	 */
	public void setLstClassifierStats(ArrayList<ClassifierStats> lstClassifierStats) {
		this.lstClassifierStats = lstClassifierStats;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<ClassifierStats> lstFullStats = new ArrayList<ClassifierStats>();
/*		
		BatchClassifier tmp = new BatchClassifier();	
		tmp.NewBatchJob("batch/Batch10_100_d01.xml");		//loads the jobs configuration
		tmp.executeBatchClassifier();				// launches the classifier
		lstFullStats.addAll(tmp.getLstClassifierStats());
		
		BatchClassifier tmp1 = new BatchClassifier();	
		tmp1.NewBatchJob("batch/Batch10_100_d001.xml");		//loads the jobs configuration
		tmp1.executeBatchClassifier();				// launches the classifier
		lstFullStats.addAll(tmp1.getLstClassifierStats());

		BatchClassifier tmp2 = new BatchClassifier();	
		tmp2.NewBatchJob("batch/Batch10_100_d005.xml");		//loads the jobs configuration
		tmp2.executeBatchClassifier();				// launches the classifier
		lstFullStats.addAll(tmp2.getLstClassifierStats());
*/
		
		// attempt to launch a batch for all files in the "/batch" directory
		final File filObj = new File("resources/batch");
		PerroUtils.print(filObj.getPath());
		
		final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("XML", "xml");

		// print list of batch jobs
		for (final File fileInDir : filObj.listFiles()) {
			if ( extensionFilter.accept(fileInDir) ) 
				if (!fileInDir.isDirectory()) 
					PerroUtils.print(fileInDir.getName());
		}

		// starts the main loop
		for (final File fileInDir : filObj.listFiles()) {
			if ( extensionFilter.accept(fileInDir) ) 
				if (!fileInDir.isDirectory()) {
					BatchClassifier tmp = new BatchClassifier();
					String strBatchName = "batch/"+ fileInDir.getName();
					PerroUtils.print("Launching batch job " + strBatchName);
					tmp.NewBatchJob(strBatchName);		//loads the jobs configuration
					tmp.executeBatchClassifier();				// launches the classifier
					lstFullStats.addAll(tmp.getLstClassifierStats());
				}
		}
		classifierStatsToCSV(false, "output/", "all_", lstFullStats);


		/*
		BatchClassifier tmp = new BatchClassifier();	
		tmp.NewBatchJob("batch/Batch100_1000_d01.xml");		//loads the jobs configuration
		tmp.executeBatchClassifier();				// launches the classifier
		
		BatchClassifier tmp1 = new BatchClassifier();	
		tmp1.NewBatchJob("batch/Batch100_1000_d001.xml");		//loads the jobs configuration
		tmp1.executeBatchClassifier();				// launches the classifier

		BatchClassifier tmp2 = new BatchClassifier();	
		tmp2.NewBatchJob("batch/Batch100_1000_d005.xml");		//loads the jobs configuration
		tmp2.executeBatchClassifier();				// launches the classifier
*/
	}


}
