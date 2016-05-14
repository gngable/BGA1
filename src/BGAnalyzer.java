
import java.util.*;
import BGAnalyzer.*;

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


public class BGAnalyzer
{
	public static class InputReading
	{
		public Date time = null;
		public int reading = 0;
	}

	public static class OutputValue
	{
		public enum BGRange
		{LOW, IN_RANGE, HIGH, NOT_SET}
		public int hour = -1;
		public int average = 0;
		public BGRange averagerange = BGRange.NOT_SET;
		public int readingsinaveragerange = 0;
		public int totalreadings = 0;
		public int percentlow = 0;
		public int percentinrange = 0;
		public int percenthigh = 0;
	}

	public static OutputValue[] analyzeBG(InputReading [] readings, int bgrangelow, int bgrangehigh, Date starttime, Date endtime)
	{
		List<OutputValue> output = new ArrayList<>();
		
		for (int hour = 0; hour < 24; hour++)
		{
			List<Integer> bg = new ArrayList<>();
			
			for (InputReading reading : readings)
			{
				if (isInTimeRange(reading.time, starttime, endtime) && isInHour(reading.time, hour))
				{
					bg.add(reading.reading);
				}
				else
				{
					continue;
				}
			}
			
			if (!bg.isEmpty()) output.add(computeOutputValue(bg, hour, bgrangelow, bgrangehigh));
			else System.out.println("There are no readings for hour " + hour);
		}


		return output.toArray(new OutputValue[output.size()]);
	}

	private static BGAnalyzer.OutputValue computeOutputValue(List<Integer> bg, int hour, int bgrangelow, int bgrangehigh)
	{
		OutputValue output = new OutputValue();
		
		output.totalreadings = bg.size();
		output.hour = hour;
		
		int totalbg = 0;
		int totallow = 0;
		int totalinrange = 0;
		int totalhigh = 0;
		
		for (Integer reading : bg){
			totalbg += reading;
			
			if (reading < bgrangelow){
				totallow++;
			} else if (reading > bgrangehigh){
				totalhigh++;
			} else {
				totalinrange++;
			}
		}
		
		output.average = totalbg / bg.size();
		output.percenthigh = ((totalhigh / bg.size()) * 100);
		output.percentinrange = ((totalinrange / bg.size()) * 100);
		output.percentlow = ((totallow / bg.size()) * 100);
		
		if (output.average < bgrangelow){
			output.averagerange = OutputValue.BGRange.LOW;
			output.readingsinaveragerange = totallow;
		} else if (output.average > bgrangehigh){
			output.averagerange = OutputValue.BGRange.HIGH;
			output.readingsinaveragerange = totalhigh;
		} else {
			output.averagerange = OutputValue.BGRange.IN_RANGE;
			output.readingsinaveragerange = totalinrange;
		}
		
		return output;
	}

	private static boolean isInHour(Date time, int hour)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(time);
		if (c.get(Calendar.HOUR_OF_DAY) == hour){
			return true;
		}
		
		return false;
	}

	private static boolean isInTimeRange(Date time, Date starttime, Date endtime)
	{
		if (time == null) return false;

		if (starttime != null)
		{
			if (time.before(starttime)) return false;
		}

		if (endtime != null)
		{
			if (time.after(endtime)) return false;
		}

		return true;
	}
}
