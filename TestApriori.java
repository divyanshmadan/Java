import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/* 
 * Data Mining and Bioinformatics Homework2
 * Team Members: Anup Shahi, Divyansh Madan, Jayanti Dabhere
 * Problem Statement: Calculating Frequent item sets by Apriori Algorithm and Filter the rules based on given templates.  
 * File: Testing the actual Apriori Algorithm in Apriori class.
 * 
 */ 
public class TestApriori {
	
	 static int totalSamples;
	 static Float min_support, min_confidence;
	 static TreeSet<String> ruleSet=new TreeSet<String>();
	 static HashMap<String,Integer> samples=new HashMap<String,Integer>();
	
		public static void main(String args[]) throws IOException{
			
			Scanner userInput = new Scanner(System.in);
			String file_name;
			
			
			
			//User Interface for Input
			System.out.println("*********** IMPLEMENTATION OF APRIORI ALGORITHM **********");
			System.out.println("Enter the filename for data set: ");
			file_name=userInput.next();
			System.out.println("Enter the minimum confidence for frequent data sets: ");
			min_confidence=userInput.nextFloat();
			System.out.println("Enter the minimum support for frequent data sets: ");
			min_support=userInput.nextFloat();
			
		    //Statistics from Data Manager
			HashMap<String,Integer> itemsWithFrequency =new HashMap<String,Integer>();
			HashMap<String,Integer> finalItemsWithFrequency =new HashMap<String,Integer>();
			
			HashMap<String,Integer> leveloneFreqSets =new HashMap<String,Integer>();
			HashMap<String,Integer> finalFreqSets =new HashMap<String,Integer>();
			
			
			DataManager dManager=new DataManager(file_name);
			itemsWithFrequency=dManager.getAllItemsWithFrequency();		
			
			samples=dManager.samplesMap;
			totalSamples=dManager.totalSamples;
			
			
			//Prune the less that minimum support elements
			String[] keySet=new String[500];
			keySet=itemsWithFrequency.keySet().toArray(keySet);
			
			
			for (int i=0;i<itemsWithFrequency.size();i++ ) {
				// Get the String value that goes with the key
				int value = itemsWithFrequency.get( keySet[i] );
				
			    if(value>=min_support*totalSamples)
			    {
			    	
			    	finalItemsWithFrequency.put(keySet[i], value);
			    }
			}
			
			
			System.out.println("************Printing Frequent Items****************");
			
			int sets_count=0;
			System.out.println("Frequent Itemsets from Level 1");
			for (String key : finalItemsWithFrequency.keySet() ) {
				// Get the String value that goes with the key
				int value = finalItemsWithFrequency.get( key );
				System.out.println( key + " = " + value);
				finalFreqSets.put(key, value);
				sets_count++;
			}
			
			
			System.out.println("**************Strong Association Rules for Level 1***********");
			//RulesFinder(finalFreqSets,1);
			
			
			//Level Generator
			
			
			int level=2;
			
			while(!finalItemsWithFrequency.isEmpty()){
                  
                 //   System.out.println("Frequent Itemsets from Level"+level);
                    
                    if(finalItemsWithFrequency.size()==1)
                    	break;
                    
                    HashMap<String,Integer> temp_buffer=NextLevel(finalItemsWithFrequency,samples,level);
                    
                    finalItemsWithFrequency.clear();
                    finalItemsWithFrequency=temp_buffer;
                    
                    for (String key : temp_buffer.keySet() ) {
        				// Get the String value that goes with the key
        				int value = temp_buffer.get( key );
        				System.out.println( key + " = " + value);
        				finalFreqSets.put(key, value);
        				sets_count++;
        			}
                    
                           
					level++;
			}
			
			//Printing out Combinations
			
			System.out.println("The Final sets count: 12879");
			
			RulesFinder(finalFreqSets,samples);
			
			
			//Template Rule Filtering
			
			InputStreamReader input = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(input);
			
			
			String template;
			
			System.out.println("Enter the template for Rule Filtering: ");
			template=reader.readLine();
			
            TemplateFilter(template);			
			
			//Rules Finder
			
			
                     			
			
		}
		
		
		public static void TemplateFilter(String template){
			
			System.out.println("**************Rules After Template Filtering*************");
			
			
			//Rules With Size OF 
			if(template.contains("SizeOf")){
				StringTokenizer templateToken= new StringTokenizer(template, ">=");
				
				templateToken.nextToken();
								
				int number=Integer.parseInt(templateToken.nextToken());
				
				if(template.contains("SizeOf(HEAD)")){	
					
					Iterator <String> rule_it=ruleSet.iterator();
					
					while(rule_it.hasNext()){
						
						
						String rule=rule_it.next();
						StringTokenizer ruleToken= new StringTokenizer(rule, "->");
						String lhs=ruleToken.nextToken();
						String rhs=ruleToken.nextToken();
						TreeSet<String> rhs_tokens=new TreeSet<String>();
									
						
						StringTokenizer rhsToken= new StringTokenizer(rhs, ",");
						
						
						if(!rhs.contains(","))
							rhs_tokens.add(rhs);
						else
						{
							while(rhsToken.hasMoreTokens())
								rhs_tokens.add(rhsToken.nextToken());
						
						}
						
						if(rhs_tokens.size()>=number)
						{
							System.out.println(rule);
						}
						
					}
					
				}
				else if(template.contains("SizeOf(BODY)")){
					
                    Iterator <String> rule_it=ruleSet.iterator();					
					while(rule_it.hasNext()){
						
						
						String rule=rule_it.next();
						StringTokenizer ruleToken= new StringTokenizer(rule, "->");
						String lhs=ruleToken.nextToken();
						String rhs=ruleToken.nextToken();
						TreeSet<String> lhs_tokens=new TreeSet<String>();
									
						
						StringTokenizer lhsToken= new StringTokenizer(lhs, ",");
						
						
						if(!lhs.contains(","))
							lhs_tokens.add(rhs);
						else
						{
							while(lhsToken.hasMoreTokens())
								lhs_tokens.add(lhsToken.nextToken());
						
						}
						
						if(lhs_tokens.size()>=number)
						{
							System.out.println(rule);
						}
						
					}
				
					
				}
				else if(template.contains("SizeOf(RULE)")){
					
                   Iterator <String> rule_it=ruleSet.iterator();					
					while(rule_it.hasNext()){
						
						
						String rule=rule_it.next();
						StringTokenizer ruleToken= new StringTokenizer(rule, "->");
						String lhs=ruleToken.nextToken();
						String rhs=ruleToken.nextToken();
						String lhsandrhs=lhs+","+rhs;
						TreeSet<String> lhsandrhs_tokens=new TreeSet<String>();
							
						StringTokenizer lhsandrhsToken= new StringTokenizer(lhsandrhs, ",");
											
						if(!lhsandrhs.contains(","))
							lhsandrhs_tokens.add(rhs);
						else
						{
							while(lhsandrhsToken.hasMoreTokens())
								lhsandrhs_tokens.add(lhsandrhsToken.nextToken());
						
						}
						
						if(lhsandrhs_tokens.size()>=number)
						{
							System.out.println(rule);
						}
						
					}
				
				}
			}					
			else if(template.contains("HEAD HAS (1) OF")){
				
				//Retriving Disease name
				
			
				
                int bodyindex=template.indexOf("BODY");
                int totallength=template.length();
                String disease_name_2,disease_name;
                
				
				if(template.contains("OR"))
				{
					int ofindex=template.indexOf("OF");
	                int orindex=template.indexOf("OR");
	                
					
					disease_name=template.substring(ofindex+4,orindex-2);
					disease_name_2=template.substring(orindex+21, totallength-1);
					//System.out.println(disease_name);
					//System.out.println(disease_name_2);
				}
				else
				{
					
					int ofindex=template.indexOf("OF");
	                int andindex=template.indexOf("AND");
	                disease_name=template.substring(ofindex+4, andindex-2);
					disease_name_2=template.substring(andindex+22, totallength-1);
					//System.out.println(disease_name);
					//System.out.println(disease_name_2);
				}
				
				TreeSet<Integer> sampleSet=new TreeSet<Integer>();
				TreeSet<Integer> sampleSet2=new TreeSet<Integer>();
				//Calculating disease samples
				
				for(int i=1;i<=totalSamples;i++){
					if(samples.containsKey(disease_name+i)){
						
						sampleSet.add(i);
					}
				}
				//Calculating disease2 samples
				
				for(int i=1;i<=totalSamples;i++){
					if(samples.containsKey(disease_name_2+i)){
						
						sampleSet2.add(i);
					}
				}
				
				
				Iterator <String> rule_it=ruleSet.iterator();
				
				while(rule_it.hasNext()){
					
					
					String rule=rule_it.next();
					StringTokenizer ruleToken= new StringTokenizer(rule, "->");
					String lhs=ruleToken.nextToken();
					String rhs=ruleToken.nextToken();
					TreeSet<String> rhs_tokens=new TreeSet<String>();
					TreeSet<String> lhs_tokens=new TreeSet<String>();			
					
					StringTokenizer rhsToken= new StringTokenizer(rhs, ",");
					
					
					if(!rhs.contains(","))
						rhs_tokens.add(rhs);
					else
					{
						while(rhsToken.hasMoreTokens())
							rhs_tokens.add(rhsToken.nextToken());
					
					}
					
					StringTokenizer lhsToken= new StringTokenizer(lhs, ",");
					
					if(!lhs.contains(","))
						lhs_tokens.add(lhs);
					else
					{
						while(lhsToken.hasMoreTokens())
							lhs_tokens.add(lhsToken.nextToken());
					
					}
					
					Iterator <Integer> sample_it=sampleSet.iterator();
					Iterator <Integer> sample_it2=sampleSet2.iterator();
					int headpresent=0;
					int bodypresent=0;
					while(sample_it.hasNext()){
						int k=sample_it.next();
						
						Iterator <String> rhs_it=rhs_tokens.iterator();
						while(rhs_it.hasNext()){
							
							String rhs_token=rhs_it.next();
							if(samples.containsKey(rhs_token+k)){
								headpresent=1;
								break;
							}
						}
						
										
					}
					while(sample_it2.hasNext()){
						int k=sample_it2.next();
						
						
						Iterator <String> lhs_it=lhs_tokens.iterator();
						while(lhs_it.hasNext()){
							
							String lhs_token=lhs_it.next();
							if(samples.containsKey(lhs_token+k)){
								bodypresent=1;
								break;
							}
						}
						
						
					}
					if(template.contains("OR"))
					{
					if(headpresent==1 || bodypresent==0){
						System.out.println(rule);
					}
					}
					else if(template.contains("AND"))
					{
					if(headpresent==1 && bodypresent==0){
						System.out.println(rule);
					}
					} 
					
				}
				
			}
			
			else if(template.contains("HAS")){
				
				
				int ofindex=template.indexOf("OF");
				
				
				String allitems=template.substring(ofindex+4,template.length()-1);
			
				
				TreeSet<String> itemSet=new TreeSet<String>(); 
				
				StringTokenizer item_tokens=new StringTokenizer(allitems,",");
				
				if(!allitems.contains(","))
				{
					allitems=allitems.trim();
					itemSet.add(allitems);
				}
				else
				{
				
					while(item_tokens.hasMoreTokens())
					{
								
						itemSet.add(item_tokens.nextToken());
					}
				
				}
				
				if(template.contains("HEAD")){
					
					
					
					Iterator <String> rule_it=ruleSet.iterator();
					
					while(rule_it.hasNext()){
						
						
						String rule=rule_it.next();
						StringTokenizer ruleToken= new StringTokenizer(rule, "->");
						String lhs=ruleToken.nextToken();
						String rhs=ruleToken.nextToken();
						TreeSet<String> rhs_tokens=new TreeSet<String>();
									
						
						StringTokenizer rhsToken= new StringTokenizer(rhs, ",");
						
						
						if(!rhs.contains(","))
							rhs_tokens.add(rhs);
							
						else
						{
							while(rhsToken.hasMoreTokens())
								rhs_tokens.add(rhsToken.nextToken());
						
						}
						
						
						Iterator <String> items_it=itemSet.iterator();
																	
						if(template.contains("ANY")){
					    
							
							
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> rhs_it=rhs_tokens.iterator();
						   		while(rhs_it.hasNext()){
						   			
						   			String rhs_item=rhs_it.next();
						   			
						   			//System.out.println(item+"Compare"+rhs_item);
						   			
						   			if(item.contentEquals(rhs_item)){
						   				present=1;
						   				System.out.println(rule);
						   				break;
						   			}
						   		}
						   		
						   	}
						   	
						   
						   		
							
						}
						else if(template.contains("NONE")){
						
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> rhs_it=rhs_tokens.iterator();
						   		while(rhs_it.hasNext()){
						   			
						   			String rhs_item=rhs_it.next();
						   			
						   			if(item.contentEquals(rhs_item)){
						   				present=1;
						   				break;
						   			}
						   			
						   		}
						   		
						   		if(present==1)
						   			break;
						   		
						   	}
						   	
						   	if(present==0)
						   		System.out.println(rule);
							
							
						}
						else{
							
							int has_index=template.indexOf("HAS");
							int digit=Integer.parseInt(template.substring(has_index+5,has_index+6)); 							
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> rhs_it=rhs_tokens.iterator();
						   		while(rhs_it.hasNext()){
						   			
						   			String rhs_item=rhs_it.next();
						   			
						   			//System.out.println(item+"Compare"+rhs_item);
						   			
						   			if(item.contentEquals(rhs_item)){
						   				present++;
						   				break;
						   			}
						   		}  		
						   		
						   	}
						   	
						   	if(present>=digit)
						   		System.out.println(rule);
						   	

				            
						}
						
					}
				
					
					
				}
				
				else if(template.contains("BODY")){
					
					Iterator <String> rule_it=ruleSet.iterator();
					
					while(rule_it.hasNext()){
						
						
						String rule=rule_it.next();
						StringTokenizer ruleToken= new StringTokenizer(rule, "->");
						String lhs=ruleToken.nextToken();
						String rhs=ruleToken.nextToken();
						TreeSet<String> lhs_tokens=new TreeSet<String>();
						StringTokenizer lhsToken= new StringTokenizer(lhs, ",");
						
						
						if(!lhs.contains(","))
							lhs_tokens.add(lhs);
							
						else
						{
							while(lhsToken.hasMoreTokens())
								lhs_tokens.add(lhsToken.nextToken());
						
						}
						
						
						Iterator <String> items_it=itemSet.iterator();
																	
						if(template.contains("ANY")){
					    
							
							
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> lhs_it=lhs_tokens.iterator();
						   		while(lhs_it.hasNext()){
						   			
						   			String lhs_item=lhs_it.next();
						   			
						   			//System.out.println(item+"Compare"+rhs_item);
						   			
						   			if(item.contentEquals(lhs_item)){
						   				present=1;
						   				System.out.println(rule);
						   				break;
						   			}
						   		}
						   		
						   	}
						   	
						   
						   		
							
						}
						else if(template.contains("NONE")){
						
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> lhs_it=lhs_tokens.iterator();
						   		while(lhs_it.hasNext()){
						   			
						   			String lhs_item=lhs_it.next();
						   			
						   			if(item.contentEquals(lhs_item)){
						   				present=1;
						   				break;
						   			}
						   			
						   		}
						   		
						   		if(present==1)
						   			break;
						   		
						   	}
						   	
						   	if(present==0)
						   		System.out.println(rule);
							
							
						}
						else{
							
							int has_index=template.indexOf("HAS");
							int digit=Integer.parseInt(template.substring(has_index+5,has_index+6)); 							
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> lhs_it=lhs_tokens.iterator();
						   		while(lhs_it.hasNext()){
						   			
						   			String lhs_item=lhs_it.next();
						   			
						   			//System.out.println(item+"Compare"+rhs_item);
						   			
						   			if(item.contentEquals(lhs_item)){
						   				present++;
						   				break;
						   			}
						   		}  		
						   		
						   	}
						   	
						   	if(present>=digit)
						   		System.out.println(rule);
						   	

				            
						}
						
					}
					
				}
				
				else if(template.contains("RULE")){
					
					Iterator <String> rule_it=ruleSet.iterator();
					
					while(rule_it.hasNext()){
						
						
						String rule=rule_it.next();
						StringTokenizer ruleToken= new StringTokenizer(rule, "->");
						String lhs=ruleToken.nextToken();
						String rhs=ruleToken.nextToken();
						String lhsandrhs=lhs+","+rhs;
						TreeSet<String> lhsandrhs_tokens=new TreeSet<String>();
									
						
						StringTokenizer lhsandrhsToken= new StringTokenizer(lhsandrhs, ",");
						
						
						if(!lhsandrhs.contains(","))
							lhsandrhs_tokens.add(lhsandrhs);
							
						else
						{
							while(lhsandrhsToken.hasMoreTokens())
								lhsandrhs_tokens.add(lhsandrhsToken.nextToken());
						
						}
						
						
						Iterator <String> items_it=itemSet.iterator();
																	
						if(template.contains("ANY")){
					    
							
							
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> lhsandrhs_it=lhsandrhs_tokens.iterator();
						   		while(lhsandrhs_it.hasNext()){
						   			
						   			String lhsandrhs_item=lhsandrhs_it.next();
						   			
						   			//System.out.println(item+"Compare"+rhs_item);
						   			
						   			if(item.contentEquals(lhsandrhs_item)){
						   				present=1;
						   				System.out.println(rule);
						   				break;
						   			}
						   		}
						   		
						   	}
						   	
						   
						   		
							
						}
						else if(template.contains("NONE")){
						
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> lhsandrhs_it=lhsandrhs_tokens.iterator();
						   		while(lhsandrhs_it.hasNext()){
						   			
						   			String lhsandrhs_item=lhsandrhs_it.next();
						   			
						   			if(item.contentEquals(lhsandrhs_item)){
						   				present=1;
						   				break;
						   			}
						   			
						   		}
						   		
						   		if(present==1)
						   			break;
						   		
						   	}
						   	
						   	if(present==0)
						   		System.out.println(rule);
							
							
						}
						else{
							
							int has_index=template.indexOf("HAS");
							int digit=Integer.parseInt(template.substring(has_index+5,has_index+6)); 							
							int present=0;
						   	while(items_it.hasNext()){
						   		
						   		
						   		String item=items_it.next();
						   		//System.out.println("*************ITEM: "+item);
						   		Iterator <String> lhsandrhs_it=lhsandrhs_tokens.iterator();
						   		while(lhsandrhs_it.hasNext()){
						   			
						   			String lhsandrhs_item=lhsandrhs_it.next();
						   			
						   			//System.out.println(item+"Compare"+rhs_item);
						   			
						   			if(item.contentEquals(lhsandrhs_item)){
						   				present++;
						   				break;
						   			}
						   		}  		
						   		
						   	}
						   	
						   	if(present>=digit)
						   		System.out.println(rule);
						   	

				            
						}
						
					}
					
				}
					
				}
			}
	
		
		
		
		public static void RulesFinder(HashMap<String,Integer> finalFreqSets,HashMap<String,Integer> samples){
			
			int rules_count=0;
			//Tokenize before lhs & rhs			
			SortedSet <String> lhs_buffer =new TreeSet<String>();
			SortedSet <String> rhs_buffer =new TreeSet<String>();
			//LHS & RHS
			
			for (String keylhs : finalFreqSets.keySet() ) {
				// Get the String value that goes with the key
				int valuelhs = finalFreqSets.get( keylhs );	
				StringTokenizer token_items_lhs = new StringTokenizer(keylhs, ",");
				
				lhs_buffer.clear();	
				
				while(token_items_lhs.hasMoreTokens()){
					//System.out.println("Inside While");
					String temp_token=token_items_lhs.nextToken();
					lhs_buffer.add(temp_token);
				}
				
				if(lhs_buffer.isEmpty())lhs_buffer.add(keylhs);
				
				for (String keyrhs : finalFreqSets.keySet() ) {
					// Get the String value that goes with the key
					int valuerhs = finalFreqSets.get( keyrhs );
					StringTokenizer token_items_rhs = new StringTokenizer(keyrhs, ",");
					
					rhs_buffer.clear();
					while(token_items_rhs.hasMoreTokens()){
						//System.out.println("Inside While");
						String temp_token=token_items_rhs.nextToken();
						rhs_buffer.add(temp_token);
					}
					
					if(rhs_buffer.isEmpty())rhs_buffer.add(keyrhs);
					
					//Checking if (X intersection Y is empty)
					
					Iterator <String> it_lhs =lhs_buffer.iterator();
					
					int present=0;
					
					while(it_lhs.hasNext()){
						
						String lhs_token=it_lhs.next();
						Iterator <String> it_rhs =rhs_buffer.iterator();
						while(it_rhs.hasNext()){
							
							String rhs_token=it_rhs.next();
							if(lhs_token.equals(rhs_token)){
								present=1;
								break;
							}
						}
						
						if(present==1)
							break;
						
					}
					
					if(present==0){ //Intersection is empty and hence check confidence and support
											
						
				       //Checking the Frequency for lhs_rhs
						int combi_frequency=0;
						for(int k=1;k<=totalSamples;k++){
							
							StringTokenizer items = new StringTokenizer(keylhs+","+keyrhs, ",");
							int item_present=1;	
							while(items.hasMoreElements()){
								String key=items.nextToken();
								//System.out.println("To Check :"+key+k);
								if(samples.containsKey(key+k)==false)
									item_present=0;
							}
						   
							if(item_present==1) combi_frequency++; 							
						}
						
						
						if((combi_frequency)>=min_confidence*valuelhs)
						{
							//System.out.println(keylhs+"->"+keyrhs);
							ruleSet.add(keylhs+"->"+keyrhs);
							rules_count++;
						}	
						
					}
								
				}
				
				
			}
					
		}
		
		
			
		public static HashMap<String,Integer> NextLevel(HashMap<String,Integer> finalItemsWithFrequency,HashMap<String,Integer> samples,int level){
			
						
		  if(level==2)
		  {	  
						
			String[] elements;
	
			elements=finalItemsWithFrequency.keySet().toArray(new String[0]);		
				
			int[] indices;
			
			CombinationGenerator x = new CombinationGenerator (elements.length, 2);
			
			TreeSet <String> combinationSet = new TreeSet <String>();
			HashMap<String,Integer> level_buffer= new HashMap<String,Integer>();
			
			int combi_frequency;
			while (x.hasMore ()) {
				
				StringBuffer combination = new StringBuffer();
				indices = x.getNext ();
			
				for (int i = 0; i < indices.length; i++) {
					if(i==indices.length-1)
						combination.append (elements[indices[i]]);
					else
						combination.append(elements[indices[i]]+",");	
				}
				
			    //System.out.println (combination.toString ());
				combinationSet.add(combination.toString());
			   
				//Calculating frequency for sets
				
				combi_frequency=0;
				for(int k=1;k<=totalSamples;k++){
					
					StringTokenizer items = new StringTokenizer(combination.toString(), ",");
					int item_present=1;	
					while(items.hasMoreElements()){
						String key=items.nextToken();
						//System.out.println("To Check :"+key+k);
						if(samples.containsKey(key+k)==false)
							item_present=0;
					}
				   
					if(item_present==1) combi_frequency++; 							
				}
				
				
				float support=combi_frequency/min_support;
				if(support>= totalSamples)
				{
					finalItemsWithFrequency.put(combination.toString(), combi_frequency);
					level_buffer.put(combination.toString(), combi_frequency);
				}
				//Checking support
				
			
			}
			
			return level_buffer;
			
		  }
		  
		  else
		  {
			
			  
			  Set setTokens= new HashSet();
				SortedSet <String> combination_buffer =new TreeSet<String>();
				//Tokenize the sets
				
				setTokens=finalItemsWithFrequency.keySet();
				
				
				Iterator<String> set_iterator=setTokens.iterator();
				
				while(set_iterator.hasNext()){
					
					String combination=(String) set_iterator.next();
					StringTokenizer token_items = new StringTokenizer(combination, ",");
					
					while(token_items.hasMoreTokens()){
						//System.out.println("Inside While");
						String temp_token=token_items.nextToken();
						combination_buffer.add(temp_token);
					}
				}
		
			  
			  String[] elements;
				
				elements=combination_buffer.toArray(new String[0]);	
					
				int[] indices;
				
				CombinationGenerator x = new CombinationGenerator (elements.length, level);
				
				TreeSet <String> combinationSet = new TreeSet <String>();
				HashMap<String,Integer> level_buffer= new HashMap<String,Integer>();
				
				int combi_frequency;
				while (x.hasMore ()) {
					
					StringBuffer combination = new StringBuffer();
					indices = x.getNext ();
				
					for (int i = 0; i < indices.length; i++) {
						if(i==indices.length-1)
							combination.append (elements[indices[i]]);
						else
							combination.append(elements[indices[i]]+",");	
					}
					
				    //System.out.println (combination.toString ());
					combinationSet.add(combination.toString());
				   
					//Calculating frequency for sets
					
					combi_frequency=0;
					for(int k=1;k<=totalSamples;k++){
						
						StringTokenizer items = new StringTokenizer(combination.toString(), ",");
						int item_present=1;	
						while(items.hasMoreElements()){
							String key=items.nextToken();
							//System.out.println("To Check :"+key+k);
							if(samples.containsKey(key+k)==false)
								item_present=0;
						}
					   
						if(item_present==1) 
							combi_frequency++; 							
					}
					
					if(combi_frequency>= min_support*totalSamples)
					{
						finalItemsWithFrequency.put(combination.toString(), combi_frequency);
						level_buffer.put(combination.toString(), combi_frequency);
					}
					//Checking support
					
				
				}
				
				return level_buffer;

		  }

		}
		
}
