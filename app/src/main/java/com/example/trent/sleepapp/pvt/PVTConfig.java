package com.example.trent.sleepapp.pvt;

import java.util.ArrayList;
import java.util.List;

public class PVTConfig {

  public String name;
  public List<PVTConfigTest> tests;

  public PVTConfig() {
    this.tests = new ArrayList<PVTConfigTest>();
  }

  /** Each PVTConfigTest is pvt_config.xml params for one n-minute
   * sit-down-and-take-a-test session. */
  public static class PVTConfigTest {
    /** URL that the results will be submitted to, when the test is done. */
    public String submitUrl;
    /** "spot" (regular PVT) or "chase". */
    public String type;
    public boolean enabled;

    public PVTConfigParamsMap testParams;

    public PVTConfigTest() {
      testParams = new PVTConfigParamsMap();
    }
  }
}
