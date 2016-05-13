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

public class Main
{
	//this function is really just to test the BGAnalyzer class
	public static void main(String[] args)
	{
		// read test file
		BGAnalyzer.InputReading[] readings = readFile("bloodglucosetestdata.txt");
		
		//send the data to BGAnalyzer
		BGAnalyzer.OutputValue[] values = BGAnalyzer.analyzeBG(readings, 80, 120, null, null);
		
		//print the result
		displayOutput(values);
	}

	private static void displayOutput(BGAnalyzer.OutputValue[] values)
	{
		
	}
	
	public static BGAnalyzer.InputReading[] readFile(String filename){
		return null;
	}
}
