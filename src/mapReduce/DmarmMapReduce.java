package mapReduce; /**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DmarmMapReduce extends Configured implements Tool 
{
	public static class MapClass extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>
	{
		private Text word = new Text();
		private final static IntWritable one = new IntWritable(1);

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException 
		{
			String delimiter = "#";/* delimiter between itemSet elements */
			String[] input_line = value.toString().split(",");/* split line on delimiter*/
			System.out.println("Working on input line :'" + value.toString() + "'...");
			
			 /* set of itemSets: set of size-1 itemSets, set of size-2 itemSets, set of size-(iteration)itemSets */
			Set<Set<String>> itemSets = new HashSet<Set<String>>();
			/* set of itemSets: set of size-1 itemSets, set of size-2 itemSets, set of size-(iteration) itemSets */
			Set<Set<String>> lastSizeItemSets = new HashSet<Set<String>>(); 
			Set<String> set = null;/* the itemSet that is generated each time */
			Set<String> workingSet = null;/* the itemSet that is generated each time */
			int iteration = 1;/* iteration number (from 1 to input line length) */

			while (iteration <= input_line.length) { // for length iterations
				System.out.println("Working for iteration:" + iteration);
				set = new TreeSet<String>();// the itemSet that is generated each time
				if (iteration == 1) {
					/* set of itemSets: set of size-1 iteSets, set of size-2 itemSets, set of size-(iteration) itemSets */
					itemSets = new HashSet<Set<String>>(); 
					for (int i = 0; i < input_line.length; i++) {
						set.add(input_line[i]);/* add items from the line to the first itemSet */
						itemSets.add(set);/* add the generated itemSet to the itemSet array */
						set = new TreeSet<String>();/* the itemSet that is generated each time */
					}
					/* prepare itemSet elements for mapper output*/
					Set<String> set2 = new HashSet<String>();
					for (int i = 0; i < itemSets.size(); i++) {
						set2.add(itemSets.toArray()[i].toString());
					}
					TreeSet<String> sortedSet = new TreeSet<String>(set2);
					StringBuffer buffer = new StringBuffer();
					/* for all size-k itemSets output pairs (key,1) */
					for (int i = 0; i < sortedSet.size(); i++) {
						buffer = new StringBuffer();
						buffer.append(sortedSet.toArray()[i].toString());
						word.set(buffer.toString().trim().replaceAll("\\[|\\]|", "").replaceAll(",", delimiter)
								.replaceAll(" ", ""));
						output.collect(word, one);
					}
					lastSizeItemSets = itemSets;
				} // end of if
				else if (iteration == 2) {
					/* set of itemSets: set of size-1 iteSets, set of size-2 itemSets, set of size-(iteration) itemSets */
					itemSets = new HashSet<Set<String>>(); 
					for (int i = 0; i < input_line.length - 1; i++) {
						for (int j = input_line.length - 1; j > i; j--) {
							if (i != j) {
								set.add(input_line[i]);/* add items from the line to the first itemSet */
								set.add(input_line[j]);
							}
							itemSets.add(set);/* add the generated itemSet to the itemSet array */
							set = new TreeSet<String>();/* the itemSet that is generated each time */
						}
					}
					/* prepare itemSet elements for mapper output */
					Set<String> set2 = new HashSet<String>();
					for (int i = 0; i < itemSets.size(); i++) {
						set2.add(itemSets.toArray()[i].toString());
					}
					TreeSet<String> sortedSet = new TreeSet<String>(set2);
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < sortedSet.size(); i++) {/* for all size-k itemSets output pairs (key,1) */
						buffer = new StringBuffer();
						buffer.append(sortedSet.toArray()[i].toString());
						word.set(buffer.toString().trim().replaceAll("\\[|\\]|", "").replaceAll(",", delimiter)
								.replaceAll(" ", ""));
						output.collect(word, one);//(value[i],1) distinct fields
					}
					lastSizeItemSets = itemSets;
				} // end of else if
				else { // size 3 or higher
					/* set of itemSets: set of size-1 iteSets, set of size-2 itemSets, set of size-(iteration) itemSets */
					itemSets = new HashSet<Set<String>>(); 
					Set[] previousSizeItemsets = new Set[lastSizeItemSets.size()];
					for (int i = 0; i < lastSizeItemSets.size(); i++) {
						previousSizeItemsets[i] = (Set) lastSizeItemSets.toArray()[i];
					}
					/* FIRST LOOP
					 * for all itemSets of size k-1 
					 */
					for (int iterator1 = 0; iterator1 < previousSizeItemsets.length; iterator1++) {
						set = new TreeSet<String>();/* the itemSet that is generated each time */
						workingSet = new TreeSet<String>();/* the itemSet that is generated each time */
						workingSet.addAll(previousSizeItemsets[iterator1]);/* add first */
						/* SECOND LOOP
						 *  for all other itemSets of size k-1 
						 */
						for (int iterator2 = previousSizeItemsets.length - 1; iterator2 >= iterator1; iterator2--) {
							set = new TreeSet<String>();/* the itemSet that is generated each time*/
							/* THIRD LOOP
							 * for every element in the current size k-1 itemSet 
							 */
							for (int iterator3 = 0; iterator3 < previousSizeItemsets[iterator2]
									.toArray().length; iterator3++) {
								set.addAll(workingSet);
								set.add(previousSizeItemsets[iterator2].toArray()[iterator3].toString());
								if (set.size() >= iteration)/* add only size k generated itemSets */
									itemSets.add(set);//* add the generated itemSet to the itemSet array */
								set = new TreeSet<String>();/* the itemSet that is generated each time */
							} // THIRD LOOP END
						} // SECOND LOOP END
					} // FIRST LOOP END
						/* prepare itemSet elements for mapper output */
					Set<String> set2 = new HashSet<String>();
					for (int i = 0; i < itemSets.size(); i++) {
						set2.add(itemSets.toArray()[i].toString());
					}
					TreeSet<String> sortedSet = new TreeSet<String>(set2);
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < sortedSet.size(); i++) {/* for all size-k itemSets output pairs (key,1) */
						buffer = new StringBuffer();
						buffer.append(sortedSet.toArray()[i].toString());
				word.set(buffer.toString().trim().replaceAll("\\[|\\]|", "").replaceAll(",", delimiter)
						.replaceAll(" ", ""));
				output.collect(word, one);//(value[i],1) distinct fields
					}
					lastSizeItemSets = itemSets;
				} // end of else
				iteration++;// next iteration
			}
		}
	}
