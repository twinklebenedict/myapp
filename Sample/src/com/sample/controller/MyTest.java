package com.sample.controller;

import java.io.IOException;
import java.util.Iterator;

import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdException;
import org.jrobin.core.Util;
import org.jrobin.core.jrrd.Archive;
import org.jrobin.core.jrrd.ConsolidationFunctionType;
import org.jrobin.core.jrrd.DataChunk;
import org.jrobin.core.jrrd.RRDatabase;

public class MyTest {
	
	public static String RRD_FILE = "/home/twinkle.b/memory-used.rrd";
	
	public static void main(String[] args) {
		try {
			RRDatabase db = new RRDatabase(RRD_FILE);
			System.out.println( "LastUpdate" + db.getLastUpdate());
			DataChunk chunk = db.getData(ConsolidationFunctionType.MAX);
			System.out.println(chunk.toString());

			/*Iterator<Archive> it = rrd.getArchives();
			while (it.hasNext()) {
				Archive archive = it.next();
				archive.toString();
				
			}*/
			// create fetch request using the database reference
			/*FetchRequest request = rrd.createFetchRequest("AVERAGE", 100100000L, 100200000L);

			// execute the request
			FetchData fetchData = request.fetchData();

			// From a logical point of view FetchData object is, simply, 
			// an array of FetchPoint objects. Each FetchPoint object 
			// represents all datasource values for the specific timestamp.
			// Here is how you can produce the same output to stdout 
			// as RRDTool's fetch command
			for(int i = 0; i < fetchData.getRowCount(); i++) {
				fetchData.getDsNames();
			    FetchPoint point = fetchData.getRow(i);
			    System.out.println(point.dump());
			}
*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RrdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
