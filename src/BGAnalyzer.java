
import java.util.*;/*
 Copyright (c) 2016 Nick Gable

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


public class BGAnalyzer
{
	public class InputReading{
		public Date time = null;
		public int reading = 0;
	}
	
	public class OutputValue {
		public enum BGRange {LOW, IN_RANGE, HIGH, NOT_SET}
		public Date time = null;
		public int average = 0;
		public BGRange averagerange = BGRange.NOT_SET;
		public int daysinaveragerange = 0;
		public int totaldays = 0;
	}
	
	public static OutputValue[] analyzeBG(InputReading [] readings, int bgrangelow, int bgrangehigh, Date starttime, Date endtime){
		for (InputReading reading : readings){
			if (!isInTimeRange(reading.time, starttime, endtime)){
				
			}
		}
		
		
		return null;
	}

	private static boolean isInTimeRange(Date time, Date starttime, Date endtime)
	{
		if (time == null) return false;
		
		if (starttime != null){
			if (time.before(starttime)) return false;
		}
		
		if (endtime != null){
			if (time.after(endtime)) return false;
		}
		
		return true;
	}
}
