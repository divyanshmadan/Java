import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/* 
 * Data Mining and Bioinformatics Homework2
 * Team Members: Anup Shahi, Divyansh Madan, Jayanti Dabhere
 * Problem Statement: Calculating Frequent item sets by Apriori Algorithm and Filter the rules based on given templates.  
 * File: Data Manager for the data set obtained from text file.
 * 
 */ 
public class DataManager {
   String fileName;
   int totalSamples;
   HashMap<String,Integer> samplesMap;	
	DataManager(String file_name){		
		fileName=file_name;
		samplesMap=new HashMap<String,Integer>();
	}
	
	
	//This function would read the file, labeling the corresponding elements and calculating the frequency
	public HashMap<String,Integer> getAllItemsWithFrequency() throws IOException {
		
		HashMap<String,Integer> itemsWithFrequency =new HashMap<String,Integer>();
		int number_of_samples=0;
		BufferedReader Test = new BufferedReader(new FileReader(fileName));
		
		while(Test.readLine() != null)
			number_of_samples++;
		
		totalSamples=number_of_samples;
		BufferedReader inFile = new BufferedReader(new FileReader(fileName));
		
		int line_counter=0, column_label,test=0;
		String current_line;
		while((current_line=inFile.readLine()) != null){
			line_counter++;
					
			StringTokenizer items = new StringTokenizer(current_line, "\t");
			items.nextToken();			
			column_label=0;
            //For each item, enter the item and frequency in the hash map
			while(items.hasMoreTokens()){
				
				column_label++;
				int old_value, new_value;
				String current_item=items.nextToken();				
				current_item=current_item.trim();
				if(current_item.equalsIgnoreCase("up")){					
					samplesMap.put("Item"+column_label+"up"+line_counter,line_counter);
					
					if(itemsWithFrequency.get("Item"+column_label+"up")==null)
					itemsWithFrequency.put("Item"+column_label+"up",1);
					else{
						old_value=itemsWithFrequency.get("Item"+column_label+"up");
						new_value=old_value+1;
						itemsWithFrequency.put("Item"+column_label+"up",new_value);
					}
				}
				else if(current_item.equalsIgnoreCase("down")){
					
					samplesMap.put("Item"+column_label+"down"+line_counter,line_counter);
					
					if(itemsWithFrequency.get("Item"+column_label+"down")==null)
					itemsWithFrequency.put("Item"+column_label+"down",1);
					else{
							old_value=itemsWithFrequency.get("Item"+column_label+"down");
							new_value=old_value+1;
							itemsWithFrequency.put("Item"+column_label+"down",new_value);
					}
				}
				else{
					samplesMap.put(current_item+line_counter,line_counter);
				}
			   
			}
			
		}
					
		//System.out.println( "Count: "+test);
		//Printing Frequent Item sets
		/*for (String key : samplesMap.keySet() ) {
			// Get the String value that goes with the key
			int value = samplesMap.get( key );
			 				 
			System.out.println( key + " = " + value);
		}*/
		
		
		
		return itemsWithFrequency;
		
	}
	
}
