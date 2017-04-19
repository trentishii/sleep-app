package com.example.trent.sleepapp.pvt;

/**
 * A String parameter for a PVT test. 
 */
public enum PVTStringParam implements PVTParam {

  /** Free-form tag for the test */
  test_tag("");

  public String defaultValue;

  private PVTStringParam(String defaultValue) {
    this.defaultValue = defaultValue;
  }
}
