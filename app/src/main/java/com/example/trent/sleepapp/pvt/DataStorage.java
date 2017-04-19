package com.example.trent.sleepapp.pvt;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
 * Data storage for a single study.
 * @author mjskay
 *
 */
public class DataStorage {

	/** Strings allocated in beginning to prevent garbage collection. */
	private static final String ANDROID = "Android";
	private static final String DATA = "data";
	private static final String FILES = "files";
	private static final String CSV = ".csv";
	private static final String SUMMARY_CSV = "summary.csv";
	private static final String WARNING = "Warning: requested summary stats have changed " +
			"since this file was created. Here are the new stats that are being " +
			"reported:";
	private static final String BLANK = "";
	private static final String COMMA = ",";
	private static final String EXCEPTION = "Cannot write to output directory";
	
	public final String TAG = getClass().getName();

	private String rawFileHeader = Trial.headerCSV();
	private String summaryFileHeader = SummaryStat.headerCSV();
	private File baseDirectory;	//base storage directory (current on external device)
	private File studyDirectory;		//directory used to store data files for the study

	/** Convenience class to return two values from openFile(). */
	private static class OpenFileResponse {
		public PrintStream printStream;
		public String firstLine;
		public OpenFileResponse(PrintStream printStream, String firstLine) {
			this.printStream = printStream;
			this.firstLine = firstLine;
		}
	}
	public DataStorage(Context context, String studyId) {
		//pre-Android 2.1 alternative to Context.getExternalFilesDir() -> baseDirectory
		String packageName = context.getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		baseDirectory = new File(new File(new File(new File(
				externalPath, ANDROID), DATA), packageName), FILES);
		
		studyDirectory = new File(baseDirectory, studyId);
	}

	public boolean writeToCSVs(List<String> lines, String fileName) {
		PrintStream out = null;
		try {
			OpenFileResponse ofResponse = openFile(fileName + CSV);
			out = ofResponse.printStream;
			if (ofResponse.firstLine == null) {
				out.println(rawFileHeader);
			}
			lines.size();
			for (int i = 0; i < lines.size(); i++) {
				out.println(lines.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return true;
	}

	/**
	 * Writes a line in summary.csv for this set of summary stats.
	 * Checks to make sure that the values it's writing are the same as they
	 * have been before; if not, writes another header line.
	 */
	public boolean writeSummaryCSVs(Map<SummaryStat, String> summaryStats) {
		OpenFileResponse ofResponse = null;
		try {
			ofResponse = openFile(SUMMARY_CSV);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		PrintStream out = ofResponse.printStream;
		if (ofResponse.firstLine == null) {
			out.println(summaryFileHeader);
		} else if (!ofResponse.firstLine.equals(summaryFileHeader)) {
			out.println(WARNING);
			out.println(summaryFileHeader);
		}
		out.println(summaryStatsToString(summaryStats));
		out.close();
		return true;
	}

	private String summaryStatsToString(Map<SummaryStat, String> stats) {
		String csv = BLANK;
		SummaryStat[] values = SummaryStat.values();
		for (int i = 0; i < values.length; i++) {
			csv += stats.get(values[i]);
			if (i < values.length - 1) {
				csv += COMMA;
			}
		}
		return csv;
	}

	private OpenFileResponse openFile(String fileName) throws IOException {
		PrintStream out = null;
		String firstLine = null;
		studyDirectory.mkdirs();
		if (studyDirectory.canWrite()) {
			File f = new File(studyDirectory, fileName);
			if (f.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(f));
				firstLine = br.readLine();
				br.close();
			}
			out = new PrintStream(new FileOutputStream(f, true));
		}
		else {
			//TODO: provide an alternative to use of external storage
			//for output if not available (e.g. internal storage)
			throw new IOException(EXCEPTION);
		}
		return new OpenFileResponse(out, firstLine);
	}

	public File getConfigFile(String fileName) {
		studyDirectory.mkdirs();
		return new File(studyDirectory, fileName);
	}
}
