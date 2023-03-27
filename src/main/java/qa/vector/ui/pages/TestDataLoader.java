package qa.vector.ui.pages;

//import com.opencsv.CSVReader;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public class TestDataLoader {

    public HashMap<String, String> config = null;
  //  private final String filePath = null;

    
   
    /**
     * Method to get all element info from a property file.
     *
     * @param filePath - from which file info needs to be fetched
     * @return - HashMap<String,String>
     */
    public HashMap<String, String> propertiesLoader(String filePath) throws Exception {
        HashMap<String, String> HMap = new HashMap<String, String>();
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(filePath);
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                HMap.put(key, value);
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return HMap;

    }

    /**
     * Gets Config data from config.properties file
     *
     * @return HashMap
     */
    public HashMap<String, String> getConfigData() throws Exception {
        if (config == null) {
            HashMap<String, String> hashMap = null;
            String path = System.getProperty("user.dir") + "/config.properties";
            hashMap = new TestDataLoader().propertiesLoader(path);
            config = hashMap;
            return hashMap;
        } else {
            return config;
        }
    }

}