package com.stratio.qa.ATests;


import com.stratio.qa.cucumber.testng.CucumberRunner;
import com.stratio.qa.utils.BaseGTest;
import cucumber.api.CucumberOptions;
import org.testng.annotations.Test;

@CucumberOptions(features = {"src/test/resources/features/backgroundTag1.feature",
                             "src/test/resources/features/backgroundTag2.feature",
                             "src/test/resources/features/backgroundTag3.feature"})
public class BackgroundTagIT extends BaseGTest {
    @Test
    public void backgroundTagIt() throws Exception {
        new CucumberRunner(this.getClass()).runCukes();
    }
}
