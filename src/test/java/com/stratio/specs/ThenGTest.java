package com.stratio.specs;

import com.stratio.tests.utils.ThreadProperty;
import cucumber.api.DataTable;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by carlosgarcia on 28/06/16.
 */
public class ThenGTest {

    @Test
    public void testValueEqualInJSON() throws Exception{
        String baseData = "consulMesosJSON.conf";
        String envVar = "consulMesos";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar,result);

        List<String> row1 = Arrays.asList("$.[0].Node","equal","paaslab31.stratio.com");
        List<String> row2 = Arrays.asList("[0].Node","equal","paaslab31.stratio.com");

        List<List<String>> rawData = Arrays.asList(row1,row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar,table);

    }


    @Test
    public void testValueNotEqualInJSON() throws Exception{
        String baseData = "consulMesosJSON.conf";
        String envVar = "consulMesos";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar,result);

        List<String> row1 = Arrays.asList("$.[1].Node","not equal","paaslab31.stratio.com");
        List<String> row2 = Arrays.asList("[2].Node","not equal","paaslab32.stratio.com");

        List<List<String>> rawData = Arrays.asList(row1,row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar,table);

    }


    @Test
    public void testValueContainsInJSON() throws Exception{
        String baseData = "consulMesosJSON.conf";
        String envVar = "consulMesos";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar,result);

        List<String> row1 = Arrays.asList("$.[0].ServiceTags","contains","leader");
        List<String> row2 = Arrays.asList("[1].ServiceTags","contains","master");

        List<List<String>> rawData = Arrays.asList(row1,row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar,table);

    }

    @Test
    public void testValueDoesNotContainInJSON() throws Exception{
        String baseData = "consulMesosJSON.conf";
        String envVar = "consulMesos";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar,result);

        List<String> row1 = Arrays.asList("$.[0].ServiceTags","does not contain","adsads");
        List<String> row2 = Arrays.asList("[1].Node","does not contain","rgrerg");

        List<List<String>> rawData = Arrays.asList(row1,row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar,table);

    }

    @Test(expectedExceptions = AssertionError.class)
    public void testWrongOperatorInJSON() throws Exception{
        String baseData = "consulMesosJSON.conf";
        String envVar = "consulMesos";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar,result);

        List<String> row1 = Arrays.asList("$.[0].ServiceTags","&&","leader");
        List<String> row2 = Arrays.asList("[1].Node","||","paaslab32.stratio.com");

        List<List<String>> rawData = Arrays.asList(row1,row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar, table);

    }

    @Test
    public void testKeysContainsInJSON() throws Exception {
        String baseData = "exampleJSON.conf";
        String envVar = "exampleEnvVar";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar, result);

        List<String> row1 = Arrays.asList("$.glossary.~[0]", "contains", "title");
        List<String> row2 = Arrays.asList("$.glossary.GlossDiv.~", "contains", "GlossList");

        List<List<String>> rawData = Arrays.asList(row1, row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar, table);

    }


    @Test
    public void testSizeInJSON() throws Exception {
        String baseData = "consulMesosJSON.conf";
        String envVar = "exampleEnvVar";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar, result);

        List<String> row1 = Arrays.asList("$", "size", "4");
        List<String> row2 = Arrays.asList("$.[0].ServiceTags", "size", "2");

        List<List<String>> rawData = Arrays.asList(row1,row2);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar, table);

    }

    @Test(expectedExceptions = AssertionError.class)
    public void testNotParsedArraySizeInJSON() throws Exception {
        String baseData = "consulMesosJSON.conf";
        String envVar = "exampleEnvVar";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar, result);

        List<String> row1 = Arrays.asList("$.[0]", "size", "4");
        List<List<String>> rawData = Arrays.asList(row1);

        DataTable table = DataTable.create(rawData);
        theng.matchWithExpresion(envVar, table);
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp=".*?Expected array for size operation.*?")
    public void testNotArraySizeInJSON() throws Exception {
        String baseData = "consulMesosJSON.conf";
        String envVar = "exampleEnvVar";
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        CommonG commong = new CommonG();
        ThenGSpec theng = new ThenGSpec(commong);

        String result = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource(baseData).getFile())));

        ThreadProperty.set(envVar, result);

        List<String> row1 = Arrays.asList("$.[0].Node", "size", "4");
        List<List<String>> rawData = Arrays.asList(row1);

        DataTable table = DataTable.create(rawData);

        theng.matchWithExpresion(envVar, table);
    }

}
