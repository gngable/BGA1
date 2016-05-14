/*
 Copyright (c) 2016 Nick Gable (Servant Software)

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */


import java.util.*;
import BGAnalyzer.*;
import java.io.*;
import java.text.*;

public class Main
{
	//this function is really just to test the BGAnalyzer class
	public static void main(String[] args)
	{
		System.out.println("CWD is " + System.getProperty("user.dir"));
		// read test file
		BGAnalyzer.InputReading[] readings = readFile("/storage/emulated/0/AppProjects/BGA1/bloodglucosetestdata.txt");
		System.out.println("Read " + readings.length + " records from the file");
		
		
		//send the data to BGAnalyzer
		BGAnalyzer.OutputValue[] values = BGAnalyzer.analyzeBG(readings, 80, 120, null, null);
		System.out.println("num output values = " + values.length);
		
		//print the result
		displayOutput(values);
	}

	private static void displayOutput(BGAnalyzer.OutputValue[] values)
	{
		
	}
	
	public static BGAnalyzer.InputReading[] readFile(String filename){
		try{
			List<BGAnalyzer.InputReading> readings = new ArrayList<>();
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			String line;
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			int totalcount =0;
			int readcount = 0;
			int shortcount = 0;
			int notabs = 0;
			
			while ((line = br.readLine()) != null){
				totalcount++;
				
				if (line.indexOf("\t") == -1){
					notabs++;
					continue;
				}
				
				String[] fields = line.split("\t");
				
				if (fields.length < 4){
					shortcount++;
					continue;
				}
				
				try {
					BGAnalyzer.InputReading reading = new BGAnalyzer.InputReading();
				
					if (fields[4].trim().toLowerCase().indexOf("high") != -1){
						reading.reading = 401;
					} else {
						reading.reading = Integer.parseInt(fields[4]);
					}
					
					reading.time = df.parse(fields[3]);
				
					readings.add(reading);
					readcount++;
				} catch(Exception ex){
					System.out.println("Non-fatal error, could not parse line/n<" + line + ">/nbecause: " + ex.getMessage());
				}
			}
			
			System.out.println("read " + readcount + " of " + totalcount + ". Short = " + shortcount + ". Notabs = " + notabs);
			
			return readings.toArray(new BGAnalyzer.InputReading[readings.size()]);
		} catch (Exception ex){
			System.out.println("Error reading file: " + ex.getMessage());
			return null;
		}
	}
}
