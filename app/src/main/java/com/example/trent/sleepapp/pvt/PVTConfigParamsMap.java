package com.example.trent.sleepapp.pvt;

import java.util.HashMap;
import java.util.Locale;

/**
 * Represents PVT Config params from the pvt_config.xml file.
 *
 * @author Dan Tasse
 */
public class PVTConfigParamsMap extends HashMap<PVTParam, String> {

	/** Strings allocated in beginning to prevent garbage collection. */
	private static final String ILLEGAL_VALUE = "Illegal value for ";
	private static final String IN_CONFIG_FILE = " in config file: ";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	
	private static final long serialVersionUID = -7405017881482218051L;

	/**
	 * @return the integer value for the parameter |paramName| in this map. If
	 * |paramName| is not a key in this map, or if get(paramName) returns a
	 * non-integer value, returns the default value for that parameter (as
	 * defined in {@link PVTIntParam}).
	 *
	 * @throws IllegalStateException if |paramName|'s is in the map but its
	 * value is not a valid integer.
	 */
	public int getIntValueOrDefault(PVTIntParam paramName) {
		if (!containsKey(paramName)) {
			return paramName.defaultValue;
		}
		String stringValue = get(paramName);
		try {
			return Integer.parseInt(stringValue);
		} catch (NumberFormatException nfe) {
			throw new IllegalStateException(ILLEGAL_VALUE + paramName +
					IN_CONFIG_FILE + get(paramName));
		}
	}

	/**
	 * @return the boolean value for the parameter |paramName|. If |paramName|
	 * is not a key in this map, returns the default value for that parameter.
	 * @throws IllegalStateException, if |paramName| is in the map but not equal
	 * to "true" or "false".
	 */
	public boolean getBooleanValueOrDefault(PVTBooleanParam paramName) {

		if (!containsKey(paramName)) {
			return paramName.defaultValue;
		}
		String stringValue = get(paramName).toLowerCase(Locale.US);
		if (TRUE.equals(stringValue)) {
			return true;
		} else if (FALSE.equals(stringValue)) {
			return false;
		}
		throw new IllegalStateException(ILLEGAL_VALUE + paramName +
				IN_CONFIG_FILE + get(paramName));
	}


	/**
	 * @return the String value for the parameter |paramName|. If |paramName|
	 * is not a key in this map, returns the default value for that parameter.
	 */
	public String getStringValueOrDefault(PVTStringParam paramName) {		
		if (!containsKey(paramName)) {
			return paramName.defaultValue;
		}
		return get(paramName);
	}
}
