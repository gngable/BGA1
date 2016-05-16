
import java.util.*;
import BGAnalyzer.*;

/*
 MIT License

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
	//this is a container class for readings from a meter
	public static class InputReading
	{
		public Date time = null;
		public int reading = 0;
	}

	//this is a container class for analysis output. 
	//there is one OutputValue per hour.
	public static class OutputValue
	{
		public enum BGRange
		{LOW, IN_RANGE, HIGH, NOT_SET}
		public int hour = -1;
		public int slice= 0;
		public int average = 0;
		public BGRange averagerange = BGRange.NOT_SET;
		public int readingsinaveragerange = 0;
		public int totalreadings = 0;
		public int readingslow = 0;
		public int readingsinrange = 0;
		public int readingshigh = 0;
		public int percentlow = 0;
		public int percentinrange = 0;
		public int percenthigh = 0;
	}

	//this is the main function that is called to analyze BG readings
	public static OutputValue[] analyzeBG(InputReading [] readings, int bgrangelow, int bgrangehigh, Date starttime, Date endtime, int slicesperhour)
	{
		List<OutputValue> output = new ArrayList<>();

		//compute the overall statistics
		{
			Integer[] bg = new Integer[readings.length];

			for (int i = 0; i < readings.length; i++)
			{
				bg[i] = new Integer(readings[i].reading);
			} 

			output.add(computeOutputValue(bg, -1, -1, bgrangelow, bgrangehigh));
		}

		//compute by hour and slice
		for (int hour = 0; hour < 24; hour++)
		{
			for (int slice = 0; slice < slicesperhour; slice++)
			{
				List<Integer> bg = new ArrayList<>();

				for (InputReading reading : readings)
				{
					if (isInTimeRange(reading.time, starttime, endtime) && isInHour(reading.time, hour) && isInSlice(reading.time, slice, slicesperhour))
					{
						bg.add(reading.reading);
					}
					else
					{
						continue;
					}
				}

				if (!bg.isEmpty()) output.add(computeOutputValue(bg.toArray(new Integer[bg.size()]), hour, slice, bgrangelow, bgrangehigh));
				else System.out.println("There are no readings for hour " + hour);
			}
		}


		return output.toArray(new OutputValue[output.size()]);
	}

	private static boolean isInSlice(Date time, int slice, int slicesperhour)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(time);
		
		int minutesinslice = (60 / slicesperhour);
		
		if (c.get(Calendar.MINUTE) >= slice * minutesinslice
		&& c.get(Calendar.MINUTE) < (slice + 1) * minutesinslice){
			return true;
		}
		
		return false;
	}

	public static String getOutput(BGAnalyzer.OutputValue[] values, int lowrange, int highrange, int slicesperhour)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Normal range is " + lowrange + " to " + highrange + "\n");
		sb.append("start time\tend time\tlabel\taverge\treadings in average/total\tlow/total\tin range/total\thigh/total\t% low\t% in range\t% high\n");

		for (OutputValue value : values)
		{
			sb.append((value.hour == -1 ? "ALL" : sliceToTime(value.hour, value.slice, slicesperhour))
					  + "\t" + (value.hour == -1 ? "ALL" : sliceToTime(value.hour, value.slice + 1, slicesperhour))
					  + "\t" + value.averagerange
					  + "\t" + value.average
					  + "\t" + value.readingsinaveragerange + "/" + value.totalreadings
					  + "\t" + value.readingslow + "/" + value.totalreadings
					  + "\t" + value.readingsinrange + "/" + value.totalreadings
					  + "\t" + value.readingshigh + "/" + value.totalreadings
					  + "\t" + value.percentlow
					  + "\t" + value.percentinrange
					  + "\t" + value.percenthigh + "\n");
		}

		return sb.toString();
	}

	private static String sliceToTime(int hour, int slice, int slicesperhour)
	{
		String hourstr = "";
		String ampm = "";
		
		String minute = Integer.toString(((60 / slicesperhour) * slice));

		if (minute.compareTo("60") == 0){
			hour++;

			if (hour == 24) hour = 0;

			minute = "00";
		}

		if (minute.length() == 1) minute = "0" + minute;
		
		if (hour == 0){
			hourstr = "12";
			ampm = " am";
		} else if (hour < 12){
			hourstr = Integer.toString(hour);
			ampm = " am";
		} else if (hour == 12){
			hourstr = Integer.toString(hour);
			ampm = " pm";
		} else {
			hourstr = Integer.toString(hour - 12);
			ampm = " pm";
		}
		
		return hourstr + ":" + minute + ampm;
	}

	private static BGAnalyzer.OutputValue computeOutputValue(Integer[] bg, int hour, int slice, int bgrangelow, int bgrangehigh)
	{
		OutputValue output = new OutputValue();

		output.totalreadings = bg.length;
		output.hour = hour;
		output.slice = slice;

		int totalbg = 0;
		int totallow = 0;
		int totalinrange = 0;
		int totalhigh = 0;

		for (Integer reading : bg)
		{
			totalbg += reading;

			if (reading < bgrangelow)
			{
				totallow++;
			}
			else if (reading > bgrangehigh)
			{
				totalhigh++;
			}
			else
			{
				totalinrange++;
			}
		}

		output.average = totalbg / bg.length;
		output.readingslow = totallow;
		output.readingsinrange = totalinrange;
		output.readingshigh = totalhigh;
		output.percenthigh = (int)(((double)totalhigh / (double)bg.length) * 100.0);
		output.percentinrange = (int)(((double)totalinrange / (double)bg.length) * 100.0);
		output.percentlow = (int)(((double)totallow / (double)bg.length) * 100.0);

		if (output.average < bgrangelow)
		{
			output.averagerange = OutputValue.BGRange.LOW;
			output.readingsinaveragerange = totallow;
		}
		else if (output.average > bgrangehigh)
		{
			output.averagerange = OutputValue.BGRange.HIGH;
			output.readingsinaveragerange = totalhigh;
		}
		else
		{
			output.averagerange = OutputValue.BGRange.IN_RANGE;
			output.readingsinaveragerange = totalinrange;
		}

		return output;
	}

	private static boolean isInHour(Date time, int hour)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(time);
		if (c.get(Calendar.HOUR_OF_DAY) == hour)
		{
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
