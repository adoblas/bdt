/*
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.qa.aspects;

import com.stratio.qa.exceptions.IncludeException;
import cucumber.runtime.io.Resource;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Aspect
public class LoopIncludeTagAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());


    @Pointcut("execution (private * cucumber.runtime.FeatureBuilder.read(..)) &&" + "args (resource)")
    protected void featureBuilderRead(Resource resource) {
    }

    /**
     * @param resource
     * @throws Throwable
     */
    @Around(value = "featureBuilderRead(resource)")
    public String aroundAddLoopTagPointcutScenario(Resource resource) throws Throwable {
        List<String> lines = Files.readAllLines(Paths.get(resource.getPath()), StandardCharsets.UTF_8);
        String listParams;
        String paramReplace;
        String path = resource.getPath();
        int endIndex = path.lastIndexOf("/") + 1;
        path = path.substring(0, endIndex);


        for (int s = 0; s < lines.size(); s++) {
            String[] elems;
            if (lines.get(s).matches("\\s*@loop.*")) {
                listParams = lines.get(s).substring((lines.get(s).lastIndexOf("(") + 1), (lines.get(s).length()) - 1).split(",")[0];
                try {
                    elems = System.getProperty(listParams).split(",");
                } catch (Exception e) {
                    logger.debug("-> {} is not defined. Exception captured till scenario execution.", listParams);
                    elems = "error,error".split(",");
                }
                paramReplace = lines.get(s).substring((lines.get(s).lastIndexOf("(") + 1), (lines.get(s).length()) - 1).split(",")[1];
                lines.set(s, " ");
                while (!(lines.get(s).toUpperCase().contains("SCENARIO:"))) {
                    s++;
                }
                lines.set(s, lines.get(s).replaceAll("Scenario", "Scenario Outline"));
                s++;
                while (s < lines.size()) {
                    if ((lines.get(s).toUpperCase().contains("SCENARIO")) || lines.get(s).matches(".*@[^\\{].*")) {
                        break;
                    }
                    s++;
                }
                lines.add(s, "Examples:");
                exampleLines(paramReplace, elems, lines,  s + 1);
                s = s + elems.length;
            }
            if (lines.get(s).matches("\\s*@background.*")) {
                listParams = lines.get(s).substring((lines.get(s).lastIndexOf("(") + 1), (lines.get(s).length()) - 1);
                if (System.getProperty(listParams) != null) {
                    lines.remove(s);
                } else {
                    if (listParams.matches("default")) {
                        lines.remove(s);
                        while (!lines.get(s).toUpperCase().contains("SCENARIO") && !lines.get(s).matches(".*@[^\\{].*")) {
                            s++;
                        }
                    } else {
                        lines.remove(s);
                        while (!lines.get(s).toUpperCase().contains("SCENARIO") && !lines.get(s).matches(".*@[^\\{].*")) {
                            lines.remove(s);
                        }
                    }
                    s--;
                }
            }
        }
        parseLines(lines, path);
        return String.join("\n", lines);
    }

    public void exampleLines (String name, String[] params, List<String> lines, int num) {
        lines.add(num, "| " + name + " | " + name + ".id |");
        for (int i = 0; i < params.length; i++) {
            num++;
            lines.add(num, "| " + params[i] + " | " + i + " |");
        }
    }


    public String parseLines(List<String> lines, String path) throws IncludeException {
        String featureName;
        String scenarioName;
        String[] params;
        boolean marked = false;
        String nwsource = "";


        for (int lineOriginalFeature = 0; lineOriginalFeature < lines.size(); lineOriginalFeature++) {
            if (lines.get(lineOriginalFeature).matches("\\s*@include.*")) {
                String lineToinclude = "";
                for (int linesWithInclude = lineOriginalFeature; linesWithInclude < lines.size() && lines.get(linesWithInclude).contains("@include"); linesWithInclude++) {
                    featureName = getFeatureName(lines.get(linesWithInclude));
                    scenarioName = getScenName(lines.get(linesWithInclude));
                    params = getParams(lines.get(linesWithInclude));
                    lines.set(linesWithInclude, "");
                    lineToinclude += featureStepConverter(path + featureName, scenarioName, params);
                }

                //TODO: Simplify code
                for (int lineAfterInclude = lineOriginalFeature; lineAfterInclude < lines.size(); lineAfterInclude++) {
                    if (lines.get(lineAfterInclude).toUpperCase().contains("FEATURE:") && !lines.get(lineAfterInclude).toUpperCase().contains("@INCLUDE")) {
                        for (int indexForBackground = lineAfterInclude; indexForBackground < lines.size(); indexForBackground++) {
                            if (lines.get(lineAfterInclude).toUpperCase().contains("BACKGROUND:") && !marked) {
                                lines.set(lineOriginalFeature, lines.get(lineAfterInclude));
                                lines.set(lineAfterInclude, lines.get(indexForBackground));
                                lines.set(indexForBackground, lineToinclude);
                                marked = true;
                                lineToinclude = "";
                                break;
                            }
                        }
                        if (!marked) {
                            lines.set(lineOriginalFeature, lines.get(lineAfterInclude));
                            lines.set(lineAfterInclude, "\nBackground:\n" + lineToinclude);
                            lineToinclude = "";
                            break;
                        }
                    }
                    if ((lines.get(lineAfterInclude).toUpperCase().contains("SCENARIO:") || lines.get(lineAfterInclude).toUpperCase().contains("OUTLINE:")) && !lines.get(lineAfterInclude).toUpperCase().contains("@INCLUDE")) {
                        lines.set(lineOriginalFeature, lines.get(lineAfterInclude));
                        lines.set(lineAfterInclude, lineToinclude);
                        lineToinclude = "";
                        break;
                    }
                }

            }
            nwsource += lines.get(lineOriginalFeature) + "\n";
            logger.debug("New line written into source:\n" + lines.get(lineOriginalFeature));
        }
        logger.debug("Final feature source: \n" + nwsource);
        return nwsource;
    }

    /**
     * @param s A string with the tag line that will be filtered and trimmed to get exactly the name of the feature
     */
    public String getFeatureName(String s) {

        String feature = s.substring((s.lastIndexOf("feature:") + "feature:".length()));
        feature = feature.substring(0, feature.indexOf(","));

        return feature.trim();
    }

    public String getScenName(String s) {

        String scenName = s.substring((s.lastIndexOf("scenario:") + "scenario:".length()));
        if (s.contains("params")) {
            scenName = scenName.substring(0, scenName.indexOf(","));
        } else {
            scenName = scenName.substring(0, scenName.indexOf(")"));
        }

        return scenName.trim();
    }

    public String[] getParams(String s) {
        String[] vals = null;
        if (s.contains("params")) {
            String[] pairs = s.substring((s.lastIndexOf("[") + 1), (s.length()) - 2).split(",");
            vals = new String[(pairs.length) * 2];
            int index = 0;
            for (int iterator = 0; iterator < pairs.length; iterator++) {
                vals[index] = "<" + pairs[iterator].split(":")[0].trim() + ">";  //key
                index++;
                vals[index] = pairs[iterator].split(":")[1].trim();           //value
                index++;
            }
        }
        return vals;
    }

    public String doReplaceKeys(String parsedFeature, String[] params) throws IncludeException {
        for (int i = 0; i < params.length; i++) {
            parsedFeature = parsedFeature.replaceAll(params[i], params[i + 1]);
            i++;
        }
        if (parsedFeature.contains("<")) {
            throw new IncludeException("-> Error while parsing keys, check your params");
        }
        return parsedFeature;
    }

    public String featureStepConverter(String feature, String scenarioName, String[] params) throws IncludeException {
        boolean scenarioexists = false;
        BufferedReader bufferedFeature = null;
        String parsedFeature = "";
        String sCurrentLine;


        try {

            bufferedFeature = new BufferedReader(new FileReader(feature));
            while ((sCurrentLine = bufferedFeature.readLine()) != null) {
                if (sCurrentLine.contains(scenarioName)) {
                    scenarioexists = true;
                    if (sCurrentLine.toUpperCase().contains("OUTLINE") && params == null) {
                        throw new IncludeException("->  Parameters were not given for this scenario outline.");
                    } else if (sCurrentLine.toUpperCase().contains("OUTLINE")) {
                        BufferedReader auxBufferedFeature = bufferedFeature;
                        String sParamline;
                        while ((sParamline = auxBufferedFeature.readLine()) != null && !sParamline.toUpperCase().contains("SCENARIO")) {
                            if (sParamline.contains("|")) {
                                if (!checkParams(sParamline, params)) {
                                    throw new IncludeException("-> Wrong number of parameters.");
                                }
                            } else if (!sParamline.toUpperCase().contains("EXAMPLES:")) {
                                parsedFeature = parsedFeature + sParamline + "\n";
                            }
                        }
                    } else if (!sCurrentLine.toUpperCase().contains("OUTLINE") && sCurrentLine.toUpperCase().contains("SCENARIO:")) {
                        while ((sCurrentLine = bufferedFeature.readLine()) != null && !sCurrentLine.toUpperCase().contains("SCENARIO:") && !sCurrentLine.toUpperCase().contains("EXAMPLES:")) {
                            parsedFeature = parsedFeature + sCurrentLine + "\n";
                        }
                    }
                }
            }
            if (!scenarioexists) {
                throw new IncludeException("-> Scenario not present at the given feature: " + scenarioName);
            }

        } catch (FileNotFoundException e) {
            throw new IncludeException("-> Feature file were not found: " + feature);
        } catch (IOException e) {
            throw new IncludeException("-> An I/O error appeared.");
        } finally {
            try {
                if (bufferedFeature != null) {
                    bufferedFeature.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (params != null) {
            parsedFeature = doReplaceKeys(parsedFeature, params);
        }

        return parsedFeature;

    }

    public boolean checkParams(String sCurrentLine, String[] params) {
        int paramcounter = 0;
        boolean checker = false;
        if (sCurrentLine.contains("|")) {

            for (int paramLineIndex = 0; paramLineIndex < sCurrentLine.length(); paramLineIndex++) {
                if (sCurrentLine.charAt(paramLineIndex) == '|') {
                    paramcounter++;
                }
                checker = true;

            }

            if ((params != null) && (paramcounter - 1) != (params.length / 2)) {
                checker = false;
            }
        }

        return checker;
    }
}
