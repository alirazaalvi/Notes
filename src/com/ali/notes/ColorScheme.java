package com.ali.notes;

import java.util.HashMap;
import java.util.Map;

public class ColorScheme {
	
	private String colorScheme;
	public static final String defaultColorScheme = "white";
	public static HashMap<String, Map<String, String>> colorCodes = new HashMap<String, Map<String,String>>();
	
	
	public ColorScheme()
	{
		initializeColorCodes();
	}
	
	public void setColorScheme(String colorScheme)
	{
		this.colorScheme = colorScheme;
	}
	
	public String getColorScheme()
	{
		return this.colorScheme;
	}
	
	private void initializeColorCodes()
	{
		HashMap<String, String> redHash = new HashMap<String, String>();
		redHash.put("title", "#FCD6D6");
		redHash.put("description", "#FF7C7C");
		this.colorCodes.put("red", redHash);

		HashMap<String, String> orangeHash = new HashMap<String, String>();
		orangeHash.put("title", "#FFD6B7");
		orangeHash.put("description", "#FFA560");
		this.colorCodes.put("orange", orangeHash);
		
		HashMap<String, String> yellowHash = new HashMap<String, String>();
		yellowHash.put("title", "#FFF499");
		yellowHash.put("description", "#FCEB50");
		this.colorCodes.put("yellow", yellowHash);
		
		
		HashMap<String, String> purpleHash = new HashMap<String, String>();
		purpleHash.put("title", "#CBB9EA");
		purpleHash.put("description", "#9A74E0");
		this.colorCodes.put("purple", purpleHash);
		
		
		HashMap<String, String> greenHash = new HashMap<String, String>();
		greenHash.put("title", "#E9FCA4");
		greenHash.put("description", "#D6F954");
		this.colorCodes.put("green", greenHash);
		
		
		HashMap<String, String> blueHash = new HashMap<String, String>();
		blueHash.put("title", "#A8B8EA");
		blueHash.put("description", "#6786EF");
		this.colorCodes.put("blue", blueHash);

		
		HashMap<String, String> grayHash = new HashMap<String, String>();
		grayHash.put("title", "#E2E2E2");
		grayHash.put("description", "#C0BFC1");
		this.colorCodes.put("gray", grayHash);
		
		
		HashMap<String, String> whiteHash = new HashMap<String, String>();
		whiteHash.put("title", "#FFFFFF");
		whiteHash.put("description", "#F4F2F2");
		this.colorCodes.put("white", whiteHash);
	}

}
