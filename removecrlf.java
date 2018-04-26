package routines;

//assuming first field not containing linebreak



public class GivFileUtils {

	public static void replacecrlf(String delimiter,Integer delimitercount,String replacement,String textfileloc,String textfiledestination) {
    	java.util.List<Integer> list = new java.util.ArrayList<Integer>();
    	java.util.List<String> errlist = new java.util.ArrayList<String>();
    	java.util.List<String> csvlist = new java.util.ArrayList<String>();
    	
    	//String filename=textfileloc.substring(textfileloc.lastIndexOf("/")+1);
    	String line;
    	

    	//read the file and put lines to list
    	try{
	    	java.io.InputStream ips=new java.io.FileInputStream(textfileloc); 
	    	java.io.InputStreamReader ipsr=new java.io.InputStreamReader(ips);
	    	java.io.BufferedReader br=new java.io.BufferedReader(ipsr);
	    	
	    	Boolean iserr=false;
	    	
    	while ((line=br.readLine())!=null)
    	 {
    		
    		int ldcount = line.length() - line.replace(delimiter, "").length();
    		
    		//we have a line break in the data
    		if(ldcount<delimitercount && ldcount!=0)
    		{
    			errlist.add(line);
        		list.add(ldcount);
        		iserr=true;
        		
    		}
			else if(ldcount==0)
			{
				if(iserr==false && csvlist.size()>1)
				{
					csvlist.set(csvlist.size() - 1,csvlist.get(csvlist.size() - 1)+replacement+line);
				}
				else if(iserr==true && errlist.size()>1)
				{
					errlist.set(errlist.size() - 1,errlist.get(errlist.size() - 1)+replacement+line);
				}
				else 
				{System.out.println("warning: unhandled case");}
			}
    		else
    		{
    			csvlist.add(line);
    			iserr=false;
    		}
    	    
    	 }
    	br.close();
    	} catch(Exception e)
    	{System.out.println("Read error: \n"+e+"\nIn file: \n"+textfileloc);}

    	
    	int i=0;
    	int j=-1;
    	int sum=0;
    	
    	while(i<errlist.size())
    	{
    		sum+=list.get(i);
    		j++;
    		String lineconcat="";
    		
    		if(sum==delimitercount && i!=errlist.size())
    		{
    			for(int k=i-j;k<=i;k++)
    			{
    				if(k<i)
    				{
    					lineconcat+=errlist.get(k)+replacement;
    				}
    				else 
    				{
    					lineconcat+=errlist.get(k);
    				}
    				
    			}
    			csvlist.add(lineconcat);
    			sum=0;
    			j=-1;
    		}
    		else if(i==errlist.size()-1)
    		{
    			csvlist.set(csvlist.size()-1, csvlist.get(csvlist.size()-1)+replacement+errlist.get(errlist.size()-1));
    		}
    		i++;
    			
    	}
  

    	//write extracted data to file
    	try{
    	java.io.File file = new java.io.File(textfiledestination);
    	file.getParentFile().mkdirs();
    	java.io.FileWriter fw= new java.io.FileWriter(file);
    
    	
    	

    	for(String l: csvlist) {
    		/* quoting possiblity
    		int qcount = l.length() - l.replace("\"", "").length();

    		if(qcount%2!=0)
    		{
    			String mid=l.replaceAll("\""+delimiter+"\"","^^"+delimiter+"^^");
    			l="\""+mid.substring(1,mid.length()-1).replaceAll("\"","\"\"").replaceAll("^^", "\"")+"\"";
    		}
    		*/
    		
    		
    	  fw.write(l+"\n");
    	
    		
    	}
    	
    	
    	fw.close();
    	} catch(Exception e)
    	{System.out.println("Write error: \n"+e+"\nIn file: \n"+textfiledestination);}
    }
}

