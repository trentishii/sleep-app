package com.example.trent.sleepapp.pvt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.trent.sleepapp.pvt.PVTConfig.PVTConfigTest;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PVTConfigGetter {
	/** Strings allocated in beginning to prevent garbage collection. */
	private static final String STUDY_ID = "study_id";
	private static final String DEFAULT_STUDY = "default_study";
	private static final String PVTCONFIG = "pvtconfig";
	private static final String PARSING_EXCEPTION = 
		"There should be exactly one pvtconfig node";
	private static final String NAME = "name";
	private static final String PARSING_EXCEPTION2 = 
		"There should be at least one test node";
	private static final String TYPE = "type";
	private static final String SPOT = "spot";
	private static final String PARSING_EXCEPTION3 = 
		"Currently, all tests must be of type 'spot'.";
	private static final String ENABLED = "enabled";
	private static final String TESTPARAMS = "testparams";
	private static final String PARAM = "param";
	private static final String PARSING_EXCEPTION4 = 
		"testparams should only have param nodes as children";
	private static final String VALUE = "value";
	private static final String ILLEGAL_PARAM_NAME = "Illegal param name: ";
	private static final String SERIAL = "serial";

	public final String TAG = getClass().getName();
	public static final String CONFIG_FILE_NAME = "pvt_config.xml";

	private Context context;
	private DataStorage dataStorage = null;
	private PVTConfig config = null;

	public PVTConfigGetter(Context context) {
		this.context = context;
	}

	public PVTConfig get() {
		if (config == null) {
			parseConfigFile();
		}
		return config;
	}

	
	/**
	 * Get the config file for reading/writing preferences
	 * @return
	 */
	private File getConfigFile() {
		if (dataStorage == null) {
			// TODO inject this dataStorage, don't new it
			SharedPreferences prefs = context.getSharedPreferences(PVT.PREFS_NAME,
					Context.MODE_PRIVATE);
			String studyId = prefs.getString(STUDY_ID, DEFAULT_STUDY);
			this.dataStorage = new DataStorage(context, studyId);

		}
		
		return dataStorage.getConfigFile(CONFIG_FILE_NAME);
	}

	/**
	 * Writes the current configuration to disk
	 * TODO: this method is a massive short-term hack that should probably not really be used over the long term. 
	 */
	public void saveConfigFile() throws IOException{
		File configFile = getConfigFile();
		FileWriter out = new FileWriter(configFile);
		try {			
			out.write("<?xml version=\"1.0\"?>\n");
			out.write("<pvtconfig name=\"Study\" >");		//TODO: allow other study names
	
			for (PVTConfigTest test : config.tests) {
		    out.write("<test type=\"spot\" enabled=\"true\" >\n");		//TODO: allow other test types
	
		    out.write("<testparams>\n");
		    for (Entry<PVTParam, String> param_value : test.testParams.entrySet()) {
					PVTParam param = param_value.getKey();
					String value = param_value.getValue();
					
					out.write("<param name=\"" + param + "\" value=\"" + value + "\" />\n");
				}
		    out.write("</testparams>\n");
	
		    out.write("</test>\n");
			}
			out.write("</pvtconfig>\n");
		}
		finally {
			out.close();
		}
	}

//	            <param name="test_duration" value="300000" />               <!-- duration of the test (5 min = 300000ms in [Loh et al. 2004]) -->
//	            <param name="min_delay" value="2000" />                     <!-- minimum foreperiod duration (2000ms in [Loh et al. 2004]) -->
//	            <param name="max_delay" value="10000" />                    <!-- maximum foreperiod duration (10000ms in [Loh et al. 2004]) -->
//	            <param name="restart_delay" value="1000" />                 <!-- restart delay: time after a response that feedback is shown until next foreperiod (1sec in [Loh et al. 2004]) -->
//	            <param name="countdown_delay" value="2000" />               <!-- brief delay before the countdown to the beginning of the test starts -->
//	            <param name="minor_lapse_delay" value="500" />              <!-- minimum response time classified as a minor lapse [Loh et al. 2004]-->
//	            <param name="visible_response_time_enabled" value="false" /><!-- should the user's response time (e.g. "250ms") be displayed after each trial? -->
//	            <param name="trial_counter_enabled" value="false" />        <!-- should the current trial number (e.g. "Trial 9") be displayed? -->
//	            
//	            <!-- Touchscreen-based responses. Enabling more than one of these simultaneously has
//	                 undefined behaviour (in most cases it will just cause only one to be active). -->
//	                 
//
//	            <!-- 1. respond on touch down -->
//	<!--             <param name="touch_down_enabled" value="true" />        -->
//
//	            <!-- 2. respond on finger lift -->
//	<!--             <param name="touch_up_enabled" value="true" />          -->
//
//	            <!-- 3. respond when the size of the touch area passes a threshold (finger tilt) -->
//	<!--             <param name="pressure_sensor_enabled" value="true" />   -->
//
//	            <!-- 4. respond when the finger crosses a goal line (goal crossing) -->
//	<!--             <param name="goal_crossing_enabled" value="true" />     -->
//
//	            <!-- 5. Physical button response (volume, menu key, etc). This can be enabled simultaneously with any of the above. -->
//	            <param name="physical_button_enabled" value="true" />
//	        </testparams>
//	    </test>
//	</pvtconfig>
	
	
	private boolean parseConfigFile() throws PVTConfigParsingException {
		File configFile = getConfigFile();

		try {
			InputStream fis;
			if (!configFile.exists()) {
				//if the config file does not exist, we'll use the default pvt_config.xml and use it instead
				Log.d(TAG, "No study config file found. Loading default configuration instead.");
				AssetManager assets = context.getAssets();
				fis = assets.open(CONFIG_FILE_NAME);
			}
			else {
				fis = new FileInputStream(configFile);
			}

			config = parseFromInputStream(fis);
		}
		catch (IOException ioe) {
			Log.d(TAG, ioe.toString());
			return false;
		}
		return true;
	}

	/*
	 * Parse the XML! This uses a DOM parser, which loads the entire document
	 * at once. This is simpler than a SAX parser because we don't have to worry
	 * about any asynchronous calls.
	 */
	// visible for testing
	PVTConfig parseFromInputStream(InputStream is) {
		PVTConfig config = new PVTConfig();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(is);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		NodeList pvtConfigNodes = dom.getElementsByTagName(PVTCONFIG);
		if (pvtConfigNodes.getLength() != 1) {
			throw new PVTConfigParsingException(PARSING_EXCEPTION);
		}
		Node pvtConfigNode = pvtConfigNodes.item(0);
		config.name = pvtConfigNode.getAttributes().getNamedItem(NAME).getNodeValue();

		NodeList testNodes = pvtConfigNode.getChildNodes();
		if (testNodes.getLength() == 0) {
			throw new PVTConfigParsingException(PARSING_EXCEPTION2);
		}
		for (int i = 0; i < testNodes.getLength(); i++) {
			Node testNode = testNodes.item(i);
			if (testNode.getNodeType() != Node.ELEMENT_NODE) {
				// because we're iterating through all children of the pvtconfig
				// node, there's a bunch of text and other junk, so skip all
				// non-element nodes.
				continue;
			}
			PVTConfigTest pvtConfigTest = new PVTConfigTest();
			String testType = testNode.getAttributes().getNamedItem(TYPE).getNodeValue();
			if (!testType.equals(SPOT)) {
				throw new PVTConfigParsingException(PARSING_EXCEPTION3);
			}
			pvtConfigTest.type = testType;
			pvtConfigTest.enabled = Boolean.parseBoolean(
					testNode.getAttributes().getNamedItem(ENABLED).getNodeValue());

			// iterate through the children, find the testparams (ignore the rest for now)
			NodeList testNodeChildren = testNode.getChildNodes();
			for (int j = 0; j < testNodeChildren.getLength(); j++) {
				Node testNodeChild = testNodeChildren.item(j);
				if (testNodeChild.getNodeName().equals(TESTPARAMS)) {
					NodeList allParamNodes = testNodeChild.getChildNodes();
					for (int k = 0; k < allParamNodes.getLength(); k++) {
						Node paramNode = allParamNodes.item(k);
						if (paramNode.getNodeType() != Node.ELEMENT_NODE) {
							continue; // see earlier comment about junk nodes.
						}
						if (!paramNode.getNodeName().equals(PARAM)) {
							throw new PVTConfigParsingException(PARSING_EXCEPTION4);
						}
						NamedNodeMap attrs = paramNode.getAttributes();
						String paramName = attrs.getNamedItem(NAME).getNodeValue();
						String paramValue = attrs.getNamedItem(VALUE).getNodeValue();

						// First try to parse it as an int param, then as a boolean, then as a String
						// TODO: think up something more elegant here. This is a bit gross.
						try {
							PVTIntParam paramNameEnum = PVTIntParam.valueOf(paramName);
							pvtConfigTest.testParams.put(paramNameEnum, paramValue);
						} catch (IllegalArgumentException iae) {
							try {
								PVTBooleanParam paramNameEnum2 = PVTBooleanParam.valueOf(paramName);
								pvtConfigTest.testParams.put(paramNameEnum2, paramValue);
							} catch (IllegalArgumentException iae2) {
								try {
									PVTStringParam paramNameEnum3 = PVTStringParam.valueOf(paramName);
									pvtConfigTest.testParams.put(paramNameEnum3, paramValue);
								} catch (IllegalArgumentException iae3) {
									throw new PVTConfigParsingException(ILLEGAL_PARAM_NAME
											+ paramName);
								}
							}
						}
					}
				}
			}
			config.tests.add(pvtConfigTest);
		}
		return config;
	}

	// TODO make this a checked exception, and catch it in the app.
	@SuppressWarnings(SERIAL)
	public static class PVTConfigParsingException extends RuntimeException {
		public PVTConfigParsingException(String text) {
			super(text);
		}
	}
}
