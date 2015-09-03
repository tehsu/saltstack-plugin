package com.waytta;

import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.EnvVars;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Utils {
    //replaces $string with value of env($string). Used in conjunction with parameterized builds
    public static String paramorize(AbstractBuild build, BuildListener listener, String paramer) {
	Pattern pattern = Pattern.compile("\\{\\{\\w+\\}\\}");
	Matcher matcher = pattern.matcher(paramer);
	while (matcher.find()) {
	    //listener.getLogger().println("FOUND: "+matcher.group());
	    try {
		EnvVars envVars;
		envVars = build.getEnvironment(listener);
		//remove leading {{
		String replacementVar = matcher.group().substring(2);
		//remove trailing }} 
		replacementVar = replacementVar.substring(0, replacementVar.length()-2);
		//using proper env var name, perform a lookup and save value
		replacementVar = envVars.get(replacementVar);
		paramer = paramer.replace(matcher.group(), replacementVar);
	    } catch (IOException e1) {
		listener.getLogger().println(e1);
		return "Error: "+e1;
	    } catch (InterruptedException e1) {
		listener.getLogger().println(e1);
		return "Error: "+e1;
	    }
	}
	return paramer;
    }
}
