package CommonUtility;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.Test;

public class SetEnvStaging {
    @Test
    public void SetFunEnv() {
        try {
            String configFilePath =
                    System.getProperty("user.dir") + "/config.properties";
            PropertiesConfiguration properties = new PropertiesConfiguration(configFilePath);
            properties.setProperty("ExecutionEnvironment", "USPS");
            System.out.println("Execution environment set to USPS");
            properties.save();
        } catch (Exception e) {
            System.out.println("check config file path");
        }
    }
}