/**
* A reducer class that just emits the sum of the input values.
*/
	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> 
	{
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) 
				throws IOException 
		{
			int count = 0;
			while (values.hasNext()) 
			{
				count+=values.next().get();
			}
			output.collect(key, new IntWritable(count));	
		}
	}

	static int printUsage() 
	{
		System.out.println("DmarmMapReduce [-m <maps>] [-r <reduces>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}
/**
* The main driver for word count map/reduce program.
* Invoke this method to submit the map/reduce job.
* @throws IOException When there is communication problems with the
* job tracker.
*/
	public int run(String[] args) throws Exception 
	{
		JobConf conf = new JobConf(getConf(), DmarmMapReduce.class);
		conf.setJobName("DmarmMapReduce");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(IntWritable.class);
		long milliSeconds = 0;
		conf.setLong("mapred.task.timeout", milliSeconds);
		conf.setMapperClass(MapClass.class);
		conf.setReducerClass(Reduce.class);
		List<String> other_args = new ArrayList<String>();
		for(int i=0; i < args.length; ++i) 
		{
			try 
			{
				if ("-m".equals(args[i])) 
				{
					conf.setNumMapTasks(Integer.parseInt(args[++i]));
				} 
				else if ("-r".equals(args[i])) 
				{
					conf.setNumReduceTasks(Integer.parseInt(args[++i]));
				}
				else 
				{
					other_args.add(args[i]);
				}
			}
			catch (NumberFormatException except)
			{
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				return printUsage();
			} 
			catch (ArrayIndexOutOfBoundsException except)
			{
				System.out.println("ERROR: Required parameter missing from " + args[i-1]);
				return printUsage();
			}
		}
		// Make sure there are exactly 2 parameters left.
		if (other_args.size() != 2) 
		{
			System.out.println("ERROR: Wrong number of parameters: " + other_args.size() + " instead of 2.");
			return printUsage();
		}
		FileInputFormat.setInputPaths(conf, other_args.get(0));
		FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));
		JobClient.runJob(conf);
		return 0;
	}

	public static void main(String[] args) throws Exception 
	{
		int res = ToolRunner.run(new Configuration(), new DmarmMapReduce(), args);
		System.exit(res);
	}
}