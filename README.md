This is a analyzer for Dexcom export logs that computes averages and other statistics.
This project is really just a test bed to develop the BGAnalyzer class which I intend on using in other projects and should be able to be used with other brands of CGMs.
It is licensed with an MIT license, so feel free to use it as you wish.

Inputs to BGAnalyzer are an array of times and readings, number of time slices an hour for analysis (so 4 would mean you want output in 15 minute slices, 60/4=15), normal range high and low, and optional start and stop dates.
Output is an array of a classes containing statistics for a slice of the day.
